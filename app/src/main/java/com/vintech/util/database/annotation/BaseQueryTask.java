package com.vintech.util.database.annotation;

import com.j256.ormlite.dao.Dao;

/**
 * Created by Vincent on 2016/5/29.
 */
public abstract class BaseQueryTask<T, V> implements Runnable {
    private Dao<T, V> mDao;
    public BaseQueryTask(Dao<T, V> t) {
        mDao = t;
    }
    @Override
    public final void run() {
        onStart();
    }

    public abstract void onStart();

    public Dao<T, V> getDao() {
        return mDao;
    }
}
