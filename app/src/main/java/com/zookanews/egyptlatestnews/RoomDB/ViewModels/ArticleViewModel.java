package com.zookanews.egyptlatestnews.RoomDB.ViewModels;

import android.app.Application;

import com.zookanews.egyptlatestnews.RoomDB.Entities.Article;
import com.zookanews.egyptlatestnews.RoomDB.Repositories.ArticleRepository;

import java.util.List;
import java.util.concurrent.ExecutionException;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.paging.PagedList;

public class ArticleViewModel extends AndroidViewModel {

    private final ArticleRepository articleRepository;
    private LiveData<PagedList<Article>> allArticles;
    private LiveData<List<Article>> favoriteArticles;

    public ArticleViewModel(@NonNull Application application) {
        super(application);
        articleRepository = new ArticleRepository(application);
        favoriteArticles = articleRepository.getFavoriteArticles();
        allArticles = articleRepository.getAllArticles();
    }

    public LiveData<PagedList<Article>> getAllArticles() {
        return allArticles;
    }

    public LiveData<PagedList<Article>> getCategoryArticles(String categoryName) {
        return articleRepository.getCategoryArticles(categoryName);
    }

    public LiveData<PagedList<Article>> getWebsiteArticles(String websiteName) {
        return articleRepository.getWebsiteArticles(websiteName);
    }

    public Article getArticleById(int articleId) throws ExecutionException, InterruptedException {
        return articleRepository.getArticleById(articleId);
    }

    public void updateReadStatus(int articleId, Boolean isRead) {
        articleRepository.updateReadStatus(articleId, isRead);
    }

    public LiveData<PagedList<Article>> searchResultArticles(String searchQuery) {
        return articleRepository.searchResultArticles(searchQuery);
    }

    public void updateFavoriteStatus(int articleId, Boolean isFavorite) {
        articleRepository.updateFavoriteStatus(articleId, isFavorite);
    }

    public LiveData<List<Article>> getFavoriteArticles() {
        return favoriteArticles;
    }

    public LiveData<Integer> getCountOfCategoryUnreadArticles(String categoryName) {
        return articleRepository.getCountOfCategoryUnreadArticles(categoryName);
    }

    public LiveData<Integer> getCountOfWebsiteUnreadArticles(String websiteName) {
        return articleRepository.getCountOfWebsiteUnreadArticles(websiteName);
    }
}
