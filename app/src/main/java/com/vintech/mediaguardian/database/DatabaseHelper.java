package com.vintech.mediaguardian.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.vintech.mediaguardian.photo.gallery.model.PhotoBean;
import com.vintech.mediaguardian.video.fullview.model.VideoSnapshotBean;
import com.vintech.mediaguardian.video.gallery.model.VideoBean;
import com.vintech.util.database.BaseDatabaseHelper;

/**
 * Created by Vincent on 2016/5/28.
 */
public class DatabaseHelper extends BaseDatabaseHelper {
    public static final String DB_NAME = "media_protector";
    public static final int DB_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, PhotoBean.class);
            TableUtils.createTable(connectionSource, VideoBean.class);
            TableUtils.createTable(connectionSource, VideoSnapshotBean.class);
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public final void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        new DatabaseUpgradeHandler().onUpgrade(database, connectionSource, oldVersion, newVersion);
    }
}
