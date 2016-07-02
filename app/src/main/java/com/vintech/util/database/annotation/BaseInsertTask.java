package com.vintech.util.database.annotation;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

/**
 * Created by Vincent on 2016/5/28.
 */
public class BaseInsertTask<T, V> implements Runnable {
    private Dao<T, V> mDao;
    private T mBean;

    public BaseInsertTask(Dao<T, V> dao, T bean) {
        mDao = dao;
        mBean = bean;
    }

    @Override
    public void run() {
        try {
            mDao.create(mBean);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
