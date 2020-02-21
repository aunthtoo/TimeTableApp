package com.aunthtoo.timetableforcupyay.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.exceptions.OutOfDateRangeException;
import com.aunthtoo.timetableforcupyay.MainPage;
import com.aunthtoo.timetableforcupyay.R;
import com.aunthtoo.timetableforcupyay.WeekTimeTable;
import com.aunthtoo.timetableforcupyay.alarm.AlarmReceiver;
import com.aunthtoo.timetableforcupyay.dbhandler.TimeTableDBHandler;
import com.aunthtoo.timetableforcupyay.model.DailyTime;
import com.aunthtoo.timetableforcupyay.prefmanager.PrefManager;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.nineoldandroids.animation.Animator;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import me.myatminsoe.mdetect.MDetect;
import me.myatminsoe.mdetect.MMButtonView;
import me.myatminsoe.mdetect.MMTextView;
import me.toptas.fancyshowcase.DismissListener;
import me.toptas.fancyshowcase.FancyShowCaseQueue;
import me.toptas.fancyshowcase.FancyShowCaseView;
import me.toptas.fancyshowcase.OnCompleteListener;

/**
 * Created by Aunt Htoo on 11/17/2017.
 */

public class FragTimeTable extends Fragment implements View.OnClickListener {
    private View view;
    private MMButtonView btnAdsentAndPresent, seeweektimetable;
    private TextView txtDate, txtForabsent, txtToday;

    boolean CHECK_PRESENT_ABSENT = true;

    private String currentDateTimeString;

    //for indicator
    private ImageView indicator1, indicator2, indicator3, indicator4, indicator5, indicator6, indicator7;
    private TextView indicatortxt1, indicatortxt2, indicatortxt3, indicatortxt4, indicatortxt5, indicatortxt6, indicatortxt7;

    private RelativeLayout time1, time2, time3, time4, time5, time6, time7;
    private TextView[] subtime;

    private Switch wholeswitch;

    //for sharedpref
    PrefManager prefManager;

    //for date time
    String day, month, year, hour, min, mDate, mTime;


    //for subject dialog
    AlertDialog subjectDialog, welcomeDialog, previousDayTracking;

    //for welcome diallog
    private View welcomeView, previousDayView, subjectView;

    //for show case
    private FancyShowCaseQueue mQueue;

    //for calendar view
    private CalendarView calendar;

    int checkcalendarpicker = 0;

    //for previous absent day
    private RadioGroup rdPrevious;
    private RadioButton[] rdButtonArray;

    private AlarmManager am;
    private PendingIntent pendingIntent1, pendingIntent2, pendingIntent3, pendingIntent4, pendingIntent5, pendingIntent6, pendingIntent7;

    //for previous
    AlertDialog.Builder builderforprevious;
    LayoutInflater inflaterprevious;

    Context context;

    private SwitchCompat getNoti;

    private TimeTableDBHandler dbHandler;

    String goal;

    private TextView txtTimeInterval, txtSubject, txtTeacher;

    public FragTimeTable() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        prefManager = new PrefManager(getContext());

        dbHandler = new TimeTableDBHandler(getContext());

        builderforprevious = new AlertDialog.Builder(getActivity());
        inflaterprevious = getLayoutInflater();

        context = getContext();

