package com.prem.android.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.prem.android.popularmovies.Models.Movies;
import com.prem.android.popularmovies.utils.FormatUtils;
import com.prem.android.popularmovies.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        //Initialisation of UI components
        TextView mTextView =(TextView) findViewById(R.id.movie_title);
        ImageView mPosterImage = (ImageView) findViewById(R.id.movie_poster);
        TextView mReleaseDate = (TextView) findViewById(R.id.release_date);
        TextView mOverview = (TextView) findViewById(R.id.overview_of_movie);
        TextView mRating = (TextView) findViewById(R.id.rating);

        Intent intentThatStartedActivity = getIntent();
        Movies currentMovie = intentThatStartedActivity.getParcelableExtra(MainActivity.CURRENT_MOVIE_DATA);

        // Populating views
        mTextView.setText(currentMovie.getTitle());
        String picassoUrl = NetworkUtils.buildPicassoUrl(currentMovie.getPoster());
        Picasso.with(this).load(picassoUrl).into(mPosterImage);
        mReleaseDate.setText(currentMovie.getReleaseDate());
        mRating.setText(FormatUtils.getFormattedRating(currentMovie.getUserRating()));
        mOverview.setText(currentMovie.getOverview());

    }

}
