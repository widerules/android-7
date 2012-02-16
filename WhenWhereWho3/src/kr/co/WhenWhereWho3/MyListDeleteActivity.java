package kr.co.WhenWhereWho3;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class MyListDeleteActivity extends Activity {

	private MovieDBHelper dbHelper;
	private SQLiteDatabase db;

	Boolean isChecked = false;

	ListView listview; // 리스트 뷰
	Button selectAllBtn;
	Button deleteBtn;

	Movie movie; // 쓰레드에서 사용할 객체
	ArrayList<Movie> movies;
	MyMovieListDeleteAdapter adapter; // MovieAdapter

	AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			// TODO Auto-generated method stub
			Toast.makeText(getApplicationContext(), "" + (position + 1),
					Toast.LENGTH_SHORT).show();
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
			listview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

			// Cursor에 대한 결과를 movie리스트 객체에 받는다.
			// 리턴을 MovieList로 함( 수정자 - 이만재 2012-02-15 )
			movies = getMyListInfo(cursor);
			adapter = new MyMovieListDeleteAdapter(MyListDeleteActivity.this,
					R.layout.mylistdelete, movies);
			listview.setAdapter(adapter);
			listview.setOnItemClickListener(mItemClickListener);
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

	public ArrayList<Movie> getMyListInfo(Cursor outCursor) {
		ArrayList<Movie> movies = new ArrayList<Movie>();
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
		return movies;
	}

	public void deleteMovieWishList() {
		dbHelper = new MovieDBHelper(this);
		db = dbHelper.getWritableDatabase();

		ArrayList<Integer> isCheckedConfrim = adapter.getChecked();

		Log.e("movie Size", "체크된 index : " + isCheckedConfrim.size());
		for (int i = 0; i < isCheckedConfrim.size(); i++) {
			Log.e("movie객체", "체크된 index : " + isCheckedConfrim.get(i));
			Movie deleteMovie = movies.get(isCheckedConfrim.get(i));

			String[] actors = deleteMovie.getActor();
			String actor = "";

			for (int j = 0; j < actors.length; j++) {
				actor += ((j < actor.length()) ? "," : "") + actors[j];
			}
			Log.e("actor", actor);

			String[] Args = { deleteMovie.getTitle(), actor };
			Log.e("args", "체크된 index : " + isCheckedConfrim.get(i));
			int cnt = db.delete("t_movielist", "m_title = ? and m_actor = ?",
					Args);
			Log.e("dbCnt", "체크된 index : " + cnt);
		}
		db.close();

		for (int i = 0; i < isCheckedConfrim.size(); i++) {
			Log.e("movie객체 삭제 부분", "체크된 index : " + isCheckedConfrim.get(i));
			movies.remove(isCheckedConfrim.get(i));
		}
	}

	public void request() {
		String title = "삭제";
		String message = "삭제 하시겠습니까?";
		String titleButtonYes = "예";
		String titleButtonNo = "아니오";

		// 나중에 메시지 수정
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
		requestDialog.setPositiveButton(titleButtonYes,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						deleteMovieWishList();
						adapter.notifyDataSetChanged();
						listview.invalidate();
						Intent intent = new Intent(getApplicationContext(),
								MyMovieListActivity.class);
						startActivity(intent);
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
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Log.e("Back", "bac");
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Log.e("Back", "bac");
			Intent intent = new Intent(this, MyMovieListActivity.class);
//			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
//					| Intent.FLAG_ACTIVITY_SINGLE_TOP);
			startActivity(intent);
			finish();
		}

		return super.onKeyDown(keyCode, event);
	}

}
