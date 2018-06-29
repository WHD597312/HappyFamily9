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

import com.squareup.picasso.Picasso;
import com.xr.happyFamily.R;
import com.xr.happyFamily.bao.ShopXQActivity;
import com.xr.happyFamily.bean.OrderBean;

import java.util.List;

//快递列表适配器

public class WuLiuXQAdapter extends RecyclerView.Adapter<WuLiuXQAdapter.MyViewHolder>  {
    private Context context;
    private List<OrderBean.OrderDetailsList> data;
    private ButtonInterface buttonInterface;

    private int defItem = -1;
    private OnItemListener onItemListener;
    private int type=0;
    private String shopId;

    public WuLiuXQAdapter(Context context, List<OrderBean.OrderDetailsList> list) {
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
                context).inflate(R.layout.item_wuliuxq, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
//        holder.tv_shop_type.setText(data.get(position).getGoods().getSimpleDescribe());
        holder.tv_shop_name.setText(data.get(position).getGoodsName());
        holder.tv_shop_type.setText(data.get(position).getSimpleDescribe());

        Picasso.with(context)
                .load(data.get(position).getImage())
                .into(holder.img_shop);//此种策略并不会压缩图片
        shopId=data.get(position).getGoodsId();
    }




    @Override
    public int getItemCount() {
        return data.size();
    }

    /**
     * ViewHolder的类，用于缓存控件
     */
    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView img_shop;
        TextView tv_shop_name,tv_shop_type;


        public MyViewHolder(View view) {
            super(view);

            img_shop= (ImageView) view.findViewById(R.id.img_shop_pic);

            tv_shop_name= (TextView) view.findViewById(R.id.tv_shop_name);
            tv_shop_type= (TextView) view.findViewById(R.id.tv_shop_type);

        }


    }
}