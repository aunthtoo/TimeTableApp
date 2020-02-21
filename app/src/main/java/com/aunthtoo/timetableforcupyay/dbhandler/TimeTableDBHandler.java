package com.aunthtoo.timetableforcupyay.dbhandler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.aunthtoo.timetableforcupyay.model.Interval;
import com.aunthtoo.timetableforcupyay.model.DailyTime;
import com.aunthtoo.timetableforcupyay.model.Monthly;
import com.aunthtoo.timetableforcupyay.model.Student;
import com.aunthtoo.timetableforcupyay.model.TeacherAndSubject;
import com.aunthtoo.timetableforcupyay.model.TimeTable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aunt Htoo on 11/26/2017.
 */

public class TimeTableDBHandler extends SQLiteOpenHelper {

    //version number to upgrade database version
    //each time if you Add, Edit table, you need to change the
    //version number.
    private static final int DATABASE_VERSION = 4;

    // Database Name
    private static final String DATABASE_NAME = "TimeTable.db";


    //for student table
    private static final String STUDENT_TABLE = "student";

    private static final String KEY_STUDENT_ID = "stid";
    private static final String KEY_STUDENT_ROLL = "strollno";
    private static final String KEY_STUDENT_NAME = "stname";
    private static final String KEY_STUDENT_PH = "stph";
    private static final String KEY_STUDENT_YEAR = "styear";
    private static final String KEY_STUDENT_SECTION = "stsection";


    //for time table table
    private static final String TIME_TABLE = "timetable";

    private static final String KEY_TIME_TABLE_ID = "timetableid";
    private static final String KEY_DAY = "day";
    private static final String KEY_SHORTSUBNAME = "shortsubname";


    //for interval
    private static final String INTERVAL_TABLE = "intervaltable";

    private static final String KEY_INTERVAL_ID = "intervalid";
    private static final String KEY_INTERVAL = "interval";
    private static final String KEY_START = "starttime";
    private static final String KEY_END = "endtime";

    //for shortlongsubname
    private static final String SHORT_LONG_NAME = "shortlongsubname";

    private static final String KEY_SHORTLONG_ID = "intervalid";
    private static final String KEY_SHORT = "shortname";
    private static final String KEY_LONG = "longname";
    private static final String KEY_TEACHER = "teacher";


    //for month one
    private static final String MONTH_ONE = "monthone";

    private static final String KEY_DATE = "date";

    private static final String KEY_TIME_ONE = "t1";
    private static final String KEY_TIME_TWO = "t2";
    private static final String KEY_TIME_THREE = "t3";
    private static final String KEY_TIME_FOUR = "t4";
    private static final String KEY_TIME_FIVE = "t5";
    private static final String KEY_TIME_SIX = "t6";
    private static final String KEY_TIME_SEVEN = "t7";


    //for month two
    private static final String MONTH_TWO = "monthtwo";

    //for month three
    private static final String MONTH_THREE = "monththree";

    //for month four
    private static final String MONTH_FOUR = "monthfour";

    //for month five
    private static final String MONTH_FIVE = "monthfive";

    //for month six
    private static final String MONTH_SIX = "monthsix";

    //for month seven
    private static final String MONTH_SEVEN = "monthseven";

    //for month eight
    private static final String MONTH_EIGHT = "montheight";

    //for monthly rc
    private static final String MONTHLY_RC = "monthlyrc";

    private static final String MONTH = "month";
    private static final String RC = "rc";

    //for offday table

    private static final String OFF_DAY = "offday";

    private static final String DATE = "date";

    public TimeTableDBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        //All necessary tables you like to create will create here

        //creating for student
        String CREATE_STUDENT = "CREATE TABLE " + STUDENT_TABLE + "("
                + KEY_STUDENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + KEY_STUDENT_ROLL + " TEXT, "
                + KEY_STUDENT_NAME + " TEXT, "
                + KEY_STUDENT_PH + " TEXT, "
                + KEY_STUDENT_YEAR + " TEXT, "
                + KEY_STUDENT_SECTION + " TEXT )";

        db.execSQL(CREATE_STUDENT);

        //creatingfortimetable
        String CREATE_TIME_TABLE = "CREATE TABLE " + TIME_TABLE + "("
                + KEY_TIME_TABLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + KEY_DAY + " TEXT, "
                + KEY_SHORTSUBNAME + " TEXT )";

        db.execSQL(CREATE_TIME_TABLE);

        //creatingforintervaltable
        String CREATE_INTERVAL = "CREATE TABLE " + INTERVAL_TABLE + "("
                + KEY_INTERVAL_ID + " INTEGER,"
                + KEY_INTERVAL + " TEXT, "
                + KEY_START + " TEXT, "
                + KEY_END + " TEXT )";

        db.execSQL(CREATE_INTERVAL);

        //creatingforlongshortname
        String CREATE_LONG_SHORT = "CREATE TABLE " + SHORT_LONG_NAME + "("
                + KEY_SHORTLONG_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + KEY_SHORT + " TEXT, "
                + KEY_LONG + " TEXT, "
                + KEY_TEACHER + " TEXT )";

        db.execSQL(CREATE_LONG_SHORT);

        //creatingfor one month
        String CREATE_TABLE_ONE_MONTH = "CREATE TABLE " + MONTH_ONE + "("
                + KEY_DATE + " TEXT, "
                + KEY_TIME_ONE + " TEXT, "
                + KEY_TIME_TWO + " TEXT, "
                + KEY_TIME_THREE + " TEXT, "
                + KEY_TIME_FOUR + " TEXT, "
                + KEY_TIME_FIVE + " TEXT, "
                + KEY_TIME_SIX + " TEXT, "
                + KEY_TIME_SEVEN + " TEXT )";

        db.execSQL(CREATE_TABLE_ONE_MONTH);

        //creatingfor two month
        String CREATE_TABLE_TWO_MONTH = "CREATE TABLE " + MONTH_TWO + "("
                + KEY_DATE + " TEXT, "
                + KEY_TIME_ONE + " TEXT, "
                + KEY_TIME_TWO + " TEXT, "
                + KEY_TIME_THREE + " TEXT, "
                + KEY_TIME_FOUR + " TEXT, "
                + KEY_TIME_FIVE + " TEXT, "
                + KEY_TIME_SIX + " TEXT, "
                + KEY_TIME_SEVEN + " TEXT )";

        db.execSQL(CREATE_TABLE_TWO_MONTH);

        //creatingfor THREE month
        String CREATE_TABLE_THREE_MONTH = "CREATE TABLE " + MONTH_THREE + "("
                + KEY_DATE + " TEXT, "
                + KEY_TIME_ONE + " TEXT, "
                + KEY_TIME_TWO + " TEXT, "
                + KEY_TIME_THREE + " TEXT, "
                + KEY_TIME_FOUR + " TEXT, "
                + KEY_TIME_FIVE + " TEXT, "
                + KEY_TIME_SIX + " TEXT, "
                + KEY_TIME_SEVEN + " TEXT )";

        db.execSQL(CREATE_TABLE_THREE_MONTH);

        //creatingfor FOUR month
        String CREATE_TABLE_FOUR_MONTH = "CREATE TABLE " + MONTH_FOUR + "("
                + KEY_DATE + " TEXT, "
                + KEY_TIME_ONE + " TEXT, "
                + KEY_TIME_TWO + " TEXT, "
                + KEY_TIME_THREE + " TEXT, "
                + KEY_TIME_FOUR + " TEXT, "
                + KEY_TIME_FIVE + " TEXT, "
                + KEY_TIME_SIX + " TEXT, "
                + KEY_TIME_SEVEN + " TEXT )";

        db.execSQL(CREATE_TABLE_FOUR_MONTH);

        //creatingfor FIVE month
        String CREATE_TABLE_FIVE_MONTH = "CREATE TABLE " + MONTH_FIVE + "("
                + KEY_DATE + " TEXT, "
                + KEY_TIME_ONE + " TEXT, "
                + KEY_TIME_TWO + " TEXT, "
                + KEY_TIME_THREE + " TEXT, "
                + KEY_TIME_FOUR + " TEXT, "
                + KEY_TIME_FIVE + " TEXT, "
                + KEY_TIME_SIX + " TEXT, "
                + KEY_TIME_SEVEN + " TEXT )";

