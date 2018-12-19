package com.zookanews.egyptlatestnews.UI;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.zookanews.egyptlatestnews.R;
import com.zookanews.egyptlatestnews.RoomDB.Entities.Article;
import com.zookanews.egyptlatestnews.RoomDB.ViewModels.ArticleViewModel;

import java.util.concurrent.ExecutionException;

public class ArticleDetailActivity extends AppCompatActivity {

    private static final String articleId = "articleId";
    private static final String ADMOB_APP_ID = "ca-app-pub-4040319527918836~7183078616";
    private Article article;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);

        int receivedArticleId = getIntent().getExtras().getInt(articleId);

        ArticleViewModel articleViewModel = ViewModelProviders.of(this).get(ArticleViewModel.class);
        try {
            article = articleViewModel.getArticleById(receivedArticleId);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        TextView articleTitle = findViewById(R.id.article_title_text_view);
        assert article != null;
        articleTitle.setText(article.getArticleTitle());
        TextView articleDescription = findViewById(R.id.article_description_text_view);
        articleDescription.setText(Html.fromHtml(article.getArticleDescription()));
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

    private void loadAd() {
        MobileAds.initialize(this, ADMOB_APP_ID);
        AdView mAdView = findViewById(R.id.article_detail_adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }
}
