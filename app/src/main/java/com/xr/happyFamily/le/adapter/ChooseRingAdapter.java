package com.xr.happyFamily.le.adapter;

import android.app.Activity;
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

import com.xr.database.dao.daoimpl.TimeDaoImpl;
import com.xr.happyFamily.R;
import com.xr.happyFamily.jia.view_custom.DeleteDeviceDialog;
import com.xr.happyFamily.le.BtClock.bjTimeActivity;
import com.xr.happyFamily.le.pojo.Time;

import java.util.ArrayList;
import java.util.List;

//快递列表适配器

public class ChooseRingAdapter extends RecyclerView.Adapter<ChooseRingAdapter.MyViewHolder> /*implements View.OnClickListener*/ {
    private Context context;
    private List data;
    private ButtonInterface buttonInterface;

    private int defItem = -1;
    private OnItemListener onItemListener;
    //    private AdapterView.OnItemClickListener mOnItemClickListener;
    private int type = 0;
    private String shopId;
    private int clicked;
    private TimeDaoImpl timeDao;

    private int lastPos=0;

    public ChooseRingAdapter(Context context, List list) {
        this.context = context;
        this.data = list;
        timeDao = new TimeDaoImpl(context);
    }

    public void setOnItemListener(OnItemListener onItemListener) {
        this.onItemListener = onItemListener;
    }
//    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener ){
//        this. mOnItemClickListener=onItemClickListener;
//    }

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

        public void onLongClick(int position);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                context).inflate(R.layout.fragment_le_clockring_item, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
//        holder.tv_shop_type.setText(data.get(position).getGoods().getSimpleDescribe());
        final String name = data.get(position).toString();
        holder.tv_name.setText(name);

        if(position==lastPos){
            holder.img_kg.setImageResource(R.mipmap.lrclock_dh);
            holder.img_kg.setTag("open");
        }else {
            holder.img_kg.setImageResource(0);
            holder.img_kg.setTag("close");
        }
//        tv_time = (TextView) view.findViewById(R.id.tv_clock_time);
//        tv_day1= (TextView) view.findViewById(R.id.tv_clock_week1);
//        img_kg = (ImageView) view.findViewById(R.id.iv_clock_kg);
//        img_kg.setTag("open");
//        rl_d1 = (RelativeLayout) view.findViewById(R.id.rl_le_commonitem);
//            rl_d1= (RelativeLayout) view.findViewById(R.id.rl_house_it2);
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                        Intent intent = new  Intent(context,RenameHourseActivity.class);
//                        intent.putExtra("houseName",houseName);
//                        intent.putExtra("houseAddress",houseAddress);
//                        startActivity(intent);
//
//                }
//            });

        holder.rl_d1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                setLastPos(position);
                if ("open".equals(holder.img_kg.getTag())) {
                    holder.img_kg.setImageResource(0);
                    holder.img_kg.setTag("close");

                } else if ("close".equals(holder.img_kg.getTag())) {
                    holder.img_kg.setImageResource(R.mipmap.lrclock_dh);
                    holder.img_kg.setTag("open");
                }

            }
        });




    }


    public void setLastPos(int pos){
        this.lastPos=pos;
        notifyDataSetChanged();
    }
    public int getLastPos(){

        return lastPos;

    }
//    @OnClick({R.id.iv_clock_kg})
//    public void onClick(View v) {
//        switch (v.getId()){
//            case R.id.iv_clock_kg:
//
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

        ImageView img_kg;

        TextView tv_name;
        RelativeLayout rl_d1;


        public MyViewHolder(View view) {
            super(view);


            tv_name = (TextView) view.findViewById(R.id.tv_clockring_name);
            img_kg = (ImageView) view.findViewById(R.id.iv_clockring_kg);
            img_kg.setTag("close");
            rl_d1 = (RelativeLayout) view.findViewById(R.id.rl_le_clockring);
//            rl_d1= (RelativeLayout) view.findViewById(R.id.rl_house_it2);
////            itemView.setOnClickListener(new View.OnClickListener() {
////                @Override
////                public void onClick(View v) {
////
////                        Intent intent = new  Intent(context,RenameHourseActivity.class);
////                        intent.putExtra("houseName",houseName);
////                        intent.putExtra("houseAddress",houseAddress);
////                        startActivity(intent);
////
////                }
////            });
//            img_kg.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if ("open".equals(img_kg.getTag())) {
//                        img_kg.setImageResource(R.mipmap.bt_kgg);
//                        img_kg.setTag("close");
//                    }else if ("close".equals(img_kg.getTag())){
//                        img_kg.setImageResource(R.mipmap.bt_kg);
//                        img_kg.setTag("open");
//                    }
//                }
//            });
        }
    }


}