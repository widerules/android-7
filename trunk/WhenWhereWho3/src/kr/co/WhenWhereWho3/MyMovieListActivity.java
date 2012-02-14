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
        myList.setIndicator("���� �� ��ȭ ���");
        Intent intent = new Intent(getApplicationContext(), MyListActivity.class);
        myList.setContent(intent);
        tabHost.addTab(myList);
        
        TabSpec wishList = tabHost.newTabSpec("wishList");
        wishList.setIndicator("���Ѹ��");
        wishList.setContent(R.id.wishTxtVw);
        tabHost.addTab(wishList);
        
        wishTxtVw = (TextView)findViewById(R.id.wishTxtVw);
        
        /**********************************************************/
        //	�׽�Ʈ*****�׽�Ʈ*****�׽�Ʈ*****�׽�Ʈ*****�׽�Ʈ*****�׽�Ʈ
        //	���߿��� �Է¼������� �ٲ��ߵ�
        URL url;
		try {
			ArrayList<String> titleList = new ArrayList<String>();
			url = new URL(
				"http://search.naver.com/search.naver?ie=utf8&sm=tab_txc&where=nexearch&query="
				+	"%EC%98%81%EA%B5%AD%20%EC%95%A1%EC%85%98%20%EC%98%81%ED%99%94");
			InputStreamReader isr = new InputStreamReader( url.openConnection().getInputStream(), "UTF-8" );
			BufferedReader br = new BufferedReader( isr );
			
			titleList = parse.htmlParse(br);
			for( String title : titleList ){
				wishTxtVw.append("���� : " + title + "\n" );
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		/**********************************************************/
    }//	onCreate( ) ��
}




