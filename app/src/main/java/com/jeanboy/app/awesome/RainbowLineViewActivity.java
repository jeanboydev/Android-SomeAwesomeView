package com.jeanboy.app.awesome;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.jeanboy.app.awesome.view.RainbowLineData;
import com.jeanboy.app.awesome.view.RainbowLineView;

import java.util.ArrayList;
import java.util.List;

public class RainbowLineViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rainbow_line_view);


        RainbowLineView weatherLineFiveView = (RainbowLineView) findViewById(R.id.weatherLineFiveView);

        List<RainbowLineData> dataList = new ArrayList<>();
        dataList.add(new RainbowLineData("18°", "13:00", 2));
        dataList.add(new RainbowLineData("17°", "14:00", 7));
        dataList.add(new RainbowLineData("15°", "15:00", 9));
        dataList.add(new RainbowLineData("10°", "16:00", 6));
        dataList.add(new RainbowLineData("8°", "17:00", 4));

        weatherLineFiveView.setData(dataList);

        weatherLineFiveView.showWithAnim();
    }
}
