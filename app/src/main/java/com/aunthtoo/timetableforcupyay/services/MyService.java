package com.aunthtoo.timetableforcupyay.services;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.aunthtoo.timetableforcupyay.dbhandler.TimeTableDBHandler;
import com.aunthtoo.timetableforcupyay.prefmanager.PrefManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Aunt Htoo on 12/2/2017.
 */

public class MyService extends Service {

    private String currentDateTimeString;
    //for date time
    private String day, month, year, hour, min, mDate, mTime;

    private PrefManager prefManager;
    private TimeTableDBHandler dbHandler;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("Service", "Service is created");
        prefManager = new PrefManager(getApplicationContext());
        dbHandler = new TimeTableDBHandler(getApplicationContext());

    }

    @Override
    public void onStart(Intent intent, int startId) {

        Log.d("Service", "Service is started");



    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        onTaskRemoved(intent);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        currentDateTimeString = df.format(Calendar.getInstance().getTime());


        // Toast.makeText(getContext(), currentDateTimeString, Toast.LENGTH_LONG).show();

        mDate = currentDateTimeString.split(" ")[0];
        mTime = currentDateTimeString.split(" ")[1];

        //for date
        year = mDate.split("-")[0];
        month = mDate.split("-")[1];
        day = mDate.split("-")[2];

        //for time
        hour = mTime.split(":")[0];
        min = mTime.split(":")[1];

        // Toast.makeText(getApplicationContext(), dbHandler.getCountOnMonthOone() + " total day to attend is " + prefManager.getMonthonetotaldaytoattend(), Toast.LENGTH_LONG).show();

        String tDay = "";

        try {
            SimpleDateFormat inFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = inFormat.parse(mDate);
            SimpleDateFormat outFormat = new SimpleDateFormat("EEEE");
            tDay = outFormat.format(date);

        } catch (Exception e) {
            Log.e("Error", e.getMessage());
        }

        if (!tDay.toLowerCase().substring(0, 3).equals("sat") && !tDay.toLowerCase().substring(0, 3).equals("sun") && !dbHandler.isAlreadyOffDay(year + "-" + month + "-" + day)) {
            switch (getValidMonth(month)) {
                case 1:

                    if (!dbHandler.isAlreadyExistThisDateInMonthOne(year + "-" + month + "-" + day)) {

                        dbHandler.initializeWithNewDateinmonthone(year + "-" + month + "-" + day);
                        prefManager.setWholedayabsent(false);

                        prefManager.setAlarmtime1(false);
                        prefManager.setAlarmtime2(false);
                        prefManager.setAlarmtime3(false);
                        prefManager.setAlarmtime4(false);
                        prefManager.setAlarmtime5(false);
                        prefManager.setAlarmtime6(false);
                        prefManager.setAlarmtime7(false);

                    }

                    if (prefManager.getMonthonetotaldaytoattend() == 0) {

                        prefManager.setMonthonetotaldaytoattend(getDaysWithoutWeekends(Integer.parseInt(day), Integer.parseInt(month), Integer.parseInt(year)));
                    }

                    prefManager.setOffmonth(false);

                    break;

                case 2:


                    if (!dbHandler.isAlreadyExistThisDateInMonthTwo(year + "-" + month + "-" + day)) {
                        dbHandler.initializeWithNewDateinmonthtwo(year + "-" + month + "-" + day);
                        prefManager.setWholedayabsent(false);

                        prefManager.setAlarmtime1(false);
                        prefManager.setAlarmtime2(false);
                        prefManager.setAlarmtime3(false);
                        prefManager.setAlarmtime4(false);
                        prefManager.setAlarmtime5(false);
                        prefManager.setAlarmtime6(false);
                        prefManager.setAlarmtime7(false);

                    }

                    if (prefManager.getMonthtwototaldaytoattend() == 0) {

                        prefManager.setMonthtwototaldaytoattend(getDaysWithoutWeekends(Integer.parseInt(day), Integer.parseInt(month), Integer.parseInt(year)));
                    }

                    prefManager.setOffmonth(false);

                    break;

                case 3:


                    if (!dbHandler.isAlreadyExistThisDateInMonthThree(year + "-" + month + "-" + day)) {
                        dbHandler.initializeWithNewDateinmonththree(year + "-" + month + "-" + day);
                        prefManager.setWholedayabsent(false);

                        prefManager.setAlarmtime1(false);
                        prefManager.setAlarmtime2(false);
                        prefManager.setAlarmtime3(false);
                        prefManager.setAlarmtime4(false);
                        prefManager.setAlarmtime5(false);
                        prefManager.setAlarmtime6(false);
                        prefManager.setAlarmtime7(false);

                    }

                    if (prefManager.getMonththreetotaldaytoattend() == 0) {

                        prefManager.setMonththreetotaldaytoattend(getDaysWithoutWeekends(Integer.parseInt(day), Integer.parseInt(month), Integer.parseInt(year)));
                    }

                    prefManager.setOffmonth(false);

                    break;

                case 4:


                    if (!dbHandler.isAlreadyExistThisDateInMonthFour(year + "-" + month + "-" + day)) {
                        dbHandler.initializeWithNewDateinmonthfour(year + "-" + month + "-" + day);
                        prefManager.setWholedayabsent(false);

                        prefManager.setAlarmtime1(false);
                        prefManager.setAlarmtime2(false);
                        prefManager.setAlarmtime3(false);
                        prefManager.setAlarmtime4(false);
                        prefManager.setAlarmtime5(false);
                        prefManager.setAlarmtime6(false);
                        prefManager.setAlarmtime7(false);

                    }

                    if (prefManager.getMonthfourtotaldaytoattend() == 0) {

                        prefManager.setMonthfourtotaldaytoattend(getDaysWithoutWeekends(Integer.parseInt(day), Integer.parseInt(month), Integer.parseInt(year)));
                    }


                    prefManager.setOffmonth(false);

                    break;

                case 5:


                    if (!dbHandler.isAlreadyExistThisDateInMonthFive(year + "-" + month + "-" + day)) {
                        dbHandler.initializeWithNewDateinmonthfive(year + "-" + month + "-" + day);
                        prefManager.setWholedayabsent(false);

                        prefManager.setAlarmtime1(false);
                        prefManager.setAlarmtime2(false);
                        prefManager.setAlarmtime3(false);
                        prefManager.setAlarmtime4(false);
                        prefManager.setAlarmtime5(false);
                        prefManager.setAlarmtime6(false);
                        prefManager.setAlarmtime7(false);

                    }

                    if (prefManager.getMonthfivetotaldaytoattend() == 0) {

                        prefManager.setMonthfivetotaldaytoattend(getDaysWithoutWeekends(Integer.parseInt(day), Integer.parseInt(month), Integer.parseInt(year)));
                    }

                    prefManager.setOffmonth(false);

                    break;

                case 6:


                    if (!dbHandler.isAlreadyExistThisDateInMonthSix(year + "-" + month + "-" + day)) {
                        dbHandler.initializeWithNewDateinmonthsix(year + "-" + month + "-" + day);
                        prefManager.setWholedayabsent(false);

                        prefManager.setAlarmtime1(false);
                        prefManager.setAlarmtime2(false);
                        prefManager.setAlarmtime3(false);
                        prefManager.setAlarmtime4(false);
                        prefManager.setAlarmtime5(false);
                        prefManager.setAlarmtime6(false);
                        prefManager.setAlarmtime7(false);

                    }

                    if (prefManager.getMonthsixtotaldaytoattend() == 0) {

                        prefManager.setMonthsixtotaldaytoattend(getDaysWithoutWeekends(Integer.parseInt(day), Integer.parseInt(month), Integer.parseInt(year)));
                    }

                    prefManager.setOffmonth(false);

                    break;

                case 7:


                    if (!dbHandler.isAlreadyExistThisDateInMonthSeven(year + "-" + month + "-" + day)) {
                        dbHandler.initializeWithNewDateinmonthseven(year + "-" + month + "-" + day);
                        prefManager.setWholedayabsent(false);

                        prefManager.setAlarmtime1(false);
                        prefManager.setAlarmtime2(false);
                        prefManager.setAlarmtime3(false);
                        prefManager.setAlarmtime4(false);
                        prefManager.setAlarmtime5(false);
                        prefManager.setAlarmtime6(false);
                        prefManager.setAlarmtime7(false);

                    }

                    if (prefManager.getMonthseventotaldaytoattend() == 0) {

                        prefManager.setMonthseventotaldaytoattend(getDaysWithoutWeekends(Integer.parseInt(day), Integer.parseInt(month), Integer.parseInt(year)));
                    }

                    prefManager.setOffmonth(false);

                    break;

                case 8:


                    if (!dbHandler.isAlreadyExistThisDateInMonthEight(year + "-" + month + "-" + day)) {
                        dbHandler.initializeWithNewDateinmontheight(year + "-" + month + "-" + day);
                        prefManager.setWholedayabsent(false);

                        prefManager.setAlarmtime1(false);
                        prefManager.setAlarmtime2(false);
                        prefManager.setAlarmtime3(false);
                        prefManager.setAlarmtime4(false);
                        prefManager.setAlarmtime5(false);
                        prefManager.setAlarmtime6(false);
                        prefManager.setAlarmtime7(false);

                    }

                    if (prefManager.getMontheighttotaldaytoattend() == 0) {

                        prefManager.setMontheighttotaldaytoattend(getDaysWithoutWeekends(Integer.parseInt(day), Integer.parseInt(month), Integer.parseInt(year)));
                    }

                    prefManager.setOffmonth(false);

                    break;

                case 0:

                    prefManager.setOffmonth(true);

                    break;


            }
        } else {

            prefManager.setWholedayabsent(false);

        }


        // Toast.makeText(getApplicationContext(), "service testing", Toast.LENGTH_LONG).show();


        return START_STICKY;


    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {

        Intent restartServiceIntent = new Intent(getApplicationContext(), this.getClass());
        restartServiceIntent.setPackage(getPackageName());
        startService(restartServiceIntent);

        super.onTaskRemoved(rootIntent);
    }


    //for getting valid date

    public int getValidMonth(String month) {
        int retVal = 0;

        l:
        for (int i = 1; i <= prefManager.getVALID_MONTH().split("_").length; i++) {
            if (prefManager.getVALID_MONTH().split("_")[i - 1].equals(month)) {
                retVal = i;

                break l;
            }
        }

        return retVal;
    }

    public int getDaysWithoutWeekends(int day, int month, int year) {

        int retVal = 0;


        for (int i = day; i <= getTotalDayInMonth(month, year); i++) {

            Calendar calendar = new GregorianCalendar(year, month - 1, i); // Note that Month value is 0-based. e.g., 0 for January.
            int result = calendar.get(Calendar.DAY_OF_WEEK);

            if (result != Calendar.SUNDAY && result != Calendar.SATURDAY) {
                retVal++;
            }
        }

        return retVal;

    }

    //getting total day in a month
    public int getTotalDayInMonth(int month, int year) {

        int iYear = year;
        int iMonth = month - 1;
        int iDay = 1;

        //for getting number of day in specific month


        // Create a calendar object and set year and month
        Calendar mycal = new GregorianCalendar(iYear, iMonth, iDay);

        return mycal.getActualMaximum(Calendar.DAY_OF_MONTH);

    }
}
