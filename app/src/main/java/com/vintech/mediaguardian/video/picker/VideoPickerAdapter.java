package com.vintech.mediaguardian.video.picker;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.vintech.mediaguardian.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vincent on 2016/5/26.
 */
public class VideoPickerAdapter extends RecyclerView.Adapter<VideoPickerAdapter.PickerViewHolder> implements View.OnClickListener {
    private ImageSize mImageSize;
    private Drawable mDefault;
    private List<String> mVideos = new ArrayList<>();
    private List<String> mPhotoSelected = new ArrayList<>();

    public VideoPickerAdapter(List<String> list) {
        if (list != null) {
            mVideos.addAll(list);
        }
    }

    @Override
    public PickerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        PickerViewHolder holder = new PickerViewHolder(new VideoPickItemView(parent.getContext()));
        holder.itemView.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(PickerViewHolder holder, int position) {
        String path = mVideos.get(position);

        VideoPickItemView itemView = (VideoPickItemView) holder.itemView;
        itemView.mImageView.setImageDrawable(getDefaultDrawable());
        itemView.mCheckBox.setChecked(mPhotoSelected.contains(path));
        itemView.setTag(path);
        VideoPreviewLoader.display(path, itemView.mImageView);
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
        return mVideos.size();
    }

    @Override
    public void onClick(View v) {
        VideoPickItemView itemView = (VideoPickItemView) v;
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
        mVideos.remove(path);
    }

    public static class PickerViewHolder extends RecyclerView.ViewHolder {
        public PickerViewHolder(View itemView) {
            super(itemView);
        }
    }
}
