package com.vintech.mediaguardian.video.gallery.model;

import com.j256.ormlite.dao.Dao;
import com.vintech.mediaguardian.util.PGW;
import com.vintech.mediaguardian.video.VideoEvents;
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
public class VideoDbModel {
    private Dao<VideoBean, Integer> mDao;
    private List<VideoBean> mVideos = new ArrayList<>();

    public VideoDbModel(Dao<VideoBean, Integer> dao) {
        mDao = dao;
    }

    public void startLoader() {
        try {
            List<VideoBean> list = mDao.queryForAll();
            if (list != null) {
                for (VideoBean bean : list) {
                    mVideos.add(bean);
                    PGW.log("bean=  " + bean);
                    EventBus.getDefault().post(new VideoEvents.EventEncryptVideoChanged(bean, VideoEvents.EventEncryptVideoChanged.TYPE_BIND));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<VideoBean> getVideos() {
        return new ArrayList<>(mVideos);
    }

    public VideoBean findVideoById(long id) {
        List<VideoBean> videos = getVideos();
        for (int i = 0; i < videos.size(); i++) {
            if (videos.get(i).getId() == id) {
                return videos.get(i);
            }
        }
        return null;
    }

    public void insertVideo(VideoBean bean) {
        mVideos.add(bean);
        BaseDatabaseHelper.post(new BaseInsertTask(mDao, bean));
    }

    public void deleteVideo(VideoBean bean) {
        mVideos.remove(bean);
        BaseDatabaseHelper.post(new BaseDeleteTask(mDao, bean));
    }

    public void updateVideo(VideoBean bean) {
        BaseDatabaseHelper.post(new BaseUpdateTask(mDao, bean));
    }
}
