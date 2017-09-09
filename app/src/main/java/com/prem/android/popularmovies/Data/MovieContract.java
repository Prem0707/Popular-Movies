package com.prem.android.popularmovies.Data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Prem on 22-08-2017.
 * It is designed to keep track of all the constants that will help to access data in given database.
 */
public class MovieContract {

    // Content Authority
    public static final String CONTENT_AUTHORITY = "";

    /**
     * The content authority is used to create the base of all URIs which apps will use to
     *  contact this content provider.
     */
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    /**
     * A list of possible paths that will be appended to the base URI for each of the different
     * tables.
     */
    public static final String TABLE_MOVIE_NAME = "movies";


    /* MovieEntry is an inner class that defines the contents of the Movie table */

    public static final class MovieEntry implements BaseColumns {

        // Content URI represents the base location for the table
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(TABLE_MOVIE_NAME).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_MOVIE_NAME;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_MOVIE_NAME;

        //Movie table contents
        public static final String MOVIE_ID = "id";
        public static final String MOVIE_POSTER_PATH = "poster_path";
        public static final String MOVIE_TITLE = "title";
        public static final String MOVIE_DESCRIPTION = "description";
        public static final String MOVIE_RATING = "rating";
        public static final String MOVIE_RELEASE_DATE = "release_date";

        public static Uri buildMovieUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
