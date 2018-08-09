package com.xr.happyFamily.le.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.xr.happyFamily.R;
import com.xr.happyFamily.bao.PingLunActivity;
import com.xr.happyFamily.le.bean.ClickFriendBean;
import com.xr.happyFamily.le.pojo.UserInfo;
import com.xr.happyFamily.together.util.GlideCircleTransform;
import com.xr.happyFamily.together.util.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//快递列表适配器

public class ClockAddQunzuAdapter extends RecyclerView.Adapter<ClockAddQunzuAdapter.MyViewHolder> implements View.OnClickListener {
    private Context context;
    private List<ClickFriendBean> data;
    private ButtonInterface buttonInterface;

    private int defItem = -1;
    private OnItemListener onItemListener;
    private int type = 0;
    boolean isFirst = true;

    public ClockAddQunzuAdapter(Context context, List<ClickFriendBean> list) {
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
                context).inflate(R.layout.item_clock_add_qunzu, parent,
                false));
        return holder;
    }




    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        if(Utils.isEmpty(data.get(position).getHeadImgUrl()))
            holder.img_touxiang.setImageResource(R.mipmap.ic_touxiang_moren);
        else
            Glide.with(context).load(data.get(position).getHeadImgUrl()).transform(new GlideCircleTransform(context.getApplicationContext())).error(R.mipmap.ic_touxiang_moren).into(holder.img_touxiang);
        holder.tv_name.setText(data.get(position).getUsername());
//        holder.tv_context.setText(data.get(position).get("context").toString());
        final int[] sign = {0};

        holder.img_sign.setImageResource(R.mipmap.ic_clock_qunzu_false);
        if(users!=null){
            for(int j=0;j<users.length;j++)
                if ((data.get(position).getUserId()+"").equals(users[j])) {
                    sign[0]=1;
                    Log.e("qqqqqqqVVV",position+"???");
                    holder.img_sign.setImageResource(R.mipmap.ic_clock_qunzu_true);
                    data.get(position).setMemSign(1);
                }
        }

        holder.img_touxiang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sign[0] == 1) {
                    sign[0] = 0;
                    holder.img_sign.setImageResource(R.mipmap.ic_clock_qunzu_false);
                    data.get(position).setMemSign(0);
                } else {
                    sign[0] = 1; Log.e("qqqqqqqVVV22222",position+"???");
                    holder.img_sign.setImageResource(R.mipmap.ic_clock_qunzu_true);
                    data.get(position).setMemSign(1);
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

        ImageView img_touxiang,img_sign;
        TextView tv_name;


        public MyViewHolder(View view) {
            super(view);

            img_touxiang = (ImageView) view.findViewById(R.id.img_touxiang);
            img_sign = (ImageView) view.findViewById(R.id.img_sign);
            tv_name = (TextView) view.findViewById(R.id.tv_name);


        }


    }


    public String getMember(){
        String str="";
        for(int i=0;i<data.size();i++){
            if (data.get(i).getMemSign()==1)
            str=str+data.get(i).getUserId()+",";
        }
        if(str.length()<1){
            str="00";
        }
        return str.substring(0,str.length()-1);
    }

    public String getMemSign(){
        String str="";
        for(int i=0;i<data.size();i++){
            if (data.get(i).getMemSign()==1)
                str=str+i+",";
        }
        if(str.length()<1){
            str="00";
        }
        return str.substring(0,str.length()-1);
    }


    private String user ;
    String[] users;
    public void setUserInfoList(String user ){
        this.user=user;
        users=user.split(",");
        notifyDataSetChanged();
    }

}