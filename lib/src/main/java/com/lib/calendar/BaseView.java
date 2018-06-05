package com.lib.calendar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

/**
 * description: 基本的日历View，派生出MonthView
 * created by kalu on 2018/4/10 23:08
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
        LunarCalendar.setupLunarCalendar(mCalendarToday);
    }

    final void setSelectCalendar(int year, int month, int day) {
        mCalendarSelect.setYear(year);
        mCalendarSelect.setMonth(month);
        mCalendarSelect.setDay(day);
        LunarCalendar.setupLunarCalendar(mCalendarSelect);
    }

    /**
     * 字体大小
     */
    final float TEXT_SIZE = sp2px(getContext(), 14);
    /**
     * 当前月份日期的笔
     */
    protected Paint mCurMonthTextPaint = new Paint();

    /**
     * 其它月份日期颜色
     */
    protected Paint mOtherMonthTextPaint = new Paint();

    /**
     * 当前月份农历文本颜色
     */
    protected Paint mCurMonthLunarTextPaint = new Paint();


    /**
     * 当前月份农历文本颜色
     */
    protected Paint mSelectedLunarTextPaint = new Paint();

    /**
     * 其它月份农历文本颜色
     */
    protected Paint mOtherMonthLunarTextPaint = new Paint();

    /**
     * 其它月份农历文本颜色
     */
    protected Paint mSchemeLunarTextPaint = new Paint();

    /**
     * 标记的日期背景颜色画笔
     */
    protected Paint mSchemePaint = new Paint();

    /**
     * 被选择的日期背景色
     */
    protected Paint mSelectedPaint = new Paint();

    /**
     * 标记的文本画笔
     */
    protected Paint mSchemeTextPaint = new Paint();

    /**
     * 选中的文本画笔
     */
    protected Paint mSelectTextPaint = new Paint();

    /**
     * 当前日期文本颜色画笔
     */
    protected Paint mCurDayTextPaint = new Paint();

    /**
     * 当前日期文本颜色画笔
     */
    protected Paint mCurDayLunarTextPaint = new Paint();

    /********************************************************************/

    public BaseView(Context context) {
        this(context, null);
    }

    public BaseView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mCurMonthTextPaint.setAntiAlias(true);
        mCurMonthTextPaint.setTextAlign(Paint.Align.CENTER);
        mCurMonthTextPaint.setColor(0xFF111111);
        mCurMonthTextPaint.setFakeBoldText(true);
        mCurMonthTextPaint.setTextSize(TEXT_SIZE);

        mOtherMonthTextPaint.setAntiAlias(true);
        mOtherMonthTextPaint.setTextAlign(Paint.Align.CENTER);
        mOtherMonthTextPaint.setColor(0xFFe1e1e1);
        mOtherMonthTextPaint.setFakeBoldText(true);
        mOtherMonthTextPaint.setTextSize(TEXT_SIZE);

        mCurMonthLunarTextPaint.setAntiAlias(true);
        mCurMonthLunarTextPaint.setTextAlign(Paint.Align.CENTER);

        mSelectedLunarTextPaint.setAntiAlias(true);
        mSelectedLunarTextPaint.setTextAlign(Paint.Align.CENTER);

        mOtherMonthLunarTextPaint.setAntiAlias(true);
        mOtherMonthLunarTextPaint.setTextAlign(Paint.Align.CENTER);


        mSchemeLunarTextPaint.setAntiAlias(true);
        mSchemeLunarTextPaint.setTextAlign(Paint.Align.CENTER);

        mSchemeTextPaint.setAntiAlias(true);
        mSchemeTextPaint.setStyle(Paint.Style.FILL);
        mSchemeTextPaint.setTextAlign(Paint.Align.CENTER);
        mSchemeTextPaint.setColor(0xffed5353);
        mSchemeTextPaint.setFakeBoldText(true);
        mSchemeTextPaint.setTextSize(TEXT_SIZE);

        mSelectTextPaint.setAntiAlias(true);
        mSelectTextPaint.setStyle(Paint.Style.FILL);
        mSelectTextPaint.setTextAlign(Paint.Align.CENTER);
        mSelectTextPaint.setColor(0xffed5353);
        mSelectTextPaint.setFakeBoldText(true);
        mSelectTextPaint.setTextSize(TEXT_SIZE);

        mSchemePaint.setAntiAlias(true);
        mSchemePaint.setStyle(Paint.Style.FILL);
        mSchemePaint.setStrokeWidth(2);
        mSchemePaint.setColor(0xffefefef);

        mCurDayTextPaint.setAntiAlias(true);
        mCurDayTextPaint.setTextAlign(Paint.Align.CENTER);
        mCurDayTextPaint.setColor(Color.RED);
        mCurDayTextPaint.setFakeBoldText(true);
        mCurDayTextPaint.setTextSize(TEXT_SIZE);

        mCurDayLunarTextPaint.setAntiAlias(true);
        mCurDayLunarTextPaint.setTextAlign(Paint.Align.CENTER);
        mCurDayLunarTextPaint.setColor(Color.RED);
        mCurDayLunarTextPaint.setFakeBoldText(true);
        mCurDayLunarTextPaint.setTextSize(TEXT_SIZE);

        mSelectedPaint.setAntiAlias(true);
        mSelectedPaint.setStyle(Paint.Style.FILL);
        mSelectedPaint.setStrokeWidth(2);
    }

    /********************************************************************/

    CalendarDelegate mDelegate;

    void setup(CalendarDelegate delegate) {
        this.mDelegate = delegate;

        this.mCurDayTextPaint.setColor(delegate.getCurDayTextColor());
        this.mCurDayLunarTextPaint.setColor(delegate.getCurDayLunarTextColor());
        this.mCurMonthTextPaint.setColor(delegate.getCurrentMonthTextColor());
        this.mOtherMonthTextPaint.setColor(delegate.getOtherMonthTextColor());
        this.mCurMonthLunarTextPaint.setColor(delegate.getCurrentMonthLunarTextColor());
        this.mSelectedLunarTextPaint.setColor(delegate.getSelectedLunarTextColor());
        this.mSelectTextPaint.setColor(delegate.getSelectedTextColor());
        this.mOtherMonthLunarTextPaint.setColor(delegate.getOtherMonthLunarTextColor());
        this.mSchemeLunarTextPaint.setColor(delegate.getSchemeLunarTextColor());

        this.mSchemePaint.setColor(delegate.getSchemeThemeColor());
        this.mSchemeTextPaint.setColor(delegate.getSchemeTextColor());


        this.mCurMonthTextPaint.setTextSize(delegate.getDayTextSize());
        this.mOtherMonthTextPaint.setTextSize(delegate.getDayTextSize());
        this.mCurDayTextPaint.setTextSize(delegate.getDayTextSize());
        this.mSchemeTextPaint.setTextSize(delegate.getDayTextSize());
        this.mSelectTextPaint.setTextSize(delegate.getDayTextSize());

        this.mCurMonthLunarTextPaint.setTextSize(delegate.getLunarTextSize());
        this.mSelectedLunarTextPaint.setTextSize(delegate.getLunarTextSize());
        this.mCurDayLunarTextPaint.setTextSize(delegate.getLunarTextSize());
        this.mOtherMonthLunarTextPaint.setTextSize(delegate.getLunarTextSize());
        this.mSchemeLunarTextPaint.setTextSize(delegate.getLunarTextSize());

        this.mSelectedPaint.setStyle(Paint.Style.FILL);
        this.mSelectedPaint.setColor(delegate.getSelectedThemeColor());
    }

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

                if (calendar.hasScheme()) {
                    mSchemePaint.setColor(calendar.getSchemeColor() != 0 ? calendar.getSchemeColor() : mDelegate.getSchemeThemeColor());
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
        int mDayCount = Util.getMonthDaysCount(mYear, mMonth);
        news.set(mYear, mMonth - 1, mDayCount);

        int preYear, preMonth;
        int nextYear, nextMonth;

        int preMonthDaysCount;
        if (mMonth == 1) {//如果是1月
            preYear = mYear - 1;
            preMonth = 12;
            nextYear = mYear;
            nextMonth = mMonth + 1;
            preMonthDaysCount = mPreDiff == 0 ? 0 : Util.getMonthDaysCount(preYear, preMonth);
        } else if (mMonth == 12) {//如果是12月
            preYear = mYear;
            preMonth = mMonth - 1;
            nextYear = mYear + 1;
            nextMonth = 1;
            preMonthDaysCount = mPreDiff == 0 ? 0 : Util.getMonthDaysCount(preYear, preMonth);
        } else {//平常
            preYear = mYear;
            preMonth = mMonth - 1;
            nextYear = mYear;
            nextMonth = mMonth + 1;
            preMonthDaysCount = mPreDiff == 0 ? 0 : Util.getMonthDaysCount(preYear, preMonth);
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
            LunarCalendar.setupLunarCalendar(calendarDate);

            // 今天日子
            if (year == mCalendarToday.getYear() && month == mCalendarToday.getMonth() && day == mCalendarToday.getDay()) {
                calendarDate.setToady(true);
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
