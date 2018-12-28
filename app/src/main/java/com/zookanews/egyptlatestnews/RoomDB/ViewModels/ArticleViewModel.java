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

    public LiveData<List<Article>> getCategoryArticles(String categoryName) {
        return articleRepository.getCategoryArticles(categoryName);
    }

    public LiveData<List<Article>> getWebsiteArticles(String websiteName) {
        return articleRepository.getWebsiteArticles(websiteName);
    }

    public Article getArticleById(int articleId) throws ExecutionException, InterruptedException {
        return articleRepository.getArticleById(articleId);
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

    public LiveData<List<Article>> getFavoriteArticles() throws ExecutionException, InterruptedException {
        return articleRepository.getFavoriteArticles();
    }

    public LiveData<Integer> getCountOfCategoryUnreadArticles(String categoryName) {
        return articleRepository.getCountOfCategoryUnreadArticles(categoryName);
    }

    public LiveData<Integer> getCountOfWebsiteUnreadArticles(String websiteName) {
        return articleRepository.getCountOfWebsiteUnreadArticles(websiteName);
    }
}
