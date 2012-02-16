package kr.co.WhenWhereWho3;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources.Theme;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class ShowMoviePhotoDialogActivity extends Activity {	

	private final ImageDownloader imageDownloader = new ImageDownloader();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.showmoviephoto);
		
		Intent intent = getIntent();
		
		ImageView photo = (ImageView)findViewById(R.id.showMoviePhoto_photo);
		imageDownloader.download(intent.getStringExtra("photo"), photo);
		
		photo.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
	}

	@Override
	protected void onApplyThemeResource(Theme theme, int resid, boolean first) {
		// TODO Auto-generated method stub
		super.onApplyThemeResource(theme, resid, first);
		//theme.applyStyle(android.R.style.Theme_Panel, true);
	};

}

