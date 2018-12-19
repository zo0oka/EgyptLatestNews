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
        new insertFeedAsyncTask(feedDao).execute(feed);
    }

    @SuppressLint("StaticFieldLeak")
    public List<Feed> getAllFeeds() throws ExecutionException, InterruptedException {
        return new getAllFeedsAsyncTask(feedDao).execute().get();
    }

    private static class insertFeedAsyncTask extends AsyncTask<Feed, Void, Void> {
        private FeedDao asyncTaskDao;

        insertFeedAsyncTask(FeedDao feedDao) {
            asyncTaskDao = feedDao;
        }

        @Override
        protected Void doInBackground(Feed... feeds) {
            asyncTaskDao.insertFeed(feeds[0]);
            return null;
        }
    }

    private static class getAllFeedsAsyncTask extends AsyncTask<Void, Void, List<Feed>> {
        private FeedDao asyncTaskDao;

        getAllFeedsAsyncTask(FeedDao feedDao) {
            asyncTaskDao = feedDao;
        }

        @Override
        protected List<Feed> doInBackground(Void... voids) {
            return asyncTaskDao.getAllFeeds();
        }
    }
}
