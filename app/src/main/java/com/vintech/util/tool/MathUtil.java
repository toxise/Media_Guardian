package com.vintech.util.tool;

/**
 * Created by Vincent on 2016/2/29.
 */
public class MathUtil {

    public static float getDistance(float x1, float y1, float x2, float y2) {
        return (float) Math.sqrt(getDistancePow(x1, y1, x2, y2));
    }

    public static float getDistancePow(float x1, float y1, float x2, float y2) {
        return Math.abs((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
    }
}
