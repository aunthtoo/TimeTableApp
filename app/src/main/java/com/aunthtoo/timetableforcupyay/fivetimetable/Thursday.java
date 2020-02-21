package com.aunthtoo.timetableforcupyay.fivetimetable;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.aunthtoo.timetableforcupyay.R;
import com.aunthtoo.timetableforcupyay.dbhandler.TimeTableDBHandler;
import com.aunthtoo.timetableforcupyay.model.Interval;

import java.util.List;

import me.myatminsoe.mdetect.MMTextView;

/**
 * A simple {@link Fragment} subclass.
 */
public class Thursday extends Fragment {


    private View view;
    private TimeTableDBHandler dbHandler;

    //for subject name
    private MMTextView time1subname, time2subname, time3subname, time4subname, time5subname, time6subname, time7subname;

    //for interval
    private MMTextView[] interval;

    public Thursday() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHandler = new TimeTableDBHandler(getContext());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_week, container, false);

        //initializing subname
        time1subname = view.findViewById(R.id.subjectname1);
        time2subname = view.findViewById(R.id.subjectname2);
        time3subname = view.findViewById(R.id.subjectname3);
        time4subname = view.findViewById(R.id.subjectname4);
        time5subname = view.findViewById(R.id.subjectname5);
        time6subname = view.findViewById(R.id.subjectname6);
        time7subname = view.findViewById(R.id.subjectname7);

        //initializing interval
        interval = new MMTextView[dbHandler.getIntervalCount()];

        interval[0] = view.findViewById(R.id.interval1);
        interval[1] = view.findViewById(R.id.interval2);
        interval[2] = view.findViewById(R.id.interval3);
        interval[3] = view.findViewById(R.id.interval4);
        interval[4] = view.findViewById(R.id.interval5);
        interval[5] = view.findViewById(R.id.interval6);
        interval[6] = view.findViewById(R.id.interval7);


        //insertdata
        insertDataToTextView();

        //insertdata to interval
        insertDataToInterval();


        return view;


    }


    public void insertDataToTextView() {
        List<String> mondaylist = dbHandler.getShortSubjectNamewithday("thu");

        if (mondaylist.size() == 7) {
            time1subname.setMMText(mondaylist.get(0));
            time2subname.setMMText(mondaylist.get(1));
            time3subname.setMMText(mondaylist.get(2));
            time4subname.setMMText(mondaylist.get(3));
            time5subname.setMMText(mondaylist.get(4));
            time6subname.setMMText(mondaylist.get(5));
            time7subname.setMMText(mondaylist.get(6));
        } else
            Toast.makeText(getContext(), "Something went wrong" + " length is " + mondaylist.size(), Toast.LENGTH_SHORT).show();


    }

    public void insertDataToInterval() {
        for (int i = 0; i < dbHandler.getIntervalCount(); i++) {
            Interval intervalModel = dbHandler.getIntervalWithNumber(i + 1);
            interval[i].setMMText(intervalModel.getInterval());
        }
    }

}
