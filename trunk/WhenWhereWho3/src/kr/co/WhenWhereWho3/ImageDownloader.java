/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package kr.co.WhenWhereWho3;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This helper class download images from the Internet and binds those with the provided ImageView.
 *
 * <p>It requires the INTERNET permission, which should be added to your application's manifest
 * file.</p>
 *
 * A local cache of downloaded images is maintained internally to improve performance.
 */
public class ImageDownloader {
    private static final String LOG_TAG = "ImageDownloader";

    public enum Mode { NO_ASYNC_TASK, NO_DOWNLOADED_DRAWABLE, CORRECT }
    private Mode mode = Mode.NO_ASYNC_TASK;
    
    /**
     * Download the specified image from the Internet and binds it to the provided ImageView. The
     * binding is immediate if the image is found in the cache and will be done asynchronously
     * otherwise. A null bitmap will be associated to the ImageView if an error occurs.
     *
     * @param url The URL of the image to download.
     * @param imageView The ImageView to bind the downloaded image to.
     */
    public void download(String url, ImageView imageView) {
    	//	핸들러를 초기화 함
        resetPurgeTimer();
        //	캐시로부터 이미지를 가져온다.
        Bitmap bitmap = getBitmapFromCache(url);

        //	캐시에 저장된 이미지가 없으면
        if (bitmap == null) {
        	//	인터넷에서 다운로드를 하고
            forceDownload(url, imageView);
        } else {
        	//	이 imageView에 다운로드를 진행하고 있을지도 모르는 잠재적 작업을 중단시킨다.
            cancelPotentialDownload(url, imageView);
            //	그리고 난 후에 비트맵을 이미지 뷰에 띄운다.
            imageView.setImageBitmap(bitmap);
        }
    }

    /*
     * Same as download but the image is always downloaded and the cache is not used.
     * Kept private at the moment as its interest is not clear.
       private void forceDownload(String url, ImageView view) {
          forceDownload(url, view, null);
       }
     */

    /**
     * Same as download but the image is always downloaded and the cache is not used.
     * Kept private at the moment as its interest is not clear.
     */
    private void forceDownload(String url, ImageView imageView) {
        // State sanity: url is guaranteed to never be null in DownloadedDrawable and cache keys.
    	//	주소가 없으면 이미지를 set시키지 않음( forground 이미지를 null로 함 )
        if (url == null) {
            imageView.setImageDrawable(null);
            return;
        }

        //	이미지가 다운로드 중인지 체크해서 다운로드 중이 아니면
        if (cancelPotentialDownload(url, imageView)) {
        	//	이미지뷰에 대한 AsyncTack를 하나 생성하고
            BitmapDownloaderTask task = new BitmapDownloaderTask(imageView);
            //	이미지가 다운로드 되는동안 대신 자리잡아줄 Drawble클래스를 생성한다.
            //	이때 생성자에 이미 생성한 task를 넘겨준다
            DownloadedDrawable downloadedDrawable = new DownloadedDrawable(task);
            //	이미지 뷰에 다운로드 하는동안 임시의 사진(여기서는 검은 배경의 이미지)을 이미지 뷰에 뿌려준다.
            imageView.setImageDrawable(downloadedDrawable);
            //	이미지 뷰 높이 설정
            imageView.setMinimumHeight(60);
            //	task를 수행시킨다.
            task.execute(url);
        }
    }

    /**
     * Returns true if the current download has been canceled or if there was no download in
     * progress on this image view.
     * 이 이미지뷰에서 진행중인 다운로가 없거나 현재의 다운로드가 취소되었을 경우 true를 반환한다.
     * Returns false if the download in progress deals with the same url. The download is not
     * stopped in that case.
     * 동일한 URL로 다운로드가 진행되는 경우 FALSE를 반환합니다. 다운로드는이 경우에 중지되지 않는다.
     */
    private static boolean cancelPotentialDownload(String url, ImageView imageView) {
    	//	AsyncTack 생성
        BitmapDownloaderTask bitmapDownloaderTask = getBitmapDownloaderTask(imageView);

        if (bitmapDownloaderTask != null) {
            String bitmapUrl = bitmapDownloaderTask.url;
            if ((bitmapUrl == null) || (!bitmapUrl.equals(url))) {
                bitmapDownloaderTask.cancel(true);
            } else {
                // The same URL is already being downloaded.
                return false;
            }
        }
        return true;
    }

    /**
     * @param imageView Any imageView
     * @return Retrieve the currently active download task (if any) associated with this imageView.
     * null if there is no such task.
     */
    private static BitmapDownloaderTask getBitmapDownloaderTask(ImageView imageView) {
        if (imageView != null) {
            Drawable drawable = imageView.getDrawable();
            if (drawable instanceof DownloadedDrawable) {
                DownloadedDrawable downloadedDrawable = (DownloadedDrawable)drawable;
                return downloadedDrawable.getBitmapDownloaderTask();
            }
        }
        return null;
    }

