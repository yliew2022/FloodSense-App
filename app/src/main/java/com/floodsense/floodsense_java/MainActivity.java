package com.floodsense.floodsense_java;

//import static com.floodsense.floodsense_java.APIClient.retrofit;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.QueryApi;
import com.influxdb.query.FluxRecord;
import com.influxdb.query.FluxTable;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "InfluxDBRequest";
    private static final String ORG = "d7e9ec57dbaaad80";
    private String token = BuildConfig.API_KEY;
    private String bucket = "TomorrowApi";
    private String org = "FloodSense";
    private String url = "http://10.0.2.2:8086";

    private TextView tvRainIntensity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize TextView
        tvRainIntensity = findViewById(R.id.tvRainIntensity);

        // WebView settings
        WebView grafanaWebView = findViewById(R.id.grafana_dashboard);
        setupWebView(grafanaWebView);

        // Initialize InfluxDB connection
        InfluxDBConnectionClass inConn = new InfluxDBConnectionClass();
        InfluxDBClient influxDBClient = inConn.buildConnection(url, token, bucket, org);

        // Execute the InfluxDB query in a background thread
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            List<FluxTable> tables = inConn.queryData(influxDBClient);

            // Extract the rain intensity value from the query result
            StringBuilder rainIntensityData = new StringBuilder();
            for (FluxTable fluxTable : tables) {
                for (FluxRecord fluxRecord : fluxTable.getRecords()) {
                    rainIntensityData.append(fluxRecord.getValueByKey("_field"))
                            .append(": ")
                            .append(fluxRecord.getValueByKey("_value"))
                            .append("\n");
                }
            }

            // Update the TextView on the main thread
            runOnUiThread(() -> {
                tvRainIntensity.setText(rainIntensityData.toString());
            });

            influxDBClient.close(); // Close the connection
        });
    }

    private void setupWebView(WebView grafanaWebView) {
        WebSettings settings = grafanaWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setLoadsImagesAutomatically(true);
        settings.setMixedContentMode(WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);

        String grafanaUrl = "https://10.0.2.2:443/public-dashboards/54dc7f900a30411abf01729bf741c92d?orgId=1&from=now-1m&to=now&refresh=auto";
        grafanaWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedSslError(WebView view, android.webkit.SslErrorHandler handler, android.net.http.SslError error) {
                handler.proceed();  // Ignore SSL certificate errors
            }
        });
        grafanaWebView.loadUrl(grafanaUrl);
    }
}
