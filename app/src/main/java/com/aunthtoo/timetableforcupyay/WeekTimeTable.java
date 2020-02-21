package com.aunthtoo.timetableforcupyay;

import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aunthtoo.timetableforcupyay.adapter.TeacherAdapter;
import com.aunthtoo.timetableforcupyay.dbhandler.TimeTableDBHandler;
import com.aunthtoo.timetableforcupyay.fivetimetable.Friday;
import com.aunthtoo.timetableforcupyay.fivetimetable.Monday;
import com.aunthtoo.timetableforcupyay.fivetimetable.Thursday;
import com.aunthtoo.timetableforcupyay.fivetimetable.Tuesday;
import com.aunthtoo.timetableforcupyay.fivetimetable.Wednesday;
import com.aunthtoo.timetableforcupyay.viewpageradapter.ViewPagerAdapter;

import me.myatminsoe.mdetect.MDetect;
import me.myatminsoe.mdetect.MMButtonView;
import me.myatminsoe.mdetect.MMTextView;

public class WeekTimeTable extends AppCompatActivity {

    private Toolbar toolbar;

    private LinearLayout dotsLayout;
    private TextView[] dots;

    private ViewPager viewPager;

    private MMTextView dayname;
    private Button btnLeft, btnRight;

    private ViewPagerAdapter adapter;

    private MMButtonView btnTeacherlist;

    private int current = 0;

    private TeacherAdapter teacherAdapter;
    private TimeTableDBHandler dbHandler;
    private AlertDialog teacherDialog;
    private View dialogView;

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MDetect.INSTANCE.init(this);

        setContentView(R.layout.activity_week_time_table);

        dbHandler = new TimeTableDBHandler(this);


        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(MDetect.INSTANCE.getText(getString(R.string.weektimetable)));

        dotsLayout = findViewById(R.id.layoutDots);

        //verificatin initialization

        viewPager = findViewById(R.id.view_pager);
        setupViewPager(viewPager);

        dayname = findViewById(R.id.dayname);

        dayname.setMMText(getString(R.string.mon));

        btnLeft = findViewById(R.id.leftarrow);
        btnLeft.setEnabled(false);
        btnLeft.setVisibility(View.INVISIBLE);

        btnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (current > 0) {
                    viewPager.setCurrentItem(current - 1);
                }
            }
        });

        btnRight = findViewById(R.id.rightarrow);

        btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (current < 4) {
                    viewPager.setCurrentItem(current + 1);
                }

            }
        });


        btnTeacherlist = findViewById(R.id.teacherlist);
        btnTeacherlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showTeacherDialog();

                // Toast.makeText(WeekTimeTable.this, "You click", Toast.LENGTH_LONG).show();
            }
        });


        //initialize the incicator
        addBottomDots(0);


    }


    private void addBottomDots(int currentPage) {
        dots = new TextView[5];

        int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
        int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorsInactive[currentPage]);
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(colorsActive[currentPage]);
    }


    //set up view pager

    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(new Monday(), "");
        adapter.addFragment(new Tuesday(), "");
        adapter.addFragment(new Wednesday(), "");
        adapter.addFragment(new Thursday(), "");
        adapter.addFragment(new Friday(), "");


        viewPager.setAdapter(adapter);


        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {


            }

            @Override
            public void onPageSelected(int position) {
                current = position;
                addBottomDots(position);
                dayname.setMMText(getDayNameWithPos(position));

                switch (position) {
                    case 0:
                        btnLeft.setEnabled(false);
                        btnRight.setEnabled(true);

                        btnLeft.setVisibility(View.INVISIBLE);
                        btnRight.setVisibility(View.VISIBLE);

                        break;

                    case 4:

                        btnLeft.setEnabled(true);
                        btnRight.setEnabled(false);

                        btnLeft.setVisibility(View.VISIBLE);
                        btnRight.setVisibility(View.INVISIBLE);

                        break;

                    default:

                        btnLeft.setEnabled(true);
                        btnRight.setEnabled(true);

                        btnLeft.setVisibility(View.VISIBLE);
                        btnRight.setVisibility(View.VISIBLE);

                        break;

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    public String getDayNameWithPos(int pos) {
        String name = null;

        switch (pos) {
            case 0:

                name = getString(R.string.mon);

                break;

            case 1:

                name = getString(R.string.tue);

                break;

            case 2:

                name = getString(R.string.wed);

                break;

            case 3:

                name = getString(R.string.thu);

                break;

            case 4:

                name = getString(R.string.fri);

                break;


        }

        return name;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Toast.makeText(this, "you click", Toast.LENGTH_SHORT).show();
                finish();

                break;
        }

        return true;
    }

    public void showTeacherDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        teacherDialog = builder.create();

        LayoutInflater inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.teacherandsub_dialog, null, false);

        teacherDialog.setView(dialogView);
        teacherDialog.setCancelable(true);

        teacherDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        teacherDialog.getWindow()
                .getAttributes().windowAnimations = R.style.DialogAnimation;

        recyclerView = dialogView.findViewById(R.id.recyclerindial);

        if (dbHandler.getAllTeacherAndSub().size() != 0)
            teacherAdapter = new TeacherAdapter(dbHandler.getAllTeacherAndSub());

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(teacherAdapter);

        teacherDialog.show();

    }
}
