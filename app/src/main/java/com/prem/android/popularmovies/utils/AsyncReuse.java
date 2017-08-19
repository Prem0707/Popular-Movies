package com.prem.android.popularmovies.utils;

import android.content.Context;
import android.os.AsyncTask;

import com.prem.android.popularmovies.Interfaces.TaskCompleted;
import com.prem.android.popularmovies.Models.Movies;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Prem on 19-08-2017.
 */
public class AsyncReuse extends AsyncTask<String, Void, ArrayList<Movies>> {
    private Context mContext;
    private TaskCompleted mCallback;

    public AsyncReuse(Context context) {
        this.mContext = context;
        this.mCallback = (TaskCompleted) context;

    }

    @Override
    protected ArrayList<Movies> doInBackground(String... params) {

        String sortOptionSelected = params[0];
        URL urlForFetchMovieDetails = NetworkUtils.buildURL(sortOptionSelected);

        if (urlForFetchMovieDetails != null) {
            try {
                String responseFromAPI = null;
                try {
                    responseFromAPI = NetworkUtils.getResponseFromHttpUrl(urlForFetchMovieDetails);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return TheMovieDbJsonUtils.getMovieListFromJson(responseFromAPI);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<Movies> movies) {
        super.onPostExecute(movies);
        //This is where you return data back to caller
        mCallback.onTaskCompleted(movies);
    }
}
