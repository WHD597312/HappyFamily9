package com.xr.happyFamily.le.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.xr.happyFamily.R;
import com.xr.happyFamily.bao.PingLunActivity;
import com.xr.happyFamily.le.bean.ClickFriendBean;
import com.xr.happyFamily.together.util.Utils;

import java.util.List;
import java.util.Map;

//快递列表适配器

public class ClockAddQinglvAdapter extends RecyclerView.Adapter<ClockAddQinglvAdapter.MyViewHolder> implements View.OnClickListener {
    private Context context;
    private List<ClickFriendBean> data;
    private ButtonInterface buttonInterface;

    private int defItem = -1;
    private OnItemListener onItemListener;
    private int type = 0;

    public ClockAddQinglvAdapter(Context context, List<ClickFriendBean> list) {
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
                context).inflate(R.layout.item_clock_add_qinglv, parent,
                false));
        return holder;
    }




    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        Log.e("qqqqqqIII",data.get(position).getHeadImgUrl());
        if(!Utils.isEmpty(data.get(position).getHeadImgUrl()))
        Picasso.with(context)
                .load(data.get(position).getHeadImgUrl())
                .error(R.mipmap.ic_touxiang_moren)
                .into(holder.img_touxiang);
        holder.tv_name.setText(data.get(position).getUsername().toString());
//        holder.tv_context.setText(data.get(position).get("context").toString());
        final int[] sign = {0};
        final Drawable drawable_false = context.getResources().getDrawable(R.mipmap.ic_clock_qinglv_false);
        final Drawable drawable_true = context.getResources().getDrawable(R.mipmap.ic_clock_qinglv_true);
        drawable_false.setBounds(0, 0, drawable_false.getMinimumWidth(), drawable_false.getMinimumHeight());
        drawable_true.setBounds(0, 0, drawable_true.getMinimumWidth(), drawable_true.getMinimumHeight());



        holder.tv_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sign[0] == 1) {
                    sign[0] = 0;
                    holder.tv_name.setCompoundDrawables(null, null, drawable_false, null);
                } else {
                    sign[0] = 1;
                    holder.tv_name.setCompoundDrawables(null, null, drawable_true, null);
                }

            }
        });




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

        ImageView img_touxiang;
        TextView tv_name;
        RelativeLayout rl_item;

        public MyViewHolder(View view) {
            super(view);

            img_touxiang = (ImageView) view.findViewById(R.id.img_touxiang);
            tv_name = (TextView) view.findViewById(R.id.tv_name);


        }


    }




}