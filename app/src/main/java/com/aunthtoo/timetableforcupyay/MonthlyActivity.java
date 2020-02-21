package com.aunthtoo.timetableforcupyay;

import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.exceptions.OutOfDateRangeException;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.applandeo.materialcalendarview.listeners.OnNavigationButtonClickListener;
import com.aunthtoo.timetableforcupyay.dbhandler.TimeTableDBHandler;
import com.aunthtoo.timetableforcupyay.model.DailyTime;
import com.aunthtoo.timetableforcupyay.prefmanager.PrefManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import me.myatminsoe.mdetect.MMButtonView;
import me.myatminsoe.mdetect.MMTextView;

public class MonthlyActivity extends AppCompatActivity implements OnDayClickListener {

    private CalendarView calendarView;

    private String monthname, year, day;
    private Calendar calendarObj;

    private TimeTableDBHandler dbHandler;
    private PrefManager prefManager;

    //for edit dialog
    private AlertDialog editDialog;
    private View editView;

    //for edit dialog components
    private MMTextView dayname, isitoff;
    private MMButtonView toedit;
    private SwitchCompat offswitch;
    private TextView time1, time2, time3, time4, time5, time6, time7;
    private CheckBox ctime1, ctime2, ctime3, ctime4, ctime5, ctime6, ctime7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly);

        dbHandler = new TimeTableDBHandler(this);
        prefManager = new PrefManager(this);

        //getting day name from intent
        monthname = getIntent().getStringExtra("monthnum");
        year = getIntent().getStringExtra("year");
        day = getIntent().getStringExtra("day");


        // Toast.makeText(this, monthname, Toast.LENGTH_LONG).show();

        //variable initializing
        calendarView = findViewById(R.id.monthlycalendar);
        calendarView.setOnDayClickListener(this);

        //setup calendarview

        calendarObj = Calendar.getInstance();
        calendarObj.set(Integer.parseInt(year), Integer.parseInt(monthname), 0);


        setDateForCalendarView();
        setupCalendarWithMark();

        calendarView.setOnForwardButtonClickListener(new OnNavigationButtonClickListener() {
            @Override
            public void onClick() {
                //empty

                MonthlyActivity.this.finish();

            }
        });

        calendarView.setOnPreviousButtonClickListener(new OnNavigationButtonClickListener() {
            @Override
            public void onClick() {
                //empty

                MonthlyActivity.this.finish();

            }
        });
    }


    //for on day click


    @Override
    public void onDayClick(EventDay eventDay) {

        Calendar calendar = eventDay.getCalendar();

        int yyyy = calendar.get(Calendar.YEAR);
        int mm = calendar.get(Calendar.MONTH) + 1;
        int dd = calendar.get(Calendar.DAY_OF_MONTH);


        Calendar cal = new GregorianCalendar(yyyy, mm - 1, dd); // Note that Month value is 0-based. e.g., 0 for January.
        int result = cal.get(Calendar.DAY_OF_WEEK);


        if (result == Calendar.SUNDAY || result == Calendar.SATURDAY) {
            Toast.makeText(this, "It is off day!", Toast.LENGTH_SHORT).show();
        } else {

            //for getting current date
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentDateTimeString = df.format(Calendar.getInstance().getTime());

            String mDate = currentDateTimeString.split(" ")[0];
            String mTime = currentDateTimeString.split(" ")[1];

            //for date
            String yyy = mDate.split("-")[0];
            String mmm = mDate.split("-")[1];
            String ddd = mDate.split("-")[2];


            Date date1 = null;
            Date date2 = null;


            try {


                SimpleDateFormat dates = new SimpleDateFormat("yyyy/MM/dd");

                //Setting dates
                date1 = dates.parse(yyyy + "/" + mm + "/" + dd);
                date2 = dates.parse(yyy + "/" + mmm + "/" + ddd);

            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            }

            if (date1.getTime() <= date2.getTime()) {
                showEditDialog(getMMDayName(result), yyyy, mm, dd);
            }

        }
    }

    //for edit dialog

    public void showEditDialog(String dayn, final int yy, final int month, final int day) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        editDialog = builder.create();

        LayoutInflater inflater = getLayoutInflater();
        editView = inflater.inflate(R.layout.month_edit_dialog, null);

        //setting view
        editDialog.setView(editView);

        //setting cancelable false
        editDialog.setCancelable(false);

        //for animation

        editDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        editDialog.getWindow()
                .getAttributes().windowAnimations = R.style.DialogAnimation;

        //variable initialize for edit view
        Button btnClose = editView.findViewById(R.id.mclose);

        dayname = editView.findViewById(R.id.daynameinmonthly);
        isitoff = editView.findViewById(R.id.isitoffday);

        toedit = editView.findViewById(R.id.toedit);

        offswitch = editView.findViewById(R.id.offdayswitch);

        time1 = editView.findViewById(R.id.onesub);
        time2 = editView.findViewById(R.id.twosub);
        time3 = editView.findViewById(R.id.threesub);
        time4 = editView.findViewById(R.id.foursub);
        time5 = editView.findViewById(R.id.fivesub);
        time6 = editView.findViewById(R.id.sixsub);
        time7 = editView.findViewById(R.id.sevensub);

        ctime1 = editView.findViewById(R.id.onecheck);
        ctime2 = editView.findViewById(R.id.twocheck);
        ctime3 = editView.findViewById(R.id.threecheck);
        ctime4 = editView.findViewById(R.id.fourcheck);
        ctime5 = editView.findViewById(R.id.fivecheck);
        ctime6 = editView.findViewById(R.id.sixcheck);
        ctime7 = editView.findViewById(R.id.sevencheck);

        String daynn = "";
        try {
            SimpleDateFormat inFormat = new SimpleDateFormat("yyyy-MM-dd");

            String formonth = "";
            if (String.valueOf(month).length() == 1)
                formonth = "0" + month;
            else
                formonth = month + "";

            Date date = inFormat.parse(yy + "-" + formonth + "-" + day);
            SimpleDateFormat outFormat = new SimpleDateFormat("EEEE");
            daynn = outFormat.format(date).toLowerCase().substring(0, 3);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        List<String> sublist = dbHandler.getShortSubjectNamewithday(daynn);

        //variable handling for edit view
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editDialog.dismiss();
            }
        });

        //setting time
        time1.setText(sublist.get(0));
        time2.setText(sublist.get(1));
        time3.setText(sublist.get(2));
        time4.setText(sublist.get(3));
        time5.setText(sublist.get(4));
        time6.setText(sublist.get(5));
        time7.setText(sublist.get(6));

        //for real event handling
        dayname.setMMText(dayn);
        isitoff.setMMText(dayn + getString(R.string.isitoffday));

        if (checkOffDayWithDate(yy, month, day)) {
            ctime1.setEnabled(false);
            ctime2.setEnabled(false);
            ctime3.setEnabled(false);
            ctime4.setEnabled(false);
            ctime5.setEnabled(false);
            ctime6.setEnabled(false);
            ctime7.setEnabled(false);

            toedit.setEnabled(false);

            offswitch.setChecked(true);

        } else {

            ctime1.setEnabled(true);
            ctime2.setEnabled(true);
            ctime3.setEnabled(true);
            ctime4.setEnabled(true);
            ctime5.setEnabled(true);
            ctime6.setEnabled(true);
            ctime7.setEnabled(true);

            toedit.setEnabled(true);

            offswitch.setChecked(false);

            //setting check box
            setUpCheckBox(yy, month, day);


        }


        toedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] bArray = new String[7];

                if (ctime1.isChecked())
                    bArray[0] = "p";
                else
                    bArray[0] = "a";

                if (ctime2.isChecked())
                    bArray[1] = "p";
                else
                    bArray[1] = "a";

                if (ctime3.isChecked())
                    bArray[2] = "p";
                else
                    bArray[2] = "a";

                if (ctime4.isChecked())
                    bArray[3] = "p";
                else
                    bArray[3] = "a";

                if (ctime5.isChecked())
                    bArray[4] = "p";
                else
                    bArray[4] = "a";

                if (ctime6.isChecked())
                    bArray[5] = "p";
                else
                    bArray[5] = "a";

                if (ctime7.isChecked())
                    bArray[6] = "p";
                else
                    bArray[6] = "a";

                setupedit(bArray, yy, month, day);

                setupCalendarWithMark();
                editDialog.dismiss();

            }
        });


        final String forday;

        if (String.valueOf(day).length() == 1)
            forday = "0" + day;
        else
            forday = day + "";

        final String formonth;
        if ((month + "").length() == 1)
            formonth = "0" + month;
        else
            formonth = month + "";


        //event handling for switch

        offswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {

                    if (!checkOffDayWithDate(yy, month, day)) {
                        deleteRecordWithDate(yy, month, day);
                    }

                    setupCalendarWithMark();
                    editDialog.dismiss();

                } else {


                    if (checkOffDayWithDate(yy, month, day)) {
                        insertRecordWithDate(yy, month, day);
                    }

                    setupCalendarWithMark();
                    editDialog.dismiss();

                }
            }
        });
        //event handling for switch


        //showing edit dialog
        editDialog.show();

    }

    //for edit dialog end

    //for deleting record


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

    //for deleting record

    //for inserting record

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


    //for inserting record

    //for setting up edit

    public void setupedit(String bol[], int y, int m, int d) {

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

        DailyTime dailyTime = new DailyTime(y + "-" + formonth + "-" + forday, bol[0], bol[1], bol[2], bol[3], bol[4], bol[5], bol[6]);

        boolean check = false;

        int ck = 0;
        for (int i = 0; i < bol.length; i++) {
            if (bol[i].equals("a"))
                ck++;

        }

        if (bol.length == ck)
            check = true;

        //for getting current date
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentDateTimeString = df.format(Calendar.getInstance().getTime());

        String mDate = currentDateTimeString.split(" ")[0];
        // String mTime = currentDateTimeString.split(" ")[1];

        //for date
        String yyy = mDate.split("-")[0];
        String mmm = mDate.split("-")[1];
        String ddd = mDate.split("-")[2];


        if ((yyy + "-" + mmm + "-" + ddd).equals(y + "-" + formonth + "-" + forday))
            if (check)
                prefManager.setWholedayabsent(true);
            else
                prefManager.setWholedayabsent(false);

        switch (getValidMonth(m + "")) {
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

    //for setting up edit

    //for calendar view date setting
    public void setDateForCalendarView() {
        try {
            calendarView.setDate(calendarObj);
        } catch (OutOfDateRangeException e) {
            Log.e("TAg", e.getMessage());
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void setupCalendarWithMark() {

        List<EventDay> list = new ArrayList<>();

        for (int i = 1; i <= getTotalDayInMonth(Integer.parseInt(monthname), Integer.parseInt(year)); i++) {

            Calendar cal = new GregorianCalendar(Integer.parseInt(year), Integer.parseInt(monthname) - 1, i); // Note that Month value is 0-based. e.g., 0 for January.


            if (isOffDay(year, monthname, i + "")) {
                EventDay eventDay = new EventDay(cal, R.drawable.ic_close);
                list.add(eventDay);

            }

            if (isPresentDay(year, monthname, i + "")) {
                EventDay eventDay = new EventDay(cal, R.drawable.ic_present);
                list.add(eventDay);
            }

            if (isAbsentDay(year, monthname, i + "")) {
                EventDay eventDay = new EventDay(cal, R.drawable.ic_absent);
                list.add(eventDay);
            }

        }


        calendarView.setEvents(list);
    }

    //for setting checkboox
    public void setUpCheckBox(int y, int m, int d) {

        DailyTime dailyTimes = null;

        switch (getValidMonth(m + "")) {
            case 1:
                String forday1;
                if (String.valueOf(d).length() == 1)
                    forday1 = "0" + d;
                else
                    forday1 = d + "";

                String formonth1;
                if ((m + "").length() == 1)
                    formonth1 = "0" + m;
                else
                    formonth1 = m + "";

                dailyTimes = dbHandler.getMonthwithdateInMonthOne(y + "-" + formonth1 + "-" + forday1);

                break;

            case 2:

                String forday2;
                if (String.valueOf(d).length() == 1)
                    forday2 = "0" + d;
                else
                    forday2 = d + "";

                String formonth2;
                if ((m + "").length() == 1)
                    formonth2 = "0" + m;
                else
                    formonth2 = m + "";

                dailyTimes = dbHandler.getMonthwithdateInMonthOne(y + "-" + formonth2 + "-" + forday2);

                break;

            case 3:


                String forday3;
                if (String.valueOf(d).length() == 1)
                    forday3 = "0" + d;
                else
                    forday3 = d + "";

                String formonth3;
                if ((m + "").length() == 1)
                    formonth3 = "0" + m;
                else
                    formonth3 = m + "";


                dailyTimes = dbHandler.getMonthwithdateInMonthOne(y + "-" + formonth3 + "-" + forday3);

                break;

            case 4:


                String forday4;
                if (String.valueOf(d).length() == 1)
                    forday4 = "0" + d;
                else
                    forday4 = d + "";

                String formonth4;
                if ((m + "").length() == 1)
                    formonth4 = "0" + m;
                else
                    formonth4 = m + "";

                dailyTimes = dbHandler.getMonthwithdateInMonthOne(y + "-" + formonth4 + "-" + forday4);

                break;

            case 5:


                String forday5;
                if (String.valueOf(d).length() == 1)
                    forday5 = "0" + d;
                else
                    forday5 = d + "";

                String formonth5;
                if ((m + "").length() == 1)
                    formonth5 = "0" + m;
                else
                    formonth5 = m + "";

                dailyTimes = dbHandler.getMonthwithdateInMonthOne(y + "-" + formonth5 + "-" + forday5);

                break;

            case 6:


                String forday6;
                if (String.valueOf(d).length() == 1)
                    forday6 = "0" + d;
                else
                    forday6 = d + "";

                String formonth6;
                if ((m + "").length() == 1)
                    formonth6 = "0" + m;
                else
                    formonth6 = m + "";


                dailyTimes = dbHandler.getMonthwithdateInMonthOne(y + "-" + formonth6 + "-" + forday6);

                break;

            case 7:


                String forday7;
                if (String.valueOf(d).length() == 1)
                    forday7 = "0" + d;
                else
                    forday7 = d + "";

                String formonth7;
                if ((m + "").length() == 1)
                    formonth7 = "0" + m;
                else
                    formonth7 = m + "";

                dailyTimes = dbHandler.getMonthwithdateInMonthOne(y + "-" + formonth7 + "-" + forday7);

                break;

            case 8:


                String forday8;
                if (String.valueOf(d).length() == 1)
                    forday8 = "0" + d;
                else
                    forday8 = d + "";

                String formonth8;
                if ((m + "").length() == 1)
                    formonth8 = "0" + m;
                else
                    formonth8 = m + "";

                dailyTimes = dbHandler.getMonthwithdateInMonthOne(y + "-" + formonth8 + "-" + forday8);

                break;

        }

        if (dailyTimes.getTimeone().equals("p"))
            ctime1.setChecked(true);
        else
            ctime1.setChecked(false);

        if (dailyTimes.getTimetwo().equals("p"))
            ctime2.setChecked(true);
        else
            ctime2.setChecked(false);

        if (dailyTimes.getTimethree().equals("p"))
            ctime3.setChecked(true);
        else
            ctime3.setChecked(false);

        if (dailyTimes.getTimefour().equals("p"))
            ctime4.setChecked(true);
        else
            ctime4.setChecked(false);

        if (dailyTimes.getTimefive().equals("p"))
            ctime5.setChecked(true);
        else
            ctime5.setChecked(false);

        if (dailyTimes.getTimesix().equals("p"))
            ctime6.setChecked(true);
        else
            ctime6.setChecked(false);

        if (dailyTimes.getTimeseven().equals("p"))
            ctime7.setChecked(true);
        else
            ctime7.setChecked(false);


    }

    //for setting check box

    //for checking offday for dialog

    public boolean checkOffDayWithDate(int y, int m, int d) {
        boolean retVal = false;


        switch (getValidMonth(m + "")) {
            case 1:
                String forday1;
                if (String.valueOf(d).length() == 1)
                    forday1 = "0" + d;
                else
                    forday1 = d + "";

                String formonth1;
                if ((m + "").length() == 1)
                    formonth1 = "0" + m;
                else
                    formonth1 = m + "";

                if (!dbHandler.isAlreadyExistThisDateInMonthOne(y + "-" + formonth1 + "-" + forday1)) {
                    retVal = true;
                }

                break;

            case 2:

                String forday2;
                if (String.valueOf(d).length() == 1)
                    forday2 = "0" + d;
                else
                    forday2 = d + "";

                String formonth2;
                if ((m + "").length() == 1)
                    formonth2 = "0" + m;
                else
                    formonth2 = m + "";

                if (!dbHandler.isAlreadyExistThisDateInMonthTwo(y + "-" + formonth2 + "-" + forday2)) {
                    retVal = true;
                }


                break;

            case 3:


                String forday3;
                if (String.valueOf(d).length() == 1)
                    forday3 = "0" + d;
                else
                    forday3 = d + "";

                String formonth3;
                if ((m + "").length() == 1)
                    formonth3 = "0" + m;
                else
                    formonth3 = m + "";


                if (!dbHandler.isAlreadyExistThisDateInMonthThree(y + "-" + formonth3 + "-" + forday3)) {
                    retVal = true;
                }

                break;

            case 4:


                String forday4;
                if (String.valueOf(d).length() == 1)
                    forday4 = "0" + d;
                else
                    forday4 = d + "";

                String formonth4;
                if ((m + "").length() == 1)
                    formonth4 = "0" + m;
                else
                    formonth4 = m + "";

                if (!dbHandler.isAlreadyExistThisDateInMonthFour(y + "-" + formonth4 + "-" + forday4)) {
                    retVal = true;
                }

                break;

            case 5:


                String forday5;
                if (String.valueOf(d).length() == 1)
                    forday5 = "0" + d;
                else
                    forday5 = d + "";

                String formonth5;
                if ((m + "").length() == 1)
                    formonth5 = "0" + m;
                else
                    formonth5 = m + "";

                if (!dbHandler.isAlreadyExistThisDateInMonthFive(y + "-" + formonth5 + "-" + forday5)) {
                    retVal = true;
                }

                break;

            case 6:


                String forday6;
                if (String.valueOf(d).length() == 1)
                    forday6 = "0" + d;
                else
                    forday6 = d + "";

                String formonth6;
                if ((m + "").length() == 1)
                    formonth6 = "0" + m;
                else
                    formonth6 = m + "";


                if (!dbHandler.isAlreadyExistThisDateInMonthSix(y + "-" + formonth6 + "-" + forday6)) {
                    retVal = true;
                }

                break;

            case 7:


                String forday7;
                if (String.valueOf(d).length() == 1)
                    forday7 = "0" + d;
                else
                    forday7 = d + "";

                String formonth7;
                if ((m + "").length() == 1)
                    formonth7 = "0" + m;
                else
                    formonth7 = m + "";

                if (!dbHandler.isAlreadyExistThisDateInMonthSeven(y + "-" + formonth7 + "-" + forday7)) {
                    retVal = true;
                }

                break;

            case 8:


                String forday8;
                if (String.valueOf(d).length() == 1)
                    forday8 = "0" + d;
                else
                    forday8 = d + "";

                String formonth8;
                if ((m + "").length() == 1)
                    formonth8 = "0" + m;
                else
                    formonth8 = m + "";

                if (!dbHandler.isAlreadyExistThisDateInMonthEight(y + "-" + formonth8 + "-" + forday8)) {
                    retVal = true;
                }

                break;

        }

        return retVal;
    }
    //for checking offday for dialog

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

    //for valid date
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


    //for checking off day
    public boolean isOffDay(String y, String m, String d) {
        boolean retVal = false;

        Calendar cal = new GregorianCalendar(Integer.parseInt(y), Integer.parseInt(m) - 1, Integer.parseInt(d)); // Note that Month value is 0-based. e.g., 0 for January.
        int result = cal.get(Calendar.DAY_OF_WEEK);


        if (result == Calendar.SUNDAY || result == Calendar.SATURDAY) {
            retVal = true;

        } else {

            //for getting current date
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentDateTimeString = df.format(Calendar.getInstance().getTime());

            String mDate = currentDateTimeString.split(" ")[0];
            String mTime = currentDateTimeString.split(" ")[1];

            //for date
            String yyy = mDate.split("-")[0];
            String mmm = mDate.split("-")[1];
            String ddd = mDate.split("-")[2];


            Date date1 = null;
            Date date2 = null;


            try {


                SimpleDateFormat dates = new SimpleDateFormat("yyyy/MM/dd");

                //Setting dates
                date1 = dates.parse(y + "/" + m + "/" + d);
                date2 = dates.parse(yyy + "/" + mmm + "/" + ddd);

            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            }

            if (date1.getTime() <= date2.getTime()) {

                switch (getValidMonth(m)) {
                    case 1:
                        String forday1 = "";
                        if ((Integer.parseInt(d) + "").length() == 1)
                            forday1 = "0" + Integer.parseInt(d);
                        else
                            forday1 = Integer.parseInt(d) + "";

                        String formonth1;
                        if ((m + "").length() == 1)
                            formonth1 = "0" + m;
                        else
                            formonth1 = m + "";

                        if (!dbHandler.isAlreadyExistThisDateInMonthOne(y + "-" + formonth1 + "-" + forday1)) {
                            retVal = true;
                        }

                        break;

                    case 2:

                        String forday2 = "";
                        if ((Integer.parseInt(d) + "").length() == 1)
                            forday2 = "0" + Integer.parseInt(d);
                        else
                            forday2 = Integer.parseInt(d) + "";

                        String formonth2;
                        if ((m + "").length() == 1)
                            formonth2 = "0" + m;
                        else
                            formonth2 = m + "";

                        if (!dbHandler.isAlreadyExistThisDateInMonthTwo(y + "-" + formonth2 + "-" + forday2)) {
                            retVal = true;
                        }


                        break;

                    case 3:


                        String forday3 = "";
                        if ((Integer.parseInt(d) + "").length() == 1)
                            forday3 = "0" + Integer.parseInt(d);
                        else
                            forday3 = Integer.parseInt(d) + "";

                        String formonth3;
                        if ((m + "").length() == 1)
                            formonth3 = "0" + m;
                        else
                            formonth3 = m + "";


                        if (!dbHandler.isAlreadyExistThisDateInMonthThree(y + "-" + formonth3 + "-" + forday3)) {
                            retVal = true;
                        }

                        break;

                    case 4:


                        String forday4 = "";
                        if ((Integer.parseInt(d) + "").length() == 1)
                            forday4 = "0" + Integer.parseInt(d);
                        else
                            forday4 = Integer.parseInt(d) + "";

                        String formonth4;
                        if ((m + "").length() == 1)
                            formonth4 = "0" + m;
                        else
                            formonth4 = m + "";

                        if (!dbHandler.isAlreadyExistThisDateInMonthFour(y + "-" + formonth4 + "-" + forday4)) {
                            retVal = true;
                        }

                        break;

                    case 5:


                        String forday5 = "";
                        if ((Integer.parseInt(d) + "").length() == 1)
                            forday5 = "0" + Integer.parseInt(d);
                        else
                            forday5 = Integer.parseInt(d) + "";

                        String formonth5;
                        if ((m + "").length() == 1)
                            formonth5 = "0" + m;
                        else
                            formonth5 = m + "";

                        if (!dbHandler.isAlreadyExistThisDateInMonthFive(y + "-" + formonth5 + "-" + forday5)) {
                            retVal = true;
                        }

                        break;

                    case 6:


                        String forday6 = "";
                        if ((Integer.parseInt(d) + "").length() == 1)
                            forday6 = "0" + Integer.parseInt(d);
                        else
                            forday6 = Integer.parseInt(d) + "";

                        String formonth6;
                        if ((m + "").length() == 1)
                            formonth6 = "0" + m;
                        else
                            formonth6 = m + "";


                        if (!dbHandler.isAlreadyExistThisDateInMonthSix(y + "-" + formonth6 + "-" + forday6)) {
                            retVal = true;
                        }

                        break;

                    case 7:


                        String forday7 = "";
                        if ((Integer.parseInt(d) + "").length() == 1)
                            forday7 = "0" + Integer.parseInt(d);
                        else
                            forday7 = Integer.parseInt(d) + "";

                        String formonth7;
                        if ((m + "").length() == 1)
                            formonth7 = "0" + m;
                        else
                            formonth7 = m + "";

                        if (!dbHandler.isAlreadyExistThisDateInMonthSeven(y + "-" + formonth7 + "-" + forday7)) {
                            retVal = true;
                        }

                        break;

                    case 8:


                        String forday8 = "";
                        if ((Integer.parseInt(d) + "").length() == 1)
                            forday8 = "0" + Integer.parseInt(d);
                        else
                            forday8 = Integer.parseInt(d) + "";

                        String formonth8;
                        if ((m + "").length() == 1)
                            formonth8 = "0" + m;
                        else
                            formonth8 = m + "";

                        if (!dbHandler.isAlreadyExistThisDateInMonthEight(y + "-" + formonth8 + "-" + forday8)) {
                            retVal = true;
                        }

                        break;

                }

            }

        }
        return retVal;
    }

    //off day end

    //for checking present day
    public boolean isPresentDay(String y, String m, String d) {
        boolean retVal = false;

        Calendar cal = new GregorianCalendar(Integer.parseInt(y), Integer.parseInt(m) - 1, Integer.parseInt(d)); // Note that Month value is 0-based. e.g., 0 for January.
        int result = cal.get(Calendar.DAY_OF_WEEK);


        //for getting current date
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentDateTimeString = df.format(Calendar.getInstance().getTime());

        String mDate = currentDateTimeString.split(" ")[0];
        String mTime = currentDateTimeString.split(" ")[1];

        //for date
        String yyy = mDate.split("-")[0];
        String mmm = mDate.split("-")[1];
        String ddd = mDate.split("-")[2];


        Date date1 = null;
        Date date2 = null;


        try {


            SimpleDateFormat dates = new SimpleDateFormat("yyyy/MM/dd");

            //Setting dates
            date1 = dates.parse(y + "/" + m + "/" + d);
            date2 = dates.parse(yyy + "/" + mmm + "/" + ddd);

        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        if (date1.getTime() <= date2.getTime()) {

            switch (getValidMonth(m)) {
                case 1:
                    String forday1 = "";
                    if ((Integer.parseInt(d) + "").length() == 1)
                        forday1 = "0" + Integer.parseInt(d);
                    else
                        forday1 = Integer.parseInt(d) + "";

                    String formonth1 = "";

                    if ((m + "").length() == 1)
                        formonth1 = "0" + m;
                    else
                        formonth1 = m + "";

                    if (dbHandler.isAlreadyExistThisDateInMonthOne(y + "-" + formonth1 + "-" + forday1)) {
                        DailyTime dailyTime = dbHandler.getMonthwithdateInMonthOne(y + "-" + formonth1 + "-" + forday1);
                        if (dailyTime.getTimeone().equals("p") && dailyTime.getTimetwo().equals("p") && dailyTime.getTimethree().equals("p") && dailyTime.getTimefour().equals("p") && dailyTime.getTimefive().equals("p") && dailyTime.getTimesix().equals("p") && dailyTime.getTimeseven().equals("p"))
                            retVal = true;
                    }

                    break;

                case 2:

                    String forday2 = "";
                    if ((Integer.parseInt(d) + "").length() == 1)
                        forday2 = "0" + Integer.parseInt(d);
                    else
                        forday2 = Integer.parseInt(d) + "";

                    String formonth2 = "";

                    if ((m + "").length() == 1)
                        formonth2 = "0" + m;
                    else
                        formonth2 = m + "";


                    if (dbHandler.isAlreadyExistThisDateInMonthTwo(y + "-" + formonth2 + "-" + forday2)) {
                        DailyTime dailyTime = dbHandler.getMonthwithdateInMonthTwo(y + "-" + formonth2 + "-" + forday2);
                        if (dailyTime.getTimeone().equals("p") && dailyTime.getTimetwo().equals("p") && dailyTime.getTimethree().equals("p") && dailyTime.getTimefour().equals("p") && dailyTime.getTimefive().equals("p") && dailyTime.getTimesix().equals("p") && dailyTime.getTimeseven().equals("p"))
                            retVal = true;
                    }


                    break;

                case 3:


                    String forday3 = "";
                    if ((Integer.parseInt(d) + "").length() == 1)
                        forday3 = "0" + Integer.parseInt(d);
                    else
                        forday3 = Integer.parseInt(d) + "";

                    String formonth3 = "";

                    if ((m + "").length() == 1)
                        formonth3 = "0" + m;
                    else
                        formonth3 = m + "";


                    if (dbHandler.isAlreadyExistThisDateInMonthThree(y + "-" + formonth3 + "-" + forday3)) {
                        DailyTime dailyTime = dbHandler.getMonthwithdateInMonthThree(y + "-" + formonth3 + "-" + forday3);
                        if (dailyTime.getTimeone().equals("p") && dailyTime.getTimetwo().equals("p") && dailyTime.getTimethree().equals("p") && dailyTime.getTimefour().equals("p") && dailyTime.getTimefive().equals("p") && dailyTime.getTimesix().equals("p") && dailyTime.getTimeseven().equals("p"))
                            retVal = true;
                    }

                    break;

                case 4:


                    String forday4 = "";
                    if ((Integer.parseInt(d) + "").length() == 1)
                        forday4 = "0" + Integer.parseInt(d);
                    else
                        forday4 = Integer.parseInt(d) + "";

                    String formonth4 = "";

                    if ((m + "").length() == 1)
                        formonth4 = "0" + m;
                    else
                        formonth4 = m + "";

                    if (dbHandler.isAlreadyExistThisDateInMonthFour(y + "-" + formonth4 + "-" + forday4)) {
                        DailyTime dailyTime = dbHandler.getMonthwithdateInMonthFour(y + "-" + formonth4 + "-" + forday4);
                        if (dailyTime.getTimeone().equals("p") && dailyTime.getTimetwo().equals("p") && dailyTime.getTimethree().equals("p") && dailyTime.getTimefour().equals("p") && dailyTime.getTimefive().equals("p") && dailyTime.getTimesix().equals("p") && dailyTime.getTimeseven().equals("p"))
                            retVal = true;
                    }

                    break;

                case 5:


                    String forday5 = "";
                    if ((Integer.parseInt(d) + "").length() == 1)
                        forday5 = "0" + Integer.parseInt(d);
                    else
                        forday5 = Integer.parseInt(d) + "";

                    String formonth5 = "";

                    if ((m + "").length() == 1)
                        formonth5 = "0" + m;
                    else
                        formonth5 = m + "";

                    if (dbHandler.isAlreadyExistThisDateInMonthFive(y + "-" + formonth5 + "-" + forday5)) {
                        DailyTime dailyTime = dbHandler.getMonthwithdateInMonthFive(y + "-" + formonth5 + "-" + forday5);
                        if (dailyTime.getTimeone().equals("p") && dailyTime.getTimetwo().equals("p") && dailyTime.getTimethree().equals("p") && dailyTime.getTimefour().equals("p") && dailyTime.getTimefive().equals("p") && dailyTime.getTimesix().equals("p") && dailyTime.getTimeseven().equals("p"))
                            retVal = true;
                    }

                    break;

                case 6:


                    String forday6 = "";
                    if ((Integer.parseInt(d) + "").length() == 1)
                        forday6 = "0" + Integer.parseInt(d);
                    else
                        forday6 = Integer.parseInt(d) + "";

                    String formonth6 = "";

                    if ((m + "").length() == 1)
                        formonth6 = "0" + m;
                    else
                        formonth6 = m + "";


                    if (dbHandler.isAlreadyExistThisDateInMonthSix(y + "-" + formonth6 + "-" + forday6)) {
                        DailyTime dailyTime = dbHandler.getMonthwithdateInMonthSix(y + "-" + formonth6 + "-" + forday6);
                        if (dailyTime.getTimeone().equals("p") && dailyTime.getTimetwo().equals("p") && dailyTime.getTimethree().equals("p") && dailyTime.getTimefour().equals("p") && dailyTime.getTimefive().equals("p") && dailyTime.getTimesix().equals("p") && dailyTime.getTimeseven().equals("p"))
                            retVal = true;
                    }

                    break;

                case 7:


                    String forday7 = "";
                    if ((Integer.parseInt(d) + "").length() == 1)
                        forday7 = "0" + Integer.parseInt(d);
                    else
                        forday7 = Integer.parseInt(d) + "";

                    String formonth7 = "";

                    if ((m + "").length() == 1)
                        formonth7 = "0" + m;
                    else
                        formonth7 = m + "";


                    if (dbHandler.isAlreadyExistThisDateInMonthSeven(y + "-" + formonth7 + "-" + forday7)) {
                        DailyTime dailyTime = dbHandler.getMonthwithdateInMonthSeven(y + "-" + formonth7 + "-" + forday7);
                        if (dailyTime.getTimeone().equals("p") && dailyTime.getTimetwo().equals("p") && dailyTime.getTimethree().equals("p") && dailyTime.getTimefour().equals("p") && dailyTime.getTimefive().equals("p") && dailyTime.getTimesix().equals("p") && dailyTime.getTimeseven().equals("p"))
                            retVal = true;
                    }

                    break;

                case 8:


                    String forday8 = "";
                    if ((Integer.parseInt(d) + "").length() == 1)
                        forday8 = "0" + Integer.parseInt(d);
                    else
                        forday8 = Integer.parseInt(d) + "";

                    String formonth8 = "";

                    if ((m + "").length() == 1)
                        formonth8 = "0" + m;
                    else
                        formonth8 = m + "";

                    if (dbHandler.isAlreadyExistThisDateInMonthEight(y + "-" + formonth8 + "-" + forday8)) {
                        DailyTime dailyTime = dbHandler.getMonthwithdateInMonthEight(y + "-" + formonth8 + "-" + forday8);
                        if (dailyTime.getTimeone().equals("p") && dailyTime.getTimetwo().equals("p") && dailyTime.getTimethree().equals("p") && dailyTime.getTimefour().equals("p") && dailyTime.getTimefive().equals("p") && dailyTime.getTimesix().equals("p") && dailyTime.getTimeseven().equals("p"))
                            retVal = true;
                    }

                    break;

            }

        }


        return retVal;
    }

    //for checking present end

    //for checking absent
    public boolean isAbsentDay(String y, String m, String d) {
        boolean retVal = false;

        Calendar cal = new GregorianCalendar(Integer.parseInt(y), Integer.parseInt(m) - 1, Integer.parseInt(d)); // Note that Month value is 0-based. e.g., 0 for January.
        int result = cal.get(Calendar.DAY_OF_WEEK);


        //for getting current date
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentDateTimeString = df.format(Calendar.getInstance().getTime());

        String mDate = currentDateTimeString.split(" ")[0];
        String mTime = currentDateTimeString.split(" ")[1];

        //for date
        String yyy = mDate.split("-")[0];
        String mmm = mDate.split("-")[1];
        String ddd = mDate.split("-")[2];


        Date date1 = null;
        Date date2 = null;


        try {


            SimpleDateFormat dates = new SimpleDateFormat("yyyy/MM/dd");

            //Setting dates
            date1 = dates.parse(y + "/" + m + "/" + d);
            date2 = dates.parse(yyy + "/" + mmm + "/" + ddd);

        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        if (date1.getTime() <= date2.getTime()) {

            switch (getValidMonth(m)) {
                case 1:
                    String forday1 = "";
                    if ((Integer.parseInt(d) + "").length() == 1)
                        forday1 = "0" + Integer.parseInt(d);
                    else
                        forday1 = Integer.parseInt(d) + "";

                    String formonth1 = "";

                    if ((m + "").length() == 1)
                        formonth1 = "0" + m;
                    else
                        formonth1 = m + "";


                    if (dbHandler.isAlreadyExistThisDateInMonthOne(y + "-" + formonth1 + "-" + forday1)) {
                        DailyTime dailyTime = dbHandler.getMonthwithdateInMonthOne(y + "-" + formonth1 + "-" + forday1);
                        if (dailyTime.getTimeone().equals("a") || dailyTime.getTimetwo().equals("a") || dailyTime.getTimethree().equals("a") || dailyTime.getTimefour().equals("a") || dailyTime.getTimefive().equals("a") || dailyTime.getTimesix().equals("a") || dailyTime.getTimeseven().equals("a"))
                            retVal = true;
                    }

                    break;

                case 2:

                    String forday2 = "";
                    if ((Integer.parseInt(d) + "").length() == 1)
                        forday2 = "0" + Integer.parseInt(d);
                    else
                        forday2 = Integer.parseInt(d) + "";

                    String formonth2 = "";

                    if ((m + "").length() == 1)
                        formonth2 = "0" + m;
                    else
                        formonth2 = m + "";

                    if (dbHandler.isAlreadyExistThisDateInMonthTwo(y + "-" + formonth2 + "-" + forday2)) {
                        DailyTime dailyTime = dbHandler.getMonthwithdateInMonthTwo(y + "-" + formonth2 + "-" + forday2);
                        if (dailyTime.getTimeone().equals("a") || dailyTime.getTimetwo().equals("a") || dailyTime.getTimethree().equals("a") || dailyTime.getTimefour().equals("a") || dailyTime.getTimefive().equals("a") || dailyTime.getTimesix().equals("a") || dailyTime.getTimeseven().equals("a"))
                            retVal = true;
                    }


                    break;

                case 3:


                    String forday3 = "";
                    if ((Integer.parseInt(d) + "").length() == 1)
                        forday3 = "0" + Integer.parseInt(d);
                    else
                        forday3 = Integer.parseInt(d) + "";

                    String formonth3 = "";

                    if ((m + "").length() == 1)
                        formonth3 = "0" + m;
                    else
                        formonth3 = m + "";


                    if (dbHandler.isAlreadyExistThisDateInMonthThree(y + "-" + formonth3 + "-" + forday3)) {
                        DailyTime dailyTime = dbHandler.getMonthwithdateInMonthThree(y + "-" + formonth3 + "-" + forday3);
                        if (dailyTime.getTimeone().equals("a") || dailyTime.getTimetwo().equals("a") || dailyTime.getTimethree().equals("a") || dailyTime.getTimefour().equals("a") || dailyTime.getTimefive().equals("a") || dailyTime.getTimesix().equals("a") || dailyTime.getTimeseven().equals("a"))
                            retVal = true;
                    }

                    break;

                case 4:


                    String forday4 = "";
                    if ((Integer.parseInt(d) + "").length() == 1)
                        forday4 = "0" + Integer.parseInt(d);
                    else
                        forday4 = Integer.parseInt(d) + "";

                    String formonth4 = "";

                    if ((m + "").length() == 1)
                        formonth4 = "0" + m;
                    else
                        formonth4 = m + "";


                    if (dbHandler.isAlreadyExistThisDateInMonthFour(y + "-" + formonth4 + "-" + forday4)) {
                        DailyTime dailyTime = dbHandler.getMonthwithdateInMonthFour(y + "-" + formonth4 + "-" + forday4);
                        if (dailyTime.getTimeone().equals("a") || dailyTime.getTimetwo().equals("a") || dailyTime.getTimethree().equals("a") || dailyTime.getTimefour().equals("a") || dailyTime.getTimefive().equals("a") || dailyTime.getTimesix().equals("a") || dailyTime.getTimeseven().equals("a"))
                            retVal = true;
                    }

                    break;

                case 5:


                    String forday5 = "";
                    if ((Integer.parseInt(d) + "").length() == 1)
                        forday5 = "0" + Integer.parseInt(d);
                    else
                        forday5 = Integer.parseInt(d) + "";

                    String formonth5 = "";

                    if ((m + "").length() == 1)
                        formonth5 = "0" + m;
                    else
                        formonth5 = m + "";

                    if (dbHandler.isAlreadyExistThisDateInMonthFive(y + "-" + formonth5 + "-" + forday5)) {
                        DailyTime dailyTime = dbHandler.getMonthwithdateInMonthFive(y + "-" + formonth5 + "-" + forday5);
                        if (dailyTime.getTimeone().equals("a") || dailyTime.getTimetwo().equals("a") || dailyTime.getTimethree().equals("a") || dailyTime.getTimefour().equals("a") || dailyTime.getTimefive().equals("a") || dailyTime.getTimesix().equals("a") || dailyTime.getTimeseven().equals("a"))
                            retVal = true;
                    }

                    break;

                case 6:


                    String forday6 = "";
                    if ((Integer.parseInt(d) + "").length() == 1)
                        forday6 = "0" + Integer.parseInt(d);
                    else
                        forday6 = Integer.parseInt(d) + "";

                    String formonth6 = "";

                    if ((m + "").length() == 1)
                        formonth6 = "0" + m;
                    else
                        formonth6 = m + "";


                    if (dbHandler.isAlreadyExistThisDateInMonthSix(y + "-" + formonth6 + "-" + forday6)) {
                        DailyTime dailyTime = dbHandler.getMonthwithdateInMonthSix(y + "-" + formonth6 + "-" + forday6);
                        if (dailyTime.getTimeone().equals("a") || dailyTime.getTimetwo().equals("a") || dailyTime.getTimethree().equals("a") || dailyTime.getTimefour().equals("a") || dailyTime.getTimefive().equals("a") || dailyTime.getTimesix().equals("a") || dailyTime.getTimeseven().equals("a"))
                            retVal = true;
                    }

                    break;

                case 7:


                    String forday7 = "";
                    if ((Integer.parseInt(d) + "").length() == 1)
                        forday7 = "0" + Integer.parseInt(d);
                    else
                        forday7 = Integer.parseInt(d) + "";

                    String formonth7 = "";

                    if ((m + "").length() == 1)
                        formonth7 = "0" + m;
                    else
                        formonth7 = m + "";

                    if (dbHandler.isAlreadyExistThisDateInMonthSeven(y + "-" + formonth7 + "-" + forday7)) {
                        DailyTime dailyTime = dbHandler.getMonthwithdateInMonthSeven(y + "-" + formonth7 + "-" + forday7);
                        if (dailyTime.getTimeone().equals("a") || dailyTime.getTimetwo().equals("a") || dailyTime.getTimethree().equals("a") || dailyTime.getTimefour().equals("a") || dailyTime.getTimefive().equals("a") || dailyTime.getTimesix().equals("a") || dailyTime.getTimeseven().equals("a"))
                            retVal = true;
                    }

                    break;

                case 8:


                    String forday8 = "";
                    if ((Integer.parseInt(d) + "").length() == 1)
                        forday8 = "0" + Integer.parseInt(d);
                    else
                        forday8 = Integer.parseInt(d) + "";

                    String formonth8 = "";

                    if ((m + "").length() == 1)
                        formonth8 = "0" + m;
                    else
                        formonth8 = m + "";

                    if (dbHandler.isAlreadyExistThisDateInMonthEight(y + "-" + formonth8 + "-" + forday8)) {
                        DailyTime dailyTime = dbHandler.getMonthwithdateInMonthEight(y + "-" + formonth8 + "-" + forday8);
                        if (dailyTime.getTimeone().equals("a") || dailyTime.getTimetwo().equals("a") || dailyTime.getTimethree().equals("a") || dailyTime.getTimefour().equals("a") || dailyTime.getTimefive().equals("a") || dailyTime.getTimesix().equals("a") || dailyTime.getTimeseven().equals("a"))
                            retVal = true;
                    }

                    break;

            }

        }


        return retVal;
    }

    //for checking absent end

    //for getting day name with result
    public String getMMDayName(int res) {
        String retVal = "";

        switch (res) {
            case Calendar.MONDAY:

                retVal = getString(R.string.mon);

                break;


            case Calendar.TUESDAY:

                retVal = getString(R.string.tue);

                break;

            case Calendar.WEDNESDAY:

                retVal = getString(R.string.wed);

                break;

            case Calendar.THURSDAY:

                retVal = getString(R.string.thu);

                break;

            case Calendar.FRIDAY:

                retVal = getString(R.string.fri);

                break;


        }

        return retVal;
    }
}
