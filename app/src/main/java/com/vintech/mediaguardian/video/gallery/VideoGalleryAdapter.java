package com.vintech.mediaguardian.video.gallery;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.vintech.mediaguardian.R;
import com.vintech.mediaguardian.framework.FrameEvent;
import com.vintech.mediaguardian.util.PGW;
import com.vintech.mediaguardian.video.fullview.VideoPlayActivity;
import com.vintech.mediaguardian.video.gallery.model.VideoBean;
import com.vintech.mediaguardian.video.picker.VideoPreviewLoader;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Vincent on 2016/5/26.
 */
public class VideoGalleryAdapter extends RecyclerView.Adapter<VideoGalleryAdapter.GalleryViewHolder> implements View.OnClickListener, View.OnLongClickListener {
    private boolean mSelectMode = false;
    private Drawable mDefault;
    private RecyclerView mGalleryView;
    private List<VideoBean> mVideos = new ArrayList<>();
    private List<VideoBean> mVideoSelected = new ArrayList<>();

    public VideoGalleryAdapter(RecyclerView gallery) {
        mGalleryView = gallery;
    }

    public void addVideoBean(VideoBean bean) {
        mVideos.add(bean);
    }

    @Override
    public GalleryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        VideoGalleryItemView itemView = new VideoGalleryItemView(parent.getContext());
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
        itemView.mCheckBox.setClickable(false);

        GalleryViewHolder holder = new GalleryViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(GalleryViewHolder holder, int position) {
        VideoBean video = mVideos.get(position);

        VideoGalleryItemView itemView = (VideoGalleryItemView) holder.itemView;
        itemView.mImageView.setImageDrawable(getDefaultDrawable());
        itemView.setTag(video);



        updateCheckBoxVisilble(itemView, video);
        VideoPreviewLoader.display(video.getEncryptPath(), itemView.mImageView);
    }

    private Drawable getDefaultDrawable() {
        if (mDefault == null) {
            mDefault = new ColorDrawable(0xffb4b4b4);
        }
        return mDefault;
    }

    private void updateCheckBoxVisilble(VideoGalleryItemView itemView, VideoBean video) {
        itemView.mCheckBox.setChecked(mVideoSelected.contains(video));
        itemView.mCheckBox.setVisibility(mSelectMode ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        return mVideos.size();
    }

    @Override
    public void onClick(View v) {
        VideoGalleryItemView itemView = (VideoGalleryItemView) v;
        VideoBean video = (VideoBean) itemView.getTag();
        if (mSelectMode) {
            if (mVideoSelected.contains(video)) {
                mVideoSelected.remove(video);
            } else {
                mVideoSelected.add(video);
            }
            updateCheckBoxVisilble(itemView, video);
            return;
        }
        VideoPlayActivity.tryDisplay(video);
    }

    @Override
    public boolean onLongClick(View view) {
        mSelectMode = !mSelectMode;
        if (mGalleryView != null) {
            PGW.log("update items checkable states in recycler view ");
            final int childCount = mGalleryView.getChildCount();
            for (int i = 0; i < childCount; i++) {
                VideoGalleryItemView childAt = (VideoGalleryItemView) mGalleryView.getChildAt(i);
                updateCheckBoxVisilble(childAt, (VideoBean) childAt.getTag());
            }
        }
        EventBus.getDefault().post(new FrameEvent.EventSetMenu(R.menu.video_gallery_decrypt));
        return true;
    }

    public static class GalleryViewHolder extends RecyclerView.ViewHolder {
        public GalleryViewHolder(View itemView) {
            super(itemView);
        }
    }
}
