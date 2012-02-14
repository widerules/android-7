package kr.co.WhenWhereWho3;

import java.util.Arrays;

import kr.co.WhenWhereWho3.RecommendMovieActivity.UserGalleryAdapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class SearchMovieDetailActivity extends Activity {	

	private final ImageDownloader imageDownloader = new ImageDownloader();
	
	TextView movieTitle;
	TextView movieNation;
	
	ImageView movieImg;
	TextView movieDirector;
	TextView movieActors;
	TextView movieGenre;
	TextView movieOpenInfo;
	
	Gallery galleryPhoto;
	TextView movieStory;
	
	String [] photo;
	
	Intent intent;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.moviedetail);	
	
		movieTitle = (TextView)findViewById(R.id.movieTitle);
		movieNation = (TextView)findViewById(R.id.movieNation);
		
		movieImg = (ImageView)findViewById(R.id.movieImg);
		movieDirector = (TextView)findViewById(R.id.movieDirector);
		movieActors = (TextView)findViewById(R.id.movieActors);
		movieGenre = (TextView)findViewById(R.id.movieGenre);
		movieOpenInfo = (TextView)findViewById(R.id.movieOpenInfo);
	
		galleryPhoto = (Gallery)findViewById(R.id.galleryPhoto);
		movieStory = (TextView)findViewById(R.id.movieStory);
		
		Button btnWishList = (Button)findViewById(R.id.btnWishList);
		btnWishList.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		
		Button btnRegistMovie = (Button)findViewById(R.id.btnRegistMovie);
		btnRegistMovie.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				intent = new Intent(SearchMovieDetailActivity.this, RegistMovieActivity.class);
				startActivity(intent);
			}
		});
		
		Intent intent = getIntent();
		
		if(intent != null) {
			Movie movie = (Movie)intent.getSerializableExtra("movie");
			
			movieTitle.setText(movie.getTitle());
			movieNation.setText("(" + movie.getNation() + ")");
			movieDirector.setText("감독 : " + movie.getDirector());
			movieActors.setText("배우 : " + Arrays.toString(movie.getActor()));
			movieGenre.setText("장르 : " + movie.getGenre());
			movieOpenInfo.setText("개봉일 : " + movie.getOpenInfo());
			movieStory.setText("줄거리 : " + movie.getStory());
			
			imageDownloader.download(movie.getThumbnail(), movieImg);
			photo = movie.getPhoto();
			
			UserGalleryAdapter adapter = new UserGalleryAdapter(this);
			galleryPhoto.setAdapter(adapter);
		}
		
		galleryPhoto.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView parent, View v, int position, long id) {
				
				Intent dialog = new Intent(SearchMovieDetailActivity.this, ShowMoviePhotoDialogActivity.class);
				dialog.putExtra("photo", photo[position]);
				startActivity(dialog);
			}
		});
	}
	
	public class UserGalleryAdapter extends BaseAdapter {
		private Context context;
		private int galleryItemBackground;

		public UserGalleryAdapter(Context context) {
			this.context = context;

			TypedArray a = obtainStyledAttributes(R.styleable.BasicGallery);
			// 
			galleryItemBackground = a.getResourceId(
					R.styleable.BasicGallery_android_galleryItemBackground, 0);

			// 백그라운드 배경을 얻기위해 얻어온 자원을 해제
			a.recycle();
		}
		public int getCount() {
			return photo.length;
		}

		public Object getItem(int position) {
			return photo[position];
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ImageView view = null;
			// 재사용의 의미	
			if(convertView != null) {
				view = (ImageView)convertView;
			}
			else {
				view = new ImageView(context);
			}
			view.setLayoutParams(new Gallery.LayoutParams(120, 100));

			// 이미지뷰에 백그라운드 배경을 설정한다.
			view.setBackgroundResource(galleryItemBackground);
			view.setScaleType(ImageView.ScaleType.FIT_CENTER);
			//view.setImageResource(photo[position]);
			imageDownloader.download(photo[position], view);
			
			return view;
		}
	}

}

