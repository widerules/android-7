package kr.co.WhenWhereWho3;

import java.util.ArrayList;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

public class MyWishListActivity extends Activity {

	private MovieDBHelper dbHelper;
	private SQLiteDatabase db;
	
	Movie movie;
	ArrayList<Movie> movies;
	
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
        	ListView listview = (ListView)findViewById(R.id.listview);
        	getMyWishListInfo(cursor);
        	MovieListAdapter adapter = new MovieListAdapter(MyWishListActivity.this, R.layout.searchlist, movies);
        	listview.setAdapter(adapter);
        }
    }
    
    public void getMyWishListInfo(Cursor outCursor) {
    	int recordCnt = outCursor.getCount();
    	
    	String[] photo_Col = {"m_photo_1", "m_photo_2", "m_photo_3", "m_photo_4", "m_photo_5"};
    	String[] actors = null;
    	String[] photos = null;
    	
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
    	
    	for(int i=0; i<recordCnt; i++) {
    		outCursor.moveToNext();
    		
    		movie = new Movie();

    		movie.setTitle(outCursor.getString(m_titleCol));   
    		movie.setThumbnail(outCursor.getString(m_thumbnailCol));
    		movie.setNation(outCursor.getString(m_nationCol));
    		movie.setDirector(outCursor.getString(m_directorCol));
    		//movie.setActor();
    		movie.setGenre(outCursor.getString(m_genreCol));
    		movie.setOpenInfo(outCursor.getString(m_open_infoCol));
    		movie.setGrade(outCursor.getString(m_gradeCol));
    		//movie.setPhoto();
    		
    		for(int j=0; j>5; j++) {
    			
    		}
    		movie.setStory(outCursor.getString(m_storyCol));
    				
    		movies.add(movie);
    	
    	}
    }
}




