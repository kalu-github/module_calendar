package com.lib.calendar;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.util.TypedValue;

/**
 * description: 画笔
 * created by kalu on 2017/11/12 3:05
 */
final class CalendarPaint {

    private final static Paint mPaint = new Paint();

    public static final int ORANGE = Color.parseColor("#ddff9e21");
    public static final int TRANSPARENT = Color.TRANSPARENT;
    public static final int WHITE = Color.WHITE;
    public static final int GREEN = Color.parseColor("#dd13ad67");
    public static final int GRAAY = Color.parseColor("#44666666");
    public static final int BLACK = Color.BLACK;
    public static final int BLACK_LUNAR = Color.parseColor("#ee000000");
    public static final int BIUE = Color.BLUE;
    public static final int GREY = Color.parseColor("#cccccc");
    public static final int RED = Color.RED;

    public static Paint getBackgroundPaint(Context context, int color) {

        mPaint.clearShadowLayer();
        mPaint.reset();

        mPaint.setAntiAlias(true);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setFakeBoldText(false);
        mPaint.setColor(color);
        mPaint.setAlpha(255);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setTextSize(0f);
        mPaint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10f, context.getResources().getDisplayMetrics()));
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        return mPaint;
    }

    public static Paint getTextPaint(int color, float textSize) {

        mPaint.clearShadowLayer();
        mPaint.reset();

        mPaint.setAntiAlias(true);
        mPaint.setAlpha(255);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setFakeBoldText(false);
        mPaint.setColor(color);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setTextSize(textSize);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        return mPaint;
    }
}
