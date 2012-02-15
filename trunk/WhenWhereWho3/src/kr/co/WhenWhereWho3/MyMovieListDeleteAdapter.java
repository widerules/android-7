package kr.co.WhenWhereWho3;

import java.util.ArrayList;

import android.R.integer;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public class MyMovieListDeleteAdapter extends ArrayAdapter<Movie> {
	ArrayList<Movie> movies = null;
	LayoutInflater li;
	boolean[] isCheckedConfrim;

	// private final ImageDownloader imageDownloader = new ImageDownloader();

	public MyMovieListDeleteAdapter(Context context, int textViewResourceId,
			ArrayList<Movie> movies) {
		super(context, textViewResourceId, movies);

		this.movies = movies;
		Log.d("MovieAdapter", "������ ȣ��, ����Ʈ ��ü ������ : " + movies.size());
		// ����Ʈ �並 ���� ���� ���÷����� ��ȯ!!
		li = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.isCheckedConfrim = new boolean[movies.size()];
	}

	// CheckBox�� ��� �����ϴ� �޼���
	public void setAllChecked(boolean ischeked) {
		int tempSize = isCheckedConfrim.length;
		for (int a = 0; a < tempSize; a++) {
			isCheckedConfrim[a] = ischeked;
		}
	}

	public void setChecked(int position) {
		isCheckedConfrim[position] = !isCheckedConfrim[position];
	}

	public ArrayList<Integer> getChecked() {
		int tempSize = isCheckedConfrim.length;
		ArrayList<Integer> mArrayList = new ArrayList<Integer>();
		for (int b = 0; b < tempSize; b++) {
			if (isCheckedConfrim[b]) {
				mArrayList.add(b);
			}
		}
		return mArrayList;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		Log.d("MovieAdapter", "getViewȣ��");
		View v = convertView;
		if (v == null) {
			v = li.inflate(R.layout.mylistdelete, null);
		}
		Movie m = movies.get(position);
		try {
			CheckBox checkDelete = (CheckBox) v.findViewById(R.id.checkDelete);
			TextView title = (TextView) v.findViewById(R.id.titleTxtVw);
			TextView when = (TextView) v.findViewById(R.id.whenTxtVw);
			TextView with = (TextView) v.findViewById(R.id.withTxtVw);
			RatingBar ratingBar = (RatingBar) v.findViewById(R.id.ratingBar);

			
			checkDelete.setClickable(false);
			checkDelete.setFocusable(false);
			checkDelete.setChecked(isCheckedConfrim[position]);

			/********************************************************/
			// ��ȭ title ��������
			title.setText(m.getTitle());
			Log.d("MovieAdapter", "��ȭ ���� : " + m.getTitle());
			/********************************************************/

			/********************************************************/
			// ��ȭ�� ���� �ô°�? ��������
			when.setText(m.getOpenInfo());
			Log.d("MovieAdapter", "������ : " + m.getOpenInfo());
			/********************************************************/

			/********************************************************/
			// ��ȭ�� ������ �ô°�? ��������
			// �ϴ� �ӽ÷� �帣 ������- 2012.02.12
			with.setText(m.getGenre());
			Log.d("MovieAdapter", "�帣 : " + m.getGenre());
			/********************************************************/

			/********************************************************/
			// ���� �� ��������
			float rating = (float) ((m.getGrade().equals("")) ? 0.0 : Float
					.parseFloat(m.getGrade())) / (float) 2.0;
			ratingBar.setRating(rating);
			Log.d("MovieAdapter", "���� : " + m.getGrade());
			/********************************************************/

			return v;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
