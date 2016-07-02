package com.vintech.util.database;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;

/**
 * Created by Vincent on 2016/5/28.
 */
public abstract class BaseDatabaseHelper extends OrmLiteSqliteOpenHelper {
    private static Handler handler;

    static {
        HandlerThread thread = new HandlerThread("db_thread");
        thread.start();
        handler = new Handler(thread.getLooper());
    }

    public BaseDatabaseHelper(Context context, String name, int version) {
        super(context, name, null, version);
    }

    public static void post(Runnable task) {
        if (task == null) {
            return;
        }
        handler.post(task);
    }
}
