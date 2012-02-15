package kr.co.WhenWhereWho3;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
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
		Movie m = movies.get(position);
		
		try {
			//	��ȭ ������
			ImageView imgVw = new ImageView( getContext() ); 
			
			/********************************************************/
			//	����� �̹��� ó��
			//	����� �ּҸ� �޾ƿ�
			String thumbnailUrl = m.getThumbnail();
			if( !thumbnailUrl.equals("") ) {
				Log.d( "MovieAdapter", "����� �ּ� : " + thumbnailUrl );
				
				imageDownloader.download(thumbnailUrl, imgVw);
				imgVw.setLayoutParams( 
						new CoverFlow.LayoutParams( 
								LayoutParams.FILL_PARENT, 
								LayoutParams.WRAP_CONTENT ) );
				imgVw.setScaleType(ImageView.ScaleType.FIT_CENTER);
				
			} else {
				//	�̹��� �ּҰ� �������� ��� ó���� ���ΰ�??
				//	�̹����� �ٸ��� ��Ƽ� ����������
				imgVw.setLayoutParams( 
						new CoverFlow.LayoutParams( 
								LayoutParams.FILL_PARENT, 
								LayoutParams.WRAP_CONTENT ) );
				imgVw.setImageResource(R.drawable.kara_1);
			}
			/********************************************************/
			
			return imgVw;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
