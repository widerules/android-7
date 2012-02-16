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
        //	�ʿ��� ������ ���� �ε�
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
        
        
        //	���޹��� ����Ʈ�� �����´�.
        Intent intent = getIntent();

        
        
        
        
        
        //	����Ʈ�� �����ϸ�
        if( intent != null ) {
        	//	movie��ü�� �����ͼ� �����͸� �ѷ��ش�.
        	Movie movie = (Movie)intent.getSerializableExtra("movie");
        	
        	myWhenTxtVw.setText(movie.getWhen());
        	myWhereTxtVw.setText(movie.getWhere());
        	myWhoTxtVw.setText(movie.getWith());
        	myCommentTxtVw.setText(movie.getComment());
        	
        	myTitleTxtVw.setText(movie.getTitle());
        	float rating = ( float )( ( movie.getGrade().equals("") ) ? 0.0 : Float.parseFloat( movie.getGrade() ) )  / ( float )2.0;
        	myRatingBar.setRating( rating );
        	myGenreTxtVw.setText( "		- �帣 : " + movie.getGenre( ) );
        	myActorTxtVw.setText( "		- ��� : " + Arrays.toString( movie.getActor() ) );
        	myOpenInfoTxtVw.setText( "		- ������ : " + movie.getOpenInfo() );
        	myStoryTxtVw.setText("		- �ٰŸ� : " + movie.getStory() );
        	
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
        
        
        //	�������� �ʴ´ٸ�...? - 2012.02.13 ����ó��
        
    }//	onCreate( ) ��

}

