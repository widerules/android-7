package kr.co.WhenWhereWho3;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class SearchMovieListActivity extends Activity {
	/*********************************************************/
	//	멤버변수 선언 부분
	InputStreamReader isr;				//	url로부터 읽어온 정보를 담을 InputStreamReader객체
	BufferedReader br;					//	속도를 위한 BufferedReader
	
	EditText editTxt;					//	검색값 입력할 에디트 창
	Button searchBtn;					//	검색 버튼
	ListView listVw;					//	리스트 뷰
	
	boolean isParsing = false;			//	
	boolean isLoaded = false;			//	멈추는 현상을 방지하기 위해 플래그값 지정
	Movie movie;						//	쓰레드에서 사용할 객체
	MovieListAdapter adapter;				//	MovieAdapter
	Parse parse;
	
	ArrayList<Movie> movies;
	
	Handler handler = new Handler(){	// 핸들러 객체
		@Override
		public void handleMessage(Message msg) {
			switch( msg.what ) {
			case 0:
				//	파싱 결과가 없을 경우
				movies = parse.jsonParse( (BufferedReader)msg.obj );
				if( movies == null ) {
					Toast.makeText(getApplicationContext(), "검색 결과가 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
				} else {
					adapter = new MovieListAdapter(SearchMovieListActivity.this, R.layout.searchlist, movies );
					listVw.setAdapter(adapter);
				}
				break;
			}
			isLoaded = false;
			Toast.makeText(getApplicationContext(), "파싱이 완료되었습니다.", Toast.LENGTH_SHORT).show();
		}
	};
	//	멤버변수 선언 끝
	/*********************************************************/
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchmovielist);
        
        parse = new Parse();
        
        editTxt = (EditText)findViewById(R.id.editSearch);
        searchBtn = (Button)findViewById(R.id.btnSearch);
        listVw = (ListView)findViewById(R.id.listview);
        
        //	버튼 이벤트 설정 ( 검색 )
        searchBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				//	입력문자가 공백이면
				if( editTxt.getText().toString().equals("") ){
					Toast.makeText(getApplicationContext(), "검색어를 입력하세요!", Toast.LENGTH_SHORT).show();
					return;
				}
				
				if( isParsing ) return;		//	버튼 중복 클릭 방지
				
				Thread parseThread = new Thread( ) {
					public void run() {
						Looper.prepare();
						loadJson();
						Looper.loop();
					}
				};
				isLoaded = true;
				parseThread.start();
			}
		});
    
        listVw.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(), SearchMovieDetailActivity.class);
				intent.putExtra("movie", movies.get(position));
				//intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
				startActivity(intent);
			}
		});
        
    }//	onCreate( ) 끝
   
    public void loadJson(){
    	try {
    		Message msg = new Message();
    		//	edit텍스트 값을 받아오기 위해 핸들러를 사용하는데
    		//	Looper가 없으면 받아오질 못한다. 그래서 run메소드에서 looper를 사용한다.
    		//	한글 입력 사용을 위한 문자 포맷을 지정한다( UTF-8 )
    		String search = URLEncoder.encode( editTxt.getText().toString().trim(), "UTF-8" );
    		
    		
    		//	네이버 주소
    		//http://openapi.naver.com/search?key=a04e8e81b48e742f0aeba99481386112&query=%EB%B2%A4%ED%97%88&display=10&start=1&target=movie
    		
    		URL url = new URL(
    				"http://apis.daum.net/contents/movie?apikey=c98d00bfc11535f42405eb2605a60586e974c279&output=json&q="
    						+ search );
    		
    		isr = new InputStreamReader( url.openConnection().getInputStream(), "UTF-8" );
    		br = new BufferedReader( isr );
    		
			msg.what = 0;
			msg.obj = br;
			handler.sendMessageDelayed(msg, 200);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}
