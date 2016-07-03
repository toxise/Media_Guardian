package com.vintech.mediaguardian.encrypt;

/**
 * Created by Vincent on 2016/5/28.
 */
public abstract class BaseCryptTask implements Runnable {
    public static final String ENCRYPT_NAME = ".guardian";

    @Override
    public final void run() {
        onStart();
        CryptTaskManager.onTaskFinished(this);
    }

    public abstract void onStart();
}
