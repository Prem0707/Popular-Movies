package com.prem.android.popularmovies.Json_Parser;


import com.prem.android.popularmovies.Models.Reviews;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Prem on 05-09-2017.
 */
public class ReviewsParser {

    public static ArrayList<Reviews> getReviewsFeomJson (String responseFromAPI) throws JSONException {

        ArrayList<Reviews> reviews = new ArrayList<>();

        JSONObject reader = new JSONObject(responseFromAPI);


            JSONArray arrayOfMoviesReview = reader.getJSONArray("results");

            //Parsing the objects of reader Array

            for (int i = 0; i < arrayOfMoviesReview.length(); i++) {
                Reviews review = new Reviews();

                JSONObject objectOfResultArray = arrayOfMoviesReview.getJSONObject(i);

                review.setAuthor(objectOfResultArray.getString("author"));
                review.setContent(objectOfResultArray.getString("content"));


                reviews.add(review);
            }
        return reviews;
    }
}
