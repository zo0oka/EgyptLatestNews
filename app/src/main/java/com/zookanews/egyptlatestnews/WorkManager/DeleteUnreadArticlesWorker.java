package com.zookanews.egyptlatestnews.WorkManager;

import android.content.Context;
import android.support.annotation.NonNull;

import com.zookanews.egyptlatestnews.RoomDB.DAO.ArticleDao;
import com.zookanews.egyptlatestnews.RoomDB.DB.FeedRoomDatabase;

import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class DeleteUnreadArticlesWorker extends Worker {

    public DeleteUnreadArticlesWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        deleteUnreadArticles();
        return Result.success();
    }

    private void deleteUnreadArticles() {
        ArticleDao articleDao = FeedRoomDatabase.getDatabase(getApplicationContext()).articleDao();
        articleDao.deleteUnreadArticles();
    }
}
