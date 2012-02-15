package kr.co.WhenWhereWho3;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class SetupActivity extends Activity {	
	String [] name = {"List로 보기", "포스터 여러개 보기"};
	private String viewType;
	Spinner spinVw;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setup);
		
		Spinner spinVw = (Spinner)findViewById(R.id.spinVw);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, name);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinVw.setAdapter(adapter);
        
        SharedPreferences pref = getSharedPreferences("viewType", Activity.MODE_PRIVATE);
        int value = pref.getInt("viewType", 0);        
        spinVw.setSelection(value);
        
        spinVw.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				SharedPreferences pref = getSharedPreferences("viewType", Activity.MODE_PRIVATE);
		    	Editor editor = (Editor)pref.edit();
		    	
		    	switch (position) {
		    	// case 0 : List로 보기
				case 0:
					editor.putInt("viewType", 0);
					break;
				// case 1 : GridView로 보기
				case 1:
					editor.putInt("viewType", 1);
					break;
				}
		    			    	
		    	editor.commit();
			}

			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}        	
		});
	}
}

