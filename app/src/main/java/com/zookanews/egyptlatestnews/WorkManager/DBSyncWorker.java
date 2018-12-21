package com.zookanews.egyptlatestnews.WorkManager;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;

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

import androidx.work.Worker;
import androidx.work.WorkerParameters;

import static com.zookanews.egyptlatestnews.Helpers.Constants.LIGHT;
import static com.zookanews.egyptlatestnews.Helpers.Constants.NEW_ARTICLE_NOTIFICATION;
import static com.zookanews.egyptlatestnews.Helpers.Constants.VIBRATE;

public class DBSyncWorker extends Worker {

    private static final int CHANNEL_ID = 1;
    private List<Article> articles = new ArrayList<>();
    private SharedPreferences sharedPreferences;

    public DBSyncWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Boolean notifications = sharedPreferences.getBoolean(NEW_ARTICLE_NOTIFICATION, true);
        updateDB();
        if (notifications) {
            sendNotification();
        }
        return Result.success();
    }

    private void sendNotification() {
        Boolean light = sharedPreferences.getBoolean(LIGHT, true);
        Boolean vibrate = sharedPreferences.getBoolean(VIBRATE, true);

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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O &&
                notificationManager.getNotificationChannel("egyptlatestnews") == null) {
            NotificationChannel notificationChannel = (new NotificationChannel("egyptlatestnews", "Egypt Latest News",
                    NotificationManager.IMPORTANCE_DEFAULT));
            notificationChannel.enableVibration(vibrate);
            notificationChannel.enableLights(light);
            notificationChannel.setDescription("Egypt Latest News Notification Channel");
            notificationChannel.setLightColor(Color.WHITE);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        NotificationCompat.Builder notificationBuilder;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), "egyptlatestnews");
        } else {
            notificationBuilder = new NotificationCompat.Builder(getApplicationContext());
        }
        notificationBuilder.setLights(Color.WHITE, 500, 2000);
        notificationBuilder.setDefaults(NotificationCompat.DEFAULT_ALL);
        notificationBuilder.setContentTitle("New Articles");
        notificationBuilder.setContentText(articles.size() + " new articles to read!");
        notificationBuilder.setSmallIcon(R.mipmap.ic_launcher);
        notificationBuilder.setContentIntent(notificationPendingIntent);
        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        notificationBuilder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
        assert articles != null;
        notificationBuilder.setStyle(new NotificationCompat.InboxStyle()
                .addLine(articles.get(articles.size() - 1).getArticleTitle())
                .addLine(articles.get(articles.size() - 2).getArticleTitle())
                .addLine(articles.get(articles.size() - 3).getArticleTitle())
                .addLine(articles.get(articles.size() - 4).getArticleTitle()));
        notificationBuilder.setTicker("New " + articles.size() + " articles to read!");
        Notification notification = notificationBuilder.build();
        notification.flags |= Notification.FLAG_SHOW_LIGHTS;
        notificationManager.notify(CHANNEL_ID, notification);
    }

    private void updateDB() {
        ArticleDao articleDao = FeedRoomDatabase.getDatabase(getApplicationContext()).articleDao();
        FeedDao feedDao = FeedRoomDatabase.getDatabase(getApplicationContext()).feedDao();
        List<Feed> feeds = feedDao.getAllFeeds();
        if (feeds != null) {
            for (Feed feed : feeds) {
                List<Article> feedArticles = SaxXmlParser.parse(feed.getFeedRssLink());
                Log.d("DBSyncWorker", String.valueOf(feeds.size()));
                if (feedArticles != null) {
                    for (Article article : feedArticles) {
                        articles.add(new Article(
                                article.getArticleTitle(),
                                article.getArticleLink(),
                                article.getArticleDescription(),
                                article.getArticlePubDate(),
                                article.getArticleThumbnailUrl(),
                                feed.getWebsiteName(),
                                feed.getCategoryName(),
                                false
                        ));
                    }
                }
            }
        }
        for (Article article : articles) {
            articleDao.insertArticle(article);
        }
    }
}
