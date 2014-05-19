package com.thompath.rottentomatoes;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.loopj.android.http.JsonHttpResponseHandler;

public class MainActivity extends Activity {

	public static final String MOVIE_DETAIL_KEY = "movie";

	
	private RottenTomatoesClient client;
	private ListView lvMovies;
	private BoxOfficeMovieAdapter adapterMovies;
	private ArrayList<BoxOfficeMovie> movies = new ArrayList<BoxOfficeMovie>();
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		

		adapterMovies = new BoxOfficeMovieAdapter(this, movies);
		lvMovies = (ListView) findViewById(R.id.lvMovies);
        lvMovies.setAdapter(adapterMovies);
		
		//Fetch movies from network
		client = new RottenTomatoesClient();
		fetchMovies(); 
		
		setupMovieSelectedListener();  

		
	}

	private void fetchMovies() {
		client.getBoxOfficeMovies(new JsonHttpResponseHandler() {
			@Override 
			public void onSuccess(JSONObject response) {
				Log.d("BoxOfficeActivity", "Got Movies!");
				try {
					JSONArray moviesJsonArray = response.getJSONArray("movies");
					ArrayList<BoxOfficeMovie> theMovies = BoxOfficeMovie.fromJson(moviesJsonArray);
					movies.addAll(theMovies);
					adapterMovies.notifyDataSetChanged();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}
	
	
	
	   public void setupMovieSelectedListener() {
	       lvMovies.setOnItemClickListener(new OnItemClickListener() {
	           @Override
	           public void onItemClick(AdapterView<?> adapterView, View item, int position, long rowId) {
	               // Launch the detail view passing movie as an extra
	               Intent i = new Intent(MainActivity.this, BoxOfficeDetailActivity.class);
	               i.putExtra(MOVIE_DETAIL_KEY, adapterMovies.getItem(position));
	               startActivity(i);
	           }
	        });
	    } 

}
