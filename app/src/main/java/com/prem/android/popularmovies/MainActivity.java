package com.prem.android.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.prem.android.popularmovies.Adapters.MovieAdapter;
import com.prem.android.popularmovies.Interfaces.TaskCompleted;
import com.prem.android.popularmovies.Models.Movies;
import com.prem.android.popularmovies.utils.AsyncReuse;
import com.prem.android.popularmovies.utils.CheckOrientation;
import com.prem.android.popularmovies.utils.Constants;
import com.prem.android.popularmovies.utils.NetworkUtils;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler,
        TaskCompleted, SharedPreferences.OnSharedPreferenceChangeListener{

    private static MovieAdapter mMovieAdapter;
    private static GridLayoutManager mGridLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fetchMoviesIfDeviceOnline(Constants.TOP_RATED_MOVIES_SORT_SELECTION);

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mGridLayoutManager = gridLayoutManagerAccordingToOrientation();

        if (mRecyclerView != null) {
            mRecyclerView.setLayoutManager(mGridLayoutManager);
            mMovieAdapter = new MovieAdapter(this);
            mRecyclerView.setAdapter(mMovieAdapter);
        }

        android.support.v7.preference.PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(this);
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
            new AsyncReuse(MainActivity.this).execute(urlEndpoint);
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

    public void displayMoviesInGridLayout(ArrayList<Movies> moviesList) {
        if (moviesList != null) {
            mMovieAdapter.setMovieList(moviesList);
            mGridLayoutManager.scrollToPositionWithOffset(0, 0);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent startsSettingsActivity = new Intent(this,SettingsActivity.class);
            startActivity(startsSettingsActivity);
            return true;
        }
        else if (id == R.id.about_the_app){
            Intent startsAboutApp = new Intent(this, AboutActivity.class);
            startActivity(startsAboutApp);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTaskCompleted(ArrayList<Movies> movies) {
        displayMoviesInGridLayout(movies);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        if(key.equals(getString(R.string.pref_sort_by_rating))){
            fetchMoviesIfDeviceOnline(Constants.TOP_RATED_MOVIES_SORT_SELECTION);

        } else if(key.equals(getString(R.string.pref_sort_by_popularity))){
            fetchMoviesIfDeviceOnline(Constants.POPULAR_MOVIES_SORT_SELECTION);
        }
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        android.support.v7.preference.PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }
}
