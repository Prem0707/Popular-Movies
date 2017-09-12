package com.prem.android.popularmovies.models;

/**
 * Created by Prem on 05-09-2017.
 */
public class Reviews {

    private String mAuthor;
    private String mContent;

    public void setContent(String content) {
        this.mContent = content;
    }

    public void setAuthor(String author) {
        this.mAuthor = author;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getContent() {
        return mContent;
    }
}
