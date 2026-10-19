package com.nexus.app.data.local.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.nexus.app.data.local.dao.UserDao;
import com.nexus.app.data.local.model.UserEntity;

@Database(entities = {UserEntity.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    private static final String DB_NAME = "nexus_local.db";
    private static volatile AppDatabase instance;

    public abstract UserDao userDao();

    public static synchronized AppDatabase getInstance(Context ctx) {
        if (instance == null) {
            instance = Room.databaseBuilder(ctx.getApplicationContext(), AppDatabase.class, DB_NAME)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
