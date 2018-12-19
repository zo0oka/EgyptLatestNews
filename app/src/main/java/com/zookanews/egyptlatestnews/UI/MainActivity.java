package com.zookanews.egyptlatestnews.UI;

import android.app.NotificationManager;
import android.app.SearchManager;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.zookanews.egyptlatestnews.Parser.SaxXmlParser;
import com.zookanews.egyptlatestnews.R;
import com.zookanews.egyptlatestnews.RoomDB.DB.FeedRoomDatabase;
import com.zookanews.egyptlatestnews.RoomDB.Entities.Article;
import com.zookanews.egyptlatestnews.RoomDB.Entities.Feed;
import com.zookanews.egyptlatestnews.RoomDB.ViewModels.ArticleViewModel;
import com.zookanews.egyptlatestnews.RoomDB.ViewModels.FeedViewModel;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String ADMOB_APP_ID = "ca-app-pub-4040319527918836~7183078616";
    private List<Article> articles;
    private ArticleViewModel articleViewModel;
    private SwipeRefreshLayout swipeRefreshLayout;
    private FeedViewModel feedViewModel;
    private AdView mAdView;
    private ProgressBar progressBar;
    private ArticlesAdapter articlesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        articleViewModel = ViewModelProviders.of(this).get(ArticleViewModel.class);
        feedViewModel = ViewModelProviders.of(this).get(FeedViewModel.class);

        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshArticles();
            }
        });

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        articlesAdapter = new ArticlesAdapter(this);
        recyclerView.setAdapter(articlesAdapter);

        articleViewModel.getAllArticles().observe(this, new Observer<List<Article>>() {
            @Override
            public void onChanged(@Nullable List<Article> articles) {
                articlesAdapter.setArticles(articles);
            }
        });

        MobileAds.initialize(this, ADMOB_APP_ID);
        mAdView = findViewById(R.id.main_activity_adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    private void refreshArticles() {
        new refreshArticlesAsyncTask(articleViewModel, feedViewModel).execute();
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_hide_read) {

        } else if (id == R.id.action_mark_all_read) {

        } else if (id == R.id.action_delete_all) {

        } else if (id == R.id.action_delete_all_read) {

        } else if (id == R.id.action_sort_by) {

        } else if (id == R.id.action_switch_layout) {

        } else if (id == R.id.action_sync) {
            refreshArticles();
        }

        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_latest_news) {
        } else if (id == R.id.nav_politics) {
        } else if (id == R.id.nav_accidents) {
        } else if (id == R.id.nav_finance) {
        } else if (id == R.id.nav_sports) {
        } else if (id == R.id.nav_woman) {
        } else if (id == R.id.nav_arts) {
        } else if (id == R.id.nav_technology) {
        } else if (id == R.id.nav_videos) {
        } else if (id == R.id.nav_automotive) {
        } else if (id == R.id.nav_investigations) {
        } else if (id == R.id.nav_culture) {
        } else if (id == R.id.nav_travel) {
        } else if (id == R.id.nav_health) {
        } else if (id == R.id.nav_almasry_alyoum) {
        } else if (id == R.id.nav_alwatan) {
        } else if (id == R.id.nav_aldostour) {
        } else if (id == R.id.nav_akhbarak) {
        } else if (id == R.id.nav_alwafd) {
        } else if (id == R.id.nav_bbc_arabic) {
        } else if (id == R.id.nav_alfagr) {
        } else if (id == R.id.nav_rose_alyousef) {
        } else if (id == R.id.nav_akhbar_elhawadeth) {
        } else if (id == R.id.nav_sada_elbalad) {
        } else if (id == R.id.nav_bawabet_veto) {
        } else if (id == R.id.nav_almogaz) {
        } else if (id == R.id.nav_app_rate) {
        } else if (id == R.id.nav_add_source) {
        } else if (id == R.id.nav_sync_news) {
        } else if (id == R.id.nav_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
        } else if (id == R.id.nav_privacy_policy) {
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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

    private static class refreshArticlesAsyncTask extends AsyncTask<Void, Void, Void> {
        private ArticleViewModel asyncTaskArticleViewModel;
        private FeedViewModel asyncTaskFeedViewModel;

        refreshArticlesAsyncTask(ArticleViewModel articleViewModel, FeedViewModel feedViewModel) {
            asyncTaskArticleViewModel = articleViewModel;
            asyncTaskFeedViewModel = feedViewModel;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                for (Feed feed : asyncTaskFeedViewModel.getAllFeeds()) {
                    for (Article article : SaxXmlParser.parse(feed.getFeedRssLink())) {
                        asyncTaskArticleViewModel.insertArticle(new Article(
                                article.getArticleTitle(),
                                article.getArticleLink(),
                                article.getArticleDescription(),
                                article.getArticlePubDate(),
                                article.getArticleThumbnailUrl(),
                                feed.getWebsiteName(),
                                feed.getCategoryName(),
                                false));
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
