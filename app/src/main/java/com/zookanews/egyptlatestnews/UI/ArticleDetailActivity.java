package com.zookanews.egyptlatestnews.UI;

import android.app.SearchManager;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.zookanews.egyptlatestnews.Helpers.Constants;
import com.zookanews.egyptlatestnews.R;
import com.zookanews.egyptlatestnews.RoomDB.Entities.Article;
import com.zookanews.egyptlatestnews.RoomDB.ViewModels.ArticleViewModel;

import java.util.concurrent.ExecutionException;

public class ArticleDetailActivity extends AppCompatActivity {

    private Article article;
    private ArticleViewModel articleViewModel;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        int receivedArticleId = sharedPreferences.getInt("article_id", 0);

        articleViewModel = ViewModelProviders.of(this).get(ArticleViewModel.class);
        try {
            article = articleViewModel.getArticleById(receivedArticleId);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        setTitle(article.getArticleTitle());
        TextView articleTitle = findViewById(R.id.article_title_text_view);
        assert article != null;
        articleTitle.setText(article.getArticleTitle());
        WebView articleDescription = findViewById(R.id.article_detail_webView);
        articleDescription.loadData("<html dir=\"rtl\" lang=\"\"><body>" + article.getArticleDescription() + "</body></html>", "text/html; charset=utf-8", "UTF-8");
        Button articleViewButton = findViewById(R.id.article_view_website_button);
        articleViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ArticleWebViewActivity.class);
                intent.putExtra("article_id", article.getArticleId());
                startActivity(intent);
            }
        });

        FloatingActionButton floatingActionButton = findViewById(R.id.article_detail_fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, article.getArticleTitle());
                sharingIntent.putExtra(Intent.EXTRA_TEXT, article.getArticleDescription());
                sharingIntent.putExtra(Intent.EXTRA_TITLE, article.getArticleTitle());
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        });

        loadAd();

    }

    private void loadAd() {
        MobileAds.initialize(this, Constants.ADMOB_APP_ID);
        mAdView = findViewById(R.id.article_detail_adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        menu.findItem(R.id.action_favorite).setVisible(true);
        if (article.getIsFavorite()) {
            menu.findItem(R.id.action_favorite).setIcon(R.drawable.ic_action_favorite_checked);
        }
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        assert searchManager != null;
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_favorite) {
            if (article.getIsFavorite()) {
                articleViewModel.updateFavoriteStatus(article.getArticleId(), false);
                item.setIcon(R.drawable.ic_action_favorite_unchecked);

            } else if (!article.getIsFavorite()) {
                articleViewModel.updateFavoriteStatus(article.getArticleId(), true);
                item.setIcon(R.drawable.ic_action_favorite_checked);
            }
        }
        return true;
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
