package com.renj.percentchart;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * ======================================================================
 * <p>
 * 作者：Renj
 * 邮箱：renjunhua@anlovek.com
 * <p>
 * 创建时间：2018-12-14   13:25
 * <p>
 * 描述：按百分比填充颜色分配图表
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
    private List<PercentChartEntity> percentChartEntities = new ArrayList<>();
    // 网格线画笔
    private Paint linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    // 标签画笔
    private Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    // 背景色画笔
    private Paint colorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private float xLabelsHeight = 50;     // x 轴方向标签高度
    private float xLabelPadding = 6;      // x 轴标签名距离x轴的距离
    private float xOffsetWidth = 20;      // x 方向偏移量，用于保证最后边一个文字不会偏移出控件范围

    private float yLabelsWidth = 60;      // y 轴方向标签宽度
    private float yLabelPadding = 6;      // y 轴标签名距离y轴的距离
    private float yOffsetHeight = 15;      // y 方向偏移量，用于保证最上边一个文字不会偏移出控件范围

    private boolean isDrawXGrid = true;   // 是否绘制x方向网格线
    private float xAxisWidth = 1;         // x轴线宽度
    private int xAxisColor = 0xFF111111;  // x轴线颜色
    private float xGridWidth = 0.5f;      // x轴方向网格线宽度
    private int xGridColor = 0xFFDDDDDD;  // x轴方向网格线颜色

    private float xLabelSize = 14;        // x 轴上标签文字大小
    private int xLabelColor = 0xFF111111; // x 轴上标签文字颜色

    private boolean isDrawYGrid = true;  // 是否绘制y方向网格线
    private float yAxisWidth = 1;         // y轴线宽度
    private int yAxisColor = 0xFF111111;  // y轴线颜色
    private float yGridWidth = 0.5f;        // y轴方向网格线宽度
    private int yGridColor = 0xFFDDDDDD;  // y轴方向网格线颜色

    private float yLabelSize = 14;        // y 轴上标签文字大小
    private int yLabelColor = 0xFF111111; // y 轴上标签文字颜色

    private boolean isAnimation = true;   // 是否需要动画，默认有
    private long animationDuration = 1200; // 动画时间，单位 ms
    private float percentValue = 0;
    private ValueAnimator valueAnimator;


    public PercentChartView(Context context) {
        this(context, null);
    }

    public PercentChartView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PercentChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public PercentChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        colorPaint.setStyle(Paint.Style.FILL);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.PercentChartView);

        xLabelsHeight = typedArray.getDimension(R.styleable.PercentChartView_x_labelsHeight, xLabelsHeight);
        xLabelPadding = typedArray.getDimension(R.styleable.PercentChartView_x_labelPadding, xLabelPadding);
        xOffsetWidth = typedArray.getDimension(R.styleable.PercentChartView_x_offsetWidth, xOffsetWidth);

        yLabelsWidth = typedArray.getDimension(R.styleable.PercentChartView_y_labelsWidth, yLabelsWidth);
        yLabelPadding = typedArray.getDimension(R.styleable.PercentChartView_y_labelPadding, yLabelPadding);
        yOffsetHeight = typedArray.getDimension(R.styleable.PercentChartView_y_offsetHeight, yOffsetHeight);

        isDrawXGrid = typedArray.getBoolean(R.styleable.PercentChartView_isDrawXGrid, isDrawXGrid);
        xAxisWidth = typedArray.getDimension(R.styleable.PercentChartView_x_axisWidth, xAxisWidth);
        xAxisColor = typedArray.getColor(R.styleable.PercentChartView_x_axisColor, xAxisColor);
        xGridWidth = typedArray.getDimension(R.styleable.PercentChartView_x_gridWidth, xGridWidth);
        xGridColor = typedArray.getColor(R.styleable.PercentChartView_x_gridColor, xGridColor);

        isDrawYGrid = typedArray.getBoolean(R.styleable.PercentChartView_isDrawYGrid, isDrawYGrid);
        yAxisWidth = typedArray.getDimension(R.styleable.PercentChartView_y_axisWidth, yAxisWidth);
        yAxisColor = typedArray.getColor(R.styleable.PercentChartView_y_axisColor, yAxisColor);
        yGridWidth = typedArray.getDimension(R.styleable.PercentChartView_y_gridWidth, yGridWidth);
        yGridColor = typedArray.getColor(R.styleable.PercentChartView_y_gridColor, yGridColor);

        xLabelSize = typedArray.getDimension(R.styleable.PercentChartView_x_labelSize, xLabelSize);
        xLabelColor = typedArray.getColor(R.styleable.PercentChartView_x_labelColor, xLabelColor);

        yLabelSize = typedArray.getDimension(R.styleable.PercentChartView_y_labelSize, yLabelSize);
        yLabelColor = typedArray.getColor(R.styleable.PercentChartView_y_labelColor, yLabelColor);

        isAnimation = typedArray.getBoolean(R.styleable.PercentChartView_is_need_animation, isAnimation);
        animationDuration = typedArray.getInteger(R.styleable.PercentChartView_animation_duration, (int) animationDuration);

        typedArray.recycle();

        if (animationType == ANIMATION_TYPE_X || animationType == ANIMATION_TYPE_Y || animationType == ANIMATION_TYPE_ALL) {
            initAnimation();
            valueAnimator.setDuration(animationDuration);
        }

    }

    /*=========【start】 setter and getter 【start】=========*/

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
    public List<PercentChartEntity> getChartData() {
        return percentChartEntities;
    }

    /**
     * 设置数据
     *
     * @param percentChartEntities
     */
    public void setChartData(List<PercentChartEntity> percentChartEntities) {
        this.percentChartEntities = percentChartEntities;
        if (isAnimation) {
            if (valueAnimator == null) initAnimation();
            if (valueAnimator.isRunning()) valueAnimator.cancel();
            valueAnimator.setDuration(animationDuration);
            valueAnimator.start();
        } else {
            percentValue = 1;
            this.postInvalidate();
        }
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
        return yOffsetHeight;
    }

    /**
     * 设置 y 方向偏移量，用于保证文字居中，最上边一个文字也不会偏移出控件范围
     *
     * @param yOffsetWidth
     */
    public void setYOffsetWidth(float yOffsetWidth) {
        this.yOffsetHeight = yOffsetWidth;
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
    public float getXAxisWidth() {
        return xAxisWidth;
    }

    /**
     * 设置 x 轴线宽度
     *
     * @param xAxisWidth
     */
    public void setXAxisWidth(float xAxisWidth) {
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
    public float getYAxisWidth() {
        return yAxisWidth;
    }

    /**
     * 设置 y 轴线宽度
     *
     * @param yAxisWidth
     */
    public void setYAxisWidth(float yAxisWidth) {
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
    public float getYGridWidth() {
        return yGridWidth;
    }

    /**
     * 设置 y 轴方向网格线宽度
     *
     * @param yGridWidth
     */
    public void setYGridWidth(float yGridWidth) {
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

    /**
     * 动画效果/类型
     *
     * @return 动画效果/类型  0/none：不需要动画 1/x：沿x轴方向动画 2/y：沿y轴方向动画 3/all：x轴和y轴方向动画，默认 2/y
     */
    public int getAnimationType() {
        return animationType;
    }

    /**
     * 设置动画效果/类型
     *
     * @param animationType 动画效果/类型  0/none：不需要动画 1/x：沿x轴方向动画 2/y：沿y轴方向动画 3/all：x轴和y轴方向动画，默认 2/y
     */
    public void setAnimation(boolean animation) {
        this.isAnimation = animation;
        if (isAnimation && valueAnimator == null)
            initAnimation();
    }

    /**
     * 获取动画时间，单位：ms
     *
     * @return
     */
    public long getAnimationDuration() {
        return animationDuration;
    }

    /**
     * 设置动画时间，单位：ms
     *
     * @param animationDuration
     */
    public void setAnimationDuration(long animationDuration) {
        this.animationDuration = animationDuration;
        if (valueAnimator != null)
            valueAnimator.setDuration(animationDuration);
    }

    /**
     * 重绘页面
     */
    public void invalidateView() {
        this.invalidate();
    }

    /**
     * 重绘页面
     */
    public void postInvalidateView() {
        this.postInvalidate();
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
        float left = yLabelsWidth;

        int size = yAxisLabels.size();
        float spacing = (measuredHeight - xLabelsHeight - yOffsetHeight) * 1.0f / (size - 1);
        float bottom = measuredHeight - xLabelsHeight;
        for (PercentChartEntity percentChartEntity : percentChartEntities) {
            float right = left + percentChartEntity.percent * (measuredWidth - yLabelsWidth - xOffsetWidth);
            float top = bottom - (yAxisLabels.size() - 1 - percentChartEntity.yValue) * spacing * percentValue;
            colorPaint.setColor(percentChartEntity.color);
            canvas.drawRect(left, top, right, bottom, colorPaint);
            left = right;
        }
    }

    /**
     * 初始化动画
     */
    private void initAnimation() {
        valueAnimator = ValueAnimator.ofFloat(0, 1);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                percentValue = (float) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        valueAnimator.setDuration(animationDuration);
    }

    /**
     * 绘制y轴向标签
     *
     * @param measuredWidth
     * @param measuredHeight
     * @param canvas
     */
    private void drawYText(int measuredWidth, int measuredHeight, Canvas canvas) {
        textPaint.setTextSize(yLabelSize);
        textPaint.setColor(yLabelColor);

        int size = yAxisLabels.size();
        float spacing = (measuredHeight - xLabelsHeight - yOffsetHeight) * 1.0f / (size - 1);
        for (int i = 0; i < size; i++) {
            String text = yAxisLabels.get(size - 1 - i);
            Rect textRect = measureTextSize(text);
            canvas.drawText(text, yLabelsWidth - textRect.width() - yLabelPadding, i * spacing + yOffsetHeight + textRect.height() / 2, textPaint);
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
        textPaint.setTextSize(xLabelSize);
        textPaint.setColor(xLabelColor);

        int size = xAxisLabels.size();
        float spacing = (measuredWidth - yLabelsWidth - xOffsetWidth) * 1.0f / (size - 1);
        for (int i = 0; i < size; i++) {
            String text = xAxisLabels.get(i);
            Rect textRect = measureTextSize(text);
            float startX = i * spacing + yLabelsWidth - textRect.width() / 2;
            canvas.drawText(text, startX, measuredHeight - (xLabelsHeight - textRect.height() - xLabelPadding), textPaint);
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
            float spacing = (measuredHeight - xLabelsHeight - yOffsetHeight) * 1.0f / (size - 1);
            for (int i = 0; i < size - 1; i++) {
                float yValue = i * spacing + yOffsetHeight;
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
        canvas.drawLine(yLabelsWidth, yOffsetHeight, yLabelsWidth, measuredHeight - xLabelsHeight, linePaint);
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
                canvas.drawLine(xValue, yOffsetHeight, xValue, measuredHeight - xLabelsHeight, linePaint);
            }
        }
    }

    /**
     * 需要的数据类型
     */
    public static class PercentChartEntity {
        public float percent;
        public Integer color;
        public Integer yValue;
    }
}
