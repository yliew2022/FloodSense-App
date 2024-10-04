package com.floodsense.floodsense_java;

import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.appcompat.app.AppCompatActivity;


import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.floodsense.floodsense_java.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WebView grafanaWebView = findViewById(R.id.grafana_dashboard);

        // Enable JavaScript for Grafana dashboards
        WebSettings settings = grafanaWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        // Removed cache to test auto refresh
        grafanaWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        grafanaWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        grafanaWebView.clearCache(true);
        settings.setLoadsImagesAutomatically(true);
        settings.setMixedContentMode(WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);
        // Set Grafana dashboard URL (ensure the link is accessible)

        String grafanaUrl = "https://10.0.2.2:443/public-dashboards/2738825593704dd3b03933c56d1869c6?orgId=1&from=now-1m&to=now&refresh=auto";
        //grafanaWebView.setWebViewClient(new WebViewClient());
        // *IMPORTANT*
        // When using production remove cuz we don't want SSL bypass on secure requests
        grafanaWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedSslError(WebView view, android.webkit.SslErrorHandler handler, android.net.http.SslError error) {
                handler.proceed();  // Ignore SSL certificate errors
            }
        });
        grafanaWebView.loadUrl(grafanaUrl);
    }

}