package com.zookanews.egyptlatestnews.RoomDB.Repositories;

import android.annotation.SuppressLint;
import android.app.Application;
import android.os.AsyncTask;

import com.zookanews.egyptlatestnews.RoomDB.DAO.CategoryDao;
import com.zookanews.egyptlatestnews.RoomDB.DB.FeedRoomDatabase;
import com.zookanews.egyptlatestnews.RoomDB.Entities.Category;

import java.util.concurrent.ExecutionException;

public class CategoryRepository {

    private final CategoryDao categoryDao;

    public CategoryRepository(Application application) {
        FeedRoomDatabase db = FeedRoomDatabase.getDatabase(application);
        categoryDao = db.categoryDao();
    }

    @SuppressLint("StaticFieldLeak")
    public Category getCategoryByName(final String categoryName) throws ExecutionException, InterruptedException {
        return new AsyncTask<String, Void, Category>() {
            @Override
            protected Category doInBackground(String... strings) {
                return categoryDao.getCategoryByName(strings[0]);
            }
        }.execute(categoryName).get();
    }
}
