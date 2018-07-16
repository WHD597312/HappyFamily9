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
import com.xr.happyFamily.le.pojo.UserInfo;
import com.xr.happyFamily.together.util.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//快递列表适配器

public class ClockAddQinglvAdapter extends RecyclerView.Adapter<ClockAddQinglvAdapter.MyViewHolder> implements View.OnClickListener {
    private Context context;
    private List<ClickFriendBean> data;
    private ButtonInterface buttonInterface;
    private int selPosition = -1;
    private int defItem = -1;
    private OnItemListener onItemListener;
    private int type = 0;
    boolean isFirst = true;

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


    public void setSelection(int position) {
        this.selPosition = position;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        if (!Utils.isEmpty(data.get(position).getHeadImgUrl()))
            Picasso.with(context)
                    .load(data.get(position).getHeadImgUrl())
                    .error(R.mipmap.ic_touxiang_moren)
                    .into(holder.img_touxiang);
        holder.tv_name.setText(data.get(position).getUsername().toString());


//        holder.tv_context.setText(data.get(position).get("context").toString());
        final int[] sign = {0};


        final Drawable drawable_false = context.getResources().getDrawable(R.mipmap.ic_clock_qinglv_false);
        final Drawable drawable_true = context.getResources().getDrawable(R.mipmap.ic_clock_qinglv_true);
        drawable_true.setBounds(0, 0, drawable_true.getMinimumWidth(), drawable_true.getMinimumHeight());
        drawable_false.setBounds(0, 0, drawable_false.getMinimumWidth(), drawable_false.getMinimumHeight());
        if (selPosition == position) {
            holder.tv_name.setCompoundDrawables(null, null, drawable_true, null);
            data.get(position).setMemSign(1);
        } else {
            holder.tv_name.setCompoundDrawables(null, null, drawable_false, null);
            data.get(position).setMemSign(0);
        }

        if (isFirst) {
            Log.e("qqqqqqqqqIIIII222",loveId+"????");
            if (data.get(position).getUserId() ==loveId) {
                isFirst = false;
                sign[0] = 1;
                this.selPosition = position;
                holder.tv_name.setCompoundDrawables(null, null, drawable_true, null);
                data.get(position).setMemSign(1);
            }
        }


        holder.tv_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelection(position);
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


    public String getMember() {
        String member = "0";
        if (selPosition != -1) {
            member = data.get(selPosition).getUserId() + "";
        }
        return member;
    }


    public int getItem() {
        return selPosition;
    }

    private int loveId=-1;

    public void setUserId(int loveId) {
        this.loveId = loveId;
        notifyDataSetChanged();
    }


}