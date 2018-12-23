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

    public List<Article> getReadArticles() throws ExecutionException, InterruptedException {
        return articleRepository.getReadArticles();
    }
}
