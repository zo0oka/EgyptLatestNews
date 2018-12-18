package com.zookanews.egyptlatestnews.RoomDB.Entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "categories_table")
public class Category {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "ID")
    private int categoryId;

    @NonNull
    @ColumnInfo(name = "name")
    private String categoryName;

    @ColumnInfo(name = "title")
    private String categoryTitle;

    public Category(@NonNull int categoryId, @NonNull String categoryName, String categoryTitle) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.categoryTitle = categoryTitle;
    }

    @Ignore
    public Category(@NonNull String categoryName, String categoryTitle) {
        this.categoryName = categoryName;
        this.categoryTitle = categoryTitle;
    }

    @NonNull
    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(@NonNull int categoryId) {
        this.categoryId = categoryId;
    }

    @NonNull
    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(@NonNull String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryTitle() {
        return categoryTitle;
    }

    public void setCategoryTitle(String categoryTitle) {
        this.categoryTitle = categoryTitle;
    }
}
