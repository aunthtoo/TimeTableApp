package com.aunthtoo.timetableforcupyay.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.aunthtoo.timetableforcupyay.R;
import com.aunthtoo.timetableforcupyay.fragments.frag_for_rollcall.FragCurrentMonth;
import com.aunthtoo.timetableforcupyay.fragments.frag_for_rollcall.FragMonthly;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import me.myatminsoe.mdetect.MDetect;

/**
 * Created by Aunt Htoo on 11/17/2017.
 */

public class FragRollCall extends Fragment {

    private View view;
    private TabLayout tabLayout;
    private ViewPager viewPager;


    //for date time
    String currentDateTimeString;
    String day, month, year, hour, min, mDate, mTime;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MDetect.INSTANCE.init(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_rollcall, container, false);

        //for getting date time
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        currentDateTimeString = df.format(Calendar.getInstance().getTime());


        mDate = currentDateTimeString.split(" ")[0];
        mTime = currentDateTimeString.split(" ")[1];

        //for date
        year = mDate.split("-")[0];
        month = mDate.split("-")[1];
        day = mDate.split("-")[2];

        //for time
        hour = mTime.split(":")[0];
        min = mTime.split(":")[1];


        viewPager = view.findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);


        return view;
    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());


        adapter.addFragment(new FragCurrentMonth(), getMonthNameinMMLan(Integer.parseInt(month)));
        adapter.addFragment(new FragMonthly(), MDetect.INSTANCE.getText(getString(R.string.monthly)));
        viewPager.setAdapter(adapter);
    }


    //for view pager
    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


    public String getMonthNameinMMLan(int month) {
        String retMonth = null;


        switch (month) {
            case 1:

                retMonth = MDetect.INSTANCE.getText(getString(R.string.jan) + "လ");

                break;

            case 2:

                retMonth = MDetect.INSTANCE.getText(getString(R.string.feb) + "လ");

                break;

            case 3:

                retMonth = MDetect.INSTANCE.getText(getString(R.string.mar) + "လ");

                break;

            case 4:

                retMonth = MDetect.INSTANCE.getText(getString(R.string.apr) + "လ");

                break;

            case 5:

                retMonth = MDetect.INSTANCE.getText(getString(R.string.may) + "လ");

                break;

            case 6:

                retMonth = MDetect.INSTANCE.getText(getString(R.string.jun) + "လ");

                break;

            case 7:

                retMonth = MDetect.INSTANCE.getText(getString(R.string.jul) + "လ");

                break;

            case 8:

                retMonth = MDetect.INSTANCE.getText(getString(R.string.aug) + "လ");

                break;

            case 9:

                retMonth = MDetect.INSTANCE.getText(getString(R.string.sep) + "လ");

                break;

            case 10:

                retMonth = MDetect.INSTANCE.getText(getString(R.string.oct) + "လ");

                break;

            case 11:

                retMonth = MDetect.INSTANCE.getText(getString(R.string.nov) + "လ");
                break;

            case 12:

                retMonth = MDetect.INSTANCE.getText(getString(R.string.dec) + "လ");
                break;

        }


        return retMonth;
    }
}
