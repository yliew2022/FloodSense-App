package com.floodsense.floodsense_java;
import android.os.Bundle;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.query.FluxRecord;
import com.influxdb.query.FluxTable;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class FirstFragment extends Fragment {
    private TextView tvRainIntensity;
    private TextView tvHumidity;
    private TextView tvTemperature;
    private static final String TAG = "InfluxDBRequest";
    private static final String ORG = "d7e9ec57dbaaad80";
    private String token = BuildConfig.API_KEY;
    private String bucket = "TomorrowApi";
    private String org = "FloodSense";
    private String url = "http://10.0.2.2:8086";
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //setContentView(R.layout.activity_main);
        // Find views by ID within the fragment layout
        tvRainIntensity = view.findViewById(R.id.tvRainIntensity);
        tvHumidity = view.findViewById(R.id.tvHumidity);
        tvTemperature = view.findViewById(R.id.tvTemperature);
        WebView leaflet = view.findViewById(R.id.leaflet);
        setupLeaflet(leaflet);

        // Initialize and execute your data fetch and update UI here
        WebView grafanaWebView = view.findViewById(R.id.grafana_dashboard);
        setupWebView(grafanaWebView);

        // Initialize InfluxDB connection
        InfluxDBConnectionClass inConn = new InfluxDBConnectionClass();
        InfluxDBClient influxDBClient = inConn.buildConnection(url, token, bucket, org);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(() -> {
                    myUpdateOperation(view);
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
            requireActivity().runOnUiThread(() -> {
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

    private void setupLeaflet(WebView leaflet) {
        WebSettings settings = leaflet.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setLoadsImagesAutomatically(true);
        settings.setMixedContentMode(WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);

        String file = "file:///android_asset/map.html";
        leaflet.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedSslError(WebView view, android.webkit.SslErrorHandler handler, android.net.http.SslError error) {
                handler.proceed();  // Ignore SSL certificate errors
            }
        });
        leaflet.loadUrl(file);
    }

    private void myUpdateOperation(@NonNull View view) {
        tvRainIntensity = view.findViewById(R.id.tvRainIntensity);
        tvHumidity = view.findViewById(R.id.tvHumidity);
        tvTemperature = view.findViewById(R.id.tvTemperature);
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
            requireActivity().runOnUiThread(() -> {
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
}

