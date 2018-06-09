package com.lib.calendar;

import android.content.Context;
import android.graphics.Canvas;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

/**
 * description: 日历基类
 * created by kalu on 2018/6/9 10:52
 */
public abstract class BaseView extends View {

    protected final ArrayList<Calendar> mItems = new ArrayList<>();

    protected final Calendar mCalendarSelect = new Calendar();
    protected final Calendar mCalendarToday = new Calendar();

    {
        final java.util.Calendar instance = java.util.Calendar.getInstance();
        mCalendarToday.setYear(instance.get(java.util.Calendar.YEAR));
        mCalendarToday.setMonth(instance.get(java.util.Calendar.MONTH) + 1);
        mCalendarToday.setDay(instance.get(java.util.Calendar.DAY_OF_MONTH));
        CalendarUtil.setupLunarCalendar(mCalendarToday);
    }

    public BaseView(Context context) {
        super(context);
    }

    final void setSelectCalendar(int year, int month, int day) {
        mCalendarSelect.setYear(year);
        mCalendarSelect.setMonth(month);
        mCalendarSelect.setDay(day);
        CalendarUtil.setupLunarCalendar(mCalendarSelect);
    }

    /********************************************************************/

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if (e.getPointerCount() > 1) return false;

        mSimpleOnGestureListener.onTouchEvent(e);

        switch (e.getAction()) {
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if (null != getTag()) {
                    Calendar calendar1 = (Calendar) getTag();
                    calendar1.setPress(false);
                }
                setTag(null);
                postInvalidate();
                break;
        }
        return true;
    }

    private final GestureDetector mSimpleOnGestureListener = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {


        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {

            if (null != getTag()) {
                Calendar calendar1 = (Calendar) getTag();
                calendar1.setPress(false);
            }

            float indexX = e.getX() / (getWidth() / 7f);
            if (indexX >= 7) {
                indexX = 6;
            }
            int indexY = (int) ((int) e.getY() / (getHeight() / 6f));
            int number = (int) (indexY * 7 + indexX);// 选择项

            if (number > 0 && number < mItems.size()) {
                final Calendar calendar = mItems.get(number);
                calendar.setPress(true);
                setTag(calendar);
                postInvalidate();
            }

            return super.onSingleTapConfirmed(e);
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

            if (Math.abs(distanceY) > 10 && null != getTag()) {

                Calendar calendar1 = (Calendar) getTag();
                calendar1.setPress(false);

                float indexX = e1.getX() / (getWidth() / 7f);
                if (indexX >= 7) {
                    indexX = 6;
                }
                int indexY = (int) ((int) e1.getY() / (getHeight() / 6f));
                int number = (int) (indexY * 7 + indexX);// 选择项
                if (number > 0 && number < mItems.size()) {
                    final Calendar calendar = mItems.get(number);
                    if (calendar1 != calendar) {
                        setTag(null);
                        postInvalidate();
                    }
                }
            }

            return super.onScroll(e1, e2, distanceX, distanceY);
        }
    });

    /***********************************************************************************************************************/

    @Override
    protected void onDraw(Canvas canvas) {

        if (mItems.isEmpty()) return;

        final int itemWidth = (int) (getWidth() / 7f);
        final int itemHeight = (int) (getHeight() / 6f);

        int number = 0;
        // 6行
        for (int row = 0; row < 6; row++) {
            // 7列
            for (int column = 0; column < 7; column++) {

                if (number >= 42) return;
                Calendar calendar = mItems.get(number);

                final int left = (column * itemWidth);
                final int top = (row * itemHeight);

                final int cx = left + itemWidth / 2;
                final int cy = top + itemHeight / 2;
                onDrawBackground(canvas, calendar, itemWidth, itemHeight, cx, cy);
                onDrawText(canvas, calendar, left, top, cx, cy);

                if (calendar.isSchemes()) {
                    onDrawSign(canvas, calendar, left, top, itemWidth, itemHeight);
                }

                ++number;
            }
        }
    }

    /***************************************************************************************************************************/

    final void setDate(int mYear, int mMonth) {

        final java.util.Calendar news = java.util.Calendar.getInstance();

        news.set(mYear, mMonth - 1, 1);
        int mPreDiff = news.get(java.util.Calendar.DAY_OF_WEEK) - 1;
        int mDayCount = CalendarUtil.getMonthDaysCount(mYear, mMonth);
        news.set(mYear, mMonth - 1, mDayCount);

        int preYear, preMonth;
        int nextYear, nextMonth;

        int preMonthDaysCount;
        if (mMonth == 1) {//如果是1月
            preYear = mYear - 1;
            preMonth = 12;
            nextYear = mYear;
            nextMonth = mMonth + 1;
            preMonthDaysCount = mPreDiff == 0 ? 0 : CalendarUtil.getMonthDaysCount(preYear, preMonth);
        } else if (mMonth == 12) {//如果是12月
            preYear = mYear;
            preMonth = mMonth - 1;
            nextYear = mYear + 1;
            nextMonth = 1;
            preMonthDaysCount = mPreDiff == 0 ? 0 : CalendarUtil.getMonthDaysCount(preYear, preMonth);
        } else {//平常
            preYear = mYear;
            preMonth = mMonth - 1;
            nextYear = mYear;
            nextMonth = mMonth + 1;
            preMonthDaysCount = mPreDiff == 0 ? 0 : CalendarUtil.getMonthDaysCount(preYear, preMonth);
        }
        int nextDay = 1;
        mItems.clear();

        // 每页显示42个日期
        for (int i = 0; i < 42; i++) {
            final Calendar calendarDate = new Calendar();
            int year, month, day;

            if (i < mPreDiff) {
                year = preYear;
                month = preMonth;
                day = preMonthDaysCount - mPreDiff + i + 1;
                calendarDate.setCurMonth(false);
            } else if (i >= mDayCount + mPreDiff) {
                year = nextYear;
                month = nextMonth;
                day = nextDay;
                ++nextDay;
                calendarDate.setCurMonth(false);
            } else {
                year = mYear;
                month = mMonth;
                day = i - mPreDiff + 1;
                calendarDate.setCurMonth(true);
            }

            // 初始化信息
            calendarDate.setYear(year);
            calendarDate.setMonth(month);
            calendarDate.setDay(day);
            // 初始化农历信息
            CalendarUtil.setupLunarCalendar(calendarDate);

            // 今天日子
            if (year == mCalendarToday.getYear() && month == mCalendarToday.getMonth() && day == mCalendarToday.getDay()) {
                calendarDate.setToady(true);
                calendarDate.setSchemes(true);
                calendarDate.setSchemesStr("诊");
            }
            // 选中日子
            if (year == mCalendarSelect.getYear() && month == mCalendarSelect.getMonth() && day == mCalendarSelect.getDay()) {
                calendarDate.setSelect(true);
            }
            mItems.add(calendarDate);
        }
        postInvalidate();
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

    /***************************************************************************************************************************/

    protected abstract void onDrawSign(Canvas canvas, Calendar calendar, int x, int y, int itemWidth, int itemHeight);


    protected abstract void onDrawText(Canvas canvas, Calendar calendar,
                                       int left, int top, int cx, int cy);

    protected abstract void onDrawBackground(Canvas canvas, Calendar calendar,
                                             int width, int height, int cx, int cy);
    /**************************************************************************************************************************/
}
