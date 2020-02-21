package com.aunthtoo.timetableforcupyay.model;

/**
 * Created by Aunt Htoo on 12/1/2017.
 */

public class TeacherAndSubject {


    private String shortSub,longSub,teacherName;

    public TeacherAndSubject(String shortSub, String longSub, String teacherName) {
        this.shortSub = shortSub;
        this.longSub = longSub;
        this.teacherName = teacherName;
    }

    public String getShortSub() {
        return shortSub;
    }

    public void setShortSub(String shortSub) {
        this.shortSub = shortSub;
    }

    public String getLongSub() {
        return longSub;
    }

    public void setLongSub(String longSub) {
        this.longSub = longSub;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }
}
