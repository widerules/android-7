package kr.co.WhenWhereWho3;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MovieGalleryAdapter extends ArrayAdapter<Movie> {
	ArrayList<Movie> movies= null;
	LayoutInflater li;
	private final ImageDownloader imageDownloader = new ImageDownloader();

	public MovieGalleryAdapter(Context context, int textViewResourceId,
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
			v = li.inflate(R.layout.recommendgallery, null);
		}
		
		Movie m = movies.get(position);
		
		try {
			//	��ȭ ������
			ImageView oriImgVw = (ImageView)v.findViewById(R.id.oriImgVw);
			//	��ȭ ����
			TextView title = (TextView)v.findViewById(R.id.imgTxt);
			
			/********************************************************/
			//	��ȭ title ��������
			title.setText(m.getTitle());
			Log.d( "MovieAdapter", "��ȭ ���� : " + m.getTitle() );
			/********************************************************/
			
			/********************************************************/
			//	����� �̹��� ó��
			//	����� �ּҸ� �޾ƿ�
			String thumbnailUrl = m.getThumbnail();
			if( !thumbnailUrl.equals("") ) {
				Log.d( "MovieAdapter", "����� �ּ� : " + thumbnailUrl );
				imageDownloader.download(thumbnailUrl, oriImgVw);
			} else {
				//	�̹��� �ּҰ� �������� ��� ó���� ���ΰ�??
				//	�̹����� �ٸ��� ��Ƽ� ����������
				oriImgVw.setImageResource(R.drawable.kara_1);
			}
			/********************************************************/
			return v;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
