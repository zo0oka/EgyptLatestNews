package com.zookanews.egyptlatestnews.RoomDB.ViewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import com.zookanews.egyptlatestnews.RoomDB.Entities.Category;
import com.zookanews.egyptlatestnews.RoomDB.Repositories.CategoryRepository;

public class CategoryViewModel extends AndroidViewModel {

    private final CategoryRepository categoryRepository;

    public CategoryViewModel(@NonNull Application application) {
        super(application);
        categoryRepository = new CategoryRepository(application);
    }

    public void insertCategory(Category category) {
        categoryRepository.insertCategory(category);
    }
}
