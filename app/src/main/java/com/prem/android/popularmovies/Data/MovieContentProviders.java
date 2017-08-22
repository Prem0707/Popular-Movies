package com.prem.android.popularmovies.Data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by Prem on 22-08-2017.
 */
public class MovieContentProviders extends ContentProvider {

    private MovieDbHelper movieDbHelper;

    // onCreate() is where you should initialize anything we need to setup your underlying data source
    @Override
    public boolean onCreate() {
        //In this case, you’re working with a SQLite database, so you’ll need to initialize a DbHelper to gain access to it.
        Context context = getContext();
        movieDbHelper = new MovieDbHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] strings, String s, String[] strings1, String s1) {
        return null;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }
}
