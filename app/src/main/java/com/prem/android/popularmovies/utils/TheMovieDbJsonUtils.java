package com.prem.android.popularmovies.utils;

import com.prem.android.popularmovies.Models.Movies;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Prem on 18-07-2017.
 * This is a util class for parsing the JSON data returned by API and returned as Movie Object.
 */
public class TheMovieDbJsonUtils {

    //The Movie Database JSON Keys
    private static final String RESULTS = "results";
    private static final String TITLE_OF_MOVIE = "title";
    private static final String OVERVIEW_OF_MOVIE = "overview";
    private static final String THUMBNAIL_OF_MOVIE = "poster_path";
    private static final String RELEASE_DATE = "release_date";
    private static final String RATING_OF_MOVIE = "vote_average";

    public static ArrayList<Movies> getMovieListFromJson(String responseFromAPI) throws JSONException {

        ArrayList<Movies> movieList = new ArrayList<>();

        JSONObject reader = new JSONObject(responseFromAPI);
        JSONArray arrayOfMoviesObject = reader.getJSONArray(RESULTS);

        //Parsing the objects of reader Array

        for (int i = 0; i < arrayOfMoviesObject.length(); i++) {

            Movies currentMovie = new Movies();

            JSONObject objectOfReaderArray = arrayOfMoviesObject.getJSONObject(i);

            currentMovie.setTitle(objectOfReaderArray.getString(TITLE_OF_MOVIE));
            currentMovie.setPoster(objectOfReaderArray.getString(THUMBNAIL_OF_MOVIE));
            currentMovie.setReleaseDate(objectOfReaderArray.getString(RELEASE_DATE));
            currentMovie.setUserRating(objectOfReaderArray.getString(RATING_OF_MOVIE));
            currentMovie.setOverview(objectOfReaderArray.getString(OVERVIEW_OF_MOVIE));

            movieList.add(currentMovie);
        }
        return movieList;
    }
}
