package com.nexus.app.data.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.nexus.app.data.local.model.UserEntity;

@Dao
public interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void upsert(UserEntity user);

    @Query("SELECT * FROM users WHERE uid = :uid LIMIT 1")
    UserEntity getById(String uid);
}
