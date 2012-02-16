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
 * ��ȭ�˻� 
 * (list�� ����)
 * 
 * listview Adapter
 */
public class MovieListAdapter extends ArrayAdapter<Movie> {

	private final ImageDownloader imageDownloader = new ImageDownloader();

	ArrayList<Movie> movies = null;
	LayoutInflater li;

	// ������
	public MovieListAdapter(Context context, int textViewResourceId,
			ArrayList<Movie> movies) {
		super(context, textViewResourceId, movies);

		// ����Ʈ �並 ���� ����
		li = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.movies = movies;
	}

	// list item���� ȣ��
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		Log.d("MovieListAdapter", "getViewȣ��");
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

			// ����� �ּҸ� �޾ƿ�
			// ����� �̹��� ó��
			String thumbnailUrl = m.getThumbnail();
			imageDownloader.download(thumbnailUrl, imageVw);
			// ��ȭ title ��������
			title.setText(m.getTitle());
			// ��ȭ ���� ���� ��������
			nation.setText(m.getNation());
			// ��ȭ �帣 ��������
			genre.setText(m.getGenre());
			// ����
			rating.setText(m.getGrade());
			
			Log.d("MovieListAdapter", "��ȭ ���� : " + m.getTitle() 
										+ "���� : " + m.getNation()
										+ "���� : " + m.getGrade() 
										+ "����� �ּ� : " + thumbnailUrl);
			return v;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
