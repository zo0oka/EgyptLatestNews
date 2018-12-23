package com.zookanews.egyptlatestnews.RoomDB.DAO;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.zookanews.egyptlatestnews.RoomDB.Entities.Article;

import java.util.List;

@Dao
public interface ArticleDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insertArticle(Article article);

    @Query("SELECT * FROM articles_table ORDER BY date DESC")
    LiveData<List<Article>> getAllArticles();

    @Query("SELECT * FROM articles_table WHERE category_name = :categoryName ORDER BY ID DESC")
    List<Article> getCategoryArticles(String categoryName);

    @Query("SELECT * FROM articles_table WHERE website_name = :websiteName ORDER BY ID DESC")
    List<Article> getWebsiteArticles(String websiteName);

    @Query("SELECT * FROM articles_table WHERE isRead = :isRead ORDER BY ID DESC")
    List<Article> getUnreadArticles(Boolean isRead);

    @Query("SELECT * FROM articles_table WHERE isRead = 1 ORDER BY ID DESC")
    List<Article> getReadArticles();

    @Query("DELETE FROM articles_table WHERE isRead = 1")
    void deleteReadArticles();

    @Query("DELETE FROM articles_table")
    void deleteAllArticles();

    @Query("DELETE FROM articles_table WHERE isRead = 0")
    void deleteUnreadArticles();

    @Query("UPDATE articles_table SET isRead = :isRead WHERE ID = :articleId")
    void updateReadStatus(int articleId, Boolean isRead);

    @Query("UPDATE articles_table SET isRead = 1")
    void setAllAsRead();

    @Query("SELECT * FROM articles_table WHERE ID = :articleId")
    Article getArticleById(int articleId);

    @Query("SELECT * FROM articles_table WHERE description LIKE :searchQuery ORDER BY ID DESC")
    List<Article> searchResultArticles(String searchQuery);
}
