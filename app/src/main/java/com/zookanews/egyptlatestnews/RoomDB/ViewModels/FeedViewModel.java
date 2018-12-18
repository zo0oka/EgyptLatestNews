package com.zookanews.egyptlatestnews.RoomDB.ViewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import com.zookanews.egyptlatestnews.RoomDB.Entities.Feed;
import com.zookanews.egyptlatestnews.RoomDB.Repositories.FeedRepository;

public class FeedViewModel extends AndroidViewModel {

    private final FeedRepository feedRepository;

    public FeedViewModel(@NonNull Application application) {
        super(application);
        feedRepository = new FeedRepository(application);
    }

    public void insertFeed(Feed feed) {
        feedRepository.insertFeed(feed);
    }
}
