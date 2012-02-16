package kr.co.WhenWhereWho3;

import java.util.ArrayList;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.RatingBar;
import android.widget.TextView;

public class MyMovieListDeleteAdapter extends ArrayAdapter<Movie> {
	
	boolean[] isCheckedConfrim;

	ArrayList<Movie> movies = null;
	LayoutInflater li;

	public MyMovieListDeleteAdapter(Context context, int textViewResourceId,
			ArrayList<Movie> movies) {
		super(context, textViewResourceId, movies);
		li = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.movies = movies;
		this.isCheckedConfrim = new boolean[movies.size()];
	}

	// CheckBox�� ��� �����ϴ� �޼���
	public void setAllChecked(boolean ischeked) {
		int tempSize = isCheckedConfrim.length;
		for (int a = 0; a < tempSize; a++) {
			isCheckedConfrim[a] = ischeked;
		}
	}
	
	//checkBox check���� ��
	public void setChecked(int position) {
		isCheckedConfrim[position] = !isCheckedConfrim[position];
	}

	//check �� list index ����
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

	// list item���� ȣ��
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		Log.d("MyMovieListDeleteAdapter", "getViewȣ��");
		View v = convertView;
		if (v == null) {
			v = li.inflate(R.layout.mylistdelete, null);
		}
		
		Movie m = movies.get(position);
		try {
			CheckBox checkDelete = (CheckBox) v.findViewById(R.id.delete_checkDelete);
			TextView title = (TextView) v.findViewById(R.id.delete_titleTxtVw);
			TextView when = (TextView) v.findViewById(R.id.delete_whenTxtVw);
			TextView with = (TextView) v.findViewById(R.id.delete_withTxtVw);
			RatingBar ratingBar = (RatingBar) v.findViewById(R.id.delete_ratingBar);

			
			checkDelete.setClickable(false);
			checkDelete.setFocusable(false);
			checkDelete.setChecked(isCheckedConfrim[position]);

			// ��ȭ title ��������
			title.setText(m.getTitle());
			// ��ȭ�� ���� �ô°�? ��������
			String getWhen = m.getWhen();
			String year = getWhen.substring(0, 4);
			String month = getWhen.substring(4, 5);
			String day = getWhen.substring(5, 7); 
			when.setText(year + "�� " + month + "�� " + day + "��");
			// ��ȭ�� ������ �ô°�? ��������
			// �ϴ� �ӽ÷� �帣 ������- 2012.02.12
			with.setText("with " + m.getWith());
			// ���� �� ��������
			float rating = (float) ((m.getGrade().equals("")) ? 0.0 : Float
					.parseFloat(m.getGrade())) / (float) 2.0;
			ratingBar.setRating(rating);

			Log.d("MovieAdapter", "��ȭ ���� : " + m.getTitle() + 
									"������ : " + m.getOpenInfo() +
									"�帣 : " + m.getGenre() +
									 "���� : " + m.getGrade());

			return v;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}