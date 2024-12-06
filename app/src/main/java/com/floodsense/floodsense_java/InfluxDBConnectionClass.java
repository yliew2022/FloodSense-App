package com.floodsense.floodsense_java;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;
import com.influxdb.client.QueryApi;
import com.influxdb.client.WriteApiBlocking;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import com.influxdb.exceptions.InfluxException;
import com.influxdb.query.FluxRecord;
import com.influxdb.query.FluxTable;
import com.influxdb.client.DeleteApi;

public class InfluxDBConnectionClass {

    private String token;
    private String bucket;
    private String org;

    private String url;
    List<FluxTable> arr = new ArrayList<FluxTable>();
    public InfluxDBClient buildConnection(String url, String token, String bucket, String org) {
        setToken(token);
        setBucket(bucket);
        setOrg(org);
        setUrl(url);
        return InfluxDBClientFactory.create(getUrl(), getToken().toCharArray(), getOrg(), getBucket());
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getOrg() {
        return org;
    }

    public void setOrg(String org) {
        this.org = org;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    public List<FluxTable> queryIntensity(InfluxDBClient influxDBClient) {
        String flux = "from(bucket:\"TomorrowApi\") |> range(start: -3m) |> filter(fn:(r) => r._measurement == \"weatherData\" and r._field == \"precipitationProbability\")";
        QueryApi queryApi = influxDBClient.getQueryApi();

        try {
            return queryApi.query(flux);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public List<FluxTable> queryHumidity(InfluxDBClient influxDBClient) {
        String flux = "from(bucket:\"TomorrowApi\") |> range(start: -3m) |> filter(fn:(r) => r._measurement == \"weatherData\" and r._field == \"humidity\")";
        QueryApi queryApi = influxDBClient.getQueryApi();

        try {
            return queryApi.query(flux);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public List<FluxTable> queryTemp(InfluxDBClient influxDBClient) {
        String flux = "from(bucket:\"TomorrowApi\") |> range(start: -3m) |> filter(fn:(r) => r._measurement == \"weatherData\" and r._field == \"temperature\")";
        QueryApi queryApi = influxDBClient.getQueryApi();

        try {
            return queryApi.query(flux);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
