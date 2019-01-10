package com.zookanews.egyptlatestnews.RoomDB.DAO;

import com.zookanews.egyptlatestnews.RoomDB.Entities.Website;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface WebsiteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertWebsite(Website website);

    @Query("SELECT * FROM websites_table WHERE name = :websiteName")
    Website getWebsiteByName(String websiteName);
}
