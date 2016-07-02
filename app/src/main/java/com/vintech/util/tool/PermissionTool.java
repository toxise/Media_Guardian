package com.vintech.util.tool;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

/**
 * Created by Vincent on 2016/5/30.
 */
public class PermissionTool {

    public static final boolean hasPermission(Context context, String permission) {
        if (Machine.IS_M) {
            return ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        }
        return true;
    }
}
