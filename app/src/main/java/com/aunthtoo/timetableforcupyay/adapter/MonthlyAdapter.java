package com.aunthtoo.timetableforcupyay.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aunthtoo.timetableforcupyay.MonthlyActivity;
import com.aunthtoo.timetableforcupyay.R;
import com.aunthtoo.timetableforcupyay.model.Monthly;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Aunt Htoo on 12/3/2017.
 */

import me.myatminsoe.mdetect.MDetect;
import me.myatminsoe.mdetect.MMTextView;

public class MonthlyAdapter extends RecyclerView.Adapter<MonthlyAdapter.MyViewHolder> {

    private List<Monthly> monthlyList;
    public Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public MMTextView monthname;
        public TextView percent;
        public LinearLayout linearLayout;


        public MyViewHolder(View view, Context _context) {
            super(view);

            context = _context;

            MDetect.INSTANCE.init(context);

            linearLayout = view.findViewById(R.id.linear);

            monthname = view.findViewById(R.id.monthname);

            percent = view.findViewById(R.id.rcpercent);


            linearLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            String month = monthlyList.get(getAdapterPosition()).getMonthname();

            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentDateTimeString = df.format(Calendar.getInstance().getTime());

            String mDate = currentDateTimeString.split(" ")[0];
            String mTime = currentDateTimeString.split(" ")[1];

            //for date
            String year = mDate.split("-")[0];
            String mm = mDate.split("-")[1];
            String day = mDate.split("-")[2];

            Intent intent = new Intent(context, MonthlyActivity.class);
            intent.putExtra("monthnum", month);
            if (month.equals("12") && Integer.parseInt(mm) < 12)
                intent.putExtra("year", (Integer.parseInt(year) - 1) + "");
            else
                intent.putExtra("year", year);

            intent.putExtra("day", day);

            context.startActivity(intent);

            //Toast.makeText(context, "SELECTED : " + month, Toast.LENGTH_SHORT).show();
        }
    }

    public MonthlyAdapter(List<Monthly> monthlyList) {

        this.monthlyList = monthlyList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.monthly_item, parent, false);

        return new MyViewHolder(itemView, parent.getContext());
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Monthly monthly = monthlyList.get(position);

        holder.monthname.setText(getMonthNameinMMLan(Integer.parseInt(monthly.getMonthname())));
        holder.percent.setText(monthly.getRcamount() + " %");

    }

    @Override
    public int getItemCount() {
        return monthlyList.size();
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

}
