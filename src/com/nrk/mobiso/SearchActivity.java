package com.nrk.mobiso;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ListView;

public class SearchActivity extends ListActivity {
	private ListView searchResultList;
	private LinearLayout searchResultLayout;
	private void setLoading(){
	}
	
	private void createLoadingView(){
	}
	
	private void createListView(){
		if(searchResultList == null){
			searchResultList = new ListView(this);
			//searchResultList.setAdapter();
		}
	}
	
	private void createSearchLayout(){
		searchResultLayout = new LinearLayout(this);
		createListView();
		searchResultLayout.addView(searchResultList);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		createSearchLayout();
	}
}
