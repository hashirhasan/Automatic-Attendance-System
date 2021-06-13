package com.example.auotmaticattendance.Attendance_section.View_Attendance;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.auotmaticattendance.R;

import java.util.List;

public class view_attenAdapter extends RecyclerView.Adapter<view_attenViewHolders> {
      private List<viewobject> viewobjectList;
      private Context context;


      public view_attenAdapter(List<viewobject> viewobjectList,Context context)
      {
          this.viewobjectList=viewobjectList;
          this.context=context;
      }
    @NonNull
    @Override
    public view_attenViewHolders onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View layoutView= LayoutInflater.from(parent.getContext()).inflate(R.layout.view_atten_item,null,false);
        RecyclerView.LayoutParams lp =new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp);
        view_attenViewHolders rcv=new view_attenViewHolders((layoutView));
        return rcv;

    }

    @Override
    public void onBindViewHolder(@NonNull view_attenViewHolders holder, int position) {
      holder.mname.setText(viewobjectList.get(position).getName());
      holder.mrollno.setText(viewobjectList.get(position).getRollno());
      holder.mattend.setText(viewobjectList.get(position).getAttended());
      holder.mtotalclass.setText(viewobjectList.get(position).getTotal_classes());
    }

    @Override
    public int getItemCount() {
        return viewobjectList.size();
    }
}
