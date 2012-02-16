package kr.co.WhenWhereWho3;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
/*
 * 영화list 
 * (나의목록 보기)
 * 
 * listview Adapter
 */

public class SetupListAdapter extends ArrayAdapter<SetupList> {
	ArrayList<SetupList> setupList;
	LayoutInflater li;
	
	public SetupListAdapter(Context context, int textViewResourceId,
			ArrayList<SetupList> setupList) {
		super(context, textViewResourceId, setupList);
		this.setupList = setupList;
		li = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = li.inflate(R.layout.setuplist, null);
		Log.d( "값들 : ", setupList.get(position).getTitle() + ";" 
				+  setupList.get(position).getContent() );
		
		
		TextView title = (TextView)v.findViewById(R.id.setUpList_titleVwSetup);
		TextView subtitle = (TextView)v.findViewById(R.id.setUpList_subTitleVwSetup);
		
		title.setText(setupList.get(position).getTitle());
		subtitle.setText(setupList.get(position).getContent());
		
		return v;
	}


}

class SetupList {
	String title;
	String content;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}	
}