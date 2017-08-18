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
 * This is a Utils class that handles operation related to networking and fetches information
 * from the themoviedb by using API
 */

public class NetworkUtils {

    //Build the URL to query the movies sort selected by the user
    public static URL buildURL(String userSelectedCategory) {

        Uri buildUrl = Uri.parse(Constants.MOVIE_BASE_URL).buildUpon()
                .appendPath(userSelectedCategory)
                .appendQueryParameter(Constants.API_KEY_PARAM, Constants.API_KEY)
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
     * throws IOException related to network and stream reading
     * params url The URL to fetch the HTTP response from.
     * returns The contents of Http response
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
        return Constants.BASE_PICASSO_URL + Constants.IMAGE_SIZE + "/" + posterPathReturned;
    }

    // check if device is online
    public static boolean checkDeviceOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

}