        am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_timetable, container, false);

        MDetect.INSTANCE.init(getContext());

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


        if (prefManager.isFirstTime()) {
            showDatePickerDialog();
            //for first time
        }

        if (prefManager.getSHOWCASE1_START_TIME_PICK()) {
            startShowCase();
        }


        DailyTime time = dbHandler.getMonthwithdateInMonthOne(year + "-" + month + "-" + 4);


        //initializing for indicator
        indicator1 = view.findViewById(R.id.time1);
        indicator2 = view.findViewById(R.id.time2);
        indicator3 = view.findViewById(R.id.time3);
        indicator4 = view.findViewById(R.id.time4);
        indicator5 = view.findViewById(R.id.time5);
        indicator6 = view.findViewById(R.id.time6);
        indicator7 = view.findViewById(R.id.time7);

        indicatortxt1 = view.findViewById(R.id.txtInd1);
        indicatortxt2 = view.findViewById(R.id.txtInd2);
        indicatortxt3 = view.findViewById(R.id.txtInd3);
        indicatortxt4 = view.findViewById(R.id.txtInd4);
        indicatortxt5 = view.findViewById(R.id.txtInd5);
        indicatortxt6 = view.findViewById(R.id.txtInd6);
        indicatortxt7 = view.findViewById(R.id.txtInd7);

        txtTimeInterval = view.findViewById(R.id.subject);
        txtSubject = view.findViewById(R.id.sublong);
        txtTeacher = view.findViewById(R.id.teacher);


        //initialize for time subjecg
        time1 = view.findViewById(R.id.tOne);
        time2 = view.findViewById(R.id.tTwo);
        time3 = view.findViewById(R.id.tThree);
        time4 = view.findViewById(R.id.tFour);
        time5 = view.findViewById(R.id.tFive);
        time6 = view.findViewById(R.id.tSix);
        time7 = view.findViewById(R.id.tSeven);

        //on click register to time
        time1.setOnClickListener(this);
        time2.setOnClickListener(this);
        time3.setOnClickListener(this);
        time4.setOnClickListener(this);
        time5.setOnClickListener(this);
        time6.setOnClickListener(this);
        time7.setOnClickListener(this);

        //for today time table initializing
        subtime = new TextView[7];
        subtime[0] = view.findViewById(R.id.time1sub);
        subtime[1] = view.findViewById(R.id.time2sub);
        subtime[2] = view.findViewById(R.id.time3sub);
        subtime[3] = view.findViewById(R.id.time4sub);
        subtime[4] = view.findViewById(R.id.time5sub);
        subtime[5] = view.findViewById(R.id.time6sub);
        subtime[6] = view.findViewById(R.id.time7sub);


        /*setYellow(3);
        setRed(5);*/


        wholeswitch = view.findViewById(R.id.wholedayabsent);

        if (prefManager.getOffmonth() || dbHandler.isAlreadyOffDay(year + "-" + month + "-" + day)) {
            wholeswitch.setEnabled(false);
        } else {
            wholeswitch.setEnabled(true);
        }

        if (!prefManager.getWholedayabsent()) {
            wholeswitch.setText(MDetect.INSTANCE.getText(context.getString(R.string.yes)));
            wholeswitch.setChecked(false);
        } else {
            wholeswitch.setText(MDetect.INSTANCE.getText(context.getString(R.string.no)));
            wholeswitch.setChecked(true);
        }

        wholeswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    AlertDialog warningDialog = builder.create();

                    warningDialog.setTitle(MDetect.INSTANCE.getText(getString(R.string.warning)));
                    warningDialog.setMessage(MDetect.INSTANCE.getText(getString(R.string.warningq)));
                    warningDialog.setCancelable(false);

                    warningDialog.setButton(DialogInterface.BUTTON_POSITIVE, MDetect.INSTANCE.getText(getString(R.string.wyes)), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {


                            wholeswitch.setChecked(true);
                            wholeswitch.setText(MDetect.INSTANCE.getText(getString(R.string.no)));

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

                            setUpCurrentTimeTable();
                            setUpTodaySevenTime();
                        }
                    });

                    warningDialog.setButton(DialogInterface.BUTTON_NEGATIVE, MDetect.INSTANCE.getText(getString(R.string.wno)), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            wholeswitch.setChecked(false);
                            wholeswitch.setText(MDetect.INSTANCE.getText(getString(R.string.yes)));

                            setUpCurrentTimeTable();
                            setUpTodaySevenTime();
                        }
                    });

                    prefManager.setWholedayabsent(true);

                    setUpCurrentTimeTable();

                    warningDialog.show();

                } else {


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

                    setUpCurrentTimeTable();

                    wholeswitch.setText(MDetect.INSTANCE.getText(getString(R.string.yes)));

                    setUpCurrentTimeTable();
                    setUpTodaySevenTime();
                }
            }
        });


        txtDate = view.findViewById(R.id.date);
        txtDate.setText(changeMonthstr(Integer.parseInt(month)) + " " + day + ", " + year);

        btnAdsentAndPresent = view.findViewById(R.id.absent);
        seeweektimetable = view.findViewById(R.id.viewtimetableforallday);

        txtForabsent = view.findViewById(R.id.forabsent);


        btnAdsentAndPresent.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

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


                    btnAdsentAndPresent.setBackgroundResource(R.drawable.redbgforabsent);
                    btnAdsentAndPresent.setText(MDetect.INSTANCE.getText(getResources().getString(R.string.present)));

                    txtForabsent.setVisibility(View.VISIBLE);

                    updateAbsentOrPresent(dailyTime, getTimeWithCurrentTime(), "a");

                    setRed(getTimeWithCurrentTime());

                } else {
                    btnAdsentAndPresent.setBackgroundResource(R.drawable.greenbgforpresent);
                    btnAdsentAndPresent.setText(MDetect.INSTANCE.getText(getResources().getString(R.string.absent)));

                    txtForabsent.setVisibility(View.INVISIBLE);

                    updateAbsentOrPresent(dailyTime, getTimeWithCurrentTime(), "p");

                    setGreen(getTimeWithCurrentTime());
                }
            }
        });

        seeweektimetable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), WeekTimeTable.class));
            }
        });

        txtToday = view.findViewById(R.id.todaytimetable);

        try {
            SimpleDateFormat inFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = inFormat.parse(mDate);
            SimpleDateFormat outFormat = new SimpleDateFormat("EEEE");
            goal = outFormat.format(date);


            // Toast.makeText(context, goal.toLowerCase().substring(0, 3), Toast.LENGTH_LONG).show();

            if (goal.toLowerCase().substring(0, 3).equals("sat") || dbHandler.isAlreadyOffDay(year + "-" + month + "-" + day)) {

                txtToday.setText(changeEngToMM(goal) + MDetect.INSTANCE.getText(getString(R.string.offday)));
                wholeswitch.setEnabled(false);

            } else if (goal.toLowerCase().substring(0, 3).equals("sun") || dbHandler.isAlreadyOffDay(year + "-" + month + "-" + day)) {

                txtToday.setText(changeEngToMM(goal) + MDetect.INSTANCE.getText(getString(R.string.offday)));
                wholeswitch.setEnabled(false);

            } else {
                txtToday.setText(changeEngToMM(goal) + MDetect.INSTANCE.getText(getString(R.string.todaytimetable)));
                wholeswitch.setEnabled(true);
            }

        } catch (Exception e) {
            Log.e("Error", e.getMessage());
        }

        if (dbHandler.getShortSubjectNamewithday(goal.toLowerCase().substring(0, 3)).size() == 0 || dbHandler.isAlreadyOffDay(year + "-" + month + "-" + day))
            wholeswitch.setEnabled(false);
        else
            wholeswitch.setEnabled(true);


        if (prefManager.isFirstTime()) {
            Handler hand = new Handler();
            hand.postDelayed(new Runnable() {
                @Override
                public void run() {
                    showIndicator(getTimeWithCurrentTime());
                    setUpCurrentTimeTable();
                    setUpTodaySevenTime();
                }
            }, 5000);
        } else {
            showIndicator(getTimeWithCurrentTime());
            setUpCurrentTimeTable();
            setUpTodaySevenTime();
        }

        updateTimeTableEveryOneMinute();


        return view;
    }


    public void showIndicator(int idx) {

        String tDay = null;

        try {
            SimpleDateFormat inFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = inFormat.parse(mDate);
            SimpleDateFormat outFormat = new SimpleDateFormat("EEEE");
            tDay = outFormat.format(date);

        } catch (Exception e) {
            Log.e("Error", e.getMessage());
        }

        if (!tDay.toLowerCase().substring(0, 3).equals("sat") && !tDay.toLowerCase().substring(0, 3).equals("sun") && !dbHandler.isAlreadyOffDay(year + "-" + month + "-" + day)) {

            switch (idx) {
                case 0:

                    indicator1.setVisibility(View.INVISIBLE);
                    indicator2.setVisibility(View.INVISIBLE);
                    indicator3.setVisibility(View.INVISIBLE);
                    indicator4.setVisibility(View.INVISIBLE);
                    indicator5.setVisibility(View.INVISIBLE);
                    indicator6.setVisibility(View.INVISIBLE);
                    indicator7.setVisibility(View.INVISIBLE);

                    indicatortxt1.setTextColor(context.getResources().getColor(R.color.tviewcolor));
                    indicatortxt2.setTextColor(context.getResources().getColor(R.color.tviewcolor));
                    indicatortxt3.setTextColor(context.getResources().getColor(R.color.tviewcolor));
                    indicatortxt4.setTextColor(context.getResources().getColor(R.color.tviewcolor));
                    indicatortxt5.setTextColor(context.getResources().getColor(R.color.tviewcolor));
                    indicatortxt6.setTextColor(context.getResources().getColor(R.color.tviewcolor));
                    indicatortxt7.setTextColor(context.getResources().getColor(R.color.tviewcolor));

                    break;

                case 1:

                    indicator1.setVisibility(View.VISIBLE);
                    indicator2.setVisibility(View.INVISIBLE);
                    indicator3.setVisibility(View.INVISIBLE);
                    indicator4.setVisibility(View.INVISIBLE);
                    indicator5.setVisibility(View.INVISIBLE);
                    indicator6.setVisibility(View.INVISIBLE);
                    indicator7.setVisibility(View.INVISIBLE);

                    indicatortxt1.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    indicatortxt2.setTextColor(context.getResources().getColor(R.color.tviewcolor));
                    indicatortxt3.setTextColor(context.getResources().getColor(R.color.tviewcolor));
                    indicatortxt4.setTextColor(context.getResources().getColor(R.color.tviewcolor));
                    indicatortxt5.setTextColor(context.getResources().getColor(R.color.tviewcolor));
                    indicatortxt6.setTextColor(context.getResources().getColor(R.color.tviewcolor));
                    indicatortxt7.setTextColor(context.getResources().getColor(R.color.tviewcolor));

                    prefManager.setAlarmtime1(false);

                    break;

                case 2:

                    indicator1.setVisibility(View.INVISIBLE);
                    indicator2.setVisibility(View.VISIBLE);
                    indicator3.setVisibility(View.INVISIBLE);
                    indicator4.setVisibility(View.INVISIBLE);
                    indicator5.setVisibility(View.INVISIBLE);
                    indicator6.setVisibility(View.INVISIBLE);
                    indicator7.setVisibility(View.INVISIBLE);

                    indicatortxt1.setTextColor(context.getResources().getColor(R.color.tviewcolor));
                    indicatortxt2.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    indicatortxt3.setTextColor(context.getResources().getColor(R.color.tviewcolor));
                    indicatortxt4.setTextColor(context.getResources().getColor(R.color.tviewcolor));
                    indicatortxt5.setTextColor(context.getResources().getColor(R.color.tviewcolor));
                    indicatortxt6.setTextColor(context.getResources().getColor(R.color.tviewcolor));
                    indicatortxt7.setTextColor(context.getResources().getColor(R.color.tviewcolor));

                    prefManager.setAlarmtime1(false);
                    prefManager.setAlarmtime2(false);

                    break;

                case 3:

                    indicator1.setVisibility(View.INVISIBLE);
                    indicator2.setVisibility(View.INVISIBLE);
                    indicator3.setVisibility(View.VISIBLE);
                    indicator4.setVisibility(View.INVISIBLE);
                    indicator5.setVisibility(View.INVISIBLE);
                    indicator6.setVisibility(View.INVISIBLE);
                    indicator7.setVisibility(View.INVISIBLE);

                    indicatortxt1.setTextColor(context.getResources().getColor(R.color.tviewcolor));
                    indicatortxt2.setTextColor(context.getResources().getColor(R.color.tviewcolor));
                    indicatortxt3.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    indicatortxt4.setTextColor(context.getResources().getColor(R.color.tviewcolor));
                    indicatortxt5.setTextColor(context.getResources().getColor(R.color.tviewcolor));
                    indicatortxt6.setTextColor(context.getResources().getColor(R.color.tviewcolor));
                    indicatortxt7.setTextColor(context.getResources().getColor(R.color.tviewcolor));

                    prefManager.setAlarmtime1(false);
                    prefManager.setAlarmtime2(false);
                    prefManager.setAlarmtime3(false);

                    break;

                case 4:

                    indicator1.setVisibility(View.INVISIBLE);
                    indicator2.setVisibility(View.INVISIBLE);
                    indicator3.setVisibility(View.INVISIBLE);
                    indicator4.setVisibility(View.VISIBLE);
                    indicator5.setVisibility(View.INVISIBLE);
                    indicator6.setVisibility(View.INVISIBLE);
                    indicator7.setVisibility(View.INVISIBLE);

                    indicatortxt1.setTextColor(context.getResources().getColor(R.color.tviewcolor));
                    indicatortxt2.setTextColor(context.getResources().getColor(R.color.tviewcolor));
                    indicatortxt3.setTextColor(context.getResources().getColor(R.color.tviewcolor));
                    indicatortxt4.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    indicatortxt5.setTextColor(context.getResources().getColor(R.color.tviewcolor));
                    indicatortxt6.setTextColor(context.getResources().getColor(R.color.tviewcolor));
                    indicatortxt7.setTextColor(context.getResources().getColor(R.color.tviewcolor));

                    prefManager.setAlarmtime1(false);
                    prefManager.setAlarmtime2(false);
                    prefManager.setAlarmtime3(false);
                    prefManager.setAlarmtime4(false);

                    break;

                case 5:

                    indicator1.setVisibility(View.INVISIBLE);
                    indicator2.setVisibility(View.INVISIBLE);
                    indicator3.setVisibility(View.INVISIBLE);
                    indicator4.setVisibility(View.INVISIBLE);
                    indicator5.setVisibility(View.VISIBLE);
                    indicator6.setVisibility(View.INVISIBLE);
                    indicator7.setVisibility(View.INVISIBLE);

                    indicatortxt1.setTextColor(context.getResources().getColor(R.color.tviewcolor));
                    indicatortxt2.setTextColor(context.getResources().getColor(R.color.tviewcolor));
                    indicatortxt3.setTextColor(context.getResources().getColor(R.color.tviewcolor));
                    indicatortxt4.setTextColor(context.getResources().getColor(R.color.tviewcolor));
                    indicatortxt5.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    indicatortxt6.setTextColor(context.getResources().getColor(R.color.tviewcolor));
                    indicatortxt7.setTextColor(context.getResources().getColor(R.color.tviewcolor));


                    prefManager.setAlarmtime1(false);
                    prefManager.setAlarmtime2(false);
                    prefManager.setAlarmtime3(false);
                    prefManager.setAlarmtime4(false);
                    prefManager.setAlarmtime5(false);

                    break;

                case 6:

                    indicator1.setVisibility(View.INVISIBLE);
                    indicator2.setVisibility(View.INVISIBLE);
                    indicator3.setVisibility(View.INVISIBLE);
                    indicator4.setVisibility(View.INVISIBLE);
                    indicator5.setVisibility(View.INVISIBLE);
                    indicator6.setVisibility(View.VISIBLE);
                    indicator7.setVisibility(View.INVISIBLE);

                    indicatortxt1.setTextColor(context.getResources().getColor(R.color.tviewcolor));
                    indicatortxt2.setTextColor(context.getResources().getColor(R.color.tviewcolor));
                    indicatortxt3.setTextColor(context.getResources().getColor(R.color.tviewcolor));
                    indicatortxt4.setTextColor(context.getResources().getColor(R.color.tviewcolor));
                    indicatortxt5.setTextColor(context.getResources().getColor(R.color.tviewcolor));
                    indicatortxt6.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    indicatortxt7.setTextColor(context.getResources().getColor(R.color.tviewcolor));

                    prefManager.setAlarmtime1(false);
                    prefManager.setAlarmtime2(false);
                    prefManager.setAlarmtime3(false);
                    prefManager.setAlarmtime4(false);
                    prefManager.setAlarmtime5(false);
                    prefManager.setAlarmtime6(false);

                    break;

                case 7:

                    indicator1.setVisibility(View.INVISIBLE);
                    indicator2.setVisibility(View.INVISIBLE);
                    indicator3.setVisibility(View.INVISIBLE);
                    indicator4.setVisibility(View.INVISIBLE);
                    indicator5.setVisibility(View.INVISIBLE);
                    indicator6.setVisibility(View.INVISIBLE);
                    indicator7.setVisibility(View.VISIBLE);


                    indicatortxt1.setTextColor(context.getResources().getColor(R.color.tviewcolor));
                    indicatortxt2.setTextColor(context.getResources().getColor(R.color.tviewcolor));
                    indicatortxt3.setTextColor(context.getResources().getColor(R.color.tviewcolor));
                    indicatortxt4.setTextColor(context.getResources().getColor(R.color.tviewcolor));
                    indicatortxt5.setTextColor(context.getResources().getColor(R.color.tviewcolor));
                    indicatortxt6.setTextColor(context.getResources().getColor(R.color.tviewcolor));
                    indicatortxt7.setTextColor(context.getResources().getColor(R.color.colorPrimary));

                    indicatortxt1.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    indicatortxt2.setTextColor(context.getResources().getColor(R.color.tviewcolor));
                    indicatortxt3.setTextColor(context.getResources().getColor(R.color.tviewcolor));
                    indicatortxt4.setTextColor(context.getResources().getColor(R.color.tviewcolor));
                    indicatortxt5.setTextColor(context.getResources().getColor(R.color.tviewcolor));
                    indicatortxt6.setTextColor(context.getResources().getColor(R.color.tviewcolor));
                    indicatortxt7.setTextColor(context.getResources().getColor(R.color.tviewcolor));

                    prefManager.setAlarmtime1(false);
                    prefManager.setAlarmtime2(false);
                    prefManager.setAlarmtime3(false);
                    prefManager.setAlarmtime4(false);
                    prefManager.setAlarmtime5(false);
                    prefManager.setAlarmtime6(false);
                    prefManager.setAlarmtime7(false);

                    break;

                case 8:

                    indicator1.setVisibility(View.INVISIBLE);
                    indicator2.setVisibility(View.INVISIBLE);
                    indicator3.setVisibility(View.INVISIBLE);
                    indicator4.setVisibility(View.INVISIBLE);
                    indicator5.setVisibility(View.INVISIBLE);
                    indicator6.setVisibility(View.INVISIBLE);
                    indicator7.setVisibility(View.INVISIBLE);

                    indicatortxt1.setTextColor(context.getResources().getColor(R.color.tviewcolor));
                    indicatortxt2.setTextColor(context.getResources().getColor(R.color.tviewcolor));
                    indicatortxt3.setTextColor(context.getResources().getColor(R.color.tviewcolor));
                    indicatortxt4.setTextColor(context.getResources().getColor(R.color.tviewcolor));
                    indicatortxt5.setTextColor(context.getResources().getColor(R.color.tviewcolor));
                    indicatortxt6.setTextColor(context.getResources().getColor(R.color.tviewcolor));
                    indicatortxt7.setTextColor(context.getResources().getColor(R.color.tviewcolor));

                    indicatortxt1.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    indicatortxt2.setTextColor(context.getResources().getColor(R.color.tviewcolor));
                    indicatortxt3.setTextColor(context.getResources().getColor(R.color.tviewcolor));

                    break;


            }
        } else {
            indicator1.setVisibility(View.INVISIBLE);
            indicator2.setVisibility(View.INVISIBLE);
            indicator3.setVisibility(View.INVISIBLE);
            indicator4.setVisibility(View.INVISIBLE);
            indicator5.setVisibility(View.INVISIBLE);
            indicator6.setVisibility(View.INVISIBLE);
            indicator7.setVisibility(View.INVISIBLE);

            indicatortxt1.setTextColor(context.getResources().getColor(R.color.tviewcolor));
            indicatortxt2.setTextColor(context.getResources().getColor(R.color.tviewcolor));
            indicatortxt3.setTextColor(context.getResources().getColor(R.color.tviewcolor));
            indicatortxt4.setTextColor(context.getResources().getColor(R.color.tviewcolor));
            indicatortxt5.setTextColor(context.getResources().getColor(R.color.tviewcolor));
            indicatortxt6.setTextColor(context.getResources().getColor(R.color.tviewcolor));
            indicatortxt7.setTextColor(context.getResources().getColor(R.color.tviewcolor));
        }

    }

    //for green
    public void setGreen(int idx)

    {
        switch (idx) {

            case 1:

                time1.setBackgroundResource(R.drawable.time_bg_green);

                break;

            case 2:

                time2.setBackgroundResource(R.drawable.time_bg_green);

                break;

            case 3:

                time3.setBackgroundResource(R.drawable.time_bg_green);

                break;

            case 4:

                time4.setBackgroundResource(R.drawable.time_bg_green);

                break;

            case 5:

                time5.setBackgroundResource(R.drawable.time_bg_green);

                break;

            case 6:

                time6.setBackgroundResource(R.drawable.time_bg_green);

                break;

            case 7:
                time7.setBackgroundResource(R.drawable.time_bg_green);
                break;
        }
    }

    public void setYellow(int idx)

    {
        switch (idx) {

            case 1:

                time1.setBackgroundResource(R.drawable.time_bg_yellow);

                break;

            case 2:

                time2.setBackgroundResource(R.drawable.time_bg_yellow);

                break;

            case 3:

                time3.setBackgroundResource(R.drawable.time_bg_yellow);

                break;

            case 4:

                time4.setBackgroundResource(R.drawable.time_bg_yellow);

                break;

            case 5:

                time5.setBackgroundResource(R.drawable.time_bg_yellow);

                break;

            case 6:

                time6.setBackgroundResource(R.drawable.time_bg_yellow);

                break;

            case 7:
                time7.setBackgroundResource(R.drawable.time_bg_yellow);
                break;
        }
    }

    public void setRed(int idx)

    {
        switch (idx) {

            case 1:

                time1.setBackgroundResource(R.drawable.time_bg_red);

                break;

            case 2:

                time2.setBackgroundResource(R.drawable.time_bg_red);

                break;

            case 3:

                time3.setBackgroundResource(R.drawable.time_bg_red);

                break;

            case 4:

                time4.setBackgroundResource(R.drawable.time_bg_red);

                break;

            case 5:

                time5.setBackgroundResource(R.drawable.time_bg_red);

                break;

            case 6:

                time6.setBackgroundResource(R.drawable.time_bg_red);

                break;

            case 7:
                time7.setBackgroundResource(R.drawable.time_bg_red);
                break;
        }
    }


    //for showing date picker
    public void showDatePickerDialog() {

        //new date picker start

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        welcomeDialog = builder.create();

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        welcomeView = inflater.inflate(R.layout.first_datepicker_dialog, null);

        ((MMTextView) welcomeView.findViewById(R.id.datepickermessage)).setText(getMonthNameinMMLan(Integer.parseInt(month)) + " " + MDetect.INSTANCE.getText(context.getString(R.string.plzchoosestartdate)));

        welcomeDialog.setView(welcomeView);
        welcomeDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        welcomeDialog.setCancelable(false);

        //for variable initialize
        final MMButtonView nextBtn = welcomeView.findViewById(R.id.datepicknext);

        final RelativeLayout childforreplace = welcomeView.findViewById(R.id.child);

        final ViewGroup parent = (ViewGroup) childforreplace.getParent();

        final int idxofchild = parent.indexOfChild(childforreplace);

        //for replace with calendar
        final View calendarview = getLayoutInflater().inflate(R.layout.date_picker, parent, false);

        calendar = calendarview.findViewById(R.id.calendarpicker);


        Calendar current = Calendar.getInstance();
        current.set(Integer.parseInt(year), Integer.parseInt(month) - 1, Integer.parseInt(day));

        try {
            calendar.setDate(current);
        } catch (OutOfDateRangeException e) {
            Log.e("TAg", e.getMessage());
        }


        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkcalendarpicker == 0) {
                    YoYo.with(Techniques.FlipOutY)
                            .duration(500)
                            .withListener(new Animator.AnimatorListener() {
                                @Override
                                public void onAnimationStart(Animator animation) {


                                }

                                @Override
                                public void onAnimationEnd(Animator animation) {


                                    parent.removeView(childforreplace);
                                    parent.addView(calendarview, idxofchild);

                                    YoYo.with(Techniques.FlipInX)
                                            .duration(500)
                                            .playOn(calendarview);
                                }

                                @Override
                                public void onAnimationCancel(Animator animation) {

                                }

                                @Override
                                public void onAnimationRepeat(Animator animation) {

                                }
                            })
                            .playOn(childforreplace);

                    YoYo.with(Techniques.FadeOutRight)
                            .duration(500)
                            .withListener(new Animator.AnimatorListener() {
                                @Override
                                public void onAnimationStart(Animator animation) {

                                }

                                @Override
                                public void onAnimationEnd(Animator animation) {


                                    YoYo.with(Techniques.FadeInRight)
                                            .duration(500)
                                            .playOn(nextBtn);

                                    // nextBtn.setMMText(getString(R.string.ok));
                                }

                                @Override
                                public void onAnimationCancel(Animator animation) {

                                }

                                @Override
                                public void onAnimationRepeat(Animator animation) {

                                }
                            })
                            .playOn(nextBtn);

                    checkcalendarpicker = 1;
                } else if (checkcalendarpicker == 1) {



                    Calendar can = calendar.getFirstSelectedDate();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Log.e("You choose ", dateFormat.format(can.getTime()));

                    checkcalendarpicker = 2;


                    int dayDiff = getDifferenceBetweenTwoDate(year + "-" + month + "-" + day, dateFormat.format(can.getTime()));

                    Log.e("Day diff", dayDiff + "");

                    if (dayDiff > 0) {
                        welcomeDialog.dismiss();
                        showRadioButton(dayDiff, dateFormat.format(can.getTime()));


                    } else if (dayDiff == 0) {
                        welcomeDialog.dismiss();


                        String tDay = null;

                        try {
                            SimpleDateFormat inFormat = new SimpleDateFormat("yyyy-MM-dd");
                            Date date = inFormat.parse(mDate);
                            SimpleDateFormat outFormat = new SimpleDateFormat("EEEE");
                            tDay = outFormat.format(date);

                        } catch (Exception e) {
                            Log.e("Error", e.getMessage());
                        }


                        if (!tDay.toLowerCase().substring(0, 3).equals("sat") && !tDay.toLowerCase().substring(0, 3).equals("sun")) {
                            initializeMonth(year + "-" + month + "-" + day, "p");
                            initializeForTotalDayToAttend(month, getDaysWithoutWeekends(Integer.parseInt(day), Integer.parseInt(month), Integer.parseInt(year)));
                            prefManager.setFirstTime(false);
                        } else {

                            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            currentDateTimeString = df.format(Calendar.getInstance().getTime());


                            // Toast.makeText(getContext(), currentDateTimeString, Toast.LENGTH_LONG).show();

                            mDate = currentDateTimeString.split(" ")[0];
                            mTime = currentDateTimeString.split(" ")[1];

                            //for date
                            String mYear = mDate.split("-")[0];
                            String mMonth = mDate.split("-")[1];
                            String dday = mDate.split("-")[2];

                            String mDay = "";

                            int c = 1;

                            int md = 0;
                            do {

                                try {

                                    md = Integer.parseInt(dday) + c;

                                    if (md > getTotalDayInMonth(Integer.parseInt(mMonth), Integer.parseInt(mYear))) {
                                        mMonth = (Integer.parseInt(mMonth) + 1) + "";
                                        c = 1;

                                        md = c;

                                    }

                                    SimpleDateFormat inFormat = new SimpleDateFormat("yyyy-MM-dd");
                                    Date date = inFormat.parse(mYear + "-" + mMonth + "-" + md);
                                    SimpleDateFormat outFormat = new SimpleDateFormat("EEEE");
                                    mDay = outFormat.format(date);

                                } catch (Exception e) {
                                    Log.e("Error", e.getMessage());
                                }

                                c++;

                            }
                            while (mDay.toLowerCase().substring(0, 3).equals("sat") || mDay.toLowerCase().substring(0, 3).equals("sun"));


                            String forday = "";

                            if (String.valueOf(md).length() == 1)
                                forday = "0" + md;
                            else
                                forday = md + "";

                            initializeMonth(mYear + "-" + mMonth + "-" + forday, "p");
                            initializeForTotalDayToAttend(mMonth, getDaysWithoutWeekends(md, Integer.parseInt(mMonth), Integer.parseInt(mYear)));
                            prefManager.setFirstTime(false);


                        }


                    } else if (dayDiff == -1) {
                        checkcalendarpicker=1;
                        Toast.makeText(context, "This date is impossible Please choose again", Toast.LENGTH_LONG).show();

                       // showDatePickerDialog();
                        //getActivity().finish();
                    }


                }


            }
        });


        welcomeDialog.show();


    }


    public String changeEngToMM(String engtxt) {
        String retVal = "";

        switch (engtxt.toLowerCase().substring(0, 3)) {
            case "mon":

                retVal = MDetect.INSTANCE.getText(getString(R.string.mon));

                break;

            case "tue":

                retVal = MDetect.INSTANCE.getText(getString(R.string.tue));

                break;

            case "wed":

                retVal = MDetect.INSTANCE.getText(getString(R.string.wed));

                break;

            case "thu":

                retVal = MDetect.INSTANCE.getText(getString(R.string.thu));

                break;

            case "fri":

                retVal = MDetect.INSTANCE.getText(getString(R.string.fri));

                break;

            case "sat":

                retVal = MDetect.INSTANCE.getText(getString(R.string.sat));

                break;

            case "sun":

                retVal = MDetect.INSTANCE.getText(getString(R.string.sun));

                break;

        }

        return retVal;
    }


    public String changeMonthstr(int monthNum) {
        String retVal = null;

        switch (monthNum) {
            case 1:

                retVal = "Jan";

                break;

            case 2:

                retVal = "Feb";

                break;

            case 3:

                retVal = "Mar";

                break;

            case 4:

                retVal = "Apr";

                break;

            case 5:

                retVal = "May";

                break;


            case 6:

                retVal = "Jun";

                break;

            case 7:

                retVal = "Jul";

                break;

            case 8:

                retVal = "Aug";

                break;

            case 9:

                retVal = "Sep";

                break;

            case 10:

                retVal = "Oct";

                break;

            case 11:

                retVal = "Nov";

                break;

            case 12:

                retVal = "Dec";

                break;
        }

        return retVal;
    }


    public String getMonthNameinMMLan(int month) {
        String retMonth = null;


        switch (month) {
            case 1:

                retMonth = MDetect.INSTANCE.getText(context.getString(R.string.jan) + "လ");

                break;

            case 2:

                retMonth = MDetect.INSTANCE.getText(context.getString(R.string.feb) + "လ");

                break;

            case 3:

                retMonth = MDetect.INSTANCE.getText(context.getString(R.string.mar) + "လ");

                break;

            case 4:

                retMonth = MDetect.INSTANCE.getText(context.getString(R.string.apr) + "လ");

                break;

            case 5:

                retMonth = MDetect.INSTANCE.getText(context.getString(R.string.may) + "လ");

                break;

            case 6:

                retMonth = MDetect.INSTANCE.getText(context.getString(R.string.jun) + "လ");

                break;

            case 7:

                retMonth = MDetect.INSTANCE.getText(context.getString(R.string.jul) + "လ");

                break;

            case 8:

                retMonth = MDetect.INSTANCE.getText(context.getString(R.string.aug) + "လ");

                break;

            case 9:

                retMonth = MDetect.INSTANCE.getText(context.getString(R.string.sep) + "လ");

                break;

            case 10:

                retMonth = MDetect.INSTANCE.getText(context.getString(R.string.oct) + "လ");

                break;

            case 11:

                retMonth = MDetect.INSTANCE.getText(context.getString(R.string.nov) + "လ");
                break;

            case 12:

                retMonth = MDetect.INSTANCE.getText(context.getString(R.string.dec) + "လ");
                break;

        }


        return retMonth;
    }

    //for show case
    public void startShowCase() {


        mQueue = new FancyShowCaseQueue();

        //for time table tab
        final FancyShowCaseView showcasetimetable = new FancyShowCaseView.Builder(getActivity())
                .focusOn(getActivity().findViewById(R.id.showcasefortimetable))
                .focusCircleRadiusFactor(0.5)
                .title(MDetect.INSTANCE.getText(getString(R.string.showcasetexttimetable)))
                .titleSize((int) getResources().getDimension(R.dimen.showcasetxtsize), TypedValue.COMPLEX_UNIT_SP)
                .backgroundColor(getResources().getColor(R.color.showcase))
                .build();

        //for rollcall

        final FancyShowCaseView showcaserollcall = new FancyShowCaseView.Builder(getActivity())
                .focusOn(((Activity) FragTimeTable.this.getContext()).findViewById(R.id.showcaseforrollcall))
                .focusCircleRadiusFactor(0.5)
                .title(MDetect.INSTANCE.getText(getString(R.string.showcasetextroll)))
                .titleSize((int) getResources().getDimension(R.dimen.showcasetxtsize), TypedValue.COMPLEX_UNIT_SP)
                .backgroundColor(getResources().getColor(R.color.showcase))
                .build();

        //for setting

        final FancyShowCaseView showcasesetting = new FancyShowCaseView.Builder(getActivity())
                .focusOn(((Activity) FragTimeTable.this.getContext()).findViewById(R.id.showcaseforsetting))
                .focusCircleRadiusFactor(0.5)
                .title(MDetect.INSTANCE.getText(getString(R.string.showcasetextsetting)))
                .titleSize((int) getResources().getDimension(R.dimen.showcasetxtsize), TypedValue.COMPLEX_UNIT_SP)
                .backgroundColor(getResources().getColor(R.color.showcase))
                .build();

        //for whole day switch

        final FancyShowCaseView showcasewholeday = new FancyShowCaseView.Builder(getActivity())
                .focusOn(view.findViewById(R.id.wholedayabsent))
                .focusCircleRadiusFactor(0.8)
                .title(MDetect.INSTANCE.getText(getString(R.string.wholeday)))
                .titleSize((int) getResources().getDimension(R.dimen.showcasetxtsize), TypedValue.COMPLEX_UNIT_SP)
                .backgroundColor(getResources().getColor(R.color.showcase))
                .build();

        //for current time

        final FancyShowCaseView showcasecurrenttime = new FancyShowCaseView.Builder(getActivity())
                .focusOn(view.findViewById(R.id.showcasecurrenttime))
                .focusCircleRadiusFactor(0.8)
                .title(MDetect.INSTANCE.getText(getString(R.string.currenttimeshow)))
                .titleSize((int) getResources().getDimension(R.dimen.showcasetxtsize), TypedValue.COMPLEX_UNIT_SP)
                .backgroundColor(getResources().getColor(R.color.showcase))
                .build();

        //for absent

        final FancyShowCaseView showcasecurrenttimeabsent = new FancyShowCaseView.Builder(getActivity())
                .focusOn(view.findViewById(R.id.absent))
                .focusCircleRadiusFactor(0.8)
                .title(MDetect.INSTANCE.getText(getString(R.string.currenttimeabsent)))
                .titleGravity(Gravity.TOP | Gravity.CENTER)
                .titleSize((int) getResources().getDimension(R.dimen.showcasetxtsize), TypedValue.COMPLEX_UNIT_SP)
                .backgroundColor(getResources().getColor(R.color.showcase))
                .build();


        //for today

        final FancyShowCaseView showcasetoday = new FancyShowCaseView.Builder(getActivity())
                .focusOn(view.findViewById(R.id.showcasetoday))
                .focusCircleRadiusFactor(0.8)
                .title(MDetect.INSTANCE.getText(getString(R.string.todayshow)) + "\n" + MDetect.INSTANCE.getText(getString(R.string.todayshow2)))
                .titleGravity(Gravity.TOP | Gravity.CENTER)
                .titleSize((int) getResources().getDimension(R.dimen.showcasetxtsize), TypedValue.COMPLEX_UNIT_SP)
                .backgroundColor(getResources().getColor(R.color.showcase))
                .build();

        //for today

        final FancyShowCaseView showcaseweek = new FancyShowCaseView.Builder(getActivity())
                .focusOn(view.findViewById(R.id.viewtimetableforallday))
                .focusCircleRadiusFactor(0.4)
                .title(MDetect.INSTANCE.getText(getString(R.string.weektimetableshow)))
                .titleSize((int) getResources().getDimension(R.dimen.showcasetxtsize), TypedValue.COMPLEX_UNIT_SP)
                .backgroundColor(getResources().getColor(R.color.showcase))
                .build();

        mQueue
                .add(showcasewholeday)
                .add(showcasecurrenttime)
                .add(showcasecurrenttimeabsent)
                .add(showcasetoday)
                .add(showcaseweek)
                .add(showcasetimetable)
                .add(showcaserollcall)
                .add(showcasesetting)
                .show();

        mQueue.setCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete() {
                prefManager.setSHOWCASE1_START_TIME_PICK(false);
                showIndicator(getTimeWithCurrentTime());
                setUpCurrentTimeTable();
                setUpTodaySevenTime();

                if (goal.toLowerCase().substring(0, 3).equals("sat")) {

                    txtToday.setText(changeEngToMM(goal) + MDetect.INSTANCE.getText(getString(R.string.offday)));
                    wholeswitch.setEnabled(false);

                } else if (goal.toLowerCase().substring(0, 3).equals("sun")) {

                    txtToday.setText(changeEngToMM(goal) + MDetect.INSTANCE.getText(getString(R.string.offday)));
                    wholeswitch.setEnabled(false);

                } else {
                    txtToday.setText(changeEngToMM(goal) + MDetect.INSTANCE.getText(getString(R.string.todaytimetable)));
                    wholeswitch.setEnabled(true);
                }


            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();
        updateTimeTableEveryOneMinute();

        if (welcomeDialog != null)
            if (welcomeDialog.isShowing()) {
                welcomeDialog.dismiss();

                if (previousDayTracking != null)
                    if (previousDayTracking.isShowing()) {
                        previousDayTracking.dismiss();
                    }
            }
    }


    private void showRadioButton(final int size, final String date) {

        //  date = date.replace("/", "-");
        //alert start

        previousDayTracking = builderforprevious.create();

        final int ttday = Integer.parseInt(day);

        previousDayView = inflaterprevious.inflate(R.layout.start_date_and_use_date_item, null);

        previousDayTracking.setView(previousDayView);
        previousDayTracking.setCancelable(false);


        //alert end

        int forsize = 0;

        for (int k = ttday; k > ttday - size; k--) {


            Calendar cc = new GregorianCalendar(Integer.parseInt(year), Integer.parseInt(month) - 1, k); // Note that Month value is 0-based. e.g., 0 for January.
            int result = cc.get(Calendar.DAY_OF_WEEK);


            if (result != Calendar.SUNDAY && result != Calendar.SATURDAY)
                forsize++;


        }


        rdButtonArray = new RadioButton[forsize + 1];

        rdPrevious = previousDayView.findViewById(R.id.rdgpdatepick); //create the RadioGroup

        for (int i = 0; i < rdButtonArray.length; i++) {
            rdButtonArray[i] = new RadioButton(context);
            rdPrevious.addView(rdButtonArray[i]); //the RadioButtons are added to the radioGroup instead of the layout

            String forsettext;

            if (i == 0) {
                forsettext = MDetect.INSTANCE.getText(context.getString(R.string.previousfull));
            } else {
                forsettext = i + " " + MDetect.INSTANCE.getText(context.getString(R.string.previousabsent));
            }
            rdButtonArray[i].setTextSize(16);
            rdButtonArray[i].setText(forsettext);
        }

        rdButtonArray[0].setChecked(true);
        ((MMButtonView) previousDayView.findViewById(R.id.submit)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selected = null;
                previousDayTracking.dismiss();

                lab:
                for (int j = 0; j < rdButtonArray.length; j++) {
                    if (rdButtonArray[j].isChecked())
                        selected = rdButtonArray[j].getText().toString();
                }

                if (selected.equals(MDetect.INSTANCE.getText(context.getString(R.string.previousfull)))) {

                    Log.e("TAG", "You choose all");

                    for (int j = ttday; j >= (ttday - size); j--) {

                        Calendar calendar = new GregorianCalendar(Integer.parseInt(year), Integer.parseInt(date.split("-")[1]) - 1, j); // Note that Month value is 0-based. e.g., 0 for January.
                        int result = calendar.get(Calendar.DAY_OF_WEEK);

                        if (result != Calendar.SATURDAY && result != Calendar.SUNDAY) {
                            String forday = "";

                            if (String.valueOf(j).length() == 1)
                                forday = "0" + j;
                            else
                                forday = j + "";

                            initializeMonth(date.split("-")[0] + "-" + date.split("-")[1] + "-" + forday, "p");
                        }

                    }


                } else {
                    int forForloop = Integer.parseInt(selected.split(" ")[0]);

                    int chk = 0;
                    int startfornextloop = 0;

                    lb1:
                    for (int j = (ttday - 1); j >= (ttday - size); j--) {
                        Calendar calendar = new GregorianCalendar(Integer.parseInt(year), Integer.parseInt(date.split("-")[1]) - 1, j); // Note that Month value is 0-based. e.g., 0 for January.
                        int result = calendar.get(Calendar.DAY_OF_WEEK);


                        if (result != Calendar.SATURDAY && result != Calendar.SUNDAY) {
                            String forday = "";

                            if (String.valueOf(j).length() == 1)
                                forday = "0" + j;
                            else
                                forday = j + "";

                            initializeMonth(year + "-" + month + "-" + forday, "a");
                            // Toast.makeText(context, "It is work", Toast.LENGTH_SHORT).show();

                            chk++;

                            if (forForloop == chk) {
                                startfornextloop = j - 1;
                                break lb1;
                            }

                        }
                    }

                    for (int j = startfornextloop; j >= (ttday - size); j--) {

                        Calendar calendar = new GregorianCalendar(Integer.parseInt(year), Integer.parseInt(date.split("-")[1]) - 1, j); // Note that Month value is 0-based. e.g., 0 for January.
                        int result = calendar.get(Calendar.DAY_OF_WEEK);

                        if (result != Calendar.SATURDAY && result != Calendar.SUNDAY) {
                            String forday = "";

                            if (String.valueOf(j).length() == 1)
                                forday = "0" + j;
                            else
                                forday = j + "";

                            initializeMonth(year + "-" + month + "-" + forday, "p");
                        }
                    }

                    String fd = "";

                    if (String.valueOf(ttday).length() == 1)
                        fd = "0" + ttday;
                    else
                        fd = ttday + "";

                    initializeMonth(year + "-" + month + "-" + fd, "p");


                }

                initializeForTotalDayToAttend(month, getDaysWithoutWeekends(Integer.parseInt(date.split("-")[2]), Integer.parseInt(date.split("-")[1]), Integer.parseInt(date.split("-")[0])));
                prefManager.setFirstTime(false);

                //Toast.makeText(context, "You choose " + selected, Toast.LENGTH_SHORT).show();

            }
        });

        previousDayTracking.show();


    }


    public int getDifferenceBetweenTwoDate(String currentDate, String userDate) {
        int retVal = 0;


        try {

            Date date1;
            Date date2;

            SimpleDateFormat dates = new SimpleDateFormat("yyyy-MM-dd");

            //Setting dates
            date1 = dates.parse(currentDate);
            date2 = dates.parse(userDate);

            //Comparing dates

            long differenceDates;

            if (date1.getTime() > date2.getTime()) {
                long difference = Math.abs(date1.getTime() - date2.getTime());
                differenceDates = difference / (24 * 60 * 60 * 1000);
            } else if (date1.getTime() == date2.getTime()) {
                differenceDates = 0;
            } else {
                differenceDates = -1;
            }


            //Convert long to String
            String dayDifference = Long.toString(differenceDates);

            retVal = Integer.parseInt(dayDifference);
            Log.e("HERE", "HERE: " + dayDifference);

        } catch (Exception exception) {
            Log.e("DIDN'T WORK", "exception " + exception);
        }

        return retVal;
    }


    public void showSubjectDialog(final String timenumber, final String subShortTerm, String teacherName, final String startTime) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        subjectDialog = builder.create();

        subjectView = inflaterprevious.inflate(R.layout.subject_dialog, null);

        subjectDialog.setView(subjectView);

        subjectDialog.setCancelable(false);
        subjectDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        subjectDialog.getWindow()
                .getAttributes().windowAnimations = R.style.DialogAnimation;


        subjectDialog.show();

        //view component initialize
        (subjectView.findViewById(R.id.close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subjectDialog.dismiss();
            }
        });

        //for noti string
        MMTextView notiString = subjectView.findViewById(R.id.notiString);

        getNoti = subjectView.findViewById(R.id.getnoti);
        getNoti.setChecked(getAlarm(timenumber));


        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("alarm", "setNoti");
        intent.putExtra("id", timenumber);
        intent.putExtra("bodyContent", subShortTerm);


        pendingIntent1 = PendingIntent.getBroadcast(context, 0, intent, 0);
        pendingIntent2 = PendingIntent.getBroadcast(context, 0, intent, 0);
        pendingIntent3 = PendingIntent.getBroadcast(context, 0, intent, 0);
        pendingIntent4 = PendingIntent.getBroadcast(context, 0, intent, 0);
        pendingIntent5 = PendingIntent.getBroadcast(context, 0, intent, 0);
        pendingIntent6 = PendingIntent.getBroadcast(context, 0, intent, 0);
        pendingIntent7 = PendingIntent.getBroadcast(context, 0, intent, 0);


        getNoti.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    currentDateTimeString = df.format(Calendar.getInstance().getTime());


                    // Toast.makeText(getContext(), currentDateTimeString, Toast.LENGTH_LONG).show();

                    mDate = currentDateTimeString.split(" ")[0];
                    mTime = currentDateTimeString.split(" ")[1];

                    //for date
                    year = mDate.split("-")[0];
                    month = mDate.split("-")[1];
                    day = mDate.split("-")[2];


                    Calendar cal = Calendar.getInstance();

                    cal.setTimeInMillis(System.currentTimeMillis());
                    cal.clear();

                    cal.set(2017, 11, 4, 9, 13);


                    if (startTime.toLowerCase().split(" ")[1].equals("am")) {

                        String forTimer = startTime.split(" ")[0];

                        cal.set(Integer.parseInt(year), Integer.parseInt(month) - 1, Integer.parseInt(day), Integer.parseInt(forTimer.split(":")[0]), Integer.parseInt(forTimer.split(":")[1]), 0);

                    } else if (startTime.toLowerCase().split(" ")[1].equals("pm")) {
                        String forTimer = startTime.split(" ")[0];

                        if (forTimer.split(":")[0].equals("12")) {
                            cal.set(Integer.parseInt(year), Integer.parseInt(month) - 1, Integer.parseInt(day), Integer.parseInt(forTimer.split(":")[0]), Integer.parseInt(forTimer.split(":")[1]), 0);

                        } else {
                            cal.set(Integer.parseInt(year), Integer.parseInt(month) - 1, Integer.parseInt(day), Integer.parseInt(forTimer.split(":")[0]) + 12, Integer.parseInt(forTimer.split(":")[1]), 0);
                        }
                    }


                    // Toast.makeText(context, startTime.split(" ")[0], Toast.LENGTH_LONG).show();

                    long when = cal.getTimeInMillis();


                    switch (timenumber) {
                        case "1":
                            am.set(AlarmManager.RTC_WAKEUP, when, pendingIntent1);
                            break;

                        case "2":

                            am.set(AlarmManager.RTC_WAKEUP, when, pendingIntent2);


                            break;


                        case "3":

                            am.set(AlarmManager.RTC_WAKEUP, when, pendingIntent3);


                            break;


                        case "4":
                            am.set(AlarmManager.RTC_WAKEUP, when, pendingIntent4);


                            break;

                        case "5":

                            am.set(AlarmManager.RTC_WAKEUP, when, pendingIntent5);

                            break;

                        case "6":

                            am.set(AlarmManager.RTC_WAKEUP, when, pendingIntent6);


                            break;

                        case "7":

                            am.set(AlarmManager.RTC_WAKEUP, when, pendingIntent7);

                            break;


                    }


                    setAlarm(timenumber, true);
                    setYellow(Integer.parseInt(timenumber));

                    Toast.makeText(context, subShortTerm + " " + MDetect.INSTANCE.getText(getString(R.string.notidone)), Toast.LENGTH_SHORT).show();

                } else {

                    setGreen(Integer.parseInt(timenumber));

                    switch (timenumber) {
                        case "1":

                            am.cancel(pendingIntent1);

                            break;

                        case "2":

                            am.cancel(pendingIntent2);

                            break;


                        case "3":

                            am.cancel(pendingIntent3);

                            break;


                        case "4":

                            am.cancel(pendingIntent4);

                            break;

                        case "5":

                            am.cancel(pendingIntent5);

                            break;

                        case "6":

                            am.cancel(pendingIntent6);

                            break;

                        case "7":

                            am.cancel(pendingIntent7);

                            break;


                    }


                    setAlarm(timenumber, false);
                    Toast.makeText(context, subShortTerm + " " + MDetect.INSTANCE.getText(getString(R.string.notiundo)), Toast.LENGTH_SHORT).show();

                }
            }
        });

        if (!checkValidateTime(timenumber)) {
            notiString.setTextColor(getResources().getColor(R.color.shadowcolor));
            getNoti.setEnabled(false);
        }


        ((TextView) subjectView.findViewById(R.id.subtitle)).setText(timenumber);
        ((TextView) subjectView.findViewById(R.id.subjectlongname)).setText(subShortTerm);
        ((MMTextView) subjectView.findViewById(R.id.teachername)).setMMText(teacherName);
        ((MMTextView) subjectView.findViewById(R.id.timeinterval)).setMMText(startTime);

    }

