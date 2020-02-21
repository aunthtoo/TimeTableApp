package com.aunthtoo.timetableforcupyay;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.aunthtoo.timetableforcupyay.dbhandler.TimeTableDBHandler;
import com.aunthtoo.timetableforcupyay.fragments.FragRollCall;
import com.aunthtoo.timetableforcupyay.fragments.FragSetting;
import com.aunthtoo.timetableforcupyay.fragments.FragTimeTable;
import com.aunthtoo.timetableforcupyay.model.Interval;
import com.aunthtoo.timetableforcupyay.model.Student;
import com.aunthtoo.timetableforcupyay.model.TeacherAndSubject;
import com.aunthtoo.timetableforcupyay.model.TimeTable;
import com.aunthtoo.timetableforcupyay.prefmanager.PrefManager;
import com.aunthtoo.timetableforcupyay.services.MyService;
import com.aunthtoo.timetableforcupyay.volleyapp.VolleyApplication;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import me.myatminsoe.mdetect.MDetect;

public class MainPage extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private BottomBar bottomBar;

    //for firebase
    private DatabaseReference databaseReference;

    private SwipeRefreshLayout swipeRefreshLayout;

    //check for two press
    private boolean check = false;

    //check current position
    private int checkcurrentposition = 0;

    //url link
    private String urllinkfortimetable;

    private ProgressDialog waitingProgress;

    //for database inserting
    private TimeTableDBHandler timeTableDBHandler;


    //for sharedpref
    private PrefManager prefManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);

        MDetect.INSTANCE.init(this);

        //for sharedpref
        prefManager = new PrefManager(this);

        //for database
        timeTableDBHandler = new TimeTableDBHandler(this);

        getSupportFragmentManager().beginTransaction().replace(R.id.contentContainer, new FragTimeTable(), null).commit();

        mAuth = FirebaseAuth.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {

            urllinkfortimetable = getIntent().getStringExtra("url_linkfortimetable");


            waitingProgress = new ProgressDialog(this);
            waitingProgress.setMessage(MDetect.INSTANCE.getText(getString(R.string.plswait)));
            waitingProgress.setCancelable(false);

            //for initializing a day
            initializeAday();

            if (prefManager.isFirstTime()) {
                insertDatatoDb(urllinkfortimetable, 0);
                prefManager.setURL_LINK(urllinkfortimetable);


            } else {

                startService(new Intent(this, MyService.class));

            }


            swipeRefreshLayout = findViewById(R.id.swipelayout);

            swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.failcolor, R.color.yellow);
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {

                    if (isNetworkConnected()) {
                        swipeRefreshLayout.setRefreshing(true);
                        Toast.makeText(getApplicationContext(), MDetect.INSTANCE.getText(getString(R.string.refreshstart)), Toast.LENGTH_SHORT).show();
                        if (prefManager.getURL_LINK() != null) {
                            insertDatatoDb(prefManager.getURL_LINK(), 1);

                        } else
                            Toast.makeText(getApplicationContext(), "Something went wrong!", Toast.LENGTH_LONG).show();

                    } else {
                        swipeRefreshLayout.setRefreshing(false);

                        Toast.makeText(getApplicationContext(), MDetect.INSTANCE.getText(getString(R.string.torefresh)), Toast.LENGTH_LONG).show();
                    }
                }
            });


            bottomBar = findViewById(R.id.bottomBar);

            bottomBar.getTabAtPosition(0).setTitle(MDetect.INSTANCE.getText(getString(R.string.timetable)));
            bottomBar.getTabAtPosition(1).setTitle(MDetect.INSTANCE.getText(getString(R.string.rollcall)));
            bottomBar.getTabAtPosition(2).setTitle(MDetect.INSTANCE.getText(getString(R.string.setting)));


            bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
                @Override
                public void onTabSelected(@IdRes int tabId) {

                    Fragment fragment = null;

                    switch (tabId) {
                        case R.id.tab_timetable:

                            checkcurrentposition = 0;

                            fragment = new FragTimeTable();

                            break;

                        case R.id.tab_rollcall:

                            checkcurrentposition = 1;

                            fragment = new FragRollCall();

                            break;

                        case R.id.tab_setting:

                            checkcurrentposition = 1;

                            fragment = new FragSetting();

                            break;

                        default:
                            break;
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.contentContainer, fragment, null).commit();


                }
            });

        } else {
            startActivity(new Intent(MainPage.this, LogIn.class));
            finish();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();

        menuInflater.inflate(R.menu.main_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        boolean retVal = false;
        switch (item.getItemId()) {
            case R.id.profile:

                Toast.makeText(this, "You click profile", Toast.LENGTH_SHORT).show();
                retVal = true;

                break;

            case R.id.setting:

                Toast.makeText(this, "You click setting", Toast.LENGTH_SHORT).show();
                retVal = true;

                break;
        }

        return retVal;

    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }


    @Override
    public void onBackPressed() {

        if (checkcurrentposition == 0) {

            if (!check) {
                check = true;
                Toast.makeText(this, MDetect.INSTANCE.getText(getString(R.string.exit)), Toast.LENGTH_SHORT).show();

                Handler handler = new Handler();

                handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {

                                            check = false;

                                        }
                                    }

                        , 2000);
            } else {
                finish();
            }

        } else if (checkcurrentposition == 1) {
            bottomBar.selectTabAtPosition(0);
            getSupportFragmentManager().beginTransaction().replace(R.id.contentContainer, new FragTimeTable(), null).commit();

        }
    }


    public void insertDatatoDb(String timeTableLink, int check) {

        waitingProgress.show();
        //loading student
        loadStudent();

        //loading time table
        loadTimeTable(timeTableLink, check);


    }

    //for timetable
    public void loadTimeTable(String link, final int check) {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, link,
                null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {

                    //load timetable
                    List<TimeTable> timeTableList = new ArrayList<>();

                    //for monday
                    TimeTable monday = new TimeTable();
                    monday.setDay("mon");
                    monday.setSubject(response.getString("mon"));
                    timeTableList.add(monday);


                    //for tuesday
                    TimeTable tuesday = new TimeTable();
                    tuesday.setDay("tue");
                    tuesday.setSubject(response.getString("tue"));
                    timeTableList.add(tuesday);

                    //for wednesday
                    TimeTable wednesday = new TimeTable();
                    wednesday.setDay("wed");
                    wednesday.setSubject(response.getString("wed"));
                    timeTableList.add(wednesday);

                    //for thursday
                    TimeTable thursday = new TimeTable();
                    thursday.setDay("thu");
                    thursday.setSubject(response.getString("thu"));
                    timeTableList.add(thursday);

                    //for friday
                    TimeTable friday = new TimeTable();
                    friday.setDay("fri");
                    friday.setSubject(response.getString("fri"));
                    timeTableList.add(friday);

                    timeTableDBHandler.insertTimeTableData(timeTableList);

                    //end of load time table

                    //start of load interval

                    List<Interval> intervalList = new ArrayList<>();

                    String intervalStr = response.getString("interval");


                    //end of load interval

                    for (int j = 0; j < intervalStr.split("/").length; j++) {
                        Interval intervalObj = new Interval();

                        intervalObj.setInterval(intervalStr.split("/")[j]);
                        intervalObj.setStartTime(intervalStr.split("/")[j].split(" - ")[0]);
                        intervalObj.setEndTime(intervalStr.split("/")[j].split(" - ")[1]);

                        intervalList.add(intervalObj);
                    }

                    timeTableDBHandler.insertIntervalData(intervalList);

                    //for teacher and subject start

                    String subShort = response.getString("subjectshort");
                    List<TeacherAndSubject> teacherAndSubjects = new ArrayList<>();


                    for (int j = 0; j < subShort.split("_").length; j++) {
                        String longandteacher = response.getString(subShort.split("_")[j]);

                        TeacherAndSubject obj = new TeacherAndSubject(subShort.split("_")[j], longandteacher.split("/")[0], longandteacher.split("/")[1]);
                        teacherAndSubjects.add(obj);

                    }


                    if (teacherAndSubjects != null)
                        timeTableDBHandler.insertToTeacherAndSubTable(teacherAndSubjects);


                    //for teacher and subject end

                    waitingProgress.dismiss();

                    if (check == 1) {

                        Toast.makeText(getApplicationContext(), MDetect.INSTANCE.getText(getString(R.string.refreshdone)), Toast.LENGTH_SHORT).show();

                        swipeRefreshLayout.setRefreshing(false);

                        Intent intent = new Intent(MainPage.this, MainPage.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                        startActivity(intent);

                    }

                } catch (JSONException e) {

                    e.printStackTrace();
                }


                //to perform data insert

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                waitingProgress.dismiss();

                Toast.makeText(getApplicationContext(), error.getMessage() + "Error", Toast.LENGTH_LONG).show();

            }
        });

        VolleyApplication.getInstance().addToReqQueue(jsonObjReq, "jreq");
    }

    //load student
    public void loadStudent() {

        databaseReference = FirebaseDatabase.getInstance().getReference("user");
        databaseReference.child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Student student = dataSnapshot.getValue(Student.class);
                timeTableDBHandler.updateStudent(student);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        //Toast.makeText(this, "Destroyed", Toast.LENGTH_SHORT).show();

    }


    //initializing a day

    public void initializeAday() {

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentDateTimeString = df.format(Calendar.getInstance().getTime());


        String mDate = currentDateTimeString.split(" ")[0];

        //for date
        String year = mDate.split("-")[0];
        String month = mDate.split("-")[1];
        String day = mDate.split("-")[2];

        String tDay = "";

        try {
            SimpleDateFormat inFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = inFormat.parse(mDate);
            SimpleDateFormat outFormat = new SimpleDateFormat("EEEE");
            tDay = outFormat.format(date);

        } catch (Exception e) {
            Log.e("Error", e.getMessage());
        }

        if (!tDay.toLowerCase().substring(0, 3).equals("sat") && !tDay.toLowerCase().substring(0, 3).equals("sun") && !timeTableDBHandler.isAlreadyOffDay(year + "-" + month + "-" + day)) {
            switch (getValidMonth(month)) {
                case 1:

                    if (!timeTableDBHandler.isAlreadyExistThisDateInMonthOne(year + "-" + month + "-" + day)) {

                        timeTableDBHandler.initializeWithNewDateinmonthone(year + "-" + month + "-" + day);
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


                    if (!timeTableDBHandler.isAlreadyExistThisDateInMonthTwo(year + "-" + month + "-" + day)) {
                        timeTableDBHandler.initializeWithNewDateinmonthtwo(year + "-" + month + "-" + day);
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


                    if (!timeTableDBHandler.isAlreadyExistThisDateInMonthThree(year + "-" + month + "-" + day)) {
                        timeTableDBHandler.initializeWithNewDateinmonththree(year + "-" + month + "-" + day);
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


                    if (!timeTableDBHandler.isAlreadyExistThisDateInMonthFour(year + "-" + month + "-" + day)) {
                        timeTableDBHandler.initializeWithNewDateinmonthfour(year + "-" + month + "-" + day);
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


                    if (!timeTableDBHandler.isAlreadyExistThisDateInMonthFive(year + "-" + month + "-" + day)) {
                        timeTableDBHandler.initializeWithNewDateinmonthfive(year + "-" + month + "-" + day);
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


                    if (!timeTableDBHandler.isAlreadyExistThisDateInMonthSix(year + "-" + month + "-" + day)) {
                        timeTableDBHandler.initializeWithNewDateinmonthsix(year + "-" + month + "-" + day);
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


                    if (!timeTableDBHandler.isAlreadyExistThisDateInMonthSeven(year + "-" + month + "-" + day)) {
                        timeTableDBHandler.initializeWithNewDateinmonthseven(year + "-" + month + "-" + day);
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


                    if (!timeTableDBHandler.isAlreadyExistThisDateInMonthEight(year + "-" + month + "-" + day)) {
                        timeTableDBHandler.initializeWithNewDateinmontheight(year + "-" + month + "-" + day);
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

    }

    //////initializing a day


    //getting valid month
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


    //getting valid month

    //getting day without weekend
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


    //getting day without weekend

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
