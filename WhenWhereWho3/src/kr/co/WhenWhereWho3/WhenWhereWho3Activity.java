package kr.co.WhenWhereWho3;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
			intent = new Intent(this, FindCinemaActivity.class);
			break;

		}
		startActivity(intent);
	}
}