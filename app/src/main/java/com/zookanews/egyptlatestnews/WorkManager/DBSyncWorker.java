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
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;

import com.zookanews.egyptlatestnews.Parser.SaxXmlParser;
import com.zookanews.egyptlatestnews.R;
import com.zookanews.egyptlatestnews.RoomDB.DAO.ArticleDao;
import com.zookanews.egyptlatestnews.RoomDB.DAO.FeedDao;
import com.zookanews.egyptlatestnews.RoomDB.DB.FeedRoomDatabase;
import com.zookanews.egyptlatestnews.RoomDB.Entities.Article;
import com.zookanews.egyptlatestnews.RoomDB.Entities.Feed;
import com.zookanews.egyptlatestnews.UI.MainActivity;

import java.util.List;

import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class DBSyncWorker extends Worker {

    private static final int CHANNEL_ID = 1;
    private static final String FIRST_RUN = "is_first_run";
    private SharedPreferences sharedPreferences;
    private List<Article> articles;


    public DBSyncWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Boolean isNotification = sharedPreferences.getBoolean(String.valueOf(R.string.pref_key_new_article_notifications), true);

        updateDB();
        if (isNotification) {
            sendNotification();
        }
        return Result.success();
    }

    private void sendNotification() {
        Boolean vibrate = sharedPreferences.getBoolean(String.valueOf(R.string.pref_key_vibrate), true);
        Boolean light = sharedPreferences.getBoolean(String.valueOf(R.string.pref_key_light), true);
        Intent notificationIntent = new Intent(getApplicationContext(), MainActivity.class);
        PendingIntent notificationPendingIntent = PendingIntent.getActivity(getApplicationContext(),
                1, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationManager notificationManager = (NotificationManager) getApplicationContext()
                .getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel notificationChannel = (new NotificationChannel("egyptlatestnews", "Egypt Latest News",
                    NotificationManager.IMPORTANCE_DEFAULT));
            notificationChannel.enableVibration(true);
            notificationChannel.enableLights(true);
            notificationChannel.setDescription("Egypt Latest News Notification Channel");
            notificationChannel.setLightColor(Color.WHITE);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), "egyptlatestnews");
        notificationBuilder.setLights(Color.WHITE, 500, 2000);
        notificationBuilder.setDefaults(NotificationCompat.DEFAULT_ALL);
        notificationBuilder.setContentTitle("New Articles");
        notificationBuilder.setContentText(articles.size() + " new articles to read!");
        notificationBuilder.setSmallIcon(R.mipmap.ic_launcher);
        notificationBuilder.setContentIntent(notificationPendingIntent);
        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        notificationBuilder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
        notificationBuilder.setStyle(new NotificationCompat.InboxStyle()
                .addLine(articles.get(articles.size() - 1).getArticleTitle())
                .addLine(articles.get(articles.size() - 2).getArticleTitle())
                .addLine(articles.get(articles.size() - 3).getArticleTitle())
                .addLine(articles.get(articles.size() - 4).getArticleTitle()));
        notificationBuilder.setTicker("New " + articles.size() + " articles to read!");
        Notification notification = notificationBuilder.build();
        notification.flags |= Notification.FLAG_SHOW_LIGHTS;


        if (notificationManager != null) {
            notificationManager.notify(CHANNEL_ID, notification);
        }
    }

    private void updateDB() {
        ArticleDao articleDao = FeedRoomDatabase.getDatabase(getApplicationContext()).articleDao();
        FeedDao feedDao = FeedRoomDatabase.getDatabase(getApplicationContext()).feedDao();
        List<Feed> feeds = feedDao.getAllFeeds();
        if (feeds != null) {
            for (Feed feed : feeds) {
                articles = SaxXmlParser.parse(feed.getFeedRssLink());
                for (Article article : articles) {
                    articleDao.insertArticle(new Article(
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
}
