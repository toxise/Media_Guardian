package com.vintech.mediaguardian.framework;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.vintech.mediaguardian.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by Vincent on 2016/5/26.
 */
public class WorkspaceLayout extends FrameLayout {
    private int mCurrentLayout = R.id.photo_gallery_layout;

    public WorkspaceLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        findViewById(mCurrentLayout).setVisibility(VISIBLE);
        EventBus.getDefault().register(this);
    }

    public boolean handleBackKey() {
        IWorkspaceFrame frame = (IWorkspaceFrame) findViewById(mCurrentLayout);
        return frame.handleBackKey();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventSetLayout(FrameEvent.EventSetLayout event) {
        if (event.mLayoutId == mCurrentLayout) {
            return;
        }

        View current = findViewById(mCurrentLayout);
        View next = findViewById(event.mLayoutId);

        current.setVisibility(GONE);
        next.setVisibility(VISIBLE);

        mCurrentLayout = event.mLayoutId;
    }
}
