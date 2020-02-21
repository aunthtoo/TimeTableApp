package com.aunthtoo.timetableforcupyay.model;

/**
 * Created by Aunt Htoo on 12/1/2017.
 */

public class Monthly {

    private String monthname;
    private int rcamount;

    public Monthly() {
    }

    public Monthly(String monthname, int rcamount) {
        this.monthname = monthname;
        this.rcamount = rcamount;
    }

    public String getMonthname() {
        return monthname;
    }

    public void setMonthname(String monthname) {
        this.monthname = monthname;
    }

    public int getRcamount() {
        return rcamount;
    }

    public void setRcamount(int rcamount) {
        this.rcamount = rcamount;
    }
}
