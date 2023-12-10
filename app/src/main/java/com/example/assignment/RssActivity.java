package com.example.assignment;

import android.os.Bundle;
import android.webkit.WebView;
import androidx.appcompat.app.AppCompatActivity;

public class RssActivity extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rss);

        webView = findViewById(R.id.webView);

        // Retrieve the RSS link from the intent
        String rssLink = getIntent().getStringExtra("rssLink");

        // Load the RSS link in the WebView
        if (rssLink != null && !rssLink.isEmpty()) {
            webView.loadUrl(rssLink);
        }
    }
}
