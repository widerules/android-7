package kr.co.WhenWhereWho3;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

/*
 * 메인화면
 *  
 * */
public class WhenWhereWho3Activity extends Activity {

	int flag;
	SharedPreferences pref;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main); 
	}
	
	//버튼 onclick 실행
	public void execute(View view) {
		Intent intent = null;
		switch (view.getId()) {
		//영화list
		case R.id.btnList:
			intent = new Intent(this, MyMovieListActivity.class);
			break;
		//영화검색
		case R.id.btnSearch:
			//설정정보 읽음
			pref = getSharedPreferences("viewType", Activity.MODE_PRIVATE);
		    flag = pref.getInt("viewType", 0);   
			
		    //검색결과 list로 보기
		    if (flag == 0) {
				intent = new Intent(this, SearchMovieListActivity.class);
			} 
		    //검색결과 포스터로 보기
		    else if (flag == 1) {
				intent = new Intent(this, SearchMovieGridActivity.class);
			}
			break;
		//추천영화
		case R.id.btnRecommend:
			intent = new Intent(this, RecommendMovieActivity.class);
			break;
		//설정
		case R.id.btnSetting:
			intent = new Intent(this, SetupActivity.class);
			break;
		//가까운영화관찾기
		case R.id.btnCinema:
			intent = new Intent(this, MyMapActivity.class);
			break;

		}
		startActivity(intent);
	}
	
	//listView item 삭제 Dialog
	public void request() {
		String title = "종료";
		String message = "종료 하시겠습니까?";
		String titleButtonYes = "예";
		String titleButtonNo = "아니오";

		AlertDialog dialog = makeRequestDialog(title, message, titleButtonYes,
				titleButtonNo);
		dialog.show();
	}
	private AlertDialog makeRequestDialog(CharSequence title,
			CharSequence message, CharSequence titleButtonYes,
			CharSequence titleButtonNo) {

		AlertDialog.Builder requestDialog = new AlertDialog.Builder(this);
		requestDialog.setTitle(title);
		requestDialog.setMessage(message);
		
		//다이얼로그 버튼 OnClick 리스너
		requestDialog.setPositiveButton(titleButtonYes,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						finish();
					}
				});
		requestDialog.setNegativeButton(titleButtonNo,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				});
		return requestDialog.show();
	}

	//Back 버튼 OnClick 리스너
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Log.e("Back", "bac");
			request();
		}
		return super.onKeyDown(keyCode, event);
	}
}