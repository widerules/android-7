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
	//	로딩 다이얼로그
	private final int PROGRESS_DIALOG = 1;
	//	커버플로우 객체 생성( gallery클래스 상속 )
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
			//	0번 메시지는 네이버 검색을 통해서 얻어온 결과물을 전달 받았다는 의미다.
			//	영화 장르 및 국가로 검색한 Html 소스를 받아서 처리
			case 0:
				//	결과 담을 영화 객체
				movieList = new ArrayList<Movie>();
				//	장르, 국가 검색 결과( 네이버 검색 결과 )를 담을 String List 객체에 
				//	htmlParse()메서드를 통해 영화 제목들을 도출함.
				titleList = parse.htmlParse( (BufferedReader)msg.obj );
				
				//	여기서부터는 각각의 영화 제목을 통해서 이미지를 얻어오는 작업을 한다.
				//	이 역시 시간이 상당히 걸릴 것으로 예상되므로, 쓰레드로 구현한다.
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
								//	검색어에 대해서 유니코드 처리
								String title = URLEncoder.encode( t, "UTF-8" );
								url = new URL(
										"http://apis.daum.net/contents/movie?apikey=c98d00bfc11535f42405eb2605a60586e974c279&output=json&q="
												+ title );
								isr = new InputStreamReader( url.openConnection().getInputStream(), "UTF-8" );
								br = new BufferedReader( isr );
								//	여기서는 제목이 여러번 검색이 되므로, movieList에 또 movieList를 더해주는
								//	addAll()이라는 메서드를 활용한다.
								movieList.addAll( parse.jsonParse( br ) );
								
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						//	1번 메시지는 완전히 파싱이 끝났다는 메시지이다.
						msg.what = 1;
						//	movieList를 담아서 다시 handler한테 전달한다.
						msg.obj = movieList;
						isLoaded = true;
						handler.sendMessageDelayed(msg, 200);
					};
				};
				//	쓰레드 동작
				parseThread.start();
				break;
			case 1:
				if( progressDialog != null && isLoaded ) {
					progressDialog.dismiss();
					removeDialog(PROGRESS_DIALOG);
				}
				movieList = (ArrayList<Movie>)msg.obj; 
				if( movieList == null ) {
					Toast.makeText(getApplicationContext(), "검색결과가 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
				} else {
					galleryAdapter = new MovieGalleryAdapter(RecommendMovieActivity.this, R.layout.recommendgallery, movieList);
					gallery.setAdapter( galleryAdapter );
					
					gallery.setSpacing(-45);
				}
				Toast.makeText(getApplicationContext(), "파싱이 완료되었습니다.", Toast.LENGTH_SHORT).show();
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
		
		//	쓰레드로 일단 네이버에서 데이터를 읽어옴
		Thread t = new Thread(){
			@Override
			public void run() {
				Looper.prepare();
				showDialog(PROGRESS_DIALOG);
				loadTitle("액션", "영국");
				Looper.loop();
			}
		};
		
		t.start();
		
		gallery.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				//	영화 정보가 다 로딩되어있는 상태라면 이미 movieList가 로딩되어있는 상태이므로
				//	영화 제목정보를 가져와서 뿌려준다.
				if( isLoaded ){
					imgTxt.setText( movieList.get(position).getTitle() );
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
//		//	갤러리의 사진을 클릭할 경우 이벤트
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
			//	뒤에 유니코드는 "영화"라는 글자임
			String preUrl = URLEncoder.encode( nation + genre + "영화", "UTF-8" ); 
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
			progressDialog.setMessage("정보를 불러오는 중입니다.\n 잠시만 기다려 주십시오...");
			progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progressDialog.setCancelable(false);
			return progressDialog;
		}
		return super.onCreateDialog(id);
	}
	
	
}
