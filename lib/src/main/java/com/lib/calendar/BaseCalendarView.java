package com.lib.calendar;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * description: 日历基类
 * created by kalu on 2018/6/9 10:52
 */
public abstract class BaseCalendarView extends View {

    private int daySelect, dayCur;
    private int yearSelect, yearCur;
    private int monthSelect, monthCur;
    protected final ArrayList<CalendarModel> mItems = new ArrayList<>();

    public BaseCalendarView(Context context) {
        super(context);
    }

    /********************************************************************/

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if (e.getPointerCount() > 1) return false;

        mSimpleOnGestureListener.onTouchEvent(e);
        return true;
    }

    private final GestureDetector mSimpleOnGestureListener = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {

        @Override
        public boolean onDown(MotionEvent e) {

            if (null != getTag()) {
                CalendarModel calendarModel1 = (CalendarModel) getTag();
                calendarModel1.setPress(false);
                setTag(null);
            }

            float indexX = e.getX() / (getWidth() / 7f);
            if (indexX >= 7) {
                indexX = 6;
            }
            int indexY = (int) ((int) e.getY() / (getHeight() / 6f));
            int number = (int) (indexY * 7 + indexX);// 选择项

            if (number > 0 && number < mItems.size()) {
                final CalendarModel calendarModel = mItems.get(number);
                calendarModel.setPress(true);
                setTag(calendarModel);
                postInvalidate();
            }

            return super.onDown(e);
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {

            final Object tag = getTag();
            if (null != tag) {

                final CalendarModel calendarModel = (CalendarModel) tag;
                calendarModel.setPress(false);
                postInvalidate();

                final int years = calendarModel.getYear();
                final int months = calendarModel.getMonth();
                final int day = calendarModel.getDay();
                final Calendar calendar = CalendarUtil.getCalendar();
                calendar.set(Calendar.YEAR, years);
                calendar.set(Calendar.MONTH, months - 1);//Java月份才0开始算
                final int maxdays = calendar.getActualMaximum(Calendar.DATE);

                if (null != mOnCalendarChangeListener) {
                    mOnCalendarChangeListener.onCalendarChange(years, months, day, maxdays, true, false);
                }
                setTag(null);
            }

            return super.onSingleTapUp(e);
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

            if (Math.abs(distanceY) > 10 && null != getTag()) {

                CalendarModel calendarModel1 = (CalendarModel) getTag();
                calendarModel1.setPress(false);

                float indexX = e1.getX() / (getWidth() / 7f);
                if (indexX >= 7) {
                    indexX = 6;
                }
                int indexY = (int) ((int) e1.getY() / (getHeight() / 6f));
                int number = (int) (indexY * 7 + indexX);// 选择项
                if (number > 0 && number < mItems.size()) {
                    final CalendarModel calendarModel = mItems.get(number);
                    if (calendarModel1 != calendarModel) {
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
                CalendarModel calendarModel = mItems.get(number);

                final int left = (column * itemWidth);
                final int top = (row * itemHeight);

                final int cx = left + itemWidth / 2;
                final int cy = top + itemHeight / 2;
                onDrawBackground(canvas, calendarModel, itemWidth, itemHeight, cx, cy);
                onDrawText(canvas, calendarModel, left, top, cx, cy);

                if (calendarModel.isSchemes()) {
                    onDrawSign(canvas, calendarModel, left, top, itemWidth, itemHeight);
                }

                ++number;
            }
        }
    }

    /***************************************************************************************************************************/

    final void setDate(int mYear, int mMonth, int mDay) {
        yearSelect = mYear;
        monthSelect = mMonth;
        daySelect = mDay;
    }

    final void calcuDate(int mYear, int mMonth) {
        Log.e("calcuDate", "year = " + mYear + ", month = " + mMonth);

        yearCur = Integer.parseInt(CalendarUtil.getYear());
        monthCur = Integer.parseInt(CalendarUtil.getMonth());
        dayCur = Integer.parseInt(CalendarUtil.getDay());

        final Calendar news = Calendar.getInstance();

        news.set(mYear, mMonth - 1, 1);
        int mPreDiff = news.get(Calendar.DAY_OF_WEEK) - 1;
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
            final CalendarModel calendarModelDate = new CalendarModel();
            int year, month, day;

            if (i < mPreDiff) {
                year = preYear;
                month = preMonth;
                day = preMonthDaysCount - mPreDiff + i + 1;
                calendarModelDate.setCurMonth(false);
            } else if (i >= mDayCount + mPreDiff) {
                year = nextYear;
                month = nextMonth;
                day = nextDay;
                ++nextDay;
                calendarModelDate.setCurMonth(false);
            } else {
                year = mYear;
                month = mMonth;
                day = i - mPreDiff + 1;
                calendarModelDate.setCurMonth(true);
            }

            // 初始化信息
            calendarModelDate.setYear(year);
            calendarModelDate.setMonth(month);
            calendarModelDate.setDay(day);
            // 初始化农历信息
            CalendarUtil.setupLunarCalendar(calendarModelDate);

            // 今天日子
            if (year == yearCur && month == monthCur && day == dayCur) {
                calendarModelDate.setToady(true);
            }
            // 选中日子
            if (year == yearSelect && month == monthSelect && day == daySelect) {
                calendarModelDate.setSelect(true);
            }
            mItems.add(calendarModelDate);
        }
        postInvalidate();
    }

    public List<CalendarModel> getModelList() {
        return mItems;
    }


    public int getYear() {
        return mItems.get(mItems.size() / 2).getYear();
    }

    public int getMonth() {
        return mItems.get(mItems.size() / 2).getMonth();
    }

    public int getDay() {
        final int year = mItems.get(mItems.size() / 2).getYear();
        final int month = mItems.get(mItems.size() / 2).getMonth();
        final Calendar calendar = CalendarUtil.getCalendar();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);//Java月份才0开始算
        int dateOfMonth = calendar.getActualMaximum(Calendar.DATE);
        return dateOfMonth;
    }

    /***************************************************************************************************************************/

    protected abstract void onDrawSign(Canvas canvas, CalendarModel calendarModel, int x, int y, int itemWidth, int itemHeight);


    protected abstract void onDrawText(Canvas canvas, CalendarModel calendarModel,
                                       int left, int top, int cx, int cy);

    protected abstract void onDrawBackground(Canvas canvas, CalendarModel calendarModel,
                                             int width, int height, int cx, int cy);

    /**************************************************************************************************************************/

    private CalendarLayout.OnCalendarChangeListener mOnCalendarChangeListener;

    public void setOnCalendarChangeListener(CalendarLayout.OnCalendarChangeListener listener) {
        this.mOnCalendarChangeListener = listener;
    }
}
