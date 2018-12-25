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
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.preference.PreferenceManager;

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

import static com.zookanews.egyptlatestnews.Helpers.Constants.ARTICLE_NOTIFICATION_GROUP_KEY;
import static com.zookanews.egyptlatestnews.Helpers.Constants.LIGHT;
import static com.zookanews.egyptlatestnews.Helpers.Constants.NEW_ARTICLE_NOTIFICATION;
import static com.zookanews.egyptlatestnews.Helpers.Constants.NEW_ARTICLE_NOTIFICATION_ID;
import static com.zookanews.egyptlatestnews.Helpers.Constants.NOTIFICATION_CHANNEL_ID;
import static com.zookanews.egyptlatestnews.Helpers.Constants.NOTIFICATION_SUMMARY_ID;
import static com.zookanews.egyptlatestnews.Helpers.Constants.VIBRATE;

public class DBSyncWorker extends Worker {

    private SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    private ArticleDao articleDao = FeedRoomDatabase.getDatabase(getApplicationContext()).articleDao();
    private FeedDao feedDao = FeedRoomDatabase.getDatabase(getApplicationContext()).feedDao();
    private Boolean notifications = sharedPreferences.getBoolean(NEW_ARTICLE_NOTIFICATION, true);
    private Boolean light = sharedPreferences.getBoolean(LIGHT, true);
    private Boolean vibrate = sharedPreferences.getBoolean(VIBRATE, true);
    private NotificationManager notificationManager = (NotificationManager) getApplicationContext()
            .getSystemService(Context.NOTIFICATION_SERVICE);
    private NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());

    public DBSyncWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        createNotificationChannel(vibrate, light);
        updateDB(notifications);
        return Result.success();
    }

    private void createNotificationChannel(Boolean vibrate, Boolean light) {
        assert notificationManager != null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O &&
                notificationManager.getNotificationChannel(NOTIFICATION_CHANNEL_ID) == null) {
            NotificationChannel notificationChannel = (new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Egypt Latest News",
                    NotificationManager.IMPORTANCE_DEFAULT));
            notificationChannel.enableVibration(vibrate);
            notificationChannel.enableLights(light);
            notificationChannel.setDescription("Egypt Latest News Notification Channel");
            notificationChannel.setLightColor(Color.WHITE);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    private void updateDB(Boolean notifications) {
        int noOfNotifications = 0;
        List<Long> ids = new ArrayList<>();
        for (Feed feed : feedDao.getAllFeeds()) {
            List<Article> articles = SaxXmlParser.parse(feed.getFeedRssLink());
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
                            false,
                            false));
                    if (id != -1) {
                        ids.add(id);
                        if (notifications) {
                            Intent notificationIntent = new Intent(getApplicationContext(), MainActivity.class);
                            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            PendingIntent notificationPendingIntent = PendingIntent.getActivity(getApplicationContext(),
                                    101, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                            noOfNotifications++;
                            Notification newArticleNotification =
                                    new NotificationCompat.Builder(getApplicationContext(), NOTIFICATION_CHANNEL_ID)
                                            .setSmallIcon(R.mipmap.ic_launcher)
                                            .setContentTitle("Egypt Latest News")
                                            .setContentText(article.getArticleTitle())
                                            .setContentIntent(notificationPendingIntent)
                                            .setGroup(ARTICLE_NOTIFICATION_GROUP_KEY)
                                            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                            .build();
                            notificationManagerCompat.notify(NEW_ARTICLE_NOTIFICATION_ID, newArticleNotification);
                            if (noOfNotifications >= 5) {
                                Notification summaryNotification =
                                        new NotificationCompat.Builder(getApplicationContext(), NOTIFICATION_CHANNEL_ID)
                                                .setSmallIcon(R.mipmap.ic_launcher)
                                                .setContentTitle("Egypt Latest News")
                                                .setContentText(noOfNotifications + " new article(s)")
                                                .setStyle(new NotificationCompat.InboxStyle()
                                                        .addLine(articleDao.getArticleById(ids.get(ids.size() - 1).intValue()).getArticleTitle())
                                                        .addLine(articleDao.getArticleById(ids.get(ids.size() - 2).intValue()).getArticleTitle())
                                                        .addLine(articleDao.getArticleById(ids.get(ids.size() - 3).intValue()).getArticleTitle())
                                                        .addLine(articleDao.getArticleById(ids.get(ids.size() - 4).intValue()).getArticleTitle())
                                                        .addLine(articleDao.getArticleById(ids.get(ids.size() - 5).intValue()).getArticleTitle())
                                                        .setBigContentTitle(ids.size() + " new article(s)")
                                                        .setSummaryText("+" + (noOfNotifications - 5) + " more article(s)"))
                                                .setNumber(noOfNotifications)
                                                .setContentIntent(notificationPendingIntent)
//                                    .setCategory(Notification.CATEGORY_EVENT)
                                                .setGroupSummary(true)
                                                .setGroup(ARTICLE_NOTIFICATION_GROUP_KEY)
                                                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                                .build();
                                notificationManagerCompat.notify(NOTIFICATION_SUMMARY_ID, summaryNotification);
                            }
                        }
                    }
                }
            }
        }
    }
}
