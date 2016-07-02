package com.vintech.mediaguardian.photo.gallery;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.vintech.mediaguardian.R;
import com.vintech.mediaguardian.framework.FrameEvent;
import com.vintech.mediaguardian.framework.IWorkspaceFrame;
import com.vintech.mediaguardian.photo.PhotoEvents;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by Vincent on 2016/5/28.
 */
public class PhotoGalleryLayout extends FrameLayout implements View.OnClickListener, IWorkspaceFrame {
    private FloatingActionButton mPlus;
    private RecyclerView mGallery;
    private PhotoGalleryAdapter mAdapter;

    public PhotoGalleryLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mPlus = (FloatingActionButton) findViewById(R.id.plus);
        mGallery = (RecyclerView) findViewById(R.id.gallery);
        mGallery.addItemDecoration(new PhotoGalleryItemDecoration());
        mAdapter = new PhotoGalleryAdapter();
        mGallery.setAdapter(mAdapter);

        GridLayoutManager manager = new GridLayoutManager(getContext(), 3);
        mGallery.setLayoutManager(manager);
        mPlus.setOnClickListener(this);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.plus:
                EventBus.getDefault().post(new FrameEvent.EventSetLayout(R.id.photo_pick_layout));
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventEncryptPhotoChanged(PhotoEvents.EventEncryptPhotoChanged event) {
        switch (event.type) {
            case PhotoEvents.EventEncryptPhotoChanged.TYPE_BIND:
            case PhotoEvents.EventEncryptPhotoChanged.TYPE_ADD:
                mAdapter.addPhotoBean(event.photoBean);
                mAdapter.notifyDataSetChanged();
                break;

        }
    }

    @Override
    public boolean handleBackKey() {
        return false;
    }
}
