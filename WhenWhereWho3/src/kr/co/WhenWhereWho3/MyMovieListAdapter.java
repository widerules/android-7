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
 * ��ȭlist 
 * (���Ǹ�� ����)
 * 
 * listview Adapter
 */
public class MyMovieListAdapter extends ArrayAdapter<Movie> {

	private final ImageDownloader imageDownloader = new ImageDownloader();
	
	ArrayList<Movie> movies= null;
	LayoutInflater li;

	//������
	public MyMovieListAdapter(Context context, int textViewResourceId,
			ArrayList<Movie> movies) {
		super(context, textViewResourceId, movies);
		
		li = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.movies = movies;
	}
	
	// list item���� ȣ��
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		Log.d( "MovieAdapter", "getViewȣ��" );
		View v = convertView;
		if (v == null) { 
			v = li.inflate(R.layout.mylist, null);
		}
		
		Movie m = movies.get(position);
		try {
			ImageView imageVw = (ImageView)v.findViewById(R.id.movieImg);
			TextView title = (TextView)v.findViewById(R.id.movieTitle);
			TextView when = (TextView)v.findViewById(R.id.whenTxtVw);
			TextView with = (TextView)v.findViewById(R.id.withTxtVw);
			RatingBar ratingBar = (RatingBar)v.findViewById(R.id.ratingBar);
			
			//	��ȭ title ��������
			title.setText(m.getTitle());
			//	��ȭ�� ���� �ô°�? ��������
			when.setText("When " + m.getWhen());
			//	��ȭ�� ������ �ô°�? ��������
			//	�ϴ� �ӽ÷� �帣 ������- 2012.02.12
			with.setText("With " + m.getWith());
			//	���� �� ��������
			float rating = (float) ( ( m.getGrade().equals("") ) ? 0.0 : Float.parseFloat( m.getGrade() ) )  / ( float )2.0; 
			ratingBar.setRating( rating );
			//	����� �̹��� ó��
			//	����� �ּҸ� �޾ƿ�
			String thumbnailUrl = m.getThumbnail();
			imageDownloader.download(thumbnailUrl, imageVw);

			Log.d( "MovieAdapter", "��ȭ ���� : " + m.getTitle() +
									"������ : " + m.getOpenInfo() +
									"�帣 : " + m.getGenre() +
									"���� : " + m.getGrade() +
									"����� �ּ� : " + thumbnailUrl);
			return v;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}