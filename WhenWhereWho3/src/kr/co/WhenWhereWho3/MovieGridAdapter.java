package kr.co.WhenWhereWho3;

import java.util.ArrayList;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
/*
 * 영화검색 
 * (GridView로 보기)
 * 
 * gridview Adapter
 */
public class MovieGridAdapter extends ArrayAdapter<Movie> {
	
	private final ImageDownloader imageDownloader = new ImageDownloader();
	
	ArrayList<Movie> movies= null;
	LayoutInflater li;

	//생성자
	public MovieGridAdapter(Context context, int textViewResourceId,
			ArrayList<Movie> movies) {
		super(context, textViewResourceId, movies);
		
		//	gridview 띄우기 위해
		li = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.movies = movies;
	}
	
	// list item마다 호출
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		Log.d( "MovieAdapter", "getView호출" );
		
		View v = convertView;
		if (v == null) { 
			v = li.inflate(R.layout.searchgrid, null);
		}
		
		Movie m = movies.get(position);
		try {
			ImageView imageVw = (ImageView)v.findViewById(R.id.searchGrid_movieImg);
			TextView title = (TextView)v.findViewById(R.id.searchGrid_movieTitle);

			//	썸네일 이미지 처리
			//	썸네일 주소를 받아옴
			String thumbnailUrl = m.getThumbnail();
			imageDownloader.download(thumbnailUrl, imageVw);
			//	영화 title 가져오기
			title.setText(m.getTitle());
			
			Log.d( "MovieAdapter", "썸네일 주소 : " + thumbnailUrl +
									"영화 제목 : " + m.getTitle() );

			return v;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
