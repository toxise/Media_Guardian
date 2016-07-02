package com.vintech.mediaguardian.photo.fullview;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;

/**
 * Created by Vincent on 2016/5/27.
 */
public class FullPhotoItemView extends AppCompatImageView {

    public FullPhotoItemView(Context context) {
        super(context);
        setScaleType(ScaleType.CENTER_INSIDE);
        setPadding(3, 3, 3, 3);
    }
}
