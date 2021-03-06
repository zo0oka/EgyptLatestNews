package com.zookanews.egyptlatestnews.WorkManager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import com.zookanews.egyptlatestnews.Helpers.Constants;
import com.zookanews.egyptlatestnews.RoomDB.DAO.ArticleDao;
import com.zookanews.egyptlatestnews.RoomDB.DB.FeedRoomDatabase;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class DeleteUnreadArticlesWorker extends Worker {
    private SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    private int noOfDays = Integer.valueOf(sharedPreferences.getString(Constants.AUTO_CLEANUP_UNREAD, "2"));

    public DeleteUnreadArticlesWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        deleteUnreadArticles(noOfDays);
        return Result.success();
    }

    @SuppressLint("StaticFieldLeak")
    private void deleteUnreadArticles(int noOfDays) {
        final ArticleDao articleDao = FeedRoomDatabase.getDatabase(getApplicationContext()).articleDao();
        new AsyncTask<Integer, Void, Void>() {
            @Override
            protected Void doInBackground(Integer... integers) {
                articleDao.deleteUnreadArticlesOlderThan(integers[0]);
                return null;
            }
        }.execute(noOfDays);
    }
}
