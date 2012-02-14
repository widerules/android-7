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
		Log.d( "MovieAdapter", "������ ȣ��, ����Ʈ ��ü ������ : " + movies.size()  );
		//	����Ʈ �並 ���� ���� ���÷����� ��ȯ!! 
		li = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		Log.d( "MovieAdapter", "getViewȣ��" );
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
			//	��ȭ title ��������
			title.setText(m.getTitle());
			Log.d( "MovieAdapter", "��ȭ ���� : " + m.getTitle() );
			/********************************************************/
			
			/********************************************************/
			//	��ȭ ���� ���� ��������
			nation.setText(m.getNation());
			Log.d( "MovieAdapter", "���� : " + m.getNation());
			/********************************************************/
			
			/********************************************************/
			// ����
			rating.setText(m.getGrade());
			Log.d( "MovieAdapter", "���� : " + m.getGrade());
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
