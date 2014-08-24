package com.nrk.mobiso;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

public class SearchActivity extends Activity {
	private LinearLayout searchLayout;
	private SearchResultAdapter adapter;
	private ListView searchResultList;
	private ProgressBar progressBar;
	private String currentSearchTerm;
	private String URL = "http://api.stackexchange.com/2.2/search/advanced?order=desc&sort=relevance&site=stackoverflow";
	private int RESULT_LIMIT = 3;
	private String result = "";
	private ArrayList<Question> parsedResult = new ArrayList<Question>();
	private final static String TAG = "mobiso-zzz";

	private Question getQuestion(JSONObject o) throws JSONException{
		return new Question(
			o.getString("title"),
			o.getInt("score"),
			o.getJSONObject("owner").getString("display_name")
		);
	}
	private void makeRequest(){
		RequestQueue queue = Volley.newRequestQueue(this);

		URL = "http://api.stackexchange.com/2.2/search/advanced?" +
				"order=desc&sort=relevance&q=android%20activity&site=stackoverflow" +
				"&q=" + currentSearchTerm + "&pagesize=" + RESULT_LIMIT;
		
		JsonObjectRequest searchRequest = new JsonObjectRequest(
				Request.Method.GET,
				URL,
				null,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response){
						Log.i(TAG, response.toString());
						try {
							JSONObject o;
							JSONArray items = response.getJSONArray("items");
							parsedResult.clear();
							for(int i = 0; i < items.length(); i ++){
								o = items.getJSONObject(i);
								Log.i(TAG, "should show " + o.toString());
								parsedResult.add(getQuestion(o));
							}
							showResults();
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							parsedResult.clear();
						}
						
						Log.i(TAG, result);
					}
				},
				new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						// TODO Auto-generated method stub
						Log.i(TAG, error.getMessage());
					}
				}
		);
		queue.add(searchRequest);
		showLoading();
	}
	
	private void showLoading(){
		progressBar.setVisibility(View.VISIBLE);
		searchResultList.setVisibility(View.GONE);
	}
	
	private void showResults() {
		progressBar.setVisibility(View.GONE);
		searchResultList.setVisibility(View.VISIBLE);		
	}

	private void createLoading(){
		progressBar = new ProgressBar(this);
		progressBar.setLayoutParams(new LayoutParams(
			LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT
		));
		progressBar.setIndeterminate(true);
		searchLayout.addView(progressBar);
	}
	private void createListView() {
		if(searchResultList == null) {
			searchResultList = new ListView(this);
			adapter = 
				new SearchResultAdapter(this, R.layout.search_result_row, parsedResult);
			searchResultList.setAdapter(adapter);
		}
	}
	private void createSearchLayout(){
		if(searchLayout == null){
			searchLayout = new LinearLayout(this);
			createLoading();
			createListView();
			searchLayout.addView(searchResultList);
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		currentSearchTerm = getIntent().getStringExtra("SEARCH_TEXT");
		createSearchLayout();
		setContentView(searchLayout);
		makeRequest();
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}
}
