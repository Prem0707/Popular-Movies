package com.prem.android.popularmovies.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Prem on 18-07-2017.
 */
public class Movies implements Parcelable{

    private String mTitle;
    private String mPoster;
    private String mOverview;
    private String mReleseDate;
    private String mUserRating;


    protected Movies(Parcel in) {
        mTitle = in.readString();
        mPoster = in.readString();
        mOverview = in.readString();
        mReleseDate = in.readString();
        mUserRating = in.readString();
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
        dest.writeString(mTitle);
        dest.writeString(mPoster);
        dest.writeString(mOverview);
        dest.writeString(mReleseDate);
        dest.writeString(mUserRating);
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
