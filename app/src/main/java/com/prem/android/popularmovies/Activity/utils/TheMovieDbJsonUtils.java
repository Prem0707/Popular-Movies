package com.prem.android.popularmovies.Activity.utils;

import com.prem.android.popularmovies.Models.Movies;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Prem on 18-07-2017.
 * This is a util class for parsing the JSON data returned by API and returned as Movie Object.
 */
public class TheMovieDbJsonUtils{

 //The Movie Database JSON Keys
 private static final String TBMB_RESULTS = "results";
 private static final String TBMB_TITLE = "title";
 private static final String TBMB_OVERVIEW ="overview";
 private static final String TBMB_IMAGE_THUMBNAIL ="poster_path";
 private static final String TBMB_RELEASE_DATE ="release_date";
 private static final String TBMB_USER_RATING ="vote_average";

 public static ArrayList<Movies> getMovieListFromJson(String responseFromAPI) throws JSONException {

  ArrayList<Movies> movieList = new ArrayList<>();

  JSONObject reader = new JSONObject(responseFromAPI);
  JSONArray arrayOfMoviesObject = reader.getJSONArray(TBMB_RESULTS);

  //Parsing the objects of reader Array

  for(int i=0; i<arrayOfMoviesObject.length(); i++){

   Movies currentMovie = new Movies();

   JSONObject objectOfReaderArray = arrayOfMoviesObject.getJSONObject(i);

   currentMovie.setTitle(objectOfReaderArray.getString(TBMB_TITLE));
   currentMovie.setPoster(objectOfReaderArray.getString(TBMB_IMAGE_THUMBNAIL));
   currentMovie.setReleaseDate(objectOfReaderArray.getString(TBMB_RELEASE_DATE));
   currentMovie.setUserRating(objectOfReaderArray.getString(TBMB_USER_RATING));
   currentMovie.setOverview(objectOfReaderArray.getString(TBMB_OVERVIEW));

   movieList.add(currentMovie);
  }

  return movieList;

 }
}
