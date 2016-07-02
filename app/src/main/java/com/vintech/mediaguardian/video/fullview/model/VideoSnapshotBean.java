package com.vintech.mediaguardian.video.fullview.model;

import android.graphics.Bitmap;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Vincent on 2016/3/4.
 */
@DatabaseTable(tableName = "snapshot")
public class VideoSnapshotBean {

    @DatabaseField(columnName = "position")
    private long position;

    @DatabaseField(columnName = "bitmap")
    private String bitmap;

    @DatabaseField(columnName = "video")
    private long video;

    // memory data, will not save to db
    public Bitmap snapshot;

    public Bitmap getSnapshot() {
        return snapshot;
    }

    public void setSnapshot(Bitmap snapshot) {
        this.snapshot = snapshot;
    }

    public long getPosition() {
        return position;
    }

    public void setPosition(long position) {
        this.position = position;
    }

    public String getBitmap() {
        return bitmap;
    }

    public void setBitmap(String bitmap) {
        this.bitmap = bitmap;
    }

    public long getVideo() {
        return video;
    }

    public void setVideo(long video) {
        this.video = video;
    }
}

