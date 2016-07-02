package com.vintech.mediaguardian.video.fullview.view;

import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.vintech.mediaguardian.MainApplication;
import com.vintech.mediaguardian.R;
import com.vintech.mediaguardian.video.fullview.model.VideoSnapshotBean;
import com.vintech.util.tool.ConvertUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vincent on 2016/2/29.
 */
public class ViewSnapshotAdapter extends BaseAdapter {

    private List<VideoSnapshotBean> mInfos = new ArrayList<VideoSnapshotBean>();

    public void addSnapshot(List<VideoSnapshotBean> beans) {
        mInfos.addAll(beans);
        notifyDataSetChanged();
    }

    public void addSnapshot(VideoSnapshotBean bean) {
        mInfos.add(0, bean);
        notifyDataSetChanged();
    }

    public void deleteSnapshot(VideoSnapshotBean bean) {
        mInfos.remove(bean);
        notifyDataSetChanged();
    }

    public void setSnapshot(List<VideoSnapshotBean> beans) {
        mInfos.clear();
        addSnapshot(beans);
    }

    @Override
    public int getCount() {
        return mInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return mInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView snapshot = null;
        if (convertView == null) {
            snapshot = new ImageView(MainApplication.getContext());
            snapshot.setScaleType(ImageView.ScaleType.CENTER_CROP);
            snapshot.setLayoutParams(new AbsListView.LayoutParams(ConvertUtil.getPixel(R.dimen.snapshot_item_view_width), ConvertUtil.getPixel(R.dimen.snapshot_item_view_height)));
        } else {
            snapshot = (ImageView) convertView;
        }

        VideoSnapshotBean info = mInfos.get(position);
        if (info.snapshot == null) {
            info.snapshot = BitmapFactory.decodeFile(info.getBitmap());
        }
        snapshot.setImageBitmap(info.snapshot);
        return snapshot;
    }
}
