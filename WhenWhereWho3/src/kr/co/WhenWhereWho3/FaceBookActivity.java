package kr.co.WhenWhereWho3;

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

public class FaceBookActivity extends Activity implements View.OnClickListener
{
  private Facebook mFacebook = new Facebook(C.FACEBOOK_APP_ID);
  private Button mBtnLogin, mBtnFeed, mBtnLogout;
  private EditText mEtContent;
  private String mFacebookAccessToken;


  
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.facebook);
    
    mEtContent = (EditText) findViewById(R.id.etContent);

    
    mBtnFeed = (Button) findViewById(R.id.btnFeed);
    mBtnLogout = (Button) findViewById(R.id.btnLogout);
    
    mBtnLogin.setOnClickListener(this);
    mBtnFeed.setOnClickListener(this);
    mBtnLogout.setOnClickListener(this);
    
    mFacebookAccessToken = getAppPreferences(this, "ACCESS_TOKEN");
    mFacebook.setAccessToken(mFacebookAccessToken);

    login();
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
      case R.id.btnFeed:  // Facebook�� �۾���
        feed();
        break;
      case R.id.btnLogout: // Facebook logout
        logout();
        break;
      default:
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
  
  

// �� ����ϱ�
  private void feed()
  {
    try
    {
      Log.v(C.LOG_TAG, "access token : " + mFacebook.getAccessToken());
      
      Bundle params = new Bundle();
      params.putString("message", mEtContent.getText().toString());
      params.putString("name", "����ڸ�");
      params.putString("link", "");
      params.putString("description", "WWW APP TEST������ ����Ʈ��.");
      params.putString("picture", "");

      mFacebook.request("me/feed", params, "POST");
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
   SharedPreferences pref = null;
   pref = context.getSharedPreferences("FacebookToken", 0);
   SharedPreferences.Editor prefEditor = pref.edit();
   prefEditor.putString(key, value);
   
   prefEditor.commit();
 }
 
 // app ����� �����۷������� ���� �о��
 private String getAppPreferences(Activity context, String key)
 {
   String returnValue = null;
   
   SharedPreferences pref = null;
   pref = context.getSharedPreferences("FacebookToken", 0);
   
   returnValue = pref.getString(key, "");
   
   return returnValue;
 }


}

