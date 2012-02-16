package kr.co.WhenWhereWho3;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

public class SetupSearchViewActivity extends Activity {
	RadioButton rb;
	SharedPreferences pref;
	Editor editor;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setupsearchview);

		pref = getSharedPreferences("viewType", Activity.MODE_PRIVATE);
		editor = (Editor)pref.edit();

		RadioGroup rg = (RadioGroup)findViewById(R.id.setupSearchVwRadioGrp);
		rg.check(R.id.listRadioBtn);
		rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if(checkedId != -1) {
					rb = (RadioButton)findViewById(checkedId);
					if(rb.getText() == "리스트로 보기") {
						editor.putInt("viewType", 0);
					} else {
						editor.putInt("viewType", 1);
					}
					editor.commit();

				}
			}
		});

//		Button setupSearchRegistBtn = (Button)findViewById(R.id.setupSearchRegistBtn);
//		setupSearchRegistBtn.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				Toast.makeText(getApplicationContext(), "변경되었습니다.", Toast.LENGTH_SHORT).show();
//			}
//		});
//		Button setupSearchCancelBtn = (Button)findViewById(R.id.setupSearchCancelBtn);
//		setupSearchCancelBtn.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//
//			}
//		});
	}

}