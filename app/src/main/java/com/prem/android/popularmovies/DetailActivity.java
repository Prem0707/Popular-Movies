package com.prem.android.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.prem.android.popularmovies.Interfaces.TaskCompleted;
import com.prem.android.popularmovies.Json_Parser.ReviewsParser;
import com.prem.android.popularmovies.Models.Movies;
import com.prem.android.popularmovies.Models.Reviews;
import com.prem.android.popularmovies.Models.Trailers;
import com.prem.android.popularmovies.utils.AsyncReuse;
import com.prem.android.popularmovies.utils.Constants;
import com.prem.android.popularmovies.utils.FormatUtils;
import com.prem.android.popularmovies.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity implements
                       LoaderManager.LoaderCallbacks<ArrayList<Reviews>> ,TaskCompleted, View.OnClickListener{

    String mMovieId;
    ArrayList<String> idOfVideos = new ArrayList<>();

    @BindView( R.id.movie_title) TextView mTextView;
    @BindView(R.id.release_date) TextView mReleaseDate;
    @BindView(R.id.movie_poster) ImageView mPosterImage;
    @BindView(R.id.overview_of_movie) TextView mOverview;
    @BindView(R.id.rating) TextView mRating;
    @BindView(R.id.review_of_movie) TextView mReviewMovies;

    private static String ID_OF_MOVIE;
    private static final int MOVIE_REVIEW_LOADER = 10;
    Button mTrailors_1, mTrailors_2, mTrailors_3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        android.support.v7.app.ActionBar actionBar = this.getSupportActionBar();

        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Intent intentThatStartedActivity = getIntent();
        Movies currentMovie = intentThatStartedActivity.getParcelableExtra(Constants.CURRENT_MOVIE_DATA);

        // Initialisation of buttons
        mTrailors_1 = (Button) findViewById(R.id.tralors_1);
        mTrailors_2 = (Button) findViewById(R.id.tralors_2);
        mTrailors_3 = (Button) findViewById(R.id.tralors_3);
        mTrailors_1.setOnClickListener(this);
        mTrailors_2.setOnClickListener(this);
        mTrailors_3.setOnClickListener(this);

        // Populating views
        mTextView.setText(currentMovie.getTitle());
        String picassoUrl = NetworkUtils.buildPicassoUrl(currentMovie.getPoster());
        Picasso.with(this).load(picassoUrl).placeholder(R.mipmap.placeholder).error(R.mipmap.placeholder).into(mPosterImage);
        mReleaseDate.setText(currentMovie.getReleaseDate());
        mRating.setText(FormatUtils.getFormattedRating(currentMovie.getUserRating()));
        mOverview.setText(currentMovie.getOverview());
        this.mMovieId = Integer.toString(currentMovie.getmMovieId());



        // AsyncTask for Trailers
        AsyncReuse asyncReuse = new AsyncReuse(DetailActivity.this);
        asyncReuse.execute(mMovieId);

        // Loader for Reviews
        Bundle reviewBundle = new Bundle();
        reviewBundle.putString(ID_OF_MOVIE, mMovieId);

        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<Reviews> moviesReviewsLoader = loaderManager.getLoader(MOVIE_REVIEW_LOADER);

        if(moviesReviewsLoader == null){
            loaderManager.initLoader(MOVIE_REVIEW_LOADER, reviewBundle,this);
        } else {
            loaderManager.restartLoader(MOVIE_REVIEW_LOADER, reviewBundle,this);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menu){
        int id = menu.getItemId();

        if(id == android.R.id.home){
            NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(menu);
    }

    /**
     * Instantiate and return a new Loader for the given ID.
     *
     * @param id   The ID whose loader is to be created.
     * @param args Any arguments supplied by the caller.
     * @return Return a new Loader instance that is ready to start loading.
     */
    @Override
    public Loader<ArrayList<Reviews>> onCreateLoader(int id, final Bundle args) {

        return new AsyncTaskLoader<ArrayList<Reviews>>(this) {

            ArrayList<Reviews> mReviews = null;

            @Override
            protected void onStartLoading() {
                super.onStartLoading();

                if(mReviews != null){
                    deliverResult(mReviews);
                } else{
                    forceLoad();
                }
            }

            @Override
            public ArrayList<Reviews> loadInBackground() {
                String idOfMovie = args.getString(ID_OF_MOVIE);

                if (idOfMovie == null) {
                    return null;
                }
                URL urlForFetchMovieDetails = NetworkUtils.buildURL(idOfMovie+"/reviews");

                if (urlForFetchMovieDetails != null) {
                    try {
                        String responseFromAPI = null;
                        try {
                             responseFromAPI = NetworkUtils.getResponseFromHttpUrl(urlForFetchMovieDetails);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        return ReviewsParser.getReviewsFeomJson(responseFromAPI);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                return null;
            }

            /**
             * Sends the result of the load to the registered listener.
             *
             * @param reviews The result of the load
             */
            public void deliverResult(ArrayList<Reviews> reviews) {
                mReviews = reviews;
                super.deliverResult(reviews);
            }
        };
    }

    /**
     * @param loader The Loader that has finished.
     * @param data   The data generated by the Loader.
     */
    @Override
    public void onLoadFinished(Loader<ArrayList<Reviews>> loader, ArrayList<Reviews> data) {
        String stringReviews= null;
        for (Reviews reviews: data){

                stringReviews = stringReviews + reviews.getAuthor()+"\n" + reviews.getContent()+"\n";
        }
        if(stringReviews != null) {
            mReviewMovies.setText(stringReviews);
        }
    }

    /**
     * Called when a previously created loader is being reset, and thus
     * making its data unavailable.  The application should at this point
     * remove any references it has to the Loader's data.
     *
     * @param loader The Loader that is being reset.
     */
    @Override
    public void onLoaderReset(Loader<ArrayList<Reviews>> loader) {

    }



    @Override
    public void onTaskCompleted(ArrayList<Trailers> moviesTrailers) {
        for (Trailers moviestrailer : moviesTrailers){
            idOfVideos.add( moviestrailer.getmVideoKey());
        }
    }


    @Override
    public void onClick(View view) {
        switch(view.getId()){

            case R.id.tralors_1:
                playVideo(idOfVideos.get(0));
                break;

            case R.id.tralors_2:

                if(idOfVideos.get(1) != null) {
                    playVideo(idOfVideos.get(1));
                }else{
                    playVideo(idOfVideos.get(0));
                }
                break;

            case R.id.tralors_3:

                if(idOfVideos.get(2) != null) {
                    playVideo(idOfVideos.get(2));
                } else if(idOfVideos.get(1) != null){
                    playVideo(idOfVideos.get(1));
                } else{
                    playVideo(idOfVideos.get(0));
                }
                break;

        }
    }

    public void playVideo(String key){

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + key));

        // Check if the youtube app exists on the device
        if (intent.resolveActivity(getPackageManager()) == null) {
            // If the youtube app doesn't exist, then use the browser
            intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://www.youtube.com/watch?v=" + key));
        }

        startActivity(intent);
    }
}