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
 * Created by panguowei on 16-7-3.
 */
public class VideoDecryptTask extends BaseCryptTask {
    private VideoBean mVideoBean;

    public VideoDecryptTask(VideoBean bean) {
        mVideoBean = bean;
    }

    @Override
    public void onStart() {
        File oriFile = new File(mVideoBean.getEncryptPath());
        File toFile = new File(mVideoBean.getOriPath());
        oriFile.renameTo(toFile);
        PGW.log("renamed from " + oriFile.getPath() + " to " + toFile.getPath());

        VideoDbModel videoModel = MainApplication.getDBManager().getVideoModel();

        videoModel.deleteVideo(mVideoBean);
        EventBus.getDefault().post(new VideoEvents.EventEncryptVideoChanged(mVideoBean, VideoEvents.EventEncryptVideoChanged.TYPE_DELETE));
        PGW.log("postEvent  EventEncryptVideoChanged.TYPE_DELETE");

        try {
            MainApplication.getContext().getContentResolver().delete(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, MediaStore.Video.Media.DATA + "=?", new String[]{mVideoBean.getOriPath()});
            MediaScannerConnection.scanFile(MainApplication.getContext(), new String[]{mVideoBean.getOriPath()}, null, null);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
