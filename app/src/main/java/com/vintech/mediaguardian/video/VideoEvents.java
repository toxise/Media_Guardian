package com.vintech.mediaguardian.video;

import com.vintech.mediaguardian.video.fullview.model.VideoSnapshotBean;
import com.vintech.mediaguardian.video.gallery.model.VideoBean;

import java.util.List;

/**
 * Created by Vincent on 2016/5/29.
 */
public class VideoEvents {
    public static class EventEncryptVideoChanged {
        public static final int TYPE_BIND = 0;
        public static final int TYPE_ADD = 1;
        public static final int TYPE_DELETE = 2;
        public static final int TYPE_UPDATE = 3;
        public VideoBean photoBean;
        public int type;

        public EventEncryptVideoChanged(VideoBean bean, int t) {
            photoBean = bean;
            type = t;
        }
    }

    public static class EventSnapshotLoaded {
        public VideoBean mVideo;
        public List<VideoSnapshotBean> mSnapshot;

        public EventSnapshotLoaded(VideoBean mVideo, List<VideoSnapshotBean> mSnapshot) {
            this.mVideo = mVideo;
            this.mSnapshot = mSnapshot;
        }
    }

    public static class EventVideoPlayingChanged {
        public VideoBean bean;
        public long duration;

        public EventVideoPlayingChanged(VideoBean b, long du) {
            bean = b;
            duration = du;
        }
    }
}
