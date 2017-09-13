package com.prem.android.popularmovies.utils;

/**
 * Created by Prem on 21-07-2017.
 * This class handles the all formatting before shown to UI
 */
public final class FormatUtils {

    private FormatUtils() {
        throw new AssertionError();
    }

    public static String getFormattedRating(String rating) {
        return rating + "/" + "10";
    }

}
