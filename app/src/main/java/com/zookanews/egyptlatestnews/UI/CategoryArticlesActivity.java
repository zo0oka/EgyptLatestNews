package com.zookanews.egyptlatestnews.UI;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.zookanews.egyptlatestnews.Helpers.Constants;
import com.zookanews.egyptlatestnews.R;
import com.zookanews.egyptlatestnews.RoomDB.Entities.Article;
import com.zookanews.egyptlatestnews.RoomDB.Entities.Category;
import com.zookanews.egyptlatestnews.RoomDB.ViewModels.ArticleViewModel;
import com.zookanews.egyptlatestnews.RoomDB.ViewModels.CategoryViewModel;
import com.zookanews.egyptlatestnews.UpdateService.DbUpdateService;

import java.util.List;
import java.util.concurrent.ExecutionException;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import static com.zookanews.egyptlatestnews.Helpers.Constants.ADMOB_APP_ID;
import static com.zookanews.egyptlatestnews.Helpers.Constants.ADMOB_INTERTITIALAD_UNIT_ID;

public class CategoryArticlesActivity extends AppCompatActivity {

    private ArticlesAdapter articlesAdapter;
    private InterstitialAd mInterstitialAd;
    private AdView mAdView;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_articles);

        String categoryName = getIntent().getExtras().getString("category_name");

        MobileAds.initialize(this, ADMOB_APP_ID);
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(ADMOB_INTERTITIALAD_UNIT_ID);
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }

        });
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout_category);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.secondaryColor));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                startSyncService();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        ArticleViewModel articleViewModel = ViewModelProviders.of(this).get(ArticleViewModel.class);
        CategoryViewModel categoryViewModel = ViewModelProviders.of(this).get(CategoryViewModel.class);
        RecyclerView recyclerView = findViewById(R.id.category_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        articlesAdapter = new ArticlesAdapter(this, articleViewModel);
        recyclerView.setAdapter(articlesAdapter);

        articleViewModel.getCategoryArticles(categoryName).observe(this, new Observer<List<Article>>() {
            @Override
            public void onChanged(@Nullable List<Article> articles) {
                articlesAdapter.setArticles(articles);
            }
        });
        try {
            Category category = categoryViewModel.getCategoryByName(categoryName);
            setTitle(category.getCategoryTitle());
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        loadAd();

    }

    private void loadAd() {
        MobileAds.initialize(this, Constants.ADMOB_APP_ID);
        mAdView = findViewById(R.id.category_articles_activity_adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
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
    public void onBackPressed() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        mAdView.destroy();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        mAdView.pause();
        super.onPause();
    }

    @Override
    public void onResume() {
        mAdView.resume();
        super.onResume();
    }

    @Override
    protected void onRestart() {
        mAdView.resume();
        super.onRestart();

    }
}