    // 웹 주소에 있는 이미지 주소로 Bitmap화 시킨다.
    Bitmap downloadBitmap(String url) {
        final int IO_BUFFER_SIZE = 4 * 1024;
        
        // AndroidHttpClient is not allowed to be used from the main thread
        //	모드 체크를 원래 했는데 여기서는 사용하지 않을 것이므로 제거한다.
        //	final HttpClient client = (mode == Mode.NO_ASYNC_TASK) ? new DefaultHttpClient() :
        final HttpClient client = AndroidHttpClient.newInstance("Android");
        final HttpGet getRequest = new HttpGet(url);

        try {
        	//	HttpResponse를 수행했으면 HttpEntity는 반드시 수행해야함!! 
        	//	그렇지 않으면 다음 HTTP Request시에 Hang이 걸리는 문제에 맞닥뜨릴 수 있다.
            HttpResponse response = client.execute(getRequest);
            final int statusCode = response.getStatusLine().getStatusCode();
            
            //	상태를 체크해서 OK가 아니면 에러 메시지 출력하고 함수 종료
            if (statusCode != HttpStatus.SC_OK) {
                Log.w("ImageDownloader", "Error " + statusCode +
                        " while retrieving bitmap from " + url);
                return null;
            }

            
            final HttpEntity entity = response.getEntity();
            //	응답이 왔으면 InputStream생성하고 Bitmap 생성한다.
            if (entity != null) {
                InputStream inputStream = null;
                try {
                    inputStream = entity.getContent();
                    //	비트맵 생성해서 리턴
                    return BitmapFactory.decodeStream(new FlushedInputStream(inputStream));
                } finally {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    entity.consumeContent();
                }
            }
        } catch (IOException e) {
            getRequest.abort();
            Log.w(LOG_TAG, "I/O error while retrieving bitmap from " + url, e);
        } catch (IllegalStateException e) {
            getRequest.abort();
            Log.w(LOG_TAG, "Incorrect URL: " + url);
        } catch (Exception e) {
            getRequest.abort();
            Log.w(LOG_TAG, "Error while retrieving bitmap from " + url, e);
        } finally {
            if ((client instanceof AndroidHttpClient)) {
                ((AndroidHttpClient) client).close();
            }
        }
        return null;
    }
    
    /**
     * A patched InputSteam that tries harder to fully read the input stream.
     */
    static class FlushedInputStream extends FilterInputStream {
        public FlushedInputStream(InputStream inputStream) {
            super(inputStream);
        }

        @Override
        public long skip(long n) throws IOException {
            long totalBytesSkipped = 0L;
            while (totalBytesSkipped < n) {
                long bytesSkipped = in.skip(n-totalBytesSkipped);
                if (bytesSkipped == 0L) break;
                totalBytesSkipped += bytesSkipped;
            }
            return totalBytesSkipped;
        }
    }

    /**
     * The actual AsyncTask that will asynchronously download the image.
     */
    class BitmapDownloaderTask extends AsyncTask<String, Void, Bitmap> {
        private String url;
        //	약한 레퍼런스이다.
        //	메모리관리를 위해 고안된것으로 레퍼런스를 WeakReference로 하면 
        //	gc(가비지 콜렉터)에 해당 레퍼런스가 관여되지 않는다.( 캐싱 이미지 사용하기 위함 )
        private final WeakReference<ImageView> imageViewReference;
        
        public BitmapDownloaderTask(ImageView imageView) {
        	//	이미지 뷰 저장
            imageViewReference = new WeakReference<ImageView>(imageView);
        }

        /**
         * Actual download method.
         */
        @Override
        protected Bitmap doInBackground(String... params) {
            url = params[0];
            return downloadBitmap(url);
        }

        /**
         * Once the image is downloaded, associates it to the imageView
         */
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (isCancelled()) {
                bitmap = null;
            }

            addBitmapToCache(url, bitmap);

