package kr.co.WhenWhereWho3;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MyListActivity extends Activity {
	
	private MovieDBHelper dbHelper;
	private SQLiteDatabase db;
	
	ListView listview;					//	리스트 뷰
	
	Movie movie;						//	쓰레드에서 사용할 객체
	ArrayList<Movie> movies;
	MyMovieListAdapter adapter;				//	MovieAdapter
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mymovielist);
        
        dbHelper = new MovieDBHelper(this);
        db = dbHelper.getWritableDatabase();
        movies = new ArrayList<Movie>();
        
        Cursor cursor = db.rawQuery("SELECT * FROM t_movielist", null);

        if(cursor!=null) {
        	listview = (ListView)findViewById(R.id.listview);
        	getMyListInfo(cursor);
        	MyMovieListAdapter adapter = new MyMovieListAdapter(MyListActivity.this, R.layout.mylist, movies);
        	listview.setAdapter(adapter);
        }
        
        
        listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(), MyMovieListDetailActivity.class);
				intent.putExtra("movie", movies.get(position));
				startActivity(intent);
			}
		});
        
    }//	onCreate( ) 끝   
    
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
    	
    	for(i=0; i<recordCnt; i++) {
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
    }

}




