package com.lib.calendar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import java.util.Arrays;
import java.util.List;

/**
 * description: 星期 - 导航栏
 * created by kalu on 2018/5/23 22:30
 */
public final class WeekBar extends View {

    private final List<String> mData = Arrays.asList("日", "一", "二", "三", "四", "五", "六");

    private int textColor = Color.BLACK;
    private float textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16f, getResources().getDisplayMetrics());

    public WeekBar(Context context) {
        super(context);
    }

    public WeekBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public WeekBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    @Override
    protected void onDraw(Canvas canvas) {


        final Paint textPaint = CalendarPaint.getTextPaint(textColor, textSize);
        float font = (textPaint.getFontMetrics().bottom - textPaint.getFontMetrics().top) / 3;

        final int width = getWidth() / 7;
        final int centerY = getHeight() / 2;
        for (int i = 0; i < 7; i++) {
            float x = width * i + width / 2;
            canvas.drawText(mData.get(i), x, centerY + font, textPaint);
        }

        final float line = 2f * getResources().getDisplayMetrics().density;
        textPaint.setColor(Color.parseColor("#66e6e6e6"));
        canvas.drawLine(0, line, getWidth(), line, textPaint);
        canvas.drawLine(0, getHeight() - line, getWidth(), getHeight(), textPaint);
    }
}
