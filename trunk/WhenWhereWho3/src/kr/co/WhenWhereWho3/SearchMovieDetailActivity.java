package kr.co.WhenWhereWho3;

import java.util.Arrays;

import kr.co.facebook.android.Facebook;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/*
 * �˻��� ��ȭ ��� Item�� ���� �� ���� 
 */
public class SearchMovieDetailActivity extends Activity {	

	private final ImageDownloader imageDownloader = new ImageDownloader();
	 private Facebook mFacebook = new Facebook(C.FACEBOOK_APP_ID);
	 
	//DB��� - ���Ͽ� ����ϱ� ����
	private MovieDBHelper dbHelper;
	private SQLiteDatabase db;
	
	Movie movie;
	
	TextView movieTitle;
	TextView movieNation;
	ImageView movieImg;
	TextView movieDirector;
	TextView movieActors;
	TextView movieGenre;
	TextView movieOpenInfo;
	TextView movieGrade;
	Gallery galleryPhoto;
	TextView movieStory;
	String [] photo;
	
	
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
		movieGrade = (TextView)findViewById(R.id.movieGrade);
	
		galleryPhoto = (Gallery)findViewById(R.id.galleryPhoto);
		movieStory = (TextView)findViewById(R.id.movieStory);
		
		
		//�� ��ư onClick ������
		Button btnWishList = (Button)findViewById(R.id.btnWishList);
		btnWishList.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				request();
			}
		});
		//��� ��ư onClick ������
		Button btnRegistMovie = (Button)findViewById(R.id.btnRegistMovie);
		btnRegistMovie.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				if(insertMovieList() == 0) {
					Intent intent = new Intent(SearchMovieDetailActivity.this, RegistMovieActivity.class);
					intent.putExtra("movie", movie);
					startActivity(intent);
				} else {
					Toast.makeText(getApplicationContext(), "���� ��ȭ��Ͽ� �̹� ��ϵǾ� �ֽ��ϴ�", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		//�Ѱܹ��� intent���� Movie���� set
		Intent getintent = getIntent();
		if(getintent != null) {
			movie = (Movie)getintent.getSerializableExtra("movie");
			
			movieTitle.setText(movie.getTitle());
			movieNation.setText("�� ���� : " + movie.getNation());
			movieDirector.setText("�� ���� : " + movie.getDirector());
			movieActors.setText("�� ��� : \n" + Arrays.toString(movie.getActor()));
			movieGenre.setText("�� �帣 : " + movie.getGenre());
			movieOpenInfo.setText("�� ������ : " + movie.getOpenInfo());
			movieGrade.setText(movie.getGrade() + "/10.0");
			movieStory.setText(movie.getStory());
			
			imageDownloader.download(movie.getThumbnail(), movieImg);
			photo = movie.getPhoto();
			UserGalleryAdapter adapter = new UserGalleryAdapter(this);
			galleryPhoto.setAdapter(adapter);
		}
		
		//�������� item onClick ������
		galleryPhoto.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView parent, View v, int position, long id) {
				
				//���̾�α� â���� ������ ���� �����
				Intent dialog = new Intent(SearchMovieDetailActivity.this, ShowMoviePhotoDialogActivity.class);
				dialog.putExtra("photo", photo[position]);
				startActivity(dialog);
			}
		});
		
		Button facebookBtn = (Button)findViewById(R.id.facebookBtn);
		facebookBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
			}
		});
	}
	
	//��DB�� Movie���� �ߺ� Check �� ����
	public void insertMovieWishList() {
		dbHelper = new MovieDBHelper(this);
		db = dbHelper.getWritableDatabase();
		
		ContentValues recordValues = new ContentValues();
		
		String[] photo = movie.getPhoto();
		String[] actors = movie.getActor();
		String actor = "";
		for(int i=0; i<actors.length; i++) {
			actor += ((i < actor.length()) ? "," : "") + actors[i];
		}
		
		//DB���� �ߺ� Check
		String SQL = "select * from t_wishlist " +
						"where m_title = ? and m_actor = ?";
		String[] args = {movie.getTitle(), actor};
		Cursor c1 = db.rawQuery(SQL, args);
		
		int recordCnt = c1.getCount();
		if(recordCnt == 0) {//��DB�� ���ٸ� insert
			recordValues.put("m_title", movie.getTitle());
			recordValues.put("m_thumbnail", movie.getThumbnail());
			recordValues.put("m_nation", movie.getNation());
			recordValues.put("m_director", movie.getDirector());
			recordValues.put("m_actor", actor);
			recordValues.put("m_genre", movie.getGenre());
			recordValues.put("m_open_info", movie.getOpenInfo());
			recordValues.put("m_grade", movie.getGrade());
			recordValues.put("m_photo_1", photo[0]);
			recordValues.put("m_photo_2", photo[1]);
			recordValues.put("m_photo_3", photo[2]);
			recordValues.put("m_photo_4", photo[3]);
			recordValues.put("m_photo_5", photo[4]);
			recordValues.put("m_story", movie.getStory());
			
			db.insert("t_wishlist", null, recordValues) ;
			Toast.makeText(getApplicationContext(), "���Ͽ� ��ϵǾ����ϴ�.", Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(getApplicationContext(), "���Ͽ� �̹� ��ϵǾ� �ֽ��ϴ�", Toast.LENGTH_SHORT).show();
		}
		db.close();
	}
	
	//MY��ȭ List DB�� Movie���� �ߺ� Check
	public int insertMovieList() {
		dbHelper = new MovieDBHelper(this);
		db = dbHelper.getWritableDatabase();
				
		String[] actors = movie.getActor();
		String actor = "";
		for(int i=0; i<actors.length; i++) {
			actor += ((i < actor.length()) ? "," : "") + actors[i];
		}
		
		//DB���� �ߺ� Check
		String SQL = "select * from t_movielist " +
						"where m_title = ? and m_actor = ?";
		String[] args = {movie.getTitle(), actor};
		Cursor c1 = db.rawQuery(SQL, args);
		int recordCnt = c1.getCount();
		db.close();
		
		return recordCnt;
	}
	
	//���� Dialogâ
	public void request() {
		String title = "���ϱ�";
		String message = "�� �Ͻðڽ��ϱ�?";
		String titleButtonYes = "��";
		String titleButtonNo = "�ƴϿ�";
		
		AlertDialog dialog = makeRequestDialog(title, message, titleButtonYes, titleButtonNo);
		dialog.show();
	}
	
	private AlertDialog makeRequestDialog(CharSequence title, CharSequence message, 
			CharSequence titleButtonYes, CharSequence titleButtonNo ) {
		
		AlertDialog.Builder requestDialog = new AlertDialog.Builder(this);
		requestDialog.setTitle(title);
		requestDialog.setMessage(message);
		
		//��ư OnClick ������
		requestDialog.setPositiveButton(titleButtonYes, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				insertMovieWishList();
			}
		});
		requestDialog.setNegativeButton(titleButtonNo, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		
		return requestDialog.show();
	}
	
	//photo GalleryAdapter
	public class UserGalleryAdapter extends BaseAdapter {
		private Context context;
		private int galleryItemBackground;

		public UserGalleryAdapter(Context context) {
			this.context = context;

			TypedArray a = obtainStyledAttributes(R.styleable.BasicGallery);
			galleryItemBackground = a.getResourceId(
					R.styleable.BasicGallery_android_galleryItemBackground, 0);

			// ��׶��� ����� ������� ���� �ڿ��� ����
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

		// Gallery item���� ȣ��
		public View getView(int position, View convertView, ViewGroup parent) {
			ImageView view = null;
			if(convertView != null) {
				view = (ImageView)convertView;
			} else {
				view = new ImageView(context);
			}
			view.setLayoutParams(new Gallery.LayoutParams(120, 100));

			// �̹����信 ��׶��� ����� �����Ѵ�.
			view.setBackgroundResource(galleryItemBackground);
			view.setScaleType(ImageView.ScaleType.FIT_CENTER);
			//view.setImageResource(photo[position]);
			imageDownloader.download(photo[position], view);
			
			return view;
		}
	}
}