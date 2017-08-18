package com.prem.android.popularmovies.utils;

/**
 * Created by Prem on 18-08-2017.
 */
public class Constants {

    // Constants used in class TheMovieDbJsonUtils
    //The Movie Database JSON Keys
    public static final String RESULTS = "results";
    public static final String TITLE_OF_MOVIE = "title";
    public static final String OVERVIEW_OF_MOVIE = "overview";
    public static final String THUMBNAIL_OF_MOVIE = "poster_path";
    public static final String RELEASE_DATE = "release_date";
    public static final String RATING_OF_MOVIE = "vote_average";

    public static final String CURRENT_MOVIE_DATA = "current_movie_data";

     //Constants used in class NetworkUtils
    //Key - Add your API Key here
    public static final String API_KEY = "89cf5fe65a50f63e8186c52216610f37";

    //Base URL
    public static final String MOVIE_BASE_URL = "http://api.themoviedb.org/3/movie";
    public static final String BASE_PICASSO_URL = "http://image.tmdb.org/t/p/";

    //Methods we want to use
    public static final String POPULAR_MOVIES_SORT_SELECTION = "popular";
    public static final String TOP_RATED_MOVIES_SORT_SELECTION= "top_rated";

    //Query paths
    public static final String API_KEY_PARAM = "api_key";
    public static final String IMAGE_SIZE = "w185";


}
