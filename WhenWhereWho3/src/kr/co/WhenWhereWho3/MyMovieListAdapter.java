package kr.co.WhenWhereWho3;

import java.util.ArrayList;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
/*
 * 영화list 
 * (나의목록 보기)
 * 
 * listview Adapter
 */
public class MyMovieListAdapter extends ArrayAdapter<Movie> {

	private final ImageDownloader imageDownloader = new ImageDownloader();
	
	ArrayList<Movie> movies= null;
	LayoutInflater li;

	//생성자
	public MyMovieListAdapter(Context context, int textViewResourceId,
			ArrayList<Movie> movies) {
		super(context, textViewResourceId, movies);
		
		li = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.movies = movies;
	}
	
	// list item마다 호출
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		Log.d( "MovieAdapter", "getView호출" );
		View v = convertView;
		if (v == null) { 
			v = li.inflate(R.layout.mylist, null);
		}
		
		Movie m = movies.get(position);
		try {
			ImageView imageVw = (ImageView)v.findViewById(R.id.myMovieList_movieImg);
			TextView title = (TextView)v.findViewById(R.id.myMovieList_movieTitle);
			TextView when = (TextView)v.findViewById(R.id.myMovieList_whenTxtVw);
			TextView with = (TextView)v.findViewById(R.id.myMovieList_withTxtVw);
			RatingBar ratingBar = (RatingBar)v.findViewById(R.id.myMovieList_ratingBar);
			
			//	영화 title 가져오기
			title.setText(m.getTitle());
			//	영화를 언제 봤는가? 가져오기
			String getWhen = m.getWhen();
			String year = getWhen.substring(0, 4);
			String month = getWhen.substring(4, 5);
			String day = getWhen.substring(5, 7); 
			when.setText(year + "년 " + month + "월 " + day + "일");
			//	영화를 누구랑 봤는가? 가져오기
			//	일단 임시로 장르 가져옴- 2012.02.12
			with.setText(m.getWith());
			//	평점 바 가져오기
			float rating = (float) ( ( m.getGrade().equals("") ) ? 0.0 : Float.parseFloat( m.getGrade() ) )  / ( float )2.0; 
			ratingBar.setRating( rating );
			//	썸네일 이미지 처리
			//	썸네일 주소를 받아옴
			String thumbnailUrl = m.getThumbnail();
			imageDownloader.download(thumbnailUrl, imageVw);

			Log.d( "MovieAdapter", "영화 제목 : " + m.getTitle() +
									"개봉일 : " + m.getOpenInfo() +
									"장르 : " + m.getGenre() +
									"평점 : " + m.getGrade() +
									"썸네일 주소 : " + thumbnailUrl);
			return v;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}