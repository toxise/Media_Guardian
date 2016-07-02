package com.vintech.mediaguardian.database;

import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.support.ConnectionSource;

/**
 * Created by Vincent on 2016/6/18.
 */
public class DatabaseUpgradeHandler {
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connection, int oldVersion, int newVersion) {
        if (oldVersion >= newVersion) {
            return;
        }

        switch (oldVersion) {
            case 1:
                from1(connection);
                break;
        }

        onUpgrade(database, connection, oldVersion + 1, newVersion);
    }

    private void from1(ConnectionSource connection) {
    }
}
