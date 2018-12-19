package com.zookanews.egyptlatestnews.RoomDB.DAO;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;

import com.zookanews.egyptlatestnews.RoomDB.Entities.Website;

@Dao
public interface WebsiteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertWebsite(Website website);
}