//for disable show subjectdialog

    public void showSubjectDialog(String s) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        subjectDialog = builder.create();

        subjectView = inflaterprevious.inflate(R.layout.subject_dialog, null);

        subjectDialog.setView(subjectView);

        subjectDialog.setCancelable(false);
        subjectDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        subjectDialog.getWindow()
                .getAttributes().windowAnimations = R.style.DialogAnimation;


        subjectDialog.show();

        //view component initialize
        (subjectView.findViewById(R.id.close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subjectDialog.dismiss();
            }
        });

        //for noti string
        MMTextView notiString = subjectView.findViewById(R.id.notiString);
        notiString.setTextColor(context.getResources().getColor(R.color.shadowcolor));

        getNoti = subjectView.findViewById(R.id.getnoti);
        getNoti.setChecked(false);

        ((TextView) subjectView.findViewById(R.id.subtitle)).setText("-");
        ((TextView) subjectView.findViewById(R.id.subjectlongname)).setText("-");
        ((MMTextView) subjectView.findViewById(R.id.teachername)).setMMText("-");
        ((MMTextView) subjectView.findViewById(R.id.timeinterval)).setMMText("-");

    }

    @Override
    public void onClick(View v) {

        if (!prefManager.getOffmonth()) {
            //for getting current day
            String tDay = null;
            try {
                SimpleDateFormat inFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date date = inFormat.parse(mDate);
                SimpleDateFormat outFormat = new SimpleDateFormat("EEEE");
                tDay = outFormat.format(date);

            } catch (Exception e) {
                Log.e("Error", e.getMessage());
            }


            //  Toast.makeText(context, hour + " : " + min, Toast.LENGTH_SHORT).show();

            List<String> subList = dbHandler.getShortSubjectNamewithday(tDay.toLowerCase().substring(0, 3));


            if (subList.size() != 0) {


                switch (v.getId()) {

                    case R.id.tOne:

                        //Toast.makeText(context, "Teacher name is " + dbHandler.getTeacherNameWithShort(subList.get(3)), Toast.LENGTH_LONG).show();

                        showSubjectDialog("1", subList.get(0), dbHandler.getTeacherNameWithShort(subList.get(0)), dbHandler.getIntervalWithNumber(1).getStartTime());

                        break;

                    case R.id.tTwo:

                        showSubjectDialog("2", subList.get(1), dbHandler.getTeacherNameWithShort(subList.get(1)), dbHandler.getIntervalWithNumber(2).getStartTime());

                        break;

                    case R.id.tThree:

                        showSubjectDialog("3", subList.get(2), dbHandler.getTeacherNameWithShort(subList.get(2)), dbHandler.getIntervalWithNumber(3).getStartTime());


                        break;

                    case R.id.tFour:

                        showSubjectDialog("4", subList.get(3), dbHandler.getTeacherNameWithShort(subList.get(3)), dbHandler.getIntervalWithNumber(4).getStartTime());


                        break;

                    case R.id.tFive:

                        showSubjectDialog("5", subList.get(4), dbHandler.getTeacherNameWithShort(subList.get(4)), dbHandler.getIntervalWithNumber(5).getStartTime());


                        break;

                    case R.id.tSix:

                        showSubjectDialog("6", subList.get(5), dbHandler.getTeacherNameWithShort(subList.get(5)), dbHandler.getIntervalWithNumber(6).getStartTime());


                        break;

                    case R.id.tSeven:

                        showSubjectDialog("7", subList.get(6), dbHandler.getTeacherNameWithShort(subList.get(6)), dbHandler.getIntervalWithNumber(7).getStartTime());


                        break;


                }
            } else {
                showSubjectDialog("off");


            }
        } else {

            Toast.makeText(context, "Today is off day", Toast.LENGTH_LONG).show();
        }
    }

    public boolean checkValidateTime(String timeNum) {
        boolean retVal = false;

        //  Toast.makeText(context, timeNum, Toast.LENGTH_SHORT).show();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = df.format(Calendar.getInstance().getTime());

        int hr = Integer.parseInt(date.split(" ")[1].split(":")[0]);
        int mi = Integer.parseInt(date.split(" ")[1].split(":")[1]);

        int currentTime = (hr * 60) + mi;

        String startTime = dbHandler.getIntervalWithNumber(Integer.parseInt(timeNum)).getStartTime();

        // Toast.makeText(context, "Start time " + startTime, Toast.LENGTH_SHORT).show();

        if (currentTime < changeExactTime(startTime))
            retVal = true;

        return retVal;
    }

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

        // Toast.makeText(context, "End time Three is " + endTimeThree + " and Start time four " + startTimeFour + " current time is " + currentTime, Toast.LENGTH_SHORT).show();

        return retVal;
    }


    public void setUpCurrentTimeTable() {
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


        if (!prefManager.getOffmonth() && !prefManager.getWholedayabsent() && !tDay.toLowerCase().substring(0, 3).equals("sat") && !tDay.toLowerCase().substring(0, 3).equals("sun") && !dbHandler.isAlreadyOffDay(year + "-" + month + "-" + day)) {


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


                    btnAdsentAndPresent.setEnabled(true);
                    btnAdsentAndPresent.setBackgroundResource(R.drawable.greenbgforpresent);
                    btnAdsentAndPresent.setText(MDetect.INSTANCE.getText(context.getResources().getString(R.string.absent)));

                    txtForabsent.setVisibility(View.INVISIBLE);


                } else if (aorp.equals("a")) {

                    btnAdsentAndPresent.setEnabled(true);
                    btnAdsentAndPresent.setBackgroundResource(R.drawable.redbgforabsent);
                    btnAdsentAndPresent.setText(MDetect.INSTANCE.getText(context.getResources().getString(R.string.present)));

                    txtForabsent.setVisibility(View.VISIBLE);

                } else {
                    btnAdsentAndPresent.setEnabled(false);
                }

                //for absent and present btn end

                //for getting current day

                if (current == 0) {

                    //pauk ka ya time
                    txtTimeInterval.setText("-");
                    txtSubject.setText("-");
                    txtTeacher.setText("-");

                    btnAdsentAndPresent.setEnabled(false);


                } else if (current == 8) {
                    //lunch time

                    txtTimeInterval.setText(MDetect.INSTANCE.getText(context.getResources().getString(R.string.lunchtime)));
                    txtSubject.setText("-");
                    txtTeacher.setText("-");
                    btnAdsentAndPresent.setEnabled(false);

                } else {
                    List<String> subList = dbHandler.getShortSubjectNamewithday(tDay.toLowerCase().substring(0, 3));

                    if (subList.size() != 0) {

                        txtTimeInterval.setText(dbHandler.getIntervalWithNumber(current).getInterval());
                        txtSubject.setText(subList.get(current - 1));
                        txtTeacher.setText(dbHandler.getTeacherNameWithShort(subList.get(current - 1)));
                        btnAdsentAndPresent.setEnabled(true);
                    } else {

                        txtTimeInterval.setText("-");
                        txtSubject.setText("-");
                        txtTeacher.setText("-");
                        btnAdsentAndPresent.setEnabled(false);

                    }
                }
            }


        } else {


            txtTimeInterval.setText("-");
            txtSubject.setText("-");
            txtTeacher.setText("-");

            btnAdsentAndPresent.setEnabled(false);

        }
    }


    //for today time table
    public void setUpTodaySevenTime() {

        if (!prefManager.getOffmonth() && !prefManager.isFirstTime() && !dbHandler.isAlreadyOffDay(year + "-" + month + "-" + day)) {
            DailyTime dailyTime = null;
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


            if (dailyTime != null) {
                //for time one
                if (dailyTime.getTimeone().equals("p")) {
                    setGreen(1);
                } else {
                    setRed(1);
                }


                //for time two
                if (dailyTime.getTimetwo().equals("p")) {
                    setGreen(2);
                } else {
                    setRed(2);
                }

                //for time three
                if (dailyTime.getTimethree().equals("p")) {
                    setGreen(3);
                } else {
                    setRed(3);
                }

                //for time four
                if (dailyTime.getTimefour().equals("p")) {
                    setGreen(4);
                } else {
                    setRed(4);
                }

                //for time five
                if (dailyTime.getTimefive().equals("p")) {
                    setGreen(5);
                } else {
                    setRed(5);
                }

                //for time six
                if (dailyTime.getTimesix().equals("p")) {
                    setGreen(6);
                } else {
                    setRed(6);
                }

                //for time seven
                if (dailyTime.getTimeseven().equals("p")) {
                    setGreen(7);
                } else {
                    setRed(7);
                }

            } else {

            }

            if (prefManager.isAlarmOne()) {
                setYellow(1);
            }
            if (prefManager.isAlarmTwo()) {
                setYellow(2);
            }
            if (prefManager.isAlarmThree()) {
                setYellow(3);
            }
            if (prefManager.isAlarmFour()) {
                setYellow(4);
            }
            if (prefManager.isAlarmFive()) {
                setYellow(5);
            }
            if (prefManager.isAlarmSix()) {
                setYellow(6);
            }
            if (prefManager.isAlarmSeven()) {
                setYellow(7);
            }


            //for getting current day
            String tDay = null;
            try {
                SimpleDateFormat inFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date date = inFormat.parse(mDate);
                SimpleDateFormat outFormat = new SimpleDateFormat("EEEE");
                tDay = outFormat.format(date);

            } catch (Exception e) {
                Log.e("Error", e.getMessage());
            }


            List<String> subList = dbHandler.getShortSubjectNamewithday(tDay.toLowerCase().substring(0, 3));


            if (subList.size() != 0) {
                for (int i = 0; i < subList.size(); i++) {
                    subtime[i].setText(subList.get(i));
                }
            } else {
                for (int k = 0; k < 7; k++) {
                    subtime[k].setText("...");
                }

            }

        } else {

            txtToday.setText(MDetect.INSTANCE.getText(context.getString(R.string.off)));
            for (int k = 0; k < 7; k++) {
                subtime[k].setText("...");
            }

        }
    }


