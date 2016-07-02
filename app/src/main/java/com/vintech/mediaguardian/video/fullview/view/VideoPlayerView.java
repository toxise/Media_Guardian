package com.vintech.mediaguardian.video.fullview.view;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.vintech.mediaguardian.video.VideoEvents;
import com.vintech.mediaguardian.video.fullview.OnVideoControlListener;
import com.vintech.mediaguardian.video.gallery.model.VideoBean;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

/**
 * Created by Vincent on 2016/2/27.
 */
public class VideoPlayerView extends SurfaceView implements SurfaceHolder.Callback, MediaPlayer.OnBufferingUpdateListener, OnVideoControlListener {
    private MediaPlayer mPlayer;
    private VideoBean mVideoBean;

    public VideoPlayerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPlayer = new MediaPlayer();
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mPlayer.setOnBufferingUpdateListener(this);
        getHolder().setKeepScreenOn(true);
        getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        getHolder().addCallback(this);
    }

    public void destroy() {
        mPlayer.reset();
        mPlayer.release();
    }

    public void display(VideoBean bean) {
        try {
            mPlayer.reset();
            mVideoBean = bean;
            mPlayer.setDataSource(bean.getEncryptPath());
            mPlayer.prepareAsync();
            mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mPlayer.start();
                    EventBus.getDefault().post(new VideoEvents.EventVideoPlayingChanged(mVideoBean, mPlayer.getDuration()));
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mPlayer.setDisplay(getHolder());
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
    }

    @Override
    public VideoPlayerView getMediaPlayerView() {
        return this;
    }

    public long getDuration() {
        return mPlayer.getDuration();
    }

    public void seekTo(int toDuration) {
        mPlayer.seekTo(toDuration);
    }

    public boolean isPlaying() {
        return mPlayer.isPlaying();
    }

    public long getCurrentPosition() {
        return mPlayer.getCurrentPosition();
    }

    public void start() {
        mPlayer.start();
    }

    public void pause() {
        mPlayer.pause();
    }
}
