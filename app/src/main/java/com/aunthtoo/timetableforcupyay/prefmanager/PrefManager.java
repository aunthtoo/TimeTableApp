package com.aunthtoo.timetableforcupyay.prefmanager;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Aunt Htoo on 11/18/2017.
 */

public class PrefManager {

    private Context context;
    SharedPreferences pref;
    SharedPreferences.Editor editor;


    // shared pref mode
    int PRIVATE_MODE = 0;


    // Shared preferences file name
    private static final String PREF_NAME = "cupyay-timetable";

    String FIRST_TIME_KEY = "isfirsttime";

    String URL_LINK = "url_link";

    String VALID_MONTH = "validmonth";


    //for show case
    String SHOWCASE1_START_TIME_PICK = "showcase1starttimepick";

    //for month total day to attend
    String monthonetotaldaytoattend = "monthonetotaldaytoattend";
    String monthtwototaldaytoattend = "monthtwototaldaytoattend";
    String monththreetotaldaytoattend = "monththreetotaldaytoattend";
    String monthfourtotaldaytoattend = "monthfourtotaldaytoattend";

    String monthfivetotaldaytoattend = "monthfivetotaldaytoattend";
    String monthsixtotaldaytoattend = "monthsixtotaldaytoattend";
    String monthseventotaldaytoattend = "monthseventotaldaytoattend";
    String montheighttotaldaytoattend = "montheighttotaldaytoattend";

    //for alarm

    String alarmtime1 = "alarmtime1";
    String alarmtime2 = "alarmtime2";
    String alarmtime3 = "alarmtime3";
    String alarmtime4 = "alarmtime4";
    String alarmtime5 = "alarmtime5";
    String alarmtime6 = "alarmtime6";
    String alarmtime7 = "alarmtime7";

    String offmonth = "offmonth";

    String wholedayabsent = "wholedayabsent";

    public PrefManager(Context context) {

        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();

    }

    public void setFirstTime(boolean val) {
        editor.putBoolean(FIRST_TIME_KEY, val);
        editor.commit();
    }

    public boolean isFirstTime() {
        return pref.getBoolean(FIRST_TIME_KEY, true);
    }

    public void setURL_LINK(String url) {
        editor.putString(URL_LINK, url);
        editor.commit();
    }

    public String getURL_LINK() {
        return pref.getString(URL_LINK, null);
    }

    public boolean getSHOWCASE1_START_TIME_PICK() {
        return pref.getBoolean(SHOWCASE1_START_TIME_PICK, true);
    }

    public void setSHOWCASE1_START_TIME_PICK(boolean start_time_pick) {
        editor.putBoolean(SHOWCASE1_START_TIME_PICK, start_time_pick);
        editor.commit();
    }


    public String getVALID_MONTH() {
        return pref.getString(VALID_MONTH, null);
    }

    public void setVALID_MONTH(String month) {
        editor.putString(VALID_MONTH, month);
        editor.commit();
    }


    //for month total day
    public int getMonthonetotaldaytoattend() {
        return pref.getInt(monthonetotaldaytoattend, 0);
    }

    public void setMonthonetotaldaytoattend(int monthonetotaldaytoattend) {
        editor.putInt(this.monthonetotaldaytoattend, monthonetotaldaytoattend);
        editor.commit();

    }

    public int getMonthtwototaldaytoattend() {
        return pref.getInt(monthtwototaldaytoattend, 0);
    }

    public void setMonthtwototaldaytoattend(int monthtwototaldaytoattend) {
        editor.putInt(this.monthtwototaldaytoattend, monthtwototaldaytoattend);
        editor.commit();
    }

    public int getMonththreetotaldaytoattend() {
        return pref.getInt(monththreetotaldaytoattend, 0);
    }

    public void setMonththreetotaldaytoattend(int monththreetotaldaytoattend) {
        editor.putInt(this.monththreetotaldaytoattend, monththreetotaldaytoattend);
        editor.commit();
    }

    public int getMonthfourtotaldaytoattend() {
        return pref.getInt(monthfourtotaldaytoattend, 0);
    }

    public void setMonthfourtotaldaytoattend(int monthfourtotaldaytoattend) {
        editor.putInt(this.monthfourtotaldaytoattend, monthfourtotaldaytoattend);
        editor.commit();
    }

    public int getMonthfivetotaldaytoattend() {
        return pref.getInt(monthfivetotaldaytoattend, 0);
    }

    public void setMonthfivetotaldaytoattend(int monthfivetotaldaytoattend) {
        editor.putInt(this.monthfivetotaldaytoattend, monthfivetotaldaytoattend);
        editor.commit();
    }

    public int getMonthsixtotaldaytoattend() {
        return pref.getInt(monthsixtotaldaytoattend, 0);
    }

    public void setMonthsixtotaldaytoattend(int monthsixtotaldaytoattend) {
        editor.putInt(this.monthsixtotaldaytoattend, monthsixtotaldaytoattend);
        editor.commit();
    }

    public int getMonthseventotaldaytoattend() {
        return pref.getInt(monthseventotaldaytoattend, 0);
    }

    public void setMonthseventotaldaytoattend(int monthseventotaldaytoattend) {
        editor.putInt(this.monthseventotaldaytoattend, monthseventotaldaytoattend);
        editor.commit();
    }

    public int getMontheighttotaldaytoattend() {
        return pref.getInt(montheighttotaldaytoattend, 0);
    }

    public void setMontheighttotaldaytoattend(int montheighttotaldaytoattend) {
        editor.putInt(this.montheighttotaldaytoattend, montheighttotaldaytoattend);
        editor.commit();
    }


    //for alarm
    public boolean isAlarmOne() {
        return pref.getBoolean(alarmtime1, false);
    }

    public boolean isAlarmTwo() {
        return pref.getBoolean(alarmtime2, false);
    }

    public boolean isAlarmThree() {
        return pref.getBoolean(alarmtime3, false);
    }

    public boolean isAlarmFour() {
        return pref.getBoolean(alarmtime4, false);
    }

    public boolean isAlarmFive() {
        return pref.getBoolean(alarmtime5, false);
    }

    public boolean isAlarmSix() {
        return pref.getBoolean(alarmtime6, false);
    }

    public boolean isAlarmSeven() {
        return pref.getBoolean(alarmtime7, false);
    }

    //setting alarm
    public void setAlarmtime1(boolean val) {
        editor.putBoolean(alarmtime1, val);
        editor.commit();
    }

    public void setAlarmtime2(boolean val) {
        editor.putBoolean(alarmtime2, val);
        editor.commit();
    }

    public void setAlarmtime3(boolean val) {
        editor.putBoolean(alarmtime3, val);
        editor.commit();
    }

    public void setAlarmtime4(boolean val) {
        editor.putBoolean(alarmtime4, val);
        editor.commit();
    }

    public void setAlarmtime5(boolean val) {
        editor.putBoolean(alarmtime5, val);
        editor.commit();
    }

    public void setAlarmtime6(boolean val) {
        editor.putBoolean(alarmtime6, val);
        editor.commit();
    }

    public void setAlarmtime7(boolean val) {
        editor.putBoolean(alarmtime7, val);
        editor.commit();
    }

    public boolean getOffmonth() {
        return pref.getBoolean(offmonth, false);
    }

    public void setOffmonth(boolean value) {
        editor.putBoolean(offmonth, value);
        editor.commit();
    }

    public void setWholedayabsent(boolean val) {
        editor.putBoolean(wholedayabsent, val);
        editor.commit();
    }

    public boolean getWholedayabsent() {
        return pref.getBoolean(wholedayabsent, false);
    }
}


