package com.vintech.mediaguardian.video.fullview;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.vintech.mediaguardian.MainApplication;
import com.vintech.mediaguardian.R;
import com.vintech.mediaguardian.framework.IntentConst;
import com.vintech.mediaguardian.video.fullview.view.VideoControlView;
import com.vintech.mediaguardian.video.fullview.view.VideoPlayerView;
import com.vintech.mediaguardian.video.gallery.model.VideoBean;
import com.vintech.util.tool.AppUtil;

public class VideoPlayActivity extends Activity {

    //    PowerManager mPowerManager = null;
//    PowerManager.WakeLock mWakeLock = null;
//    private FrameLayout mRootView;
    private VideoPlayerView mPlayerView;
    private VideoControlView mControlView;

    private VideoBean mVideoBean;

    public static boolean displayableFile(String path) {
        return true;
    }

    public static boolean tryDisplay(VideoBean bean) {
        if (bean == null || !displayableFile(bean.getEncryptPath())) {
            return false;
        }
        Intent intent = new Intent(MainApplication.getContext(), VideoPlayActivity.class);
        intent.putExtra(IntentConst.Display.DISPLAY_PATH, bean.getId());
        return AppUtil.startActivity(MainApplication.getContext(), intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_player_activity);
        mPlayerView = (VideoPlayerView) findViewById(R.id.player);
        mControlView = (VideoControlView) findViewById(R.id.controller);

        mControlView.setControlListener(mPlayerView);
        display(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        display(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPlayerView.destroy();
    }

    private void display(Intent intent) {
        long id = intent.getLongExtra(IntentConst.Display.DISPLAY_PATH, -1);
        if (id >= 0) {
            VideoBean videoById = MainApplication.getDBManager().getVideoModel().findVideoById(id);
            mPlayerView.display(videoById);
        }
    }
}
