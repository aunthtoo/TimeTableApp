package com.aunthtoo.timetableforcupyay.model;

/**
 * Created by Aunt Htoo on 11/27/2017.
 */

public class Interval {

    public String intervalNumber;


    public String interval;
    public String startTime;
    public String endTime;

    public Interval() {
    }

    public Interval(String intervalNumber, String interval, String startTime, String endTime) {

        this.intervalNumber = intervalNumber;
        this.interval = interval;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getIntervalNumber() {
        return intervalNumber;
    }

    public void setIntervalNumber(String intervalNumber) {
        this.intervalNumber = intervalNumber;
    }

    public String getInterval() {
        return interval;
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
