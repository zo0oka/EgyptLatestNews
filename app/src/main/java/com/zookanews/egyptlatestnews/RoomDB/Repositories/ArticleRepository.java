package com.zookanews.egyptlatestnews.RoomDB.Repositories;

import android.annotation.SuppressLint;
import android.app.Application;
import android.os.AsyncTask;

import com.zookanews.egyptlatestnews.Helpers.Params;
import com.zookanews.egyptlatestnews.RoomDB.DAO.ArticleDao;
import com.zookanews.egyptlatestnews.RoomDB.DB.FeedRoomDatabase;
import com.zookanews.egyptlatestnews.RoomDB.Entities.Article;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

public class ArticleRepository {
    private static final int DATABASE_PAGE_SIZE = 50;
    private final ArticleDao articleDao;
    private LiveData<List<Article>> favoriteArticles;

    public ArticleRepository(Application application) {
        FeedRoomDatabase db = FeedRoomDatabase.getDatabase(application);
        articleDao = db.articleDao();
        favoriteArticles = articleDao.getFavoriteArticles();
    }

    public LiveData<PagedList<Article>> getAllArticles() {
        DataSource.Factory<Integer, Article> articleDataSourceFactory = articleDao.getAllArticles();
        return new LivePagedListBuilder<>(articleDataSourceFactory, DATABASE_PAGE_SIZE)
                .setFetchExecutor(Executors.newFixedThreadPool(5))
                .build();
    }

    public LiveData<PagedList<Article>> getCategoryArticles(String categoryName) {
        DataSource.Factory<Integer, Article> articleDataSourceFactory = articleDao.getCategoryArticles(categoryName);
        return new LivePagedListBuilder<>(articleDataSourceFactory, DATABASE_PAGE_SIZE)
                .setFetchExecutor(Executors.newFixedThreadPool(5))
                .build();
    }

    public LiveData<PagedList<Article>> getWebsiteArticles(String websiteName) {
        DataSource.Factory<Integer, Article> articleDataSourceFactory = articleDao.getWebsiteArticles(websiteName);
        return new LivePagedListBuilder<>(articleDataSourceFactory, DATABASE_PAGE_SIZE)
                .setFetchExecutor(Executors.newFixedThreadPool(5))
                .build();
    }

    @SuppressLint("StaticFieldLeak")
    public void updateReadStatus(final int articleId, final Boolean isRead) {
        Params params = new Params(articleId, isRead);
        new AsyncTask<Params, Void, Void>() {
            @Override
            protected Void doInBackground(Params... params) {
                articleDao.updateReadStatus(params[0].getId(), params[0].getRead());
                return null;
            }
        }.execute(params);
    }

    @SuppressLint("StaticFieldLeak")
    public void updateFavoriteStatus(final int articleId, final Boolean isFavorite) {
        Params params = new Params(articleId, isFavorite);
        new AsyncTask<Params, Void, Void>() {
            @Override
            protected Void doInBackground(Params... params) {
                articleDao.updateFavoriteStatus(params[0].getId(), params[0].getRead());
                return null;
            }
        }.execute(params);
    }

    @SuppressLint("StaticFieldLeak")
    public Article getArticleById(final int articleId) throws ExecutionException, InterruptedException {
        return new AsyncTask<Integer, Void, Article>() {
            @Override
            protected Article doInBackground(Integer... integers) {
                return articleDao.getArticleById(integers[0]);
            }
        }.execute(articleId).get();
    }

    public LiveData<PagedList<Article>> searchResultArticles(String searchQuery) {
        DataSource.Factory<Integer, Article> articleDataSourceFactory = articleDao.searchResultArticles(searchQuery);
        return new LivePagedListBuilder<>(articleDataSourceFactory, DATABASE_PAGE_SIZE)
                .setFetchExecutor(Executors.newFixedThreadPool(5))
                .build();
    }

    public LiveData<List<Article>> getFavoriteArticles() {
        return favoriteArticles;
    }

    public LiveData<Integer> getCountOfCategoryUnreadArticles(String categoryName) {
        return articleDao.getCountOfCategoryUnreadArticles(categoryName);
    }

    public LiveData<Integer> getCountOfWebsiteUnreadArticles(String websiteName) {
        return articleDao.getCountOfWebsiteUnreadArticles(websiteName);
    }
}
