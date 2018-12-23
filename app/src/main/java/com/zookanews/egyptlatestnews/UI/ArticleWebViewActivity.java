package com.zookanews.egyptlatestnews.UI;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import static com.zookanews.egyptlatestnews.Helpers.Constants.ADMOB_APP_ID;

public class ArticleWebViewActivity extends AppCompatActivity {

    private WebView webView;
    private AdView mAdView;
    private InterstitialAd mInterstitialAd;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        webView = new WebView(getApplicationContext());
        setContentView(webView);

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

        setWebViewSettings();
        loadUrlInWebView();
    }

    private void loadUrlInWebView() {
        String url = getIntent().getExtras().getString("articleLink");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            webView.getWebChromeClient();
        }
        webView.loadUrl(url);
    }

    private void setWebViewSettings() {
        //        webView = findViewById(R.id.article_web_view);
        webView.setWebViewClient(new WebViewClient());
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportZoom(false);
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowContentAccess(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setDomStorageEnabled(true);
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.setScrollbarFadingEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            Log.d("TAG", "The interstitial wasn't loaded yet.");
        }
    }
}
