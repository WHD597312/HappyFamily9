package com.xr.happyFamily.le.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xr.happyFamily.R;
import com.xr.happyFamily.bao.PingLunActivity;
import com.xr.happyFamily.le.pojo.MsgData;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

//快递列表适配器

public class MsgClockAdapter extends RecyclerView.Adapter<MsgClockAdapter.MyViewHolder> implements View.OnClickListener {
    private Context context;
    private List<MsgData> data;
    private ButtonInterface buttonInterface;

    private int defItem = -1;
    private OnItemListener onItemListener;
    private int type = 0;

    public MsgClockAdapter(Context context, List<MsgData> list) {
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

        Log.e("qqqqqLLLLaaa", data.get(position).getState() + "???");

        long lcc = data.get(position).getCreateTime();
        SimpleDateFormat sdr = new SimpleDateFormat("HH时mm分");
        String times = sdr.format(new Date(lcc * 1000L));
        SimpleDateFormat sdr1 = new SimpleDateFormat("yyyy年MM月dd日");
        String times1 = sdr1.format(new Date(lcc * 1000L));
        Date date = new Date();
        long l = 24 * 60 * 60 * 1000; //每天的毫秒数
        //date.getTime()是现在的毫秒数，它 减去 当天零点到现在的毫秒数（ 现在的毫秒数%一天总的毫秒数，取余。），理论上等于零点的毫秒数，不过这个毫秒数是UTC+0时区的。
        //减8个小时的毫秒值是为了解决时区的问题。
        long today = date.getTime() - (date.getTime() % l) - 8 * 60 * 60 * 1000;

        Log.e("qqqqqqqqqqqTTTT", lcc * 1000 + "," + today);
        if (lcc * 1000 > today) {
            holder.tv_time.setVisibility(View.VISIBLE);
            holder.tv_time.setText(times);
            if (position > 0) {
                Log.e("qqqqqqTTTTMMMM", lcc + "," + data.get(position - 1).getCreateTime());
                if ((data.get(position - 1).getCreateTime() - lcc) < 5 * 60)
                    holder.tv_time.setVisibility(View.GONE);
                else
                    holder.tv_time.setVisibility(View.VISIBLE);
            }
        } else {

            holder.tv_time.setVisibility(View.VISIBLE);
            holder.tv_time.setText(times1);
            if (position > 0) {
                if (times1.equals(sdr1.format(new Date(data.get(position - 1).getCreateTime() * 1000L))))
                    holder.tv_time.setVisibility(View.GONE);
            }

        }


        holder.tv_name.setText(data.get(position).getUserName());
        String cont = null;
        switch (data.get(position).getState()) {
            case 1:
            case 4:
                cont = "添加了情侣闹钟";
                break;
            case 2:
                cont = "修改了情侣闹钟";
                break;
            case 3:
                cont = "删除了情侣闹钟";
                break;
            case 11:
            case 14:
                cont = "添加了群组闹钟";
                break;
            case 12:
                cont = "修改了群组闹钟";
                break;
            case 13:
                cont = "删除了群组闹钟";
                break;
            case 5:
                cont = "同意了您的好友请求";
                break;
            case 6:
                cont = "拒绝了您的好友请求";
                break;
            default:
                holder.ll_data.setVisibility(View.GONE);
                break;

        }
        holder.tv_context.setText(cont);
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


        TextView tv_name, tv_context, tv_time;
        LinearLayout ll_data;

        public MyViewHolder(View view) {
            super(view);

            tv_time = (TextView) view.findViewById(R.id.tv_time);
            tv_name = (TextView) view.findViewById(R.id.tv_name);
            tv_context = (TextView) view.findViewById(R.id.tv_context);
            ll_data = (LinearLayout) view.findViewById(R.id.ll_data);


        }


    }


}