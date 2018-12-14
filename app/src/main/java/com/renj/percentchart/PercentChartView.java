package com.renj.percentchart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * ======================================================================
 * <p>
 * 作者：Renj
 * 邮箱：renjunhua@anlovek.com
 * <p>
 * 创建时间：2018-12-14   13:25
 * <p>
 * 描述：按百分比分配图
 * <p>
 * 修订历史：
 * <p>
 * ======================================================================
 */
public class PercentChartView extends View {
    private List<String> xAxisLabels = new ArrayList<>(Arrays.asList("19时", "20时", "21时", "22时",
            "23时", "00时", "01时", "02时", "03时", "04时", "05时", "06时", "07时", "08时", "09时"));

    private List<String> yAxisLabels = new ArrayList<>(Arrays.asList("", "清醒", "浅睡", "深睡", ""));

    private List<DataBean> dataBeans = new ArrayList<>();

    private Paint linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint colorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public PercentChartView(Context context) {
        this(context, null);
    }

    public PercentChartView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PercentChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        linePaint.setStrokeWidth(2);
        linePaint.setColor(Color.BLACK);

        textPaint.setColor(Color.RED);
        textPaint.setTextSize(30);

        colorPaint.setStyle(Paint.Style.FILL);

        Random random = new Random();
        float temp = 0;
        while (temp < 1) {
            float tempValue = random.nextInt(200) * 1.0f / 20000;
            if (tempValue == 0) continue;
            if (temp + tempValue > 1) {
                tempValue = 1 - temp;
            }
            temp += tempValue;
            DataBean dataBean = new DataBean();
            dataBean.percent = tempValue;
            int yValue = random.nextInt(3) + 1;
            dataBean.yValue = yValue;
            if (yValue == 1) dataBean.color = Color.GRAY;
            if (yValue == 2) dataBean.color = Color.BLUE;
            if (yValue == 3) dataBean.color = Color.GREEN;
            dataBeans.add(dataBean);
        }

        for (DataBean dataBean : dataBeans) {
            Log.i("SleepQualityView", dataBean.percent + " - " + dataBean.yValue + " - " + dataBean.color);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public PercentChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int measuredWidth = getMeasuredWidth();
        int measuredHeight = getMeasuredHeight();

        // 绘制x轴及x轴方向的网格线
        drawXLine(measuredWidth, measuredHeight, canvas);
        // 绘制y轴及y轴方向的网格线
        drawYLine(measuredWidth, measuredHeight, canvas);
        // 绘制x轴方向文字
        drawXText(measuredWidth, measuredHeight, canvas);
        // 绘制y轴方向文字
        drawYText(measuredWidth, measuredHeight, canvas);
        // 绘制填充颜色
        drawFillColor(measuredWidth, measuredHeight, canvas);
    }

    private void drawFillColor(int measuredWidth, int measuredHeight, Canvas canvas) {
        float startXValue = 120;

        int size = yAxisLabels.size();
        float spacing = (measuredHeight - 100 - 30) * 1.0f / (size - 1);
        float startYValue = measuredHeight - 100;
        for (DataBean dataBean : dataBeans) {
            float endXValue = startXValue + dataBean.percent * (measuredWidth - 120 - 40);
            float endYValue = (yAxisLabels.size() - 1 - dataBean.yValue) * spacing + 30;
            colorPaint.setColor(dataBean.color);
            canvas.drawRect(startXValue, startYValue, endXValue, endYValue, colorPaint);
            startXValue = endXValue;
        }
    }

    private void drawYText(int measuredWidth, int measuredHeight, Canvas canvas) {
        int size = yAxisLabels.size();
        float spacing = (measuredHeight - 100 - 30) * 1.0f / (size - 1);
        for (int i = 0; i < size; i++) {
            canvas.drawText(yAxisLabels.get(size - 1 - i), 40, i * spacing + 30 + 10, textPaint);
        }
    }

    private void drawXText(int measuredWidth, int measuredHeight, Canvas canvas) {
        int size = xAxisLabels.size();
        float spacing = (measuredWidth - 120 - 40) * 1.0f / (size - 1);
        for (int i = 0; i < size; i++) {
            float startX = i * spacing + 120;
            canvas.drawText(xAxisLabels.get(i), startX - 30, measuredHeight - 50, textPaint);
        }
    }

    private void drawXLine(int measuredWidth, int measuredHeight, Canvas canvas) {
        int size = yAxisLabels.size();
        float spacing = (measuredHeight - 100 - 30) * 1.0f / (size - 1);
        for (int i = 0; i < size; i++) {
            canvas.drawLine(120, i * spacing + 30, measuredWidth - 40, i * spacing + 30, linePaint);
        }
    }

    private void drawYLine(int measuredWidth, int measuredHeight, Canvas canvas) {
        int size = xAxisLabels.size();
        float spacing = (measuredWidth - 120 - 40) * 1.0f / (size - 1);
        for (int i = 0; i < size; i++) {
            float startX = i * spacing + 120;
            canvas.drawLine(startX, 30, startX, measuredHeight - 100, linePaint);
        }
    }

    public static class DataBean {
        public float percent;
        public Integer color;
        public Integer yValue;
    }
}
