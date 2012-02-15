package kr.co.WhenWhereWho3;

import java.util.Arrays;
import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import android.widget.TimePicker;
import android.widget.Toast;

public class RegistMovieActivity extends Activity {	

	private Button dateBtn;
	private Button timeBtn;
	private int mYear;
	private int mMonth;
	private int mDay;
	private int mHour;
	private int mMinute;

	RatingBar ratingBar;

	static final int TIME_DIALOG_ID = 0;
	static final int DATE_DIALOG_ID = 1;
	static final int PROGRESS_BAR = 2;

	ProgressDialog pd;

	TextView titleTxtVw, dateTxtVw, ratingBarTxt;
	EditText whereTxt, whoTxt, commentEditTxt;
	
	String when;
	String where;
	String with;
	String comment;
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
				Toast.makeText(getApplicationContext(), "등록이 완료되었습니다.", Toast.LENGTH_SHORT).show();
				
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
		
		
		titleTxtVw = (TextView)findViewById(R.id.titleTxtVw);
		dateTxtVw = (TextView)findViewById(R.id.dateTxtVw);
		whereTxt = (EditText)findViewById(R.id.whereTxt);
		whoTxt = (EditText)findViewById(R.id.whoTxt);
		commentEditTxt = (EditText)findViewById(R.id.commentEditTxt);
		

		// 상세정보 페이지의 값들을 movie 객체로 넘겨받음
		Intent intent = getIntent();
		
		if(intent != null) {
		movie = (Movie)intent.getSerializableExtra("movie");
		} else {
			Log.d("에러",movie.toString());
		}
		
		titleTxtVw.setText(movie.getTitle());

		// 날짜와 시간 설정
		dateTxtVw = (TextView)findViewById(R.id.dateTxtVw);
		dateBtn = (Button)findViewById(R.id.dateBtn);
		dateBtn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				showDialog(DATE_DIALOG_ID);
			}
		});
		timeBtn = (Button)findViewById(R.id.timeBtn);
		timeBtn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				showDialog(TIME_DIALOG_ID);				
			}
		});

		Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DATE);
		mHour = c.get(Calendar.HOUR);
		mMinute = c.get(Calendar.MINUTE);

		updateDisplay();

		//		 RatingBar 표시
		ratingBar = (RatingBar)findViewById(R.id.ratingbar);
		ratingBarTxt = (TextView)findViewById(R.id.ratingBarTxt);
		ratingBar.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {

			public void onRatingChanged(RatingBar ratingBar, float rating,
					boolean fromUser) {
				ratingBarTxt.setText((rating*2) + " / 10.0");
			}
		});
		
		
		Button registBtn = (Button)findViewById(R.id.registBtn);
		registBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {	
				
				where = whereTxt.getText().toString();
				with = whoTxt.getText().toString();
				comment = commentEditTxt.getText().toString();
				grade = (ratingBar.getRating())*2;
				
				if (where.trim().equals("") || with.trim().equals("") || comment.trim().equals("")) {
					Toast.makeText(getApplicationContext(), "데이터를 먼저 입력하세요.", Toast.LENGTH_LONG).show();
					return;
				}

				Thread t = new Thread() {
					public void run() {
						Looper.prepare();				
						
//						showDialog(PROGRESS_BAR);		
						
						insertRecordParam();
						handler.sendEmptyMessage(0);
					};
				};			
				t.start();				
			}
		});

		Button cancelBtn = (Button)findViewById(R.id.cancleBtn);
		cancelBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
//				Intent intent = new Intent(getApplicationContext(), MyMovieListActivity.class);
//				startActivity(intent);
				finish();
			}
		});
	}

	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			mYear = year;
			mMonth = monthOfYear;
			mDay = dayOfMonth;
			updateDisplay();
		}
	};

	private TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {

		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			mHour = hourOfDay;
			mMinute = minute;
			updateDisplay();
		}
	};


	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case TIME_DIALOG_ID:
			return new TimePickerDialog(this, mTimeSetListener, mHour, mMinute, false);
		case DATE_DIALOG_ID:
			return new DatePickerDialog(this, mDateSetListener, mYear, mMonth, mDay);
		case PROGRESS_BAR:
			pd = new ProgressDialog(this);
			pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pd.setMessage("등록 중입니다");
			return pd;
		}
		return null;
	}

	private void updateDisplay() {
		dateTxtVw.setText(new StringBuilder().append(mYear).append("-")
				.append(pad(mMonth+1)).append("-").append(pad(mDay)).append(" ")
				.append(pad(mHour)).append(":").append(pad(mMinute))
				);
	}
	private static String pad(int value) {
		if(value >= 10) {
			return String.valueOf(value);
		}
		else {
			return "0" + String.valueOf(value);
		}
	}

	private void insertRecordParam() {
		DBHelper = new MovieDBHelper(this);
		db = DBHelper.getWritableDatabase();

		ContentValues recordValues = new ContentValues();

		String[] actors = movie.getActor();
		String actor = "";
		
		for(int i=0; i<actors.length; i++) {
			actor += actors[i] + ((i < actor.length()) ? "," : "");
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
	}


	@Override
	protected Dialog onCreateDialog(int id, Bundle args) {
		pd = new ProgressDialog(this);
		pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		pd.setMessage("등록 중입니다");
		return pd;
	}
}

