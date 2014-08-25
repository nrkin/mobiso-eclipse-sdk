package com.nrk.mobiso;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

public class SearchActivity extends Activity {
	private enum ResultStatus {
		PROGRESS,
		SUCCESS,
		ERROR,
		EMPTY
	};
	private LinearLayout searchLayout;
	private SearchResultAdapter adapter;
	private ListView searchResultList;
	private ProgressBar progressBar;
	private TextView statusText;
	private String currentSearchTerm;
	private String URL = "http://api.stackexchange.com/2.2/search/advanced?order=desc&sort=relevance&site=stackoverflow";
	private int RESULT_LIMIT = 3;
	private String result = "";
	private ArrayList<Question> parsedResult = new ArrayList<Question>();
	private final static String TAG = "mobiso-zzz";

	private Question getQuestion(JSONObject o) throws JSONException{
		return new Question(
			o.getLong("question_id"),
			o.getString("title"),
			o.getInt("score"),
			o.getJSONObject("owner").getString("display_name"),
			o.getString("body")
		);
	}
	
	private void updateView(ResultStatus status){
		switch(status){
			case PROGRESS:
				progressBar.setVisibility(View.VISIBLE);
				searchResultList.setVisibility(View.GONE);
				statusText.setText("Loading");
				statusText.setVisibility(View.GONE);
				break;
			case EMPTY:
				progressBar.setVisibility(View.GONE);
				searchResultList.setVisibility(View.GONE);
				statusText.setText("No results for " + currentSearchTerm);
				statusText.setVisibility(View.VISIBLE);
				break;
			case ERROR:
				progressBar.setVisibility(View.GONE);
				searchResultList.setVisibility(View.GONE);
				statusText.setText("Some error while loading results");
				statusText.setVisibility(View.VISIBLE);
				break;
			case SUCCESS:
				progressBar.setVisibility(View.GONE);
				searchResultList.setVisibility(View.VISIBLE);
				statusText.setText("Done !");
				statusText.setVisibility(View.GONE);
				break;
			default:
				break;
		}
	}
	private void makeRequest(){
		RequestQueue queue = Volley.newRequestQueue(this);

		URL = "http://api.stackexchange.com/2.2/search/advanced?" +
				"order=desc&sort=relevance&site=stackoverflow" +
				"&q=" + currentSearchTerm + "&pagesize=" + RESULT_LIMIT +
				"&filter=withbody";
		
		JsonObjectRequest searchRequest = new JsonObjectRequest(
				Request.Method.GET,
				URL,
				null,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response){
						try {
							JSONObject o;
							JSONArray items = response.getJSONArray("items");
							parsedResult.clear();
							for(int i = 0; i < items.length(); i ++){
								o = items.getJSONObject(i);
								parsedResult.add(getQuestion(o));
							}
							if(parsedResult.isEmpty()){
								updateView(ResultStatus.EMPTY);
							} else {
								updateView(ResultStatus.SUCCESS);
							}
						} catch (JSONException e) {
							e.printStackTrace();
							parsedResult.clear();
							updateView(ResultStatus.ERROR);
						}
					}
				},
				new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						updateView(ResultStatus.ERROR);
					}
				}
		);
		queue.add(searchRequest);
		updateView(ResultStatus.PROGRESS);
	}

	private void createLoading(){
		progressBar = new ProgressBar(this);
		progressBar.setLayoutParams(new LayoutParams(
			LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT
		));
		progressBar.setIndeterminate(true);
		searchLayout.addView(progressBar);
	}
	
	private void createEmpty(){
		statusText = new TextView(this);
		statusText.setLayoutParams(new LayoutParams(
			LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT
		));
		statusText.setGravity(Gravity.CENTER);
		statusText.setText("");
		searchLayout.addView(statusText);
	}
	
	private void createListView() {
		if(searchResultList == null) {
			searchResultList = new ListView(this);
			adapter = 
				new SearchResultAdapter(this, R.layout.search_result_row, parsedResult);
			searchResultList.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					Question q;
					Intent i;
					SearchResultAdapter adapter = (SearchResultAdapter)parent.getAdapter(); 
					if(adapter != null && !adapter.isEmpty()){
						q = adapter.getItem(position);
						i = new Intent(SearchActivity.this, AnswerActivity.class);
						i.putExtra("CURRENT_QUESTION", q);
						startActivity(i);
					}
				}
			});
			searchResultList.setAdapter(adapter);
		}
	}
	private void createSearchLayout(){
		if(searchLayout == null){
			searchLayout = new LinearLayout(this);
			createLoading();
			createEmpty();
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
