package com.lib.calendar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * description: 月视图
 * created by kalu on 2018/5/23 22:30
 */
final class MonthView extends BaseCalendarView {

    // 圆点半径, 日期底部
    private float mDotRadius = (3 * getResources().getDisplayMetrics().density);

    public MonthView(Context context) {
        super(context);
    }

    @Override
    protected void onDrawSign(Canvas canvas, CalendarModel calendarModel, int left, int top, int itemWidth, int itemHeight) {

        final CalendarModel.SchemeModel schemeModel = calendarModel.getSchemeModel();
        if (null == schemeModel)
            return;

        // 1.底部圆点
        if (schemeModel.isWarning()) {
            final Paint dotPaint = CalendarPaint.getBackgroundPaint(CalendarPaint.RED);
            canvas.drawCircle(left + itemWidth * 0.5f, top + itemHeight * 0.95f, mDotRadius, dotPaint);
        }else{
            final Paint dotPaint = CalendarPaint.getBackgroundPaint(CalendarPaint.GREEN);
            canvas.drawCircle(left + itemWidth * 0.5f, top + itemHeight * 0.95f, mDotRadius, dotPaint);
        }
        // 2.右上角文字
        if (schemeModel.isDiagnose()) {
            final Paint lunarPaint = CalendarPaint.getTextPaint(CalendarPaint.RED, 24f);
            lunarPaint.setFakeBoldText(true);
            final float font = (lunarPaint.getFontMetrics().bottom - lunarPaint.getFontMetrics().top) * 0.3f;
            canvas.drawText(schemeModel.getScheme(), left + itemWidth * 0.82f, top + itemHeight * 0.18f + font, lunarPaint);
        }
    }

    @Override
    protected void onDrawText(Canvas canvas, CalendarModel calendarModel, int left, int top, int cx, int cy) {

        int color;
        if (calendarModel.isToady()) {
            color = CalendarPaint.WHITE;
        } else if (calendarModel.isSelect()) {
            color = CalendarPaint.WHITE;
        } else {
            color = calendarModel.isCurMonth() ? CalendarPaint.BLACK : CalendarPaint.GREY;
        }

        // 1.日期
        final Paint datePaint = CalendarPaint.getTextPaint(color, 40f);
        final Paint.FontMetrics dateMetrics = datePaint.getFontMetrics();
        final float dateFont = (dateMetrics.bottom - dateMetrics.top) * 0.08f;
        canvas.drawText(String.valueOf(calendarModel.getDay()), cx, cy - dateFont, datePaint);

        // 2.农历
        final Paint lunarPaint = CalendarPaint.getTextPaint(color, 25f);
        lunarPaint.setAlpha(155);
        final Paint.FontMetrics lunarMetrics = lunarPaint.getFontMetrics();
        final float lunarFont = (lunarMetrics.bottom - lunarMetrics.top) * 0.9f;
        canvas.drawText(calendarModel.getLunar(), cx, cy + lunarFont, lunarPaint);
    }

    @Override
    protected void onDrawBackground(Canvas canvas, CalendarModel calendarModel, int width, int height, int cx, int cy) {

        if (calendarModel.isToady()) {
            final float radius = Math.min(width, height) * 0.38f;
            final Paint dotPaint = CalendarPaint.getBackgroundPaint(CalendarPaint.GREEN);
            canvas.drawCircle(cx, cy, radius, dotPaint);
        }else if (calendarModel.isSelect()) {
            final float radius = Math.min(width, height) * 0.38f;
            final Paint dotPaint = CalendarPaint.getBackgroundPaint(CalendarPaint.GREY);
            canvas.drawCircle(cx, cy, radius, dotPaint);
        }
//        else if (calendarModel.isPress()) {
//            final float radius = Math.min(width, height) * 0.38f;
//            final Paint dotPaint = CalendarPaint.getBackgroundPaint(getContext().getApplicationContext(), CalendarPaint.PRESS);
//            canvas.drawCircle(cx, cy, radius, dotPaint);
//        }
    }
}
