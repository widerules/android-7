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
 * ��ȭ�˻� 
 * (GridView�� ����)
 * 
 * gridview Adapter
 */
public class MovieGridAdapter extends ArrayAdapter<Movie> {
	
	private final ImageDownloader imageDownloader = new ImageDownloader();
	
	ArrayList<Movie> movies= null;
	LayoutInflater li;

	//������
	public MovieGridAdapter(Context context, int textViewResourceId,
			ArrayList<Movie> movies) {
		super(context, textViewResourceId, movies);
		
		//	gridview ���� ����
		li = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.movies = movies;
	}
	
	// list item���� ȣ��
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		Log.d( "MovieAdapter", "getViewȣ��" );
		
		View v = convertView;
		if (v == null) { 
			v = li.inflate(R.layout.searchgrid, null);
		}
		
		Movie m = movies.get(position);
		try {
			ImageView imageVw = (ImageView)v.findViewById(R.id.searchGrid_movieImg);
			TextView title = (TextView)v.findViewById(R.id.searchGrid_movieTitle);

			//	����� �̹��� ó��
			//	����� �ּҸ� �޾ƿ�
			String thumbnailUrl = m.getThumbnail();
			imageDownloader.download(thumbnailUrl, imageVw);
			//	��ȭ title ��������
			title.setText(m.getTitle());
			
			Log.d( "MovieAdapter", "����� �ּ� : " + thumbnailUrl +
									"��ȭ ���� : " + m.getTitle() );

			return v;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
