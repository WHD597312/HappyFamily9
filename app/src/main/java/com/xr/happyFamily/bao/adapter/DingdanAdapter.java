package com.xr.happyFamily.bao.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xr.happyFamily.R;
import com.xr.happyFamily.bao.ShopDingdanActivity;
import com.xr.happyFamily.bao.ShopDingdanXQActivity;

import java.util.ArrayList;

//快递列表适配器

public class DingdanAdapter extends RecyclerView.Adapter<DingdanAdapter.MyViewHolder> implements View.OnClickListener {
    private Context context;
    private ArrayList<String> list;
    private ButtonInterface buttonInterface;

    private int defItem = -1;
    private OnItemListener onItemListener;

    public DingdanAdapter(Context context, ArrayList<String> list) {
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
                context).inflate(R.layout.item_dingdan, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.tv_shop_name.setText(list.get(position));

        holder.img_del.setOnClickListener(this);
        holder.img_chakan.setOnClickListener(this);
        holder.img_queren.setOnClickListener(this);
        holder.rl_dd.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_chakan:
                Toast.makeText(context,"查看",Toast.LENGTH_SHORT).show();
                break;
            case R.id.img_del:
                Toast.makeText(context,"删除",Toast.LENGTH_SHORT).show();
                break;
            case R.id.img_queren:
                Toast.makeText(context,"确认",Toast.LENGTH_SHORT).show();
                break;
            case R.id.rl_dingdan:
                MyViewHolder tag = (MyViewHolder)v.getTag();
                Intent intent  = new Intent(v.getContext(),ShopDingdanXQActivity.class);
//                intent.putExtra("argCon",tag.argCon);
//                intent.putExtra("argName",tag.argName);
//                intent.putExtra("argValue",tag.argValue);
                v.getContext().startActivity(intent);
                break;
        }
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    /**
     * ViewHolder的类，用于缓存控件
     */
    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView img_del,img_chakan,img_queren;
        TextView tv_shop_price,tv_shop_name,tv_shop_type,tv_shop_num;
        RelativeLayout rl_dd;

        public MyViewHolder(View view) {
            super(view);
            img_del = (ImageView) view.findViewById(R.id.img_del);
            img_chakan = (ImageView) view.findViewById(R.id.img_chakan);
            img_queren = (ImageView) view.findViewById(R.id.img_queren);
            tv_shop_name= (TextView) view.findViewById(R.id.tv_shop_name);
            tv_shop_type= (TextView) view.findViewById(R.id.tv_shop_type);
            tv_shop_price= (TextView) view.findViewById(R.id.tv_shop_price);
            tv_shop_num= (TextView) view.findViewById(R.id.tv_shop_num);
            rl_dd= (RelativeLayout) view.findViewById(R.id.rl_dingdan);
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