package kr.co.WhenWhereWho3;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

/*
 * ��ȭ ���
 */
public class RegistMovieActivity extends Activity {	

	InputStreamReader isr;				//	url�κ��� �о�� ������ ���� InputStreamReader��ü
	BufferedReader br;		
	
	static final int DATE_DIALOG_ID =0;

	RatingBar ratingBar;
	Button whenBtn;
	ProgressDialog pd;
	TextView movieTitle, whenTxtVw, ratingBarTxt;
	EditText whereTxt, withTxt, commentEditTxt;

	private int mYear, mMonth, mDay;
	
	String when, where, with, comment;
	float grade;

	Movie movie;
	MovieDBHelper DBHelper;
	SQLiteDatabase db;
	
	Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch( msg.what ) {
			case 0:
				if(pd != null) {
					pd.dismiss();
				}
				Toast.makeText(getApplicationContext(), "����� �Ϸ�Ǿ����ϴ�.", Toast.LENGTH_SHORT).show();
				
				Intent intent = new Intent(getApplicationContext(), MyMovieListActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
				startActivity(intent);
				
				break;
			}

		};

	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.registmovie);

		movieTitle = (TextView)findViewById(R.id.movieTitle);
		whenTxtVw = (TextView)findViewById(R.id.whenTxtVw);
		whereTxt = (EditText)findViewById(R.id.whereTxt);
		withTxt = (EditText)findViewById(R.id.withTxt);
		commentEditTxt = (EditText)findViewById(R.id.commentEditTxt);
		
		
		// ������ �������� ������ movie ��ü�� �Ѱܹ���
		Intent intent = getIntent();
		if(intent != null) {
			movie = (Movie)intent.getSerializableExtra("movie");
		} else {
			Log.d("����",movie.toString());
		}
		movieTitle.setText(movie.getTitle());

		// ��¥����
		whenTxtVw = (TextView)findViewById(R.id.whenTxtVw);
		whenBtn = (Button)findViewById(R.id.whenBtn);
		whenBtn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				showDialog(DATE_DIALOG_ID);
			}
		});
		
		Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DATE);
		updateDisplay();

		//RatingBar ǥ��
		ratingBar = (RatingBar)findViewById(R.id.ratingbar);
		ratingBarTxt = (TextView)findViewById(R.id.ratingBarTxt);
		ratingBar.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {

			public void onRatingChanged(RatingBar ratingBar, float rating,
					boolean fromUser) {
				ratingBarTxt.setText((rating*2) + " / 10.0");
			}
		});
		
		
		//��Ϲ�ư onClick ������
		Button registBtn = (Button)findViewById(R.id.registBtn);
		registBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {	
				when = (mYear+"") + (mMonth+"") + (mDay+""); 
				where = whereTxt.getText().toString();
				with = withTxt.getText().toString();
				comment = commentEditTxt.getText().toString();
				grade = (ratingBar.getRating())*2;
				
				if (where.trim().equals("") || with.trim().equals("") || comment.trim().equals("")) {
					Toast.makeText(getApplicationContext(), "�����͸� ���� �Է��ϼ���.", Toast.LENGTH_LONG).show();
					return;
				}
				insertRecordParam();	
				Intent intent = new Intent(getApplicationContext(), MyMovieListActivity.class);
				startActivity(intent);
			}
		});

		//��ҹ�ư onClick ������
		Button cancelBtn = (Button)findViewById(R.id.cancleBtn);
		cancelBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	//DatePickerDialog���
	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			mYear = year;
			mMonth = monthOfYear;
			mDay = dayOfMonth;
			updateDisplay();
		}
	};
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID:
			return new DatePickerDialog(this, mDateSetListener, mYear, mMonth, mDay);
		}
		return null;
	}
	private void updateDisplay() {
		whenTxtVw.setText(new StringBuilder().append(mYear).append("-")
				.append(pad(mMonth+1)).append("-").append(pad(mDay)).append(" "));
	}
	private static String pad(int value) {
		if(value >= 10) {
			return String.valueOf(value);
		}
		else {
			return "0" + String.valueOf(value);
		}
	}

	//MyMovieList DB�� ����
	private void insertRecordParam() {
		DBHelper = new MovieDBHelper(this);
		db = DBHelper.getWritableDatabase();

		ContentValues recordValues = new ContentValues();

		String[] actors = movie.getActor();
		String actor = "";
		for(int i=0; i<actors.length; i++) {
			actor += ((i < actor.length()) ? "," : "") + actors[i];
		}
		
		recordValues.put("m_title", movie.getTitle());
		recordValues.put("m_when", when);
		recordValues.put("m_where", where);
		recordValues.put("m_with", with);
		recordValues.put("m_grade", grade + "");
		recordValues.put("m_comment", comment);
		recordValues.put("m_thumbnail", movie.getThumbnail());
		recordValues.put("m_nation", movie.getNation());
		recordValues.put("m_director", movie.getDirector());
		recordValues.put("m_actor", actor);
		recordValues.put("m_genre", movie.getGenre());
		recordValues.put("m_open_info", movie.getOpenInfo());
		recordValues.put("m_story", movie.getStory());
		
		db.insert("t_movielist", null, recordValues);
		db.close();
	}

}

