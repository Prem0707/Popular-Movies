package com.prem.android.popularmovies.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.prem.android.popularmovies.R;
import com.prem.android.popularmovies.adapters.MovieAdapter;
import com.prem.android.popularmovies.json_parser.TheMovieDbJsonUtils;
import com.prem.android.popularmovies.models.Movies;
import com.prem.android.popularmovies.sharedpref.UserPreference;
import com.prem.android.popularmovies.utils.CheckOrientation;
import com.prem.android.popularmovies.utils.Constants;
import com.prem.android.popularmovies.utils.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler,
        LoaderManager.LoaderCallbacks<ArrayList<Movies>>, SharedPreferences.OnSharedPreferenceChangeListener{

    private static MovieAdapter mMovieAdapter;
    private static GridLayoutManager mGridLayoutManager;
    private static final String POPULAR_MOVIES_LOADER = "22";
    private ArrayList<Movies> moviesList;
    RecyclerView mRecyclerView;
    Parcelable state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mGridLayoutManager = CheckOrientation.gridLayoutManagerAccordingToOrientation(this);

        if (mRecyclerView != null) {
            mRecyclerView.setLayoutManager(mGridLayoutManager);
            mMovieAdapter = new MovieAdapter(this);
            mRecyclerView.setAdapter(mMovieAdapter);}

        if( savedInstanceState != null){
            Toast.makeText(this, "No need to call API",Toast.LENGTH_SHORT).show();
            ArrayList<Movies> moviesArrayList = savedInstanceState.getParcelableArrayList("MovieData");
            mMovieAdapter.setMovieList(moviesArrayList);
            mGridLayoutManager.scrollToPositionWithOffset(0, 0);
        }else {
            try {
                Toast.makeText(this, "Going to call API",Toast.LENGTH_SHORT).show();
                String sharedPrefByUser = UserPreference.getSharedPref(this);
                fetchMoviesIfDeviceOnline(sharedPrefByUser);
            } catch (NullPointerException e) {
                fetchMoviesIfDeviceOnline(Constants.TOP_RATED_MOVIES_SORT_SELECTION);
            }
        }

    }
    @Override
    public void onPause() {
        super.onPause();
        Toast.makeText(this, "In Pause",Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.registerOnSharedPreferenceChangeListener(this);
        Toast.makeText(this, "In Resume",Toast.LENGTH_SHORT).show();
        mGridLayoutManager.onRestoreInstanceState(state);
    }

    @Override
    public void onSaveInstanceState(Bundle bundleOutState) {
        bundleOutState.putParcelableArrayList("MovieData", moviesList);
        super.onSaveInstanceState(bundleOutState);
        state = mGridLayoutManager.onSaveInstanceState();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.most_popular) {
            Toast.makeText(this, "Most Popular", Toast.LENGTH_SHORT).show();
            UserPreference.setSharedPref(Constants.POPULAR_MOVIES_SORT_SELECTION, this);
            return true;
        } else if (id == R.id.most_rated) {
            Toast.makeText(this, "Top Rated", Toast.LENGTH_SHORT).show();
            UserPreference.setSharedPref(Constants.TOP_RATED_MOVIES_SORT_SELECTION, this);
            return true;
        } else if (id == R.id.favourite_movies) {
            Intent favouriteMovie = new Intent(this, FavouriteActivity.class);
            startActivity(favouriteMovie);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // we will only execute the FetchMoviesTask if device online.
    public void fetchMoviesIfDeviceOnline(String urlEndpoint) {
        if (NetworkUtils.checkDeviceOnline(this)) {

            Bundle queryBundle = new Bundle();
            queryBundle.putString(String.valueOf(POPULAR_MOVIES_LOADER), urlEndpoint);
            LoaderManager loaderManager = getSupportLoaderManager();
            Loader<ArrayList<Movies>> popMoviesLoader = loaderManager.getLoader(Integer.parseInt(POPULAR_MOVIES_LOADER));

            if (popMoviesLoader == null) {
                loaderManager.initLoader(Integer.parseInt(POPULAR_MOVIES_LOADER), queryBundle, this);
            } else {
                loaderManager.restartLoader(Integer.parseInt(POPULAR_MOVIES_LOADER), queryBundle, this);
            }
        } else {
            Toast.makeText(this, "Check network connection", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onMovieClick(Movies currentMovie) {
        Toast.makeText(this, currentMovie.getTitle(), Toast.LENGTH_LONG).show();
        Intent goesToDetailActivity = new Intent(this, DetailActivity.class);
        goesToDetailActivity.putExtra(Constants.CURRENT_MOVIE_DATA, currentMovie);
        startActivity(goesToDetailActivity);
    }

    // Implementing AsyncTakLoader instead of AsyncTask to avoid zombies activity creation whenever device is rotated.
    @Override
    public Loader<ArrayList<Movies>> onCreateLoader(int id, final Bundle args) {

        return new AsyncTaskLoader<ArrayList<Movies>>(this) {

            private ArrayList<Movies> moviesList = null;


            @Override
            public void onStartLoading() {
                super.onStartLoading();
                if (moviesList != null) {
                    deliverResult(moviesList);
                } else {
                    forceLoad();
                }
            }

            @Override
            public ArrayList<Movies> loadInBackground() {
                String movieUrlEndpoint = args.getString(POPULAR_MOVIES_LOADER);

                URL urlForFetchMovieDetails = NetworkUtils.buildURL(movieUrlEndpoint);

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

            /**
             * Sends the result of the load to the registered listener.
             * @param data The result of the load
             */
            public void deliverResult(ArrayList<Movies> data) {
                moviesList = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Movies>> loader, ArrayList<Movies> moviesList) {
        this.moviesList = moviesList;
        mMovieAdapter.setMovieList(moviesList);
        mGridLayoutManager.scrollToPositionWithOffset(0, 0);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Movies>> loader) {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Cleanup the shared preference listener
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.unregisterOnSharedPreferenceChangeListener(this);
    }

    //This is a listener that will update the UI when sorting criteria will change
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        String sharedPrefByUser = UserPreference.getSharedPref(this);
        Toast.makeText(this, "Preference Changed" + sharedPrefByUser, Toast.LENGTH_SHORT).show();
        fetchMoviesIfDeviceOnline(sharedPrefByUser);
    }

    //This is a listener that will update the UI when sorting criteria will change
    /*@Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Toast.makeText(this, "Preference Changed", Toast.LENGTH_SHORT).show();
        String sharedPrefByUser = UserPreference.getSharedPref(this);
        fetchMoviesIfDeviceOnline(sharedPrefByUser);
    }*/
}
