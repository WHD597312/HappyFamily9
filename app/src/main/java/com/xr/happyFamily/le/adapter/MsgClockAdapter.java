package com.xr.happyFamily.le.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xr.happyFamily.R;
import com.xr.happyFamily.bao.PingLunActivity;

import java.util.List;

//快递列表适配器

public class MsgClockAdapter extends RecyclerView.Adapter<MsgClockAdapter.MyViewHolder> implements View.OnClickListener {
    private Context context;
    private List<String> data;
    private ButtonInterface buttonInterface;

    private int defItem = -1;
    private OnItemListener onItemListener;
    private int type = 0;

    public MsgClockAdapter(Context context, List<String> list) {
        this.context = context;
        this.data = list;
    }

    public void setOnItemListener(OnItemListener onItemListener) {
        this.onItemListener = onItemListener;
    }


    public interface OnItemListener {
        void onClick(View v, int pos, String projectc);
    }

    public void setDefSelect(int position) {
        this.defItem = position;
        notifyDataSetChanged();
    }

    /**
     * 按钮点击事件需要的方法
     */
    public void buttonSetOnclick(ButtonInterface buttonInterface) {
        this.buttonInterface = buttonInterface;
    }

    /**
     * 按钮点击事件对应的接口
     */
    public interface ButtonInterface {
        public void onclick(View view, int position);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                context).inflate(R.layout.item_clock_msg_clock, parent,
                false));
        return holder;
    }




    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.tv_time.setText(data.get(position).toString());
//



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_zhuiping:
                context.startActivity(new Intent(context, PingLunActivity.class));
                break;

        }
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    /**
     * ViewHolder的类，用于缓存控件
     */
    class MyViewHolder extends RecyclerView.ViewHolder {


        TextView tv_name,tv_context,tv_time;
        RelativeLayout rl_item;

        public MyViewHolder(View view) {
            super(view);

            tv_time = (TextView) view.findViewById(R.id.tv_time);
            tv_name = (TextView) view.findViewById(R.id.tv_name);
            tv_context = (TextView) view.findViewById(R.id.tv_context);


        }


    }




}