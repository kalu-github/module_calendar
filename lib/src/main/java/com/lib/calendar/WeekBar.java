package com.lib.calendar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.FrameLayout;

import java.util.Arrays;
import java.util.List;

/**
 * description: 星期 - 导航栏
 * created by kalu on 2018/5/23 22:30
 */
public final class WeekBar extends View {

    private final List<String> mData = Arrays.asList("日", "一", "二", "三", "四", "五", "六");
    private final Paint mPaint = new Paint();
    private CalendarDelegate mDelegate;

    public WeekBar(Context context) {
        super(context);
    }

    public WeekBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public WeekBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    void setSize(int width, int height) {
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(width, height);
        setLayoutParams(layoutParams);
    }

    /**
     * 传递属性
     *
     * @param delegate delegate
     */
    void setup(CalendarDelegate delegate) {
        this.mDelegate = delegate;
    }

    /**
     * 设置文本颜色，
     * 如果这里报错了，请确定你自定义XML文件跟布局是不是使用merge，而不是LinearLayout
     *
     * @param color color
     */
    void setTextColor(int color) {
    }

    /**
     * 日期选择事件，这里提供这个回调，可以方便定制WeekBar需要
     *
     * @param calendar calendar 选择的日期
     * @param isClick  isClick 点击
     */
    @SuppressWarnings("unused")
    protected void onDateSelected(Calendar calendar, boolean isClick) {

    }

    @Override
    protected void onDraw(Canvas canvas) {

        mPaint.setAntiAlias(true);
        mPaint.setFilterBitmap(true);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setFakeBoldText(false);
        mPaint.setTextSize(dp2px(getContext(), 16f));
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setTextAlign(Paint.Align.CENTER);

        float font = (mPaint.getFontMetrics().bottom - mPaint.getFontMetrics().top) / 3;

        final int width = getWidth() / 7;
        final int centerY = getHeight() / 2;
        for (int i = 0; i < 7; i++) {
            // mPaint.setColor(i == 0 || i == 6 ? Color.RED : Color.BLACK);
            mPaint.setColor(Color.BLACK);
            float x = width * i + width / 2;
            canvas.drawText(mData.get(i), x, centerY + font, mPaint);
        }

        final float line = dp2px(getContext(), 2f);
        mPaint.setColor(Color.parseColor("#66e6e6e6"));
        canvas.drawLine(0, line, getWidth(), line, mPaint);
        canvas.drawLine(0, getHeight() - line, getWidth(), getHeight(), mPaint);
    }

    private final float sp2px(Context context, float sp) {

        final DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, metrics);
    }

    private final float dp2px(Context context, float dp) {

        final DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return dp * metrics.density;
    }

    private final int dp2px(Context context, int dp) {

        final DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return (int) (dp * metrics.density);
    }
}
