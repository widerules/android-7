package kr.co.WhenWhereWho3;

import java.util.Arrays;

import kr.co.facebook.android.DialogError;
import kr.co.facebook.android.Facebook;
import kr.co.facebook.android.Facebook.DialogListener;
import kr.co.facebook.android.FacebookError;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class FaceBookActivity extends Activity implements View.OnClickListener
{
	private Facebook mFacebook = new Facebook(C.FACEBOOK_APP_ID);
	private Button mBtnFeed, mBtnLogout;
	private EditText mEtContent;
	private String mFacebookAccessToken;
	private SharedPreferences pref = null;
	private String returnValue = null;
	
	Movie movie;

	private final ImageDownloader imageDownloader = new ImageDownloader();

	TextView myTitleTxtVw;
	TextView myWhereTxtVw;
	TextView myGenreTxtVw;
	TextView myOpenInfoTxtVw;
	TextView myActorTxtVw;

	ImageView myThumbnail;
	Button myModifyBtn;
	RatingBar myRatingBar;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.facebook);

		/***********************************************************************/
		//	필요한 위젯들 전부 로딩
		myTitleTxtVw 		= (TextView)findViewById(R.id.myTitleTxtVw);
		myWhereTxtVw		= (TextView)findViewById(R.id.myWhereTxtVw);
		myGenreTxtVw 		= (TextView)findViewById(R.id.myGenreTxtVw);
		myOpenInfoTxtVw 	= (TextView)findViewById(R.id.myOpenInfoTxtVw);
		myActorTxtVw		= (TextView)findViewById(R.id.myActorTxtVw);

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

			myTitleTxtVw.setText(movie.getTitle());
			myWhereTxtVw.setText(movie.getWhere());

			float rating = ( float )( ( movie.getGrade().equals("") ) ? 0.0 : Float.parseFloat( movie.getGrade() ) )  / ( float )2.0;
			myRatingBar.setRating( rating );
			myGenreTxtVw.setText( "		- 장르 : " + movie.getGenre( ) );
			myActorTxtVw.setText( "		- 배우 : " + Arrays.toString( movie.getActor() ) );
			myOpenInfoTxtVw.setText( "		- 개봉일 : " + movie.getOpenInfo() );

			imageDownloader.download( movie.getThumbnail(), myThumbnail );

			mEtContent = (EditText) findViewById(R.id.etContent);


			mBtnFeed = (Button) findViewById(R.id.btnFeed);
			mBtnLogout = (Button) findViewById(R.id.btnLogout);

			mBtnFeed.setOnClickListener(this);
			mBtnLogout.setOnClickListener(this);

			mFacebookAccessToken = getAppPreferences(this, "ACCESS_TOKEN");
			if(mFacebookAccessToken != "") {		
				mFacebook.setAccessToken(mFacebookAccessToken);
			} else {
				login();			
			}	
		}
	}

	private void login()
	{
		if (!"".equals(mFacebookAccessToken) && mFacebookAccessToken != null)
			mFacebook.setAccessToken(mFacebookAccessToken);
		else
			mFacebook.authorize2(this, new String[] {"publish_stream, user_photos, email"}, new AuthorizeListener());
	}

	// 버튼의 OnClick 이벤트 처리 
	public void onClick(View v)
	{
		switch(v.getId())
		{
		case R.id.btnFeed:  // Facebook에 글쓰기
			feed();
			break;
		case R.id.btnLogout: // Facebook logout
			logout();
			break;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == RESULT_OK)
		{
			if (requestCode == C.FACEBOOK_AUTH_CODE)
			{
				mFacebook.authorizeCallback(requestCode, resultCode, data);
			}
		}
		else
		{
			if (requestCode == C.FACEBOOK_AUTH_CODE)
			{
				mFacebook.authorizeCallback(requestCode, resultCode, data);
			}      
		}
	}



	// 글, 사진 등록하기
	private void feed()
	{
		try
		{
			Log.v(C.LOG_TAG, "access token : " + mFacebook.getAccessToken());

			Bundle params = new Bundle();
			params.putString("message", mEtContent.getText().toString());
			params.putString("name", "사용자명");
			params.putString("link", "");
			params.putString("description", "WWW APP TEST를통해 포스트됨.");
			params.putString("picture", "");

			mFacebook.request("me/feed", params, "POST");
			Toast.makeText(FaceBookActivity.this, "feed 성공", Toast.LENGTH_SHORT).show();

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void logout()
	{
		try {
			mFacebook.logout(this);
			setAppPreferences(FaceBookActivity.this, "ACCESS_TOKEN", "");
			Toast.makeText(getApplicationContext(), "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}



	// Facebook 인증후 처리를 위한 callback class
	public class AuthorizeListener implements DialogListener
	{
		public void onCancel()
		{
			// TODO Auto-generated method stub
			if(C.D)Log.v(C.LOG_TAG,"::: onCancel :::");
		}

		public void onComplete(Bundle values)
		{
			// TODO Auto-generated method stub
			if (C.D) Log.v(C.LOG_TAG, "::: onComplete :::");

			mFacebookAccessToken = mFacebook.getAccessToken();
			setAppPreferences(FaceBookActivity.this, "ACCESS_TOKEN", mFacebookAccessToken);      

		}

		public void onError(DialogError e)
		{
			// TODO Auto-generated method stub

		}

		public void onFacebookError(FacebookError e)
		{
			// TODO Auto-generated method stub

		}
	}

	//app 쉐어드 프레퍼런스에 값 저장
	private void setAppPreferences(Activity context, String key, String value)
	{

		pref = context.getSharedPreferences("FacebookToken", 0);
		SharedPreferences.Editor prefEditor = pref.edit();
		prefEditor.putString(key, value);

		prefEditor.commit();
	}

	// app 쉐어드 프레퍼런스에서 값을 읽어옴
	private String getAppPreferences(Activity context, String key)
	{


		pref = context.getSharedPreferences("FacebookToken", 0);

		returnValue = pref.getString(key, "");

		return returnValue;
	}


}

