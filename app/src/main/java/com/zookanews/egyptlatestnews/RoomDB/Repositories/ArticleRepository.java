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

    public long insertArticle(Article article) throws ExecutionException, InterruptedException {
        return new insertArticleAsyncTask(articleDao).execute(article).get();
    }

    public LiveData<List<Article>> getAllArticles() {
        return allArticles;
    }

    public List<Article> getCategoryArticles(String categoryName) throws ExecutionException, InterruptedException {
        return new getCategoryArticlesAsyncTask(articleDao).execute(categoryName).get();
    }

    public List<Article> getWebsiteArticles(String websiteName) throws ExecutionException, InterruptedException {
        return new getWebsiteArticlesAsyncTask(articleDao).execute(websiteName).get();
    }

    public List<Article> getUnreadArticles() throws ExecutionException, InterruptedException {
        return new getUnreadArticlesAsyncTask(articleDao).execute().get();
    }

    public void deleteAllArticles() {
        new deleteAllArticlesAsyncTask(articleDao).execute();
    }

    public void deleteUnreadArticles() {
        new deleteUnreadArticlesAsyncTask(articleDao).execute();
    }

    public void deleteReadArticles() {
        new deleteReadArticlesAsyncTask(articleDao).execute();
    }

    public void updateReadStatus(int articleId, Boolean isRead) {
        Params params = new Params(articleId, isRead);
        new updateReadStatusAsyncTask(articleDao).execute(params);
    }

    public void setAllAsRead() {
        new setAllAsReadAsyncTask(articleDao).execute();
    }

    public Article getArticleById(int articleId) throws ExecutionException, InterruptedException {
        Params params = new Params(articleId);
        return new getArticleByIdAsyncTask(articleDao).execute(params).get();
    }

    public List<Article> searchResultArticles(String searchQuery) throws ExecutionException, InterruptedException {
        return new searchResultArticlesAsyncTask(articleDao).execute(searchQuery).get();
    }

    public List<Article> getReadArticles() throws ExecutionException, InterruptedException {
        return new getReadArticlesAsyncTask(articleDao).execute().get();
    }

    private static class insertArticleAsyncTask extends AsyncTask<Article, Void, Long> {
        private ArticleDao asyncTaskDao;

        insertArticleAsyncTask(ArticleDao articleDao) {
            asyncTaskDao = articleDao;
        }

        @Override
        protected Long doInBackground(Article... articles) {
            return asyncTaskDao.insertArticle(articles[0]);

        }
    }

    private static class getCategoryArticlesAsyncTask extends AsyncTask<String, Void, List<Article>> {
        private ArticleDao asyncTaskDao;

        getCategoryArticlesAsyncTask(ArticleDao articleDao) {
            asyncTaskDao = articleDao;
        }

        @Override
        protected List<Article> doInBackground(String... strings) {
            return asyncTaskDao.getCategoryArticles(strings[0]);
        }
    }

    private static class getWebsiteArticlesAsyncTask extends AsyncTask<String, Void, List<Article>> {
        private ArticleDao asyncTaskDao;

        getWebsiteArticlesAsyncTask(ArticleDao articleDao) {
            asyncTaskDao = articleDao;
        }

        @Override
        protected List<Article> doInBackground(String... strings) {
            return asyncTaskDao.getWebsiteArticles(strings[0]);
        }
    }

    private static class getUnreadArticlesAsyncTask extends AsyncTask<Void, Void, List<Article>> {
        private ArticleDao asyncTaskDao;

        getUnreadArticlesAsyncTask(ArticleDao articleDao) {
            asyncTaskDao = articleDao;
        }

        @Override
        protected List<Article> doInBackground(Void... voids) {
            return asyncTaskDao.getUnreadArticles();
        }
    }

    private static class deleteAllArticlesAsyncTask extends AsyncTask<Void, Void, Void> {
        private ArticleDao asyncTaskDao;

        deleteAllArticlesAsyncTask(ArticleDao articleDao) {
            asyncTaskDao = articleDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            asyncTaskDao.deleteAllArticles();
            return null;
        }
    }

    private static class deleteUnreadArticlesAsyncTask extends AsyncTask<Void, Void, Void> {
        private ArticleDao asyncTaskDao;

        deleteUnreadArticlesAsyncTask(ArticleDao articleDao) {
            asyncTaskDao = articleDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            asyncTaskDao.getUnreadArticles();
            return null;
        }
    }

    private static class deleteReadArticlesAsyncTask extends AsyncTask<Void, Void, Void> {
        private ArticleDao asyncTaskDao;

        deleteReadArticlesAsyncTask(ArticleDao articleDao) {
            asyncTaskDao = articleDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            asyncTaskDao.deleteReadArticles();
            return null;
        }
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

    private static class getReadArticlesAsyncTask extends AsyncTask<Void, Void, List<Article>> {
        private ArticleDao asyncTaskDao;

        getReadArticlesAsyncTask(ArticleDao articleDao) {
            asyncTaskDao = articleDao;
        }

        @Override
        protected List<Article> doInBackground(Void... voids) {
            return asyncTaskDao.getReadArticles();
        }
    }

    private static class setAllAsReadAsyncTask extends AsyncTask<Void, Void, Void> {
        private ArticleDao asyncTaskDao;

        setAllAsReadAsyncTask(ArticleDao articleDao) {
            asyncTaskDao = articleDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            asyncTaskDao.setAllAsRead();
            return null;
        }
    }
}
