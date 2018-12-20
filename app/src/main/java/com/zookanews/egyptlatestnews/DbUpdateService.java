package com.zookanews.egyptlatestnews;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.zookanews.egyptlatestnews.Parser.SaxXmlParser;
import com.zookanews.egyptlatestnews.RoomDB.DAO.ArticleDao;
import com.zookanews.egyptlatestnews.RoomDB.DAO.FeedDao;
import com.zookanews.egyptlatestnews.RoomDB.DB.FeedRoomDatabase;
import com.zookanews.egyptlatestnews.RoomDB.Entities.Article;
import com.zookanews.egyptlatestnews.RoomDB.Entities.Feed;
import com.zookanews.egyptlatestnews.UI.MainActivity;

import java.util.ArrayList;
import java.util.List;

public class DbUpdateService extends IntentService {

    private static final String CHANNEL_ID = "egyptlatestnews";

    public DbUpdateService() {
        super("dbsync");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        showToast("Sync started");
        startForeground(1, createNotification());
        List<Article> articles = new ArrayList<>();
        List<Long> ids = new ArrayList<>();
        ArticleDao articleDao = FeedRoomDatabase.getDatabase(getApplicationContext()).articleDao();
        FeedDao feedDao = FeedRoomDatabase.getDatabase(getApplicationContext()).feedDao();
        for (Feed feed : feedDao.getAllFeeds()) {
            for (Article article : SaxXmlParser.parse(feed.getFeedRssLink())) {
                ids.add(articleDao.insertArticle(new Article(
                        article.getArticleTitle(),
                        article.getArticleLink(),
                        article.getArticleDescription(),
                        article.getArticlePubDate(),
                        article.getArticleThumbnailUrl(),
                        feed.getWebsiteName(),
                        feed.getCategoryName(),
                        false)));
            }
        }

        showToast(ids.size() + " new articles");
        ids.clear();
        stopForeground(true);
        stopSelf();
    }

    protected void showToast(final String msg) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                // run this code in the main thread
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Notification createNotification() {

        Intent notificationIntent = new Intent(getApplicationContext(), MainActivity.class);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        } else {
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }

        PendingIntent notificationPendingIntent = PendingIntent.getActivity(getApplicationContext(),
                1, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationManager notificationManager = (NotificationManager) getApplicationContext()
                .getSystemService(Context.NOTIFICATION_SERVICE);
        assert notificationManager != null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && notificationManager.getNotificationChannel(CHANNEL_ID) == null) {
            NotificationChannel notificationChannel = (new NotificationChannel(CHANNEL_ID, "Egypt Latest News",
                    NotificationManager.IMPORTANCE_DEFAULT));
            notificationChannel.enableVibration(true);
            notificationChannel.enableLights(true);
            notificationChannel.setDescription("Egypt Latest News Notification Channel");
            notificationChannel.setLightColor(Color.WHITE);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        NotificationCompat.Builder builder;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID);
        } else {
            builder = new NotificationCompat.Builder(getApplicationContext());
        }
        builder.setContentTitle("Getting latest news")
                .setContentText("Loading...")
                .setSmallIcon(R.mipmap.logo)
                .setContentIntent(notificationPendingIntent)
                .setTicker("Sync in progress")
                .setOngoing(true)
//                .setAutoCancel(true)
                .setCategory(NotificationCompat.CATEGORY_SERVICE);
        return builder.build();
    }
}
