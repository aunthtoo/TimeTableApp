package com.aunthtoo.timetableforcupyay.model;

/**
 * Created by Aunt Htoo on 11/16/2017.
 */

public class Student {

    public String year;
    public String rollno;
    public String section;
    public String stName;
    public String phonenumber;

    public Student() {

    }

    public Student(String year, String rollno, String section, String stName, String phonenumber) {
        this.year = year;
        this.rollno = rollno;
        this.section = section;
        this.stName = stName;
        this.phonenumber = phonenumber;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getRollno() {
        return rollno;
    }

    public void setRollno(String rollno) {
        this.rollno = rollno;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getStName() {
        return stName;
    }

    public void setStName(String stName) {
        this.stName = stName;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }
}
