package kr.co.WhenWhereWho3;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;



public class RecommendMovieActivity extends Activity {
	//	�ε� ���̾�α�
	private final int PROGRESS_DIALOG = 1;
	//	Ŀ���÷ο� ��ü ����( galleryŬ���� ��� )
	private CoverFlow gallery;
	private TextView imgTxt;
	
	private InputStreamReader isr;
	private BufferedReader br;
	private URL url;
	
	private Parse parse;
	
	private ArrayList<Movie> movieList;
	private ArrayList<String> titleList;
	
	private MovieGalleryAdapter galleryAdapter;
	
	private boolean isLoaded = false;
	
	private ProgressDialog progressDialog;
	
	private Handler handler = new Handler(){
		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			switch( msg.what ) {
			//	0�� �޽����� ���̹� �˻��� ���ؼ� ���� ������� ���� �޾Ҵٴ� �ǹ̴�.
			//	��ȭ �帣 �� ������ �˻��� Html �ҽ��� �޾Ƽ� ó��
			case 0:
				//	��� ���� ��ȭ ��ü
				movieList = new ArrayList<Movie>();
				//	�帣, ���� �˻� ���( ���̹� �˻� ��� )�� ���� String List ��ü�� 
				//	htmlParse()�޼��带 ���� ��ȭ ������� ������.
				titleList = parse.htmlParse( (BufferedReader)msg.obj );
				
				//	���⼭���ʹ� ������ ��ȭ ������ ���ؼ� �̹����� ������ �۾��� �Ѵ�.
				//	�� ���� �ð��� ����� �ɸ� ������ ����ǹǷ�, ������� �����Ѵ�.
				Thread parseThread = new Thread(){
					public void run() {
						Message msg = new Message();
						if( titleList == null ) {
							progressDialog.dismiss();
							handler.sendEmptyMessage(1);
							return;
						}
						for( String t : titleList ) {
							try {
								//	�˻�� ���ؼ� �����ڵ� ó��
								String title = URLEncoder.encode( t, "UTF-8" );
								url = new URL(
										"http://apis.daum.net/contents/movie?apikey=c98d00bfc11535f42405eb2605a60586e974c279&output=json&q="
												+ title );
								isr = new InputStreamReader( url.openConnection().getInputStream(), "UTF-8" );
								br = new BufferedReader( isr );
								//	���⼭�� ������ ������ �˻��� �ǹǷ�, movieList�� �� movieList�� �����ִ�
								//	addAll()�̶�� �޼��带 Ȱ���Ѵ�.
								movieList.addAll( parse.jsonParse( br ) );
								
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						//	1�� �޽����� ������ �Ľ��� �����ٴ� �޽����̴�.
						msg.what = 1;
						//	movieList�� ��Ƽ� �ٽ� handler���� �����Ѵ�.
						msg.obj = movieList;
						isLoaded = true;
						handler.sendMessageDelayed(msg, 200);
					};
				};
				//	������ ����
				parseThread.start();
				break;
			case 1:
				if( progressDialog != null && isLoaded ) {
					progressDialog.dismiss();
					removeDialog(PROGRESS_DIALOG);
				}
				movieList = (ArrayList<Movie>)msg.obj; 
				if( movieList == null ) {
					Toast.makeText(getApplicationContext(), "�˻������ �������� �ʽ��ϴ�.", Toast.LENGTH_SHORT).show();
				} else {
					galleryAdapter = new MovieGalleryAdapter(RecommendMovieActivity.this, R.layout.recommendgallery, movieList);
					gallery.setAdapter( galleryAdapter );
					
					gallery.setSpacing(-45);
				}
				Toast.makeText(getApplicationContext(), "�Ľ��� �Ϸ�Ǿ����ϴ�.", Toast.LENGTH_SHORT).show();
				break;
			}
		}
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recommendmovie);
		
		gallery = (CoverFlow)findViewById(R.id.gallery);
		imgTxt = (TextView)findViewById(R.id.imgTxt);
		parse = new Parse();
		
		//	������� �ϴ� ���̹����� �����͸� �о��
		Thread t = new Thread(){
			@Override
			public void run() {
				Looper.prepare();
				showDialog(PROGRESS_DIALOG);
				loadTitle("�׼�", "����");
				Looper.loop();
			}
		};
		
		t.start();
		
		gallery.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				//	��ȭ ������ �� �ε��Ǿ��ִ� ���¶�� �̹� movieList�� �ε��Ǿ��ִ� �����̹Ƿ�
				//	��ȭ ���������� �����ͼ� �ѷ��ش�.
				if( isLoaded ){
					imgTxt.setText( movieList.get(position).getTitle() );
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
//		//	�������� ������ Ŭ���� ��� �̺�Ʈ
//		gallery.setOnItemClickListener(new OnItemClickListener() {
//			public void onItemClick(AdapterView parent, View v, int position, long id) {
//				imgTxt = (TextView)findViewById(R.id.imgTxt);
//				imgTxt.setText(name[position]);
//
//				oriImgVw = (ImageView)findViewById(R.id.oriImgVw);
//				oriImgVw.setImageResource(images[position]);
//			}
//		});
	}
	
	private void loadTitle( String genre, String nation ) {
	    URL url;
	    Message msg = new Message();
		try {
			//	�ڿ� �����ڵ�� "��ȭ"��� ������
			String preUrl = URLEncoder.encode( nation + genre + "��ȭ", "UTF-8" ); 
			url = new URL( "http://m.search.naver.com/search.naver?ie=utf8&sm=tab_txc&where=nexearch&query=" + preUrl );
			InputStreamReader isr = new InputStreamReader( url.openConnection().getInputStream(), "UTF-8" );
			BufferedReader br = new BufferedReader( isr );
			
			msg.what = 0;
			msg.obj = br;
			handler.sendMessageDelayed(msg, 200);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch( id ){
		case PROGRESS_DIALOG:
			progressDialog = new ProgressDialog(RecommendMovieActivity.this);
			progressDialog.setMessage("������ �ҷ����� ���Դϴ�.\n ��ø� ��ٷ� �ֽʽÿ�...");
			progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progressDialog.setCancelable(false);
			return progressDialog;
		}
		return super.onCreateDialog(id);
	}
	
	
}
