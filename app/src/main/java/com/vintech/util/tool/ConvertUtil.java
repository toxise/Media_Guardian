package com.vintech.util.tool;


import com.vintech.mediaguardian.MainApplication;

/**
 * Created by Vincent on 2016/2/28.
 */
public class ConvertUtil {
    public static final int SECOND = 1000;
    public static final int MINUTES = 60 * SECOND;
    public static final int HOURS = 60 * MINUTES;
    public static final int DAY = 24 * HOURS;


    public static int getPixel(int resId) {
        return MainApplication.getContext().getResources().getDimensionPixelSize(resId);
    }

    public static float dip2Px(int dip) {
        return MainApplication.getContext().getResources().getDisplayMetrics().density * dip;
    }
}
