package com.renj.percentchart;

import android.graphics.Color;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private Button btReLoad;
    private PercentChartView percentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btReLoad = findViewById(R.id.bt_reload);
        percentView = findViewById(R.id.percent_view);

        percentView.setOnSelectedChangeListener(new PercentChartView.OnSelectedChangeListener() {
            @Override
            public void onSelectedChange(PercentChartView.PercentChartEntity percentChartEntity, RectF fillRectF, RectF screenRectF) {
                Log.i("MainActivity", "fillRectF: " + fillRectF + "  --  screenRectF: " + screenRectF);
            }
        });

        btReLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setData();
            }
        });

        setData();
    }

    private void setData() {
        List<PercentChartView.PercentChartEntity> percentChartEntities = new ArrayList<>();
        Random random = new Random();
        float temp = 0;
        while (temp < 1) {
            float tempValue = random.nextInt(200) * 1.0f / 20000;
            if (tempValue == 0) continue;
            if (temp + tempValue > 1) {
                tempValue = 1 - temp;
            }
            temp += tempValue;
            PercentChartView.PercentChartEntity percentChartEntity = new PercentChartView.PercentChartEntity();
            percentChartEntity.percent = tempValue;
            int yValue = random.nextInt(3) + 1;
            percentChartEntity.yValue = yValue;
            if (yValue == 1) percentChartEntity.color = Color.parseColor("#6569FF");
            if (yValue == 2) percentChartEntity.color = Color.parseColor("#B5E22B");
            if (yValue == 3) percentChartEntity.color = Color.parseColor("#1FCC7B");
            percentChartEntities.add(percentChartEntity);
        }

//        for (PercentChartView.PercentChartEntity percentChartEntity : percentChartEntities) {
//            Log.i("SleepQualityView", percentChartEntity.percent + " - " + percentChartEntity.yValue + " - " + percentChartEntity.color);
//        }

        percentView.setChartData(percentChartEntities);
    }
}
