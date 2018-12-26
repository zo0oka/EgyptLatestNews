package com.zookanews.egyptlatestnews.RoomDB.DAO;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.zookanews.egyptlatestnews.RoomDB.Entities.Category;

@Dao
public interface CategoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCategory(Category category);

    @Query("SELECT * FROM categories_table WHERE name = :categoryName")
    Category getCategoryByName(String categoryName);

}
