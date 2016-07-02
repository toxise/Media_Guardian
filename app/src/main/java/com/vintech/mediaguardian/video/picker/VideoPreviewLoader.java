package com.vintech.mediaguardian.video.picker;

import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.Looper;
import android.provider.MediaStore;
import android.widget.ImageView;

import com.vintech.mediaguardian.R;
import com.vintech.mediaguardian.util.PGW;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by Vincent on 2016/6/1.
 */
public class VideoPreviewLoader {
    public static Executor mExecutor = Executors.newFixedThreadPool(3);
    private static final int TAG_ID = R.id.workspace;


    public static void display(String path, ImageView imageView) {
        VideoPreviewLoadTask task = new VideoPreviewLoadTask(path, imageView);
        mExecutor.execute(task);
    }

    static class VideoPreviewLoadTask implements Runnable {
        private String mPath;
        private Bitmap mBitmap;
        private ImageView mTargetImage;

        public VideoPreviewLoadTask(String path, ImageView imageView) {
            mPath = path;
            mTargetImage = imageView;
            mTargetImage.setTag(TAG_ID, mPath);
        }

        @Override
        public void run() {
            if (Looper.getMainLooper() == Looper.myLooper()) {
                if (mPath.equals(mTargetImage.getTag(TAG_ID))) {
                    mTargetImage.setImageBitmap(mBitmap);
                }
            } else {
                mBitmap = ThumbnailUtils.createVideoThumbnail(mPath, MediaStore.Video.Thumbnails.MICRO_KIND);
                if (mBitmap != null) {
                    PGW.log("VideoPreviewTask  bitmap  w=" + mBitmap.getWidth() + ", h=" + mBitmap.getHeight());
                }
                mTargetImage.post(this);
            }
        }
    }
}
