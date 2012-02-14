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

public class MovieGridAdapter extends ArrayAdapter<Movie> {
	ArrayList<Movie> movies= null;
	LayoutInflater li;
	private final ImageDownloader imageDownloader = new ImageDownloader();

	public MovieGridAdapter(Context context, int textViewResourceId,
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
		View v = convertView;
		if (v == null) { 
			v = li.inflate(R.layout.searchgrid, null);
		}
		Movie m = movies.get(position);
		try {
			ImageView imageVw = (ImageView)v.findViewById(R.id.movieImg);
			TextView title = (TextView)v.findViewById(R.id.movieTitle);

			/********************************************************/
			//	영화 title 가져오기
			title.setText(m.getTitle());
			Log.d( "MovieAdapter", "영화 제목 : " + m.getTitle() );
			/********************************************************/
			
			/********************************************************/
			//	썸네일 이미지 처리
			//	썸네일 주소를 받아옴
			String thumbnailUrl = m.getThumbnail();
			Log.d( "MovieAdapter", "썸네일 주소 : " + thumbnailUrl );
			imageDownloader.download(thumbnailUrl, imageVw);
			/********************************************************/
			return v;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
