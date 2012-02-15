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
	//	������� ���� �κ�
	InputStreamReader isr;				//	url�κ��� �о�� ������ ���� InputStreamReader��ü
	BufferedReader br;					//	�ӵ��� ���� BufferedReader
	
	EditText editTxt;					//	�˻��� �Է��� ����Ʈ â
	Button searchBtn;					//	�˻� ��ư
	ListView listVw;					//	����Ʈ ��
	
	boolean isParsing = false;			//	
	boolean isLoaded = false;			//	���ߴ� ������ �����ϱ� ���� �÷��װ� ����
	Movie movie;						//	�����忡�� ����� ��ü
	MovieListAdapter adapter;				//	MovieAdapter
	Parse parse;
	
	ArrayList<Movie> movies;
	
	Handler handler = new Handler(){	// �ڵ鷯 ��ü
		@Override
		public void handleMessage(Message msg) {
			switch( msg.what ) {
			case 0:
				//	�Ľ� ����� ���� ���
				movies = parse.jsonParse( (BufferedReader)msg.obj );
				if( movies == null ) {
					Toast.makeText(getApplicationContext(), "�˻� ����� �������� �ʽ��ϴ�.", Toast.LENGTH_SHORT).show();
				} else {
					adapter = new MovieListAdapter(SearchMovieListActivity.this, R.layout.searchlist, movies );
					listVw.setAdapter(adapter);
				}
				break;
			}
			isLoaded = false;
			Toast.makeText(getApplicationContext(), "�Ľ��� �Ϸ�Ǿ����ϴ�.", Toast.LENGTH_SHORT).show();
		}
	};
	//	������� ���� ��
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
        
        //	��ư �̺�Ʈ ���� ( �˻� )
        searchBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				//	�Է¹��ڰ� �����̸�
				if( editTxt.getText().toString().equals("") ){
					Toast.makeText(getApplicationContext(), "�˻�� �Է��ϼ���!", Toast.LENGTH_SHORT).show();
					return;
				}
				
				if( isParsing ) return;		//	��ư �ߺ� Ŭ�� ����
				
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
        
    }//	onCreate( ) ��
   
    public void loadJson(){
    	try {
    		Message msg = new Message();
    		//	edit�ؽ�Ʈ ���� �޾ƿ��� ���� �ڵ鷯�� ����ϴµ�
    		//	Looper�� ������ �޾ƿ��� ���Ѵ�. �׷��� run�޼ҵ忡�� looper�� ����Ѵ�.
    		//	�ѱ� �Է� ����� ���� ���� ������ �����Ѵ�( UTF-8 )
    		String search = URLEncoder.encode( editTxt.getText().toString().trim(), "UTF-8" );
    		
    		
    		//	���̹� �ּ�
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
