package com.vintech.mediaguardian.video.gallery;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.vintech.mediaguardian.R;
import com.vintech.mediaguardian.video.fullview.VideoPlayActivity;
import com.vintech.mediaguardian.video.gallery.model.VideoBean;
import com.vintech.mediaguardian.video.picker.VideoPreviewLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vincent on 2016/5/26.
 */
public class VideoGalleryAdapter extends RecyclerView.Adapter<VideoGalleryAdapter.GalleryViewHolder> implements View.OnClickListener {
    private ImageSize mImageSize;
    private Drawable mDefault;
    private List<VideoBean> mPhotos = new ArrayList<>();
    private List<VideoBean> mPhotoSelected = new ArrayList<>();

    public void addPhotoBean(VideoBean bean) {
        mPhotos.add(bean);
    }

    @Override
    public GalleryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        GalleryViewHolder holder = new GalleryViewHolder(new VideoGalleryItemView(parent.getContext()));
        holder.itemView.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(GalleryViewHolder holder, int position) {
        VideoBean photo = mPhotos.get(position);

        VideoGalleryItemView itemView = (VideoGalleryItemView) holder.itemView;
        itemView.mImageView.setImageDrawable(getDefaultDrawable());
        itemView.mCheckBox.setChecked(mPhotoSelected.contains(photo));
        itemView.setTag(photo);
        VideoPreviewLoader.display(photo.getEncryptPath(), itemView.mImageView);
    }

    public ImageSize getImageSize(Context context) {
        if (mImageSize == null) {
            int size = context.getResources().getDimensionPixelSize(R.dimen.photo_pick_item_size);
            mImageSize = new ImageSize(size, size);
        }
        return mImageSize;
    }

    private Drawable getDefaultDrawable() {
        if (mDefault == null) {
            mDefault = new ColorDrawable(0xffb4b4b4);
        }
        return mDefault;
    }

    @Override
    public int getItemCount() {
        return mPhotos.size();
    }

    @Override
    public void onClick(View v) {
        VideoGalleryItemView itemView = (VideoGalleryItemView) v;
        VideoBean path = (VideoBean) itemView.getTag();
        VideoPlayActivity.tryDisplay(path);
    }

    public static class GalleryViewHolder extends RecyclerView.ViewHolder {
        public GalleryViewHolder(View itemView) {
            super(itemView);
        }
    }
}
