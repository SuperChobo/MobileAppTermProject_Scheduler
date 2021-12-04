package com.example.scheduler;

import java.util.Calendar;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class ScheduleEntity {
    private String name;
    private Calendar date;
    DateFormat df;

    public ScheduleEntity(String name, int year, int month, int day, int hour, int minute){
        this.name = name;
        this.date = Calendar.getInstance();
        date.set(year, month, day, hour, minute);
        df = new SimpleDateFormat("yy년 MM월 dd일");

    }

    public Calendar getDate(){
        return date;
    }

    public String getDateString(){
        return df.format(date.getTime());
    }
}
