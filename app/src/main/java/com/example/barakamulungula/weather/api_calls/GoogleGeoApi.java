package com.example.barakamulungula.weather.api_calls;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GoogleGeoApi {
    @GET("/maps/api/geocode/json")
    Call<GoogleAddress> getLocation(@Query("address") String address, @Query("api_key") String apiKey);

}
