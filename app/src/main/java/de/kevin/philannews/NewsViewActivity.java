package de.kevin.philannews;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

public class NewsViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_view);
        WebView webView = findViewById(R.id.newsWebView);
        if (getIntent().getAction() != null) {
            webView.loadUrl(getIntent().getAction());
        }
    }
}
