/*package com.floodsense.floodsense_java;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Header;
import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.QueryApi;
import com.influxdb.client.WriteApiBlocking;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import com.influxdb.query.FluxRecord;
import com.influxdb.query.FluxTable;
public interface APIInterface {
    //@FormUrlEncoded
    @POST("api/v2/query")
    @Headers({
            "Accept: application/csv",
            "Content-type: application/vnd.flux",
            "Authorization: Token EDZaz6AiynCX1DAOgMyC0Bt_P_wMaaocSGVFqmEZFt4VCkPwECnorMcjzVNrmxPygTvfJVK8aPvJV9YNvQJ9jw=="
    })

    Call<String> queryRainIntensity(
            //@Header("Authorization: Token EDZaz6AiynCX1DAOgMyC0Bt_P_wMaaocSGVFqmEZFt4VCkPwECnorMcjzVNrmxPygTvfJVK8aPvJV9YNvQJ9jw==")
            @Query("orgID") String orgID,
            @Body String fluxQuery
    );
}*/

