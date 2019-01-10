package com.zookanews.egyptlatestnews.WorkManager;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.zookanews.egyptlatestnews.Helpers.Constants;
import com.zookanews.egyptlatestnews.RoomDB.DAO.ArticleDao;
import com.zookanews.egyptlatestnews.RoomDB.DB.FeedRoomDatabase;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class DeleteReadArticlesWorker extends Worker {
    private SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    private int noOfDays = Integer.valueOf(sharedPreferences.getString(Constants.AUTO_CLEANUP_READ, "2"));

    public DeleteReadArticlesWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        deleteReadArticles(noOfDays);
        return Result.success();
    }

    private void deleteReadArticles(int noOfDays) {
        ArticleDao articleDao = FeedRoomDatabase.getDatabase(getApplicationContext()).articleDao();
        articleDao.deleteReadArticlesOlderThan(noOfDays);
    }
}
