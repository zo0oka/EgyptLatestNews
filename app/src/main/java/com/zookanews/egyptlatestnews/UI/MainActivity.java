package com.zookanews.egyptlatestnews.UI;

import android.app.NotificationManager;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.navigation.NavigationView;
import com.zookanews.egyptlatestnews.R;
import com.zookanews.egyptlatestnews.RoomDB.DB.FeedRoomDatabase;
import com.zookanews.egyptlatestnews.RoomDB.Entities.Article;
import com.zookanews.egyptlatestnews.RoomDB.ViewModels.ArticleViewModel;
import com.zookanews.egyptlatestnews.UpdateService.DbUpdateService;
import com.zookanews.egyptlatestnews.WorkManager.DBSyncWorker;
import com.zookanews.egyptlatestnews.WorkManager.DeleteReadArticlesWorker;
import com.zookanews.egyptlatestnews.WorkManager.DeleteUnreadArticlesWorker;

import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
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
import static com.zookanews.egyptlatestnews.Helpers.Constants.categoryMenuIds;
import static com.zookanews.egyptlatestnews.Helpers.Constants.categoryNames;
import static com.zookanews.egyptlatestnews.Helpers.Constants.websiteMenuIds;
import static com.zookanews.egyptlatestnews.Helpers.Constants.websiteNames;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    private SwipeRefreshLayout swipeRefreshLayout;
    private ArticleViewModel articleViewModel;
    private AdView mAdView;
    private ArticlesAdapter articlesAdapter;
    private Boolean backgroundSync;
    private String syncFrequency;
    private Boolean syncOnStartup;
    private Boolean wifiForDownload;
    private Boolean keepUnread;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTheme(R.style.AppTheme);
        initializeViews();
        initializeViewModels();
        setupRecyclerView();
        loadAd();

        articleViewModel.getAllArticles().observe(this, new Observer<PagedList<Article>>() {
            @Override
            public void onChanged(@Nullable PagedList<Article> articles) {
                articlesAdapter.submitList(articles);
            }
        });

        setCategoryCounters();
        setWebsiteCounters();

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert notificationManager != null;
        notificationManager.cancelAll();

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        getSharedPrefValues(sharedPreferences);
        registerSyncOnStartupWorker(syncOnStartup, wifiForDownload);
        registerDBSyncWorker(backgroundSync, syncFrequency, wifiForDownload);
        registerDeleteReadArticles();
        registerDeleteUnreadArticlesWorker(keepUnread);

    }

    private void setWebsiteCounters() {
        for (int i = 0; i < 12; i++) {
            getWebsiteArticlesCount(websiteNames[i], websiteMenuIds[i]);
        }
    }

    private void getWebsiteArticlesCount(String websiteName, final int menuId) {
        articleViewModel.getCountOfWebsiteUnreadArticles(websiteName).observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer integer) {
                TextView counter = (TextView) navigationView.getMenu().findItem(menuId).getActionView();
                counter.setText(String.valueOf(integer));
                counter.setGravity(Gravity.CENTER_VERTICAL);
            }
        });
    }

    private void setCategoryCounters() {
        for (int i = 0; i < 14; i++) {
            getCategoryArticlesCount(categoryNames[i], categoryMenuIds[i]);
        }
    }

    private void getCategoryArticlesCount(String categoryName, final int menuId) {
        articleViewModel.getCountOfCategoryUnreadArticles(categoryName).observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer integer) {
                TextView counter = (TextView) navigationView.getMenu().findItem(menuId).getActionView();
                counter.setText(String.valueOf(integer));
                counter.setGravity(Gravity.CENTER_VERTICAL);
            }
        });
    }

    private void getSharedPrefValues(SharedPreferences sharedPreferences) {
        backgroundSync = sharedPreferences.getBoolean(AUTOMATIC_BACKGROUND_SYNC, true);
        syncFrequency = sharedPreferences.getString(SYNC_FREQUENCY, "15");
        syncOnStartup = sharedPreferences.getBoolean(SYNC_ON_STARTUP, true);
        wifiForDownload = sharedPreferences.getBoolean(WIFI_ONLY_FOR_DOWNLOAD, true);
        keepUnread = sharedPreferences.getBoolean(KEEP_UNREAD_ARTICLES, true);
    }

    private void setupRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        articlesAdapter = new ArticlesAdapter(this, articleViewModel);
        recyclerView.setAdapter(articlesAdapter);
    }

    private void initializeViewModels() {
        articleViewModel = ViewModelProviders.of(this).get(ArticleViewModel.class);
    }

    private void initializeViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);

        toggle.syncState();
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout_main);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.secondaryColor));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                startSyncService();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        navigationView = findViewById(R.id.nav_view);
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

    private void registerDeleteReadArticles() {
        OneTimeWorkRequest.Builder deleteReadWorker = new OneTimeWorkRequest.Builder(DeleteReadArticlesWorker.class);
        WorkManager.getInstance().enqueue(deleteReadWorker.build());
    }

    private void registerDeleteUnreadArticlesWorker(Boolean keepUnread) {
        if (!keepUnread) {
            OneTimeWorkRequest.Builder deleteUnreadWorker = new OneTimeWorkRequest.Builder(DeleteUnreadArticlesWorker.class);
            WorkManager.getInstance().enqueue(deleteUnreadWorker.build());
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

    private void startSyncService() {
        Intent serviceIntent = new Intent(this, DbUpdateService.class);
        serviceIntent.setAction("sync_DB");
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent);
        } else {
            startService(serviceIntent);
        }
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
        getMenuInflater().inflate(R.menu.main, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        ComponentName componentName = new ComponentName(this, SearchResultsActivity.class);
        assert searchManager != null;
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName));
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        final int id = item.getItemId();

        if (id == R.id.nav_latest_news) {
            articleViewModel.getAllArticles().observe(this, new Observer<PagedList<Article>>() {
                @Override
                public void onChanged(@Nullable PagedList<Article> articles) {
                    articlesAdapter.submitList(articles);
                }
            });
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
            getWebsiteArticles("akhbar_alhawadeth");
        } else if (id == R.id.nav_sada_elbalad) {
            getWebsiteArticles("sada_albalad");
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
            Intent privacyIntent = new Intent(this, PrivacyPolicyActivity.class);
            startActivity(privacyIntent);
        } else if (id == R.id.nav_favorites) {
            getFavoriteArticles();
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void getFavoriteArticles() {
        Intent favoritesIntent = new Intent(MainActivity.this, FavoriteArticlesActivity.class);
        startActivity(favoritesIntent);
    }

    private void getWebsiteArticles(String websiteName) {
        Intent websiteIntent = new Intent(MainActivity.this, WebsiteArticlesActivity.class);
        websiteIntent.putExtra("website_name", websiteName);
        startActivity(websiteIntent);
    }

    private void getCategoryArticles(String categoryName) {
        Intent categoryIntent = new Intent(MainActivity.this, CategoryArticlesActivity.class);
        categoryIntent.putExtra("category_name", categoryName);
        startActivity(categoryIntent);
    }

    @Override
    protected void onDestroy() {
        mAdView.destroy();
        FeedRoomDatabase.destroyInstance();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        mAdView.pause();
        super.onPause();
    }

    @Override
    protected void onRestart() {
        mAdView.resume();
        super.onRestart();

    }
}
