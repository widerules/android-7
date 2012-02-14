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

public class MyMovieListAdapter extends ArrayAdapter<Movie> {
	ArrayList<Movie> movies= null;
	LayoutInflater li;
	private final ImageDownloader imageDownloader = new ImageDownloader();

	public MyMovieListAdapter(Context context, int textViewResourceId,
			ArrayList<Movie> movies) {
		super(context, textViewResourceId, movies);
		
		this.movies = movies;
		Log.d( "MovieAdapter", "������ ȣ��, ����Ʈ ��ü ������ : " + movies.size()  );
		//	����Ʈ �並 ���� ���� ���÷����� ��ȯ!! 
		li = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		Log.d( "MovieAdapter", "getViewȣ��" );
		View v = convertView;
		if (v == null) { 
			v = li.inflate(R.layout.mylist, null);
		}
		Movie m = movies.get(position);
		try {
			ImageView imageVw = (ImageView)v.findViewById(R.id.imageVw);
			TextView title = (TextView)v.findViewById(R.id.titleTxtVw);
			TextView when = (TextView)v.findViewById(R.id.whenTxtVw);
			TextView with = (TextView)v.findViewById(R.id.withTxtVw);
			RatingBar ratingBar = (RatingBar)v.findViewById(R.id.ratingBar);
			
			/********************************************************/
			//	��ȭ title ��������
			title.setText(m.getTitle());
			Log.d( "MovieAdapter", "��ȭ ���� : " + m.getTitle() );
			/********************************************************/
			
			/********************************************************/
			//	��ȭ�� ���� �ô°�? ��������
			when.setText(m.getOpenInfo());
			Log.d( "MovieAdapter", "������ : " + m.getOpenInfo() );
			/********************************************************/
			
			/********************************************************/
			//	��ȭ�� ������ �ô°�? ��������
			//	�ϴ� �ӽ÷� �帣 ������- 2012.02.12
			with.setText(m.getGenre());
			Log.d( "MovieAdapter", "�帣 : " + m.getGenre() );
			/********************************************************/
			
			/********************************************************/
			//	���� �� ��������
			float rating = (float) ( ( m.getGrade().equals("") ) ? 0.0 : Float.parseFloat( m.getGrade() ) )  / ( float )2.0; 
			ratingBar.setRating( rating );
			Log.d( "MovieAdapter", "���� : " + m.getGrade() );
			/********************************************************/
			
			/********************************************************/
			//	����� �̹��� ó��
			//	����� �ּҸ� �޾ƿ�
			String thumbnailUrl = m.getThumbnail();
			Log.d( "MovieAdapter", "����� �ּ� : " + thumbnailUrl );
			imageDownloader.download(thumbnailUrl, imageVw);
			/********************************************************/
			
			return v;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
