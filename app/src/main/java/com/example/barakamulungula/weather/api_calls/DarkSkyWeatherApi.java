package com.example.barakamulungula.weather.api_calls;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface DarkSkyWeatherApi {
    @GET("/forecast/{api_key}/{latitude},{longitude}")
    Call<Weather> getWeather(@Path("api_key") String apiKey, @Path("latitude") double latitude, @Path("longitude") double longitude);

}
