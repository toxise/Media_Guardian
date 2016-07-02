package com.vintech.mediaguardian.photo.gallery.model;

import com.j256.ormlite.dao.Dao;
import com.vintech.mediaguardian.photo.PhotoEvents;
import com.vintech.mediaguardian.util.PGW;
import com.vintech.util.database.BaseDatabaseHelper;
import com.vintech.util.database.annotation.BaseDeleteTask;
import com.vintech.util.database.annotation.BaseInsertTask;
import com.vintech.util.database.annotation.BaseUpdateTask;

import org.greenrobot.eventbus.EventBus;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vincent on 2016/5/28.
 */
public class PhotoDbModel {
    private Dao<PhotoBean, Integer> mDao;
    private List<PhotoBean> mPhotos = new ArrayList<>();

    public PhotoDbModel(Dao<PhotoBean, Integer> dao) {
        mDao = dao;
    }

    public void startLoader() {
        try {
            List<PhotoBean> list = mDao.queryForAll();
            if (list != null) {
                for (PhotoBean bean : list) {
                    mPhotos.add(bean);
                    PGW.log("bean=  " + bean);
                    EventBus.getDefault().post(new PhotoEvents.EventEncryptPhotoChanged(bean, PhotoEvents.EventEncryptPhotoChanged.TYPE_BIND));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<PhotoBean> getPhotos() {
        return new ArrayList<>(mPhotos);
    }

    public void insertPhoto(PhotoBean bean) {
        mPhotos.add(bean);
        BaseDatabaseHelper.post(new BaseInsertTask(mDao, bean));
    }

    public void deletePhoto(PhotoBean bean) {
        mPhotos.remove(bean);
        BaseDatabaseHelper.post(new BaseDeleteTask(mDao, bean));
    }

    public void updatePhoto(PhotoBean bean) {
        BaseDatabaseHelper.post(new BaseUpdateTask(mDao, bean));
    }
}
