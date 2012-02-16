package kr.co.WhenWhereWho3;

import java.util.ArrayList;
import java.util.Arrays;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class MyMovieListDetailActivity extends Activity {	
	TextView myTitleTxtVw;
	TextView myWhenTxtVw;
	TextView myWhoTxtVw;
	TextView myWhereTxtVw;
	TextView myGenreTxtVw;
	TextView myOpenInfoTxtVw;
	TextView myActorTxtVw;
	TextView myStoryTxtVw;
	TextView myCommentTxtVw;
	
	Movie movie;
	
	ImageView myThumbnail;
	Button myModifyBtn;
	RatingBar myRatingBar;
	
	private final ImageDownloader imageDownloader = new ImageDownloader();
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mymovielistdetail);
        
        /***********************************************************************/
        //	필요한 위젯들 전부 로딩
        myTitleTxtVw 		= (TextView)findViewById(R.id.myTitleTxtVw);
        myWhenTxtVw 		= (TextView)findViewById(R.id.myWhenTxtVw);
        myWhoTxtVw 			= (TextView)findViewById(R.id.myWhoTxtVw);
        myWhereTxtVw		= (TextView)findViewById(R.id.myWhereTxtVw);
        myGenreTxtVw 		= (TextView)findViewById(R.id.myGenreTxtVw);
        myOpenInfoTxtVw 	= (TextView)findViewById(R.id.myOpenInfoTxtVw);
        myActorTxtVw		= (TextView)findViewById(R.id.myActorTxtVw);
        myStoryTxtVw		= (TextView)findViewById(R.id.myStoryTxtVw);
        myCommentTxtVw 		= (TextView)findViewById(R.id.myCommentTxtVw);
        
        myThumbnail 		= (ImageView)findViewById(R.id.myThumbnail);
        myModifyBtn			= (Button)findViewById(R.id.myModifyBtn);
        myRatingBar			= (RatingBar)findViewById(R.id.myRatingBar);
        /***********************************************************************/
        
        
        //	전달받은 인텐트를 가져온다.
        Intent intent = getIntent();

        
        
        
        
        
        //	인텐트가 존재하면
        if( intent != null ) {
        	//	movie객체를 가져와서 데이터를 뿌려준다.
        	Movie movie = (Movie)intent.getSerializableExtra("movie");
        	
        	myWhenTxtVw.setText(movie.getWhen());
        	myWhereTxtVw.setText(movie.getWhere());
        	myWhoTxtVw.setText(movie.getWith());
        	myCommentTxtVw.setText(movie.getComment());
        	
        	myTitleTxtVw.setText(movie.getTitle());
        	float rating = ( float )( ( movie.getGrade().equals("") ) ? 0.0 : Float.parseFloat( movie.getGrade() ) )  / ( float )2.0;
        	myRatingBar.setRating( rating );
        	myGenreTxtVw.setText( "		- 장르 : " + movie.getGenre( ) );
        	myActorTxtVw.setText( "		- 배우 : " + Arrays.toString( movie.getActor() ) );
        	myOpenInfoTxtVw.setText( "		- 개봉일 : " + movie.getOpenInfo() );
        	myStoryTxtVw.setText("		- 줄거리 : " + movie.getStory() );
        	
			imageDownloader.download( movie.getThumbnail(), myThumbnail );

        	
        	Toast.makeText(getApplicationContext(), movie.getTitle(), Toast.LENGTH_SHORT).show();
        }
        
        
        Button facebookBtn = (Button)findViewById(R.id.facebookBtn);
		facebookBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(getApplicationContext(), FaceBookActivity.class);
				intent.putExtra("movie", movie);
				startActivity(intent);
			}
		});
        
        
        //	존재하지 않는다면...? - 2012.02.13 예외처리
        
    }//	onCreate( ) 끝

}

