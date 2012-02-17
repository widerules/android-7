package kr.co.WhenWhereWho3;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

public class SetupHelpActivity extends Activity {

	
		Integer [] images = {
				R.drawable.main_setup, R.drawable.main_backbutton, R.drawable.main_movielist, 
				R.drawable.main_setup, R.drawable.main_setup_searchview, R.drawable.main_setup_searchview_list, 
				R.drawable.main_setup_searchview_grid, R.drawable.search_detail, R.drawable.search_garellry, 
				R.drawable.search_regist_button, R.drawable.search_wishlist_button, R.drawable.serarchview_grid,
				R.drawable.serarchview_list
			};
			
		    @Override
		    public void onCreate(Bundle savedInstanceState) {
		        super.onCreate(savedInstanceState);
		        setContentView(R.layout.setuphelp);
		        
		        Gallery gallery = (Gallery)findViewById(R.id.setupHelpGallery);
		        UserGalleryAdapter adapter = new UserGalleryAdapter(this);
		        gallery.setAdapter(adapter);
		        
		    }
		    
		    public class UserGalleryAdapter extends BaseAdapter {
		    	private Context context;
		    	private int galleryItemBackground;
		    	LayoutInflater li;
		    	
		    	public UserGalleryAdapter(Context context) {
		    		this.context = context;
		    		
		    		li = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		    		
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
					View view = null;
					// 재사용의 의미	
					if(convertView != null) {
						view = (ImageView)convertView;
					}
					else {
						view = li.inflate(R.layout.setupimg, null);
					}
					
					ImageView setupImg = (ImageView)view.findViewById(R.id.setupImg);
					setupImg.setBackgroundResource(galleryItemBackground);
					setupImg.setScaleType(ImageView.ScaleType.FIT_CENTER);
					setupImg.setImageResource(images[position]);
					
					return view;
				}
		    }

}
