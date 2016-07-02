package com.vintech.mediaguardian;

import android.app.Application;
import android.content.Context;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.vintech.mediaguardian.database.DatabaseManager;

/**
 * Created by Vincent on 2016/5/27.
 */
public class MainApplication extends Application {
    private DatabaseManager mDbManager;
    private static MainApplication sInstance;

    public MainApplication() {
        sInstance = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mDbManager = new DatabaseManager(this);
        initImageLoader();
    }

    private void initImageLoader() {
        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).build();
        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(this)
                .defaultDisplayImageOptions(options)
                .build();
        ImageLoader.getInstance().init(configuration);
    }

    public static final DatabaseManager getDBManager() {
        return sInstance.mDbManager;
    }

    public static Context getContext() {
        return sInstance;
    }
}
