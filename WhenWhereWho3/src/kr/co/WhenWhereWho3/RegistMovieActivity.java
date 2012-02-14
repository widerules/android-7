package kr.co.WhenWhereWho3;

import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

	String DBName = "WWW";
	String TableName = "t_movie_list";

	ProgressDialog pd;

	TextView titleTxtVw, dateTxtVw, ratingBarTxt;
	EditText whereTxt, whoTxt, commentEditTxt;

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
				break;
			}

		};

	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.registmovie);

		createDatabase(DBName);
		createTable(TableName);


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

				showDialog(1001);

				titleTxtVw = (TextView)findViewById(R.id.titleTxtVw);
				dateTxtVw = (TextView)findViewById(R.id.dateTxtVw);
				whereTxt = (EditText)findViewById(R.id.whereTxt);
				whoTxt = (EditText)findViewById(R.id.whoTxt);
				commentEditTxt = (EditText)findViewById(R.id.commentEditTxt);


				final String title = titleTxtVw.getText().toString();
				final String date = dateTxtVw.getText().toString();
				final String where = whereTxt.getText().toString();
				final String who = whoTxt.getText().toString();
				final String grade = ratingBar.getRating()*2 + "";
				final String comment = commentEditTxt.getText().toString();


				if (where.trim().equals("") || who.trim().equals("") || comment.trim().equals("")) {
					Toast.makeText(getApplicationContext(), "데이터를 먼저 입력하세요.", Toast.LENGTH_LONG).show();
					return;
				}

				Thread t = new Thread(){
					@Override
					public void run() {
						insertData(title, date, where, who, grade, comment);
						handler.sendEmptyMessage(0);
					}
				};
				t.start();
			}
		});
		
		Button cancelBtn = (Button)findViewById(R.id.cancleBtn);
		cancelBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), MyMovieListActivity.class);
				startActivity(intent);
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

	private void createDatabase(String inputDBName) {
		db = openOrCreateDatabase(inputDBName, MODE_PRIVATE, null);
	}


	private void createTable(String inputTableName) {
		String tableSql = "create table " + inputTableName + " ( "
				+ " _id integer PRIMARY KEY autoincrement, "
				+ " m_title text, "
				+ " m_open_info text, "
				+ " m_nation text, "
				+ " m_actor text, "
				+ " m_genre text, "
				+ " m_grade text, "
				+ " m_thumbnail blob, "
				+ " m_who text, "
				+ " m_where	text, "
				+ " m_when	text, "
				+ " m_comment text );";
		db.execSQL(tableSql);
	}

	private void insertData(String title, String date, String where, String who, String grade, String comment) {
		String sql = "insert into t_movie_list(m_title, m_when, m_where, m_who, m_grade, m_comment) values('" + title + "', '" + date + "', '" + where + "', '" + who + "', '" + grade + "', '"+ comment + "')";
		db.execSQL(sql);
	}


	@Override
	protected Dialog onCreateDialog(int id, Bundle args) {
		pd = new ProgressDialog(this);
		pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		pd.setMessage("등록 중입니다");

		return pd;
	}


}

