package com.zookanews.egyptlatestnews.RoomDB.ViewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import com.zookanews.egyptlatestnews.RoomDB.Entities.Website;
import com.zookanews.egyptlatestnews.RoomDB.Repositories.WebsiteRepository;

public class WebsiteViewModel extends AndroidViewModel {

    private final WebsiteRepository websiteRepository;

    public WebsiteViewModel(@NonNull Application application) {
        super(application);
        websiteRepository = new WebsiteRepository(application);
    }

    public void insertWebsite(Website website) {
        websiteRepository.insertWebsite(website);
    }
}
