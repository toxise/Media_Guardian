package com.vintech.util.tool;

import android.content.Context;
import android.content.Intent;

/**
 * Created by vincent on 2015/12/19.
 */
public class AppUtil {

    public static boolean startActivity(Context context, Intent intent) {
        if (context == null || intent == null) {
            return false;
        }

        try {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            return true;
        } catch (Throwable e) {
        }
        return false;
    }
}
