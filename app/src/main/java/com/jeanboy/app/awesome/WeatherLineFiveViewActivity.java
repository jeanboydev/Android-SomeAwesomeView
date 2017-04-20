package com.jeanboy.app.awesome;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.jeanboy.app.awesome.view.WeatherGoRunData;
import com.jeanboy.app.awesome.view.WeatherLineFiveView;

import java.util.ArrayList;
import java.util.List;

public class WeatherLineFiveViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_line_five_view);


        WeatherLineFiveView weatherLineFiveView = (WeatherLineFiveView) findViewById(R.id.weatherLineFiveView);

        List<WeatherGoRunData> dataList = new ArrayList<>();
        dataList.add(new WeatherGoRunData("18°", "13:00", 2));
        dataList.add(new WeatherGoRunData("17°", "14:00", 7));
        dataList.add(new WeatherGoRunData("15°", "15:00", 9));
        dataList.add(new WeatherGoRunData("10°", "16:00", 6));
        dataList.add(new WeatherGoRunData("8°", "17:00", 4));

        weatherLineFiveView.setData(dataList);

        weatherLineFiveView.showWithAnim();
    }
}
