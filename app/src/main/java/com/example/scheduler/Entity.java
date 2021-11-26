package com.example.scheduler;

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

    private String name;
    private int type;
    private int value;
    private int goal;
    private int periodType;
    private int period;
    private Calendar date;
    private static final DateFormat dateFormat = new SimpleDateFormat("yy-MM-dd HH:mm");
    private String valueList;

    public Entity(String name, String valueList, int periodType, int period){
        this.name = name;
        this.valueList = valueList;
        this.periodType = periodType;
        this.period = period;
    }

    public Entity(int type, String name, int value, int goal, int periodType, int period){
        this(type, name, value, goal, periodType, period, getToday());
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

    public Entity(String name, int year, int month, int day, int hour, int minute){
        this.name = name;
        this.date = Calendar.getInstance();
        this.date.set(year, month - 1, day, hour, minute);
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

    public int getGoal() {
        return goal;
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

    public String getDateString(){
        return dateFormat.format(date.getTime());
    }

    public String getValueList(){
        return valueList;
    }

    public String removeValueList(int index){
        return valueList = valueList.substring(0, index) + valueList.substring(index + 1);
    }

    public String setValueOfList(char value, int index){
        return valueList = valueList.substring(0, index) + value + valueList.substring(index + 1);
    }
}
