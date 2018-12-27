package com.zookanews.egyptlatestnews.Helpers;

import com.zookanews.egyptlatestnews.R;

public class Constants {
    public static final String AUTOMATIC_BACKGROUND_SYNC = "pref_key_automatic_background_sync";
    public static final String SYNC_FREQUENCY = "pref_key_sync_frequency";
    public static final String SYNC_ON_STARTUP = "pref_key_sync_on_startup";
    public static final String WIFI_ONLY_FOR_DOWNLOAD = "pref_key_wifi_only_for_download";
    public static final String KEEP_UNREAD_ARTICLES = "pref_key_keep_unread_articles";
    public static final String AUTO_CLEANUP_READ = "pref_key_auto_cleanup_read";
    public static final String AUTO_CLEANUP_UNREAD = "pref_key_auto_cleanup_unread";
    public static final String NEW_ARTICLE_NOTIFICATION = "pref_key_new_article_notifications";
    public static final String VIBRATE = "pref_key_vibrate";
    public static final String LIGHT = "pref_key_light";
    public static final String ADMOB_APP_ID = "ca-app-pub-4040319527918836~7183078616";
    public static final String NOTIFICATION_CHANNEL_ID = "com.zookanews.egyptlatestnews.NOTIFICATION_CHANNEL";
    public static final int NEW_ARTICLE_NOTIFICATION_ID = 1;
    public static final String ARTICLE_NOTIFICATION_GROUP_KEY = "com.zookanews.egyptlatestnews.ARTICLES_NOTIFICATION";
    public static final int NOTIFICATION_SUMMARY_ID = 2;
    public static final String[] categoryNames = {"latest_news", "politics", "accidents", "finance", "sports", "woman",
            "arts", "technology", "videos", "automotive", "investigations", "culture", "travel", "health"};
    public static final String[] websiteNames = {"almasry_alyoum", "alwatan", "aldostour", "akhbarak", "alwafd", "bbc_arabic",
            "alfagr", "rose_alyousef", "akhbar_alhawadeth", "sada_albalad", "bawabet_veto", "almogaz"};
    public static final int[] categoryMenuIds = {R.id.nav_latest_news, R.id.nav_politics, R.id.nav_accidents, R.id.nav_finance,
            R.id.nav_sports, R.id.nav_woman, R.id.nav_arts, R.id.nav_technology, R.id.nav_videos, R.id.nav_automotive,
            R.id.nav_investigations, R.id.nav_culture, R.id.nav_travel, R.id.nav_health};
    public static final int[] websiteMenuIds = {R.id.nav_almasry_alyoum, R.id.nav_alwatan, R.id.nav_aldostour, R.id.nav_akhbarak,
            R.id.nav_alwafd, R.id.nav_bbc_arabic, R.id.nav_alfagr, R.id.nav_rose_alyousef, R.id.nav_akhbar_elhawadeth,
            R.id.nav_sada_elbalad, R.id.nav_bawabet_veto, R.id.nav_almogaz};
    public static final String ADMOB_INTERTITIALAD_UNIT_ID = "ca-app-pub-4040319527918836/4947968954";

}
