package com.zookanews.egyptlatestnews.RoomDB.DAO;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.zookanews.egyptlatestnews.RoomDB.Entities.Website;

@Dao
public interface WebsiteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertWebsite(Website website);

    @Query("SELECT * FROM websites_table WHERE name = :websiteName")
    Website getWebsiteByName(String websiteName);
}
