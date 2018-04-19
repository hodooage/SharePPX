package com.example.z1310_000.sharedppx.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.z1310_000.sharedppx.R;
import com.example.z1310_000.sharedppx.entity.UseRecord;

import java.util.List;

/**
 * Created by Administrator on 2018/4/19 0019.
 */

public class UseRecordAdapter  extends RecyclerView.Adapter<UseRecordAdapter.ViewHolder>{
    private List<UseRecord> mUseRecordList;



    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView startTime,xiaId,duration,totalMoney;
        public ViewHolder(View itemView) {
            super(itemView);
            startTime=itemView.findViewById(R.id.startTime);
            xiaId=itemView.findViewById(R.id.xiaId);
            duration=itemView.findViewById(R.id.duration);
            totalMoney=itemView.findViewById(R.id.totalMoney);
        }
    }

    public UseRecordAdapter(List<UseRecord> mUseRecordList) {
        this.mUseRecordList = mUseRecordList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.use_record_item,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        UseRecord useRecord=mUseRecordList.get(position);
        holder.startTime.setText(useRecord.getStarttime());
        holder.duration.setText(useRecord.getDuration());
        holder.totalMoney.setText(String.valueOf(useRecord.getTotalmoney()));
        holder.xiaId.setText(String.valueOf(useRecord.getxId()));
    }

    @Override
    public int getItemCount() {
        return mUseRecordList.size();
    }


}
