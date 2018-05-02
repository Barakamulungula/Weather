package com.example.barakamulungula.weather;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.example.barakamulungula.weather.api_calls.DarkSkyWeatherApi;
import com.example.barakamulungula.weather.api_calls.GoogleAddress;
import com.example.barakamulungula.weather.api_calls.GoogleGeoApi;
import com.example.barakamulungula.weather.api_calls.Weather;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    public static final String WEATHER_KEY = "WEATHER";
    public static final String ADDRESS_KEY = "ADDRESS_KEY";
    @BindView(R.id.location_input)
    TextInputEditText locationInput;
    Bundle bundle;
    private DarkSkyWeatherApi darkSkyRequest;
    private GoogleGeoApi googleGeoApi;
    private Retrofit mGoogleRetrofit;
    private Retrofit mDarkskyRetrofit;
    private WeatherFragment weatherFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        bundle = new Bundle();
    }

    @Override
    protected void onStart() {
        super.onStart();
        googleGeoApi = getGoogleMapsRetroFit().create(GoogleGeoApi.class);
        darkSkyRequest = getDarkSkyRetrofit().create(DarkSkyWeatherApi.class);
    }

    @OnClick(R.id.search_button)
    protected void showWeather() {
        if (!locationInput.getText().toString().isEmpty()) {
            weatherFragment = WeatherFragment.newInstance();
            googleMapsmakeApiRequest(locationInput.getText().toString(), getString(R.string.google_maps_api_key));
            weatherFragment.setArguments(bundle);
            Log.println(Log.WARN, "Request Made", "Done");

        } else {
            Toast.makeText(this, "enter address", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        if (weatherFragment.isAdded()) {
            getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.exit_to_left, R.anim.exit_to_right).remove(weatherFragment).commit();
            locationInput.setText("");
        } else {
            super.onBackPressed();
        }
    }

    private void googleMapsmakeApiRequest(final String address, final String apiKey) {
        Log.println(Log.WARN, "User Input Address", address);
        googleGeoApi.getLocation(address, apiKey).
                enqueue(new Callback<GoogleAddress>() {
                    @Override
                    public void onResponse(Call<GoogleAddress> call, Response<GoogleAddress> response) {
                        if (response.isSuccessful()) {
                            Log.println(Log.WARN, "STATUS: ", response.body().getStatus());
                            if (response.body().getStatus().equalsIgnoreCase("ok")) {
                                String address = response.body().getResultsList().get(0).getAddressName();
                                bundle.putString(ADDRESS_KEY, address);
                                Log.println(Log.WARN, "Address", address);
                                double latitude = response.body().getResultsList().get(0).
                                        getGeometry().getGoogleLocation().getLatitude();
                                double longitude = response.body().getResultsList().get(0).
                                        getGeometry().getGoogleLocation().getLongitude();
                                getWeather(latitude, longitude);
                            }else if(response.body().getStatus().trim().equals("ZERO_RESULTS")){
                                Toast.makeText(MainActivity.this, "Address not found", Toast.LENGTH_SHORT).show();
                            }else{
                                googleMapsmakeApiRequest(address, apiKey);
                            }

                        } else {
                            Toast.makeText(MainActivity.this, "unsuccessful", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<GoogleAddress> call, Throwable t) {
                        t.printStackTrace();
                        Toast.makeText(MainActivity.this, "Google Request Failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void getWeather(final double lat, final double lng) {
        darkSkyRequest.getWeather(getString(R.string.darksky_api_key), lat, lng)
                .enqueue(new Callback<Weather>() {
                    @Override
                    public void onResponse(Call<Weather> call, Response<Weather> response) {
                        try {
                            if (response.isSuccessful()) {
                                bundle.putParcelable(WEATHER_KEY, response.body());
                                transitionToWeatherFragment();
                                hideKeyboard(MainActivity.this);
                            } else {
                                Toast.makeText(MainActivity.this, "unsuccessful weather request", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Toast.makeText(MainActivity.this, "Try again", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<Weather> call, Throwable t) {
                        Toast.makeText(MainActivity.this, "Weather call failed", Toast.LENGTH_SHORT).show();
                        t.printStackTrace();
                    }
                });
    }

    private Retrofit getGoogleMapsRetroFit() {
        if (googleGeoApi == null) {
            mGoogleRetrofit = new Retrofit.Builder().baseUrl(getString(R.string.google_base_url))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return mGoogleRetrofit;
    }

    private Retrofit getDarkSkyRetrofit() {
        if (mDarkskyRetrofit == null) {
            mDarkskyRetrofit = new Retrofit.Builder().baseUrl(getString(R.string.darksky_base_url))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return mDarkskyRetrofit;
    }

    private void transitionToWeatherFragment() {
        getSupportFragmentManager().beginTransaction().
                setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right)
                .replace(R.id.fragment_holder, weatherFragment).commit();
    }

    public static void hideKeyboard(Activity activity) {
        View view = activity.findViewById(android.R.id.content);
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            assert imm != null;
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


}
