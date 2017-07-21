package com.prem.android.popularmovies.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.prem.android.popularmovies.Models.Movies;
import com.prem.android.popularmovies.R;
import com.prem.android.popularmovies.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    TextView mTextView;
    TextView mReleaseDate;
    TextView mOverview;
    TextView mRating;
    ImageView mPosterImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Separate initilasation of UI component
        initUiComponents();

        Intent intentThatStartedActivity = getIntent();
        Movies currentMovie = intentThatStartedActivity.getParcelableExtra(MainActivity.PARCELABLE_MOVIE);
        populateViews(currentMovie);
    }

    void initUiComponents(){

         mTextView =(TextView) findViewById(R.id.movie_title);
         mReleaseDate = (TextView) findViewById(R.id.release_date);
         mOverview = (TextView) findViewById(R.id.overview_of_movie);
         mRating = (TextView) findViewById(R.id.rating);
         mPosterImage = (ImageView) findViewById(R.id.movie_poster);
    }

    //Populating data in views
    void populateViews(Movies Movie){

        mTextView.setText(Movie.getTitle());
        mReleaseDate.setText(Movie.getReleaseDate());
        mRating.setText(Movie.getUserRating());
        mOverview.setText(Movie.getOverview());

        String picassoUrl = NetworkUtils.buildPicassoUrl(Movie.getPoster());
        Picasso.with(this).load(picassoUrl).into(mPosterImage);

    }
}
