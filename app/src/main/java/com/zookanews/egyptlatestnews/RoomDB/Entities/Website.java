package com.zookanews.egyptlatestnews.RoomDB.Entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "websites_table")
public class Website {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "ID")
    private int websiteId;

    @ColumnInfo(name = "title")
    private String websiteTitle;

    @NonNull
    @ColumnInfo(name = "name")
    private String websiteName;

    @ColumnInfo(name = "link")
    private String websiteLink;

    @NonNull
    @ColumnInfo(name = "rss_link")
    private String websiteRssLink;

    public Website(@NonNull int websiteId, @NonNull String websiteName, String websiteTitle, String websiteLink, @NonNull String websiteRssLink) {
        this.websiteId = websiteId;
        this.websiteName = websiteName;
        this.websiteTitle = websiteTitle;
        this.websiteLink = websiteLink;
        this.websiteRssLink = websiteRssLink;
    }

    @Ignore
    public Website(@NonNull String websiteName, String websiteTitle, String websiteLink, @NonNull String websiteRssLink) {
        this.websiteName = websiteName;
        this.websiteTitle = websiteTitle;
        this.websiteLink = websiteLink;
        this.websiteRssLink = websiteRssLink;
    }

    @NonNull
    public int getWebsiteId() {
        return websiteId;
    }

    public void setWebsiteId(@NonNull int websiteId) {
        this.websiteId = websiteId;
    }

    public String getWebsiteTitle() {
        return websiteTitle;
    }

    public void setWebsiteTitle(String websiteTitle) {
        this.websiteTitle = websiteTitle;
    }

    @NonNull
    public String getWebsiteName() {
        return websiteName;
    }

    public void setWebsiteName(@NonNull String websiteName) {
        this.websiteName = websiteName;
    }

    public String getWebsiteLink() {
        return websiteLink;
    }

    public void setWebsiteLink(String websiteLink) {
        this.websiteLink = websiteLink;
    }

    @NonNull
    public String getWebsiteRssLink() {
        return websiteRssLink;
    }

    public void setWebsiteRssLink(@NonNull String websiteRssLink) {
        this.websiteRssLink = websiteRssLink;
    }
}