            if (imageViewReference != null) {
                ImageView imageView = imageViewReference.get();
                BitmapDownloaderTask bitmapDownloaderTask = getBitmapDownloaderTask(imageView);
                // Change bitmap only if this process is still associated with it
                // Or if we don't use any bitmap to task association (NO_DOWNLOADED_DRAWABLE mode)
                if ( this == bitmapDownloaderTask ) {
                    imageView.setImageBitmap(bitmap);
                }
            }
        }
    }


    /**
     * A fake Drawable that will be attached to the imageView while the download is in progress.
     * 다운로드가 진행되는 동안에 이미지 뷰에 대신 자리잡고 있을 Drawable
     * 
     * Contains a reference to the actual download task, so that a download task can be stopped
     * if a new binding is required, and makes sure that only the last started download process can
     * bind its result, independently of the download finish order.
     * 
     * 구현 클래스 DownloadedDrawable는 ColorDrawable의 지원을 받아, 다운로딩이 진행되는 동안 ImageView가
     * 검정색 백드라운드를 디스플레이하게 될 것이다. 이것 대신 “다운로드 진행 중”임을 알리는 이미지를 사용하면
     * 사용자에게 작업 상황을 피드백할 수 있을 것이다. 여기서도 객체의 의존성을 줄이기 위해 WeakReference를
     * 사용하고 있음을 주목하라.
     */
    static class DownloadedDrawable extends ColorDrawable {
        private final WeakReference<BitmapDownloaderTask> bitmapDownloaderTaskReference;

        public DownloadedDrawable(BitmapDownloaderTask bitmapDownloaderTask) {
            super(Color.BLACK);
            bitmapDownloaderTaskReference =
                new WeakReference<BitmapDownloaderTask>(bitmapDownloaderTask);
        }

        public BitmapDownloaderTask getBitmapDownloaderTask() {
            return bitmapDownloaderTaskReference.get();
        }
    }

    public void setMode(Mode mode) {
        this.mode = mode;
        clearCache();
    }

    
    /*
     * Cache-related fields and methods.
     * 
     * We use a hard and a soft cache. A soft reference cache is too aggressively cleared by the
     * Garbage Collector.
     */
    
    private static final int HARD_CACHE_CAPACITY = 50;
    private static final int DELAY_BEFORE_PURGE = 10 * 1000; // in milliseconds

    // Hard cache, with a fixed maximum capacity and a life duration
    @SuppressWarnings("serial")
	private final HashMap<String, Bitmap> sHardBitmapCache =
        new LinkedHashMap<String, Bitmap>(HARD_CACHE_CAPACITY / 2, 0.75f, true) {
        @Override
        protected boolean removeEldestEntry(LinkedHashMap.Entry<String, Bitmap> eldest) {
            if (size() > HARD_CACHE_CAPACITY) {
                // Entries push-out of hard reference cache are transferred to soft reference cache
                sSoftBitmapCache.put(eldest.getKey(), new SoftReference<Bitmap>(eldest.getValue()));
                return true;
            } else
                return false;
        }
    };

    // Soft cache for bitmaps kicked out of hard cache
    private final static ConcurrentHashMap<String, SoftReference<Bitmap>> sSoftBitmapCache =
        new ConcurrentHashMap<String, SoftReference<Bitmap>>(HARD_CACHE_CAPACITY / 2);

    private final Handler purgeHandler = new Handler();

    private final Runnable purger = new Runnable() {
        public void run() {
            clearCache();
        }
    };

    /**
     * Adds this bitmap to the cache.
     * @param bitmap The newly downloaded bitmap.
     */
    private void addBitmapToCache(String url, Bitmap bitmap) {
    	//	비트맵을 캐시에 저장
        if (bitmap != null) {
            synchronized (sHardBitmapCache) {
                sHardBitmapCache.put(url, bitmap);
            }
        }
    }

    /**
     * @param url The URL of the image that will be retrieved from the cache.
     * @return The cached bitmap or null if it was not found.
     */
    private Bitmap getBitmapFromCache(String url) {
        // First try the hard reference cache
    	//	동기화 시킴( 데드락 발생 방지 )
        synchronized (sHardBitmapCache) {
        	//	캐시로부터 다운로드 된 이미지를 찾고
            final Bitmap bitmap = sHardBitmapCache.get(url);
            //	그 이미지가 존재한다면
            if (bitmap != null) {
                // Bitmap found in hard cache
                // Move element to first position, so that it is removed last
            	//	지우고
                sHardBitmapCache.remove(url);
                //	맨 뒤로 다시 집어 넣는다. 왜?? 
                //	그래야지 다운로드 된 순서를 알 수 있으니까
                sHardBitmapCache.put(url, bitmap);
                return bitmap;
            }
        }

        // Then try the soft reference cache
        SoftReference<Bitmap> bitmapReference = sSoftBitmapCache.get(url);
        if (bitmapReference != null) {
            final Bitmap bitmap = bitmapReference.get();
            if (bitmap != null) {
                // Bitmap found in soft cache
                return bitmap;
            } else {
                // Soft reference has been Garbage Collected
                sSoftBitmapCache.remove(url);
            }
        }

        return null;
    }
 
    /**
     * Clears the image cache used internally to improve performance. Note that for memory
     * efficiency reasons, the cache will automatically be cleared after a certain inactivity delay.
     */
    public void clearCache() {
        sHardBitmapCache.clear();
        sSoftBitmapCache.clear();
    }

    /**
     * Allow a new delay before the automatic cache clear is done.
     */
    private void resetPurgeTimer() {
        purgeHandler.removeCallbacks(purger);
        purgeHandler.postDelayed(purger, DELAY_BEFORE_PURGE);
    }
}

