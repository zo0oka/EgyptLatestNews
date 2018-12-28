package com.zookanews.egyptlatestnews.RoomDB.Repositories;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.zookanews.egyptlatestnews.Helpers.Params;
import com.zookanews.egyptlatestnews.RoomDB.DAO.ArticleDao;
import com.zookanews.egyptlatestnews.RoomDB.DB.FeedRoomDatabase;
import com.zookanews.egyptlatestnews.RoomDB.Entities.Article;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class ArticleRepository {
    private final ArticleDao articleDao;
    private LiveData<List<Article>> allArticles;

    public ArticleRepository(Application application) {
        FeedRoomDatabase db = FeedRoomDatabase.getDatabase(application);
        articleDao = db.articleDao();
        allArticles = articleDao.getAllArticles();
    }

    public LiveData<List<Article>> getAllArticles() {
        return allArticles;
    }

    public LiveData<List<Article>> getCategoryArticles(String categoryName) {
        return articleDao.getCategoryArticles(categoryName);
    }

    public LiveData<List<Article>> getWebsiteArticles(String websiteName) {
        return articleDao.getWebsiteArticles(websiteName);
    }

    public void updateReadStatus(int articleId, Boolean isRead) {
        Params params = new Params(articleId, isRead);
        new updateReadStatusAsyncTask(articleDao).execute(params);
    }

    public void updateFavoriteStatus(int articleId, Boolean isFavorite) {
        Params params = new Params(articleId, isFavorite);
        new updateFavoriteStatusAsyncTask(articleDao).execute(params);
    }

    public Article getArticleById(int articleId) throws ExecutionException, InterruptedException {
        Params params = new Params(articleId);
        return new getArticleByIdAsyncTask(articleDao).execute(params).get();
    }

    public List<Article> searchResultArticles(String searchQuery) throws ExecutionException, InterruptedException {
        return new searchResultArticlesAsyncTask(articleDao).execute(searchQuery).get();
    }

    public LiveData<List<Article>> getFavoriteArticles() throws ExecutionException, InterruptedException {
        return new getFavoriteArticlesAsyncTask(articleDao).execute().get();
    }

    public LiveData<Integer> getCountOfCategoryUnreadArticles(String categoryName) {
        return articleDao.getCountOfCategoryUnreadArticles(categoryName);
    }

    public LiveData<Integer> getCountOfWebsiteUnreadArticles(String websiteName) {
        return articleDao.getCountOfWebsiteUnreadArticles(websiteName);
    }

    private static class updateReadStatusAsyncTask extends AsyncTask<Params, Void, Void> {
        private ArticleDao asyncTaskDao;

        updateReadStatusAsyncTask(ArticleDao articleDao) {
            asyncTaskDao = articleDao;
        }

        @Override
        protected Void doInBackground(Params... params) {
            asyncTaskDao.updateReadStatus(params[0].getId(), params[0].getRead());
            return null;
        }
    }

    private static class getArticleByIdAsyncTask extends AsyncTask<Params, Void, Article> {
        private ArticleDao asyncTaskDao;

        getArticleByIdAsyncTask(ArticleDao articleDao) {
            asyncTaskDao = articleDao;
        }

        @Override
        protected Article doInBackground(Params... params) {
            return asyncTaskDao.getArticleById(params[0].getId());
        }
    }

    private static class searchResultArticlesAsyncTask extends AsyncTask<String, Void, List<Article>> {
        private ArticleDao asyncTaskDao;

        searchResultArticlesAsyncTask(ArticleDao articleDao) {
            asyncTaskDao = articleDao;
        }

        @Override
        protected List<Article> doInBackground(String... strings) {
            return asyncTaskDao.searchResultArticles(strings[0]);
        }
    }

    private static class updateFavoriteStatusAsyncTask extends AsyncTask<Params, Void, Void> {
        ArticleDao asyncTaskDao;

        updateFavoriteStatusAsyncTask(ArticleDao articleDao) {
            asyncTaskDao = articleDao;
        }

        @Override
        protected Void doInBackground(Params... params) {
            asyncTaskDao.updateFavoriteStatus(params[0].getId(), params[0].getRead());
            return null;
        }
    }

    private static class getFavoriteArticlesAsyncTask extends AsyncTask<Void, Void, LiveData<List<Article>>> {
        private ArticleDao asyncTaskDao;

        getFavoriteArticlesAsyncTask(ArticleDao articleDao) {
            asyncTaskDao = articleDao;
        }

        @Override
        protected LiveData<List<Article>> doInBackground(Void... voids) {
            return asyncTaskDao.getFavoriteArticles();
        }
    }
}
