package com.lib.calendar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.util.TypedValue;

/**
 * description: 月视图
 * created by kalu on 2018/5/23 22:30
 */
public final class MonthView extends BaseView {

    // 圆点半径, 日期底部
    private float mDotRadius = dp2px(getContext(), 3);
    // 圆点底部, 内边距
    private float mDotPaddingBottom = dp2px(getContext(), 2);

    public MonthView(Context context) {
        super(context);
    }

    @Override
    protected void onDrawSign(Canvas canvas, Calendar calendar, int left, int top, int itemWidth, int itemHeight) {

//        final boolean isSelected = isSelected(calendar);
        final Paint dotPaint = CalendarPaint.getBackgroundPaint(CalendarPaint.BIUE);
        canvas.drawCircle(left + itemWidth / 2, top + itemHeight - 3 * mDotPaddingBottom, mDotRadius, dotPaint);
    }

    @Override
    protected void onDrawText(Canvas canvas, Calendar calendar, int left, int top, int cx, int cy) {

        int color;
        if (calendar.isToady()) {
            color = CalendarPaint.RED;
        } else if (calendar.isSelect()) {
            color = CalendarPaint.BIUE;
        } else {
            color = calendar.isCurMonth() ? CalendarPaint.BLACK : CalendarPaint.GRAAY;
        }

        final Paint datePaint = CalendarPaint.getTextPaint(color, CalendarPaint.DATE_TEXT_SIZE);
        final Paint.FontMetrics dateMetrics = datePaint.getFontMetrics();
        final float dateFont = (dateMetrics.bottom - dateMetrics.top) * 0.08f;
        canvas.drawText(String.valueOf(calendar.getDay()), cx, cy - dateFont, datePaint);

        final Paint lunarPaint = CalendarPaint.getTextPaint(color, CalendarPaint.LUNAR_TEXT_SIZE);
        lunarPaint.setAlpha(155);
        final Paint.FontMetrics lunarMetrics = lunarPaint.getFontMetrics();
        final float lunarFont = (lunarMetrics.bottom - lunarMetrics.top) * 0.9f;
        canvas.drawText(calendar.getLunar(), cx, cy + lunarFont, lunarPaint);
    }

    @Override
    protected void onDrawBackground(Canvas canvas, Calendar calendar, int width, int height, int cx, int cy) {

        // Log.e("onDrawBackground", " ==> " + calendar.getYear() + "-" + calendar.getMonth() + "-" + calendar.getDay() + ", isToday = " + calendar.isToady() + ", isSelect = " + calendar.isSelect());

        if (calendar.isToady()) {
            final float radius = Math.min(width, height) * 0.4f;
            final Paint dotPaint = CalendarPaint.getBackgroundPaint(CalendarPaint.GREEN);
            canvas.drawCircle(cx, cy, radius, dotPaint);
            // Log.e("onDrawBacSelect11", " ==> today = " + calendar.getDay());
        }

        if (calendar.isSelect()) {
            final float radius = Math.min(width, height) * 0.4f;
            final Paint dotPaint = CalendarPaint.getBackgroundPaint(CalendarPaint.ORANGE);
            canvas.drawCircle(cx, cy, radius, dotPaint);
            //   Log.e("onDrawBacSelect22", " ==> select = " + calendar.getDay());
        }

        if (calendar.isPress()) {
            final float radius = Math.min(width, height) * 0.4f;
            final Paint dotPaint = CalendarPaint.getBackgroundPaint(CalendarPaint.GRAAY);
            canvas.drawCircle(cx, cy, radius, dotPaint);
            //  Log.e("onDrawBackground", " ==> press = " + calendar.getDay());
        }
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
