package kr.co.WhenWhereWho3;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

public class MyMovieListActivity extends TabActivity {
	
	private Parse parse;
	
	TextView wishTxtVw;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mymovielistmain);
        
        parse = new Parse();
        
        TabHost tabHost = getTabHost();
        TabSpec myList = tabHost.newTabSpec("myList");
        myList.setIndicator("내가 본 영화 목록");
        Intent intent = new Intent(getApplicationContext(), MyListActivity.class);
        myList.setContent(intent);
        tabHost.addTab(myList);
        
        TabSpec wishList = tabHost.newTabSpec("wishList");
        wishList.setIndicator("찜한목록");
        wishList.setContent(R.id.wishTxtVw);
        tabHost.addTab(wishList);
        
        wishTxtVw = (TextView)findViewById(R.id.wishTxtVw);
        

    }//	onCreate( ) 끝
}




