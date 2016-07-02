package com.vintech.mediaguardian.photo.fullview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.vintech.mediaguardian.MainApplication;
import com.vintech.mediaguardian.R;
import com.vintech.mediaguardian.photo.gallery.model.PhotoBean;

import java.util.List;

/**
 * Created by Vincent on 2016/5/31.
 */
public class FullPhotoActivity extends AppCompatActivity {
    public static final String BUNDLE_POS_PHOTO = "bundle_pos_photo";
    private ViewPager mGallery;
    private FullPhotoAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        int index = getIntent().getIntExtra(BUNDLE_POS_PHOTO, 0);
        setContentView(R.layout.photo_full_gallery_activity);

        mGallery = (ViewPager) findViewById(R.id.gallery);

        mAdapter = new FullPhotoAdapter();
        List<PhotoBean> photos = MainApplication.getDBManager().getPhotoModel().getPhotos();
        mAdapter.addPhotoBean(photos);
        mGallery.setAdapter(mAdapter);
        mGallery.setCurrentItem(index);
    }
}
