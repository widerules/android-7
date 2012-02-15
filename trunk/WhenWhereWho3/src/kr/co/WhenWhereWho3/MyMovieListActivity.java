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
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mymovielistmain);
        
        
        TabHost tabHost = getTabHost();
        TabSpec myList = tabHost.newTabSpec("myList");
        TabSpec wishList = tabHost.newTabSpec("wishList");

        myList.setIndicator("���� �� ��ȭ ���").setContent(new Intent(this, MyListActivity.class));
        wishList.setIndicator("���Ѹ��").setContent(new Intent(this, MyWishListActivity.class));
        
        tabHost.addTab(myList);        
        tabHost.addTab(wishList);
        
        
    }//	onCreate( ) ��
}



