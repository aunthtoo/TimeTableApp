package com.aunthtoo.timetableforcupyay.model;

/**
 * Created by Aunt Htoo on 12/1/2017.
 */

public class DailyTime {

    private String date, timeone, timetwo, timethree, timefour, timefive, timesix, timeseven;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTimeone() {
        return timeone;
    }

    public void setTimeone(String timeone) {
        this.timeone = timeone;
    }

    public String getTimetwo() {
        return timetwo;
    }

    public void setTimetwo(String timetwo) {
        this.timetwo = timetwo;
    }

    public String getTimethree() {
        return timethree;
    }

    public void setTimethree(String timethree) {
        this.timethree = timethree;
    }

    public String getTimefour() {
        return timefour;
    }

    public void setTimefour(String timefour) {
        this.timefour = timefour;
    }

    public String getTimefive() {
        return timefive;
    }

    public void setTimefive(String timefive) {
        this.timefive = timefive;
    }

    public String getTimesix() {
        return timesix;
    }

    public void setTimesix(String timesix) {
        this.timesix = timesix;
    }

    public String getTimeseven() {
        return timeseven;
    }

    public void setTimeseven(String timeseven) {
        this.timeseven = timeseven;
    }

    public DailyTime(String date, String timeone, String timetwo, String timethree, String timefour, String timefive, String timesix, String timeseven) {

        this.date = date;
        this.timeone = timeone;
        this.timetwo = timetwo;
        this.timethree = timethree;
        this.timefour = timefour;
        this.timefive = timefive;
        this.timesix = timesix;
        this.timeseven = timeseven;
    }

    public DailyTime() {
    }

}
