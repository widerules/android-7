package kr.co.WhenWhereWho3;

import java.util.Arrays;

import kr.co.facebook.android.DialogError;
import kr.co.facebook.android.Facebook;
import kr.co.facebook.android.Facebook.DialogListener;
import kr.co.facebook.android.FacebookError;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
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
	private Button mBtnFeed;
	private EditText mEtContent;
	private String mFacebookAccessToken;
	private SharedPreferences pref = null;
	private String returnValue = null;

	Movie movie;
	float rating;

	private final ImageDownloader imageDownloader = new ImageDownloader();

	TextView myTitleTxtVw;
	TextView myWhereTxtVw;
	TextView myGenreTxtVw;
	TextView myOpenInfoTxtVw;
	TextView myActorTxtVw;

	ImageView myThumbnail;
	Button myModifyBtn;
	RatingBar myRatingBar;

	ProgressDialog pd;
	final int FACEBOOK_PROGRESS_DIALOG = 1002;

	Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			switch( msg.what ) {
			case 0 : 
				pd.dismiss();
				Toast.makeText(FaceBookActivity.this, "FaceBook �㺭�� ��Ͽ� �����Ͽ����ϴ�.", Toast.LENGTH_SHORT).show();
				finish();
				break;
			}
		}

	};



	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.facebook);

		//	�ʿ��� ������ ���� �ε�
		myTitleTxtVw 		= (TextView)findViewById(R.id.faceBook_myTitleTxtVw);
		myWhereTxtVw		= (TextView)findViewById(R.id.faceBook_myWhereTxtVw);
		myGenreTxtVw 		= (TextView)findViewById(R.id.faceBook_myGenreTxtVw);
		myOpenInfoTxtVw 	= (TextView)findViewById(R.id.faceBook_myOpenInfoTxtVw);
		myActorTxtVw		= (TextView)findViewById(R.id.faceBook_myActorTxtVw);

		myThumbnail 		= (ImageView)findViewById(R.id.faceBook_myThumbnail);
		myRatingBar			= (RatingBar)findViewById(R.id.faceBook_myRatingBar);

		//	���޹��� ����Ʈ�� �����´�.
		Intent intent = getIntent();       
		//	����Ʈ�� �����ϸ�
		if( intent != null ) {
			//	movie��ü�� �����ͼ� �����͸� �ѷ��ش�.
			Movie movie = (Movie)intent.getSerializableExtra("movie");
			this.movie = movie;
			myTitleTxtVw.setText(movie.getTitle());
			myWhereTxtVw.setText(movie.getWhere());

			rating = ( float )( ( movie.getGrade().equals("") ) ? 0.0 : Float.parseFloat( movie.getGrade() ) )  / ( float )2.0;
			myRatingBar.setRating( rating );
			myGenreTxtVw.setText( "		- �帣 : " + movie.getGenre( ) );
			myActorTxtVw.setText( "		- ��� : " + Arrays.toString( movie.getActor() ) );
			myOpenInfoTxtVw.setText( "		- ������ : " + movie.getOpenInfo() );

			imageDownloader.download( movie.getThumbnail(), myThumbnail );

		}
		
		mEtContent = (EditText) findViewById(R.id.faceBook_etContent);
		mBtnFeed = (Button) findViewById(R.id.faceBook_btnFeed);

		mBtnFeed.setOnClickListener(this);

		mFacebookAccessToken = getAppPreferences(this, "ACCESS_TOKEN");
		if(mFacebookAccessToken != "") {		
			mFacebook.setAccessToken(mFacebookAccessToken);
		} else {
			login();			
		}	
	}


	private void login()
	{
		if (!"".equals(mFacebookAccessToken) && mFacebookAccessToken != null)
			mFacebook.setAccessToken(mFacebookAccessToken);
		else
			mFacebook.authorize2(this, new String[] {"publish_stream, user_photos, email"}, new AuthorizeListener());
	}

	// ��ư�� OnClick �̺�Ʈ ó�� 
	public void onClick(View v)
	{
		switch(v.getId())
		{
		case R.id.faceBook_btnFeed:  // Facebook�� �۾���
			if(mEtContent.getText().toString().trim().equals("")) {
				Toast.makeText(getApplicationContext(), "������ �Է��ϼ���", Toast.LENGTH_SHORT).show();
			} else {
				showDialog(FACEBOOK_PROGRESS_DIALOG);
				Thread t = new Thread() {
					public void run() {
						Looper.prepare();
						feed();
						Looper.loop();
					};
				};
				t.start();				
			}
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



	// ��, ���� ����ϱ�
	private void feed()
	{
		try
		{
			Log.v(C.LOG_TAG, "access token : " + mFacebook.getAccessToken());

			Bundle params = new Bundle();

			String message = "< WhenWhereWith APP���� ���� �ڵ� ��� > \n\n"
					+ "- ��� : " + myWhereTxtVw.getText().toString().trim() + "\n"
					+ "- ���� ���� : " + rating + "/10.0 \n"
					+ myGenreTxtVw.getText().toString().trim() + "\n"
					+ myOpenInfoTxtVw.getText().toString().trim() + "\n"
					+ myActorTxtVw.getText().toString().trim() + "\n\n\n"
					+ "- �ı� : " + mEtContent.getText().toString().trim() + "\n\n";						   

			params.putString("message", message);			
			params.putString("name", myTitleTxtVw.getText().toString().trim());
			params.putString("link", "");
			params.putString("description", "WWW APP �׽�Ʈ��");
			params.putString("picture", movie.getThumbnail());

			mFacebook.request("me/feed", params, "POST");
			handler.sendEmptyMessage(0);

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
			Toast.makeText(getApplicationContext(), "�α׾ƿ� �Ǿ����ϴ�.", Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}



	// Facebook ������ ó���� ���� callback class
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

	//app ����� �����۷����� �� ����
	private void setAppPreferences(Activity context, String key, String value)
	{

		pref = context.getSharedPreferences("FacebookToken", 0);
		SharedPreferences.Editor prefEditor = pref.edit();
		prefEditor.putString(key, value);

		prefEditor.commit();
	}

	// app ����� �����۷������� ���� �о��
	private String getAppPreferences(Activity context, String key)
	{


		pref = context.getSharedPreferences("FacebookToken", 0);

		returnValue = pref.getString(key, "");

		return returnValue;
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch( id ){
		case FACEBOOK_PROGRESS_DIALOG:
			pd = new ProgressDialog(FaceBookActivity.this);
			pd.setMessage("FaceBook�� ������Դϴ�...");
			pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pd.setCancelable(false);
			return pd;
		}
		return super.onCreateDialog(id);
	}



}

