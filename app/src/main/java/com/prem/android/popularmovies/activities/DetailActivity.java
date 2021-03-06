package com.prem.android.popularmovies.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
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
import android.widget.ScrollView;
import android.widget.TextView;

import com.prem.android.popularmovies.R;
import com.prem.android.popularmovies.data.MovieContract;
import com.prem.android.popularmovies.interfaces.TaskCompleted;
import com.prem.android.popularmovies.json_parser.ReviewsParser;
import com.prem.android.popularmovies.models.Movies;
import com.prem.android.popularmovies.models.Reviews;
import com.prem.android.popularmovies.models.Trailers;
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

import static com.prem.android.popularmovies.data.MovieContract.MovieEntry.CONTENT_URI;


public class DetailActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<ArrayList<Reviews>>, TaskCompleted,
        View.OnClickListener {

    private String mMovieId;
    private static String ID_OF_MOVIE;
    private static final int MOVIE_REVIEW_LOADER = 10;
    private Movies currentMovie;
    private ArrayList<Reviews> mReviews;
    private String stringReviews = null;

    private ArrayList<String> idOfVideos = new ArrayList<>();

    @BindView(R.id.movie_title)
    TextView mTextView;
    @BindView(R.id.release_date)
    TextView mReleaseDate;
    @BindView(R.id.movie_poster)
    ImageView mPosterImage;
    @BindView(R.id.overview_of_movie)
    TextView mOverview;
    @BindView(R.id.rating)
    TextView mRating;
    @BindView(R.id.review_of_movie)
    TextView mReviewMovies;
    @BindView(R.id.trailer_1)
    Button mTrailers_1;
    @BindView(R.id.trailer_2)
    Button mTrailers_2;
    @BindView(R.id.trailer_3)
    Button mTrailers_3;
    private ScrollView mScrollView;
    @BindView(R.id.button_for_favorite)
    Button mButtonMarkAsFavorite;
    @BindView(R.id.button_remove_from_favorites)
    Button mButtonRemoveFavorites;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        android.support.v7.app.ActionBar actionBar = this.getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (savedInstanceState == null || !savedInstanceState.containsKey("movieDetails")) {
            Intent intentThatStartedActivity = getIntent();

            this.currentMovie = intentThatStartedActivity.getParcelableExtra(Constants.CURRENT_MOVIE_DATA);
        } else {
            this.currentMovie = savedInstanceState.getParcelable("movieDetails");
        }
        mTrailers_1.setOnClickListener(this);
        mTrailers_2.setOnClickListener(this);
        mTrailers_3.setOnClickListener(this);


        // Populating views
        if (currentMovie != null) {
            mTextView.setText(currentMovie.getTitle());
            String picassoUrl = NetworkUtils.buildPicassoUrl(currentMovie.getPoster());
            Picasso.with(this).load(picassoUrl).into(mPosterImage);
            mReleaseDate.setText(currentMovie.getReleaseDate());
            mRating.setText(FormatUtils.getFormattedRating(currentMovie.getUserRating()));
            mOverview.setText(currentMovie.getOverview());
            this.mMovieId = Integer.toString(currentMovie.getmMovieId());
        }

        if (savedInstanceState == null) {
            // AsyncTask for Trailers
            AsyncReuse asyncReuse = new AsyncReuse(DetailActivity.this);
            asyncReuse.execute(mMovieId);
        } else {
            this.idOfVideos = savedInstanceState.getStringArrayList("mVideoId");
        }


        // Loader for Reviews
        Bundle reviewBundle = new Bundle();
        reviewBundle.putString(ID_OF_MOVIE, mMovieId);
        if (savedInstanceState == null) {
            LoaderManager loaderManager = getSupportLoaderManager();
            Loader<Reviews> moviesReviewsLoader = loaderManager.getLoader(MOVIE_REVIEW_LOADER);

            if (moviesReviewsLoader == null) {
                loaderManager.initLoader(MOVIE_REVIEW_LOADER, reviewBundle, this);
            } else {
                loaderManager.restartLoader(MOVIE_REVIEW_LOADER, reviewBundle, this);
            }
        } else {
            stringReviews = savedInstanceState.getString("movieReviews");
            mReviewMovies.setText(stringReviews);
        }

        updateFavoriteButtons();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable("movieDetails", currentMovie);
        outState.putString("movieReviews", stringReviews);
        outState.putStringArrayList("mVideoId", idOfVideos);
        outState.putString("MovieId", mMovieId);
        mScrollView = (ScrollView) findViewById(R.id.scrollView);
        outState.putIntArray("ARTICLE_SCROLL_POSITION",
                new int[]{mScrollView.getScrollX(), mScrollView.getScrollY()});
        super.onSaveInstanceState(outState);
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        final int[] position = savedInstanceState.getIntArray("ARTICLE_SCROLL_POSITION");
        mScrollView = (ScrollView) findViewById(R.id.scrollView);
        if (position != null)
            mScrollView.post(new Runnable() {
                public void run() {
                    mScrollView.scrollTo(position[0], position[1]);
                }
            });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menu) {
        int id = menu.getItemId();

        if (id == android.R.id.home) {
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


            @Override
            protected void onStartLoading() {
                super.onStartLoading();

                if (mReviews != null) {
                    deliverResult(mReviews);
                } else {
                    forceLoad();
                }
            }

            @Override
            public ArrayList<Reviews> loadInBackground() {
                String idOfMovie = args.getString(ID_OF_MOVIE);

                if (idOfMovie == null) {
                    return null;
                }
                URL urlForFetchMovieDetails = NetworkUtils.buildURL(idOfMovie + "/reviews");

                if (urlForFetchMovieDetails != null) {
                    try {
                        String responseFromAPI = null;
                        try {
                            responseFromAPI = NetworkUtils.getResponseFromHttpUrl(urlForFetchMovieDetails);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        return ReviewsParser.getReviewsFromJson(responseFromAPI);

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
        for (Reviews reviews : data) {

            this.stringReviews = stringReviews + reviews.getAuthor() + "\n" + reviews.getContent() + "\n";
        }
        if (stringReviews != null) {
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
        for (Trailers moviestrailer : moviesTrailers) {
            idOfVideos.add(moviestrailer.getmVideoKey());
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.trailer_1:
                playVideo(idOfVideos.get(0));
                break;

            case R.id.trailer_2:
                if (idOfVideos.get(1) != null) {
                    playVideo(idOfVideos.get(1));
                } else {
                    playVideo(idOfVideos.get(0));
                }
                break;

            case R.id.trailer_3:
                if (idOfVideos.get(2) != null) {
                    playVideo(idOfVideos.get(2));
                } else if (idOfVideos.get(1) != null) {
                    playVideo(idOfVideos.get(1));
                } else {
                    playVideo(idOfVideos.get(0));
                }
                break;
        }
    }

    private void playVideo(String key) {

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + key));

        // Check if the youtube app exists on the device
        if (intent.resolveActivity(getPackageManager()) == null) {
            // If the youtube app doesn't exist, then use the browser
            intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://www.youtube.com/watch?v=" + key));
        }
        startActivity(intent);
    }


    private void markAsFavorite() {

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                if (!isFavorite()) {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(MovieContract.MovieEntry.MOVIE_ID, currentMovie.getmMovieId());
                    contentValues.put(MovieContract.MovieEntry.MOVIE_POSTER_PATH, currentMovie.getPoster());
                    contentValues.put(MovieContract.MovieEntry.MOVIE_TITLE, currentMovie.getTitle());
                    contentValues.put(MovieContract.MovieEntry.MOVIE_DESCRIPTION, currentMovie.getTitle());
                    contentValues.put(MovieContract.MovieEntry.MOVIE_RATING, currentMovie.getUserRating());
                    contentValues.put(MovieContract.MovieEntry.MOVIE_RELEASE_DATE, currentMovie.getReleaseDate());

                    // Insert the content values via a ContentResolver
                    getContentResolver().insert(CONTENT_URI, contentValues);

                    //  Don't forget to call finish() to return to MainActivity after this insert is complete
                    // Toast.makeText(getApplicationContext(), uri != null ? uri.toString() : null, Toast.LENGTH_LONG).show();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void nothing) {
                updateFavoriteButtons();
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void removeFromFavorites() {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                if (isFavorite()) {

                    String mUri = MovieContract.MovieEntry.CONTENT_URI + "/" + mMovieId;
                    Uri myUri = Uri.parse(mUri);
                    getContentResolver().delete(myUri, null, null);

                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                updateFavoriteButtons();
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }


    private void updateFavoriteButtons() {
        // Needed to avoid "skip frames".
        new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(Void... params) {
                return isFavorite();
            }

            @Override
            protected void onPostExecute(Boolean isFavorite) {
                if (isFavorite) {
                    mButtonRemoveFavorites.setVisibility(View.VISIBLE);
                    mButtonMarkAsFavorite.setVisibility(View.GONE);
                } else {
                    mButtonMarkAsFavorite.setVisibility(View.VISIBLE);
                    mButtonRemoveFavorites.setVisibility(View.GONE);
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        mButtonMarkAsFavorite.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        markAsFavorite();
                    }
                });

        mButtonRemoveFavorites.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        removeFromFavorites();
                    }
                });
    }

    private boolean isFavorite() {
        Cursor movieCursor = getContentResolver().query(
                MovieContract.MovieEntry.CONTENT_URI,
                new String[]{MovieContract.MovieEntry.MOVIE_ID},
                MovieContract.MovieEntry.MOVIE_ID + " = " + mMovieId,
                null,
                null);

        if (movieCursor != null && movieCursor.moveToFirst()) {
            movieCursor.close();
            return true;
        } else {
            return false;
        }
    }
}
