package com.lib.calendar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;

/**
 * description: 属性委托
 * created by kalu on 2018/4/10 23:09
 */
public final class CalendarDelegate {

    public final static int MIN_YEAR = 1900;
    public final static int MAX_YEAR = 2099;

    private int mMinYear = MIN_YEAR, mMaxYear = MAX_YEAR;
    private int mMinYearMonth = 1, mMaxYearMonth = 12;

    /*********************************************************/

    private int mCurDayTextColor,
            mCurDayLunarTextColor,
            mWeekTextColor,
            mSchemeTextColor,
            mSchemeLunarTextColor,
            mOtherMonthTextColor,
            mCurrentMonthTextColor,
            mSelectedTextColor,
            mSelectedLunarTextColor,
            mCurMonthLunarTextColor,
            mOtherMonthLunarTextColor;
    /**
     * 星期栏的背景、线的背景、年份背景
     */
    private int mWeekLineBackground,
            mWeekBackground;
    /**
     * 标记的主题色和选中的主题色
     */
    private int mSchemeThemeColor, mSelectedThemeColor;
    /**
     * 自定义的日历路径
     */
    private String mMonthViewClass;
    /**
     * 自定义周视图路径
     */
    private String mWeekViewClass;
    /**
     * 自定义周栏路径
     */
    private String mWeekBarClass;
    /**
     * 标记文本
     */
    private String mSchemeText;
    /**
     * 日期和农历文本大小
     */
    private float mDayTextSize, mLunarTextSize;
    /**
     * 星期栏的高度
     */
    private int mWeekBarHeight;

    void setAttributeSet(Context context, @Nullable AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CalendarLayout);

        LunarCalendar.init(context);

        mSchemeTextColor = array.getColor(R.styleable.CalendarLayout_scheme_text_color, 0xFFFFFFFF);
        mSchemeLunarTextColor = array.getColor(R.styleable.CalendarLayout_scheme_lunar_text_color, 0xFFe1e1e1);
        mSchemeThemeColor = array.getColor(R.styleable.CalendarLayout_scheme_theme_color, 0x50CFCFCF);
        mMonthViewClass = array.getString(R.styleable.CalendarLayout_month_view);

        mWeekViewClass = array.getString(R.styleable.CalendarLayout_week_view);
        mWeekBarClass = array.getString(R.styleable.CalendarLayout_week_bar_view);
        mWeekBarHeight = (int) array.getDimension(R.styleable.CalendarLayout_week_bar_height, dp2px(context, 40f));

        mSchemeText = array.getString(R.styleable.CalendarLayout_scheme_text);
        if (TextUtils.isEmpty(mSchemeText)) {
            mSchemeText = "记";
        }

        mWeekBackground = array.getColor(R.styleable.CalendarLayout_week_background, Color.WHITE);
        mWeekLineBackground = array.getColor(R.styleable.CalendarLayout_week_line_background, Color.TRANSPARENT);
        mWeekTextColor = array.getColor(R.styleable.CalendarLayout_week_text_color, 0xFF333333);

        mSelectedTextColor = array.getColor(R.styleable.CalendarLayout_selected_text_color, 0xFF111111);

        mSelectedLunarTextColor = array.getColor(R.styleable.CalendarLayout_selected_lunar_text_color, 0xFF111111);

        mDayTextSize = array.getDimension(R.styleable.CalendarLayout_day_text_size, sp2px(context, 16f));
        mLunarTextSize = array.getDimension(R.styleable.CalendarLayout_lunar_text_size, sp2px(context, 10f));

        /***********************************************************************************************************/

        mCurrentMonthTextColor = array.getColor(R.styleable.CalendarLayout_cl_month_text_color_current, Color.BLACK);
        mOtherMonthTextColor = array.getColor(R.styleable.CalendarLayout_cl_month_text_color_other, Color.GRAY);

        mCurMonthLunarTextColor = array.getColor(R.styleable.CalendarLayout_cl_month_lunar_text_color_other, Color.GRAY);
        mOtherMonthLunarTextColor = array.getColor(R.styleable.CalendarLayout_cl_month_lunar_text_color_other, Color.GRAY);

        mCurDayTextColor = array.getColor(R.styleable.CalendarLayout_cl_day_text_color_today, Color.RED);
        mCurDayLunarTextColor = array.getColor(R.styleable.CalendarLayout_cl_day_lunar_text_color_today, Color.RED);

