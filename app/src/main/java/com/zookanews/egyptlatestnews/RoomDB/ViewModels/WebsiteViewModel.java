package com.zookanews.egyptlatestnews.RoomDB.ViewModels;

import android.app.Application;

import com.zookanews.egyptlatestnews.RoomDB.Entities.Website;
import com.zookanews.egyptlatestnews.RoomDB.Repositories.WebsiteRepository;

import java.util.concurrent.ExecutionException;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public class WebsiteViewModel extends AndroidViewModel {

    private final WebsiteRepository websiteRepository;

    public WebsiteViewModel(@NonNull Application application) {
        super(application);
        websiteRepository = new WebsiteRepository(application);
    }

    public Website getWebsiteByName(String websiteName) throws ExecutionException, InterruptedException {
        return websiteRepository.getWebsiteByName(websiteName);
    }
}
