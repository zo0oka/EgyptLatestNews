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
    public void insertCategory(Category category) {
        new insertCategoryAsyncTask(categoryDao).execute(category);
    }

    public Category getCategoryByName(String categoryName) throws ExecutionException, InterruptedException {
        return new getCategoryByNameAsyncTask(categoryDao).execute(categoryName).get();
    }

    private static class insertCategoryAsyncTask extends AsyncTask<Category, Void, Void> {
        private CategoryDao asyncTaskDao;

        insertCategoryAsyncTask(CategoryDao categoryDao) {
            asyncTaskDao = categoryDao;
        }

        @Override
        protected Void doInBackground(Category... categories) {
            asyncTaskDao.insertCategory(categories[0]);
            return null;
        }
    }

    private static class getCategoryByNameAsyncTask extends AsyncTask<String, Void, Category> {
        private CategoryDao asyncTaskDao;

        getCategoryByNameAsyncTask(CategoryDao categoryDao) {
            asyncTaskDao = categoryDao;
        }

        @Override
        protected Category doInBackground(String... strings) {
            return asyncTaskDao.getCategoryByName(strings[0]);
        }
    }
}
