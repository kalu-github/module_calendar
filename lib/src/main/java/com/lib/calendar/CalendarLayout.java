package com.lib.calendar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.lang.reflect.Constructor;

/**
 * description: 日历布局
 * created by kalu on 2018/4/10 23:09
 */
public final class CalendarLayout extends LinearLayout {

    private final CalendarDelegate mDelegate = new CalendarDelegate();
    private final PagerLayoutManager mPagerLayoutManager = new PagerLayoutManager(getContext().getApplicationContext(), LinearLayout.HORIZONTAL, false);

    private int selectYear = -1, selectMonth = -1, selectDay = -1;

    public CalendarLayout(@NonNull Context context) {
        this(context, null);
    }

    public CalendarLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mDelegate.setAttributeSet(context, attrs);
        setOrientation(LinearLayout.VERTICAL);

        TypedArray typed = null;
        try {
            typed = context.obtainStyledAttributes(attrs, R.styleable.CalendarLayout);
            selectYear = typed.getInt(R.styleable.CalendarLayout_cl_select_year, selectYear);
            selectMonth = typed.getInt(R.styleable.CalendarLayout_cl_select_month, selectMonth);
            selectDay = typed.getInt(R.styleable.CalendarLayout_cl_select_day, selectDay);
        } catch (Exception e) {
            Log.e("", e.getMessage(), e);
        } finally {
            if (null == typed) return;
            typed.recycle();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                getParent().requestDisallowInterceptTouchEvent(false);
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onFinishInflate() {

        // 1.星期栏
        if (TextUtils.isEmpty(mDelegate.getWeekBarClass())) {
            final WeekBar weekBar = new WeekBar(getContext());
            LinearLayout.LayoutParams weekBarParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp2px(getContext(), 40));
            weekBar.setLayoutParams(weekBarParams);
            addView(weekBar);
        } else {
            try {
                Class cls = Class.forName(mDelegate.getWeekBarClass());
                Constructor constructor = cls.getConstructor(Context.class);
                final WeekBar weekBar = (WeekBar) constructor.newInstance(getContext());
                addView(weekBar);
            } catch (Exception e) {
                Log.e("", e.getMessage(), e);
            }
        }

        // 2.日期
        final RecyclerView recyclerView = new RecyclerView(getContext().getApplicationContext());
        recyclerView.setBackgroundColor(Color.WHITE);
        LinearLayout.LayoutParams paramsRecyclerView = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        paramsRecyclerView.setMargins(10, 10, 10, 10);
        recyclerView.setLayoutParams(paramsRecyclerView);
        addView(recyclerView);

        recyclerView.setLayoutManager(mPagerLayoutManager);
        recyclerView.setAdapter(new CalendarAdapter());
        mPagerLayoutManager.setOnPagerChangeListener(new PagerLayoutManager.OnPagerChangeListener() {

            @Override
            public void onPageSelect(int position, boolean isTop, boolean isBottom) {
                final int year = (position + mDelegate.getMinYearMonth() - 1) / 12 + mDelegate.getMinYear();
                final int month = ((position + mDelegate.getMinYearMonth() - 1) % 12 + 1);

                Log.e("onCalendarChange", "position = " + position + ", year = " + year + ", month = " + month + ", isTop = " + isTop + ", isBottom = " + isBottom);

//                Calendar calendar = new Calendar();
//                calendar.setYear();
//                calendar.setMonth((position + mDelegate.getMinYearMonth() - 1) % 12 + 1);
//                calendar.setDay(1);
//                calendar.setCurrentMonth(calendar.getYear() == mDelegate.getTodayCalendar().getYear() &&
//                        calendar.getMonth() == mDelegate.getTodayCalendar().getMonth());
//                calendar.setSelect(calendar.equals(mDelegate.getTodayCalendar()));
//                LunarCalendar.setupLunarCalendar(calendar);

//                if (!calendar.isCurrentMonth()) {
//                    mDelegate.mSelectedCalendar = calendar;
//                } else {
//                    mDelegate.mSelectedCalendar = mDelegate.getTodayCalendar();
//                }

                if (null == mOnCalendarChangeListener) return;
                mOnCalendarChangeListener.onCalendarChange(year, month, 1, false);
            }

            @Override
            public void onPageDetach(boolean isNext, int position) {
            }

            @Override
            public void onPageFinish() {
            }
        });

        super.onFinishInflate();
    }

