package com.prem.android.popularmovies.interfaces;

import com.prem.android.popularmovies.models.Trailers;

import java.util.ArrayList;

/**
 * Created by Prem on 19-08-2017.
 * Whenever we need to call to another thread and expecting some result return you need to implement this interface
 */
public interface TaskCompleted {

    // Define data you like to return from AsyncTask
    void onTaskCompleted(ArrayList<Trailers> moviesTrailers);

}
