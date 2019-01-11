package com.zookanews.egyptlatestnews.RoomDB.Repositories;

import android.annotation.SuppressLint;
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

    @SuppressLint("StaticFieldLeak")
    public Website getWebsiteByName(final String websiteName) throws ExecutionException, InterruptedException {
        return new AsyncTask<String, Void, Website>() {
            @Override
            protected Website doInBackground(String... strings) {
                return websiteDao.getWebsiteByName(strings[0]);
            }
        }.execute(websiteName).get();
    }
}
