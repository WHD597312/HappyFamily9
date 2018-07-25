package com.xr.happyFamily.jia.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xr.database.dao.daoimpl.HourseDaoImpl;
import com.xr.happyFamily.R;
import com.xr.happyFamily.bao.ShopXQActivity;
import com.xr.happyFamily.bao.adapter.ShopCartAdapter;
import com.xr.happyFamily.jia.RenameHourseActivity;
import com.xr.happyFamily.jia.pojo.Hourse;
import com.xr.happyFamily.main.MainActivity;

import java.util.ArrayList;
import java.util.List;

//快递列表适配器

public class ChooseHouseAdapter extends RecyclerView.Adapter<ChooseHouseAdapter.MyViewHolder> /*implements View.OnClickListener*/ {
    private Context context;
    private List<Hourse> data;
    private ButtonInterface buttonInterface;

    private int defItem = -1;
    private OnItemListener onItemListener;
    //    private AdapterView.OnItemClickListener mOnItemClickListener;
    private int type = 0;
    private String shopId;
    private int clicked;
    private SharedPreferences mPositionPreferences;

    public ChooseHouseAdapter(Context context, List<Hourse> list) {
        this.context = context;
        this.data = list;
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
                context).inflate(R.layout.activity_hourse_chhoose_item, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
//        holder.tv_shop_type.setText(data.get(position).getGoods().getSimpleDescribe());
        holder.tv_name.setText(data.get(position).getHouseName());
        holder.tv_address.setText(data.get(position).getHouseAddress());
        if (clicked == -1) {
            holder.img_change.setVisibility(View.GONE);
        } else if (clicked == 1) {
            holder.img_change.setVisibility(View.VISIBLE);
        }
        if (position == data.size()) {
            holder.view1.setVisibility(View.GONE);
        }

        holder.rl_d1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sign == 1) {
                    Log.i("dddddddd", "???????????");

                    HourseDaoImpl hourseDao = new HourseDaoImpl(context);
                    List<Hourse> houses = hourseDao.findAllHouse();
                    Hourse house = houses.get(position);
                    String houseName = house.getHouseName();
                    String houseAddress = house.getHouseAddress();
                    long houseId = house.getHouseId();
                    Intent intent = new Intent(context, RenameHourseActivity.class);
                    intent.putExtra("houseName", houseName);
                    intent.putExtra("houseAddress", houseAddress);
                    intent.putExtra("houseId", houseId);
                    context.startActivity(intent);
                } else if (sign == 2) {
                    mPositionPreferences = context.getSharedPreferences("position", Context.MODE_PRIVATE);
                    mPositionPreferences.edit().clear().commit();
                    HourseDaoImpl hourseDao = new HourseDaoImpl(context);
                    List<Hourse> houses = hourseDao.findAllHouse();
                    Hourse house = houses.get(position);
                    String houseName = house.getHouseName();
                    String houseAddress = house.getHouseAddress();
                    long houseId = house.getHouseId();
                    Intent intent = new Intent(context, MainActivity.class);
                    intent.putExtra("houseName", houseName);
                    intent.putExtra("houseAddress", houseAddress);
                    intent.putExtra("houseId", houseId);
                    context.startActivity(intent);
                }
            }

        });
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
        View view1;
        TextView tv_address, tv_name;
        RelativeLayout rl_d1;
        RelativeLayout rl_d2;

        public MyViewHolder(View view) {
            super(view);


            tv_name = (TextView) view.findViewById(R.id.tv_hourse_choosen);
            tv_address = (TextView) view.findViewById(R.id.tv_hourse_choosep);
            img_change = (ImageView) view.findViewById(R.id.iv_hourse_c);
            view1 = view.findViewById(R.id.house_view);
            rl_d1 = (RelativeLayout) view.findViewById(R.id.rl_house_it1);
//            rl_d1= (RelativeLayout) view.findViewById(R.id.rl_house_it2);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    if (onItemListener != null) {
//                        if(sign==1)
//                        Log.i("dddddddd","???????????");
//                        else{
//                        HourseDaoImpl  hourseDao=new HourseDaoImpl(getApplicationContext());
//                        List<Hourse> houses = hourseDao.findAllHouse();
//                        Hourse house= houses.get();
//                        String houseName = house.getHouseName();
//                        String houseAddress = house.getHouseAddress();
//                        long houseId = house.getHouseId();
//                        Intent intent = new  Intent(context,RenameHourseActivity.class);
//                        intent.putExtra("houseName",houseName);
//                        intent.putExtra("houseAddress",houseAddress);
//                        startActivity(intent);
//                        }
//                        onItemListener.onClick(v,getLayoutPosition(),data.get(getLayoutPosition()).getHouseName().toString());
//                    }
                }
            });
        }
    }

    public void setClicked(int clicked) {
        this.clicked = clicked;
    }

    int sign = 1;

    public void setSign(int sign) {
        this.sign = sign;
    }
}