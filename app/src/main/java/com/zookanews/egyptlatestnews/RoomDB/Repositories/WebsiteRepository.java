package com.zookanews.egyptlatestnews.RoomDB.Repositories;

import android.annotation.SuppressLint;
import android.app.Application;
import android.os.AsyncTask;

import com.zookanews.egyptlatestnews.RoomDB.DAO.WebsiteDao;
import com.zookanews.egyptlatestnews.RoomDB.DB.FeedRoomDatabase;
import com.zookanews.egyptlatestnews.RoomDB.Entities.Website;

public class WebsiteRepository {

    private final WebsiteDao websiteDao;

    public WebsiteRepository(Application application) {
        FeedRoomDatabase db = FeedRoomDatabase.getDatabase(application);
        websiteDao = db.websiteDao();
    }

    @SuppressLint("StaticFieldLeak")
    public void insertWebsite(Website website) {
        new AsyncTask<Website, Void, Void>() {
            @Override
            protected Void doInBackground(Website... websites) {
                websiteDao.insertWebsite(websites[0]);
                return null;
            }
        }.execute(website);
    }
}
