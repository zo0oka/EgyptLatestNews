package com.zookanews.egyptlatestnews.UI;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.SearchManager;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.zookanews.egyptlatestnews.Helpers.Constants;
import com.zookanews.egyptlatestnews.R;
import com.zookanews.egyptlatestnews.RoomDB.DB.FeedRoomDatabase;
import com.zookanews.egyptlatestnews.RoomDB.Entities.Article;
import com.zookanews.egyptlatestnews.RoomDB.ViewModels.ArticleViewModel;
import com.zookanews.egyptlatestnews.RoomDB.ViewModels.FeedViewModel;
import com.zookanews.egyptlatestnews.UpdateService.DbUpdateService;
import com.zookanews.egyptlatestnews.WorkManager.DBSyncWorker;
import com.zookanews.egyptlatestnews.WorkManager.DeleteReadArticlesWorker;
import com.zookanews.egyptlatestnews.WorkManager.DeleteUnreadArticlesWorker;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import static com.zookanews.egyptlatestnews.Helpers.Constants.ADMOB_APP_ID;
import static com.zookanews.egyptlatestnews.Helpers.Constants.AUTOMATIC_BACKGROUND_SYNC;
import static com.zookanews.egyptlatestnews.Helpers.Constants.KEEP_UNREAD_ARTICLES;
import static com.zookanews.egyptlatestnews.Helpers.Constants.SYNC_FREQUENCY;
import static com.zookanews.egyptlatestnews.Helpers.Constants.SYNC_ON_STARTUP;
import static com.zookanews.egyptlatestnews.Helpers.Constants.WIFI_ONLY_FOR_DOWNLOAD;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    private static final String TAG = MainActivity.class.getSimpleName();
    public SwipeRefreshLayout swipeRefreshLayout;
    private List<Article> articles;
    private ArticleViewModel articleViewModel;
    private FeedViewModel feedViewModel;
    private AdView mAdView;
    private ArticlesAdapter articlesAdapter;
    private SharedPreferences sharedPreferences;
    private Boolean backgroundSync;
    private String syncFrequency;
    private Boolean syncOnStartup;
    private Boolean wifiForDownload;
    private Boolean keepUnread;
    private String cleanupRead;
    private String cleanUnread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeViews();
        initializeViewModels();
        setupRecyclerView();
        articleViewModel.getAllArticles().observe(this, new Observer<List<Article>>() {
            @Override
            public void onChanged(@Nullable List<Article> articles) {
                articlesAdapter.setArticles(articles);
            }
        });

        loadAd();

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        getSharedPrefValues(sharedPreferences);
        registerSyncOnStartupWorker(syncOnStartup, wifiForDownload);
        registerDBSyncWorker(backgroundSync, syncFrequency, wifiForDownload);
