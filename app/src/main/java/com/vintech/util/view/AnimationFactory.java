package com.vintech.util.view;

import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

/**
 * Created by vincent on 2015/12/7.
 */
public class AnimationFactory {
    public static final float CENTER = 0.5f;
    public static final int SELF = Animation.RELATIVE_TO_SELF;
    public static final Interpolator ACCELERATE = new AccelerateInterpolator();
    public static final Interpolator DECELERATE = new DecelerateInterpolator();
    public static final Interpolator DECELERATE_2 = new DecelerateInterpolator(2);

    public static AnimationSet buildScaleAlphaAnimation(boolean enter, int duration) {
        return buildScaleAlphaAnimation(enter, 0, duration);
    }

    public static AnimationSet buildScaleAlphaAnimation(boolean enter, float scaleMin, int duration) {
        float alphaFrom = enter ? 0 : 1;
        float alphaTo = enter ? 1 : 0;
        float scaleFrom = enter ? scaleMin : 1;
        float scaleTo = enter ? 1 : scaleMin;
        AnimationSet set = new AnimationSet(false);
        ScaleAnimation scale = buildScaleAnimation(scaleFrom, scaleTo, duration);
        AlphaAnimation alpha = buildAlphaAnimation(alphaFrom, alphaTo, duration);

        set.addAnimation(scale);
        set.addAnimation(alpha);
        return set;
    }

    public static ScaleAnimation buildScaleAnimation(float from, float to, int duration) {
        ScaleAnimation scale = new ScaleAnimation(from, to, from, to, SELF, CENTER, SELF, CENTER);
        scale.setDuration(duration);
        return scale;
    }

    public static AlphaAnimation buildAlphaAnimation(float from, float to, int duration) {
        AlphaAnimation alpha = new AlphaAnimation(from, to);
        alpha.setDuration(duration);
        return alpha;
    }

    public static AnimationSet buildTranslateAlphaAnimation(boolean enter, int deltaX, int deltaY, int duration) {
        float alphaFrom = enter ? 0 : 1;
        float alphaTo = enter ? 1 : 0;

        int xFrom = enter ? deltaX : 0;
        int xTo = enter ? 0 : deltaX;
        int yFrom = enter ? deltaY : 0;
        int yTo = enter ? 0 : deltaY;

        TranslateAnimation tsl = buildTranslateAnimation(xFrom, xTo, yFrom, yTo, duration);
        AlphaAnimation alpha = buildAlphaAnimation(alphaFrom, alphaTo, duration);
        AnimationSet set = new AnimationSet(false);
        set.addAnimation(alpha);
        set.addAnimation(tsl);

        return set;
    }

    public static AnimationSet buildTranslateAlphaAnimation(boolean enter, float deltaX, float deltaY, int duration) {
        float alphaFrom = enter ? 0 : 1;
        float alphaTo = enter ? 1 : 0;

        float xFrom = enter ? deltaX : 0;
        float xTo = enter ? 0 : deltaX;
        float yFrom = enter ? deltaY : 0;
        float yTo = enter ? 0 : deltaY;

        int alphaDuration = enter ? duration / 2 : duration;
        int tslDuration = duration;
        TranslateAnimation tsl = buildTranslateAnimation(xFrom, xTo, yFrom, yTo, tslDuration);
        AlphaAnimation alpha = buildAlphaAnimation(alphaFrom, alphaTo, alphaDuration);
        AnimationSet set = new AnimationSet(false);
        set.addAnimation(alpha);
        set.addAnimation(tsl);
        if (enter) {
            tsl.setInterpolator(DECELERATE);
        }
        return set;
    }

    public static TranslateAnimation buildTranslateAnimationY(int fromY, int toY, int duration) {
        return buildTranslateAnimation(0, 0, fromY, toY, duration);
    }

    public static TranslateAnimation buildTranslateAnimationX(int fromX, int toX, int duration) {
        return buildTranslateAnimation(fromX, toX, 0, 0, duration);
    }

    public static TranslateAnimation buildTranslateAnimation(float fromX, float toX, float fromY, float toY, int duration) {
        TranslateAnimation tsl = new TranslateAnimation(Animation.RELATIVE_TO_SELF, fromX, Animation.RELATIVE_TO_SELF, toX, Animation.RELATIVE_TO_SELF, fromY, Animation.RELATIVE_TO_SELF, toY);
        tsl.setDuration(duration);
        return tsl;
    }

    public static TranslateAnimation buildTranslateAnimation(int fromX, int toX, int fromY, int toY, int duration) {
        TranslateAnimation tsl = new TranslateAnimation(fromX, toX, fromY, toY);
        tsl.setDuration(duration);
        return tsl;
    }

    public static TranslateAnimation buildTranslateAnimationY(float fromY, float toY, int duration) {
        TranslateAnimation tsl = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, fromY, Animation.RELATIVE_TO_SELF, toY);
        tsl.setDuration(duration);
        return tsl;
    }

    public static TranslateAnimation buildTranslateAnimationX(boolean enter, float deltaX, int duration) {
        if (enter) {
            return buildTranslateAnimationX(deltaX, 0, duration);
        } else {
            return buildTranslateAnimationX(0, deltaX, duration);
        }
    }

    public static TranslateAnimation buildTranslateAnimationY(boolean enter, float deltaY, int duration) {
        if (enter) {
            return buildTranslateAnimationY(deltaY, 0, duration);
        } else {
            return buildTranslateAnimationY(0, deltaY, duration);
        }
    }

    public static TranslateAnimation buildTranslateAnimationX(float fromX, float toX, int duration) {
        TranslateAnimation tsl = new TranslateAnimation(Animation.RELATIVE_TO_SELF, fromX, Animation.RELATIVE_TO_SELF, toX, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);
        tsl.setDuration(duration);
        return tsl;
    }
}

