package kr.co.WhenWhereWho3;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;



public class RecommendMovieActivity extends Activity {	
	private TextView imgTxt;
	private ImageView oriImgVw;
	
	private InputStreamReader isr;
	private BufferedReader br;
	private URL url;
	
	private Parse parse;
	private ImageDownloader imageDownloader = new ImageDownloader();
	
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch( msg.what ) {
			case 0:
				ArrayList<String> titleList = parse.htmlParse( (BufferedReader)msg.obj );
				Thread parseThread = new Thread(){
					public void run() {
						
					};
				};
				parseThread.start();
				break;
			}
		}
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recommendmovie);

		Gallery gallery = (Gallery)findViewById(R.id.gallery);
		Thread t = new Thread(){
			@Override
			public void run() {
				loadTitle("액션", "영국");
			}
		};
		
		gallery.setAdapter(adapter);
		
		parse = new Parse();
		
		
		//	갤러리의 사진을 클릭할 경우 이벤트
		gallery.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView parent, View v, int position, long id) {
				imgTxt = (TextView)findViewById(R.id.imgTxt);
				imgTxt.setText(name[position]);

				oriImgVw = (ImageView)findViewById(R.id.oriImgVw);
				oriImgVw.setImageResource(images[position]);
			}
		});
	}
	
	private void loadTitle( String genre, String nation ) {
	    URL url;
	    ArrayList<String> titleList = new ArrayList<String>();
	    Message msg = new Message();
		try {
			//	뒤에 유니코드는 "영화"라는 글자임
			String preUrl = URLEncoder.encode( "http://search.naver.com/search.naver?ie=utf8&sm=tab_txc&where=nexearch&query="
							+ nation + genre + "%EC%98%81%ED%99%94", "UTF-8" ); 
			url = new URL( preUrl );
			InputStreamReader isr = new InputStreamReader( url.openConnection().getInputStream(), "UTF-8" );
			BufferedReader br = new BufferedReader( isr );
			
			msg.what = 0;
			msg.obj = br;
			handler.sendMessageDelayed(msg, 200);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	
}
