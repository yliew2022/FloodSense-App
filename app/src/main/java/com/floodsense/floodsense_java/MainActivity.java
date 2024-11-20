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

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.query.FluxRecord;
import com.influxdb.query.FluxTable;
import java.util.List;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.floodsense.floodsense_java.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;

import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.io.*;
public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {


    //private static final String TAG = "InfluxDBRequest";
    //private static final String ORG = "d7e9ec57dbaaad80";
    //private String token = BuildConfig.API_KEY;
    //private String bucket = "TomorrowApi";
    //private String org = "FloodSense";
    //private String url = "http://10.0.2.2:8086";
    //private SwipeRefreshLayout swipeRefreshLayout;
    //private TextView tvRainIntensity;
    //private TextView tvHumidity;
    //private TextView tvTemperature;
    BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.person);



        // Initialize TextView
        //tvRainIntensity = findViewById(R.id.tvRainIntensity);
        //tvHumidity = findViewById(R.id.tvHumidity);
        //tvTemperature = findViewById(R.id.tvTemperature);
        //WebView leafletWebView = findViewById(R.id.leafletWebView);
        //setupLeaflet(leafletWebView);


        // WebView settings

    }


    FirstFragment firstFragment = new FirstFragment();
    SecondFragment secondFragment = new SecondFragment();
    ThirdFragment thirdFragment = new ThirdFragment();

    @Override
    public boolean
    onNavigationItemSelected(@NonNull MenuItem item)
    {

        switch (item.getItemId()) {
            case R.id.person:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flFragment, firstFragment)
                        .commit();
                return true;

            case R.id.home:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flFragment, secondFragment)
                        .commit();
                return true;

            case R.id.settings:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flFragment, thirdFragment)
                        .commit();
                return true;
        }
        return false;
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.flFragment, fragment);
        fragmentTransaction.commit();
    }

    private void setupLeaflet(WebView leafletWebView) {
        WebSettings settings = leafletWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setLoadsImagesAutomatically(true);
        settings.setMixedContentMode(WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);
        leafletWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedSslError(WebView view, android.webkit.SslErrorHandler handler, android.net.http.SslError error) {
                handler.proceed();  // Ignore SSL certificate errors
            }
        });

        // Load the HTML file from the assets folder
        leafletWebView.loadUrl("map.html");
    }



}

