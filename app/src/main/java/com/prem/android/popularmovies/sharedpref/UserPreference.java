package com.prem.android.popularmovies.sharedpref;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Prem on 07-09-2017.
 */
public class UserPreference {

    public static void setSharedPref(String value, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("SORT_ACCORDING_TO_USER_PREF", value);
        editor.apply();
    }

    public static String getSharedPref(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString("SORT_ACCORDING_TO_USER_PREF","popular");
    }
}
