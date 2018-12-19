package com.zookanews.egyptlatestnews.RoomDB.ViewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import com.zookanews.egyptlatestnews.RoomDB.Entities.Feed;
import com.zookanews.egyptlatestnews.RoomDB.Repositories.FeedRepository;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class FeedViewModel extends AndroidViewModel {

    private final FeedRepository feedRepository;

    public FeedViewModel(@NonNull Application application) {
        super(application);
        feedRepository = new FeedRepository(application);
    }

    public void insertFeed(Feed feed) {
        feedRepository.insertFeed(feed);
    }

    public List<Feed> getAllFeeds() throws ExecutionException, InterruptedException {
        return feedRepository.getAllFeeds();
    }
}
