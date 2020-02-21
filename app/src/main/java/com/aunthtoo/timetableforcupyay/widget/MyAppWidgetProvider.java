package com.aunthtoo.timetableforcupyay.widget;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.aunthtoo.timetableforcupyay.R;
import com.aunthtoo.timetableforcupyay.dbhandler.TimeTableDBHandler;
import com.aunthtoo.timetableforcupyay.model.DailyTime;
import com.aunthtoo.timetableforcupyay.prefmanager.PrefManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import me.myatminsoe.mdetect.MDetect;

/**
 * Created by Aunt Htoo on 12/2/2017.
 */

public class MyAppWidgetProvider extends AppWidgetProvider {

    public static String WIDGET_BUTTON = "com.aunthtoo.timetableforcupyay.WIDGET_BUTTON";
    private static final String ACTION_CLICK = "ACTION_CLICK";

    String buttonclickforabsent = "btnabsent", wholeday = "wholeday", offday = "offday";

    private String currentDateTimeString;
    //for date time
    private String day, month, year, hour, min, mDate, mTime;

    private PrefManager prefManager;
    private TimeTableDBHandler dbHandler;


    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        MDetect.INSTANCE.init(context);

        prefManager = new PrefManager(context);
        dbHandler = new TimeTableDBHandler(context);

    }

    @Override
    public void onUpdate(final Context context, final AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int count = appWidgetIds.length;

        MDetect.INSTANCE.init(context);

        prefManager = new PrefManager(context);
        dbHandler = new TimeTableDBHandler(context);

        //for initializing for uni zaw problem

        for (int i = 0; i < count; i++) {
            int widgetId = appWidgetIds[i];

            final RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                    R.layout.myappwidget_layout);

            final ComponentName thisWidget = new ComponentName(context, MyAppWidgetProvider.class);

            Intent intent = new Intent(context, MyAppWidgetProvider.class);
            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);


            // PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            //for setting up subject and teacher
            remoteViews.setTextViewText(R.id.subnamewid, MDetect.INSTANCE.getText(context.getResources().getString(R.string.subject)));
            remoteViews.setTextViewText(R.id.teachnamewid, MDetect.INSTANCE.getText(context.getResources().getString(R.string.teacher)));
            remoteViews.setTextViewText(R.id.absent, MDetect.INSTANCE.getText(context.getResources().getString(R.string.absent)));

            remoteViews.setTextViewText(R.id.isitoff, MDetect.INSTANCE.getText(context.getString(R.string.isifoff)));
            remoteViews.setTextViewText(R.id.isitwhole, MDetect.INSTANCE.getText(context.getString(R.string.wholedayabsent)));

            //remoteViews.setTextViewText(R.id.wholedayyesno, MDetect.INSTANCE.getText());
            //  remoteViews.setOnClickPendingIntent(R.id.click, getPendingSelfIntent(context, buttonclick, 0));

            //event handling
            remoteViews.setOnClickPendingIntent(R.id.absent, getPendingSelfIntent(context, buttonclickforabsent, 0));

            //for whole day absent
            remoteViews.setOnClickPendingIntent(R.id.wholedayyesno, getPendingSelfIntent(context, wholeday, 1));

            //for off day
            remoteViews.setOnClickPendingIntent(R.id.offdayyesno, getPendingSelfIntent(context, offday, 2));

            setUpCurrentTimeTable(context, thisWidget, remoteViews);
            //update within 5 second
            final Handler handler = new Handler();
            Timer timer = new Timer();

            TimerTask task = new TimerTask() {

                @Override
                public void run() {

                    handler.post(new Runnable() {
                        public void run() {

                            // Toast.makeText(context, "Timer", Toast.LENGTH_SHORT).show();

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

                            String tDay = "";

                            try {
                                SimpleDateFormat inFormat = new SimpleDateFormat("yyyy-MM-dd");
                                Date date = inFormat.parse(mDate);
                                SimpleDateFormat outFormat = new SimpleDateFormat("EEEE");
                                tDay = outFormat.format(date);

                            } catch (Exception e) {
                                Log.e("Error", e.getMessage());
                            }


                            setUpCurrentTimeTable(context, thisWidget, remoteViews);


                        }
                    });
                }
            };
            timer.scheduleAtFixedRate(task, 0, 10000);

            AppWidgetManager.getInstance(context).updateAppWidget(widgetId, remoteViews);


        }


    }


    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        MDetect.INSTANCE.init(context);

        prefManager = new PrefManager(context);
        dbHandler = new TimeTableDBHandler(context);

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.myappwidget_layout);
        ComponentName thisWidget = new ComponentName(context, MyAppWidgetProvider.class);

        //AppWidgetManager appWidgetManager;


        //for absent btn click
        if (intent.getAction() == buttonclickforabsent) {

            DailyTime dailyTime = getDailyTimeOnCurrentDate();

            String aorp = null;

            switch (getTimeWithCurrentTime()) {
                case 1:

                    aorp = dailyTime.getTimeone();

                    break;

                case 2:

                    aorp = dailyTime.getTimetwo();

                    break;

                case 3:

                    aorp = dailyTime.getTimethree();

                    break;

                case 4:

                    aorp = dailyTime.getTimefour();

                    break;

                case 5:

                    aorp = dailyTime.getTimefive();

                    break;

                case 6:

                    aorp = dailyTime.getTimesix();

                    break;

                case 7:

                    aorp = dailyTime.getTimeseven();

                    break;

                default:

                    Toast.makeText(context, "Something went wrong in getting current time", Toast.LENGTH_LONG).show();

                    break;


            }

            if (aorp.equals("p")) {


                remoteViews.setInt(R.id.absent, "setBackgroundResource", R.drawable.redbgforabsent);
                remoteViews.setTextViewText(R.id.absent, MDetect.INSTANCE.getText(context.getResources().getString(R.string.present)));

                remoteViews.setViewVisibility(R.id.forabsent, View.VISIBLE);

                updateAbsentOrPresent(dailyTime, getTimeWithCurrentTime(), "a");


            } else {

                remoteViews.setInt(R.id.absent, "setBackgroundResource", R.drawable.greenbgforpresent);
                remoteViews.setTextViewText(R.id.absent, MDetect.INSTANCE.getText(context.getResources().getString(R.string.absent)));


                remoteViews.setViewVisibility(R.id.forabsent, View.INVISIBLE);

                updateAbsentOrPresent(dailyTime, getTimeWithCurrentTime(), "p");


            }


            AppWidgetManager.getInstance(context).updateAppWidget(thisWidget, remoteViews);


        } else if (intent.getAction() == wholeday) {
            // Toast.makeText(context, "Whole day click", Toast.LENGTH_SHORT).show();

            if (!prefManager.getWholedayabsent()) {
                wholeDayAbsentClick(context, remoteViews);

            } else {
                wholeDayAbsentUnclick(context, remoteViews);

            }
            AppWidgetManager.getInstance(context).updateAppWidget(thisWidget, remoteViews);

        } else if (intent.getAction() == offday) {

            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            currentDateTimeString = df.format(Calendar.getInstance().getTime());


            // Toast.makeText(getContext(), currentDateTimeString, Toast.LENGTH_LONG).show();

            mDate = currentDateTimeString.split(" ")[0];
            mTime = currentDateTimeString.split(" ")[1];

            //for date
            year = mDate.split("-")[0];
            month = mDate.split("-")[1];
            day = mDate.split("-")[2];

            if (!dbHandler.isAlreadyOffDay(year + "-" + month + "-" + day)) {

                clickingOffday(context, remoteViews);
                deleteRecordWithDate(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day));
            } else {

                unclickingOffday(context, remoteViews);
                insertRecordWithDate(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day));

            }

            AppWidgetManager.getInstance(context).updateAppWidget(thisWidget, remoteViews);

            //Toast.makeText(context, "Off day click", Toast.LENGTH_SHORT).show();
        } else if (intent.getAction() == "act")
            Toast.makeText(context, "one minute", Toast.LENGTH_SHORT).show();
    }


    //for getting pending event
    protected PendingIntent getPendingSelfIntent(Context context, String action, int requestcode) {
        Intent intent = new Intent(context, getClass());
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, requestcode, intent, 0);
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


    //for addtional methods

    //setting up current time table

    public void setUpCurrentTimeTable(Context con, ComponentName componentName, RemoteViews remoteViews) {


        if (prefManager.getOffmonth() || dbHandler.isAlreadyOffDay(year + "-" + month + "-" + day)) {
            remoteViews.setBoolean(R.id.wholedayyesno, "setEnabled", false);
        } else {
            remoteViews.setBoolean(R.id.wholedayyesno, "setEnabled", true);
        }

        if (!prefManager.getWholedayabsent()) {
            remoteViews.setTextViewText(R.id.wholedayyesno, MDetect.INSTANCE.getText(con.getString(R.string.yes)));
            remoteViews.setInt(R.id.wholedayyesno, "setBackgroundResource", R.drawable.greenbgforpresent);

        } else {

            remoteViews.setTextViewText(R.id.wholedayyesno, MDetect.INSTANCE.getText(con.getString(R.string.no)));
            remoteViews.setInt(R.id.wholedayyesno, "setBackgroundResource", R.drawable.redbgforabsent);
        }


        int current = getTimeWithCurrentTime();

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

        String tDay = "";

        try {
            SimpleDateFormat inFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = inFormat.parse(mDate);
            SimpleDateFormat outFormat = new SimpleDateFormat("EEEE");
            tDay = outFormat.format(date);

        } catch (Exception e) {
            Log.e("Error", e.getMessage());
        }

        if (tDay.toLowerCase().substring(0, 3).equals("sun") || tDay.toLowerCase().substring(0, 3).equals("sat")) {
            remoteViews.setBoolean(R.id.wholedayyesno, "setEnabled", false);
            remoteViews.setBoolean(R.id.offdayyesno, "setEnabled", false);

        } else {

            remoteViews.setBoolean(R.id.wholedayyesno, "setEnabled", true);
            remoteViews.setBoolean(R.id.offdayyesno, "setEnabled", true);


        }

        if (dbHandler.isAlreadyOffDay(year + "-" + month + "-" + day)) {
            remoteViews.setTextViewText(R.id.offdayyesno, MDetect.INSTANCE.getText(con.getString(R.string.mahote)));
            remoteViews.setInt(R.id.offdayyesno, "setBackgroundResource", R.drawable.redbgforabsent);
        } else {

            remoteViews.setTextViewText(R.id.offdayyesno, MDetect.INSTANCE.getText(con.getString(R.string.yes)));
            remoteViews.setInt(R.id.offdayyesno, "setBackgroundResource", R.drawable.greenbgforpresent);

        }

        if (!prefManager.getOffmonth() && !prefManager.getWholedayabsent() && !tDay.toLowerCase().substring(0, 3).equals("sat") && !tDay.toLowerCase().substring(0, 3).equals("sun")) {


            if (!prefManager.isFirstTime()) {


                //for absent and present btn

                DailyTime dailyTime = getDailyTimeOnCurrentDate();

                String aorp = "";

                switch (getTimeWithCurrentTime()) {
                    case 1:

                        aorp = dailyTime.getTimeone();

                        break;

                    case 2:

                        aorp = dailyTime.getTimetwo();

                        break;

                    case 3:

                        aorp = dailyTime.getTimethree();

                        break;

                    case 4:

                        aorp = dailyTime.getTimefour();

                        break;

                    case 5:

                        aorp = dailyTime.getTimefive();

                        break;

                    case 6:

                        aorp = dailyTime.getTimesix();

                        break;

                    case 7:

                        aorp = dailyTime.getTimeseven();

                        break;

                    default:

                        // Toast.makeText(context, "Something went wrong in getting current time Please clear data for this app", Toast.LENGTH_LONG).show();

                        break;


                }


                if (aorp.equals("p")) {


                    remoteViews.setBoolean(R.id.absent, "setEnabled", true);
                    remoteViews.setInt(R.id.absent, "setBackgroundResource", R.drawable.greenbgforpresent);
                    remoteViews.setTextViewText(R.id.absent, MDetect.INSTANCE.getText(con.getResources().getString(R.string.absent)));

                    remoteViews.setViewVisibility(R.id.forabsent, View.INVISIBLE);


                } else if (aorp.equals("a")) {
                    remoteViews.setBoolean(R.id.absent, "setEnabled", true);
                    remoteViews.setInt(R.id.absent, "setBackgroundResource", R.drawable.redbgforabsent);
                    remoteViews.setTextViewText(R.id.absent, MDetect.INSTANCE.getText(con.getResources().getString(R.string.present)));

                    remoteViews.setViewVisibility(R.id.forabsent, View.VISIBLE);


                } else {


                    remoteViews.setBoolean(R.id.absent, "setEnabled", false);

                }


                //for absent and present btn end

                //for getting current day

                if (current == 0) {

                    //pauk ka ya time
                    remoteViews.setTextViewText(R.id.timetxt, "-");
                    remoteViews.setTextViewText(R.id.subwidget, "-");
                    remoteViews.setTextViewText(R.id.teacherwidget, "-");

                    //for testing
                    remoteViews.setBoolean(R.id.absent, "setEnabled", false);


                } else if (current == 8) {
                    //lunch time

                    remoteViews.setTextViewText(R.id.timetxt, MDetect.INSTANCE.getText(con.getResources().getString(R.string.lunchtime)));
                    remoteViews.setTextViewText(R.id.subwidget, "-");
                    remoteViews.setTextViewText(R.id.teacherwidget, "-");

                    remoteViews.setBoolean(R.id.absent, "setEnabled", false);


                } else {
                    List<String> subList = dbHandler.getShortSubjectNamewithday(tDay.toLowerCase().substring(0, 3));

                    if (subList.size() != 0) {

                        remoteViews.setTextViewText(R.id.timetxt, dbHandler.getIntervalWithNumber(current).getInterval());
                        remoteViews.setTextViewText(R.id.subwidget, subList.get(current - 1));
                        remoteViews.setTextViewText(R.id.teacherwidget, dbHandler.getTeacherNameWithShort(subList.get(current - 1)));

                        remoteViews.setBoolean(R.id.absent, "setEnabled", true);
                    } else {

                        remoteViews.setTextViewText(R.id.timetxt, "-");
                        remoteViews.setTextViewText(R.id.subwidget, "-");
                        remoteViews.setTextViewText(R.id.teacherwidget, "-");

                        remoteViews.setBoolean(R.id.absent, "setEnabled", false);

                    }
                }
            }


        } else {

            remoteViews.setTextViewText(R.id.timetxt, "-");
            remoteViews.setTextViewText(R.id.subwidget, "-");
            remoteViews.setTextViewText(R.id.teacherwidget, "-");

            remoteViews.setBoolean(R.id.absent, "setEnabled", false);


        }

        AppWidgetManager.getInstance(con).updateAppWidget(componentName, remoteViews);
    }


    //setting up current time table

    //for getting current time

    public int getTimeWithCurrentTime() {
        int retVal = 0;


        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = df.format(Calendar.getInstance().getTime());

        int hr = Integer.parseInt(date.split(" ")[1].split(":")[0]);
        int mi = Integer.parseInt(date.split(" ")[1].split(":")[1]);

        int currentTime = (hr * 60) + mi;


        //for time one
        int startTimeOne = changeExactTime(dbHandler.getIntervalWithNumber(1).getStartTime());
        int endTimeOne = changeExactTime(dbHandler.getIntervalWithNumber(1).getEndTime());

        //for time two
        int startTimeTwo = changeExactTime(dbHandler.getIntervalWithNumber(2).getStartTime());
        int endTimeTwo = changeExactTime(dbHandler.getIntervalWithNumber(2).getEndTime());

        //for time three
        int startTimeThree = changeExactTime(dbHandler.getIntervalWithNumber(3).getStartTime());
        int endTimeThree = changeExactTime(dbHandler.getIntervalWithNumber(3).getEndTime());

        //for time four
        int startTimeFour = changeExactTime(dbHandler.getIntervalWithNumber(4).getStartTime());
        int endTimeFour = changeExactTime(dbHandler.getIntervalWithNumber(4).getEndTime());

        //for time five
        int startTimeFive = changeExactTime(dbHandler.getIntervalWithNumber(5).getStartTime());
        int endTimeFive = changeExactTime(dbHandler.getIntervalWithNumber(5).getEndTime());

        //for time six
        int startTimeSix = changeExactTime(dbHandler.getIntervalWithNumber(6).getStartTime());
        int endTimeSix = changeExactTime(dbHandler.getIntervalWithNumber(6).getEndTime());

        //for time seven
        int startTimeSeven = changeExactTime(dbHandler.getIntervalWithNumber(7).getStartTime());
        int endTimeSeven = changeExactTime(dbHandler.getIntervalWithNumber(7).getEndTime());

        if (currentTime >= startTimeOne && currentTime <= endTimeOne) {
            retVal = 1;
        } else if (currentTime >= startTimeTwo && currentTime <= endTimeTwo) {
            retVal = 2;
        } else if (currentTime >= startTimeThree && currentTime <= endTimeThree) {
            retVal = 3;
        } else if (currentTime >= startTimeFour && currentTime <= endTimeFour) {
            retVal = 4;
        } else if (currentTime >= startTimeFive && currentTime <= endTimeFive) {
            retVal = 5;
        } else if (currentTime >= startTimeSix && currentTime <= endTimeSix) {
            retVal = 6;
        } else if (currentTime >= startTimeSeven && currentTime <= endTimeSeven) {
            retVal = 7;
        } else if (currentTime >= endTimeThree && currentTime <= startTimeFour) {
            retVal = 8;
        }

        return retVal;
    }


    //for getting current time

    //for changing exact time

    public int changeExactTime(String time) {
        int retVal = 0;

        // String myTime = time.toUpperCase();

        if (time.endsWith("AM")) {


            int hr = Integer.parseInt(time.substring(0, time.indexOf("AM") - 1).split(":")[0]);
            int mi = Integer.parseInt(time.substring(0, time.indexOf("AM") - 1).split(":")[1]);

            retVal = (hr * 60) + mi;


        } else if (time.endsWith("PM")) {

            if (Integer.parseInt(time.substring(0, time.indexOf("PM") - 1).split(":")[0]) == 12) {
                int hr = Integer.parseInt(time.substring(0, time.indexOf("PM") - 1).split(":")[0]);
                int mi = Integer.parseInt(time.substring(0, time.indexOf("PM") - 1).split(":")[1]);

                retVal = (hr * 60) + mi;
            } else {
                int hr = Integer.parseInt(time.substring(0, time.indexOf("PM") - 1).split(":")[0]);
                int mi = Integer.parseInt(time.substring(0, time.indexOf("PM") - 1).split(":")[1]);

                retVal = (hr * 60) + mi + (12 * 60);
            }

        }

        return retVal;
    }

    //for changing exact time

    //getting daily time on current date

    public DailyTime getDailyTimeOnCurrentDate() {

        DailyTime dailyTime = null;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        currentDateTimeString = df.format(Calendar.getInstance().getTime());


        // Toast.makeText(getContext(), currentDateTimeString, Toast.LENGTH_LONG).show();

        mDate = currentDateTimeString.split(" ")[0];
        mTime = currentDateTimeString.split(" ")[1];

        //for date
        year = mDate.split("-")[0];
        month = mDate.split("-")[1];
        day = mDate.split("-")[2];

        switch (getValidMonth(month)) {
            case 1:

                dailyTime = dbHandler.getMonthwithdateInMonthOne(year + "-" + month + "-" + day);

                break;


            case 2:

                dailyTime = dbHandler.getMonthwithdateInMonthTwo(year + "-" + month + "-" + day);

                break;

            case 3:

                dailyTime = dbHandler.getMonthwithdateInMonthThree(year + "-" + month + "-" + day);

                break;

            case 4:

                dailyTime = dbHandler.getMonthwithdateInMonthFour(year + "-" + month + "-" + day);

                break;

            case 5:

                dailyTime = dbHandler.getMonthwithdateInMonthFive(year + "-" + month + "-" + day);

                break;


            case 6:

                dailyTime = dbHandler.getMonthwithdateInMonthSix(year + "-" + month + "-" + day);

                break;

            case 7:

                dailyTime = dbHandler.getMonthwithdateInMonthSeven(year + "-" + month + "-" + day);

                break;

            case 8:

                dailyTime = dbHandler.getMonthwithdateInMonthEight(year + "-" + month + "-" + day);

                break;

        }

        return dailyTime;
    }

    //getting daily time on current date


    //updating absent and present
    public void updateAbsentOrPresent(DailyTime dailyTime, int whichTime, String aorp) {

        switch (whichTime) {
            case 1:

                dailyTime.setTimeone(aorp);
                break;


            case 2:

                dailyTime.setTimetwo(aorp);
                break;
            case 3:

                dailyTime.setTimethree(aorp);
                break;

            case 4:

                dailyTime.setTimefour(aorp);

                break;

            case 5:

                dailyTime.setTimefive(aorp);

                break;

            case 6:

                dailyTime.setTimesix(aorp);

                break;

            case 7:

                dailyTime.setTimeseven(aorp);

                break;

        }

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        currentDateTimeString = df.format(Calendar.getInstance().getTime());


        // Toast.makeText(getContext(), currentDateTimeString, Toast.LENGTH_LONG).show();

        mDate = currentDateTimeString.split(" ")[0];
        mTime = currentDateTimeString.split(" ")[1];

        //for date
        year = mDate.split("-")[0];
        month = mDate.split("-")[1];
        day = mDate.split("-")[2];

        switch (getValidMonth(month)) {
            case 1:

                dbHandler.updateMonthOne(dailyTime);

                break;


            case 2:

                dbHandler.updateMonthTwo(dailyTime);

                break;

            case 3:

                dbHandler.updateMonthThree(dailyTime);

                break;

            case 4:

                dbHandler.updateMonthFour(dailyTime);

                break;

            case 5:

                dbHandler.updateMonthFive(dailyTime);

                break;


            case 6:

                dbHandler.updateMonthSix(dailyTime);

                break;

            case 7:

                dbHandler.updateMonthSeven(dailyTime);

                break;

            case 8:

                dbHandler.updateMonthEight(dailyTime);

                break;

        }
    }


    //updating absent and present


    //whole day absent click
    public void wholeDayAbsentClick(Context context, RemoteViews remoteViews) {
        remoteViews.setTextViewText(R.id.wholedayyesno, MDetect.INSTANCE.getText(context.getString(R.string.no)));
        remoteViews.setInt(R.id.wholedayyesno, "setBackgroundResource", R.drawable.redbgforabsent);


        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        currentDateTimeString = df.format(Calendar.getInstance().getTime());


        // Toast.makeText(getContext(), currentDateTimeString, Toast.LENGTH_LONG).show();

        mDate = currentDateTimeString.split(" ")[0];
        mTime = currentDateTimeString.split(" ")[1];

        //for date
        year = mDate.split("-")[0];
        month = mDate.split("-")[1];
        day = mDate.split("-")[2];


        switch (getValidMonth(month)) {
            case 1:

                dbHandler.initializeWithNewDateinmonthoneAbsent(year + "-" + month + "-" + day);

                break;

            case 2:

                dbHandler.initializeWithNewDateinmonthtwoabsent(year + "-" + month + "-" + day);

                break;

            case 3:

                dbHandler.initializeWithNewDateinmonththreeabsent(year + "-" + month + "-" + day);

                break;

            case 4:
                dbHandler.initializeWithNewDateinmonthfourabsent(year + "-" + month + "-" + day);

                break;


            case 5:

                dbHandler.initializeWithNewDateinmonthfiveabsent(year + "-" + month + "-" + day);

                break;

            case 6:

                dbHandler.initializeWithNewDateinmonthsixabsent(year + "-" + month + "-" + day);


                break;

            case 7:

                dbHandler.initializeWithNewDateinmonthsevenabsent(year + "-" + month + "-" + day);

                break;

            case 8:

                dbHandler.initializeWithNewDateinmontheightabsent(year + "-" + month + "-" + day);

                break;

        }


        prefManager.setWholedayabsent(true);
    }

    //for whole day absent unclick
    public void wholeDayAbsentUnclick(Context context, RemoteViews remoteViews) {

        remoteViews.setTextViewText(R.id.wholedayyesno, MDetect.INSTANCE.getText(context.getString(R.string.yes)));
        remoteViews.setInt(R.id.wholedayyesno, "setBackgroundResource", R.drawable.greenbgforpresent);


        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        currentDateTimeString = df.format(Calendar.getInstance().getTime());


        // Toast.makeText(getContext(), currentDateTimeString, Toast.LENGTH_LONG).show();

        mDate = currentDateTimeString.split(" ")[0];
        mTime = currentDateTimeString.split(" ")[1];

        //for date
        year = mDate.split("-")[0];
        month = mDate.split("-")[1];
        day = mDate.split("-")[2];


        prefManager.setWholedayabsent(false);

        int cTime = getTimeWithCurrentTime();

        if (cTime == 0)
            cTime = 1;
        else if (cTime == 8)
            cTime = 4;

        String[] currentArray = new String[7];

        currentArray[0] = getDailyTimeOnCurrentDate().getTimeone();
        currentArray[1] = getDailyTimeOnCurrentDate().getTimetwo();
        currentArray[2] = getDailyTimeOnCurrentDate().getTimethree();
        currentArray[3] = getDailyTimeOnCurrentDate().getTimefour();
        currentArray[4] = getDailyTimeOnCurrentDate().getTimefive();
        currentArray[5] = getDailyTimeOnCurrentDate().getTimesix();
        currentArray[6] = getDailyTimeOnCurrentDate().getTimeseven();

        for (int i = cTime - 1; i < currentArray.length; i++) {
            currentArray[i] = "p";
        }

        DailyTime dailyTime = new DailyTime(getDailyTimeOnCurrentDate().getDate(), currentArray[0], currentArray[1], currentArray[2], currentArray[3], currentArray[4], currentArray[5], currentArray[6]);

        switch (getValidMonth(month)) {
            case 1:

                dbHandler.updateMonthOne(dailyTime);
                break;

            case 2:

                dbHandler.updateMonthTwo(dailyTime);

                break;

            case 3:

                dbHandler.updateMonthThree(dailyTime);

                break;

            case 4:

                dbHandler.updateMonthFour(dailyTime);

                break;

            case 5:

                dbHandler.updateMonthFive(dailyTime);

                break;
            case 6:

                dbHandler.updateMonthSix(dailyTime);

                break;

            case 7:

                dbHandler.updateMonthSeven(dailyTime);

                break;

            case 8:

                dbHandler.updateMonthEight(dailyTime);

                break;


        }


    }


    //for clicking off day

    public void clickingOffday(Context context, RemoteViews remoteViews) {

        remoteViews.setTextViewText(R.id.offdayyesno, MDetect.INSTANCE.getText(context.getString(R.string.mahote)));
        remoteViews.setInt(R.id.offdayyesno, "setBackgroundResource", R.drawable.redbgforabsent);


    }

    //for clicking off day

    //for unclicking off day

    public void unclickingOffday(Context context, RemoteViews remoteViews) {

        remoteViews.setTextViewText(R.id.offdayyesno, MDetect.INSTANCE.getText(context.getString(R.string.yes)));
        remoteViews.setInt(R.id.offdayyesno, "setBackgroundResource", R.drawable.greenbgforpresent);

    }

    //for unclicking off day


    public void insertRecordWithDate(int y, int m, int d) {
        String forday;

        if (String.valueOf(d).length() == 1)
            forday = "0" + d;
        else
            forday = d + "";

        String formonth;
        if ((m + "").length() == 1)
            formonth = "0" + m;
        else
            formonth = m + "";

        switch (getValidMonth(m + "")) {
            case 1:

                dbHandler.initializeWithNewDateinmonthone(y + "-" + formonth + "-" + forday);
                prefManager.setMonthonetotaldaytoattend(prefManager.getMonthonetotaldaytoattend() + 1);

                break;

            case 2:

                dbHandler.initializeWithNewDateinmonthtwo(y + "-" + formonth + "-" + forday);
                prefManager.setMonthtwototaldaytoattend(prefManager.getMonthtwototaldaytoattend() + 1);

                break;

            case 3:


                dbHandler.initializeWithNewDateinmonththree(y + "-" + formonth + "-" + forday);
                prefManager.setMonththreetotaldaytoattend(prefManager.getMonththreetotaldaytoattend() + 1);

                break;

            case 4:


                dbHandler.initializeWithNewDateinmonthfour(y + "-" + formonth + "-" + forday);
                prefManager.setMonthfourtotaldaytoattend(prefManager.getMonthfourtotaldaytoattend() + 1);

                break;

            case 5:


                dbHandler.initializeWithNewDateinmonthfive(y + "-" + formonth + "-" + forday);
                prefManager.setMonthfivetotaldaytoattend(prefManager.getMonthfivetotaldaytoattend() + 1);

                break;

            case 6:

                dbHandler.initializeWithNewDateinmonthsix(y + "-" + formonth + "-" + forday);
                prefManager.setMonthsixtotaldaytoattend(prefManager.getMonthsixtotaldaytoattend() + 1);

                break;

            case 7:

                dbHandler.initializeWithNewDateinmonthseven(y + "-" + formonth + "-" + forday);
                prefManager.setMonthseventotaldaytoattend(prefManager.getMonthseventotaldaytoattend() + 1);

                break;

            case 8:


                dbHandler.initializeWithNewDateinmontheight(y + "-" + formonth + "-" + forday);
                prefManager.setMontheighttotaldaytoattend(prefManager.getMontheighttotaldaytoattend() + 1);

                break;

        }

        dbHandler.deleteOffDay(y + "-" + formonth + "-" + forday);


    }


    public void deleteRecordWithDate(int y, int m, int d) {
        String forday;

        if (String.valueOf(d).length() == 1)
            forday = "0" + d;
        else
            forday = d + "";

        String formonth;
        if ((m + "").length() == 1)
            formonth = "0" + m;
        else
            formonth = m + "";

        switch (getValidMonth(m + "")) {
            case 1:

                dbHandler.deleteRecordInMonthOne(y + "-" + formonth + "-" + forday);
                prefManager.setMonthonetotaldaytoattend(prefManager.getMonthonetotaldaytoattend() - 1);

                break;

            case 2:

                dbHandler.deleteRecordInMonthTwo(y + "-" + formonth + "-" + forday);
                prefManager.setMonthtwototaldaytoattend(prefManager.getMonthtwototaldaytoattend() - 1);

                break;

            case 3:


                dbHandler.deleteRecordInMonthThree(y + "-" + formonth + "-" + forday);
                prefManager.setMonththreetotaldaytoattend(prefManager.getMonththreetotaldaytoattend() - 1);

                break;

            case 4:

                dbHandler.deleteRecordInMonthFour(y + "-" + formonth + "-" + forday);
                prefManager.setMonthfourtotaldaytoattend(prefManager.getMonthfourtotaldaytoattend() - 1);

                break;

            case 5:


                dbHandler.deleteRecordInMonthFive(y + "-" + formonth + "-" + forday);
                prefManager.setMonthfivetotaldaytoattend(prefManager.getMonthfivetotaldaytoattend() - 1);

                break;

            case 6:

                dbHandler.deleteRecordInMonthSix(y + "-" + formonth + "-" + forday);
                prefManager.setMonthsixtotaldaytoattend(prefManager.getMonthsixtotaldaytoattend() - 1);

                break;

            case 7:

                dbHandler.deleteRecordInMonthSeven(y + "-" + formonth + "-" + forday);
                prefManager.setMonthseventotaldaytoattend(prefManager.getMonthseventotaldaytoattend() - 1);

                break;

            case 8:


                dbHandler.deleteRecordInMonthEight(y + "-" + formonth + "-" + forday);
                prefManager.setMontheighttotaldaytoattend(prefManager.getMontheighttotaldaytoattend() - 1);

                break;

        }

        dbHandler.insertOffDay(y + "-" + formonth + "-" + forday);

    }
}
