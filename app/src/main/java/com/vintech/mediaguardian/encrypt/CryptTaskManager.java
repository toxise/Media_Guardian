package com.vintech.mediaguardian.encrypt;

import android.os.Handler;
import android.os.HandlerThread;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vincent on 2016/5/28.
 */
public class CryptTaskManager {
    private static List<BaseCryptTask> sTasks = new ArrayList<>();
    private static Handler sHandler;

    static {
        HandlerThread thread = new HandlerThread("encrypt-thread");
        thread.start();
        sHandler = new Handler(thread.getLooper());
    }

    public synchronized static void addTask(BaseCryptTask task) {
        sTasks.add(task);
        sHandler.post(task);
    }

    public synchronized static void onTaskFinished(BaseCryptTask task) {
        sTasks.remove(task);
    }

}
