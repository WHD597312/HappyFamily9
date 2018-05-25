package com.xr.happyFamily.bao.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xr.happyFamily.R;
import com.xr.happyFamily.bao.ShopDingdanXQActivity;

import java.util.ArrayList;

//快递列表适配器

public class PinglunAdapter extends RecyclerView.Adapter<PinglunAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<String> list;
    private ButtonInterface buttonInterface;

    private int defItem = -1;
    private OnItemListener onItemListener;

    public PinglunAdapter(Context context, ArrayList<String> list) {
        this.context=context;
        this.list=list;
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
     *按钮点击事件需要的方法
     */
    public void buttonSetOnclick(ButtonInterface buttonInterface){
        this.buttonInterface=buttonInterface;
    }

    /**
     * 按钮点击事件对应的接口
     */
    public interface ButtonInterface{
        public void onclick(View view, int position);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                context).inflate(R.layout.item_shop_pinglun, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.tv_name.setText(list.get(position));

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    /**
     * ViewHolder的类，用于缓存控件
     */
    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView img_touxiang,img_xing;
        TextView tv_name,tv_time,tv_pinglun;


        public MyViewHolder(View view) {
            super(view);
            img_touxiang = (ImageView) view.findViewById(R.id.img_touxiang);
            img_xing = (ImageView) view.findViewById(R.id.img_xing);
            tv_name= (TextView) view.findViewById(R.id.tv_name);
            tv_time= (TextView) view.findViewById(R.id.tv_time);
            tv_pinglun= (TextView) view.findViewById(R.id.tv_pinglun);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemListener != null) {
                        onItemListener.onClick(v,getLayoutPosition(),list.get(getLayoutPosition()));
                    }
                }
            });
        }


    }
}