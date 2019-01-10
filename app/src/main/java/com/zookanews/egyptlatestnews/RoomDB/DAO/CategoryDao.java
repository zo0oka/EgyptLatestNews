package com.zookanews.egyptlatestnews.RoomDB.DAO;

import com.zookanews.egyptlatestnews.RoomDB.Entities.Category;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface CategoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCategory(Category category);

    @Query("SELECT * FROM categories_table WHERE name = :categoryName")
    Category getCategoryByName(String categoryName);

}
