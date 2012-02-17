package kr.co.WhenWhereWho3;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class SetupActivity extends Activity {	

	/***************************************************/
	//	멤버변수 선언
	ListView systemListVw;	//	시스템 설정 리스트 뷰
	ListView basicListVw;	//	기본 설정 리스트 뷰
	SetupListAdapter systemAdapter;	//	시스템 설정 어댑터
	SetupListAdapter basicAdapter;	//	기본설정 어댑터

	ArrayList<SetupList> systemList;
	ArrayList<SetupList> basicList;

	String [] systemTitle = { "검색화면 설정" };
	String [] systemSubTitle = { "검색 결과 화면 레이아웃을 설정" };
	String [] basicTitle = { "도움말", "버전 정보" };
	String [] basicSubTitle = { "애플리케이션 사용 설명서", "애플리케이션 버전 정보 및 개발자 정보" };
	/***************************************************/


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setup);

		systemListVw = (ListView)findViewById(R.id.setUp_systemListVw);
		basicListVw = (ListView)findViewById(R.id.setUp_basicListVw);

		systemList = new ArrayList<SetupList>();
		basicList = new ArrayList<SetupList>();

		for( int i = 0; i < systemTitle.length; i++ ) {
			SetupList  setupList = new SetupList();
			setupList.setTitle(systemTitle[i]);
			setupList.setContent(systemSubTitle[i]);

			systemList.add(setupList);
		}

		for( int i = 0; i < basicTitle.length; i++ ) {
			SetupList  setupList = new SetupList();
			setupList.setTitle(basicTitle[i]);
			setupList.setContent(basicSubTitle[i]);

			basicList.add(setupList);
		}


		systemAdapter = new SetupListAdapter(
				getApplicationContext(),
				R.layout.setuplist,
				systemList);
		basicAdapter = new SetupListAdapter(
				getApplicationContext(), 
				R.layout.setuplist,
				basicList);


		systemListVw.setAdapter(systemAdapter);
		basicListVw.setAdapter(basicAdapter);

		systemListVw.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(getApplicationContext(), SetupSearchViewActivity.class);
				startActivity(intent);
				finish();
			}

		});


	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}
}

