package com.zookanews.egyptlatestnews.RoomDB.DAO;

import com.zookanews.egyptlatestnews.RoomDB.Entities.Feed;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface FeedDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertFeed(Feed feed);

    @Query("SELECT * FROM feeds_table ORDER BY ID DESC")
    List<Feed> getAllFeeds();
}
