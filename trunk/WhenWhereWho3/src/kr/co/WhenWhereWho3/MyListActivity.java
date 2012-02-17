package kr.co.WhenWhereWho3;

import java.util.ArrayList;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
/*
 * 영화list
 * (내가 본 영화 목록)
 */
public class MyListActivity extends Activity {

	private MovieDBHelper dbHelper;
	private SQLiteDatabase db;

	EditText editSearch;
	ListView listview; // 리스트 뷰
	TextView dataCntTxt;
	Movie movie; // 쓰레드에서 사용할 객체
	ArrayList<Movie> movies;
	int deleteMoviePosition;
	MyMovieListAdapter adapter; // MovieAdapter

	Cursor cursor;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mymovielist);

		editSearch = (EditText) findViewById(R.id.myMovie_editSearch);
//		TextWatcher watcher = new TextWatcher() {
//			
//			@Override
//			public void onTextChanged(CharSequence s, int start, int before, int count) {
//				// TODO Auto-generated method stub
//				if(editSearch.getText().toString()!=null) {
//					
//				}
//			}
//			
//			@Override
//			public void beforeTextChanged(CharSequence s, int start, int count,
//					int after) {
//				// TODO Auto-generated method stub
//				
//			}
//			
//			@Override
//			public void afterTextChanged(Editable s) {
//				// TODO Auto-generated method stub
//				
//			}
//		};
//		editSearch.addTextChangedListener(watcher);
//		
		listview = (ListView) findViewById(R.id.myMovie_listview);
		dataCntTxt = (TextView)findViewById(R.id.myMovieList_dataCnt);
		
		movies = new ArrayList<Movie>();
		getCursor();
		
		
		
		//listview item OnClick 리스너
		listview.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub
				Log.d("movieTitle", movies.get(position).getTitle());
				Intent intent = new Intent(getApplicationContext(),
						MyMovieListDetailActivity.class);
				intent.putExtra("movie", movies.get(position));
				startActivity(intent);
			}
		});

		//list item long Click 리스너
		listview.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				deleteMoviePosition = position;
				request();
				return false;
			}
		});	
	}
	
	
	
	//MyMovieList DB all 얻어옴
	public void getCursor() {
		dbHelper = new MovieDBHelper(this);
		db = dbHelper.getWritableDatabase();
		
		cursor = db.rawQuery("SELECT * FROM t_movielist ORDER BY m_when DESC", null);
		
		if (cursor != null) {
			getMyListInfo(cursor);
			if(movies.size() != 0) {
				dataCntTxt.setText("등록된 data 수 : " + movies.size() + "개");
			}
			adapter = new MyMovieListAdapter(MyListActivity.this,
					R.layout.mylist, movies);
			listview.setAdapter(adapter);
		}
		db.close();
	}

	//MyMovieList DB all list 추가
	public void getMyListInfo(Cursor outCursor) {
		dbHelper = new MovieDBHelper(this);
		db = dbHelper.getWritableDatabase();

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

		int recordCnt = outCursor.getCount();
		for (int i = 0; i < recordCnt; i++) {
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

	//MyMovieList DB item삭제 
	public void deleteMovieWishList() {
		dbHelper = new MovieDBHelper(this);
		db = dbHelper.getWritableDatabase();
		
		Movie deleteMovie = movies.get(deleteMoviePosition);

		String[] actors = deleteMovie.getActor();
		String actor = "";
		for (int i = 0; i < actors.length; i++) {
			actor += ((i < actor.length()) ? "," : "") + actors[i];
		}

		String[] Args = { deleteMovie.getTitle(), actor };
		int recordCnt = db.delete("t_movielist", "m_title = ? and m_actor = ?",
				Args);
		if (recordCnt == 1) {
			Toast.makeText(getApplicationContext(), "삭제되었습니다.",
					Toast.LENGTH_SHORT).show();
		}
		db.close();
	}

	//listView item 삭제 Dialog
	public void request() {
		String title = "삭제";
		String message = "삭제 하시겠습니까?";
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
						deleteMovieWishList();
						movies.remove(deleteMoviePosition);
						if(movies.size() != 0) {
							dataCntTxt.setText("등록된 data 수 : " + movies.size() + "개");
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

	//Back 버튼 OnClick 리스너
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Log.e("Back", "bac");
			Intent intent = new Intent(this, WhenWhereWho3Activity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
					| Intent.FLAG_ACTIVITY_SINGLE_TOP);
			startActivity(intent);
		}
		return super.onKeyDown(keyCode, event);
	}

	
	//옵션메뉴
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add("선택삭제");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (movies.size() != 0) {
			Intent intent = new Intent(getApplicationContext(),
					MyListDeleteActivity.class);
			startActivity(intent);
			finish();
		}
		return super.onOptionsItemSelected(item);
	}
}