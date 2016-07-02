package com.vintech.mediaguardian.database;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.vintech.mediaguardian.photo.gallery.model.PhotoBean;
import com.vintech.mediaguardian.photo.gallery.model.PhotoDbModel;
import com.vintech.mediaguardian.video.fullview.model.VideoSnapshotBean;
import com.vintech.mediaguardian.video.fullview.model.VideoSnapshotDbModel;
import com.vintech.mediaguardian.video.gallery.model.VideoBean;
import com.vintech.mediaguardian.video.gallery.model.VideoDbModel;
import com.vintech.util.database.BaseDatabaseHelper;

import java.sql.SQLException;

/**
 * Created by Vincent on 2016/5/28.
 */
public class DatabaseManager {
    private DatabaseHelper mDBHelper;
    private PhotoDbModel mPhotoDbModel;
    private VideoDbModel mVideoDbModel;
    private VideoSnapshotDbModel mVideoSnapshotDbModel;


    public DatabaseManager(Context context) {
        mDBHelper = new DatabaseHelper(context);
        mPhotoDbModel = new PhotoDbModel(getDao(PhotoBean.class));
        mVideoDbModel = new VideoDbModel(getDao(VideoBean.class));
        mVideoSnapshotDbModel = new VideoSnapshotDbModel(getDao(VideoSnapshotBean.class));
    }

    public void startModel() {
        BaseDatabaseHelper.post(new Runnable() {
            @Override
            public void run() {
                mPhotoDbModel.startLoader();
                mVideoDbModel.startLoader();
            }
        });
    }

    public PhotoDbModel getPhotoModel() {
        return mPhotoDbModel;
    }

    public VideoDbModel getVideoModel() {
        return mVideoDbModel;
    }

    public VideoSnapshotDbModel getVideoSnapshotDbModel() {
        return mVideoSnapshotDbModel;
    }

    private <D extends Dao<T, Integer>, T> D getDao(Class<T> cls) {
        try {
            return mDBHelper.getDao(cls);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
