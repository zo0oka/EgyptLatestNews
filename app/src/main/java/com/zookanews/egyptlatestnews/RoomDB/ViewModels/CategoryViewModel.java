package com.zookanews.egyptlatestnews.RoomDB.ViewModels;

import android.app.Application;

import com.zookanews.egyptlatestnews.RoomDB.Entities.Category;
import com.zookanews.egyptlatestnews.RoomDB.Repositories.CategoryRepository;

import java.util.concurrent.ExecutionException;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public class CategoryViewModel extends AndroidViewModel {

    private final CategoryRepository categoryRepository;

    public CategoryViewModel(@NonNull Application application) {
        super(application);
        categoryRepository = new CategoryRepository(application);
    }
    public Category getCategoryByName(String categoryName) throws ExecutionException, InterruptedException {
        return categoryRepository.getCategoryByName(categoryName);
    }
}
