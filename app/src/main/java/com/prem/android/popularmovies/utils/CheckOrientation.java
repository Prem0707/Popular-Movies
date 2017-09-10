package com.prem.android.popularmovies.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.support.v7.widget.GridLayoutManager;

/**
 * Created by Prem on 20-07-2017.
 * This class handles the device orientation
 */
public class CheckOrientation {

    private static int checkDeviceOrientation(Context context) {
        return context.getResources().getConfiguration().orientation;
    }


    public static GridLayoutManager gridLayoutManagerAccordingToOrientation(Context context) {
        int deviceOrientation = CheckOrientation.checkDeviceOrientation(context);

        if (deviceOrientation == Configuration.ORIENTATION_PORTRAIT) {
            return new GridLayoutManager(context, 2);
        } else
            return new GridLayoutManager(context, 3);

    }
}
