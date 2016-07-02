package com.vintech.mediaguardian.photo;

import com.vintech.mediaguardian.photo.gallery.model.PhotoBean;

/**
 * Created by Vincent on 2016/5/29.
 */
public class PhotoEvents {
    public static class EventEncryptPhotoChanged {
        public static final int TYPE_BIND = 0;
        public static final int TYPE_ADD = 1;
        public static final int TYPE_DELETE = 2;
        public static final int TYPE_UPDATE = 3;
        public PhotoBean photoBean;
        public int type;

        public EventEncryptPhotoChanged(PhotoBean bean, int t) {
            photoBean = bean;
            type = t;
        }

    }
}
