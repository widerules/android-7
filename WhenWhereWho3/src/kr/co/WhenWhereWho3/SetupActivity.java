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
	//	������� ����
	ListView systemListVw;	//	�ý��� ���� ����Ʈ ��
	ListView basicListVw;	//	�⺻ ���� ����Ʈ ��
	SetupListAdapter systemAdapter;	//	�ý��� ���� �����
	SetupListAdapter basicAdapter;	//	�⺻���� �����

	ArrayList<SetupList> systemList;
	ArrayList<SetupList> basicList;

	String [] systemTitle = { "�˻�ȭ�� ����" };
	String [] systemSubTitle = { "�˻� ��� ȭ�� ���̾ƿ��� ����" };
	String [] basicTitle = { "����", "���� ����" };
	String [] basicSubTitle = { "���ø����̼� ��� ����", "���ø����̼� ���� ���� �� ������ ����" };
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

