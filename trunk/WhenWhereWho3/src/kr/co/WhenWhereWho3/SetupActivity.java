package kr.co.WhenWhereWho3;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

public class SetupActivity extends Activity {	
//	String [] name = {"List로 보기", "포스터 여러개 보기"};
//	private String viewType;
//	Spinner spinVw;
	
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
		
		
		
		
//		Spinner spinVw = (Spinner)findViewById(R.id.spinVw);
//        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, name);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinVw.setAdapter(adapter);
//
//        SharedPreferences pref = getSharedPreferences("viewType", Activity.MODE_PRIVATE);
//        int value = pref.getInt("viewType", 0);        
//        spinVw.setSelection(value);
//        spinVw.setOnItemSelectedListener(new OnItemSelectedListener() {
//
//			public void onItemSelected(AdapterView<?> arg0, View arg1,
//					int position, long arg3) {
//				SharedPreferences pref = getSharedPreferences("viewType", Activity.MODE_PRIVATE);
//		    	Editor editor = (Editor)pref.edit();
//		    	
//		    	switch (position) {
//		    	// case 0 : List로 보기
//				case 0:
//					editor.putInt("viewType", 0);
//					break;
//				// case 1 : GridView로 보기
//				case 1:
//					editor.putInt("viewType", 1);
//					break;
//				}
//		    	editor.commit();
//			}
//
//			public void onNothingSelected(AdapterView<?> arg0) {
//				// TODO Auto-generated method stub
//				
//			}        	
//		});
	}
}

