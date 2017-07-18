package com.prem.android.popularmovies.Models;

/**
 * Created by Prem on 18-07-2017.
 */
public class Movies {

    private String mTitle;
    private String mPoster;
    private String mOverview;
    private String mReleseDate;
    private String mUserRating;

    public Movies() {
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public void setPoster(String mPoster) {
        this.mPoster = mPoster;
    }

    public void setOverview(String mOverview) {
        this.mOverview = mOverview;
    }

    public void setReleaseDate(String mReleseDate) {
        this.mReleseDate = mReleseDate;
    }

    public void setUserRating(String mUserRating) {
        this.mUserRating = mUserRating;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getPoster() {
        return mPoster;
    }

    public String getOverview() {
        return mOverview;
    }

    public String getReleaseDate() {
        return mReleseDate;
    }

    public String getUserRating() {
        return mUserRating;
    }
}
