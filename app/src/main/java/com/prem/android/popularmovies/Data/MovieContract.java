package com.prem.android.popularmovies.Data;

import android.provider.BaseColumns;

/**
 * Created by Prem on 22-08-2017.
 * It is designed to keep track of all the constants that will help to access data in given database.
 */
public class MovieContract {

    /* MovieEntry is an inner class that defines the contents of the Movie table */

    public static final class MovieEntry implements BaseColumns {

        // Table name
        public static final String TABLE_MOVIE_NAME = "movies";

        //Movie table contents
        public static final String MOVIE_ID = "id";
        public static final String MOVIE_POSTER_PATH = "poster_path";
        public static final String MOVIE_TITLE = "title";
        public static final String MOVIE_DESCRIPTION = "description";
        public static final String MOVIE_RATING = "rating";
        public static final String MOVIE_RELEASE_DATE = "release_date";

    }
}
