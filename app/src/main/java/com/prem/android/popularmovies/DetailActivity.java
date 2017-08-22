package com.prem.android.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.prem.android.popularmovies.Models.Movies;
import com.prem.android.popularmovies.utils.Constants;
import com.prem.android.popularmovies.utils.FormatUtils;
import com.prem.android.popularmovies.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener {

    String mMovieId;

    @BindView( R.id.movie_title) TextView mTextView;
    @BindView(R.id.release_date) TextView mReleaseDate;
    @BindView(R.id.movie_poster) ImageView mPosterImage;
    @BindView(R.id.overview_of_movie) TextView mOverview;
    @BindView(R.id.rating) TextView mRating;

    @BindView(R.id.movie_trailers) Button b1;
    @BindView(R.id.movie_reviews) Button b2;

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

        // Populating views
        mTextView.setText(currentMovie.getTitle());
        String picassoUrl = NetworkUtils.buildPicassoUrl(currentMovie.getPoster());
        Picasso.with(this).load(picassoUrl).placeholder(R.mipmap.placeholder).error(R.mipmap.placeholder).into(mPosterImage);
        mReleaseDate.setText(currentMovie.getReleaseDate());
        mRating.setText(FormatUtils.getFormattedRating(currentMovie.getUserRating()));
        mOverview.setText(currentMovie.getOverview());
        this.mMovieId = Integer.toString(currentMovie.getmMovieId());

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menu){
        int id = menu.getItemId();

        if(id == android.R.id.home){
            NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(menu);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId())
        {
            case R.id.movie_reviews:
                Intent reviewsIntent = new Intent(this, ReviewActivity.class);
                reviewsIntent.putExtra(Constants.DETAIL_TO_REVIEW, mMovieId);
                startActivity(reviewsIntent);
                break;

            case R.id.movie_trailers:

            break;

            default:
                break;
        }

    }
}
