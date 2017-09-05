package com.prem.android.popularmovies.Json_Parser;

import com.prem.android.popularmovies.Models.Trailers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Prem on 05-09-2017.
 */
public class TrailerJsonParser {

    public static ArrayList<Trailers> getTrailersFeomJson (String responseFromAPI) throws JSONException {

        ArrayList<Trailers> trailersMovies = new ArrayList<>();

        JSONObject reader = new JSONObject(responseFromAPI);


        JSONArray arrayOfMoviesReview = reader.getJSONArray("results");

        //Parsing the objects of reader Array

        for (int i = 0; i < arrayOfMoviesReview.length(); i++) {
            Trailers trailers = new Trailers();

            JSONObject objectOfResultArray = arrayOfMoviesReview.getJSONObject(i);
            trailers.setmVideoKey(objectOfResultArray.getString("key"));

            trailersMovies.add(trailers);
        }
        return trailersMovies;
    }
}