        db.execSQL(CREATE_TABLE_FIVE_MONTH);

        //creatingfor SIX month
        String CREATE_TABLE_SIX_MONTH = "CREATE TABLE " + MONTH_SIX + "("
                + KEY_DATE + " TEXT, "
                + KEY_TIME_ONE + " TEXT, "
                + KEY_TIME_TWO + " TEXT, "
                + KEY_TIME_THREE + " TEXT, "
                + KEY_TIME_FOUR + " TEXT, "
                + KEY_TIME_FIVE + " TEXT, "
                + KEY_TIME_SIX + " TEXT, "
                + KEY_TIME_SEVEN + " TEXT )";

        db.execSQL(CREATE_TABLE_SIX_MONTH);

        //creatingfor SEVEN month
        String CREATE_TABLE_SEVEN_MONTH = "CREATE TABLE " + MONTH_SEVEN + "("
                + KEY_DATE + " TEXT, "
                + KEY_TIME_ONE + " TEXT, "
                + KEY_TIME_TWO + " TEXT, "
                + KEY_TIME_THREE + " TEXT, "
                + KEY_TIME_FOUR + " TEXT, "
                + KEY_TIME_FIVE + " TEXT, "
                + KEY_TIME_SIX + " TEXT, "
                + KEY_TIME_SEVEN + " TEXT )";

        db.execSQL(CREATE_TABLE_SEVEN_MONTH);

        //creatingfor EIGHT month
        String CREATE_TABLE_EIGHT_MONTH = "CREATE TABLE " + MONTH_EIGHT + "("
                + KEY_DATE + " TEXT, "
                + KEY_TIME_ONE + " TEXT, "
                + KEY_TIME_TWO + " TEXT, "
                + KEY_TIME_THREE + " TEXT, "
                + KEY_TIME_FOUR + " TEXT, "
                + KEY_TIME_FIVE + " TEXT, "
                + KEY_TIME_SIX + " TEXT, "
                + KEY_TIME_SEVEN + " TEXT )";

        db.execSQL(CREATE_TABLE_EIGHT_MONTH);

        //creatingfor monthly roll call
        String CREATE_TABLE_MONTHLY_ROLL_CALL = "CREATE TABLE " + MONTHLY_RC + "("
                + MONTH + " TEXT, "
                + RC + " TEXT )";

        db.execSQL(CREATE_TABLE_MONTHLY_ROLL_CALL);

        //creatingfor off day
        String CREATE_TABLE_OFF_DAY = "CREATE TABLE " + OFF_DAY + "("
                + DATE + " TEXT )";

        db.execSQL(CREATE_TABLE_OFF_DAY);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed, all data will be gone!!!

        db.execSQL("DROP TABLE IF EXISTS " + STUDENT_TABLE);

        db.execSQL("DROP TABLE IF EXISTS " + TIME_TABLE);

        db.execSQL("DROP TABLE IF EXISTS " + INTERVAL_TABLE);

        db.execSQL("DROP TABLE IF EXISTS " + SHORT_LONG_NAME);

        db.execSQL("DROP TABLE IF EXISTS " + MONTH_ONE);

        db.execSQL("DROP TABLE IF EXISTS " + MONTH_TWO);

        db.execSQL("DROP TABLE IF EXISTS " + MONTH_THREE);

        db.execSQL("DROP TABLE IF EXISTS " + MONTH_FOUR);

        db.execSQL("DROP TABLE IF EXISTS " + MONTH_FIVE);

        db.execSQL("DROP TABLE IF EXISTS " + MONTH_SIX);

        db.execSQL("DROP TABLE IF EXISTS " + MONTH_SEVEN);

        db.execSQL("DROP TABLE IF EXISTS " + MONTH_EIGHT);

        db.execSQL("DROP TABLE IF EXISTS " + MONTHLY_RC);

        db.execSQL("DROP TABLE IF EXISTS " + OFF_DAY);


