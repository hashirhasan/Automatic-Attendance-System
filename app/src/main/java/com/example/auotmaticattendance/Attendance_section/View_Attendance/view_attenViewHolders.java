package com.example.auotmaticattendance.Attendance_section.View_Attendance;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.auotmaticattendance.R;

public class view_attenViewHolders  extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView mname,mrollno,mattend,mtotalclass;

    public view_attenViewHolders(@NonNull View itemView) {
        super(itemView);

        mname=itemView.findViewById(R.id.name);
        mrollno=itemView.findViewById(R.id.rollno);
        mattend=itemView.findViewById(R.id.attendno);
        mtotalclass=itemView.findViewById(R.id.totalclassno);
    }


    @Override
    public void onClick(View v) {

    }
}
