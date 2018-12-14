package com.renj.percentchart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.NonNull;
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
    // 网格线画笔
    private Paint linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    // 标签画笔
    private Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    // 背景色画笔
    private Paint colorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private float yLabelsWidth = 120;  // y 轴方向标签宽度
    private float xLabelsHeight = 100; // x 轴方向标签高度
    private float xLabelPadding = 12;  // x 轴标签名距离x轴的距离
    private float yLabelPadding = 12;  // y 轴标签名距离y轴的距离
    private float xOffsetWidth = 40;   // x 方向偏移量，用于保证文字居中，最后边一个文字也不会偏移出控件范围
    private float yOffsetWidth = 30;   // y 方向偏移量，用于保证文字居中，最上边一个文字也不会偏移出控件范围

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
        textPaint.setTextSize(28);

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
        float startXValue = yLabelsWidth;

        int size = yAxisLabels.size();
        float spacing = (measuredHeight - xLabelsHeight - yOffsetWidth) * 1.0f / (size - 1);
        float startYValue = measuredHeight - xLabelsHeight;
        for (DataBean dataBean : dataBeans) {
            float endXValue = startXValue + dataBean.percent * (measuredWidth - yLabelsWidth - xOffsetWidth);
            float endYValue = (yAxisLabels.size() - 1 - dataBean.yValue) * spacing + yOffsetWidth;
            colorPaint.setColor(dataBean.color);
            canvas.drawRect(startXValue, startYValue, endXValue, endYValue, colorPaint);
            startXValue = endXValue;
        }
    }

    private void drawYText(int measuredWidth, int measuredHeight, Canvas canvas) {
        int size = yAxisLabels.size();
        float spacing = (measuredHeight - xLabelsHeight - yOffsetWidth) * 1.0f / (size - 1);
        for (int i = 0; i < size; i++) {
            String text = yAxisLabels.get(size - 1 - i);
            Rect textRect = measureTextSize(text);
            canvas.drawText(text, yLabelsWidth - textRect.width() - yLabelPadding, i * spacing + yOffsetWidth + textRect.height() / 2, textPaint);
        }
    }

    private void drawXText(int measuredWidth, int measuredHeight, Canvas canvas) {
        int size = xAxisLabels.size();
        float spacing = (measuredWidth - yLabelsWidth - xOffsetWidth) * 1.0f / (size - 1);
        for (int i = 0; i < size; i++) {
            float startX = i * spacing + yLabelsWidth;
            String text = xAxisLabels.get(i);
            Rect textRect = measureTextSize(text);
            canvas.drawText(text, startX - yOffsetWidth, measuredHeight - (xLabelsHeight - textRect.height() - xLabelPadding), textPaint);
        }
    }

    private Rect measureTextSize(@NonNull String text) {
        Rect bounds = new Rect();
        textPaint.getTextBounds(text, 0, text.length(), bounds);
        return bounds;
    }

    private void drawXLine(int measuredWidth, int measuredHeight, Canvas canvas) {
        int size = yAxisLabels.size();
        float spacing = (measuredHeight - xLabelsHeight - yOffsetWidth) * 1.0f / (size - 1);
        for (int i = 0; i < size; i++) {
            canvas.drawLine(yLabelsWidth, i * spacing + yOffsetWidth, measuredWidth - xOffsetWidth, i * spacing + yOffsetWidth, linePaint);
        }
    }

    private void drawYLine(int measuredWidth, int measuredHeight, Canvas canvas) {
        int size = xAxisLabels.size();
        float spacing = (measuredWidth - yLabelsWidth - xOffsetWidth) * 1.0f / (size - 1);
        for (int i = 0; i < size; i++) {
            float startX = i * spacing + yLabelsWidth;
            canvas.drawLine(startX, yOffsetWidth, startX, measuredHeight - xLabelsHeight, linePaint);
        }
    }

    public static class DataBean {
        public float percent;
        public Integer color;
        public Integer yValue;
    }
}
