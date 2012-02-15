package kr.co.WhenWhereWho3;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MovieGalleryAdapter extends ArrayAdapter<Movie> {
	ArrayList<Movie> movies= null;
	LayoutInflater li;
	
	private final ImageDownloader imageDownloader = new ImageDownloader();
	
	public MovieGalleryAdapter(Context context, int textViewResourceId,
			ArrayList<Movie> movies) {
		super(context, textViewResourceId, movies);
		
		this.movies = movies;
		Log.d( "MovieAdapter", "생성자 호출, 리스트 객체 사이즈 : " + movies.size()  );
		//	리스트 뷰를 띄우기 위한 인플레이터 소환!! 
		li = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		Log.d( "MovieAdapter", "getView호출" );
		Movie m = movies.get(position);
		
		try {
			//	영화 포스터
			ImageView imgVw = new ImageView( getContext() ); 
			
			/********************************************************/
			//	썸네일 이미지 처리
			//	썸네일 주소를 받아옴
			String thumbnailUrl = m.getThumbnail();
			if( !thumbnailUrl.equals("") ) {
				Log.d( "MovieAdapter", "썸네일 주소 : " + thumbnailUrl );
				
				imageDownloader.download(thumbnailUrl, imgVw);
				imgVw.setLayoutParams( 
						new CoverFlow.LayoutParams( 
								LayoutParams.FILL_PARENT, 
								LayoutParams.WRAP_CONTENT ) );
				imgVw.setScaleType(ImageView.ScaleType.FIT_CENTER);
				
			} else {
				//	이미지 주소가 없을경우는 어떻게 처리할 것인가??
				//	이미지는 다른걸 담아서 리턴해주자
				imgVw.setLayoutParams( 
						new CoverFlow.LayoutParams( 
								LayoutParams.FILL_PARENT, 
								LayoutParams.WRAP_CONTENT ) );
				imgVw.setImageResource(R.drawable.kara_1);
			}
			/********************************************************/
			
			return imgVw;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
