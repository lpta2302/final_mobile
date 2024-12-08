package com.dev.mail.lpta2302.final_mobile.logic.cache;

import android.content.Context;

import androidx.room.Room;

/**
 * Lớp dùng để kết nối ứng dụng với Room database, Room sẽ tìm thấy nơi lưu database qua Context.
 */
public class CacheDatabaseClient {
    private static final String databaseName = "mobile";
    private static CacheDatabase appDatabase;

    public static CacheDatabase getInstance(Context context) {
        if (appDatabase == null) {
            appDatabase = Room.databaseBuilder(context, CacheDatabase.class, databaseName)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return appDatabase;
    }
}
