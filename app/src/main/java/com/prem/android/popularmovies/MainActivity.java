package com.prem.android.popularmovies;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.prem.android.popularmovies.Adapters.MovieAdapter;
import com.prem.android.popularmovies.Models.Movies;
import com.prem.android.popularmovies.utils.CheckOrientation;
import com.prem.android.popularmovies.utils.Constants;
import com.prem.android.popularmovies.utils.NetworkUtils;
import com.prem.android.popularmovies.utils.TheMovieDbJsonUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler{

    private MovieAdapter mMovieAdapter;
    private GridLayoutManager mGridLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fetchMoviesIfDeviceOnline(Constants.POPULAR_MOVIES_SORT_SELECTION);

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mGridLayoutManager = gridLayoutManagerAccordingToOrientation();

        if (mRecyclerView != null) {
            mRecyclerView.setLayoutManager(mGridLayoutManager);
            mMovieAdapter = new MovieAdapter(this);
            mRecyclerView.setAdapter(mMovieAdapter);
        }
    }

    private GridLayoutManager gridLayoutManagerAccordingToOrientation() {
        int deviceOrientation = CheckOrientation.checkDeviceOrientation(this);

        if (deviceOrientation == Configuration.ORIENTATION_PORTRAIT) {
            return new GridLayoutManager(this,2);
        } else
            return new GridLayoutManager(this,3);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    // we will only execute the FetchMoviesTask if device online.
    private void fetchMoviesIfDeviceOnline(String urlEndpoint){
        if (NetworkUtils.checkDeviceOnline(this)) {
            new FetchMovieTask().execute(urlEndpoint);
        }else{
            Toast.makeText(this, "Check network connection", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onMovieClick(Movies currentMovie) {
        Toast.makeText(this, currentMovie.getTitle(),Toast.LENGTH_LONG).show();
        Intent goesToDetailActivity = new Intent(this,DetailActivity.class);
        goesToDetailActivity.putExtra(Constants.CURRENT_MOVIE_DATA, currentMovie);
        startActivity(goesToDetailActivity);
    }

    private class FetchMovieTask extends AsyncTask<String, Void, ArrayList<Movies>> {

        @Override
        protected ArrayList<Movies> doInBackground(String... params) {

            String sortOptionSelected = params[0];
            URL urlForFetchMovieDetails = NetworkUtils.buildURL(sortOptionSelected);

            if (urlForFetchMovieDetails != null) {
                try {
                    String responseFromAPI = null;
                    try {
                        responseFromAPI = NetworkUtils.getResponseFromHttpUrl(urlForFetchMovieDetails);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return TheMovieDbJsonUtils.getMovieListFromJson(responseFromAPI);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<Movies> movies) {
            super.onPostExecute(movies);
            displayMoviesInGridLayout(movies);
        }
    }

    private void displayMoviesInGridLayout(ArrayList<Movies> moviesList) {
        if (moviesList != null) {
            mMovieAdapter.setMovieList(moviesList);
            mGridLayoutManager.scrollToPositionWithOffset(0, 0);
        } else {
            Toast.makeText(this, "Nothing in MovieList", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_sort_by_popularity) {
            fetchMoviesIfDeviceOnline(Constants.POPULAR_MOVIES_SORT_SELECTION);
            return true;
        }
        else if (id == R.id.action_sort_by_top_rated){
            fetchMoviesIfDeviceOnline(Constants.TOP_RATED_MOVIES_SORT_SELECTION);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
