package com.zookanews.egyptlatestnews.UI;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

    private static final String articleId = "articleId";
    private Article article;
    private int receivedArticleId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);

        if (savedInstanceState != null) {
            receivedArticleId = savedInstanceState.getInt("article_id");
        } else {
            receivedArticleId = getIntent().getExtras().getInt(articleId);
        }
        ArticleViewModel articleViewModel = ViewModelProviders.of(this).get(ArticleViewModel.class);
        try {
            article = articleViewModel.getArticleById(receivedArticleId);
            articleViewModel.updateReadStatus(receivedArticleId, true);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
                intent.putExtra("articleLink", article.getArticleLink());
                startActivity(intent);
            }
        });

        loadAd();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("article_id", receivedArticleId);
    }



    private void loadAd() {
        MobileAds.initialize(this, Constants.ADMOB_APP_ID);
        AdView mAdView = findViewById(R.id.article_detail_adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }
}
