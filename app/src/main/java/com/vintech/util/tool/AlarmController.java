package com.vintech.util.tool;

import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vincent on 2016/2/27.
 */
public class AlarmController {
    private static List<TimeTask> sTaskList = new ArrayList<>();
    private static Handler sHandler = new Handler() {
        private List<TimeTask> mTempList = new ArrayList<>();

        @Override
        public void handleMessage(Message msg) {
            mTempList.clear();
            synchronized (sTaskList) {
                for (int i = 0; i < sTaskList.size(); i++) {
                    TimeTask task = sTaskList.get(i);
                    if (task.targetTime <= SystemClock.uptimeMillis()) {
                        mTempList.add(task);
                    } else {
                        break;
                    }
                }
                sTaskList.removeAll(mTempList);
            }
            for (int i = 0; i < mTempList.size(); i++) {
                TimeTask task = mTempList.get(i);
                executeTask(task);
                if (task.repeatCount == 1) {
                    continue;
                } else {
                    task.targetTime = SystemClock.uptimeMillis() + task.repeatTime;
                    task.scheduled = false;
                    if (task.repeatCount > 1) {
                        task.repeatCount--;
                    }
                    addTask(task);
                }
            }
            scheduleNext();
        }
    };

    public static void removeTask(Runnable runnable) {
        synchronized (sTaskList) {
            for (int i = sTaskList.size() - 1; i >= 0; i--) {
                TimeTask t = sTaskList.get(i);
                if (t.runnable == runnable) {
                    sTaskList.remove(t);
                }
            }
        }
        scheduleNext();
    }

    public static void addTimeTask(long runTime, int repeatCount, int repeatTime, Runnable runnable) {
        TimeTask run = new TimeTask();
        run.targetTime = SystemClock.uptimeMillis() + runTime;
        run.repeatCount = repeatCount;
        run.repeatTime = repeatTime;
        run.runnable = runnable;
        addTask(run);
    }

    public static void addTimeTask(long runTime, Runnable runnable) {
        TimeTask run = new TimeTask();
        run.targetTime = SystemClock.uptimeMillis() + runTime;
        run.runnable = runnable;
        addTask(run);
    }

    private static void addTask(TimeTask task) {
        synchronized (sTaskList) {
            int index = -1;
            for (int i = 0; i < sTaskList.size(); i++) {
                TimeTask t = sTaskList.get(i);
                if (t.targetTime > task.targetTime) {
                    index = i;
                    break;
                }
            }

            if (index >= 0) {
                sTaskList.add(index, task);
            } else {
                sTaskList.add(task);
            }
        }
        scheduleNext();
    }

    private synchronized static void scheduleNext() {
        synchronized (sTaskList) {
            if (sTaskList.size() == 0) {
                return;
            }
            TimeTask task = sTaskList.get(0);
            if (!task.scheduled) {
                task.scheduled = true;
                sHandler.sendEmptyMessageAtTime(0, task.targetTime);
            }
        }
    }

    private static void executeTask(final TimeTask task) {
        task.runnable.run();
    }

    static class TimeTask {
        private long targetTime;
        private int repeatTime;
        private int repeatCount = 1;
        private boolean scheduled = false;
        private Runnable runnable;
    }
}
