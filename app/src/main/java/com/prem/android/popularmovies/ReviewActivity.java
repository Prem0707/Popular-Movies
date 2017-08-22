package com.prem.android.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.prem.android.popularmovies.utils.AsyncReuse;
import com.prem.android.popularmovies.utils.Constants;
import com.prem.android.popularmovies.utils.NetworkUtils;

public class ReviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        Intent intentThatStartedActivity = getIntent();
        String mMovieId = intentThatStartedActivity.getStringExtra(Constants.DETAIL_TO_REVIEW);
        if (NetworkUtils.checkDeviceOnline(this)) {
            new AsyncReuse(ReviewActivity.this).execute("movies/" +mMovieId + "/reviews");
        }else{
            Toast.makeText(this, "Check network connection", Toast.LENGTH_LONG).show();
        }
    }
}
