package com.lib.calendar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * description: 月视图
 * created by kalu on 2018/5/23 22:30
 */
public final class MonthView extends BaseView {

    // 圆点半径, 日期底部
    private float mDotRadius = (3 * getResources().getDisplayMetrics().density);
    // 圆点底部, 内边距
    private float mDotPaddingBottom = (2 * getResources().getDisplayMetrics().density);

    public MonthView(Context context) {
        super(context);
    }

    @Override
    protected void onDrawSign(Canvas canvas, Calendar calendar, int left, int top, int itemWidth, int itemHeight) {

//        final boolean isSelected = isSelected(calendar);
        final Paint dotPaint = CalendarPaint.getBackgroundPaint(getContext().getApplicationContext(), CalendarPaint.BIUE);
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

        // 1.日期
        final Paint datePaint = CalendarPaint.getTextPaint(getContext().getApplicationContext(), color, 40f);
        final Paint.FontMetrics dateMetrics = datePaint.getFontMetrics();
        final float dateFont = (dateMetrics.bottom - dateMetrics.top) * 0.08f;
        canvas.drawText(String.valueOf(calendar.getDay()), cx, cy - dateFont, datePaint);

        // 2.农历
        final Paint lunarPaint = CalendarPaint.getTextPaint(getContext().getApplicationContext(), color, 25f);
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
            final Paint dotPaint = CalendarPaint.getBackgroundPaint(getContext().getApplicationContext(), CalendarPaint.GREEN);
            canvas.drawCircle(cx, cy, radius, dotPaint);
            // Log.e("onDrawBacSelect11", " ==> today = " + calendar.getDay());
        }

        if (calendar.isSelect()) {
            final float radius = Math.min(width, height) * 0.4f;
            final Paint dotPaint = CalendarPaint.getBackgroundPaint(getContext().getApplicationContext(), CalendarPaint.ORANGE);
            canvas.drawCircle(cx, cy, radius, dotPaint);
            //   Log.e("onDrawBacSelect22", " ==> select = " + calendar.getDay());
        }

        if (calendar.isPress()) {
            final float radius = Math.min(width, height) * 0.4f;
            final Paint dotPaint = CalendarPaint.getBackgroundPaint(getContext().getApplicationContext(), CalendarPaint.GRAAY);
            canvas.drawCircle(cx, cy, radius, dotPaint);
            //  Log.e("onDrawBackground", " ==> press = " + calendar.getDay());
        }
    }
}
