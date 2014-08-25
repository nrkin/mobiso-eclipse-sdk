package com.nrk.mobiso;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.text.Html;
import android.util.Log;

public class SearchResultAdapter extends ArrayAdapter<Question>{
	final static private String TAG = "SearchResultAdapter";
	Context context;
	int layoutResourceId;
	ArrayList<Question> data = null;

	public SearchResultAdapter(Context context, int resource, ArrayList<Question> objects) {
		super(context, resource, objects);
		this.context = context;
		this.layoutResourceId = resource;
		this.data = objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null){
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			Log.i(TAG, "layoutResourceId " + (layoutResourceId == R.layout.search_result_row));
			convertView = inflater.inflate(layoutResourceId, parent, false); 
		}
		Question q = data.get(position);
		View textView = convertView.findViewById(R.id.question_votes);
		((TextView)textView).setText("" + q.score);
		textView = convertView.findViewById(R.id.question_text);
		((TextView)textView).setText(q.title);
		textView = convertView.findViewById(R.id.question_owner);
		((TextView)textView).setText(q.ownerName);
		return convertView;
	}
}
