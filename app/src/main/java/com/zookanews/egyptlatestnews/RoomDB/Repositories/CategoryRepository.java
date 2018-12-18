package com.zookanews.egyptlatestnews.RoomDB.Repositories;

import android.annotation.SuppressLint;
import android.app.Application;
import android.os.AsyncTask;

import com.zookanews.egyptlatestnews.RoomDB.DAO.CategoryDao;
import com.zookanews.egyptlatestnews.RoomDB.DB.FeedRoomDatabase;
import com.zookanews.egyptlatestnews.RoomDB.Entities.Category;

public class CategoryRepository {

    private final CategoryDao categoryDao;

    public CategoryRepository(Application application) {
        FeedRoomDatabase db = FeedRoomDatabase.getDatabase(application);
        categoryDao = db.categoryDao();
    }

    @SuppressLint("StaticFieldLeak")
    public void insertCategory(Category category) {
        new AsyncTask<Category, Void, Void>() {
            @Override
            protected Void doInBackground(Category... categories) {
                categoryDao.insertCategory(categories[0]);
                return null;
            }
        }.execute(category);
    }
}
