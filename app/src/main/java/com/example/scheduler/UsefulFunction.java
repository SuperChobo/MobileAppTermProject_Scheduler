package com.example.scheduler;

import android.util.Log;

import java.util.Calendar;

public class UsefulFunction {
    public static void refreshEntityList(EntityList target){
        for(Entity item : target.getList()){
            if(UsefulFunction.refreshEntity(item)){
                item.setValue(0);
            }
        }
    }

    public static boolean refreshEntity(Entity target)
    {
        if(target.getPeriodType() == Entity.PERIOD_NONE)
        {
            return false;
        }
        int calendarType = 0;
        int offset = 1;
        switch(target.getPeriodType()) {
            case Entity.PERIOD_DAY:
                calendarType = Calendar.DATE;
                break;
            case Entity.PERIOD_WEEK:
                calendarType = Calendar.DATE;
                offset = 7;
                break;
            case Entity.PERIOD_MONTH:
                calendarType = Calendar.MONTH;
                break;
            case Entity.PERIOD_YEAR:
                calendarType = Calendar.YEAR;
                break;
            default:
                Log.i("test", "No Period Type");
                return false;
        }
        offset = offset * target.getPeriod();

        boolean output = false;
        Calendar today = Calendar.getInstance();
        Calendar nextDate = Calendar.getInstance();
        nextDate.set(target.getDate().get(Calendar.YEAR),target.getDate().get(Calendar.MONTH),target.getDate().get(Calendar.DATE),0, 0, 0);
        while(nextDate.compareTo(today) < 0)
        {
            output = true;
            Calendar newDate = Calendar.getInstance();
            newDate.set(nextDate.get(Calendar.YEAR),nextDate.get(Calendar.MONTH),nextDate.get(Calendar.DATE), 0, 0, 0);
            newDate.set(calendarType, nextDate.get(calendarType) + offset);
            nextDate = newDate;

        }
        if(output) {
            target.getDate().set(Calendar.YEAR, nextDate.get(Calendar.YEAR));
            target.getDate().set(Calendar.MONTH, nextDate.get(Calendar.MONTH));
            target.getDate().set(Calendar.DATE, nextDate.get(Calendar.DATE));
        }
        return output;
    }

    public static boolean isDateEqual(Calendar a, Calendar b){
        return a.get(Calendar.YEAR) == b.get(Calendar.YEAR) &&
                a.get(Calendar.MONTH) == b.get(Calendar.MONTH) &&
                a.get(Calendar.DATE) == b.get(Calendar.DATE);
    }
}
