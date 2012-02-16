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

/*
 * 영화검색 
 * (list로 보기)
 * 
 * listview Adapter
 */
public class MovieListAdapter extends ArrayAdapter<Movie> {

	private final ImageDownloader imageDownloader = new ImageDownloader();

	ArrayList<Movie> movies = null;
	LayoutInflater li;

	// 생성자
	public MovieListAdapter(Context context, int textViewResourceId,
			ArrayList<Movie> movies) {
		super(context, textViewResourceId, movies);

		// 리스트 뷰를 띄우기 위해
		li = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.movies = movies;
	}

	// list item마다 호출
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		Log.d("MovieListAdapter", "getView호출");
		View v = convertView;

		if (v == null) {
			v = li.inflate(R.layout.searchlist, null);
		}
		
		Movie m = movies.get(position);
		try {
			ImageView imageVw = (ImageView) v.findViewById(R.id.searchList_movieImg);
			TextView title = (TextView) v.findViewById(R.id.searchList_movieTitle);
			TextView nation = (TextView) v.findViewById(R.id.searchList_movieNation);
			TextView genre = (TextView) v.findViewById(R.id.searchList_movieGenre);
			TextView rating = (TextView) v.findViewById(R.id.searchList_movieRating);

			// 썸네일 주소를 받아옴
			// 썸네일 이미지 처리
			String thumbnailUrl = m.getThumbnail();
			imageDownloader.download(thumbnailUrl, imageVw);
			// 영화 title 가져오기
			title.setText(m.getTitle());
			// 영화 제작 국가 가져오기
			nation.setText(m.getNation());
			// 영화 장르 가져오기
			genre.setText(m.getGenre());
			// 평점
			rating.setText(m.getGrade());
			
			Log.d("MovieListAdapter", "영화 제목 : " + m.getTitle() 
										+ "국가 : " + m.getNation()
										+ "평점 : " + m.getGrade() 
										+ "썸네일 주소 : " + thumbnailUrl);
			return v;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
