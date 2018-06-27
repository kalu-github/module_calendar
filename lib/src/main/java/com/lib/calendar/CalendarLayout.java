package com.lib.calendar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.IntRange;
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
import java.util.Calendar;
import java.util.List;

/**
 * description: 日历布局
 * created by kalu on 2018/6/9 10:53
 */
public final class CalendarLayout extends LinearLayout {

    private final CalendartManager mPagerLayoutManager = new CalendartManager(getContext().getApplicationContext(), LinearLayout.HORIZONTAL, false);

    private int selectYear = Integer.parseInt(CalendarUtil.getYear());
    private int selectMonth = Integer.parseInt(CalendarUtil.getMonth());
    private int selectDay = Integer.parseInt(CalendarUtil.getDay());

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
            if (null != typed) {
                typed.recycle();
            }
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
        LayoutParams weekBarParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, weekHeight);
        weekBar.setLayoutParams(weekBarParams);
        weekBar.setBackgroundColor(weekBgcolor);
        weekBar.setTextSize(weekTextsize);
        weekBar.setTextColor(weekTextcolor);
        addView(weekBar);
    }

    public void notifyDataSetChanged() {

        final int year = Integer.parseInt(CalendarUtil.getYear());
        final int month = Integer.parseInt(CalendarUtil.getMonth());
        final int day = Integer.parseInt(CalendarUtil.getDay());
        notifyDataSetChanged(year, month, day);
    }

    public void notifyDataSetChanged(int year, @IntRange(from = 1, to = 12) int month, int day) {

        final RecyclerView recyclerView = new RecyclerView(getContext().getApplicationContext());
        recyclerView.setBackgroundColor(Color.WHITE);
        LayoutParams paramsRecyclerView = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        paramsRecyclerView.setMargins(10, 10, 10, 10);
        recyclerView.setLayoutParams(paramsRecyclerView);
        addView(recyclerView);

        recyclerView.setLayoutManager(mPagerLayoutManager);
        recyclerView.setAdapter(new CalendarAdapter());

        selectDay = day;
        selectYear = year;
        selectMonth = month;

        if (year < minYear) {
            year = minYear;
        }

        if (year == minYear) {
            final int position = month - 1;
            mPagerLayoutManager.scrollToPositionWithOffset(position, 0);
        } else {
            int position;
            if (minYearMonth == 1) {
                position = 12 * (year - minYear - 1) + 12 + month - 1;
            } else {
                position = 12 * (year - minYear - 1) + (12 - minYearMonth) + month - 1;
            }
            mPagerLayoutManager.scrollToPositionWithOffset(position, 0);
        }

        if (null != mOnCalendarChangeListener)
            return;

        mPagerLayoutManager.setOnPagerChangeListener(new CalendartManager.OnPagerChangeListener() {

            @Override
            public void onPageSelect(int position, boolean isFirst, boolean isLast, boolean isInit) {

                final View view = mPagerLayoutManager.findViewByPosition(position);
                if (null == view || !(view instanceof BaseCalendarView))
                    return;

                final BaseCalendarView month = (BaseCalendarView) view;

                final List<CalendarModel> list = month.getModelList();
                if (null == list || list.size() == 0)
                    return;

                final int years = month.getYear();
                final int months = month.getMonth();
                final int days = month.getDay();
                final Calendar calendar = CalendarUtil.getCalendar();
                calendar.set(Calendar.YEAR, years);
                calendar.set(Calendar.MONTH, months - 1);//Java月份才0开始算
                final int maxdays = calendar.getActualMaximum(Calendar.DATE);
                //    Log.e("rili_1", "years =" + years + ", months = " + months + ", day = " + days + ", maxDay = " + maxdays + ", isClick = " + false + ", isInit = " + isInit);
                mOnCalendarChangeListener.onCalendarChange(years, months, days, maxdays, false, isInit);
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

    public void setRange(int minYear, @IntRange(from = 1, to = 12) int minYearMonth, int maxYear, @IntRange(from = 1, to = 12) int maxYearMonth) {
        this.minYear = minYear;
        this.maxYear = maxYear;
        this.minYearMonth = minYearMonth;
        this.maxYearMonth = maxYearMonth;
    }

    public void setScheme(List<CalendarModel.SchemeModel> schemeList) {

        if (null == mPagerLayoutManager || null == schemeList || schemeList.isEmpty())
            return;

        try {

            final int firstVisibleItemPosition = mPagerLayoutManager.findFirstVisibleItemPosition();
            if (firstVisibleItemPosition < 0)
                return;

            final View view = mPagerLayoutManager.findViewByPosition(firstVisibleItemPosition);
            if (null == view || !(view instanceof BaseCalendarView))
                return;

            final BaseCalendarView month = (BaseCalendarView) view;

            final List<CalendarModel> modelList = month.getModelList();
            if (null == modelList || modelList.size() == 0)
                return;

            for (CalendarModel.SchemeModel scheme : schemeList) {
                for (CalendarModel model : modelList) {
                    if (scheme.getKey().startsWith(model.getKey())) {
                        model.setSchemeModel(scheme);
                    }
                }
            }

            month.postInvalidate();

        } catch (Exception e) {
            Log.e("", e.getMessage(), e);
        }
    }

    public final class CalendarAdapter extends RecyclerView.Adapter {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            final BaseCalendarView view = new MonthView(getContext().getApplicationContext());
            if (null != mOnCalendarChangeListener) {
                view.setOnCalendarChangeListener(mOnCalendarChangeListener);
            }
            view.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            return new RecyclerHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

            final MonthView view = (MonthView) holder.itemView;
            view.setDate(selectYear, selectMonth, selectDay);

            final int number1 = (position + minYearMonth) / 12;
            final int number2 = (position + minYearMonth) % 12;
            int year = minYear + number1;
            int month = number2;
            if (month == 0) {
                month = 12;
                year = year - 1;
            }
            view.calcuDate(year, month);
        }

        @Override
        public int getItemCount() {

            final Object tag = getTag();
            if (null != tag) {
                return (int) getTag();
            }

            if (maxYear < minYear)
                return 1;

            if (minYearMonth < 1)
                return 1;

            if (maxYearMonth > 12)
                return 1;

            final int count = ((maxYear - minYear) - 1) * 12 + (12 - minYearMonth + 1) + maxYearMonth;
            setTag(count);
            return count;
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
        void onCalendarChange(int year, int month, int day, int maxDay, boolean isClick, boolean isInflate);
    }
}
