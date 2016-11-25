package com.jeanboy.app.awesome;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.jeanboy.app.awesome.view.DashBoardView;

public class DashBoardViewActivity extends AppCompatActivity {


    private DashBoardView dashBoardView1,dashBoardView2,dashBoardView3,dashBoardView4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board_view);

        dashBoardView1= (DashBoardView) findViewById(R.id.dashBoardView1);
        dashBoardView2= (DashBoardView) findViewById(R.id.dashBoardView2);
        dashBoardView3= (DashBoardView) findViewById(R.id.dashBoardView3);
        dashBoardView4= (DashBoardView) findViewById(R.id.dashBoardView4);

        dashBoardView1.setShowAngle(360f);
        dashBoardView1.setStrokeWidth(20f);
        dashBoardView1.setBoardPercent(0.8f);
        dashBoardView1.setLevel(4);
        dashBoardView1.showWithAnim();

        dashBoardView2.setShowAngle(360f);
        dashBoardView2.setStrokeWidth(20f);
        dashBoardView2.setBoardPercent(0.7f);
        dashBoardView2.setLevel(6);
        dashBoardView2.showWithAnim();

        dashBoardView3.setShowAngle(360f);
        dashBoardView3.setStrokeWidth(20f);
        dashBoardView3.setBoardPercent(0.6f);
        dashBoardView3.setLevel(8);
        dashBoardView3.showWithAnim();

        dashBoardView4.setShowAngle(360f);
        dashBoardView4.setStrokeWidth(20f);
        dashBoardView4.setBoardPercent(0.5f);
        dashBoardView4.setLevel(10);
        dashBoardView4.showWithAnim();
    }
}
