package com.aunthtoo.timetableforcupyay.fragments.frag_for_rollcall;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.aunthtoo.timetableforcupyay.R;
import com.aunthtoo.timetableforcupyay.adapter.MonthlyAdapter;
import com.aunthtoo.timetableforcupyay.dbhandler.TimeTableDBHandler;

/**
 * Created by Aunt Htoo on 11/17/2017.
 */

public class FragMonthly extends Fragment {

    private View view;

    private RecyclerView monthlyRecycler;
    private RelativeLayout nodatacheck;

    private TimeTableDBHandler dbHandler;

    private MonthlyAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHandler = new TimeTableDBHandler(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_monthly, container, false);

        //initialize
        nodatacheck = view.findViewById(R.id.nodatacheck);

        monthlyRecycler = view.findViewById(R.id.monthlyrecycler);

        //for relative

        if (dbHandler.getAllMonthly().size() != 0) {

            adapter = new MonthlyAdapter(dbHandler.getAllMonthly());

            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
            monthlyRecycler.setLayoutManager(mLayoutManager);
            monthlyRecycler.setItemAnimator(new DefaultItemAnimator());
            monthlyRecycler.setAdapter(adapter);


            setNodatacheck(false);
        } else {
            setNodatacheck(true);
        }


        return view;
    }

    public void setNodatacheck(boolean val) {
        if (val == true)
            nodatacheck.setVisibility(View.VISIBLE);
        else
            nodatacheck.setVisibility(View.INVISIBLE);

    }

    @Override
    public void onResume() {
        super.onResume();

        if (dbHandler.getAllMonthly().size() != 0) {

            adapter = new MonthlyAdapter(dbHandler.getAllMonthly());

            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
            monthlyRecycler.setLayoutManager(mLayoutManager);
            monthlyRecycler.setItemAnimator(new DefaultItemAnimator());
            monthlyRecycler.setAdapter(adapter);


            setNodatacheck(false);
        } else {
            setNodatacheck(true);
        }
    }
}
