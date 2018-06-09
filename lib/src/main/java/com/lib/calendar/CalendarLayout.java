package com.lib.calendar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.lang.reflect.Constructor;

/**
 * description: 日历布局
 * created by kalu on 2018/6/9 10:53
 */
public final class CalendarLayout extends LinearLayout {

    private final CalendartManager mPagerLayoutManager = new CalendartManager(getContext().getApplicationContext(), LinearLayout.HORIZONTAL, false);

    private int selectYear = -1, selectMonth = -1, selectDay = -1;

    public final static int MIN_YEAR = 1900;
    public final static int MAX_YEAR = 2099;
    private int minYear = MIN_YEAR, maxYear = MAX_YEAR, minYearMonth = 1, maxYearMonth = 12;

    public CalendarLayout(@NonNull Context context) {
        this(context, null);
    }

    public CalendarLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setOrientation(LinearLayout.VERTICAL);

        String weekClassname = "";
        int weekTextcolor = Color.BLACK;
        int weekBgcolor = Color.TRANSPARENT;
        int weekHeight = (int) (40 * getResources().getDisplayMetrics().density);
        float weekTextsize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16f, getResources().getDisplayMetrics());

        TypedArray typed = null;
        try {
            typed = context.obtainStyledAttributes(attrs, R.styleable.CalendarLayout);
            selectYear = typed.getInt(R.styleable.CalendarLayout_cl_select_year, selectYear);
            selectMonth = typed.getInt(R.styleable.CalendarLayout_cl_select_month, selectMonth);
            selectDay = typed.getInt(R.styleable.CalendarLayout_cl_select_day, selectDay);

            minYear = typed.getInt(R.styleable.CalendarLayout_cl_min_year, minYear);
            maxYear = typed.getInt(R.styleable.CalendarLayout_cl_max_year, maxYear);
            if (minYear <= MIN_YEAR) minYear = MIN_YEAR;
            if (maxYear >= MAX_YEAR) maxYear = MAX_YEAR;

            minYearMonth = typed.getInt(R.styleable.CalendarLayout_cl_min_year_month, minYearMonth);
            maxYearMonth = typed.getInt(R.styleable.CalendarLayout_cl_max_year_month, maxYearMonth);

            weekHeight = (int) typed.getDimension(R.styleable.CalendarLayout_cl_week_height, weekHeight);
            weekBgcolor = typed.getColor(R.styleable.CalendarLayout_cl_week_bg_color, weekBgcolor);
            weekTextcolor = typed.getColor(R.styleable.CalendarLayout_cl_week_text_color, weekTextcolor);
            weekTextsize = typed.getDimension(R.styleable.CalendarLayout_cl_week_text_size, weekTextsize);
            weekClassname = typed.getString(R.styleable.CalendarLayout_cl_week_class);
        } catch (Exception e) {
            Log.e("", e.getMessage(), e);
        } finally {
            if (null == typed) return;
            typed.recycle();
        }

        // 1.星期栏
        WeekBar weekBar;
        try {
            if (TextUtils.isEmpty(weekClassname)) {
                weekBar = new WeekBar(getContext());
            } else {
                final Constructor constructor = Class.forName(weekClassname).getConstructor(Context.class);
                weekBar = (WeekBar) constructor.newInstance(getContext().getApplicationContext());
            }
        } catch (Exception e) {
            Log.e("", e.getMessage(), e);
            weekBar = new WeekBar(getContext());
        }
        LinearLayout.LayoutParams weekBarParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, weekHeight);
        weekBar.setLayoutParams(weekBarParams);
        weekBar.setBackgroundColor(weekBgcolor);
        weekBar.setTextSize(weekTextsize);
        weekBar.setTextColor(weekTextcolor);
        addView(weekBar);

        // 2.日期
        final RecyclerView recyclerView = new RecyclerView(getContext().getApplicationContext());
        recyclerView.setBackgroundColor(Color.WHITE);
        LinearLayout.LayoutParams paramsRecyclerView = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        paramsRecyclerView.setMargins(10, 10, 10, 10);
        recyclerView.setLayoutParams(paramsRecyclerView);
        addView(recyclerView);

        recyclerView.setLayoutManager(mPagerLayoutManager);
        recyclerView.setAdapter(new CalendarAdapter());
        mPagerLayoutManager.setOnPagerChangeListener(new CalendartManager.OnPagerChangeListener() {

            @Override
            public void onPageSelect(int position, boolean isTop, boolean isBottom) {
                if (null == mOnCalendarChangeListener) return;
                final int year = (position + minYearMonth - 1) / 12 + minYear;
                final int month = ((position + minYearMonth - 1) % 12 + 1);
                mOnCalendarChangeListener.onCalendarChange(year, month, 1, false);
            }

            @Override
            public void onPageDetach(boolean isNext, int position) {
            }

            @Override
            public void onPageFinish() {
            }
        });
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

    public void setSelectDate(int year, int month, int day) {
        selectYear = year;
        selectMonth = month;
        selectDay = day;

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
            final BaseView view = new MonthView(getContext().getApplicationContext());
            view.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            return new RecyclerHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

            final MonthView view = (MonthView) holder.itemView;
            if (selectYear != -1 && selectMonth != -1 && selectDay != -1) {
                view.setSelectCalendar(selectYear, selectMonth, selectDay);
            }

            final int number1 = (position + minYearMonth) / 12;
            final int number2 = (position + minYearMonth) % 12;
            int year = minYear + number1;
            int month = number2;
            if (month == 0) return;
            view.setDate(year, month);
        }

        @Override
        public int getItemCount() {

            final Object tag = getTag();
            if (null != tag) {
                return (int) getTag();
            }

            if (minYear == maxYear) {
                if (minYearMonth >= maxYearMonth || Math.abs(maxYearMonth - minYearMonth) == 1) {
                    return 1;
                } else {
                    return (maxYearMonth - minYearMonth);
                }
            } else {
                final int count = 12 * (maxYear - minYear - 1) + (12 - minYearMonth) + maxYearMonth;
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

    /**********************************************************************************************/

    private OnCalendarChangeListener mOnCalendarChangeListener;

    public void setOnCalendarChangeListener(OnCalendarChangeListener listener) {
        this.mOnCalendarChangeListener = listener;
    }

    public interface OnCalendarChangeListener {
        void onCalendarChange(int year, int month, int day, boolean isClick);
    }
}
