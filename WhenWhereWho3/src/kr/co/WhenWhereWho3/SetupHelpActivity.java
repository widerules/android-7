package kr.co.WhenWhereWho3;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.Toast;

public class SetupHelpActivity extends Activity {

	
		Integer [] images = {
			};
			String [] name = {
				"니콜", "박규리", "구하라", "강지영", "한승연"	
			};
			
//		    @Override
//		    public void onCreate(Bundle savedInstanceState) {
//		        super.onCreate(savedInstanceState);
//		        setContentView(R.layout.main);
//		        
//		        Gallery gallery = (Gallery)findViewById(R.id.gallery1);
//		        UserGalleryAdapter adapter = new UserGalleryAdapter(this);
//		        gallery.setAdapter(adapter);
//		        
//		        gallery.setOnItemClickListener(new OnItemClickListener() {
//		            public void onItemClick(AdapterView parent, View v, int position, long id) {
//		                Toast.makeText(BasicGalleryViewActivity.this, "선택된 이미지 : " + name[position], Toast.LENGTH_SHORT).show();
//		                
//		                ImageView oriImgVw = (ImageView)findViewById(R.id.oriImgVw);
//		                oriImgVw.setImageResource(images[position]);
//		            }
//		        });
//		    }
//		    
//		    public class UserGalleryAdapter extends BaseAdapter {
//		    	private Context context;
//		    	private int galleryItemBackground;
//		    	
//		    	public UserGalleryAdapter(Context context) {
//		    		this.context = context;
//		    		
//		            TypedArray a = obtainStyledAttributes(R.styleable.BasicGallery);
//		            // 
//		            galleryItemBackground = a.getResourceId(
//		            		R.styleable.BasicGallery_android_galleryItemBackground, 0);
//		            
//		            // 백그라운드 배경을 얻기위해 얻어온 자원을 해제
//		            a.recycle();
//		    	}
//				public int getCount() {
//					return images.length;
//				}
//
//				public Object getItem(int position) {
//					return images[position];
//				}
//
//				public long getItemId(int position) {
//					return position;
//				}
//
//				public View getView(int position, View convertView, ViewGroup parent) {
//					ImageView view = null;
//					// 재사용의 의미	
//					if(convertView != null) {
//						view = (ImageView)convertView;
//					}
//					else {
//						view = new ImageView(context);
//					}
//					view.setLayoutParams(new Gallery.LayoutParams(120, 100));
//					
//					// 이미지뷰에 백그라운드 배경을 설정한다.
//					view.setBackgroundResource(galleryItemBackground);
//					view.setScaleType(ImageView.ScaleType.FIT_CENTER);
//					view.setImageResource(images[position]);
//					
//					return view;
//				}
//		    }

}