//for update in every minute

    public void updateTimeTableEveryOneMinute() {
        Handler h = new Handler();

        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                setUpCurrentTimeTable();
                showIndicator(getTimeWithCurrentTime());
                setUpTodaySevenTime();

                updateTimeTableEveryOneMinute();
            }
        }, 60000);
    }

    //for alarm
    public void setAlarm(String time, boolean value) {

        switch (time) {
            case "1":

                prefManager.setAlarmtime1(value);

                break;

            case "2":
                prefManager.setAlarmtime2(value);

                break;

            case "3":

                prefManager.setAlarmtime3(value);

                break;

            case "4":

                prefManager.setAlarmtime4(value);

                break;

            case "5":
                prefManager.setAlarmtime5(value);

                break;

            case "6":

                prefManager.setAlarmtime6(value);

                break;

            case "7":

                prefManager.setAlarmtime7(value);

                break;
        }
    }

    public boolean getAlarm(String time) {
        boolean retVal = false;

        switch (time) {
            case "1":

                retVal = prefManager.isAlarmOne();

                break;

            case "2":

                retVal = prefManager.isAlarmTwo();

                break;

            case "3":

                retVal = prefManager.isAlarmThree();

                break;

            case "4":

                retVal = prefManager.isAlarmFour();

                break;

            case "5":

                retVal = prefManager.isAlarmFive();

                break;

            case "6":

                retVal = prefManager.isAlarmSix();

                break;

            case "7":

                retVal = prefManager.isAlarmSeven();

                break;

        }

        return retVal;
    }

