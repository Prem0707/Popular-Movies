package com.prem.android.popularmovies.utils;

import android.content.Context;

/**
 * Created by Prem on 20-07-2017.
 * This class handles the device orientation
 */
public class CheckOrientation {

    public static int checkDeviceOrientation(Context context){
        return context.getResources().getConfiguration().orientation;
    }
}
