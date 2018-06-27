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
    private boolean isToady = false, isSelect = false, isPress = false, isCurMonth = false, isLeapYear = false, isLeapMonth = false;

    private int year, month, day;

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

    public String getKey() {
        return year + "-" + (month < 10 ? "0" + month : month) + "-" + (day < 10 ? "0" + day : day);
    }

    public boolean isEqual(int years, int months, int days) {

        if (years != year)
            return false;
        if (months != month)
            return false;
        if (days != day)
            return false;
        isSelect = true;
        return true;
    }

    /*******************************************************/

    private SchemeModel schemeModel = null;

    public boolean isSchemes() {
        return (null != schemeModel && (schemeModel.isDiagnose() || schemeModel.isWarning()));
    }

    public void setSchemeModel(SchemeModel schemeModel) {
        this.schemeModel = schemeModel;
    }

    public SchemeModel getSchemeModel() {
        return schemeModel;
    }

    public interface SchemeModel {

        String getKey();

        boolean isDiagnose();

        boolean isWarning();

        String getScheme();
    }
}
