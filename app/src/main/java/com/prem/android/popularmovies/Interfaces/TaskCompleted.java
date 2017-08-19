package com.prem.android.popularmovies.Interfaces;

import com.prem.android.popularmovies.Models.Movies;

import java.util.ArrayList;

/**
 * Created by Prem on 19-08-2017.
 * Whenever we need to call to another thread and expecting some result return you need to implement this interface
 */
public interface TaskCompleted {

    // Define data you like to return from AysncTask
    public void onTaskCompleted(ArrayList<Movies> movies);

}
