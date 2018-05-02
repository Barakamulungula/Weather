package com.example.barakamulungula.weather;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.barakamulungula.weather.api_calls.Weather;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.barakamulungula.weather.MainActivity.ADDRESS_KEY;
import static com.example.barakamulungula.weather.MainActivity.WEATHER_KEY;

public class WeatherFragment extends Fragment {


    @BindView(R.id.temperature)
    protected TextView tempTextView;
    @BindView(R.id.temp_high)
    protected TextView tempHighTextView;
    @BindView(R.id.temp_low)
    protected TextView tempLowTextView;
    @BindView(R.id.summary_textview)
    protected TextView summaryTextView;
    @BindView(R.id.precip)
    protected TextView precipTextView;
    @BindView(R.id.progress)
    ConstraintLayout progressLayout;
    @BindView(R.id.textview_location)
    protected TextView locationTextview;
    @BindView(R.id.weather_icon)
    ImageView weatherIcon;
    @BindView(R.id.weather_layout_background)
    ConstraintLayout weatherBackground;

    private Weather mWeather;
    private String formatted_address;

    public static WeatherFragment newInstance() {

        Bundle args = new Bundle();

        WeatherFragment fragment = new WeatherFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWeather = getArguments().getParcelable(WEATHER_KEY);
        formatted_address = getArguments().getString(ADDRESS_KEY);
    }

    @Override
    public void onStart() {
        super.onStart();
        formatted_address = getArguments().getString(ADDRESS_KEY);
        locationTextview.setText(getString(R.string.weather_for, formatted_address));
        summaryTextView.setText(mWeather.getCurrentProperties().getSummary());
        tempTextView.setText(getString(R.string.temperature, (int) Math.ceil(mWeather.getCurrentProperties().getTemperature())));
        tempHighTextView.setText(getString(R.string.high, (int) Math.ceil(mWeather.getDailyProperties().getDailyDataList()
                .get(0).getTemperatureHigh())));
        tempLowTextView.setText(getString(R.string.low, (int) Math.ceil(mWeather.getDailyProperties().getDailyDataList().get(0)
                .getTemperatureLow())));
        precipTextView.setText(getString(R.string.precip_num, 100* ((int) Math.ceil(mWeather.getDailyProperties()
                .getDailyDataList().get(0).getPrecipProbability()))));
        setWeatherIcon();
    }

    private void setWeatherIcon(){
        switch (mWeather.getCurrentProperties().getIcon()){
            case "clear-day":
                weatherIcon.setImageResource(R.drawable.clear_day);
                weatherBackground.setBackgroundResource(R.color.sunColor);
                break;
            case "clear-night":
                weatherIcon.setImageResource(R.drawable.clear_night);
                weatherBackground.setBackgroundResource(R.color.clear_night);
                break;
            case "rain":
                weatherIcon.setImageResource(R.drawable.rain);
                weatherBackground.setBackgroundResource(R.color.rainColor);
                break;
            case "sleet":
                weatherIcon.setImageResource(R.drawable.sleet);
                weatherBackground.setBackgroundResource(R.color.snowColor);
                break;
            case "fog":
                weatherIcon.setImageResource(R.drawable.fog);
                weatherBackground.setBackgroundResource(R.color.snowColor);
                break;
            case "snow":
                weatherIcon.setImageResource(R.drawable.snow);
                weatherBackground.setBackgroundResource(R.color.snowColor);
                break;
            case "partly-cloudy-day":
                weatherIcon.setImageResource(R.drawable.partly_cloudy_day);
                weatherBackground.setBackgroundResource(R.color.sunColor);
                break;
            case "partly-cloudy-night":
                weatherIcon.setImageResource(R.drawable.partly_cloudy_night);
                weatherBackground.setBackgroundResource(R.color.clear_night);
                break;


        }
        progressLayout.setVisibility(View.GONE);
    }

}