        // Create tables again
        onCreate(db);

    }

    //for student start

    public void insertStudent(Student student) {
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_STUDENT_ROLL, student.getRollno());
        values.put(KEY_STUDENT_NAME, student.getStName());
        values.put(KEY_STUDENT_PH, student.getPhonenumber());
        values.put(KEY_STUDENT_YEAR, student.getYear());
        values.put(KEY_STUDENT_SECTION, student.getSection());

        database.insert(STUDENT_TABLE, null, values);


    }

    public boolean isStudentExist() {
        boolean retVal = false;

        SQLiteDatabase database = this.getReadableDatabase();

        String getQuery = "SELECT * FROM " + STUDENT_TABLE;

        Cursor cursor = database.rawQuery(getQuery, null);

        if (cursor.moveToFirst())
            retVal = true;


        return retVal;
    }

    public Student getStudent() {
        Student retVal = new Student();

        SQLiteDatabase database = this.getReadableDatabase();

        String getQuery = "SELECT * FROM " + STUDENT_TABLE;

        Cursor cursor = database.rawQuery(getQuery, null);
        if (cursor.moveToFirst()) {

            retVal.setRollno(cursor.getString(1));
            retVal.setStName(cursor.getString(2));
            retVal.setPhonenumber(cursor.getString(3));
            retVal.setYear(cursor.getString(4));
            retVal.setSection(cursor.getString(5));
        }

        cursor.close();

        return retVal;
    }

    public void updateStudent(Student student) {
        SQLiteDatabase database = this.getWritableDatabase();

        String delete = "DELETE FROM " + STUDENT_TABLE;

        if (isStudentExist()) {
            database.execSQL(delete);
        }

        insertStudent(student);


    }

    //for student end


    //for timetable start

    public void insertTimeTableData(List<TimeTable> timeTableList) {
        SQLiteDatabase database = this.getWritableDatabase();

        String delete = "DELETE FROM " + TIME_TABLE;

        if (isTimeTableExist())
            database.execSQL(delete);

        for (int i = 0; i < timeTableList.size(); i++) {
            TimeTable timeTablemodel = timeTableList.get(i);

            ContentValues contentValues = new ContentValues();
            contentValues.put(KEY_DAY, timeTablemodel.getDay());
            contentValues.put(KEY_SHORTSUBNAME, timeTablemodel.getSubject());
            database.insert(TIME_TABLE, null, contentValues);
        }

    }

    public boolean isTimeTableExist() {
        boolean retVal = false;

        SQLiteDatabase database = this.getReadableDatabase();

        String getQuery = "SELECT * FROM " + TIME_TABLE;

        Cursor cursor = database.rawQuery(getQuery, null);

        if (cursor.moveToFirst())
            retVal = true;

        cursor.close();

        return retVal;
    }

    public List<String> getShortSubjectNamewithday(String dayname) {
        List<String> retVal = new ArrayList<>();

        SQLiteDatabase database = this.getReadableDatabase();

        String selectquery = "SELECT " + KEY_SHORTSUBNAME + " FROM " + TIME_TABLE + " WHERE " + KEY_DAY + " = '" + dayname + "'";

        Cursor cursor = database.rawQuery(selectquery, null);

        // looping through rows and adding to model
        if (cursor.moveToFirst()) {
            do {
                String subname = cursor.getString(0);

                for (int i = 0; i < subname.split("_").length; i++) {
                    retVal.add(subname.split("_")[i]);
                }

            } while (cursor.moveToNext());
        }

        cursor.close();

        return retVal;
    }


    //for timetable end

    //for interval table start

    public void insertIntervalData(List<Interval> intervalList) {
        SQLiteDatabase database = this.getWritableDatabase();

        String delete = "DELETE FROM " + INTERVAL_TABLE;

        if (isIntervalExist())
            database.execSQL(delete);

        for (int i = 0; i < intervalList.size(); i++) {

            Interval interval = intervalList.get(i);
            ContentValues contentValues = new ContentValues();
            contentValues.put(KEY_INTERVAL_ID, i + 1);
            contentValues.put(KEY_INTERVAL, interval.getInterval());
            contentValues.put(KEY_START, interval.getStartTime());
            contentValues.put(KEY_END, interval.getEndTime());
            database.insert(INTERVAL_TABLE, null, contentValues);
        }


    }

    public boolean isIntervalExist() {
        boolean retVal = false;

        SQLiteDatabase database = this.getReadableDatabase();

        String getQuery = "SELECT * FROM " + INTERVAL_TABLE;

        Cursor cursor = database.rawQuery(getQuery, null);

        if (cursor.moveToFirst())
            retVal = true;

        return retVal;
    }

    public Interval getIntervalWithNumber(int num) {
        Interval retVal = new Interval();
        SQLiteDatabase database = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + INTERVAL_TABLE + " WHERE " + KEY_INTERVAL_ID + " = " + num;

        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {

                retVal.setIntervalNumber(cursor.getString(0));
                retVal.setInterval(cursor.getString(1));
                retVal.setStartTime(cursor.getString(2));
                retVal.setEndTime(cursor.getString(3));

            } while (cursor.moveToNext());
        }

        cursor.close();

        return retVal;
    }

    public int getIntervalCount() {
        int retVal = 0;

        SQLiteDatabase database = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + INTERVAL_TABLE;

        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                retVal++;

            } while (cursor.moveToNext());
        }


        cursor.close();

        return retVal;
    }

    //for interval table end


    //for teacher and subject name

    public void insertToTeacherAndSubTable(List<TeacherAndSubject> teacherAndSubjectList) {
        SQLiteDatabase database = this.getWritableDatabase();

        String delete = "DELETE FROM " + SHORT_LONG_NAME;

        if (isTeacherAndSubExist())
            database.execSQL(delete);

        for (int i = 0; i < teacherAndSubjectList.size(); i++) {

            TeacherAndSubject ts = teacherAndSubjectList.get(i);
            ContentValues contentValues = new ContentValues();

            contentValues.put(KEY_SHORT, ts.getShortSub());
            contentValues.put(KEY_LONG, ts.getLongSub());
            contentValues.put(KEY_TEACHER, ts.getTeacherName());

            database.insert(SHORT_LONG_NAME, null, contentValues);
        }


    }

    public boolean isTeacherAndSubExist() {
        boolean retVal = false;

        SQLiteDatabase database = this.getReadableDatabase();

        String getQuery = "SELECT * FROM " + SHORT_LONG_NAME;

        Cursor cursor = database.rawQuery(getQuery, null);

        if (cursor.moveToFirst())
            retVal = true;

        cursor.close();

        return retVal;
    }

    public String getSubLongNameWithShort(String shortname) {
        SQLiteDatabase database = this.getReadableDatabase();

        String retVal = null;

        String selectquery = "SELECT * FROM " + SHORT_LONG_NAME + " WHERE " + KEY_SHORT + " = '" + shortname + "'";


        Cursor cursor = database.rawQuery(selectquery, null);

        if (cursor.moveToFirst()) {
            do {

                retVal = cursor.getString(2);

            } while (cursor.moveToNext());
        }

        cursor.close();

        return retVal;
    }

    public String getTeacherNameWithShort(String shortname) {
        SQLiteDatabase database = this.getReadableDatabase();
        String retVal = null;

        String selectquery = "SELECT * FROM " + SHORT_LONG_NAME + " WHERE " + KEY_SHORT + " = '" + shortname + "'";


        Cursor cursor = database.rawQuery(selectquery, null);

        if (cursor.moveToFirst()) {
            do {

                retVal = cursor.getString(3);

            } while (cursor.moveToNext());
        }


        cursor.close();

        return retVal;
    }


    public List<TeacherAndSubject> getAllTeacherAndSub() {
        SQLiteDatabase database = this.getReadableDatabase();

        List<TeacherAndSubject> teacherAndSubjects = new ArrayList<>();


        String selectquery = "SELECT * FROM " + SHORT_LONG_NAME;


        Cursor cursor = database.rawQuery(selectquery, null);

        if (cursor.moveToFirst()) {
            do {

                teacherAndSubjects.add(new TeacherAndSubject(cursor.getString(1), cursor.getString(2), cursor.getString(3)));

            } while (cursor.moveToNext());
        }

        cursor.close();

        return teacherAndSubjects;

    }

    //for teacher and subject name


    //MONTH ONE START

    public void initializeWithNewDateinmonthone(String date) {
        SQLiteDatabase database = this.getWritableDatabase();

        String deleteq = "DELETE FROM " + MONTH_ONE + " WHERE " + KEY_DATE + " = '" + date + "'";

        if (isAlreadyExistThisDateInMonthOne(date))
            database.execSQL(deleteq);

        ContentValues contentValues = new ContentValues();

        contentValues.put(KEY_DATE, date);
        contentValues.put(KEY_TIME_ONE, "p");
        contentValues.put(KEY_TIME_TWO, "p");
        contentValues.put(KEY_TIME_THREE, "p");
        contentValues.put(KEY_TIME_FOUR, "p");
        contentValues.put(KEY_TIME_FIVE, "p");
        contentValues.put(KEY_TIME_SIX, "p");
        contentValues.put(KEY_TIME_SEVEN, "p");

        database.insert(MONTH_ONE, null, contentValues);


    }

    public void initializeWithNewDateinmonthoneAbsent(String date) {
        SQLiteDatabase database = this.getWritableDatabase();

        String deleteq = "DELETE FROM " + MONTH_ONE + " WHERE " + KEY_DATE + " = '" + date + "'";

        if (isAlreadyExistThisDateInMonthOne(date))
            database.execSQL(deleteq);


        ContentValues contentValues = new ContentValues();

        contentValues.put(KEY_DATE, date);
        contentValues.put(KEY_TIME_ONE, "a");
        contentValues.put(KEY_TIME_TWO, "a");
        contentValues.put(KEY_TIME_THREE, "a");
        contentValues.put(KEY_TIME_FOUR, "a");
        contentValues.put(KEY_TIME_FIVE, "a");
        contentValues.put(KEY_TIME_SIX, "a");
        contentValues.put(KEY_TIME_SEVEN, "a");

        database.insert(MONTH_ONE, null, contentValues);

    }

    public boolean isAlreadyExistThisDateInMonthOne(String date) {
        SQLiteDatabase database = this.getReadableDatabase();

        String getQuery = "SELECT * FROM " + MONTH_ONE + " WHERE " + KEY_DATE + " = '" + date + "'";

        Cursor cursor = database.rawQuery(getQuery, null);

        boolean retVal = cursor.moveToFirst();

        cursor.close();

        return retVal;
    }

    public int getCountOnMonthOone() {

        SQLiteDatabase database = this.getReadableDatabase();

        String getQuery = "SELECT * FROM " + MONTH_ONE;

        Cursor cursor = database.rawQuery(getQuery, null);

        int count = cursor.getCount();

        cursor.close();

        return count;

    }

    public List<DailyTime> getAllOnMOnthOne() {
        SQLiteDatabase database = this.getReadableDatabase();
        List<DailyTime> list = new ArrayList<>();

        String getQuery = "SELECT * FROM " + MONTH_ONE;

        Cursor cursor = database.rawQuery(getQuery, null);

        if (cursor.moveToFirst()) {
            do {

                DailyTime dailyTime = new DailyTime();

                dailyTime.setDate(cursor.getString(0));

                list.add(dailyTime);

            } while (cursor.moveToNext());
        }
        return list;
    }

    public DailyTime getMonthwithdateInMonthOne(String date) {
        DailyTime month = null;

        SQLiteDatabase database = this.getReadableDatabase();

        String getQuery = "SELECT * FROM " + MONTH_ONE + " WHERE " + KEY_DATE + " = '" + date + "'";

        Cursor cursor = database.rawQuery(getQuery, null);

        if (cursor.moveToFirst()) {
            month = new DailyTime(date, cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7));


        }

        cursor.close();

        return month;

    }

    public void updateMonthOne(DailyTime dailyTime) {
        SQLiteDatabase database = this.getWritableDatabase();

        String delete = "DELETE FROM " + MONTH_ONE + " WHERE " + KEY_DATE + " = '" + dailyTime.getDate() + "'";

        if (isAlreadyExistThisDateInMonthOne(dailyTime.getDate()))
            database.execSQL(delete);

        ContentValues contentValues = new ContentValues();

        contentValues.put(KEY_DATE, dailyTime.getDate());
        contentValues.put(KEY_TIME_ONE, dailyTime.getTimeone());
        contentValues.put(KEY_TIME_TWO, dailyTime.getTimetwo());
        contentValues.put(KEY_TIME_THREE, dailyTime.getTimethree());
        contentValues.put(KEY_TIME_FOUR, dailyTime.getTimefour());
        contentValues.put(KEY_TIME_FIVE, dailyTime.getTimefive());
        contentValues.put(KEY_TIME_SIX, dailyTime.getTimesix());
        contentValues.put(KEY_TIME_SEVEN, dailyTime.getTimeseven());

        database.insert(MONTH_ONE, null, contentValues);

    }

    public int getTotalAbsentTimeInMonthOne() {
        int count = 0;

        SQLiteDatabase database = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + MONTH_ONE;

        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {

                if (cursor.getString(1).equals("a"))
                    count++;

                if (cursor.getString(2).equals("a"))
                    count++;

                if (cursor.getString(3).equals("a"))
                    count++;

                if (cursor.getString(4).equals("a"))
                    count++;

                if (cursor.getString(5).equals("a"))
                    count++;

                if (cursor.getString(6).equals("a"))
                    count++;

                if (cursor.getString(7).equals("a"))
                    count++;


            }
            while (cursor.moveToNext());
        }

        cursor.close();

        return count;
    }


    public void deleteRecordInMonthOne(String date) {
        SQLiteDatabase database = this.getWritableDatabase();

        String deletequery = "DELETE FROM " + MONTH_ONE + " WHERE " + KEY_DATE + " = '" + date + "'";

        if (isAlreadyExistThisDateInMonthOne(date))
            database.execSQL(deletequery);

    }


    //MONTH ONE END

    //MONTH TWO START

    public void initializeWithNewDateinmonthtwo(String date) {
        SQLiteDatabase database = this.getWritableDatabase();


        String delete = "DELETE FROM " + MONTH_TWO + " WHERE " + KEY_DATE + " = '" + date + "'";

        if (isAlreadyExistThisDateInMonthTwo(date))
            database.execSQL(delete);


        ContentValues contentValues = new ContentValues();

        contentValues.put(KEY_DATE, date);
        contentValues.put(KEY_TIME_ONE, "p");
        contentValues.put(KEY_TIME_TWO, "p");
        contentValues.put(KEY_TIME_THREE, "p");
        contentValues.put(KEY_TIME_FOUR, "p");
        contentValues.put(KEY_TIME_FIVE, "p");
        contentValues.put(KEY_TIME_SIX, "p");
        contentValues.put(KEY_TIME_SEVEN, "p");

        database.insert(MONTH_TWO, null, contentValues);

    }

    public void initializeWithNewDateinmonthtwoabsent(String date) {
        SQLiteDatabase database = this.getWritableDatabase();


        String delete = "DELETE FROM " + MONTH_TWO + " WHERE " + KEY_DATE + " = '" + date + "'";

        if (isAlreadyExistThisDateInMonthTwo(date))
            database.execSQL(delete);


        ContentValues contentValues = new ContentValues();

        contentValues.put(KEY_DATE, date);
        contentValues.put(KEY_TIME_ONE, "a");
        contentValues.put(KEY_TIME_TWO, "a");
        contentValues.put(KEY_TIME_THREE, "a");
        contentValues.put(KEY_TIME_FOUR, "a");
        contentValues.put(KEY_TIME_FIVE, "a");
        contentValues.put(KEY_TIME_SIX, "a");
        contentValues.put(KEY_TIME_SEVEN, "a");

        database.insert(MONTH_TWO, null, contentValues);

    }

    public boolean isAlreadyExistThisDateInMonthTwo(String date) {
        SQLiteDatabase database = this.getReadableDatabase();

        String getQuery = "SELECT * FROM " + MONTH_TWO + " WHERE " + KEY_DATE + " = '" + date + "'";

        Cursor cursor = database.rawQuery(getQuery, null);

        boolean retVal = cursor.moveToFirst();

        cursor.close();

        return retVal;
    }

    public DailyTime getMonthwithdateInMonthTwo(String date) {
        DailyTime month = null;

        SQLiteDatabase database = this.getReadableDatabase();

        String getQuery = "SELECT * FROM " + MONTH_TWO + " WHERE " + KEY_DATE + " = '" + date + "'";

        Cursor cursor = database.rawQuery(getQuery, null);

        if (cursor.moveToFirst()) {
            month = new DailyTime(date, cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7));


        }

        cursor.close();

        return month;

    }

    public void updateMonthTwo(DailyTime dailyTime) {
        SQLiteDatabase database = this.getWritableDatabase();

        String delete = "DELETE FROM " + MONTH_TWO + " WHERE " + KEY_DATE + " = '" + dailyTime.getDate() + "'";

        if (isAlreadyExistThisDateInMonthTwo(dailyTime.getDate()))
            database.execSQL(delete);

        ContentValues contentValues = new ContentValues();

        contentValues.put(KEY_DATE, dailyTime.getDate());
        contentValues.put(KEY_TIME_ONE, dailyTime.getTimeone());
        contentValues.put(KEY_TIME_TWO, dailyTime.getTimetwo());
        contentValues.put(KEY_TIME_THREE, dailyTime.getTimethree());
        contentValues.put(KEY_TIME_FOUR, dailyTime.getTimefour());
        contentValues.put(KEY_TIME_FIVE, dailyTime.getTimefive());
        contentValues.put(KEY_TIME_SIX, dailyTime.getTimesix());
        contentValues.put(KEY_TIME_SEVEN, dailyTime.getTimeseven());

        database.insert(MONTH_TWO, null, contentValues);

    }

    public int getTotalAbsentTimeInMonthTwo() {
        int count = 0;

        SQLiteDatabase database = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + MONTH_TWO;

        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {

                if (cursor.getString(1).equals("a"))
                    count++;

                if (cursor.getString(2).equals("a"))
                    count++;

                if (cursor.getString(3).equals("a"))
                    count++;

                if (cursor.getString(4).equals("a"))
                    count++;

                if (cursor.getString(5).equals("a"))
                    count++;

                if (cursor.getString(6).equals("a"))
                    count++;

                if (cursor.getString(7).equals("a"))
                    count++;


            }
            while (cursor.moveToNext());
        }

        cursor.close();

        return count;
    }

    public void deleteRecordInMonthTwo(String date) {
        SQLiteDatabase database = this.getWritableDatabase();

        String deletequery = "DELETE FROM " + MONTH_TWO + " WHERE " + KEY_DATE + " = '" + date + "'";

        if (isAlreadyExistThisDateInMonthTwo(date))
            database.execSQL(deletequery);

    }


    //MONTH TWO END

    //MONTH THREE START

    public void initializeWithNewDateinmonththree(String date) {
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();


        String delete = "DELETE FROM " + MONTH_THREE + " WHERE " + KEY_DATE + " = '" + date + "'";

        if (isAlreadyExistThisDateInMonthThree(date))
            database.execSQL(delete);

        contentValues.put(KEY_DATE, date);
        contentValues.put(KEY_TIME_ONE, "p");
        contentValues.put(KEY_TIME_TWO, "p");
        contentValues.put(KEY_TIME_THREE, "p");
        contentValues.put(KEY_TIME_FOUR, "p");
        contentValues.put(KEY_TIME_FIVE, "p");
        contentValues.put(KEY_TIME_SIX, "p");
        contentValues.put(KEY_TIME_SEVEN, "p");

        database.insert(MONTH_THREE, null, contentValues);

    }

    public void initializeWithNewDateinmonththreeabsent(String date) {
        SQLiteDatabase database = this.getWritableDatabase();

        String delete = "DELETE FROM " + MONTH_THREE + " WHERE " + KEY_DATE + " = '" + date + "'";

        if (isAlreadyExistThisDateInMonthThree(date))
            database.execSQL(delete);


        ContentValues contentValues = new ContentValues();

        contentValues.put(KEY_DATE, date);
        contentValues.put(KEY_TIME_ONE, "a");
        contentValues.put(KEY_TIME_TWO, "a");
        contentValues.put(KEY_TIME_THREE, "a");
        contentValues.put(KEY_TIME_FOUR, "a");
        contentValues.put(KEY_TIME_FIVE, "a");
        contentValues.put(KEY_TIME_SIX, "a");
        contentValues.put(KEY_TIME_SEVEN, "a");

        database.insert(MONTH_THREE, null, contentValues);


    }

    public boolean isAlreadyExistThisDateInMonthThree(String date) {
        SQLiteDatabase database = this.getReadableDatabase();

        String getQuery = "SELECT * FROM " + MONTH_THREE + " WHERE " + KEY_DATE + " = '" + date + "'";

        Cursor cursor = database.rawQuery(getQuery, null);

        boolean retVal = cursor.moveToFirst();
        cursor.close();

        return retVal;
    }

    public DailyTime getMonthwithdateInMonthThree(String date) {
        DailyTime month = null;

        SQLiteDatabase database = this.getReadableDatabase();

        String getQuery = "SELECT * FROM " + MONTH_THREE + " WHERE " + KEY_DATE + " = '" + date + "'";


        Cursor cursor = database.rawQuery(getQuery, null);

        if (cursor.moveToFirst()) {
            month = new DailyTime(date, cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7));


        }

        cursor.close();

        return month;

    }


    public void updateMonthThree(DailyTime dailyTime) {
        SQLiteDatabase database = this.getWritableDatabase();

        String delete = "DELETE FROM " + MONTH_THREE + " WHERE " + KEY_DATE + " = '" + dailyTime.getDate() + "'";

        if (isAlreadyExistThisDateInMonthThree(dailyTime.getDate()))
            database.execSQL(delete);

        ContentValues contentValues = new ContentValues();

        contentValues.put(KEY_DATE, dailyTime.getDate());
        contentValues.put(KEY_TIME_ONE, dailyTime.getTimeone());
        contentValues.put(KEY_TIME_TWO, dailyTime.getTimetwo());
        contentValues.put(KEY_TIME_THREE, dailyTime.getTimethree());
        contentValues.put(KEY_TIME_FOUR, dailyTime.getTimefour());
        contentValues.put(KEY_TIME_FIVE, dailyTime.getTimefive());
        contentValues.put(KEY_TIME_SIX, dailyTime.getTimesix());
        contentValues.put(KEY_TIME_SEVEN, dailyTime.getTimeseven());

        database.insert(MONTH_THREE, null, contentValues);


    }

    public int getTotalAbsentTimeInMonthThree() {
        int count = 0;

        SQLiteDatabase database = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + MONTH_THREE;

        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {

                if (cursor.getString(1).equals("a"))
                    count++;

                if (cursor.getString(2).equals("a"))
                    count++;

                if (cursor.getString(3).equals("a"))
                    count++;

                if (cursor.getString(4).equals("a"))
                    count++;

                if (cursor.getString(5).equals("a"))
                    count++;

                if (cursor.getString(6).equals("a"))
                    count++;

                if (cursor.getString(7).equals("a"))
                    count++;


            }
            while (cursor.moveToNext());
        }

        cursor.close();

        return count;
    }

    public void deleteRecordInMonthThree(String date) {
        SQLiteDatabase database = this.getWritableDatabase();

        String deletequery = "DELETE FROM " + MONTH_THREE + " WHERE " + KEY_DATE + " = '" + date + "'";

        if (isAlreadyExistThisDateInMonthThree(date))
            database.execSQL(deletequery);

    }


    //MONTH THREE END

    //MONTH FOUR START

    public void initializeWithNewDateinmonthfour(String date) {
        SQLiteDatabase database = this.getWritableDatabase();

        String delete = "DELETE FROM " + MONTH_FOUR + " WHERE " + KEY_DATE + " = '" + date + "'";

        if (isAlreadyExistThisDateInMonthFour(date))
            database.execSQL(delete);

        ContentValues contentValues = new ContentValues();

        contentValues.put(KEY_DATE, date);
        contentValues.put(KEY_TIME_ONE, "p");
        contentValues.put(KEY_TIME_TWO, "p");
        contentValues.put(KEY_TIME_THREE, "p");
        contentValues.put(KEY_TIME_FOUR, "p");
        contentValues.put(KEY_TIME_FIVE, "p");
        contentValues.put(KEY_TIME_SIX, "p");
        contentValues.put(KEY_TIME_SEVEN, "p");

        database.insert(MONTH_FOUR, null, contentValues);


    }

    public void initializeWithNewDateinmonthfourabsent(String date) {
        SQLiteDatabase database = this.getWritableDatabase();

        String delete = "DELETE FROM " + MONTH_FOUR + " WHERE " + KEY_DATE + " = '" + date + "'";

        if (isAlreadyExistThisDateInMonthFour(date))
            database.execSQL(delete);

        ContentValues contentValues = new ContentValues();

        contentValues.put(KEY_DATE, date);
        contentValues.put(KEY_TIME_ONE, "a");
        contentValues.put(KEY_TIME_TWO, "a");
        contentValues.put(KEY_TIME_THREE, "a");
        contentValues.put(KEY_TIME_FOUR, "a");
        contentValues.put(KEY_TIME_FIVE, "a");
        contentValues.put(KEY_TIME_SIX, "a");
        contentValues.put(KEY_TIME_SEVEN, "a");

        database.insert(MONTH_FOUR, null, contentValues);


    }

    public boolean isAlreadyExistThisDateInMonthFour(String date) {
        SQLiteDatabase database = this.getReadableDatabase();

        String getQuery = "SELECT * FROM " + MONTH_FOUR + " WHERE " + KEY_DATE + " = '" + date + "'";

        Cursor cursor = database.rawQuery(getQuery, null);

        boolean retVal = cursor.moveToFirst();

        cursor.close();

        return retVal;
    }

    public DailyTime getMonthwithdateInMonthFour(String date) {
        DailyTime month = null;

        SQLiteDatabase database = this.getReadableDatabase();

        String getQuery = "SELECT * FROM " + MONTH_FOUR + " WHERE " + KEY_DATE + " = '" + date + "'";


        Cursor cursor = database.rawQuery(getQuery, null);

        if (cursor.moveToFirst()) {
            month = new DailyTime(date, cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7));


        }

        cursor.close();

        return month;

    }

    public void updateMonthFour(DailyTime dailyTime) {
        SQLiteDatabase database = this.getWritableDatabase();

        String delete = "DELETE FROM " + MONTH_FOUR + " WHERE " + KEY_DATE + " = '" + dailyTime.getDate() + "'";

        if (isAlreadyExistThisDateInMonthFour(dailyTime.getDate()))
            database.execSQL(delete);


        ContentValues contentValues = new ContentValues();

        contentValues.put(KEY_DATE, dailyTime.getDate());
        contentValues.put(KEY_TIME_ONE, dailyTime.getTimeone());
        contentValues.put(KEY_TIME_TWO, dailyTime.getTimetwo());
        contentValues.put(KEY_TIME_THREE, dailyTime.getTimethree());
        contentValues.put(KEY_TIME_FOUR, dailyTime.getTimefour());
        contentValues.put(KEY_TIME_FIVE, dailyTime.getTimefive());
        contentValues.put(KEY_TIME_SIX, dailyTime.getTimesix());
        contentValues.put(KEY_TIME_SEVEN, dailyTime.getTimeseven());

        database.insert(MONTH_FOUR, null, contentValues);


    }

    public int getTotalAbsentTimeInMonthFour() {
        int count = 0;

        SQLiteDatabase database = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + MONTH_FOUR;

        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {

                if (cursor.getString(1).equals("a"))
                    count++;

                if (cursor.getString(2).equals("a"))
                    count++;

                if (cursor.getString(3).equals("a"))
                    count++;

                if (cursor.getString(4).equals("a"))
                    count++;

                if (cursor.getString(5).equals("a"))
                    count++;

                if (cursor.getString(6).equals("a"))
                    count++;

                if (cursor.getString(7).equals("a"))
                    count++;


            }
            while (cursor.moveToNext());
        }

        cursor.close();

        return count;
    }


    public void deleteRecordInMonthFour(String date) {
        SQLiteDatabase database = this.getWritableDatabase();

        String deletequery = "DELETE FROM " + MONTH_FOUR + " WHERE " + KEY_DATE + " = '" + date + "'";

        if (isAlreadyExistThisDateInMonthFour(date))
            database.execSQL(deletequery);

    }


    //MONTH FOUR END

    //MONTH FIVE START

    public void initializeWithNewDateinmonthfive(String date) {
        SQLiteDatabase database = this.getWritableDatabase();

        String delete = "DELETE FROM " + MONTH_FIVE + " WHERE " + KEY_DATE + " = '" + date + "'";

        if (isAlreadyExistThisDateInMonthFive(date))
            database.execSQL(delete);

        ContentValues contentValues = new ContentValues();

        contentValues.put(KEY_DATE, date);
        contentValues.put(KEY_TIME_ONE, "p");
        contentValues.put(KEY_TIME_TWO, "p");
        contentValues.put(KEY_TIME_THREE, "p");
        contentValues.put(KEY_TIME_FOUR, "p");
        contentValues.put(KEY_TIME_FIVE, "p");
        contentValues.put(KEY_TIME_SIX, "p");
        contentValues.put(KEY_TIME_SEVEN, "p");

        database.insert(MONTH_FIVE, null, contentValues);

    }

    public void initializeWithNewDateinmonthfiveabsent(String date) {
        SQLiteDatabase database = this.getWritableDatabase();

        String delete = "DELETE FROM " + MONTH_FIVE + " WHERE " + KEY_DATE + " = '" + date + "'";

        if (isAlreadyExistThisDateInMonthFive(date))
            database.execSQL(delete);

        ContentValues contentValues = new ContentValues();

        contentValues.put(KEY_DATE, date);
        contentValues.put(KEY_TIME_ONE, "a");
        contentValues.put(KEY_TIME_TWO, "a");
        contentValues.put(KEY_TIME_THREE, "a");
        contentValues.put(KEY_TIME_FOUR, "a");
        contentValues.put(KEY_TIME_FIVE, "a");
        contentValues.put(KEY_TIME_SIX, "a");
        contentValues.put(KEY_TIME_SEVEN, "a");

        database.insert(MONTH_FIVE, null, contentValues);

    }

    public boolean isAlreadyExistThisDateInMonthFive(String date) {
        SQLiteDatabase database = this.getReadableDatabase();

        String getQuery = "SELECT * FROM " + MONTH_FIVE + " WHERE " + KEY_DATE + " = '" + date + "'";

        Cursor cursor = database.rawQuery(getQuery, null);

        boolean retVal = cursor.moveToFirst();

        cursor.close();

        return retVal;
    }

    public DailyTime getMonthwithdateInMonthFive(String date) {
        DailyTime month = null;

        SQLiteDatabase database = this.getReadableDatabase();

        String getQuery = "SELECT * FROM " + MONTH_FIVE + " WHERE " + KEY_DATE + " = '" + date + "'";


        Cursor cursor = database.rawQuery(getQuery, null);

        if (cursor.moveToFirst()) {
            month = new DailyTime(date, cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7));


        }

        cursor.close();

        return month;

    }

    public void updateMonthFive(DailyTime dailyTime) {
        SQLiteDatabase database = this.getWritableDatabase();

        String delete = "DELETE FROM " + MONTH_FIVE + " WHERE " + KEY_DATE + " = '" + dailyTime.getDate() + "'";

        if (isAlreadyExistThisDateInMonthFive(dailyTime.getDate()))
            database.execSQL(delete);

        ContentValues contentValues = new ContentValues();

        contentValues.put(KEY_DATE, dailyTime.getDate());
        contentValues.put(KEY_TIME_ONE, dailyTime.getTimeone());
        contentValues.put(KEY_TIME_TWO, dailyTime.getTimetwo());
        contentValues.put(KEY_TIME_THREE, dailyTime.getTimethree());
        contentValues.put(KEY_TIME_FOUR, dailyTime.getTimefour());
        contentValues.put(KEY_TIME_FIVE, dailyTime.getTimefive());
        contentValues.put(KEY_TIME_SIX, dailyTime.getTimesix());
        contentValues.put(KEY_TIME_SEVEN, dailyTime.getTimeseven());

        database.insert(MONTH_FIVE, null, contentValues);


    }

    public int getTotalAbsentTimeInMonthFive() {
        int count = 0;

        SQLiteDatabase database = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + MONTH_FIVE;

        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {

                if (cursor.getString(1).equals("a"))
                    count++;

                if (cursor.getString(2).equals("a"))
                    count++;

                if (cursor.getString(3).equals("a"))
                    count++;

                if (cursor.getString(4).equals("a"))
                    count++;

                if (cursor.getString(5).equals("a"))
                    count++;

                if (cursor.getString(6).equals("a"))
                    count++;

                if (cursor.getString(7).equals("a"))
                    count++;


            }
            while (cursor.moveToNext());
        }

        cursor.close();

        return count;
    }

    public void deleteRecordInMonthFive(String date) {
        SQLiteDatabase database = this.getWritableDatabase();

        String deletequery = "DELETE FROM " + MONTH_FIVE + " WHERE " + KEY_DATE + " = '" + date + "'";

        if (isAlreadyExistThisDateInMonthFive(date))
            database.execSQL(deletequery);

    }


    //MONTH FIVE END

    //MONTH SIX START

    public void initializeWithNewDateinmonthsix(String date) {
        SQLiteDatabase database = this.getWritableDatabase();


        String delete = "DELETE FROM " + MONTH_SIX + " WHERE " + KEY_DATE + " = '" + date + "'";

        if (isAlreadyExistThisDateInMonthSix(date))
            database.execSQL(delete);


        ContentValues contentValues = new ContentValues();

        contentValues.put(KEY_DATE, date);
        contentValues.put(KEY_TIME_ONE, "p");
        contentValues.put(KEY_TIME_TWO, "p");
        contentValues.put(KEY_TIME_THREE, "p");
        contentValues.put(KEY_TIME_FOUR, "p");
        contentValues.put(KEY_TIME_FIVE, "p");
        contentValues.put(KEY_TIME_SIX, "p");
        contentValues.put(KEY_TIME_SEVEN, "p");

        database.insert(MONTH_SIX, null, contentValues);

    }

    public void initializeWithNewDateinmonthsixabsent(String date) {
        SQLiteDatabase database = this.getWritableDatabase();

        String delete = "DELETE FROM " + MONTH_SIX + " WHERE " + KEY_DATE + " = '" + date + "'";

        if (isAlreadyExistThisDateInMonthSix(date))
            database.execSQL(delete);

        ContentValues contentValues = new ContentValues();

        contentValues.put(KEY_DATE, date);
        contentValues.put(KEY_TIME_ONE, "a");
        contentValues.put(KEY_TIME_TWO, "a");
        contentValues.put(KEY_TIME_THREE, "a");
        contentValues.put(KEY_TIME_FOUR, "a");
        contentValues.put(KEY_TIME_FIVE, "a");
        contentValues.put(KEY_TIME_SIX, "a");
        contentValues.put(KEY_TIME_SEVEN, "a");

        database.insert(MONTH_SIX, null, contentValues);

    }

    public boolean isAlreadyExistThisDateInMonthSix(String date) {
        SQLiteDatabase database = this.getReadableDatabase();

        String getQuery = "SELECT * FROM " + MONTH_SIX + " WHERE " + KEY_DATE + " = '" + date + "'";

        Cursor cursor = database.rawQuery(getQuery, null);

        boolean retVal = cursor.moveToFirst();

        cursor.close();

        return retVal;
    }

    public DailyTime getMonthwithdateInMonthSix(String date) {
        DailyTime month = null;

        SQLiteDatabase database = this.getReadableDatabase();

        String getQuery = "SELECT * FROM " + MONTH_SIX + " WHERE " + KEY_DATE + " = '" + date + "'";


        Cursor cursor = database.rawQuery(getQuery, null);

        if (cursor.moveToFirst()) {
            month = new DailyTime(date, cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7));


        }

        cursor.close();

        return month;

    }

    public void updateMonthSix(DailyTime dailyTime) {
        SQLiteDatabase database = this.getWritableDatabase();

        String delete = "DELETE FROM " + MONTH_SIX + " WHERE " + KEY_DATE + " = '" + dailyTime.getDate() + "'";

        if (isAlreadyExistThisDateInMonthSix(dailyTime.getDate()))
            database.execSQL(delete);

        ContentValues contentValues = new ContentValues();

        contentValues.put(KEY_DATE, dailyTime.getDate());
        contentValues.put(KEY_TIME_ONE, dailyTime.getTimeone());
        contentValues.put(KEY_TIME_TWO, dailyTime.getTimetwo());
        contentValues.put(KEY_TIME_THREE, dailyTime.getTimethree());
        contentValues.put(KEY_TIME_FOUR, dailyTime.getTimefour());
        contentValues.put(KEY_TIME_FIVE, dailyTime.getTimefive());
        contentValues.put(KEY_TIME_SIX, dailyTime.getTimesix());
        contentValues.put(KEY_TIME_SEVEN, dailyTime.getTimeseven());

        database.insert(MONTH_SIX, null, contentValues);

    }

    public int getTotalAbsentTimeInMonthSix() {
        int count = 0;

        SQLiteDatabase database = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + MONTH_SIX;

        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {

                if (cursor.getString(1).equals("a"))
                    count++;

                if (cursor.getString(2).equals("a"))
                    count++;

                if (cursor.getString(3).equals("a"))
                    count++;

                if (cursor.getString(4).equals("a"))
                    count++;

                if (cursor.getString(5).equals("a"))
                    count++;

                if (cursor.getString(6).equals("a"))
                    count++;

                if (cursor.getString(7).equals("a"))
                    count++;


            }
            while (cursor.moveToNext());
        }

        cursor.close();

        return count;
    }

    public void deleteRecordInMonthSix(String date) {
        SQLiteDatabase database = this.getWritableDatabase();

        String deletequery = "DELETE FROM " + MONTH_SIX + " WHERE " + KEY_DATE + " = '" + date + "'";

        if (isAlreadyExistThisDateInMonthSix(date))
            database.execSQL(deletequery);

    }


    //MONTH SIX END

    //MONTH SEVEN START

    public void initializeWithNewDateinmonthseven(String date) {
        SQLiteDatabase database = this.getWritableDatabase();

        String delete = "DELETE FROM " + MONTH_SEVEN + " WHERE " + KEY_DATE + " = '" + date + "'";

        if (isAlreadyExistThisDateInMonthSeven(date))
            database.execSQL(delete);

        ContentValues contentValues = new ContentValues();

        contentValues.put(KEY_DATE, date);
        contentValues.put(KEY_TIME_ONE, "p");
        contentValues.put(KEY_TIME_TWO, "p");
        contentValues.put(KEY_TIME_THREE, "p");
        contentValues.put(KEY_TIME_FOUR, "p");
        contentValues.put(KEY_TIME_FIVE, "p");
        contentValues.put(KEY_TIME_SIX, "p");
        contentValues.put(KEY_TIME_SEVEN, "p");

        database.insert(MONTH_SEVEN, null, contentValues);


    }

    public void initializeWithNewDateinmonthsevenabsent(String date) {
        SQLiteDatabase database = this.getWritableDatabase();

        String delete = "DELETE FROM " + MONTH_SEVEN + " WHERE " + KEY_DATE + " = '" + date + "'";

        if (isAlreadyExistThisDateInMonthSeven(date))
            database.execSQL(delete);

        ContentValues contentValues = new ContentValues();

        contentValues.put(KEY_DATE, date);
        contentValues.put(KEY_TIME_ONE, "a");
        contentValues.put(KEY_TIME_TWO, "a");
        contentValues.put(KEY_TIME_THREE, "a");
        contentValues.put(KEY_TIME_FOUR, "a");
        contentValues.put(KEY_TIME_FIVE, "a");
        contentValues.put(KEY_TIME_SIX, "a");
        contentValues.put(KEY_TIME_SEVEN, "a");

        database.insert(MONTH_SEVEN, null, contentValues);


    }

    public boolean isAlreadyExistThisDateInMonthSeven(String date) {
        SQLiteDatabase database = this.getReadableDatabase();

        String getQuery = "SELECT * FROM " + MONTH_SEVEN + " WHERE " + KEY_DATE + " = '" + date + "'";

        Cursor cursor = database.rawQuery(getQuery, null);

        boolean retval = cursor.moveToFirst();
        cursor.close();

        return retval;
    }

    public DailyTime getMonthwithdateInMonthSeven(String date) {
        DailyTime month = null;

        SQLiteDatabase database = this.getReadableDatabase();

        String getQuery = "SELECT * FROM " + MONTH_SEVEN + " WHERE " + KEY_DATE + " = '" + date + "'";


        Cursor cursor = database.rawQuery(getQuery, null);

        if (cursor.moveToFirst()) {
            month = new DailyTime(date, cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7));


        }

        cursor.close();

        return month;

    }

    public void updateMonthSeven(DailyTime dailyTime) {
        SQLiteDatabase database = this.getWritableDatabase();

        String delete = "DELETE FROM " + MONTH_SEVEN + " WHERE " + KEY_DATE + " = '" + dailyTime.getDate() + "'";

        if (isAlreadyExistThisDateInMonthSeven(dailyTime.getDate()))
            database.execSQL(delete);

        ContentValues contentValues = new ContentValues();

        contentValues.put(KEY_DATE, dailyTime.getDate());
        contentValues.put(KEY_TIME_ONE, dailyTime.getTimeone());
        contentValues.put(KEY_TIME_TWO, dailyTime.getTimetwo());
        contentValues.put(KEY_TIME_THREE, dailyTime.getTimethree());
        contentValues.put(KEY_TIME_FOUR, dailyTime.getTimefour());
        contentValues.put(KEY_TIME_FIVE, dailyTime.getTimefive());
        contentValues.put(KEY_TIME_SIX, dailyTime.getTimesix());
        contentValues.put(KEY_TIME_SEVEN, dailyTime.getTimeseven());

        database.insert(MONTH_SEVEN, null, contentValues);


    }

    public int getTotalAbsentTimeInMonthSeven() {
        int count = 0;

        SQLiteDatabase database = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + MONTH_SEVEN;

        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {

                if (cursor.getString(1).equals("a"))
                    count++;

                if (cursor.getString(2).equals("a"))
                    count++;

                if (cursor.getString(3).equals("a"))
                    count++;

                if (cursor.getString(4).equals("a"))
                    count++;

                if (cursor.getString(5).equals("a"))
                    count++;

                if (cursor.getString(6).equals("a"))
                    count++;

                if (cursor.getString(7).equals("a"))
                    count++;


            }
            while (cursor.moveToNext());
        }

        cursor.close();

        return count;
    }

    public void deleteRecordInMonthSeven(String date) {
        SQLiteDatabase database = this.getWritableDatabase();

        String deletequery = "DELETE FROM " + MONTH_SEVEN + " WHERE " + KEY_DATE + " = '" + date + "'";

        if (isAlreadyExistThisDateInMonthSeven(date))
            database.execSQL(deletequery);

    }


    //MONTH SEVEN END

    //MONTH EIGHT START

    public void initializeWithNewDateinmontheight(String date) {
        SQLiteDatabase database = this.getWritableDatabase();


        String delete = "DELETE FROM " + MONTH_EIGHT + " WHERE " + KEY_DATE + " = '" + date + "'";

        if (isAlreadyExistThisDateInMonthEight(date))
            database.execSQL(delete);

        ContentValues contentValues = new ContentValues();

        contentValues.put(KEY_DATE, date);
        contentValues.put(KEY_TIME_ONE, "p");
        contentValues.put(KEY_TIME_TWO, "p");
        contentValues.put(KEY_TIME_THREE, "p");
        contentValues.put(KEY_TIME_FOUR, "p");
        contentValues.put(KEY_TIME_FIVE, "p");
        contentValues.put(KEY_TIME_SIX, "p");
        contentValues.put(KEY_TIME_SEVEN, "p");

        database.insert(MONTH_EIGHT, null, contentValues);


    }

    public void initializeWithNewDateinmontheightabsent(String date) {
        SQLiteDatabase database = this.getWritableDatabase();

        String delete = "DELETE FROM " + MONTH_EIGHT + " WHERE " + KEY_DATE + " = '" + date + "'";

        if (isAlreadyExistThisDateInMonthEight(date))
            database.execSQL(delete);

        ContentValues contentValues = new ContentValues();

        contentValues.put(KEY_DATE, date);
        contentValues.put(KEY_TIME_ONE, "a");
        contentValues.put(KEY_TIME_TWO, "a");
        contentValues.put(KEY_TIME_THREE, "a");
        contentValues.put(KEY_TIME_FOUR, "a");
        contentValues.put(KEY_TIME_FIVE, "a");
        contentValues.put(KEY_TIME_SIX, "a");
        contentValues.put(KEY_TIME_SEVEN, "a");

        database.insert(MONTH_EIGHT, null, contentValues);


    }

    public boolean isAlreadyExistThisDateInMonthEight(String date) {
        SQLiteDatabase database = this.getReadableDatabase();

        String getQuery = "SELECT * FROM " + MONTH_EIGHT + " WHERE " + KEY_DATE + " = '" + date + "'";

        Cursor cursor = database.rawQuery(getQuery, null);

        boolean retVal = cursor.moveToFirst();

        cursor.close();
        return retVal;
    }

    public DailyTime getMonthwithdateInMonthEight(String date) {
        DailyTime month = null;

        SQLiteDatabase database = this.getReadableDatabase();

        String getQuery = "SELECT * FROM " + MONTH_EIGHT + " WHERE " + KEY_DATE + " = '" + date + "'";


        Cursor cursor = database.rawQuery(getQuery, null);

        if (cursor.moveToFirst()) {
            month = new DailyTime(date, cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7));


        }

        cursor.close();

        return month;

    }

    public void updateMonthEight(DailyTime dailyTime) {
        SQLiteDatabase database = this.getWritableDatabase();

        String delete = "DELETE FROM " + MONTH_EIGHT + " WHERE " + KEY_DATE + " = '" + dailyTime.getDate() + "'";

        if (isAlreadyExistThisDateInMonthEight(dailyTime.getDate()))
            database.execSQL(delete);

        ContentValues contentValues = new ContentValues();

        contentValues.put(KEY_DATE, dailyTime.getDate());
        contentValues.put(KEY_TIME_ONE, dailyTime.getTimeone());
        contentValues.put(KEY_TIME_TWO, dailyTime.getTimetwo());
        contentValues.put(KEY_TIME_THREE, dailyTime.getTimethree());
        contentValues.put(KEY_TIME_FOUR, dailyTime.getTimefour());
        contentValues.put(KEY_TIME_FIVE, dailyTime.getTimefive());
        contentValues.put(KEY_TIME_SIX, dailyTime.getTimesix());
        contentValues.put(KEY_TIME_SEVEN, dailyTime.getTimeseven());

        database.insert(MONTH_EIGHT, null, contentValues);

    }

    public int getTotalAbsentTimeInMonthEight() {
        int count = 0;

        SQLiteDatabase database = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + MONTH_EIGHT;

        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {

                if (cursor.getString(1).equals("a"))
                    count++;

                if (cursor.getString(2).equals("a"))
                    count++;

                if (cursor.getString(3).equals("a"))
                    count++;

                if (cursor.getString(4).equals("a"))
                    count++;

                if (cursor.getString(5).equals("a"))
                    count++;

                if (cursor.getString(6).equals("a"))
                    count++;

                if (cursor.getString(7).equals("a"))
                    count++;


            }
            while (cursor.moveToNext());
        }

        cursor.close();

        return count;
    }

    public void deleteRecordInMonthEight(String date) {
        SQLiteDatabase database = this.getWritableDatabase();

        String deletequery = "DELETE FROM " + MONTH_EIGHT + " WHERE " + KEY_DATE + " = '" + date + "'";

        if (isAlreadyExistThisDateInMonthEight(date))
            database.execSQL(deletequery);

    }


    //MONTH EIGHT END


    //month update case

    public void updateAbsentOrPresentInMonthOneWithTime(int month, DailyTime dailyTime) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_TIME_ONE, dailyTime.getTimeone());
        values.put(KEY_TIME_TWO, dailyTime.getTimetwo());
        values.put(KEY_TIME_THREE, dailyTime.getTimethree());
        values.put(KEY_TIME_FOUR, dailyTime.getTimefour());
        values.put(KEY_TIME_FIVE, dailyTime.getTimefive());
        values.put(KEY_TIME_SIX, dailyTime.getTimesix());
        values.put(KEY_TIME_SEVEN, dailyTime.getTimeseven());

        String[] selectionArgs = {String.valueOf(dailyTime.getDate())};

        switch (month) {
            case 1:

                db.update(MONTH_ONE, values, KEY_DATE + " = ?", selectionArgs);

                break;

            case 2:

                db.update(MONTH_TWO, values, KEY_DATE + " = ?", selectionArgs);

                break;

            case 3:

                db.update(MONTH_THREE, values, KEY_DATE + " = ?", selectionArgs);

                break;

            case 4:

                db.update(MONTH_FOUR, values, KEY_DATE + " = ?", selectionArgs);

                break;

            case 5:

                db.update(MONTH_FIVE, values, KEY_DATE + " = ?", selectionArgs);

                break;

            case 6:

                db.update(MONTH_SIX, values, KEY_DATE + " = ?", selectionArgs);


                break;

            case 7:

                db.update(MONTH_SEVEN, values, KEY_DATE + " = ?", selectionArgs);

                break;

            case 8:

                db.update(MONTH_EIGHT, values, KEY_DATE + " = ?", selectionArgs);
                break;


        }


    }


    //for monthly rollcall
    public void insertMonthlyRollCall(Monthly monthly) {
        SQLiteDatabase database = this.getWritableDatabase();

        String deletequery = "DELETE FROM " + MONTHLY_RC + " WHERE " + MONTH + " = '" + monthly.getMonthname() + "'";

        if (isExistThisMonthRollcall(monthly.getMonthname())) {
            database.execSQL(deletequery);
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put(MONTH, monthly.getMonthname());
        contentValues.put(RC, monthly.getRcamount());

        database.insert(MONTHLY_RC, null, contentValues);


    }

    public boolean isExistThisMonthRollcall(String month) {
        SQLiteDatabase database = this.getReadableDatabase();
        String select = "SELECT * FROM " + MONTHLY_RC + " WHERE " + MONTH + " = '" + month + "'";

        Cursor cursor = database.rawQuery(select, null);

        boolean retVal = cursor.moveToFirst();

        cursor.close();

        return retVal;
    }


    public Monthly getRCwithmonth(String month) {
        Monthly monthly = new Monthly();

        SQLiteDatabase database = this.getReadableDatabase();

        String select = "SELECT * FROM " + MONTHLY_RC + " WHERE " + MONTH + " = '" + month + "'";

        Cursor cursor = database.rawQuery(select, null);

        if (cursor.moveToFirst()) {
            monthly.setMonthname(cursor.getString(0));
            monthly.setRcamount(Integer.parseInt(cursor.getString(1)));
        }

        cursor.close();

        return monthly;
    }

    public List<Monthly> getAllMonthly() {

        List<Monthly> monthlyList = new ArrayList<>();

        SQLiteDatabase database = this.getReadableDatabase();

        String select = "SELECT * FROM " + MONTHLY_RC + " WHERE " + MONTH;

        Cursor cursor = database.rawQuery(select, null);

        if (cursor.moveToFirst()) {
            do {

                monthlyList.add(new Monthly(cursor.getString(0), Integer.parseInt(cursor.getString(1))));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return monthlyList;
    }

    //insert data to off day

    public void insertOffDay(String date) {
        SQLiteDatabase database = this.getWritableDatabase();

        String deleteQuery = "DELETE FROM " + OFF_DAY + " WHERE " + DATE + " = '" + date + "'";
        if (isAlreadyOffDay(date)) {

            database.execSQL(deleteQuery);
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put(DATE, date);

        database.insert(OFF_DAY, null, contentValues);

    }

    //insert data to off day

    //delete date from off day

    public void deleteOffDay(String date)
    {
        SQLiteDatabase database = this.getWritableDatabase();

        String deleteQuery = "DELETE FROM " + OFF_DAY + " WHERE " + DATE + " = '" + date + "'";

        if (isAlreadyOffDay(date)) {

            database.execSQL(deleteQuery);
        }

    }

    //delete date from off day

    //checking existing date
    public boolean isAlreadyOffDay(String date) {
        SQLiteDatabase database = this.getReadableDatabase();

        String selectquery = "SELECT * FROM " + OFF_DAY + " WHERE " + DATE + " = '" + date + "'";

        Cursor cursor = database.rawQuery(selectquery, null);

        boolean retVal = cursor.moveToFirst();

        cursor.close();
        return retVal;
    }

    //checking existing date

}
