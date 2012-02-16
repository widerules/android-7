package kr.co.WhenWhereWho3;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

/*
 * ����ȭ��
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
	
	//��ư onclick ����
	public void execute(View view) {
		Intent intent = null;
		switch (view.getId()) {
		//��ȭlist
		case R.id.btnList:
			intent = new Intent(this, MyMovieListActivity.class);
			break;
		//��ȭ�˻�
		case R.id.btnSearch:
			//�������� ����
			pref = getSharedPreferences("viewType", Activity.MODE_PRIVATE);
		    flag = pref.getInt("viewType", 0);   
			
		    //�˻���� list�� ����
		    if (flag == 0) {
				intent = new Intent(this, SearchMovieListActivity.class);
			} 
		    //�˻���� �����ͷ� ����
		    else if (flag == 1) {
				intent = new Intent(this, SearchMovieGridActivity.class);
			}
			break;
		//��õ��ȭ
		case R.id.btnRecommend:
			intent = new Intent(this, RecommendMovieActivity.class);
			break;
		//����
		case R.id.btnSetting:
			intent = new Intent(this, SetupActivity.class);
			break;
		//����ȭ��ã��
		case R.id.btnCinema:
			intent = new Intent(this, FindCinemaActivity.class);
			break;

		}
		startActivity(intent);
	}
}