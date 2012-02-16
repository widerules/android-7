package kr.co.WhenWhereWho3;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

/*
 * 영화list
 */
public class MyMovieListActivity extends TabActivity {
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mymovielistmain);
        
        //탭화면
        TabHost tabHost = getTabHost();
        TabSpec myList = tabHost.newTabSpec("myList");
        TabSpec wishList = tabHost.newTabSpec("wishList");

        myList.setIndicator("내가 본 영화 목록").setContent(new Intent(this, MyListActivity.class));
        wishList.setIndicator("찜 목록").setContent(new Intent(this, MyWishListActivity.class));
        
        tabHost.addTab(myList);        
        tabHost.addTab(wishList);
    }//	onCreate( ) 끝


    
    
}




