package com.lib.calendar;

import java.io.Serializable;

/**
 * description: 日历对象
 * created by kalu on 2018/4/10 23:08
 */
public final class CalendarModel implements Serializable {

    /**
     * isSchemes:   是否有标记
     * isToady:     是否当天
     * isSelect：   是否选中
     * isPress：    是否按压
     * isCurMonth： 是否当前月
     * isLeapYear： 是否闰年
     * isLeapMonth: 是否闰月
     */
    private boolean isSchemes = false, isToady = false, isSelect = false, isPress = false, isCurMonth = false, isLeapYear = false, isLeapMonth = false;

    private int year, month, day;

    private String schemesStr = "";

    public boolean isSchemes() {
        return isSchemes;
    }

    public void setSchemes(boolean schemes) {
        isSchemes = schemes;
    }

    public String getSchemesStr() {
        return schemesStr;
    }

    public void setSchemesStr(String schemesStr) {
        this.schemesStr = schemesStr;
    }

    public boolean isPress() {
        return isPress;
    }

    public void setPress(boolean press) {
        isPress = press;
    }

    public boolean isToady() {
        return isToady;
    }

    public void setToady(boolean toady) {
        isToady = toady;
    }

    /**
     * 农历字符串，没有特别大的意义，用来做简单的农历或者节日标记
     * 建议通过lunarCakendar获取完整的农历日期
     */
    private String lunar;

    /**
     * 24节气
     */
    private String solarTerm;

    /**
     * 公历节日
     */
    private String gregorianFestival;

    /**
     * 传统农历节日
     */
    private String traditionFestival;

    /**
     * 计划，可以用来标记当天是否有任务,这里是默认的，如果使用多标记，请使用下面API
     * using addScheme(int schemeColor,String scheme); multi scheme
     */
    private String scheme;

    /**
     * 各种自定义标记颜色、没有则选择默认颜色，如果使用多标记，请使用下面API
     * using addScheme(int schemeColor,String scheme); multi scheme
     */
    private int schemeColor;

    /**
     * 是否是周末
     */
    private boolean isWeekend;

    /**
     * 星期,0-6 对应周日到周一
     */
    private int week;

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public boolean isCurMonth() {
        return isCurMonth;
    }

    public void setCurMonth(boolean curMonth) {
        isCurMonth = curMonth;
    }

    public String getLunar() {
        return lunar;
    }

    public void setLunar(String lunar) {
        this.lunar = lunar;
    }


    public String getScheme() {
        return scheme;
    }


    public void setScheme(String scheme) {
        this.scheme = scheme;
    }


    public int getSchemeColor() {
        return schemeColor;
    }

    public void setSchemeColor(int schemeColor) {
        this.schemeColor = schemeColor;
    }

    public boolean isWeekend() {
        return isWeekend;
    }

    public void setWeekend(boolean weekend) {
        isWeekend = weekend;
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public String getSolarTerm() {
        return solarTerm;
    }

    public void setSolarTerm(String solarTerm) {
        this.solarTerm = solarTerm;
    }

    public String getGregorianFestival() {
        return gregorianFestival;
    }

    public void setGregorianFestival(String gregorianFestival) {
        this.gregorianFestival = gregorianFestival;
    }

    public boolean isLeapMonth() {
        return isLeapMonth;
    }

    public void setLeapMonth(boolean leapMonth) {
        isLeapMonth = leapMonth;
    }

    public boolean isLeapYear() {
        return isLeapYear;
    }

    public void setLeapYear(boolean leapYear) {
        isLeapYear = leapYear;
    }

    public String getTraditionFestival() {
        return traditionFestival;
    }

    public void setTraditionFestival(String traditionFestival) {
        this.traditionFestival = traditionFestival;
    }

    @Override
    public boolean equals(Object o) {
        if (o != null && o instanceof CalendarModel) {
            if (((CalendarModel) o).getYear() == year && ((CalendarModel) o).getMonth() == month && ((CalendarModel) o).getDay() == day)
                return true;
        }
        return super.equals(o);
    }

    @Override
    public String toString() {
        return year + "" + (month < 10 ? "0" + month : month) + "" + (day < 10 ? "0" + day : day);
    }

    /*******************************************************/
}
