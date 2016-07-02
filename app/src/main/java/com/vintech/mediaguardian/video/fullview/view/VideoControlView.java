package com.vintech.mediaguardian.video.fullview.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.vintech.mediaguardian.MainApplication;
import com.vintech.mediaguardian.R;
import com.vintech.mediaguardian.video.VideoEvents;
import com.vintech.mediaguardian.video.fullview.OnVideoControlListener;
import com.vintech.mediaguardian.video.fullview.VideoControlGesture;
import com.vintech.mediaguardian.video.fullview.VideoSnapshotController;
import com.vintech.mediaguardian.video.fullview.model.VideoSnapshotBean;
import com.vintech.mediaguardian.video.gallery.model.VideoBean;
import com.vintech.util.tool.AlarmController;
import com.vintech.util.tool.ConvertUtil;
import com.vintech.util.view.AnimationFactory;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * Created by Vincent on 2016/2/27.
 */
public class VideoControlView extends FrameLayout implements SeekBar.OnSeekBarChangeListener, Runnable, View.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private final int CONTROL_PANEL_ANIM_DURATION = 500;

    private SeekBar mProgressBar;
    private ImageView mPlayView;
    private TextView mTimeView;
    private ListView mSnapshotListView;
    private View mSnapView;

    private VideoBean mVideoBean;

    private OnVideoControlListener mControlListener;
    private VideoControlGesture mControlGesture = new VideoControlGesture();
    private VideoSnapshotController mSnapshotController = new VideoSnapshotController();
    private Runnable mHideControlPanelRunnable = new Runnable() {
        @Override
        public void run() {
            setPanelVisible(false);
        }
    };

    public VideoControlView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setControlListener(OnVideoControlListener listener) {
        mControlListener = listener;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mProgressBar = (SeekBar) findViewById(R.id.seek);
        mPlayView = (ImageView) findViewById(R.id.play);
        mTimeView = (TextView) findViewById(R.id.time);
        mSnapshotListView = (ListView) findViewById(R.id.list);
        mSnapView = findViewById(R.id.mark);

        mProgressBar.setOnSeekBarChangeListener(this);
        mPlayView.setOnClickListener(this);
        mSnapView.setOnClickListener(this);
        mSnapshotListView.setOnItemClickListener(this);
        mSnapshotListView.setOnItemLongClickListener(this);
        setOnClickListener(this);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        AlarmController.addTimeTask(1, -1, 50, this);
        refreshHideControlPanelDelayed();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        AlarmController.removeTask(this);
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventVideoPlayingChanged(VideoEvents.EventVideoPlayingChanged event) {
        mVideoBean = event.bean;
        MainApplication.getDBManager().getVideoSnapshotDbModel().getSnapshotByVideo(mVideoBean);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventSnapshotLoaded(VideoEvents.EventSnapshotLoaded event) {
        if (event.mVideo == mVideoBean) {
            getAdapter().setSnapshot(event.mSnapshot);
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (mControlListener != null && fromUser) {
            VideoPlayerView player = mControlListener.getMediaPlayerView();
            long duration = player.getDuration();
            int toDuration = (int) (duration * progress / seekBar.getMax());
            float p = (float) progress / seekBar.getMax();
            toDuration = (int) (duration * p);
            player.seekTo(toDuration);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void run() {
        if (mControlListener == null || mProgressBar == null) {
            return;
        }
        VideoPlayerView player = mControlListener.getMediaPlayerView();
        float progress = 0;
        int min = 0;
        int sec = 0;
        if (player.isPlaying()) {
            long currentPos = player.getCurrentPosition();
            long duration = player.getDuration();
            progress = duration == 0 ? 0 : (float) currentPos / duration;
            min = (int) (currentPos / 1000 / 60);
            sec = (int) (currentPos / 1000 % 60);
        }

        String time = min < 10 ? "0" : "";
        time = time + min + " : ";
        if (sec < 10) {
            time = time + "0";
        }
        time = time + sec;
        mProgressBar.setProgress((int) (progress * mProgressBar.getMax()));
        mTimeView.setText(time);
    }

    @Override
    public void onClick(View v) {
        if (mControlListener == null) {
            return;
        }
        VideoPlayerView mediaPlayer = mControlListener.getMediaPlayerView();
        if (v == mPlayView) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
            } else {
                mediaPlayer.start();
            }

            mPlayView.setImageResource(mediaPlayer.isPlaying() ? R.drawable.video_pause : R.drawable.video_play);
        } else if (v == mSnapView) {
            mSnapshotController.snapshot(mVideoBean, mediaPlayer.getCurrentPosition());
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                removeCallbacks(mHideControlPanelRunnable);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                refreshHideControlPanelDelayed();
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int gesture = mControlGesture.onTouchEvent(event);
        switch (gesture) {
            case VideoControlGesture.GESTURE_CLICK:
                setPanelVisible(!isPanelVisible());
                refreshHideControlPanelDelayed();
                break;
            case VideoControlGesture.GESTURE_SLIDE:
                int time = mControlGesture.optInt();
                if (mControlListener != null) {
                    VideoPlayerView player = mControlListener.getMediaPlayerView();
                    player.seekTo((int) (Math.max(0, player.getCurrentPosition() + time)));
                }
                break;
        }
        return true;
    }

    private boolean isPanelVisible() {
        View view = findViewById(R.id.process_panel);
        return view != null && view.getVisibility() == VISIBLE;
    }

    private void setPanelVisible(boolean visible) {
        setPanelVisible(R.id.process_panel, visible, Gravity.BOTTOM);
        setPanelVisible(R.id.snap_panel, visible, Gravity.RIGHT);
    }

    private void setPanelVisible(int viewId, boolean visible, int direction) {
        View view = findViewById(viewId);

        if (visible != view.isShown()) {
            view.setVisibility(visible ? VISIBLE : INVISIBLE);
            TranslateAnimation anim = null;
            switch (direction) {
                case Gravity.TOP:
                    anim = AnimationFactory.buildTranslateAnimationY(visible, -1f, CONTROL_PANEL_ANIM_DURATION);
                    break;
                case Gravity.BOTTOM:
                    anim = AnimationFactory.buildTranslateAnimationY(visible, 1f, CONTROL_PANEL_ANIM_DURATION);
                    break;
                case Gravity.RIGHT:
                    anim = AnimationFactory.buildTranslateAnimationX(visible, 1f, CONTROL_PANEL_ANIM_DURATION);
                    break;
                case Gravity.LEFT:
                    anim = AnimationFactory.buildTranslateAnimationX(visible, -1f, CONTROL_PANEL_ANIM_DURATION);
                    break;
            }
            view.startAnimation(anim);
        }
    }

    private void refreshHideControlPanelDelayed() {
        removeCallbacks(mHideControlPanelRunnable);
        postDelayed(mHideControlPanelRunnable, 8 * ConvertUtil.SECOND);
    }

    public void onSnapshotLoaded(List<VideoSnapshotBean> bean) {
        getAdapter().addSnapshot(bean);
    }

    public void onSnapshotFinished(VideoSnapshotBean bean) {
        getAdapter().addSnapshot(bean);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        VideoSnapshotBean bean = (VideoSnapshotBean) parent.getItemAtPosition(position);
        if (mControlListener != null) {
            mControlListener.getMediaPlayerView().seekTo((int) bean.getPosition());
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        VideoSnapshotBean bean = (VideoSnapshotBean) parent.getItemAtPosition(position);
        getAdapter().deleteSnapshot(bean);
        mSnapshotController.deleteSnapshot(bean);
        return true;
    }

    public ViewSnapshotAdapter getAdapter() {
        ListAdapter adapter = mSnapshotListView.getAdapter();
        if (adapter == null) {
            adapter = new ViewSnapshotAdapter();
            mSnapshotListView.setAdapter(adapter);
        }
        return (ViewSnapshotAdapter) adapter;
    }

}
