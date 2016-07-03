package com.vintech.mediaguardian.encrypt;

import android.media.MediaScannerConnection;
import android.provider.MediaStore;

import com.vintech.mediaguardian.MainApplication;
import com.vintech.mediaguardian.util.PGW;
import com.vintech.mediaguardian.video.VideoEvents;
import com.vintech.mediaguardian.video.gallery.model.VideoBean;
import com.vintech.mediaguardian.video.gallery.model.VideoDbModel;

import org.greenrobot.eventbus.EventBus;

import java.io.File;

/**
 * Created by Vincent on 2016/5/28.
 */
public class VideoEncryptTask extends BaseCryptTask {
    private String mOriPath;

    public VideoEncryptTask(String str) {
        mOriPath = str;
    }

    @Override
    public void onStart() {
        File oriFile = new File(mOriPath);
        File toFile = new File(mOriPath + ENCRYPT_NAME);
        oriFile.renameTo(toFile);
        PGW.log("renamed from " + oriFile.getPath() + " to " + toFile.getPath());

        VideoDbModel videoModel = MainApplication.getDBManager().getVideoModel();

        VideoBean bean = new VideoBean();
        bean.setOriPath(oriFile.getPath());
        bean.setEncryptPath(toFile.getPath());
        videoModel.insertVideo(bean);
        EventBus.getDefault().post(new VideoEvents.EventEncryptVideoChanged(bean, VideoEvents.EventEncryptVideoChanged.TYPE_ADD));
        PGW.log("postEvent  EventEncryptVideoChanged.TYPE_ADD");

        try {
            MainApplication.getContext().getContentResolver().delete(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, MediaStore.Video.Media.DATA + "=?", new String[]{mOriPath});
            MediaScannerConnection.scanFile(MainApplication.getContext(), new String[]{mOriPath}, null, null);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
