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
			intent = new Intent(this, MyMapActivity.class);
			break;

		}
		startActivity(intent);
	}
	
	//listView item ���� Dialog
	public void request() {
		String title = "����";
		String message = "���� �Ͻðڽ��ϱ�?";
		String titleButtonYes = "��";
		String titleButtonNo = "�ƴϿ�";

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
		
		//���̾�α� ��ư OnClick ������
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

	//Back ��ư OnClick ������
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Log.e("Back", "bac");
			request();
		}
		return super.onKeyDown(keyCode, event);
	}
}