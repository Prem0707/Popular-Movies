package com.prem.android.popularmovies.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Prem on 22-08-2017.
 */
class MovieDbHelper extends SQLiteOpenHelper {

    // The name of the database
    private static final String DATABASE_NAME = "Movie.db";

    // If you change the database schema, you must increment the database version
    private static final int DATABASE_VERSION = 1;

    // Constructor
    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Called when the tasks database is created for the first time.
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Create Movie table
        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + MovieContract.TABLE_MOVIE_NAME + " (" +
                MovieContract.MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                MovieContract.MovieEntry.MOVIE_ID + " INTEGER NOT NULL, " +
                MovieContract.MovieEntry.MOVIE_POSTER_PATH + " TEXT NOT NULL, " +
                MovieContract.MovieEntry.MOVIE_DESCRIPTION + " TEXT NOT NULL, " +
                MovieContract.MovieEntry.MOVIE_TITLE + " TEXT NOT NULL, " +
                MovieContract.MovieEntry.MOVIE_RATING + " TEXT NOT NULL, " +
                MovieContract.MovieEntry.MOVIE_RELEASE_DATE + " TEXT NOT NULL " + " );";

        sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_TABLE);

    }

    /**
     * This method discards the old table of data and calls onCreate to recreate a new one.
     * This only occurs when the version number for this database (DATABASE_VERSION) is incremented.
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXIST" + MovieContract.TABLE_MOVIE_NAME);
        onCreate(sqLiteDatabase);
    }
}
