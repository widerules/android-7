package kr.co.WhenWhereWho3;

import java.util.ArrayList;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

public class MyWishListActivity extends Activity {

	private MovieDBHelper dbHelper;
	private SQLiteDatabase db;
	
	Movie movie;
	int deleteMoviePosition;
	ArrayList<Movie> movies;
	
	ListView listview;
	MovieListAdapter adapter;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); 
        setContentView(R.layout.mywishlist);
        
        dbHelper = new MovieDBHelper(this);
        db = dbHelper.getWritableDatabase();
        movies = new ArrayList<Movie>();
        
        Cursor cursor = db.rawQuery("SELECT * FROM t_wishlist", null);

        if(cursor!=null) {
        	listview = (ListView)findViewById(R.id.listview);
        	getMyWishListInfo(cursor);
        	adapter = new MovieListAdapter(MyWishListActivity.this, R.layout.searchlist, movies);
        	listview.setAdapter(adapter);
        }
        
        listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(), SearchMovieDetailActivity.class);
				intent.putExtra("movie", movies.get(position));
				startActivity(intent);
			}
		});
        
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
    
    public void getMyWishListInfo(Cursor outCursor) {
    	int recordCnt = outCursor.getCount();
    	int i = 0;
    	
    	String photo[] = null;
    	    	
    	int m_titleCol = outCursor.getColumnIndex("m_title");
    	int m_thumbnailCol = outCursor.getColumnIndex("m_thumbnail");
    	int m_nationCol = outCursor.getColumnIndex("m_nation");
    	int m_directorCol = outCursor.getColumnIndex("m_director");
    	int m_actorCol = outCursor.getColumnIndex("m_actor");
    	int m_genreCol = outCursor.getColumnIndex("m_genre");
    	int m_open_infoCol = outCursor.getColumnIndex("m_open_info");
    	int m_gradeCol = outCursor.getColumnIndex("m_grade");
    	int m_photo_1Col = outCursor.getColumnIndex("m_photo_1");
    	int m_photo_2Col = outCursor.getColumnIndex("m_photo_2");
    	int m_photo_3Col = outCursor.getColumnIndex("m_photo_3");
    	int m_photo_4Col = outCursor.getColumnIndex("m_photo_4");
    	int m_photo_5Col = outCursor.getColumnIndex("m_photo_5");
    	int m_storyCol = outCursor.getColumnIndex("m_story");
    	
    	for(i=0; i<recordCnt; i++) {
    		outCursor.moveToNext();
    		
    		movie = new Movie();
    		photo = new String[5];
    		
    		movie.setTitle(outCursor.getString(m_titleCol));   
    		movie.setThumbnail(outCursor.getString(m_thumbnailCol));
    		movie.setNation(outCursor.getString(m_nationCol));
    		movie.setDirector(outCursor.getString(m_directorCol));
    		movie.setActor(outCursor.getString(m_actorCol).split(","));
    		
    		
    		movie.setGenre(outCursor.getString(m_genreCol));
    		movie.setOpenInfo(outCursor.getString(m_open_infoCol));
    		movie.setGrade(outCursor.getString(m_gradeCol));

    		photo[0] = outCursor.getString(m_photo_1Col);
    		photo[1] = outCursor.getString(m_photo_2Col);
    		photo[2] = outCursor.getString(m_photo_3Col);
    		photo[3] = outCursor.getString(m_photo_4Col);
    		photo[4] = outCursor.getString(m_photo_5Col);
    		
    		Log.e("wish",photo[0].toString());
    		movie.setPhoto(photo);	
    		movie.setStory(outCursor.getString(m_storyCol));
    				
    		movies.add(movie);
    	
    	}
    	db.close();
    }
    
    public void deleteMovieWishList() {
		dbHelper = new MovieDBHelper(this);
		db = dbHelper.getWritableDatabase();
		
		Movie deleteMovie = movies.get(deleteMoviePosition);
		String[] actors = deleteMovie.getActor();
		String actor = "";
		
		for(int i=0; i<actors.length; i++) {
			actor += ((i < actor.length()) ? "," : "") + actors[i];
		}
		
		String[] Args = {deleteMovie.getTitle(), actor};
		int recordCnt = db.delete("t_wishlist", "m_title = ? and m_actor = ?", Args);
		
		if(recordCnt == 1) {
			Toast.makeText(getApplicationContext(), "삭제되었습니다.", Toast.LENGTH_SHORT).show();
		}
		
		db.close();
	}
    
    public void request() {
		String title = "삭제";
		String message = "삭제 하시겠습니까?";
		String titleButtonYes = "예";
		String titleButtonNo = "아니오";
		
		AlertDialog dialog = makeRequestDialog(title, message, titleButtonYes, titleButtonNo);
		dialog.show();
	}
	
	private AlertDialog makeRequestDialog(CharSequence title, CharSequence message, 
			CharSequence titleButtonYes, CharSequence titleButtonNo ) {
		
		AlertDialog.Builder requestDialog = new AlertDialog.Builder(this);
		requestDialog.setTitle(title);
		requestDialog.setMessage(message);
		requestDialog.setPositiveButton(titleButtonYes, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				deleteMovieWishList();
				movies.remove(deleteMoviePosition);
				adapter.notifyDataSetChanged();
			}
		});
		requestDialog.setNegativeButton(titleButtonNo, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
			}
		});
		
		return requestDialog.show();
	}
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Log.e("Back", "bac");
		if(keyCode == KeyEvent.KEYCODE_BACK) {
			Log.e("Back", "bac");
			Intent intent = new Intent(this, WhenWhereWho3Activity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP & Intent.FLAG_ACTIVITY_SINGLE_TOP);
			startActivity(intent);
		}
		
		return super.onKeyDown(keyCode, event);
	}
}




