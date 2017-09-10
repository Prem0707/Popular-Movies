package com.prem.android.popularmovies.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Prem on 18-07-2017.
 *
 */
public class Movies implements Parcelable {

    private String mTitle;
    private String mPoster;
    private String mOverview;
    private String mReleaseDate;
    private String mUserRating;
    private int mMovieId;


    private Movies(Parcel in) {
        mTitle = in.readString();
        mPoster = in.readString();
        mOverview = in.readString();
        mReleaseDate = in.readString();
        mUserRating = in.readString();
        mMovieId = in.readInt();
    }

    public static final Creator<Movies> CREATOR = new Creator<Movies>() {
        @Override
        public Movies createFromParcel(Parcel in) {
            return new Movies(in);
        }

        @Override
        public Movies[] newArray(int size) {
            return new Movies[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // Taking the parcel object and saving all data of it into instance variables
        dest.writeString(mTitle);
        dest.writeString(mPoster);
        dest.writeString(mOverview);
        dest.writeString(mReleaseDate);
        dest.writeString(mUserRating);
        dest.writeInt(mMovieId);
    }


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

    public void setReleaseDate(String mReleaseDate) {
        this.mReleaseDate = mReleaseDate;
    }

    public void setUserRating(String mUserRating) {
        this.mUserRating = mUserRating;
    }

    public void setIdMovie(int mMovieId) {
        this.mMovieId = mMovieId;
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
        return mReleaseDate;
    }

    public String getUserRating() {
        return mUserRating;
    }

    public int getmMovieId() {
        return mMovieId;
    }

}
