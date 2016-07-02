package com.vintech.mediaguardian.encrypt;

import android.media.MediaScannerConnection;
import android.provider.MediaStore;

import com.vintech.mediaguardian.MainApplication;
import com.vintech.mediaguardian.photo.PhotoEvents;
import com.vintech.mediaguardian.photo.gallery.model.PhotoBean;
import com.vintech.mediaguardian.photo.gallery.model.PhotoDbModel;

import org.greenrobot.eventbus.EventBus;

import java.io.File;

/**
 * Created by Vincent on 2016/5/28.
 */
public class PhotoEncryptTask extends BaseEncryptTask {
    public static final String ENCRYPT_NAME = ".mpphoto";
    private String mOriPath;

    public PhotoEncryptTask(String str) {
        mOriPath = str;
    }

    @Override
    public void onStart() {
        File oriFile = new File(mOriPath);
        File toFile = new File(mOriPath + ENCRYPT_NAME);
        oriFile.renameTo(toFile);

        PhotoDbModel photoModel = MainApplication.getDBManager().getPhotoModel();

        PhotoBean bean = new PhotoBean();
        bean.setOriPath(oriFile.getPath());
        bean.setEncryptPath(toFile.getPath());
        photoModel.insertPhoto(bean);
        EventBus.getDefault().post(new PhotoEvents.EventEncryptPhotoChanged(bean, PhotoEvents.EventEncryptPhotoChanged.TYPE_ADD));

        try {
            MainApplication.getContext().getContentResolver().delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, MediaStore.Images.Media.DATA + "=?", new String[]{mOriPath});
            MediaScannerConnection.scanFile(MainApplication.getContext(), new String[]{mOriPath}, null, null);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
