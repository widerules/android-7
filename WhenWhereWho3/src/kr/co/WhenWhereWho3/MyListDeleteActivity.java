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

/*
 * ���� �� ��ȭ ����
 */
public class MyListDeleteActivity extends Activity {

	private MovieDBHelper dbHelper;
	private SQLiteDatabase db;

	Boolean isChecked = false;

	Cursor cursor;

	ListView listview; // ����Ʈ ��
	Button selectAllBtn;
	Button deleteBtn;

	Movie movie;
	ArrayList<Movie> movies;
	MyMovieListDeleteAdapter adapter;

	// ����Ʈ�䳻�� item OnClick
	AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {
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

		listview = (ListView) findViewById(R.id.listview);

		movies = new ArrayList<Movie>();
		getCursor();

		//��ü ����/���� ��ư OnClick
		selectAllBtn = (Button) findViewById(R.id.selectAllBtn);
		selectAllBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				isChecked = !isChecked;
				adapter.setAllChecked(isChecked);
				adapter.notifyDataSetChanged();
				listview.invalidate();
			}
		});

		//���� ��ư OnClick
		deleteBtn = (Button) findViewById(R.id.deleteBtn);
		deleteBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				request();
			}
		});
	}

	
	//MyMovieList DB all ����
	public void getCursor() {
		dbHelper = new MovieDBHelper(this);
		db = dbHelper.getWritableDatabase();
		
		cursor = db.rawQuery("SELECT * FROM t_movielist", null);
		if (cursor != null) {
			getMyListInfo(cursor);
			adapter = new MyMovieListDeleteAdapter(MyListDeleteActivity.this,
					R.layout.mylistdelete, movies);
			listview.setOnItemClickListener(mItemClickListener);
			listview.setAdapter(adapter);
		}
		db.close();
	}

	//MyMovieList DB all list �߰�
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

	//MyMovieList DB item���� 
	public void deleteMovieWishList() {
		dbHelper = new MovieDBHelper(this);
		db = dbHelper.getWritableDatabase();

		//DB����
		ArrayList<Integer> isCheckedConfrim = adapter.getChecked();
		for (int i = 0; i < isCheckedConfrim.size(); i++) {
			Movie deleteMovie = movies.get(isCheckedConfrim.get(i));

			String[] actors = deleteMovie.getActor();
			String actor = "";
			for (int j = 0; j < actors.length; j++) {
				actor += ((j < actor.length()) ? "," : "") + actors[j];
			}

			String[] Args = { deleteMovie.getTitle(), actor };
			db.delete("t_movielist", "m_title = ? and m_actor = ?",
					Args);
		}
		db.close();

		//Arraylist���� ����
		for (int i = 0; i < isCheckedConfrim.size(); i++) {
			movies.remove(isCheckedConfrim.get(i));
		}
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

	//�ڷΰ��� 
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent(this, MyMovieListActivity.class);
			startActivity(intent);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

}
