package com.vintech.mediaguardian.photo.gallery.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Vincent on 2016/5/28.
 */
@DatabaseTable(tableName = "photos")
public class PhotoBean {
    @DatabaseField(columnName = "ori_path")
    private String oriPath;

    @DatabaseField(columnName = "encrypt_path")
    private String encryptPath;

    @DatabaseField(columnName = "__sid", index = true, generatedId = true)
    private long id;

    public String getOriPath() {
        return oriPath;
    }

    public void setOriPath(String oriPath) {
        this.oriPath = oriPath;
    }

    public String getEncryptPath() {
        return encryptPath;
    }

    public void setEncryptPath(String encryptPath) {
        this.encryptPath = encryptPath;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "this= " + this.getClass() + ", ori= " + oriPath + ", encrypt= " + encryptPath;
    }
}
