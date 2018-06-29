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

import com.squareup.picasso.Picasso;
import com.xr.happyFamily.R;
import com.xr.happyFamily.bao.ShopDingdanXQActivity;
import com.xr.happyFamily.bean.ShopPinglunBean;
import com.xr.happyFamily.together.util.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//快递列表适配器

public class PinglunAdapter extends RecyclerView.Adapter<PinglunAdapter.MyViewHolder> {
    private Context context;
    private List<ShopPinglunBean> list;
    private ButtonInterface buttonInterface;

    private int defItem = -1;
    private OnItemListener onItemListener;

    public PinglunAdapter(Context context, List<ShopPinglunBean> list) {
        this.context = context;
        this.list = list;
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
                context).inflate(R.layout.item_shop_pinglun, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        if (list.get(position).getAnonymous())
            holder.tv_name.setText("匿名");
        else
            holder.tv_name.setText(list.get(position).getUsername());


        if((list.get(position).getComment())!=null)
        holder.tv_pinglun.setText(list.get(position).getComment().toString());
        if (list.get(position).getImage() == null) {
            holder.img_touxiang.setImageResource(R.mipmap.ic_touxiang_moren);
        } else
            Picasso.with(context).load(list.get(position).getImage().toString())
                    .error(R.mipmap.ic_touxiang_moren)
                    .into(holder.img_touxiang);
        int buyerRate = list.get(position).getBuyerRate();
        for (int i = 0; i < buyerRate; i++) {
            holder.imgs[i].setImageResource(R.mipmap.ic_pl_xx_true);
        }

//        holder.tv_time.setText(list.get(position).get("time").toString());
//        holder.img_pingjia.setImageResource( Integer.parseInt(list.get(position).get("pic").toString()));
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    /**
     * ViewHolder的类，用于缓存控件
     */
    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView img_touxiang, img1, img2, img3, img4, img5, img_pingjia;
        TextView tv_name, tv_time, tv_pinglun;
        ImageView[] imgs;

        public MyViewHolder(View view) {
            super(view);
            img_touxiang = (ImageView) view.findViewById(R.id.img_touxiang);
            img1 = (ImageView) view.findViewById(R.id.img1);
            img2 = (ImageView) view.findViewById(R.id.img2);
            img3 = (ImageView) view.findViewById(R.id.img3);
            img4 = (ImageView) view.findViewById(R.id.img4);
            img5 = (ImageView) view.findViewById(R.id.img5);
            imgs = new ImageView[]{img1, img2, img3, img4, img5};
            img_pingjia = (ImageView) view.findViewById(R.id.img_pinglun);
            tv_name = (TextView) view.findViewById(R.id.tv_name);
            tv_time = (TextView) view.findViewById(R.id.tv_time);
            tv_pinglun = (TextView) view.findViewById(R.id.tv_pinglun);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemListener != null) {
//                        onItemListener.onClick(v,getLayoutPosition(),list.get(getLayoutPosition()).get("name").toString());
                    }
                }
            });
        }


    }
}