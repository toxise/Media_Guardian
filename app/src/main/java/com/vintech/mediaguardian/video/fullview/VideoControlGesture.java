package com.vintech.mediaguardian.video.fullview;

import android.view.MotionEvent;

import com.vintech.mediaguardian.MainApplication;
import com.vintech.util.tool.ConvertUtil;
import com.vintech.util.tool.MathUtil;
import com.vintech.util.view.AnimationFactory;

/**
 * Created by Vincent on 2016/2/29.
 */
public class VideoControlGesture {

    public static final int GESTURE_INVALID = 0;
    public static final int GESTURE_CLICK = 1;
    public static final int GESTURE_SLIDE = 2;

    private float mDownX = -1;
    private float mDownY = -1;
    private float mTouchSlop = 18;
    private int mGestureMaked = GESTURE_INVALID;
    private Object mGestureData = null;

    public VideoControlGesture() {
        mTouchSlop = ConvertUtil.dip2Px(6);
        mTouchSlop *= mTouchSlop;
    }

    public int onTouchEvent(MotionEvent e) {
        float x = e.getX();
        float y = e.getY();
        switch (e.getAction()) {
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                float distance = MathUtil.getDistancePow(x, y, mDownX, mDownY);
                if (mTouchSlop > distance) {
                    mGestureMaked = GESTURE_CLICK;
                } else {
                    mGestureMaked = GESTURE_SLIDE;
                    int slideTime = computeGestureSlideTime((float) Math.sqrt(distance));
                    mGestureData = x > mDownX ? -slideTime : slideTime;
                }
                break;
            case MotionEvent.ACTION_DOWN:
                mDownX = x;
                mDownY = y;
                mGestureMaked = GESTURE_INVALID;
                mGestureData = null;
                break;
        }
        return mGestureMaked;
    }


    private int computeGestureSlideTime(float distance) {
        float p = distance / MainApplication.getContext().getResources().getDisplayMetrics().widthPixels;
        p = AnimationFactory.ACCELERATE.getInterpolation(p);
        return (int) (p * 10 * ConvertUtil.MINUTES);
    }

    public int optInt() {
        return (Integer) mGestureData;
    }
}