//        registerDeleteReadArticles(cleanupRead);
//        registerDeleteUnreadArticlesWorker(keepUnread, cleanUnread);


    }

    private void getSharedPrefValues(SharedPreferences sharedPreferences) {
        backgroundSync = sharedPreferences.getBoolean(AUTOMATIC_BACKGROUND_SYNC, true);
        syncFrequency = sharedPreferences.getString(SYNC_FREQUENCY, "15");
        syncOnStartup = sharedPreferences.getBoolean(SYNC_ON_STARTUP, true);
        wifiForDownload = sharedPreferences.getBoolean(WIFI_ONLY_FOR_DOWNLOAD, true);
        keepUnread = sharedPreferences.getBoolean(KEEP_UNREAD_ARTICLES, true);
        cleanupRead = sharedPreferences.getString(Constants.AUTO_CLEANUP_READ, "2");
        cleanUnread = sharedPreferences.getString(Constants.AUTO_CLEANUP_UNREAD, "2");
    }

    private void setupRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        articlesAdapter = new ArticlesAdapter(this);
        recyclerView.setAdapter(articlesAdapter);
    }

    private void initializeViewModels() {
        articleViewModel = ViewModelProviders.of(this).get(ArticleViewModel.class);
        feedViewModel = ViewModelProviders.of(this).get(FeedViewModel.class);
    }

    private void initializeViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);

        toggle.syncState();
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                startSyncService();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

    }

    private void loadAd() {
        MobileAds.initialize(this, ADMOB_APP_ID);
        mAdView = findViewById(R.id.main_activity_adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    private void registerSyncOnStartupWorker(Boolean syncOnStartup, Boolean wifiForDownload) {
        if (syncOnStartup) {
            OneTimeWorkRequest.Builder syncDBWorker = new OneTimeWorkRequest.Builder(DBSyncWorker.class);
            Constraints.Builder constraintsBuilder = new Constraints.Builder();
            Constraints constraints;
            if (wifiForDownload) {
                constraints = constraintsBuilder.setRequiredNetworkType(NetworkType.UNMETERED).build();
            } else {
                constraints = constraintsBuilder.setRequiredNetworkType(NetworkType.CONNECTED).build();
            }
            syncDBWorker.setConstraints(constraints);
            WorkManager.getInstance().enqueue(syncDBWorker.build());
        }
    }

    private void registerDeleteReadArticles(String cleanupRead) {
        PeriodicWorkRequest.Builder deleteReadWorker = new PeriodicWorkRequest.Builder(DeleteReadArticlesWorker.class, Integer.valueOf(cleanupRead), TimeUnit.DAYS);
        WorkManager.getInstance().enqueueUniquePeriodicWork("DeleteUnread", ExistingPeriodicWorkPolicy.KEEP, deleteReadWorker.build());
    }

    private void registerDeleteUnreadArticlesWorker(Boolean keepUnread, String cleanUnread) {
        if (!keepUnread) {
            PeriodicWorkRequest.Builder deleteUnreadWorker = new PeriodicWorkRequest.Builder(DeleteUnreadArticlesWorker.class, Integer.valueOf(cleanUnread), TimeUnit.DAYS);
            WorkManager.getInstance().enqueueUniquePeriodicWork("DeleteUnread", ExistingPeriodicWorkPolicy.KEEP, deleteUnreadWorker.build());
        }
    }

    private void registerDBSyncWorker(Boolean backgroundSync, String syncFrequency, Boolean wifiForDownload) {
        if (backgroundSync) {
            PeriodicWorkRequest.Builder syncDBWorker = new PeriodicWorkRequest.Builder(DBSyncWorker.class, Integer.valueOf(syncFrequency), TimeUnit.MINUTES);
            Constraints.Builder constraintsBuilder = new Constraints.Builder();
            Constraints constraints;
            if (wifiForDownload) {
                constraints = constraintsBuilder.setRequiredNetworkType(NetworkType.UNMETERED).build();
            } else {
                constraints = constraintsBuilder.setRequiredNetworkType(NetworkType.CONNECTED).build();
            }
            syncDBWorker.setConstraints(constraints);
            WorkManager.getInstance().enqueueUniquePeriodicWork("DBSync", ExistingPeriodicWorkPolicy.KEEP, syncDBWorker.build());
        }
    }

    public void startSyncService() {
        Intent serviceIntent = new Intent(this, DbUpdateService.class);
        serviceIntent.setAction("sync_DB");
        startService(serviceIntent);
//        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
//            startForegroundService(serviceIntent);
//        } else {
//            startService(serviceIntent);
//        }
    }

    @Override
    public void onResume() {
        super.onResume();
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert notificationManager != null;
        notificationManager.cancelAll();
        mAdView.resume();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mAdView.resume();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        assert searchManager != null;
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(true);
        return true;
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_hide_read) {
            try {
                articlesAdapter.setArticles(articleViewModel.getUnreadArticles(false));
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else if (id == R.id.action_mark_all_read) {
            articleViewModel.setAllAsRead();
        } else if (id == R.id.action_delete_all) {
            articleViewModel.deleteAllArticles();
        } else if (id == R.id.action_delete_all_read) {
            articleViewModel.deleteReadArticles();
        }

        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_latest_news) {
            articlesAdapter.setArticles(articleViewModel.getAllArticles().getValue());
        } else if (id == R.id.nav_politics) {
            getCategoryArticles("politics");
        } else if (id == R.id.nav_accidents) {
            getCategoryArticles("accidents");
        } else if (id == R.id.nav_finance) {
            getCategoryArticles("finance");
        } else if (id == R.id.nav_sports) {
            getCategoryArticles("sports");
        } else if (id == R.id.nav_woman) {
            getCategoryArticles("woman");
        } else if (id == R.id.nav_arts) {
            getCategoryArticles("arts");
        } else if (id == R.id.nav_technology) {
            getCategoryArticles("technology");
        } else if (id == R.id.nav_videos) {
            getCategoryArticles("videos");
        } else if (id == R.id.nav_automotive) {
            getCategoryArticles("automotive");
        } else if (id == R.id.nav_investigations) {
            getCategoryArticles("investigations");
        } else if (id == R.id.nav_culture) {
            getCategoryArticles("culture");
        } else if (id == R.id.nav_travel) {
            getCategoryArticles("travel");
        } else if (id == R.id.nav_health) {
            getCategoryArticles("health");
        } else if (id == R.id.nav_almasry_alyoum) {
            getWebsiteArticles("almasry_alyoum");
        } else if (id == R.id.nav_alwatan) {
            getWebsiteArticles("alwatan");
        } else if (id == R.id.nav_aldostour) {
            getWebsiteArticles("aldostour");
        } else if (id == R.id.nav_akhbarak) {
            getWebsiteArticles("akhbarak");
        } else if (id == R.id.nav_alwafd) {
            getWebsiteArticles("alwafd");
        } else if (id == R.id.nav_bbc_arabic) {
            getWebsiteArticles("bbc_arabic");
        } else if (id == R.id.nav_alfagr) {
            getWebsiteArticles("alfagr");
        } else if (id == R.id.nav_rose_alyousef) {
            getWebsiteArticles("rose_alyousef");
        } else if (id == R.id.nav_akhbar_elhawadeth) {
            getWebsiteArticles("akhbar_elhawadeth");
        } else if (id == R.id.nav_sada_elbalad) {
            getWebsiteArticles("sada_elbalad");
        } else if (id == R.id.nav_bawabet_veto) {
            getWebsiteArticles("bawabet_veto");
        } else if (id == R.id.nav_almogaz) {
            getWebsiteArticles("almogaz");
        } else if (id == R.id.nav_sync_news) {
            startSyncService();
        } else if (id == R.id.nav_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
        } else if (id == R.id.nav_privacy_policy) {
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void getWebsiteArticles(String websiteName) {
        try {
            articlesAdapter.setArticles(articleViewModel.getWebsiteArticles(websiteName));
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    private void getCategoryArticles(String categoryName) {
        try {
            articlesAdapter.setArticles(articleViewModel.getCategoryArticles(categoryName));
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        FeedRoomDatabase.destroyInstance();
        mAdView.destroy();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        mAdView.pause();
        super.onPause();
    }
}