        mSelectedThemeColor = array.getColor(R.styleable.CalendarLayout_cl_bg_today_color, 0x50CFCFCF);

        mMinYear = array.getInt(R.styleable.CalendarLayout_cl_min_year, mMinYear);
        mMaxYear = array.getInt(R.styleable.CalendarLayout_cl_max_year, mMaxYear);
        mMinYearMonth = array.getInt(R.styleable.CalendarLayout_cl_min_year_month, mMinYearMonth);
        mMaxYearMonth = array.getInt(R.styleable.CalendarLayout_cl_max_year_month, mMaxYearMonth);
        if (mMinYear <= MIN_YEAR) mMinYear = MIN_YEAR;
        if (mMaxYear >= MAX_YEAR) mMaxYear = MAX_YEAR;

        array.recycle();
    }

    final void setRange(int minYear, int minYearMonth, int maxYear, int maxYearMonth) {
        this.mMinYear = minYear;
        this.mMinYearMonth = minYearMonth;
        this.mMaxYear = maxYear;
        this.mMaxYearMonth = maxYearMonth;
    }

    String getSchemeText() {
        return mSchemeText;
    }

    int getCurDayTextColor() {
        return mCurDayTextColor;
    }

    int getCurDayLunarTextColor() {
        return mCurDayLunarTextColor;
    }

    int getWeekTextColor() {
        return mWeekTextColor;
    }

    int getSchemeTextColor() {
        return mSchemeTextColor;
    }

    int getSchemeLunarTextColor() {
        return mSchemeLunarTextColor;
    }

    int getOtherMonthTextColor() {
        return mOtherMonthTextColor;
    }

    int getCurrentMonthTextColor() {
        return mCurrentMonthTextColor;
    }

    int getSelectedTextColor() {
        return mSelectedTextColor;
    }

    int getSelectedLunarTextColor() {
        return mSelectedLunarTextColor;
    }

    int getCurrentMonthLunarTextColor() {
        return mCurMonthLunarTextColor;
    }

    int getOtherMonthLunarTextColor() {
        return mOtherMonthLunarTextColor;
    }

    int getSchemeThemeColor() {
        return mSchemeThemeColor;
    }

    int getSelectedThemeColor() {
        return mSelectedThemeColor;
    }

    int getWeekBackground() {
        return mWeekBackground;
    }

    int getWeekLineBackground() {
        return mWeekLineBackground;
    }

    String getMonthViewClass() {
        return mMonthViewClass;
    }

    String getWeekViewClass() {
        return mWeekViewClass;
    }

    String getWeekBarClass() {
        return mWeekBarClass;
    }

    int getWeekBarHeight() {
        return mWeekBarHeight;
    }

    public int getMinYear() {
        return mMinYear;
    }

    public int getMaxYear() {
        return mMaxYear;
    }

    float getDayTextSize() {
        return mDayTextSize;
    }

    float getLunarTextSize() {
        return mLunarTextSize;
    }

    public int getMinYearMonth() {
        return mMinYearMonth;
    }

    public int getMaxYearMonth() {
        return mMaxYearMonth;
    }

    void setTextColor(int curDayTextColor, int curMonthTextColor, int otherMonthTextColor, int curMonthLunarTextColor, int otherMonthLunarTextColor) {
        mCurDayTextColor = curDayTextColor;
        mOtherMonthTextColor = otherMonthTextColor;
        mCurrentMonthTextColor = curMonthTextColor;
        mCurMonthLunarTextColor = curMonthLunarTextColor;
        mOtherMonthLunarTextColor = otherMonthLunarTextColor;
    }

    void setSchemeColor(int schemeColor, int schemeTextColor, int schemeLunarTextColor) {
        this.mSchemeThemeColor = schemeColor;
        this.mSchemeTextColor = schemeTextColor;
        this.mSchemeLunarTextColor = schemeLunarTextColor;
    }

    void setSelectColor(int selectedColor, int selectedTextColor, int selectedLunarTextColor) {
        this.mSelectedThemeColor = selectedColor;
        this.mSelectedTextColor = selectedTextColor;
        this.mSelectedLunarTextColor = selectedLunarTextColor;
    }

    void setThemeColor(int selectedThemeColor, int schemeColor) {
        this.mSelectedThemeColor = selectedThemeColor;
        this.mSchemeThemeColor = schemeColor;
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
