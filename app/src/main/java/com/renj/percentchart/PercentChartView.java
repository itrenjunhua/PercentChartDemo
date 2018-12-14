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
    // 默认 x 轴标签
    private List<String> xAxisLabels = new ArrayList<>(Arrays.asList("19时", "20时", "21时", "22时",
            "23时", "00时", "01时", "02时", "03时", "04时", "05时", "06时", "07时", "08时", "09时"));
    // 默认 y 轴标签
    private List<String> yAxisLabels = new ArrayList<>(Arrays.asList("", "清醒", "浅睡", "深睡", ""));
    // 数据集合
    private List<DataBean> dataBeans = new ArrayList<>();
    // 网格线画笔
    private Paint linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    // 标签画笔
    private Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    // 背景色画笔
    private Paint colorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private float xLabelsHeight = 100;    // x 轴方向标签高度
    private float xLabelPadding = 12;     // x 轴标签名距离x轴的距离
    private float xOffsetWidth = 40;      // x 方向偏移量，用于保证文字居中，最后边一个文字也不会偏移出控件范围

    private float yLabelsWidth = 120;     // y 轴方向标签宽度
    private float yLabelPadding = 12;     // y 轴标签名距离y轴的距离
    private float yOffsetWidth = 30;      // y 方向偏移量，用于保证文字居中，最上边一个文字也不会偏移出控件范围

    private boolean isDrawXGrid = true;   // 是否绘制x方向网格线
    private int xAxisWidth = 2;           // x轴线宽度
    private int xAxisColor = 0xFF111111;  // x轴线颜色
    private float xGridWidth = 1;         // x轴方向网格线宽度
    private int xGridColor = 0xFFDDDDDD;  // x轴方向网格线颜色

    private float xLabelSize = 28;        // x 轴上标签文字大小
    private int xLabelColor = 0xFF111111; // x 轴上标签文字颜色

    private boolean isDrawYGrid = true;  // 是否绘制y方向网格线
    private int yAxisWidth = 2;           // y轴线宽度
    private int yAxisColor = 0xFF111111;  // y轴线颜色
    private int yGridWidth = 1;           // y轴方向网格线宽度
    private int yGridColor = 0xFFDDDDDD;  // y轴方向网格线颜色

    private float yLabelSize = 28;        // y 轴上标签文字大小
    private int yLabelColor = 0xFF111111; // y 轴上标签文字颜色


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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public PercentChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
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

    /*=========【end】 setter and getter 【end】=========*/

    /**
     * 获取 x 轴标签值
     *
     * @return
     */
    public List<String> getXAxisLabels() {
        return xAxisLabels;
    }

    /**
     * 设置 x 轴标签值
     *
     * @param xAxisLabels
     */
    public void setXAxisLabels(List<String> xAxisLabels) {
        this.xAxisLabels = xAxisLabels;
    }

    /**
     * 获取 y 轴标签值
     *
     * @return
     */
    public List<String> getYAxisLabels() {
        return yAxisLabels;
    }

    /**
     * 设置 y 轴标签值
     *
     * @param yAxisLabels
     */
    public void setYAxisLabels(List<String> yAxisLabels) {
        this.yAxisLabels = yAxisLabels;
    }

    /**
     * 获取数据
     *
     * @return
     */
    public List<DataBean> getChartData() {
        return dataBeans;
    }

    /**
     * 设置数据
     *
     * @param dataBeans
     */
    public void setChartData(List<DataBean> dataBeans) {
        this.dataBeans = dataBeans;
    }

    /**
     * 获取 x 轴方向标签高度
     *
     * @return
     */
    public float getXLabelsHeight() {
        return xLabelsHeight;
    }

    /**
     * 设置 x 轴方向标签高度
     *
     * @param xLabelsHeight
     */
    public void setXLabelsHeight(float xLabelsHeight) {
        this.xLabelsHeight = xLabelsHeight;
    }

    /**
     * 获取 x 轴标签名距离x轴的距离
     *
     * @return
     */
    public float getXLabelPadding() {
        return xLabelPadding;
    }

    /**
     * 、设置 x 轴标签名距离x轴的距离
     *
     * @param xLabelPadding
     */
    public void setXLabelPadding(float xLabelPadding) {
        this.xLabelPadding = xLabelPadding;
    }

    /**
     * 获取 x 方向偏移量，用于保证文字居中，最后边一个文字也不会偏移出控件范围
     *
     * @return
     */
    public float getXOffsetWidth() {
        return xOffsetWidth;
    }

    /**
     * 设置 x 方向偏移量，用于保证文字居中，最后边一个文字也不会偏移出控件范围
     *
     * @param xOffsetWidth
     */
    public void setXOffsetWidth(float xOffsetWidth) {
        this.xOffsetWidth = xOffsetWidth;
    }

    /**
     * 获取 y 轴方向标签宽度
     *
     * @return
     */
    public float getYLabelsWidth() {
        return yLabelsWidth;
    }

    /**
     * 设置 y 轴方向标签宽度
     *
     * @param yLabelsWidth
     */
    public void setYLabelsWidth(float yLabelsWidth) {
        this.yLabelsWidth = yLabelsWidth;
    }

    /**
     * 获取 y 轴标签名距离y轴的距离
     *
     * @return
     */
    public float getYLabelPadding() {
        return yLabelPadding;
    }

    /**
     * 设置 y 轴标签名距离y轴的距离
     *
     * @param yLabelPadding
     */
    public void setYLabelPadding(float yLabelPadding) {
        this.yLabelPadding = yLabelPadding;
    }

    /**
     * 获取 y 方向偏移量，用于保证文字居中，最上边一个文字也不会偏移出控件范围
     *
     * @return
     */
    public float getYOffsetWidth() {
        return yOffsetWidth;
    }

    /**
     * 设置 y 方向偏移量，用于保证文字居中，最上边一个文字也不会偏移出控件范围
     *
     * @param yOffsetWidth
     */
    public void setYOffsetWidth(float yOffsetWidth) {
        this.yOffsetWidth = yOffsetWidth;
    }

    /**
     * 是否绘制 x 方向网格线
     *
     * @return
     */
    public boolean isDrawXGrid() {
        return isDrawXGrid;
    }

    /**
     * 设置是否绘制 x 方向网格线
     *
     * @param drawXGrid
     */
    public void setDrawXGrid(boolean drawXGrid) {
        isDrawXGrid = drawXGrid;
    }

    /**
     * 获取 x 轴线宽度
     *
     * @return
     */
    public int getXAxisWidth() {
        return xAxisWidth;
    }

    /**
     * 设置 x 轴线宽度
     *
     * @param xAxisWidth
     */
    public void setXAxisWidth(int xAxisWidth) {
        this.xAxisWidth = xAxisWidth;
    }

    /**
     * 获取 x 轴线颜色
     *
     * @return
     */
    public int getXAxisColor() {
        return xAxisColor;
    }

    /**
     * 设置 x 轴线颜色
     *
     * @param xAxisColor
     */
    public void setXAxisColor(int xAxisColor) {
        this.xAxisColor = xAxisColor;
    }

    /**
     * 获取 x 轴方向网格线宽度
     *
     * @return
     */
    public float getXGridWidth() {
        return xGridWidth;
    }

    /**
     * 设置 x 轴方向网格线宽度
     *
     * @param xGridWidth
     */
    public void setXGridWidth(float xGridWidth) {
        this.xGridWidth = xGridWidth;
    }

    /**
     * 获取 x 轴方向网格线颜色
     *
     * @return
     */
    public int getXGridColor() {
        return xGridColor;
    }

    /**
     * 设置 x 轴方向网格线颜色
     *
     * @param xGridColor
     */
    public void setXGridColor(int xGridColor) {
        this.xGridColor = xGridColor;
    }

    /**
     * 获取 x 轴上标签文字大小
     *
     * @return
     */
    public float getXLabelSize() {
        return xLabelSize;
    }

    /**
     * 设置 x 轴上标签文字大小
     *
     * @param xLabelSize
     */
    public void setXLabelSize(float xLabelSize) {
        this.xLabelSize = xLabelSize;
    }

    /**
     * 获取 x 轴上标签文字颜色
     *
     * @return
     */
    public int getXLabelColor() {
        return xLabelColor;
    }

    /**
     * 设置 x 轴上标签文字颜色
     *
     * @param xLabelColor
     */
    public void setXLabelColor(int xLabelColor) {
        this.xLabelColor = xLabelColor;
    }

    /**
     * 是否绘制 y 方向网格线
     *
     * @return
     */
    public boolean isDrawYGrid() {
        return isDrawYGrid;
    }

    /**
     * 获取是否绘制 y 方向网格线
     *
     * @param drawYGrid
     */
    public void setDrawYGrid(boolean drawYGrid) {
        isDrawYGrid = drawYGrid;
    }

    /**
     * 获取 y 轴线宽度
     *
     * @return
     */
    public int getYAxisWidth() {
        return yAxisWidth;
    }

    /**
     * 设置 y 轴线宽度
     *
     * @param yAxisWidth
     */
    public void setYAxisWidth(int yAxisWidth) {
        this.yAxisWidth = yAxisWidth;
    }

    /**
     * 获取 y 轴线颜色
     *
     * @return
     */
    public int getYAxisColor() {
        return yAxisColor;
    }

    /**
     * 设置 y 轴线颜色
     *
     * @param yAxisColor
     */
    public void setYAxisColor(int yAxisColor) {
        this.yAxisColor = yAxisColor;
    }

    /**
     * 获取 y 轴方向网格线宽度
     *
     * @return
     */
    public int getYGridWidth() {
        return yGridWidth;
    }

    /**
     * 设置 y 轴方向网格线宽度
     *
     * @param yGridWidth
     */
    public void setYGridWidth(int yGridWidth) {
        this.yGridWidth = yGridWidth;
    }

    /**
     * 获取 y 轴方向网格线颜色
     *
     * @return
     */
    public int getYGridColor() {
        return yGridColor;
    }

    /**
     * 设置 y 轴方向网格线颜色
     *
     * @param yGridColor
     */
    public void setYGridColor(int yGridColor) {
        this.yGridColor = yGridColor;
    }

    /**
     * 获取 y 轴上标签文字大小
     *
     * @return
     */
    public float getYLabelSize() {
        return yLabelSize;
    }

    /**
     * 设置 y 轴上标签文字大小
     *
     * @param yLabelSize
     */
    public void setYLabelSize(float yLabelSize) {
        this.yLabelSize = yLabelSize;
    }

    /**
     * 获取 y 轴上标签文字颜色
     *
     * @return
     */
    public int getYLabelColor() {
        return yLabelColor;
    }

    /**
     * 设置 y 轴上标签文字颜色
     *
     * @param yLabelColor
     */
    public void setYLabelColor(int yLabelColor) {
        this.yLabelColor = yLabelColor;
    }

    /*======= 【end】 setter and getter 【end】 ========*/

    @Override
    protected void onDraw(Canvas canvas) {
        int measuredWidth = getMeasuredWidth();
        int measuredHeight = getMeasuredHeight();

        // 绘制x轴线
        drawXAxisLine(measuredWidth, measuredHeight, canvas);
        // 绘制x轴方向的网格线
        drawXGridLine(measuredWidth, measuredHeight, canvas);
        // 绘制y轴线
        drawYAxisLine(measuredWidth, measuredHeight, canvas);
        // 绘制y轴方向的网格线
        drawYGridLine(measuredWidth, measuredHeight, canvas);
        // 绘制x轴方向文字
        drawXText(measuredWidth, measuredHeight, canvas);
        // 绘制y轴方向文字
        drawYText(measuredWidth, measuredHeight, canvas);
        // 绘制填充颜色
        drawFillColor(measuredWidth, measuredHeight, canvas);
    }

    /**
     * 根据数据绘制填充颜色
     *
     * @param measuredWidth
     * @param measuredHeight
     * @param canvas
     */
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

    /**
     * 绘制x轴向标签
     *
     * @param measuredWidth
     * @param measuredHeight
     * @param canvas
     */
    private void drawYText(int measuredWidth, int measuredHeight, Canvas canvas) {
        textPaint.setTextSize(xLabelSize);
        textPaint.setColor(xLabelColor);

        int size = yAxisLabels.size();
        float spacing = (measuredHeight - xLabelsHeight - yOffsetWidth) * 1.0f / (size - 1);
        for (int i = 0; i < size; i++) {
            String text = yAxisLabels.get(size - 1 - i);
            Rect textRect = measureTextSize(text);
            canvas.drawText(text, yLabelsWidth - textRect.width() - yLabelPadding, i * spacing + yOffsetWidth + textRect.height() / 2, textPaint);
        }
    }

    /**
     * 绘制x轴向标签
     *
     * @param measuredWidth
     * @param measuredHeight
     * @param canvas
     */
    private void drawXText(int measuredWidth, int measuredHeight, Canvas canvas) {
        textPaint.setTextSize(yLabelSize);
        textPaint.setColor(yLabelColor);

        int size = xAxisLabels.size();
        float spacing = (measuredWidth - yLabelsWidth - xOffsetWidth) * 1.0f / (size - 1);
        for (int i = 0; i < size; i++) {
            float startX = i * spacing + yLabelsWidth;
            String text = xAxisLabels.get(i);
            Rect textRect = measureTextSize(text);
            canvas.drawText(text, startX - yOffsetWidth, measuredHeight - (xLabelsHeight - textRect.height() - xLabelPadding), textPaint);
        }
    }

    /**
     * 测量文字大小
     *
     * @param text
     * @return
     */
    private Rect measureTextSize(@NonNull String text) {
        Rect bounds = new Rect();
        textPaint.getTextBounds(text, 0, text.length(), bounds);
        return bounds;
    }

    /**
     * 绘制x轴线
     *
     * @param measuredWidth
     * @param measuredHeight
     * @param canvas
     */
    private void drawXAxisLine(int measuredWidth, int measuredHeight, Canvas canvas) {
        linePaint.setStrokeWidth(xAxisWidth);
        linePaint.setColor(xAxisColor);
        canvas.drawLine(yLabelsWidth, measuredHeight - xLabelsHeight, measuredWidth - xOffsetWidth, measuredHeight - xLabelsHeight, linePaint);
    }

    /**
     * 绘制x轴方向的网格线
     *
     * @param measuredWidth
     * @param measuredHeight
     * @param canvas
     */
    private void drawXGridLine(int measuredWidth, int measuredHeight, Canvas canvas) {
        if (isDrawXGrid) {
            int size = yAxisLabels.size();
            float spacing = (measuredHeight - xLabelsHeight - yOffsetWidth) * 1.0f / (size - 1);
            for (int i = 0; i < size - 1; i++) {
                float yValue = i * spacing + yOffsetWidth;
                linePaint.setStrokeWidth(xGridWidth);
                linePaint.setColor(xGridColor);
                canvas.drawLine(yLabelsWidth, yValue, measuredWidth - xOffsetWidth, yValue, linePaint);
            }
        }
    }

    /**
     * 绘制y轴线
     *
     * @param measuredWidth
     * @param measuredHeight
     * @param canvas
     */
    private void drawYAxisLine(int measuredWidth, int measuredHeight, Canvas canvas) {
        linePaint.setStrokeWidth(yAxisWidth);
        linePaint.setColor(yAxisColor);
        canvas.drawLine(yLabelsWidth, yOffsetWidth, yLabelsWidth, measuredHeight - xLabelsHeight, linePaint);
    }

    /**
     * 绘制y轴方向的网格线
     *
     * @param measuredWidth
     * @param measuredHeight
     * @param canvas
     */
    private void drawYGridLine(int measuredWidth, int measuredHeight, Canvas canvas) {
        // 判断是否需要 y 轴方向的网格线
        if (isDrawYGrid) {
            int size = xAxisLabels.size();
            float spacing = (measuredWidth - yLabelsWidth - xOffsetWidth) * 1.0f / (size - 1);
            for (int i = 1; i < size; i++) {
                float xValue = i * spacing + yLabelsWidth;
                linePaint.setStrokeWidth(yGridWidth);
                linePaint.setColor(yGridColor);
                canvas.drawLine(xValue, yOffsetWidth, xValue, measuredHeight - xLabelsHeight, linePaint);
            }
        }
    }

    /**
     * 需要的数据类型
     */
    public static class DataBean {
        public float percent;
        public Integer color;
        public Integer yValue;
    }
}
