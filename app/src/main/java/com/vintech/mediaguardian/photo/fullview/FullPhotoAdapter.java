package com.vintech.mediaguardian.photo.fullview;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.vintech.mediaguardian.photo.gallery.model.PhotoBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vincent on 2016/5/26.
 */
public class FullPhotoAdapter extends PagerAdapter {
    private ImageSize mImageSize;
    private Drawable mDefault;
    private List<PhotoBean> mPhotos = new ArrayList<>();

    public void addPhotoBean(PhotoBean bean) {
        mPhotos.add(bean);
    }

    public void addPhotoBean(List<PhotoBean> bean) {
        mPhotos.addAll(bean);
    }


    public ImageSize getImageSize(Context context) {
        if (mImageSize == null) {
            int size = context.getResources().getDisplayMetrics().widthPixels;
            mImageSize = new ImageSize(size, size);
        }
        return mImageSize;
    }

    @Override
    public int getCount() {
        return mPhotos.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);

    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        FullPhotoItemView itemView = new FullPhotoItemView(container.getContext());
        container.addView(itemView);

        PhotoBean path = mPhotos.get(position);
        ImageLoader.getInstance().displayImage("file://" + path.getEncryptPath(), itemView, getImageSize(itemView.getContext()));
        return itemView;
    }
}
