package kr.co.WhenWhereWho3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class WhenWhereWho3Activity extends Activity {
	
	int flag = 0;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
    
    
    public void execute(View view) {
    	Intent intent = null;
    	switch (view.getId()) {
			case R.id.btnList:
				intent = new Intent(this,MyMovieListActivity.class);
				break;
			case R.id.btnSearch:
				if(flag == 0) {
					intent = new Intent(this,SearchMovieListActivity.class);
				} else if(flag == 1) {
					intent = new Intent(this,SearchMovieGridActivity.class);
				}
				break;
			case R.id.btnRecommend:
				intent = new Intent(this,RecommendMovieActivity.class);
				break;
			case R.id.btnSetting:
				intent = new Intent(this,SetupActivity.class);
				break;
			case R.id.btnCinema:
				
				break;
				
				
				
		}
    	startActivity(intent);
    }
}