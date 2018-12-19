package com.zookanews.egyptlatestnews.RoomDB.DAO;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.zookanews.egyptlatestnews.RoomDB.Entities.Feed;

import java.util.List;

@Dao
public interface FeedDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertFeed(Feed feed);

    @Query("SELECT * FROM feeds_table ORDER BY ID DESC")
    List<Feed> getAllFeeds();
}
