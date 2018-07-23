package com.xr.happyFamily.le.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.xr.happyFamily.R;
import com.xr.happyFamily.bao.PingLunActivity;
import com.xr.happyFamily.bean.ShopCartBean;
import com.xr.happyFamily.le.bean.HappyBannerBean;

import java.util.List;

//快递列表适配器

public class HappyFootAdapter extends RecyclerView.Adapter<HappyFootAdapter.MyViewHolder> implements View.OnClickListener {
    private Context context;
    private List<HappyBannerBean> data;
    private ButtonInterface buttonInterface;

    private int defItem = -1;
    private OnItemListener onItemListener;
    private int type=0;

    public HappyFootAdapter(Context context, List<HappyBannerBean> list) {
        this.context=context;
        this.data=list;
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
                context).inflate(R.layout.item_happy_foot, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        Picasso.with(context)
                .load(data.get(position).getPicUrl())
                .into(holder.img_footBanner);//此种策略并不会压缩图片
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
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

        ImageView img_footBanner;

        public MyViewHolder(View view) {
            super(view);
            img_footBanner= (ImageView) view.findViewById(R.id.img_footBanner);
        }


    }
}