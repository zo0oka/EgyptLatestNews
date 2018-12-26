package com.zookanews.egyptlatestnews.RoomDB.Repositories;

import android.app.Application;
import android.os.AsyncTask;

import com.zookanews.egyptlatestnews.RoomDB.DAO.WebsiteDao;
import com.zookanews.egyptlatestnews.RoomDB.DB.FeedRoomDatabase;
import com.zookanews.egyptlatestnews.RoomDB.Entities.Website;

import java.util.concurrent.ExecutionException;

public class WebsiteRepository {

    private final WebsiteDao websiteDao;

    public WebsiteRepository(Application application) {
        FeedRoomDatabase db = FeedRoomDatabase.getDatabase(application);
        websiteDao = db.websiteDao();
    }

    public void insertWebsite(Website website) {
        new insertWebsiteAsyncTask(websiteDao).execute(website);
    }

    public Website getWebsiteByName(String websiteName) throws ExecutionException, InterruptedException {
        return new getWebsiteByNameAsyncTask(websiteDao).execute(websiteName).get();
    }

    private static class insertWebsiteAsyncTask extends AsyncTask<Website, Void, Void> {
        private WebsiteDao asyncTaskDao;

        insertWebsiteAsyncTask(WebsiteDao websiteDao) {
            asyncTaskDao = websiteDao;
        }

        @Override
        protected Void doInBackground(Website... websites) {
            asyncTaskDao.insertWebsite(websites[0]);
            return null;
        }
    }

    private static class getWebsiteByNameAsyncTask extends AsyncTask<String, Void, Website> {
        private WebsiteDao asyncTaskDao;

        getWebsiteByNameAsyncTask(WebsiteDao websiteDao) {
            asyncTaskDao = websiteDao;
        }

        @Override
        protected Website doInBackground(String... strings) {
            return asyncTaskDao.getWebsiteByName(strings[0]);
        }
    }
}
