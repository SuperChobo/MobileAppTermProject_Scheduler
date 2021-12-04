package com.example.scheduler;

import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Entity {
    public static final int TYPE_NULL = -1;
    public static final int TYPE_CHECK = 0;
    public static final int TYPE_NUM = 1;
    public static final int TYPE_COUNT = 2;

    public static final int VALUE_TRUE = 1;
    public static final int VALUE_FALSE = 0;


    public static final int PERIOD_DAY = 3;
    public static final int PERIOD_WEEK = 4;
    public static final int PERIOD_MONTH = 5;
    public static final int PERIOD_YEAR = 6;
    public static final int PERIOD_NONE = 7;

    private String name = "";
    private int type;
    private int value;
    private int goal;
    private int periodType;
    private int period;
    private Calendar date = null;
    private String valueList = "";

    private static final DateFormat hourFormat = new SimpleDateFormat("HH시 mm분");
    private static final DateFormat dateFormat = new SimpleDateFormat("yy년 MM월 dd일");

    public Entity(String name, int type, int value, int goal, int periodType, int period, int year, int month, int date, int hour, int minute, String valueList){
        this.name = name;
        this.type = type;
        this.value = value;
        this.goal = goal;
        this.periodType = periodType;
        this.period = period;

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, date, hour, minute, 0);
        this.date = calendar;
        this.valueList = valueList;
    }

    public Entity(String name, String valueList, int periodType, int period){
        this(name, valueList, periodType, period, getToday());
    }

    public Entity(String name, String valueList, int periodType, int period, Calendar date){
        this.name = name;
        this.valueList = valueList;
        this.periodType = periodType;
        this.period = period;
        this.date = date;
    }

    public Entity(int type, String name, int value, int goal, int periodType, int period){
        this(type, name, value, goal, periodType, period, getToday());
    }

    public Entity(String name, int periodType, int period, Calendar date){
        this.name = name;
        this.periodType = periodType;
        this.period = period;
        this.date = date;
    }

    public Entity(int type, String name, int value, int goal, int periodType, int period, Calendar date){
        this.type = type;
        this.name = name;
        this.value = value;
        this.goal = goal;
        this.periodType = periodType;
        this.period = period;
        this.date = date;
    }

    public Entity(String name, int year, int month, int day, int hour, int minute, int periodType, int period){
        this.name = name;
        this.date = Calendar.getInstance();
        this.date.set(year, month - 1, day, hour, minute);
        this.periodType = periodType;
        this.period = period;
    }

    public Entity(String name, Calendar date){
        this.name = name;
        this.date = date;
    }

    public static Calendar getToday(){
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        //c.add(Calendar.HOUR_OF_DAY, 9);
        return c;
    }

    public String getName() {
        return name;
    }

    public int getType() {
        return type;
    }

    public int getValue() {
        return value;
    }

    public boolean getValueBool(){
        if(value == VALUE_TRUE){
            return true;
        }else{
            return false;
        }
    }

    public void setType(int type) { this.type = type; }

    public void setGoal(int goal) { this.goal = goal; }

    public void setName(String name){
        this.name = name;
    }

    public void setDate(Calendar date){
        this.date = date;
    }

    public void setPeriod(int period) { this.period = period; }

    public void setPeriodType(int periodType) { this.periodType = periodType; }

    public int getGoal() {
        return goal;
    }

    public String getPeriodTypeString() {
        switch(periodType){
            case PERIOD_NONE:
                return "반복 없음";
            case PERIOD_DAY:
                return "일";
            case PERIOD_WEEK:
                return "주";
            case PERIOD_MONTH:
                return "개월";
            case PERIOD_YEAR:
                return "년";
            default:
                Log.i("test", "Entity: 알 수 없는 기간 정보");
                return null;
        }
    }

    public int getPeriodType() {
        return periodType;
    }

    public int getPeriod() {
        return period;
    }

    public void setValue(int value){
        this.value = value;
    }

    public void valueNotBoolean(){
        if(value == VALUE_TRUE){
            value = VALUE_FALSE;
        }else if(value == VALUE_FALSE){
            value = VALUE_TRUE;
        }
    }

    public Calendar getDate(){
        return date;
    }

    public String getHourString(){
        return hourFormat.format(date.getTime());
    }

    public String getDateString(){
        return dateFormat.format(date.getTime());
    }

    public String getValueList(){
        return valueList;
    }

    public String removeValueListAt(int index){
        return valueList = valueList.substring(0, index) + valueList.substring(index + 1);
    }

    public void setValueList(String valueList){
        this.valueList = valueList;
    }

    public String setValueOfList(char value, int index) {
        return valueList = valueList.substring(0, index) + value + valueList.substring(index + 1);
    }

    public String addValueToList(char value){
        return valueList = valueList + value;
    }

    public int getDayDifference(Calendar target)
    {
        long output;
        Calendar a = Calendar.getInstance();
        a.set(date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DATE), 1, 1, 2);

        Calendar b = Calendar.getInstance();
        int sec = 3;
        if(a.after(b)){
            sec = 1;
        }
        b.set(target.get(Calendar.YEAR), target.get(Calendar.MONTH), target.get(Calendar.DATE), 1, 1, sec);
        output = (a.getTimeInMillis() - b.getTimeInMillis()) / 1000;
        output = output / (24 * 3600);
        return (int)output;
    }
}
