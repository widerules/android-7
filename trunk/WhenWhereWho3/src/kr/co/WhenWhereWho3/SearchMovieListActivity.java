package kr.co.WhenWhereWho3;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/*
 * ��ȭ�˻� 
 * (list�� ����)
 */
public class SearchMovieListActivity extends Activity {

	//	������� ���� �κ�
	InputStreamReader isr;				//	url�κ��� �о�� ������ ���� InputStreamReader��ü
	BufferedReader br;					//	�ӵ��� ���� BufferedReader
	
	InputMethodManager imm;
	
	EditText editTxt;					//	�˻��� �Է��� ����Ʈ â
	Button searchBtn;					//	�˻� ��ư
	TextView dataCntTxt;				//  �˻��� data �� setting
	ListView listview;					//	����Ʈ ��
	
	boolean isParsing = false;			//	
	boolean isLoaded = false;			//	���ߴ� ������ �����ϱ� ���� �÷��װ� ����
	Movie movie;						//	�����忡�� ����� ��ü
	MovieListAdapter adapter;				
	Parse parse;
	ArrayList<Movie> movies;
	
	ProgressDialog pd;
	final int FACEBOOK_PROGRESS_DIALOG = 1010;
	
	Handler handler = new Handler(){	// �ڵ鷯 ��ü
		@Override
		public void handleMessage(Message msg) {
			switch( msg.what ) {
			case 0:
				movies = parse.jsonParse( (BufferedReader)msg.obj );
				pd.dismiss();
				//�Ľ̰���� ���� Toast �޽��� 
				if( movies == null ) {
					Toast.makeText(getApplicationContext(), "�˻� ����� �������� �ʽ��ϴ�.", Toast.LENGTH_SHORT).show();
				} 
				//�Ľ̰�� ����� listview item �߰�
				else {	
					dataCntTxt.setText("�˻��� ��ȭ : " + movies.size() + "��");
					adapter = new MovieListAdapter(SearchMovieListActivity.this, R.layout.searchlist, movies );
					listview.setAdapter(adapter);
				}
				break;
			}
			isLoaded = false;
//			Toast.makeText(getApplicationContext(), "�Ľ��� �Ϸ�Ǿ����ϴ�.", Toast.LENGTH_SHORT).show();
		}
	};
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchmovielist);        
        
        parse = new Parse();
        
        editTxt = (EditText)findViewById(R.id.searchMovieList_editSearch);
        searchBtn = (Button)findViewById(R.id.searchMovieList_btnSearch);
        dataCntTxt = (TextView)findViewById(R.id.searchMovieList_dataCnt);
        listview = (ListView)findViewById(R.id.searchMovieList_listview);
        
        //�˻���ư onclick ������
        searchBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				//	�Է¹��ڰ� �����̸�
				if( editTxt.getText().toString().equals("") ){
					Toast.makeText(getApplicationContext(), "�˻�� �Է��ϼ���!", Toast.LENGTH_SHORT).show();
					return;
				}
				
				//�˻��� Ű���� �����
				imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE); 
				imm.hideSoftInputFromWindow(editTxt.getWindowToken(), 0); 
				
				if( isParsing ) return;		//	��ư �ߺ� Ŭ�� ����
				
				showDialog(FACEBOOK_PROGRESS_DIALOG);
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
    
        //����Ʈ item onclick ������
        listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(), SearchMovieDetailActivity.class);
				intent.putExtra("movie", movies.get(position));
				startActivity(intent);
			}
		});
        
    }
    
   //�Ľ� �� ������ ���� ����
    public void loadJson(){
    	try {
    		Message msg = new Message();
    		//	edit�ؽ�Ʈ ���� �޾ƿ��� ���� �ڵ鷯�� ����ϴµ�
    		//	Looper�� ������ �޾ƿ��� ���Ѵ�. �׷��� run�޼ҵ忡�� looper�� ����Ѵ�.
    		//	�ѱ� �Է� ����� ���� ���� ������ �����Ѵ�( UTF-8 )
    		String search = URLEncoder.encode( editTxt.getText().toString().trim(), "UTF-8" );
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

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case FACEBOOK_PROGRESS_DIALOG:
			pd = new ProgressDialog(SearchMovieListActivity.this);
			pd.setMessage("�˻� ���Դϴ�...");
			pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pd.setCancelable(false);
			return pd;
		}
		return super.onCreateDialog(id);
	}
}