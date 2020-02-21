package com.aunthtoo.timetableforcupyay.model;

/**
 * Created by Aunt Htoo on 11/26/2017.
 */

public class TimeTable {

    String day, subject;

    public TimeTable() {
    }

    public TimeTable(String day, String subject) {
        this.day = day;
        this.subject = subject;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
