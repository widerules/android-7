package kr.co.WhenWhereWho3;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import android.R.bool;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MyListDeleteActivity extends Activity {

	private MovieDBHelper dbHelper;
	private SQLiteDatabase db;

	Boolean isChecked = false;

	ListView listview; // 리스트 뷰
	Button selectAllBtn;
	Button deleteBtn;

	Movie movie; // 쓰레드에서 사용할 객체
	ArrayList<Movie> movies;
	int deleteMoviePosition;
	MyMovieListDeleteAdapter adapter; // MovieAdapter

	AdapterView.OnItemClickListener mItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			// TODO Auto-generated method stub
			adapter.setChecked(position);
			adapter.notifyDataSetChanged();
		}
	};
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mymovielistdelete);

		dbHelper = new MovieDBHelper(this);
		db = dbHelper.getWritableDatabase();
		movies = new ArrayList<Movie>();

		Cursor cursor = db.rawQuery("SELECT * FROM t_movielist", null);

		if (cursor != null) {
			listview = (ListView) findViewById(R.id.listview);
			getMyListInfo(cursor);
			adapter = new MyMovieListDeleteAdapter(MyListDeleteActivity.this,
					R.layout.mylistdelete, movies);
			listview.setAdapter(adapter);
		}

		selectAllBtn = (Button) findViewById(R.id.selectAllBtn);
		selectAllBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				isChecked = !isChecked;
				adapter.setAllChecked(isChecked);
				// Adapter에 Data에 변화가 생겼을때 Adapter에 알려준다.
				adapter.notifyDataSetChanged();
				listview.invalidate();
			}
		});

		deleteBtn = (Button) findViewById(R.id.deleteBtn);
		deleteBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				request();
			}
		});

	}// onCreate( ) 끝

	public void getMyListInfo(Cursor outCursor) {
		int recordCnt = outCursor.getCount();
		int i = 0;

		int m_titleCol = outCursor.getColumnIndex("m_title");
		int m_thumbnailCol = outCursor.getColumnIndex("m_thumbnail");
		int m_nationCol = outCursor.getColumnIndex("m_nation");
		int m_directorCol = outCursor.getColumnIndex("m_director");
		int m_actorCol = outCursor.getColumnIndex("m_actor");
		int m_genreCol = outCursor.getColumnIndex("m_genre");
		int m_open_infoCol = outCursor.getColumnIndex("m_open_info");
		int m_gradeCol = outCursor.getColumnIndex("m_grade");
		int m_storyCol = outCursor.getColumnIndex("m_story");
		int m_whenCol = outCursor.getColumnIndex("m_when");
		int m_whereCol = outCursor.getColumnIndex("m_where");
		int m_withCol = outCursor.getColumnIndex("m_with");
		int m_commentCol = outCursor.getColumnIndex("m_comment");

		for (i = 0; i < recordCnt; i++) {
			outCursor.moveToNext();

			movie = new Movie();

			movie.setTitle(outCursor.getString(m_titleCol));
			movie.setThumbnail(outCursor.getString(m_thumbnailCol));
			movie.setNation(outCursor.getString(m_nationCol));
			movie.setDirector(outCursor.getString(m_directorCol));
			movie.setActor(outCursor.getString(m_actorCol).split(","));
			movie.setGenre(outCursor.getString(m_genreCol));
			movie.setOpenInfo(outCursor.getString(m_open_infoCol));
			movie.setGrade(outCursor.getString(m_gradeCol));
			movie.setStory(outCursor.getString(m_storyCol));
			movie.setWhen(outCursor.getString(m_whenCol));
			movie.setWhere(outCursor.getString(m_whereCol));
			movie.setWith(outCursor.getString(m_withCol));
			movie.setComment(outCursor.getString(m_commentCol));

			movies.add(movie);

		}
		db.close();
	}

	public void deleteMovieWishList() {
		dbHelper = new MovieDBHelper(this);
		db = dbHelper.getWritableDatabase();

		boolean[] isCheckedConfrim = adapter.getChecked();
		
		for (int j = 0; j < isCheckedConfrim.length; j++) {
			Log.e("삭제", "들렸다"+ isCheckedConfrim[j]);
			if (isCheckedConfrim[j] == true) {
				Log.e("삭제", "지운다" + isCheckedConfrim[j]);
				Movie deleteMovie = movies.get(j);

				String[] actors = deleteMovie.getActor();
				String actor = "";

				for (int i = 0; i < actors.length; i++) {
					actor += ((i < actor.length()) ? "," : "") + actors[i];
				}

				String[] Args = { deleteMovie.getTitle(), actor };
				db.delete("t_movielist", "m_title = ? and m_actor = ?", Args);

				db.close();
			}
		}
		
		for (int j = 0; j < isCheckedConfrim.length; j++) {
			Log.e("삭제", "들렸다");
			if (isCheckedConfrim[j] == true) {
				movies.remove(j);
			}
		}
		
	}

	public void request() {
		String title = "삭제";
		String message = "삭제 하시겠습니까?";
		String titleButtonYes = "예";
		String titleButtonNo = "아니오";

		AlertDialog dialog = makeRequestDialog(title, adapter.getChecked().length + message, titleButtonYes,
				titleButtonNo);
		dialog.show();
	}

	private AlertDialog makeRequestDialog(CharSequence title,
			CharSequence message, CharSequence titleButtonYes,
			CharSequence titleButtonNo) {

		AlertDialog.Builder requestDialog = new AlertDialog.Builder(this);
		requestDialog.setTitle(title);
		requestDialog.setMessage(message);
		requestDialog.setPositiveButton(titleButtonYes,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						deleteMovieWishList();
						if(movies.size() == 0) {
							Intent intent = new Intent(getApplicationContext(), MyMovieListActivity.class);
							intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
							startActivity(intent);
						}
						adapter.notifyDataSetChanged();
						listview.invalidate();
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

}
