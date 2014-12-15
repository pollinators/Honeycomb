package io.github.pollinators.honeycomb.util;

import java.util.List;
import java.util.concurrent.Future;

import io.github.pollinators.honeycomb.data.models.ResponseData;
import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import rx.Observable;

/**
 * Created by ted on 12/9/14.
 */
public interface PollinatorCoridorsAPI {

    @GET("/php/getResponses.php")
    List<ResponseData> getResponses();

    @FormUrlEncoded
    @POST("/php/insertResponse.php")
    void insertResponse(@Field("id")    int id,
                          @Field("altTypeName")     int altTypeName,
                          @Field("alwaysCollects")  int alwaysCollects,
                          @Field("flowerColor")     int flowerColor,
                          @Field("flowerSize")      int flowerSize,
                          @Field("multipleVisits")  int multipleVisits,
                          @Field("plant")           int plant,
                          @Field("regularPattern")  int regularPattern,
                          @Field("sitsOnPlants")    int sitsOnPlants,
                          @Field("sky")             int sky,
                          @Field("temp")            int temp,
                          @Field("type")            int type,
                          @Field("wind")            int wind,
                          @Field("latitude")        Double latitude,
                          @Field("longitude")       Double longitude,
                          Callback<Response> callback);

    public static final class Response {

    }
}
