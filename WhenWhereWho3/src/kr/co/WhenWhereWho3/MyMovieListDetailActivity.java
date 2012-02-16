package kr.co.WhenWhereWho3;

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
	TextView myWithTxtVw;
	TextView myWhereTxtVw;
	TextView myGenreTxtVw;
	TextView myOpenInfoTxtVw;
	TextView myDirectorTxtVw;
	TextView myActorTxtVw;
	TextView myStoryTxtVw;
	TextView myCommentTxtVw;
	
	ImageView myThumbnail;
	Button myUpdateBtn;
	RatingBar myRatingBar;
	
	private final ImageDownloader imageDownloader = new ImageDownloader();
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mymovielistdetail);
        
        /***********************************************************************/
        //	�ʿ��� ������ ���� �ε�
        myTitleTxtVw 		= (TextView)findViewById(R.id.myMovieListDetail_movieTitle);
        myWhenTxtVw 		= (TextView)findViewById(R.id.myMovieListDetail_myWhenTxtVw);
        myWithTxtVw 			= (TextView)findViewById(R.id.myMovieListDetail_myWithTxtVw);
        myWhereTxtVw		= (TextView)findViewById(R.id.myMovieListDetail_myWhereTxtVw);
        myGenreTxtVw 		= (TextView)findViewById(R.id.myMovieListDetail_myGenreTxtVw);
        myOpenInfoTxtVw 	= (TextView)findViewById(R.id.myMovieListDetail_myOpenInfoTxtVw);
        myDirectorTxtVw     = (TextView)findViewById(R.id.myMovieListDetail_myDirectorTxtVw);
        myActorTxtVw		= (TextView)findViewById(R.id.myMovieListDetail_myActorTxtVw);
        myStoryTxtVw		= (TextView)findViewById(R.id.myMovieListDetail_myStoryTxtVw);
        myCommentTxtVw 		= (TextView)findViewById(R.id.myMovieListDetail_myCommentTxtVw);
        
        myThumbnail 		= (ImageView)findViewById(R.id.myMovieListDetail_myThumbnail);
        myUpdateBtn			= (Button)findViewById(R.id.myMovieListDetail_myUpdateBtn);
        myRatingBar			= (RatingBar)findViewById(R.id.myMovieListDetail_myRatingBar);
        /***********************************************************************/
        
        
        
        //	���޹��� ����Ʈ�� �����´�.
        Intent intent = getIntent();

        
        
        
        
        
        //	����Ʈ�� �����ϸ�
        if( intent != null ) {
        	//	movie��ü�� �����ͼ� �����͸� �ѷ��ش�.
        	Movie movie = (Movie)intent.getSerializableExtra("movie");
        	
        	myWhenTxtVw.setText(movie.getWhen());
        	myWhereTxtVw.setText(movie.getWhere());
        	myWithTxtVw.setText(movie.getWith());
        	myCommentTxtVw.setText(movie.getComment());
        	
        	myTitleTxtVw.setText(movie.getTitle());
        	float rating = ( float )( ( movie.getGrade().equals("") ) ? 0.0 : Float.parseFloat( movie.getGrade() ) )  / ( float )2.0;
        	myRatingBar.setRating( rating );
        	myGenreTxtVw.setText( "�� �帣 : " + movie.getGenre( ) );
        	myDirectorTxtVw.setText( "��  ���� : " + movie.getDirector() );
        	myActorTxtVw.setText( "��  ��� : " + Arrays.toString( movie.getActor() ) );
        	myOpenInfoTxtVw.setText( "��  ������ : " + movie.getOpenInfo() );
        	myStoryTxtVw.setText(movie.getStory() );
        	
			imageDownloader.download( movie.getThumbnail(), myThumbnail );

        	
        	Toast.makeText(getApplicationContext(), movie.getTitle(), Toast.LENGTH_SHORT).show();
        }
        
        Button facebookBtn = (Button)findViewById(R.id.myMovieListDetail_facebookBtn);
		facebookBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(getApplicationContext(), FaceBookActivity.class);
				startActivity(intent);
			}
		});
        
        
        //	�������� �ʴ´ٸ�...? - 2012.02.13 ����ó��
        
    }//	onCreate( ) ��

}

