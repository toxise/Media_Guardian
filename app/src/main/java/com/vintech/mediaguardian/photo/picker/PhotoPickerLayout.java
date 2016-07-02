package com.vintech.mediaguardian.photo.picker;

import android.Manifest;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.vintech.mediaguardian.R;
import com.vintech.mediaguardian.encrypt.EncryptTaskManager;
import com.vintech.mediaguardian.encrypt.PhotoEncryptTask;
import com.vintech.mediaguardian.framework.FrameEvent;
import com.vintech.mediaguardian.framework.IWorkspaceFrame;
import com.vintech.util.tool.PermissionTool;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vincent on 2016/5/26.
 */
public class PhotoPickerLayout extends FrameLayout implements View.OnClickListener, IWorkspaceFrame {
    private RecyclerView mPhotos;
    private FloatingActionButton mOk;

    public PhotoPickerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mPhotos = (RecyclerView) findViewById(R.id.photo);
        mOk = (FloatingActionButton) findViewById(R.id.ok);

        GridLayoutManager manager = new GridLayoutManager(getContext(), 3);
        mPhotos.setLayoutManager(manager);
        mPhotos.addItemDecoration(new PhotoPickerItemDecoration());
        mOk.setOnClickListener(this);
    }

    @Override
    public void setVisibility(int visibility) {
        if (visibility == getVisibility() || visibility != VISIBLE) {
            super.setVisibility(visibility);
            return;
        } else {
            super.setVisibility(visibility);
        }
        checkPermission();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventPermissionResult(FrameEvent.EventPermissionResult event) {
        if (getVisibility() != VISIBLE) {
            return;
        }
        if (Manifest.permission.READ_EXTERNAL_STORAGE.equals(event.mPermission) || Manifest.permission.WRITE_EXTERNAL_STORAGE.equals(event.mPermission)) {
            checkPermission();
        }
    }

    private void checkPermission() {
        if (!PermissionTool.hasPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
            EventBus.getDefault().post(new FrameEvent.EventRequestPermission(Manifest.permission.READ_EXTERNAL_STORAGE));
            return;
        } else if (!PermissionTool.hasPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            EventBus.getDefault().post(new FrameEvent.EventRequestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE));
            return;
        }
        EventBus.getDefault().post(this);
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEventStartLoader(PhotoPickerLayout event) {
        Cursor cursor = null;
        try {
            final List<String> list = new ArrayList<String>();
            cursor = getContext().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{MediaStore.Images.Media.DATA}, null, null, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    String string = cursor.getString(0);
                    list.add(string);
                }
            }
            post(new Runnable() {
                @Override
                public void run() {
                    refreshPhotos(list);
                }
            });
        } catch (Exception e) {
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private void refreshPhotos(List<String> list) {
        mPhotos.setAdapter(new PhotoPickerAdapter(list));
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.ok) {
            PhotoPickerAdapter adapter = (PhotoPickerAdapter) mPhotos.getAdapter();
            ArrayList<String> selectedList = adapter.getSelectedList();
            for (String str : selectedList) {
                adapter.removeItem(str);
                EncryptTaskManager.addTask(new PhotoEncryptTask(str));
            }
            adapter.notifyDataSetChanged();
            EventBus.getDefault().post(new FrameEvent.EventSetLayout(R.id.photo_gallery_layout));
        }
    }

    @Override
    public boolean handleBackKey() {
//        EventBus.getDefault().post(new FrameEvent.EventSetLayout(R.id.photo_gallery_layout));
        return false;
    }
}
