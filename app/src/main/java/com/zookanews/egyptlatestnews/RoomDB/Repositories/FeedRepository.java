package com.zookanews.egyptlatestnews.RoomDB.Repositories;

import android.annotation.SuppressLint;
import android.app.Application;
import android.os.AsyncTask;

import com.zookanews.egyptlatestnews.RoomDB.DAO.FeedDao;
import com.zookanews.egyptlatestnews.RoomDB.DB.FeedRoomDatabase;
import com.zookanews.egyptlatestnews.RoomDB.Entities.Feed;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class FeedRepository {

    private final FeedDao feedDao;

    public FeedRepository(Application application) {
        FeedRoomDatabase db = FeedRoomDatabase.getDatabase(application);
        feedDao = db.feedDao();
    }

    @SuppressLint("StaticFieldLeak")
    public void insertFeed(Feed feed) {
        new AsyncTask<Feed, Void, Void>() {
            @Override
            protected Void doInBackground(Feed... feeds) {
                feedDao.insertFeed(feeds[0]);
                return null;
            }
        }.execute(feed);
    }

    @SuppressLint("StaticFieldLeak")
    public List<Feed> getAllFeeds() throws ExecutionException, InterruptedException {
        return new AsyncTask<Void, Void, List<Feed>>() {
            @Override
            protected List<Feed> doInBackground(Void... voids) {
                return feedDao.getAllFeeds();
            }
        }.execute().get();
    }
}