//setup for alarm setting


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

    public void initializeMonth(String date, String aorp) {

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        currentDateTimeString = df.format(Calendar.getInstance().getTime());


        // Toast.makeText(getContext(), currentDateTimeString, Toast.LENGTH_LONG).show();

        mDate = currentDateTimeString.split(" ")[0];
        mTime = currentDateTimeString.split(" ")[1];

        //for date
        year = mDate.split("-")[0];
        month = mDate.split("-")[1];
        day = mDate.split("-")[2];

        if (aorp.equals("p")) {
            switch (getValidMonth(month)) {
                case 1:

                    dbHandler.initializeWithNewDateinmonthone(date);

                    break;

                case 2:

                    dbHandler.initializeWithNewDateinmonthtwo(date);

                    break;

                case 3:

                    dbHandler.initializeWithNewDateinmonththree(date);


                    break;

                case 4:

                    dbHandler.initializeWithNewDateinmonthfour(date);

                    break;

                case 5:

                    dbHandler.initializeWithNewDateinmonthfive(date);

                    break;

                case 6:

                    dbHandler.initializeWithNewDateinmonthsix(date);

                    break;

                case 7:

                    dbHandler.initializeWithNewDateinmonthseven(date);

                    break;

                case 8:

                    dbHandler.initializeWithNewDateinmontheight(date);

                    break;


            }
        } else {

            switch (getValidMonth(month)) {
                case 1:

                    dbHandler.initializeWithNewDateinmonthoneAbsent(date);

                    break;

                case 2:

                    dbHandler.initializeWithNewDateinmonthtwoabsent(date);

                    break;

                case 3:

                    dbHandler.initializeWithNewDateinmonththreeabsent(date);


                    break;

                case 4:

                    dbHandler.initializeWithNewDateinmonthfourabsent(date);

                    break;

                case 5:

                    dbHandler.initializeWithNewDateinmonthfiveabsent(date);

                    break;

                case 6:

                    dbHandler.initializeWithNewDateinmonthsixabsent(date);

                    break;

                case 7:

                    dbHandler.initializeWithNewDateinmonthsevenabsent(date);

                    break;

                case 8:

                    dbHandler.initializeWithNewDateinmontheightabsent(date);

                    break;


            }

        }
    }


    public void initializeForTotalDayToAttend(String month, int total) {

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        currentDateTimeString = df.format(Calendar.getInstance().getTime());


        // Toast.makeText(getContext(), currentDateTimeString, Toast.LENGTH_LONG).show();

        mDate = currentDateTimeString.split(" ")[0];
        mTime = currentDateTimeString.split(" ")[1];

        //for date
        year = mDate.split("-")[0];
        //  month = mDate.split("-")[1];
        day = mDate.split("-")[2];

        switch (getValidMonth(month)) {

            case 1:

                prefManager.setMonthonetotaldaytoattend(total);

                break;

            case 2:

                prefManager.setMonthtwototaldaytoattend(total);

                break;

            case 3:

                prefManager.setMonththreetotaldaytoattend(total);


                break;

            case 4:

                prefManager.setMonthfourtotaldaytoattend(total);

                break;

            case 5:

                prefManager.setMonthfivetotaldaytoattend(total);

                break;

            case 6:

                prefManager.setMonthsixtotaldaytoattend(total);

                break;

            case 7:

                prefManager.setMonthseventotaldaytoattend(total);

                break;

            case 8:

                prefManager.setMontheighttotaldaytoattend(total);

                break;

        }

    }

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

