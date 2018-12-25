package com.zookanews.egyptlatestnews.RoomDB.ViewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.zookanews.egyptlatestnews.RoomDB.Entities.Article;
import com.zookanews.egyptlatestnews.RoomDB.Repositories.ArticleRepository;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class ArticleViewModel extends AndroidViewModel {

    private final ArticleRepository articleRepository;
    private LiveData<List<Article>> allArticles;

    public ArticleViewModel(@NonNull Application application) {
        super(application);
        articleRepository = new ArticleRepository(application);
        allArticles = articleRepository.getAllArticles();
    }

    public LiveData<List<Article>> getAllArticles() {
        return allArticles;
    }

    public long insertArticle(Article article) throws ExecutionException, InterruptedException {
        return articleRepository.insertArticle(article);
    }

    public List<Article> getCategoryArticles(String categoryName) throws ExecutionException, InterruptedException {
        return articleRepository.getCategoryArticles(categoryName);
    }

    public List<Article> getWebsiteArticles(String websiteName) throws ExecutionException, InterruptedException {
        return articleRepository.getWebsiteArticles(websiteName);
    }

    public void setAllAsRead() {
        articleRepository.setAllAsRead();
    }

    public List<Article> getUnreadArticles(Boolean isRead) throws ExecutionException, InterruptedException {
        return articleRepository.getUnreadArticles(isRead);
    }

    public Article getArticleById(int articleId) throws ExecutionException, InterruptedException {
        return articleRepository.getArticleById(articleId);
    }

    public void deleteReadArticles() {
        articleRepository.deleteReadArticles();
    }

    public void deleteUnreadArticles() {
        articleRepository.deleteUnreadArticles();
    }

    public void deleteAllArticles() {
        articleRepository.deleteAllArticles();
    }

    public void updateReadStatus(int articleId, Boolean isRead) {
        articleRepository.updateReadStatus(articleId, isRead);
    }

    public List<Article> searchResultArticles(String searchQuery) throws ExecutionException, InterruptedException {
        return articleRepository.searchResultArticles(searchQuery);
    }

    public void updateFavoriteStatus(int articleId, Boolean isFavorite) {
        articleRepository.updateFavoriteStatus(articleId, isFavorite);
    }

    public List<Article> getReadArticles() throws ExecutionException, InterruptedException {
        return articleRepository.getReadArticles();
    }

    public List<Article> getFavoriteArticles() throws ExecutionException, InterruptedException {
        return articleRepository.getFavoriteArticles();
    }

    public List<Article> getArticlesOlderThan() throws ExecutionException, InterruptedException {
        return articleRepository.getArticlesOlderthan();
    }

    public void deleteUnreadArticlesOlderThan(int noOfDays) {
        articleRepository.deleteUnreadArticlesOlderThan(noOfDays);
    }

    public void deleteReadArticlesOlderThan(int noOfDays) {
        articleRepository.deleteReadArticlesOlderThan(noOfDays);
    }

    public LiveData<Integer> getCountOfCategoryUnreadArticles(String categoryName) {
        return articleRepository.getCountOfCategoryUnreadArticles(categoryName);
    }

    public LiveData<Integer> getCountOfWebsiteUnreadArticles(String websiteName) {
        return articleRepository.getCountOfWebsiteUnreadArticles(websiteName);
    }
}
