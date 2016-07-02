package com.vintech.mediaguardian.video.picker;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Vincent on 2016/5/27.
 */
public class VideoPickerItemDecoration extends RecyclerView.ItemDecoration {
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.set(1, 1, 1, 1);
    }
}
