package com.prem.android.popularmovies.utils;

import android.content.Context;
import android.os.AsyncTask;

import com.prem.android.popularmovies.interfaces.TaskCompleted;
import com.prem.android.popularmovies.json_parser.TrailerJsonParser;
import com.prem.android.popularmovies.models.Trailers;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Prem on 19-08-2017.
 */
public class AsyncReuse extends AsyncTask<String, Void, ArrayList<Trailers>> {

    private final TaskCompleted mCallback;

    public AsyncReuse(Context context) {
        this.mCallback = (TaskCompleted) context;

    }

    @Override
    protected ArrayList<Trailers> doInBackground(String... params) {

        String sortOptionSelected = params[0];
        URL urlForFetchMovieDetails = NetworkUtils.buildURL(sortOptionSelected + "/videos");

        if (urlForFetchMovieDetails != null) {
            try {
                String responseFromAPI = null;
                try {
                    responseFromAPI = NetworkUtils.getResponseFromHttpUrl(urlForFetchMovieDetails);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return TrailerJsonParser.getTrailersFromJson(responseFromAPI);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<Trailers> moviesTrailers) {
        super.onPostExecute(moviesTrailers);
        //This is where you return data back to caller
        mCallback.onTaskCompleted(moviesTrailers);
    }
}
