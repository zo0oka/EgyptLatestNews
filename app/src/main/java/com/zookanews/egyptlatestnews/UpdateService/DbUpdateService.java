package com.zookanews.egyptlatestnews.UpdateService;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;
import android.support.v7.preference.PreferenceManager;
import android.widget.Toast;

import com.zookanews.egyptlatestnews.Parser.SaxXmlParser;
import com.zookanews.egyptlatestnews.R;
import com.zookanews.egyptlatestnews.RoomDB.DAO.ArticleDao;
import com.zookanews.egyptlatestnews.RoomDB.DAO.FeedDao;
import com.zookanews.egyptlatestnews.RoomDB.DB.FeedRoomDatabase;
import com.zookanews.egyptlatestnews.RoomDB.Entities.Article;
import com.zookanews.egyptlatestnews.RoomDB.Entities.Feed;
import com.zookanews.egyptlatestnews.UI.MainActivity;

import java.util.ArrayList;
import java.util.List;

import static com.zookanews.egyptlatestnews.Helpers.Constants.LIGHT;
import static com.zookanews.egyptlatestnews.Helpers.Constants.NOTIFICATION_CHANNEL_ID;
import static com.zookanews.egyptlatestnews.Helpers.Constants.VIBRATE;

public class DbUpdateService extends IntentService {

    public DbUpdateService() {
        super("dbsync");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        showToast("Sync started");
        startForeground(1, createNotification());
        List<Article> articles;
        List<Long> ids = new ArrayList<>();
        ArticleDao articleDao = FeedRoomDatabase.getDatabase(getApplicationContext()).articleDao();
        FeedDao feedDao = FeedRoomDatabase.getDatabase(getApplicationContext()).feedDao();
        List<Feed> feeds = new ArrayList<>(feedDao.getAllFeeds());
        for (Feed feed : feeds) {
//                articles.clear();
            articles = SaxXmlParser.parse(feed.getFeedRssLink());
            if (articles != null) {
                for (Article article : articles) {
                    long id = articleDao.insertArticle(new Article(
                            article.getArticleTitle(),
                            article.getArticleLink(),
                            article.getArticleDescription(),
                            article.getArticlePubDate(),
                            article.getArticleThumbnailUrl(),
                            feed.getWebsiteName(),
                            feed.getCategoryName(),
                            false));
                    if (id != -1) {
                        ids.add(id);
                    }
                }
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
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Boolean light = sharedPreferences.getBoolean(LIGHT, true);
        Boolean vibrate = sharedPreferences.getBoolean(VIBRATE, true);

        Intent notificationIntent = new Intent(getApplicationContext(), MainActivity.class);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        } else {
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }

        PendingIntent notificationPendingIntent = PendingIntent.getActivity(getApplicationContext(),
                102, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationManager notificationManager = (NotificationManager) getApplicationContext()
                .getSystemService(Context.NOTIFICATION_SERVICE);
        assert notificationManager != null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && notificationManager.getNotificationChannel(NOTIFICATION_CHANNEL_ID) == null) {
            NotificationChannel notificationChannel = (new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Egypt Latest News",
                    NotificationManager.IMPORTANCE_DEFAULT));
            notificationChannel.enableVibration(vibrate);
            notificationChannel.enableLights(light);
            notificationChannel.setDescription("Egypt Latest News Notification Channel");
            notificationChannel.setLightColor(Color.WHITE);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        NotificationCompat.Builder builder;
        builder = new NotificationCompat.Builder(getApplicationContext(), NOTIFICATION_CHANNEL_ID);
        builder.setContentTitle("Getting latest news")
                .setContentText("Loading...")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(notificationPendingIntent)
                .setTicker("Sync in progress")
                .setOngoing(true)
//                .setAutoCancel(true)
                .setCategory(NotificationCompat.CATEGORY_SERVICE);
        return builder.build();
    }
}
