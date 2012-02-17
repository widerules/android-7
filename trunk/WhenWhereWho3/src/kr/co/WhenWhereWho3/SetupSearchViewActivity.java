package kr.co.WhenWhereWho3;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

public class SetupSearchViewActivity extends Activity implements OnCheckedChangeListener {
	RadioButton rb;
	SharedPreferences pref;
	Editor editor;
	int checkValue = 0;
	RadioGroup rg;
	private String shareId = "viewType";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setupsearchview);

		pref = getSharedPreferences(shareId, Activity.MODE_PRIVATE);
		int value = pref.getInt(shareId, 0);
		rg = (RadioGroup)findViewById(R.id.setupSearchVwRadioGrp);
		if(value == 0) {
			rg.check(R.id.listRadioBtn);
		} else {
			rg.check(R.id.gridRadioBtn);
		}

		rg.setOnCheckedChangeListener(this);
		
		Button setupSearchRegistBtn = (Button)findViewById(R.id.setupSearchRegistBtn);
		setupSearchRegistBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				SharedPreferences pref = getSharedPreferences(shareId, Activity.MODE_PRIVATE);
		    	Editor editor = (Editor)pref.edit();
		    	if(checkValue == 0) {
		    		editor.putInt(shareId, 0);
		    	} else {
		    		editor.putInt(shareId, 1);
		    	}
		    	editor.commit();
		    	
		    	Toast.makeText(getApplicationContext(), "변경되었습니다.", Toast.LENGTH_SHORT).show();
		    	finish();
			}
		});
		Button setupSearchCancelBtn = (Button)findViewById(R.id.setupSearchCancelBtn);
		setupSearchCancelBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	@Override
	public void onCheckedChanged(RadioGroup arg0, int arg1) {
		switch(arg1) {
		case R.id.listRadioBtn:
			checkValue = 0;
			break;
		case R.id.gridRadioBtn:
			checkValue = 1;
			break;
		}
	}
		

	}

