package com.zookanews.egyptlatestnews.RoomDB.DAO;

import com.zookanews.egyptlatestnews.RoomDB.Entities.Article;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface ArticleDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insertArticle(Article article);

    @Query("SELECT * FROM articles_table ORDER BY date DESC")
    DataSource.Factory<Integer, Article> getAllArticles();

    @Query("SELECT * FROM articles_table WHERE category_name = :categoryName ORDER BY date DESC")
    DataSource.Factory<Integer, Article> getCategoryArticles(String categoryName);

    @Query("SELECT * FROM articles_table WHERE website_name = :websiteName ORDER BY date DESC")
    DataSource.Factory<Integer, Article> getWebsiteArticles(String websiteName);

    @Query("SELECT * FROM articles_table WHERE isFavorite = 1 ORDER BY date DESC")
    LiveData<List<Article>> getFavoriteArticles();

    @Query("UPDATE articles_table SET isRead = :isRead WHERE ID = :articleId")
    void updateReadStatus(int articleId, Boolean isRead);

    @Query("UPDATE articles_table SET isFavorite = :isFavorite WHERE ID = :articleId")
    void updateFavoriteStatus(int articleId, Boolean isFavorite);

    @Query("SELECT * FROM articles_table WHERE ID = :articleId")
    Article getArticleById(int articleId);

    @Query("SELECT * FROM articles_table WHERE description LIKE :searchQuery ORDER BY date DESC")
    DataSource.Factory<Integer, Article> searchResultArticles(String searchQuery);

    @Query("DELETE FROM articles_table WHERE date <= strftime('%s', 'now', '-' || :noOfDays || ' days') AND isRead = 0")
    void deleteUnreadArticlesOlderThan(int noOfDays);

    @Query("DELETE FROM articles_table WHERE date <= strftime('%s', 'now', '-' || :noOfDays || ' days') AND isRead = 1")
    void deleteReadArticlesOlderThan(int noOfDays);

    @Query("SELECT COUNT(ID) FROM articles_table WHERE isRead = 0 AND category_name = :categoryName")
    LiveData<Integer> getCountOfCategoryUnreadArticles(String categoryName);

    @Query("SELECT COUNT(ID) FROM articles_table WHERE isRead = 0 AND website_name = :websiteName")
    LiveData<Integer> getCountOfWebsiteUnreadArticles(String websiteName);
}
