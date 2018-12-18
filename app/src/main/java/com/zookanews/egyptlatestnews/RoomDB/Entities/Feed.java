package com.zookanews.egyptlatestnews.RoomDB.Entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "feeds_table", indices = {@Index(value = {"rss_link"}, unique = true)})
public class Feed {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "ID")
    private int feedId;

    @ColumnInfo(name = "title")
    private String feedTitle;

    @NonNull
    @ColumnInfo(name = "rss_link")
    private String feedRssLink;

    @ColumnInfo(name = "category_name")
    private String categoryName;

    @ColumnInfo(name = "website_name")
    private String websiteName;

    public Feed(@NonNull int feedId, String feedTitle, @NonNull String feedRssLink, String categoryName, String websiteName) {
        this.feedId = feedId;
        this.feedTitle = feedTitle;
        this.feedRssLink = feedRssLink;
        this.categoryName = categoryName;
        this.websiteName = websiteName;
    }

    @Ignore
    public Feed(String feedTitle, @NonNull String feedRssLink, String categoryName, String websiteName) {
        this.feedTitle = feedTitle;
        this.feedRssLink = feedRssLink;
        this.categoryName = categoryName;
        this.websiteName = websiteName;
    }

    @NonNull
    public int getFeedId() {
        return feedId;
    }

    public void setFeedId(@NonNull int feedId) {
        this.feedId = feedId;
    }

    public String getFeedTitle() {
        return feedTitle;
    }

    public void setFeedTitle(String feedTitle) {
        this.feedTitle = feedTitle;
    }

    @NonNull
    public String getFeedRssLink() {
        return feedRssLink;
    }

    public void setFeedRssLink(@NonNull String feedRssLink) {
        this.feedRssLink = feedRssLink;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getWebsiteName() {
        return websiteName;
    }

    public void setWebsiteName(String websiteName) {
        this.websiteName = websiteName;
    }
}