//for whole day absent

    public boolean isWholeDayAbsent() {
        boolean retVal = false;

        DailyTime dailyTime;

        switch (getValidMonth(month)) {
            case 1:

                dailyTime = dbHandler.getMonthwithdateInMonthOne(year + "-" + month + "-" + day);

                if (dailyTime.getTimeone().equals("a") && dailyTime.getTimetwo().equals("a") && dailyTime.getTimethree().equals("a") && dailyTime.getTimefour().equals("a") && dailyTime.getTimefive().equals("a") && dailyTime.getTimesix().equals("a") && dailyTime.getTimeseven().equals("a")) {
                    retVal = true;
                }

                break;

            case 2:

                dailyTime = dbHandler.getMonthwithdateInMonthTwo(year + "-" + month + "-" + day);

                if (dailyTime.getTimeone().equals("a") && dailyTime.getTimetwo().equals("a") && dailyTime.getTimethree().equals("a") && dailyTime.getTimefour().equals("a") && dailyTime.getTimefive().equals("a") && dailyTime.getTimesix().equals("a") && dailyTime.getTimeseven().equals("a")) {
                    retVal = true;
                }


                break;

            case 3:

                dailyTime = dbHandler.getMonthwithdateInMonthThree(year + "-" + month + "-" + day);

                if (dailyTime.getTimeone().equals("a") && dailyTime.getTimetwo().equals("a") && dailyTime.getTimethree().equals("a") && dailyTime.getTimefour().equals("a") && dailyTime.getTimefive().equals("a") && dailyTime.getTimesix().equals("a") && dailyTime.getTimeseven().equals("a")) {
                    retVal = true;
                }

                break;

            case 4:

                dailyTime = dbHandler.getMonthwithdateInMonthFour(year + "-" + month + "-" + day);

                if (dailyTime.getTimeone().equals("a") && dailyTime.getTimetwo().equals("a") && dailyTime.getTimethree().equals("a") && dailyTime.getTimefour().equals("a") && dailyTime.getTimefive().equals("a") && dailyTime.getTimesix().equals("a") && dailyTime.getTimeseven().equals("a")) {
                    retVal = true;
                }

                break;

            case 5:

                dailyTime = dbHandler.getMonthwithdateInMonthFive(year + "-" + month + "-" + day);

                if (dailyTime.getTimeone().equals("a") && dailyTime.getTimetwo().equals("a") && dailyTime.getTimethree().equals("a") && dailyTime.getTimefour().equals("a") && dailyTime.getTimefive().equals("a") && dailyTime.getTimesix().equals("a") && dailyTime.getTimeseven().equals("a")) {
                    retVal = true;
                }

                break;

            case 6:

                dailyTime = dbHandler.getMonthwithdateInMonthSix(year + "-" + month + "-" + day);

                if (dailyTime.getTimeone().equals("a") && dailyTime.getTimetwo().equals("a") && dailyTime.getTimethree().equals("a") && dailyTime.getTimefour().equals("a") && dailyTime.getTimefive().equals("a") && dailyTime.getTimesix().equals("a") && dailyTime.getTimeseven().equals("a")) {
                    retVal = true;
                }
                break;

            case 7:

                dailyTime = dbHandler.getMonthwithdateInMonthSeven(year + "-" + month + "-" + day);

                if (dailyTime.getTimeone().equals("a") && dailyTime.getTimetwo().equals("a") && dailyTime.getTimethree().equals("a") && dailyTime.getTimefour().equals("a") && dailyTime.getTimefive().equals("a") && dailyTime.getTimesix().equals("a") && dailyTime.getTimeseven().equals("a")) {
                    retVal = true;
                }

                break;

            case 8:

                dailyTime = dbHandler.getMonthwithdateInMonthEight(year + "-" + month + "-" + day);

                if (dailyTime.getTimeone().equals("a") && dailyTime.getTimetwo().equals("a") && dailyTime.getTimethree().equals("a") && dailyTime.getTimefour().equals("a") && dailyTime.getTimefive().equals("a") && dailyTime.getTimesix().equals("a") && dailyTime.getTimeseven().equals("a")) {
                    retVal = true;
                }

                break;

        }


        return retVal;
    }


}
