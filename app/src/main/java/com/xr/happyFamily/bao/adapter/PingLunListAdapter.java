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
import com.xr.happyFamily.bao.PingLunActivity;
import com.xr.happyFamily.bao.ShopDingdanXQActivity;
import com.xr.happyFamily.bao.WuLiuActivity;

import java.util.ArrayList;
import java.util.Map;

import butterknife.ButterKnife;

//快递列表适配器

public class PingLunListAdapter extends RecyclerView.Adapter<PingLunListAdapter.MyViewHolder> implements View.OnClickListener {
    private Context context;
    private ArrayList<Map<String,Object>> list;
    private ButtonInterface buttonInterface;

    private int defItem = -1;
    private OnItemListener onItemListener;
    private int type=0;

    public PingLunListAdapter(Context context, ArrayList<Map<String,Object>> list) {
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
                context).inflate(R.layout.item_pingjia_wode, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.tv_shop_name.setText(list.get(position).get("name").toString());
        holder.tv_zhuiping.setOnClickListener(this);
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
        return list.size();
    }

    /**
     * ViewHolder的类，用于缓存控件
     */
    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView img_shop,img_touxiang;
        TextView tv_name,tv_time,tv_color,tv_type,tv_context,tv_shop_price,tv_shop_name,tv_shop_type,tv_shop_num,tv_zhuiping;

        public MyViewHolder(View view) {
            super(view);
            img_touxiang= (ImageView) view.findViewById(R.id.img_touxiang);
            tv_context= (TextView) view.findViewById(R.id.tv_context);
            tv_name= (TextView) view.findViewById(R.id.tv_name);
            tv_time= (TextView) view.findViewById(R.id.tv_time);
            tv_color= (TextView) view.findViewById(R.id.tv_color);
            tv_type= (TextView) view.findViewById(R.id.tv_type);
            img_shop= (ImageView) view.findViewById(R.id.img_shop_pic);
            tv_shop_name= (TextView) view.findViewById(R.id.tv_shop_name);
            tv_shop_type= (TextView) view.findViewById(R.id.tv_shop_type);
            tv_shop_price= (TextView) view.findViewById(R.id.tv_shop_price);
            tv_shop_num= (TextView) view.findViewById(R.id.tv_shop_num);
            tv_zhuiping= (TextView) view.findViewById(R.id.tv_zhuiping);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemListener != null) {
                        onItemListener.onClick(v,getLayoutPosition(),list.get(getLayoutPosition()).get("name").toString());
                    }
                }
            });
        }


    }
}