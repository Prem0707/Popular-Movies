package com.prem.android.popularmovies.Activity.utils;

import android.net.Uri;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Prem on 17-07-2017.
 * This is a Utils class that handles operation related to networking and fetches information
 * from the themoviedb by using API
 */

public class NetworkUtils {

    //Key - Add your API Key here
    private static final String API_KEY = "89cf5fe65a50f63e8186c52216610f37";

   //Base URL
    private static final String MOVIE_BASE_URL = "http://api.themoviedb.org/3/movie";

    //Methods we want to use
    private static final String POPULAR_MOVIES = "popular";
    private static final String TOP_RATED_MOVIES = "top_rated";

   //Query paths
    private static final String API_KEY_PARAM = "api_key";


    //Build the URL to query the movies sort selected by the user
    public static URL buildURL(String POPULAR_MOVIES){

        Uri buildUrl = Uri.parse(MOVIE_BASE_URL).buildUpon()
                .appendPath(POPULAR_MOVIES)
                .appendQueryParameter(API_KEY_PARAM, API_KEY)
                .build();

        return convertAndroidUrltoJavaUrl(buildUrl);
    }

    private static URL convertAndroidUrltoJavaUrl(Uri uriToConvert) {
        URL url = null;
        try {
            url = new URL(uriToConvert.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    /**
     * This method returns the entire result from the Http response
     * 
     */
  }