    public void setSelectDate(int year, int month, int day) {
        selectYear = year;
        selectMonth = month;
        selectDay = day;

        final int minYear = mDelegate.getMinYear();
        final int minYearMonth = mDelegate.getMinYearMonth();
//        Log.e("onCalendarChange", "minYear = " + minYear + ", minYearMonth = " + minYearMonth);
//        Log.e("onCalendarChange", "year = " + year + ", month = " + month);

        if (year < minYear) {
            year = minYear;
        }

        if (year == minYear) {
            final int position = month - 1;
            mPagerLayoutManager.scrollToPositionWithOffset(position, 0);
            mPagerLayoutManager.setStackFromEnd(true);
            if (null != mOnCalendarChangeListener) {
                mOnCalendarChangeListener.onCalendarChange(year, month, day, false);
            }
        } else {
            int position;
            if (minYearMonth == 1) {
                position = 12 * (year - minYear - 1) + 12 + month;
            } else {
                position = 12 * (year - minYear - 1) + (12 - minYearMonth) + month;
            }

            //  Log.e("onCalendarChange22", "position = " + position);
            mPagerLayoutManager.scrollToPositionWithOffset(position, 0);
            mPagerLayoutManager.setStackFromEnd(true);
            if (null != mOnCalendarChangeListener) {
                mOnCalendarChangeListener.onCalendarChange(year, month, day, false);
            }
        }
    }

    public final class CalendarAdapter extends RecyclerView.Adapter {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            try {
                final String className = mDelegate.getMonthViewClass();
                if (TextUtils.isEmpty(className)) {
                    final BaseView view = new MonthView(getContext().getApplicationContext());
                    view.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                    return new RecyclerHolder(view);
                } else {
                    final Class cls = Class.forName(className);
                    final Constructor constructor = cls.getConstructor(Context.class);
                    final BaseView view = (BaseView) constructor.newInstance(getContext());
                    view.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                    return new RecyclerHolder(view);
                }
            } catch (Exception e) {
                Log.e("", e.getMessage(), e);
                final BaseView view = new MonthView(getContext().getApplicationContext());
                view.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                return new RecyclerHolder(view);
            }
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

//            if (mPagerLayoutManager.findFirstVisibleItemPosition() == position)
//                return;

            final MonthView view = (MonthView) holder.itemView;
            if (selectYear != -1 && selectMonth != -1 && selectDay != -1) {
                view.setSelectCalendar(selectYear, selectMonth, selectDay);
            }

            final int minYear = mDelegate.getMinYear();
            final int minYearMonth = mDelegate.getMinYearMonth();
            final int number1 = (position + minYearMonth) / 12;
            final int number2 = (position + minYearMonth) % 12;
            int year = minYear + number1;
            int month = number2;
            if (month == 0) return;

            // Log.e("onCalendarChange22", "position = " + position + ", year = " + year + ", month = " + month);
            view.setup(mDelegate);
            view.setDate(year, month);
        }

        @Override
        public int getItemCount() {

            final Object tag = getTag();
            if (null != tag) {
                return (int) getTag();
            }

            int maxYear = mDelegate.getMaxYear();
            int minYear = mDelegate.getMinYear();
            int minYearMonth = mDelegate.getMinYearMonth();
            int maxYearMonth = mDelegate.getMaxYearMonth();

            if (minYear >= maxYear) {
                maxYear = minYear;
            }

            if (minYear == maxYear) {
                if (minYearMonth >= maxYearMonth || Math.abs(maxYearMonth - minYearMonth) == 1) {
                    return 1;
                } else {
                    return (maxYearMonth - minYearMonth);
                }
            } else {
                final int count = 12 * (maxYear - minYear - 1) + (12 - minYearMonth) + maxYearMonth;
                // Log.e("getItemCount", "count = " + count + ", minYear = " + minYear + ", minYearMonth = " + minYearMonth + ", maxYear = " + maxYear + ", maxYearMonth = " + maxYearMonth);
                setTag(count);
                return count;
            }
        }
    }

    private final class RecyclerHolder extends RecyclerView.ViewHolder {
        private RecyclerHolder(View itemView) {
            super(itemView);
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

    /**********************************************************************************************/

    private OnCalendarChangeListener mOnCalendarChangeListener;

    public void setOnCalendarChangeListener(OnCalendarChangeListener listener) {
        this.mOnCalendarChangeListener = listener;
    }

    public interface OnCalendarChangeListener {

        void onCalendarChange(int year, int month, int day, boolean isClick);
    }
}
