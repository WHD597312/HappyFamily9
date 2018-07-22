package com.xr.happyFamily.le.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.xr.happyFamily.R;
import com.xr.happyFamily.bao.PingLunActivity;
import com.xr.happyFamily.bao.adapter.DingdanAdapter;
import com.xr.happyFamily.le.bean.ClickFriendBean;
import com.xr.happyFamily.le.clock.FriendAddActivity;
import com.xr.happyFamily.together.util.GlideCircleTransform;
import com.xr.happyFamily.together.util.Utils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//快递列表适配器

public class FriendFindAdapter extends RecyclerView.Adapter<FriendFindAdapter.MyViewHolder> implements View.OnClickListener {
    private Context context;
    private List<ClickFriendBean> data;
    private ButtonInterface buttonInterface;

    private int defItem = -1;
    private OnItemListener onItemListener;
    private int type = 0;

    public FriendFindAdapter(Context context, List<ClickFriendBean> list) {
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
                context).inflate(R.layout.item_friend_add, parent,
                false));
        return holder;
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        if (!Utils.isEmpty(data.get(position).getHeadImgUrl()))
            Glide.with(context).load(data.get(position).getHeadImgUrl()).transform(new GlideCircleTransform(context.getApplicationContext())).error(R.mipmap.ic_touxiang_moren).into(holder.img_touxiang);
        else{
            holder.img_touxiang.setImageResource(R.mipmap.ic_touxiang_moren);
        }
        holder.tv_name.setText(data.get(position).getUsername());
        holder.tv_tel.setText(data.get(position).getPhone());
        if (data.get(position).getSex())
            holder.tv_sex.setText("男");
        else
            holder.tv_sex.setText("女");
        holder.tv_age.setText(data.get(position).getAge()+"岁");
        holder.rl_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, FriendAddActivity.class);
                intent.putExtra("friendData", (Serializable) data.get(position));
                context.startActivity(intent);
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
        TextView tv_name, tv_tel, tv_sex, tv_age;
        RelativeLayout rl_item;


        public MyViewHolder(View view) {
            super(view);

            img_touxiang = (ImageView) view.findViewById(R.id.img_touxiang);
            tv_name = (TextView) view.findViewById(R.id.tv_name);
            tv_tel = (TextView) view.findViewById(R.id.tv_tel);
            tv_sex = (TextView) view.findViewById(R.id.tv_sex);
            tv_age = (TextView) view.findViewById(R.id.tv_age);
            rl_item = (RelativeLayout) view.findViewById(R.id.rl_item);


        }


    }



}