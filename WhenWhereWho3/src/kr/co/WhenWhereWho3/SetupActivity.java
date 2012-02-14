package kr.co.WhenWhereWho3;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class SetupActivity extends Activity {	
	String [] name = {"��ȭ �˻� ȭ�� ����", "������ ũ�� ����", "List�� ����", "������ ������ ����"};
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setup);
		
		Spinner spinVw = (Spinner)findViewById(R.id.spinVw);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, name);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinVw.setAdapter(adapter);
        spinVw.setSelection(0);
        spinVw.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				Toast.makeText(getApplicationContext(), "���õ� View : " + name[position], 2000).show();
			}

			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}        	
		});
	}
}

