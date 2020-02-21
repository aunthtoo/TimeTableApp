package com.aunthtoo.timetableforcupyay.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aunthtoo.timetableforcupyay.MonthlyActivity;
import com.aunthtoo.timetableforcupyay.R;
import com.aunthtoo.timetableforcupyay.model.Monthly;
import com.aunthtoo.timetableforcupyay.model.TeacherAndSubject;

import java.util.List;

import me.myatminsoe.mdetect.MDetect;
import me.myatminsoe.mdetect.MMTextView;

/**
 * Created by Aunt Htoo on 12/4/2017.
 */

public class TeacherAdapter extends RecyclerView.Adapter<TeacherAdapter.MyViewHolder> {

    private List<TeacherAndSubject> teacherAndSubjectsList;
    public Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public MMTextView teachername;
        public TextView subshort, sublong;
        public LinearLayout linearLayout;


        public MyViewHolder(View view, Context _context) {
            super(view);

            context = _context;

            MDetect.INSTANCE.init(context);

            linearLayout = view.findViewById(R.id.linearinteacher);

            teachername = view.findViewById(R.id.teacherinsub);

            subshort = view.findViewById(R.id.subshort);

            sublong = view.findViewById(R.id.sublong);


            linearLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

           // Toast.makeText(context, "YOu click", Toast.LENGTH_SHORT).show();
        }
    }

    public TeacherAdapter(List<TeacherAndSubject> teacherAndSubjectList) {

        this.teacherAndSubjectsList = teacherAndSubjectList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.teacherandsubject_item, parent, false);

        return new MyViewHolder(itemView, parent.getContext());
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        TeacherAndSubject teacherAndSubject = teacherAndSubjectsList.get(position);

        holder.subshort.setText(teacherAndSubject.getShortSub());
        holder.sublong.setText(teacherAndSubject.getLongSub());
        holder.teachername.setMMText(teacherAndSubject.getTeacherName());

    }

    @Override
    public int getItemCount() {
        return teacherAndSubjectsList.size();
    }


}
