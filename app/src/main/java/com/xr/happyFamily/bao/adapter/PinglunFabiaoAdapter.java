package com.xr.happyFamily.bao.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.xr.happyFamily.R;
import com.xr.happyFamily.bao.ShopXQActivity;
import com.xr.happyFamily.bean.OrderBean;

import java.util.ArrayList;
import java.util.List;

//快递列表适配器

public class PinglunFabiaoAdapter extends RecyclerView.Adapter<PinglunFabiaoAdapter.MyViewHolder>  {
    private Context context;
    private List<String> data;
    private ButtonInterface buttonInterface;

    private int defItem = -1;
    private OnItemListener onItemListener;
    private int type=0;
    private String shopId;
    private String[] str_ed;
    private int[] str_img;

    public PinglunFabiaoAdapter(Context context, List<String> list) {
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
                context).inflate(R.layout.item_pinglun_fabiao, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
//        holder.tv_shop_type.setText(data.get(position).getGoods().getSimpleDescribe());
        Picasso.with(context)
                .load(data.get(position))
                .into(holder.img_pl);//此种策略并不会压缩图片
        str_ed=new String[data.size()];
        str_img=new int[data.size()];
        for(int i=0;i<data.size();i++){
            str_img[i]=5;
        }
        holder.img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.img2.setImageResource(R.mipmap.ic_tuihuo_xingxing_false);
                holder.img3.setImageResource(R.mipmap.ic_tuihuo_xingxing_false);
                holder.img4.setImageResource(R.mipmap.ic_tuihuo_xingxing_false);
                holder.img5.setImageResource(R.mipmap.ic_tuihuo_xingxing_false);
                holder.tv_level.setText("非常差");
                str_img[position]=1;
            }
        });

        holder.img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.img2.setImageResource(R.mipmap.iv_tuihuo_xing_true);
                holder.img3.setImageResource(R.mipmap.ic_tuihuo_xingxing_false);
                holder.img4.setImageResource(R.mipmap.ic_tuihuo_xingxing_false);
                holder.img5.setImageResource(R.mipmap.ic_tuihuo_xingxing_false);
                holder.tv_level.setText("差");
                str_img[position]=2;
            }
        });

        holder.img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.img2.setImageResource(R.mipmap.iv_tuihuo_xing_true);
                holder.img3.setImageResource(R.mipmap.iv_tuihuo_xing_true);
                holder.img4.setImageResource(R.mipmap.ic_tuihuo_xingxing_false);
                holder.img5.setImageResource(R.mipmap.ic_tuihuo_xingxing_false);
                holder.tv_level.setText("一般");
                str_img[position]=3;
            }
        });

        holder.img4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.img2.setImageResource(R.mipmap.iv_tuihuo_xing_true);
                holder.img3.setImageResource(R.mipmap.iv_tuihuo_xing_true);
                holder.img4.setImageResource(R.mipmap.iv_tuihuo_xing_true);
                holder.img5.setImageResource(R.mipmap.ic_tuihuo_xingxing_false);
                holder.tv_level.setText("好");
                str_img[position]=4;
            }
        });

        holder.img5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.img2.setImageResource(R.mipmap.iv_tuihuo_xing_true);
                holder.img3.setImageResource(R.mipmap.iv_tuihuo_xing_true);
                holder.img4.setImageResource(R.mipmap.iv_tuihuo_xing_true);
                holder.img5.setImageResource(R.mipmap.iv_tuihuo_xing_true);
                holder.tv_level.setText("非常好");
                str_img[position]=5;
            }
        });

        holder.ed_pl.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                str_ed[position]=s.toString();
            }
        });
    }


    public String[] getStr_ed() {
        return str_ed;
    }

    public int[] getStr_img() {
        return str_img;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    /**
     * ViewHolder的类，用于缓存控件
     */
    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView img_pl,img1,img2,img3,img4,img5;
        EditText ed_pl;
        TextView tv_level;

        public MyViewHolder(View view) {
            super(view);

            img_pl= (ImageView) view.findViewById(R.id.img_pl);
            img1= (ImageView) view.findViewById(R.id.img_xing1);
            img2= (ImageView) view.findViewById(R.id.img_xing2);
            img3= (ImageView) view.findViewById(R.id.img_xing3);
            img4= (ImageView) view.findViewById(R.id.img_xing4);
            img5= (ImageView) view.findViewById(R.id.img_xing5);

            tv_level= (TextView) view.findViewById(R.id.tv_level);
            ed_pl= (EditText) view.findViewById(R.id.ed_pl);

//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (onItemListener != null) {
//                        onItemListener.onClick(v,getLayoutPosition(),data.get(getLayoutPosition()).getGoodsName().toString());
//                    }
//                }
//            });
        }


    }
}