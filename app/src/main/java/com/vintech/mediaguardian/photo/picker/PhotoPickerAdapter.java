package com.vintech.mediaguardian.photo.picker;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.vintech.mediaguardian.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vincent on 2016/5/26.
 */
public class PhotoPickerAdapter extends RecyclerView.Adapter<PhotoPickerAdapter.PickViewHolder> implements View.OnClickListener {
    private ImageSize mImageSize;
    private Drawable mDefault;
    private List<String> mPhotos = new ArrayList<>();
    private List<String> mPhotoSelected = new ArrayList<>();

    public PhotoPickerAdapter(List<String> list) {
        if (list != null) {
            mPhotos.addAll(list);
        }
    }

    @Override
    public PickViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        PickViewHolder holder = new PickViewHolder(new PhotoPickItemView(parent.getContext()));
        holder.itemView.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(PickViewHolder holder, int position) {
        String path = mPhotos.get(position);

        PhotoPickItemView itemView = (PhotoPickItemView) holder.itemView;
        itemView.mImageView.setImageDrawable(getDefaultDrawable());
        itemView.mCheckBox.setChecked(mPhotoSelected.contains(path));
        itemView.setTag(path);
        ImageLoader.getInstance().displayImage("file://" + path, itemView.mImageView, getImageSize(itemView.getContext()));
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
        PhotoPickItemView itemView = (PhotoPickItemView) v;
        String path = (String) itemView.getTag();
        if (mPhotoSelected.contains(path)) {
            mPhotoSelected.remove(path);
            itemView.mCheckBox.setChecked(false);
        } else {
            mPhotoSelected.add(path);
            itemView.mCheckBox.setChecked(true);
        }
    }

    public ArrayList<String> getSelectedList() {
        return new ArrayList<>(mPhotoSelected);
    }

    public void removeItem(String path) {
        mPhotos.remove(path);
    }

    public static class PickViewHolder extends RecyclerView.ViewHolder {
        public PickViewHolder(View itemView) {
            super(itemView);
        }
    }
}
