package kr.co.WhenWhereWho3;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;

public class RecommendMovieActivity extends Activity {	
	private TextView imgTxt;
	private ImageView oriImgVw;
	
	private InputStreamReader isr;
	private BufferedReader br;
	private URL url;
	
	private Parse parse;
	private ImageDownloader imageDownloader = new ImageDownloader();
	
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch( msg.what ) {
			case 0:
				
			}
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recommendmovie);

		Gallery gallery = (Gallery)findViewById(R.id.gallery);
		Thread t = new Thread(){
			@Override
			public void run() {
				
				
			}
		};
		
		
		
		
		UserGalleryAdapter adapter = new UserGalleryAdapter(this);
		gallery.setAdapter(adapter);
		
		parse = new Parse();
		
		
		//	갤러리의 사진을 클릭할 경우 이벤트
		gallery.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView parent, View v, int position, long id) {
				imgTxt = (TextView)findViewById(R.id.imgTxt);
				imgTxt.setText(name[position]);

				oriImgVw = (ImageView)findViewById(R.id.oriImgVw);
				oriImgVw.setImageResource(images[position]);
			}
		});
	}

	public class UserGalleryAdapter extends BaseAdapter {
		private Context context;
		private int galleryItemBackground;

		public UserGalleryAdapter(Context context) {
			this.context = context;

			TypedArray a = obtainStyledAttributes(R.styleable.BasicGallery);
			// 
			galleryItemBackground = a.getResourceId(
					R.styleable.BasicGallery_android_galleryItemBackground, 0);

			// 백그라운드 배경을 얻기위해 얻어온 자원을 해제
			a.recycle();
		}
		public int getCount() {
			return images.length;
		}

		public Object getItem(int position) {
			return images[position];
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ImageView view = null;
			// 재사용의 의미	
			if(convertView != null) {
				view = (ImageView)convertView;
			}
			else {
				view = new ImageView(context);
			}
			view.setLayoutParams(new Gallery.LayoutParams(120, 100));

			// 이미지뷰에 백그라운드 배경을 설정한다.
			view.setBackgroundResource(galleryItemBackground);
			view.setScaleType(ImageView.ScaleType.FIT_CENTER);
			//	이미지 세팅 이미지 다운로더를 쓰자
			view.setImageResource(images[position]);

			return view;
		}
	}
}
