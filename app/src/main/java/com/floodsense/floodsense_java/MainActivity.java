package com.floodsense.floodsense_java;

//import static com.floodsense.floodsense_java.APIClient.retrofit;


import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.floodsense.floodsense_java.databinding.ActivityMainBinding;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.query.FluxRecord;
import com.influxdb.query.FluxTable;
import java.util.List;


public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "InfluxDBRequest";
    private static final String ORG = "d7e9ec57dbaaad80";
    private String token = BuildConfig.API_KEY;
    private String bucket = "TomorrowApi";
    private String org = "FloodSense";
    private String url = "http://10.0.2.2:8086";
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView tvRainIntensity;
    private TextView tvHumidity;
    private TextView tvTemperature;
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.map);

        // Initialize TextView
        tvRainIntensity = findViewById(R.id.tvRainIntensity);
        tvHumidity = findViewById(R.id.tvHumidity);
        tvTemperature = findViewById(R.id.tvTemperature);

        // WebView settings
        WebView grafanaWebView = findViewById(R.id.grafana_dashboard);
        setupWebView(grafanaWebView);

        // Initialize InfluxDB connection
        InfluxDBConnectionClass inConn = new InfluxDBConnectionClass();
        InfluxDBClient influxDBClient = inConn.buildConnection(url, token, bucket, org);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(() -> {
                    myUpdateOperation();
                }
        );
        // Execute the InfluxDB query in a background thread
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            List<FluxTable> rainTable = inConn.queryIntensity(influxDBClient);
            StringBuilder rainIntensityData = new StringBuilder();
            for (FluxTable fluxTable : rainTable) {
                for (FluxRecord fluxRecord : fluxTable.getRecords()) {
                    rainIntensityData
                            .append("\t\t\t\t\t\t\t\t\t\t\t")
                            .append(fluxRecord.getValueByKey("_value"))
                            .append("\n");
                }
            }
            List<FluxTable> humidityTable = inConn.queryHumidity(influxDBClient);
            StringBuilder humidityData = new StringBuilder();
            for (FluxTable fluxTable : humidityTable) {
                for (FluxRecord fluxRecord : fluxTable.getRecords()) {
                    humidityData
                            .append("\t\t\t\t\t\t\t\t\t\t\t")
                            .append(fluxRecord.getValueByKey("_value"))
                            .append("\n");
                }
            }
            List<FluxTable> tempTable = inConn.queryTemp(influxDBClient);
            StringBuilder tempData = new StringBuilder();
            for (FluxTable fluxTable : tempTable) {
                for (FluxRecord fluxRecord : fluxTable.getRecords()) {
                    tempData
                            .append("\t\t\t\t\t\t\t\t\t\t\t")
                            .append(fluxRecord.getValueByKey("_value"))
                            .append("\n");
                }
            }

            // Update the TextView on the main thread
            runOnUiThread(() -> {
                tvRainIntensity.setText(rainIntensityData.toString());
                tvHumidity.setText(humidityData.toString());
                tvTemperature.setText(tempData.toString());
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
    private void myUpdateOperation() {
        tvRainIntensity = findViewById(R.id.tvRainIntensity);
        tvHumidity = findViewById(R.id.tvHumidity);
        tvTemperature = findViewById(R.id.tvTemperature);
        InfluxDBConnectionClass inConn = new InfluxDBConnectionClass();
        InfluxDBClient influxDBClient = inConn.buildConnection(url, token, bucket, org);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            List<FluxTable> rainTable = inConn.queryIntensity(influxDBClient);
            StringBuilder rainIntensityData = new StringBuilder();
            for (FluxTable fluxTable : rainTable) {
                for (FluxRecord fluxRecord : fluxTable.getRecords()) {
                    rainIntensityData
                            .append("\t\t\t\t\t\t\t\t\t\t\t")
                            .append(fluxRecord.getValueByKey("_value"))
                            .append("\n");
                }
            }
            List<FluxTable> humidityTable = inConn.queryHumidity(influxDBClient);
            StringBuilder humidityData = new StringBuilder();
            for (FluxTable fluxTable : humidityTable) {
                for (FluxRecord fluxRecord : fluxTable.getRecords()) {
                    humidityData
                            .append("\t\t\t\t\t\t\t\t\t\t\t")
                            .append(fluxRecord.getValueByKey("_value"))
                            .append("\n");
                }
            }
            List<FluxTable> tempTable = inConn.queryTemp(influxDBClient);
            StringBuilder tempData = new StringBuilder();
            for (FluxTable fluxTable : tempTable) {
                for (FluxRecord fluxRecord : fluxTable.getRecords()) {
                    tempData
                            .append("\t\t\t\t\t\t\t\t\t\t\t")
                            .append(fluxRecord.getValueByKey("_value"))
                            .append("\n");
                }
            }

            // Update the TextView on the main thread
            runOnUiThread(() -> {
                tvRainIntensity.setText("");
                tvRainIntensity.setText(rainIntensityData.toString());
                tvHumidity.setText("");
                tvHumidity.setText(humidityData.toString());
                tvTemperature.setText("");
                tvTemperature.setText(tempData.toString());
            });

            influxDBClient.close(); // Close the connection
        });
        swipeRefreshLayout.setRefreshing(false);
    }

    MapFragment mapFragment = new MapFragment();

    @Override
    public boolean
    onNavigationItemSelected(@NonNull MenuItem item)
    {
        int id = item.getItemId();
        if(id == R.id.map) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flFragment, mapFragment)
                    .commit();
            return true;
        }
        return false;
    }
}
