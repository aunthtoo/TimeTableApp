package com.aunthtoo.timetableforcupyay.fragments.frag_for_rollcall;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aunthtoo.timetableforcupyay.R;
import com.aunthtoo.timetableforcupyay.dbhandler.TimeTableDBHandler;
import com.aunthtoo.timetableforcupyay.model.Monthly;
import com.aunthtoo.timetableforcupyay.prefmanager.PrefManager;
import com.github.guilhe.circularprogressview.CircularProgressView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import me.myatminsoe.mdetect.MDetect;
import me.myatminsoe.mdetect.MMTextView;

/**
 * Created by Aunt Htoo on 11/17/2017.
 */

public class FragCurrentMonth extends Fragment {

    private View view;
    private CircularProgressView circularProgressView;
    private TextView txtPercent;
    private MMTextView monthname, absenttime, remainingtime, totaltime, totalday;

    private String currentDateTimeString;

    private PrefManager prefManager;

    //for date time
    String day, month, year, hour, min, mDate, mTime;

    //for database
    private TimeTableDBHandler dbHandler;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefManager = new PrefManager(getContext());
        dbHandler = new TimeTableDBHandler(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_currentmonth, container, false);

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


        circularProgressView = view.findViewById(R.id.circularprogress);


        txtPercent = view.findViewById(R.id.percent);
        absenttime = view.findViewById(R.id.absenttimeval);
        remainingtime = view.findViewById(R.id.remainingtimeval);
        totaltime = view.findViewById(R.id.totaltimeval);
        totalday = view.findViewById(R.id.totaldayval);

        setupForTextView();

        if (getValidMonth(month) != 0) {
            setupCircularProgress(dbHandler.getRCwithmonth(month).getRcamount());
        } else {
            setupCircularProgress(0);
        }

        return view;
    }

    public void setupCircularProgress(int rc) {

        if (rc >= 0 && rc <= 76) {
            circularProgressView.setColor(getResources().getColor(R.color.red));
            circularProgressView.setProgress(rc, true, 2000);
        } else if (rc > 76 && rc <= 85) {
            circularProgressView.setColor(getResources().getColor(R.color.yellow));
            circularProgressView.setProgress(rc, true, 2000);
        } else {
            circularProgressView.setColor(getResources().getColor(R.color.colorPrimary));
            circularProgressView.setProgress(rc, true, 2000);
        }

        txtPercent.setText(rc + " %");

    }

    public void setupForTextView() {

        int absentTime = 0, toAbsentTime = 0, totalTime = 0, totalDay = 0;

        if (getValidMonth(month) != 0) {
            switch (getValidMonth(month)) {
                case 1:

                    absentTime = dbHandler.getTotalAbsentTimeInMonthOne();
                    totalTime = prefManager.getMonthonetotaldaytoattend() * 7;
                    totalDay = prefManager.getMonthonetotaldaytoattend();


                    break;


                case 2:

                    absentTime = dbHandler.getTotalAbsentTimeInMonthTwo();

                    totalTime = prefManager.getMonthtwototaldaytoattend() * 7;
                    totalDay = prefManager.getMonthtwototaldaytoattend();

                    break;

                case 3:

                    absentTime = dbHandler.getTotalAbsentTimeInMonthThree();

                    totalTime = prefManager.getMonththreetotaldaytoattend() * 7;
                    totalDay = prefManager.getMonththreetotaldaytoattend();

                    break;

                case 4:

                    absentTime = dbHandler.getTotalAbsentTimeInMonthFour();

                    totalTime = prefManager.getMonthfourtotaldaytoattend() * 7;
                    totalDay = prefManager.getMonthfourtotaldaytoattend();

                    break;

                case 5:

                    absentTime = dbHandler.getTotalAbsentTimeInMonthFive();

                    totalTime = prefManager.getMonthfivetotaldaytoattend() * 7;
                    totalDay = prefManager.getMonthfivetotaldaytoattend();

                    break;


                case 6:

                    absentTime = dbHandler.getTotalAbsentTimeInMonthSix();

                    totalTime = prefManager.getMonthsixtotaldaytoattend() * 7;

                    totalDay = prefManager.getMonthsixtotaldaytoattend();

                    break;

                case 7:

                    absentTime = dbHandler.getTotalAbsentTimeInMonthSeven();

                    totalTime = prefManager.getMonthseventotaldaytoattend() * 7;

                    totalDay = prefManager.getMonthseventotaldaytoattend();

                    break;

                case 8:

                    absentTime = dbHandler.getTotalAbsentTimeInMonthEight();

                    totalTime = prefManager.getMontheighttotaldaytoattend() * 7;
                    totalDay = prefManager.getMontheighttotaldaytoattend();

                    break;


            }


            Monthly monthly = new Monthly(month, getRcPercent(totalTime - absentTime, totalTime));

            dbHandler.insertMonthlyRollCall(monthly);

            int canAbsentTime = totalTime * 25 / 100;

            if (absentTime >= canAbsentTime) {
                toAbsentTime = 0;
            } else
                toAbsentTime = canAbsentTime - absentTime;

            absenttime.setMMText(absentTime + "");
            remainingtime.setMMText(toAbsentTime + "");
            totaltime.setMMText(totalTime + "");
            totalday.setMMText(totalDay + "");


        } else {
            absenttime.setMMText("-");
            remainingtime.setMMText("-");
            totaltime.setMMText("-");
            totalday.setMMText("-");
        }

    }

    public String getMonthNameinMMLan(String month) {
        String retMonth = null;

        if (month.length() == 3) {
            switch (month.toLowerCase()) {
                case "jan":

                    retMonth = MDetect.INSTANCE.getText(getString(R.string.jan) + "လ");

                    break;

                case "feb":

                    retMonth = MDetect.INSTANCE.getText(getString(R.string.feb) + "လ");

                    break;

                case "mar":

                    retMonth = MDetect.INSTANCE.getText(getString(R.string.mar) + "လ");

                    break;

                case "apr":

                    retMonth = MDetect.INSTANCE.getText(getString(R.string.apr) + "လ");

                    break;

                case "may":

                    retMonth = MDetect.INSTANCE.getText(getString(R.string.may) + "လ");

                    break;

                case "jun":

                    retMonth = MDetect.INSTANCE.getText(getString(R.string.jun) + "လ");

                    break;

                case "jul":

                    retMonth = MDetect.INSTANCE.getText(getString(R.string.jul) + "လ");

                    break;

                case "aug":

                    retMonth = MDetect.INSTANCE.getText(getString(R.string.aug) + "လ");

                    break;

                case "sep":

                    retMonth = MDetect.INSTANCE.getText(getString(R.string.sep) + "လ");

                    break;

                case "oct":

                    retMonth = MDetect.INSTANCE.getText(getString(R.string.oct) + "လ");

                    break;

                case "nov":

                    retMonth = MDetect.INSTANCE.getText(getString(R.string.nov) + "လ");
                    break;

                case "dec":

                    retMonth = MDetect.INSTANCE.getText(getString(R.string.dec) + "လ");
                    break;

            }
        }

        return retMonth;
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


    public int getRcPercent(int pTime, int tTime) {
        return pTime * 100 / tTime;
    }

    @Override
    public void onResume() {
        super.onResume();
        setupForTextView();

        if (getValidMonth(month) != 0) {
            setupCircularProgress(dbHandler.getRCwithmonth(month).getRcamount());
        } else {
            setupCircularProgress(0);
        }
    }
}
