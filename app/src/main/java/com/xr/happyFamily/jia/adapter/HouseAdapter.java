package com.xr.happyFamily.jia.adapter;

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
import com.xr.happyFamily.bao.FuWuActivity;
import com.xr.happyFamily.bao.ShopXQActivity;
import com.xr.happyFamily.bean.OrderBean;
import com.xr.happyFamily.jia.pojo.Hourse;

import java.util.List;

//快递列表适配器

public class HouseAdapter extends RecyclerView.Adapter<HouseAdapter.MyViewHolder> /*implements View.OnClickListener*/ {
    private Context context;
    private List<Hourse> data;
    private ButtonInterface buttonInterface;

    private int defItem = -1;
    private OnItemListener onItemListener;
    private int type=0;
    private String shopId;

    public HouseAdapter(Context context, List<Hourse> list) {
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
                context).inflate(R.layout.activity_home_hourseitem, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
//        holder.tv_shop_type.setText(data.get(position).getGoods().getSimpleDescribe());
        holder.tv_name.setText(data.get(position).getHouseName());
        holder.tv_address.setText(data.get(position).getHouseAddress());
//        shopId=data.get(position).getGoodsId();
//        holder.img_tui.setOnClickListener(this);
//        holder.rl_dingdan.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent  = new Intent(v.getContext(),ShopXQActivity.class);
//                intent.putExtra("goodsId",data.get(position).getGoodsId());
////                intent.putExtra("argName",tag.argName);
////                intent.putExtra("argValue",tag.argValue);
//                v.getContext().startActivity(intent);

//            }
//        });
    }

//    @Override
//    public void onClick(View v) {
//        switch (v.getId()){
//            case R.id.img_tui:
//               context.startActivity(new Intent(context,FuWuActivity.class));
//                break;
//
//
//        }
//    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    /**
     * ViewHolder的类，用于缓存控件
     */
    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView img_change;
        TextView tv_address,tv_name;
        RelativeLayout rl_d1;
        RelativeLayout rl_d2;
        public MyViewHolder(View view) {
            super(view);



            tv_name= (TextView) view.findViewById(R.id.tv_hourse_h);
            tv_address= (TextView) view.findViewById(R.id.tv_hourse_ad);
            rl_d1= (RelativeLayout) view.findViewById(R.id.rl_house_it1);
            rl_d1= (RelativeLayout) view.findViewById(R.id.rl_house_it2);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemListener != null) {
//                        onItemListener.onClick(v,getLayoutPosition(),data.get(getLayoutPosition()).getGoodsName().toString());
                    }
                }
            });
        }


    }
}