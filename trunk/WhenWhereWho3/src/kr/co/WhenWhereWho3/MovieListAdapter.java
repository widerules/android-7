package kr.co.WhenWhereWho3;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MovieListAdapter extends ArrayAdapter<Movie> {
	ArrayList<Movie> movies= null;
	LayoutInflater li;
	private final ImageDownloader imageDownloader = new ImageDownloader();

	public MovieListAdapter(Context context, int textViewResourceId,
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
			v = li.inflate(R.layout.searchlist, null);
		}
		Movie m = movies.get(position);
		try {
			ImageView imageVw = (ImageView)v.findViewById(R.id.movieImg);
			TextView title = (TextView)v.findViewById(R.id.movieTitle);
			TextView nation = (TextView)v.findViewById(R.id.movieNation);
			TextView rating = (TextView)v.findViewById(R.id.movieRating);
			
			/********************************************************/
			//	영화 title 가져오기
			title.setText(m.getTitle());
			Log.d( "MovieAdapter", "영화 제목 : " + m.getTitle() );
			/********************************************************/
			
			/********************************************************/
			//	영화 제작 국가 가져오기
			nation.setText(m.getNation());
			Log.d( "MovieAdapter", "국가 : " + m.getNation());
			/********************************************************/
			
			/********************************************************/
			// 평점
			rating.setText(m.getGrade());
			Log.d( "MovieAdapter", "평점 : " + m.getGrade());
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
