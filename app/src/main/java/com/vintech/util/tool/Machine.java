package com.vintech.util.tool;

import android.os.Build;

import com.vintech.mediaguardian.MainApplication;

/**
 * Created by Vincent on 2016/5/30.
 */
public class Machine {
    public static final boolean IS_M = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    public static final String OBB_PATH = MainApplication.getContext().getFilesDir().getPath();
}
