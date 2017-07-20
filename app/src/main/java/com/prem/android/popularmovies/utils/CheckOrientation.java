package com.prem.android.popularmovies.utils;

import android.content.Context;

/**
 * Created by Prem on 20-07-2017.
 */
public class CheckOrientation {

    public static int getDeviceOrientation(Context context){
        return context.getResources().getConfiguration().orientation;
    }
}
