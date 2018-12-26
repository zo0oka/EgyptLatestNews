package com.zookanews.egyptlatestnews.UI;

import android.app.SearchManager;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
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

import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.zookanews.egyptlatestnews.Helpers.Constants.ADMOB_APP_ID;

public class CategoryArticlesActivity extends AppCompatActivity {

    private ArticlesAdapter articlesAdapter;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_articles);

        String categoryName = getIntent().getExtras().getString("category_name");

        MobileAds.initialize(this, ADMOB_APP_ID);
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                // Load the next interstitial.
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }

        });

        ArticleViewModel articleViewModel = ViewModelProviders.of(this).get(ArticleViewModel.class);
        CategoryViewModel categoryViewModel = ViewModelProviders.of(this).get(CategoryViewModel.class);
        RecyclerView recyclerView = findViewById(R.id.category_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
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
        AdView mAdView = findViewById(R.id.category_articles_activity_adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
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
    public void onBackPressed() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            Log.d("TAG", "The interstitial wasn't loaded yet.");
        }
        super.onBackPressed();
    }
}
