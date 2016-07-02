package com.vintech.mediaguardian.photo.gallery;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.vintech.mediaguardian.R;
import com.vintech.mediaguardian.photo.fullview.FullPhotoActivity;
import com.vintech.mediaguardian.photo.gallery.model.PhotoBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vincent on 2016/5/26.
 */
public class PhotoGalleryAdapter extends RecyclerView.Adapter<PhotoGalleryAdapter.GalleryViewHolder> implements View.OnClickListener {
    private ImageSize mImageSize;
    private Drawable mDefault;
    private List<PhotoBean> mPhotos = new ArrayList<>();
    private List<PhotoBean> mPhotoSelected = new ArrayList<>();

    public void addPhotoBean(PhotoBean bean) {
        mPhotos.add(bean);
    }

    @Override
    public GalleryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        GalleryViewHolder holder = new GalleryViewHolder(new PhotoGalleryItemView(parent.getContext()));
        holder.itemView.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(GalleryViewHolder holder, int position) {
        PhotoBean photo = mPhotos.get(position);

        PhotoGalleryItemView itemView = (PhotoGalleryItemView) holder.itemView;
        itemView.mImageView.setImageDrawable(getDefaultDrawable());
        itemView.mCheckBox.setChecked(mPhotoSelected.contains(photo));
        itemView.setTag(photo);
        ImageLoader.getInstance().displayImage("file://" + photo.getEncryptPath(), itemView.mImageView, getImageSize(itemView.getContext()));
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
        PhotoGalleryItemView itemView = (PhotoGalleryItemView) v;
        PhotoBean path = (PhotoBean) itemView.getTag();

        Intent intent = new Intent(v.getContext(), FullPhotoActivity.class);
        intent.putExtra(FullPhotoActivity.BUNDLE_POS_PHOTO, mPhotos.indexOf(path));
        v.getContext().startActivity(intent);
    }

    public static class GalleryViewHolder extends RecyclerView.ViewHolder {
        public GalleryViewHolder(View itemView) {
            super(itemView);
        }
    }
}
