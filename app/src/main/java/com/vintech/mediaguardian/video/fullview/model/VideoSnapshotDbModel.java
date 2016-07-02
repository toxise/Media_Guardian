package com.vintech.mediaguardian.video.fullview.model;

import com.j256.ormlite.dao.Dao;
import com.vintech.mediaguardian.database.DatabaseHelper;
import com.vintech.mediaguardian.util.PGW;
import com.vintech.mediaguardian.video.VideoEvents;
import com.vintech.mediaguardian.video.gallery.model.VideoBean;
import com.vintech.util.database.BaseDatabaseHelper;
import com.vintech.util.database.annotation.BaseDeleteTask;
import com.vintech.util.database.annotation.BaseInsertTask;
import com.vintech.util.database.annotation.BaseQueryTask;
import com.vintech.util.database.annotation.BaseUpdateTask;

import org.greenrobot.eventbus.EventBus;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Vincent on 2016/2/29.
 */
public class VideoSnapshotDbModel {
    private Dao<VideoSnapshotBean, Integer> mDao;

    public VideoSnapshotDbModel(Dao<VideoSnapshotBean, Integer> dao) {
        mDao = dao;
    }

    public Dao<VideoSnapshotBean, Integer> getDao() {
        return mDao;
    }

    public void getSnapshotByVideo(VideoBean video) {
        DatabaseHelper.post(new QuerySnapshotByVideoTask(mDao, video));
    }

    public void insertSnapshot(VideoSnapshotBean bean) {
        BaseDatabaseHelper.post(new BaseInsertTask(mDao, bean));
    }

    public void deleteSnapshot(VideoSnapshotBean bean) {
        BaseDatabaseHelper.post(new BaseDeleteTask(mDao, bean));
    }

    public void updateSnapshot(VideoSnapshotBean bean) {
        BaseDatabaseHelper.post(new BaseUpdateTask(mDao, bean));
    }

    public class QuerySnapshotByVideoTask extends BaseQueryTask<VideoSnapshotBean, Integer> {
        private VideoBean mVideo;

        public QuerySnapshotByVideoTask(Dao<VideoSnapshotBean, Integer> dao, VideoBean video) {
            super(dao);
            mVideo = video;
        }

        @Override
        public void onStart() {
            try {
                PGW.log("videosnapshotdbmodel.onStart");
                List<VideoSnapshotBean> video = getDao().queryForEq("video", mVideo.getId());
                PGW.log("videosnapshotdbmodel.onstart size= " + video.size());
                EventBus.getDefault().post(new VideoEvents.EventSnapshotLoaded(mVideo, video));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
