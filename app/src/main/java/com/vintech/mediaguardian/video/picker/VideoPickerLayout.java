package com.vintech.mediaguardian.video.picker;

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
import com.vintech.mediaguardian.encrypt.VideoEncryptTask;
import com.vintech.mediaguardian.framework.FrameEvent;
import com.vintech.mediaguardian.framework.IWorkspaceFrame;
import com.vintech.mediaguardian.util.PGW;
import com.vintech.util.tool.PermissionTool;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vincent on 2016/5/26.
 */
public class VideoPickerLayout extends FrameLayout implements View.OnClickListener, IWorkspaceFrame {
    private RecyclerView mVideos;
    private FloatingActionButton mOk;

    public VideoPickerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mVideos = (RecyclerView) findViewById(R.id.photo);
        mOk = (FloatingActionButton) findViewById(R.id.ok);

        GridLayoutManager manager = new GridLayoutManager(getContext(), 3);
        mVideos.setLayoutManager(manager);
        mVideos.addItemDecoration(new VideoPickerItemDecoration());
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
    public void onEventStartLoader(VideoPickerLayout event) {
        Cursor cursor = null;
        PGW.log("start video loader");
        try {
            final List<String> list = new ArrayList<>();
            cursor = getContext().getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, new String[]{MediaStore.Video.Media.DATA}, null, null, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    String string = cursor.getString(0);
                    File f = new File(string);
                    if (f.exists()) {
                        list.add(string);
                    }
                }
            }
            post(new Runnable() {
                @Override
                public void run() {
                    refreshVideo(list);
                }
            });
        } catch (Exception e) {
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private void refreshVideo(List<String> list) {
        mVideos.setAdapter(new VideoPickerAdapter(list));
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.ok) {
            VideoPickerAdapter adapter = (VideoPickerAdapter) mVideos.getAdapter();
            ArrayList<String> selectedList = adapter.getSelectedList();
            for (String str : selectedList) {
                adapter.removeItem(str);
                EncryptTaskManager.addTask(new VideoEncryptTask(str));
            }
            adapter.notifyDataSetChanged();
            EventBus.getDefault().post(new FrameEvent.EventSetLayout(R.id.video_gallery_layout));
        }
    }

    @Override
    public boolean handleBackKey() {
//        EventBus.getDefault().post(new FrameEvent.EventSetLayout(R.id.video_gallery_layout));
        return false;
    }
}
