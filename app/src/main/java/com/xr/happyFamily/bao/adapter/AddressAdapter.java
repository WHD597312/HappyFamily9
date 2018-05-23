package com.xr.happyFamily.bao.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xr.happyFamily.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//快递列表适配器

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<String> list;
    private ButtonInterface buttonInterface;

    private int defItem = -1;
    private OnItemListener onItemListener;

    public AddressAdapter(Context context, ArrayList<String> list) {
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
        public void onclick( View view,int position);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                context).inflate(R.layout.item_address, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.tv_name.setText(list.get(position));
        if (defItem != -1) {
            if (defItem == position) {
                holder.img_choose.setImageResource(R.mipmap.xuanzhong3x);
            } else {
                holder.img_choose.setImageResource(R.mipmap.weixuanzhong3x);
            }
        }
        holder.img_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(buttonInterface!=null) {
//                  接口实例化后的而对象，调用重写后的方法
                    buttonInterface.onclick(v,position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    /**
     * ViewHolder的类，用于缓存控件
     */
    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView img_choose,img_bianji,img_del;
        TextView tv_name,tv_tel,tv_address;

        public MyViewHolder(View view) {
            super(view);
            img_choose = (ImageView) view.findViewById(R.id.img_choose);
            img_bianji = (ImageView) view.findViewById(R.id.img_bianji);
            img_del = (ImageView) view.findViewById(R.id.img_del);
            tv_name= (TextView) view.findViewById(R.id.tv_name);
            tv_tel= (TextView) view.findViewById(R.id.tv_tel);
            tv_address= (TextView) view.findViewById(R.id.tv_address);
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