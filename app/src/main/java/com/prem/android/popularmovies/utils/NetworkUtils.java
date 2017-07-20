package com.prem.android.popularmovies.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by Prem on 17-07-2017.
 * <p/>
 * This is a Utils class that handles operation related to networking and fetches information
 * from the themoviedb by using API
 */

public class NetworkUtils {

    //Key - Add your API Key here
    private static final String API_KEY = "89cf5fe65a50f63e8186c52216610f37";

    //Base URL
    private static final String MOVIE_BASE_URL = "http://api.themoviedb.org/3/movie";
    private static final String BASE_PICASSO_URL = "http://image.tmdb.org/t/p/";

    //Methods we want to use
    private static final String POPULAR_MOVIES = "popular";
    private static final String TOP_RATED_MOVIES = "top_rated";

    //Query paths
    private static final String API_KEY_PARAM = "api_key";

    private static final String IMAGE_SIZE = "w185";


    //Build the URL to query the movies sort selected by the user
    public static URL buildURL(String userSelectedCategory) {

        Uri buildUrl = Uri.parse(MOVIE_BASE_URL).buildUpon()
                .appendPath(userSelectedCategory)
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
     * @throws IOException related to network and stream reading
     * @params url The URL to fetch the HTTP response from.
     * @returns The contents of Http response
     */

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    // return a complete picasso url
    public static String buildPicassoUrl(String posterPathReturned) {
        return BASE_PICASSO_URL + IMAGE_SIZE + "/" + posterPathReturned;
    }

    // check if device is online
    public static boolean isDeviceOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

}