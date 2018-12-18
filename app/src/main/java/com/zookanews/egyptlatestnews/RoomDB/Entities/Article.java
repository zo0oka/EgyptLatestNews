package com.zookanews.egyptlatestnews.RoomDB.Entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.Date;

@Entity(tableName = "articles_table", indices = {@Index(value = {"link"}, unique = true)})
public class Article {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "ID")
    private int articleId;

    @ColumnInfo(name = "title")
    private String articleTitle;

    @NonNull
    @ColumnInfo(name = "link")
    private String articleLink;

    @ColumnInfo(name = "description")
    private String articleDescription;

    @ColumnInfo(name = "date")
    private Date articlePubDate;

    @ColumnInfo(name = "thumb_url")
    private String articleThumbnailUrl;

    @ColumnInfo(name = "website_name")
    private String websiteName;

    @ColumnInfo(name = "category_name")
    private String categoryName;

    @ColumnInfo(name = "isRead")
    private Boolean isRead;

    public Article(@NonNull int articleId, String articleTitle, @NonNull String articleLink, String articleDescription, Date articlePubDate, String articleThumbnailUrl, String websiteName, String categoryName, Boolean isRead) {
        this.articleId = articleId;
        this.articleTitle = articleTitle;
        this.articleLink = articleLink;
        this.articleDescription = articleDescription;
        this.articlePubDate = articlePubDate;
        this.articleThumbnailUrl = articleThumbnailUrl;
        this.websiteName = websiteName;
        this.categoryName = categoryName;
        this.isRead = isRead;
    }

    @Ignore
    public Article(String articleTitle, @NonNull String articleLink, String articleDescription, Date articlePubDate, String articleThumbnailUrl, String websiteName, String categoryName, Boolean isRead) {
        this.articleTitle = articleTitle;
        this.articleLink = articleLink;
        this.articleDescription = articleDescription;
        this.articlePubDate = articlePubDate;
        this.articleThumbnailUrl = articleThumbnailUrl;
        this.websiteName = websiteName;
        this.categoryName = categoryName;
        this.isRead = isRead;
    }

    @NonNull
    public int getArticleId() {
        return articleId;
    }

    public void setArticleId(@NonNull int articleId) {
        this.articleId = articleId;
    }

    public String getArticleTitle() {
        return articleTitle;
    }

    public void setArticleTitle(String articleTitle) {
        this.articleTitle = articleTitle;
    }

    @NonNull
    public String getArticleLink() {
        return articleLink;
    }

    public void setArticleLink(@NonNull String articleLink) {
        this.articleLink = articleLink;
    }

    public String getArticleDescription() {
        return articleDescription;
    }

    public void setArticleDescription(String articleDescription) {
        this.articleDescription = articleDescription;
    }

    public Date getArticlePubDate() {
        return articlePubDate;
    }

    public void setArticlePubDate(Date articlePubDate) {
        this.articlePubDate = articlePubDate;
    }

    public String getArticleThumbnailUrl() {
        return articleThumbnailUrl;
    }

    public void setArticleThumbnailUrl(String articleThumbnailUrl) {
        this.articleThumbnailUrl = articleThumbnailUrl;
    }

    public String getWebsiteName() {
        return websiteName;
    }

    public void setWebsiteName(String websiteName) {
        this.websiteName = websiteName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Boolean getIsRead() {
        return isRead;
    }

    public void setIsRead(Boolean isRead) {
        this.isRead = isRead;
    }

    @NonNull
    @Override
    public String toString() {
        return "Title: " + articleTitle + "\n"
                + "Link: " + articleLink + "\n"
                + "Description: " + articleDescription + "\n"
                + "Date: " + articlePubDate + "\n"
                + "Thumbnail: " + articleThumbnailUrl + "\n"
                + "Website: " + websiteName + "\n"
                + "Category: " + categoryName + "\n"
                + "Read: " + isRead + ".";
    }
}
