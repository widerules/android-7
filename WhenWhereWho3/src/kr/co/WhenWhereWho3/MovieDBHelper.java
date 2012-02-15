package kr.co.WhenWhereWho3;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MovieDBHelper extends SQLiteOpenHelper {

	String[] tableNames = { "t_movielist", "t_wishlist" };
	
	String t_movieListSql = "CREATE TABLE " + tableNames[0] + " ( "
			+ " _id integer PRIMARY KEY autoincrement, "
			+ " m_title TEXT, "
			+ " m_when TEXT, "
			+ " m_where TEXT, "
			+ " m_with TEXT, "
			+ " m_thumbnail TEXT, "
			//+ " m_thumbnail_binary BINARY, "
			+ " m_nation TEXT, "
			+ " m_director TEXT, "
			+ " m_actor TEXT, "
			+ " m_genre TEXT, "
			+ " m_open_info TEXT, "
			+ " m_grade TEXT, "
			+ " m_story TEXT, "
			+ " m_comment TEXT);";
	
	String t_wishListSql = "CREATE TABLE " + tableNames[1] + " ( "
			+ " _id integer PRIMARY KEY autoincrement, "
			+ " m_title TEXT, "
			+ " m_thumbnail TEXT, "
			+ " m_nation TEXT, "
			+ " m_director TEXT, "
			+ " m_actor TEXT, "
			+ " m_genre TEXT, "
			+ " m_open_info TEXT, "
			+ " m_grade TEXT, "
			+ " m_photo_1 TEXT, "
			+ " m_photo_2 TEXT, "
			+ " m_photo_3 TEXT, "
			+ " m_photo_4 TEXT, "
			+ " m_photo_5 TEXT, "
			+ " m_story TEXT );";
			
			
	public MovieDBHelper(Context context) {
		//DB¿Ã∏ß - WWW3
		super(context, "WWW3.db", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		try {			
			db.execSQL(t_movieListSql);	
		} catch (Exception e) {
			Log.e("TABLE CREATE", "Exception in create_movielist");
		} 
		
		try {			
			db.execSQL(t_wishListSql);	
		} catch (Exception e) {
			Log.e("TABLE CREATE", "Exception in create_wishlist");
		} 
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS t_movielist");
		db.execSQL("DROP TABLE IF EXISTS t_wishlist");
		onCreate(db);		
	}

}