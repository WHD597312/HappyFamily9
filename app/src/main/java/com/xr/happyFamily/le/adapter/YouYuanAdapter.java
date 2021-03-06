package com.xr.happyFamily.le.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xr.happyFamily.R;

import java.util.List;
import java.util.Map;

//快递列表适配器

public class YouYuanAdapter extends RecyclerView.Adapter<YouYuanAdapter.MyViewHolder> implements View.OnClickListener {
    private Context context;
    private List<Map<String, Object>> list;
    private ButtonInterface buttonInterface;

    private int defItem = -1;
    private OnItemListener onItemListener;
    private InnerItemOnclickListener mListener;

    public YouYuanAdapter(Context context, List<Map<String, Object>> list) {
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
                context).inflate(R.layout.item_youyuan, parent,
                false),mItemClickListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.tv_name.setText(list.get(position).get("name").toString());
        holder.tv_old.setText(list.get(position).get("old").toString());
        holder.tv_address.setText(list.get(position).get("address").toString());
        holder.tv_id.setText(list.get(position).get("id").toString());
        holder.img_touxiang.setImageResource((int)list.get(position).get("touxiang"));
        holder.img_add.setOnClickListener(this);
        holder.img_add.setTag(position);
        holder.img_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(buttonInterface!=null) {
//                  接口实例化后的而对象，调用重写后的方法
                    buttonInterface.onclick(v,position);
                }
            }
        });
    }

    public interface InnerItemOnclickListener {
        void itemClick(View v);
    }

    public void setOnInnerItemOnClickListener(InnerItemOnclickListener listener){
        this.mListener=listener;
    }
    @Override
    public void onClick(View v) {
        mListener.itemClick(v);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    /**
     * ViewHolder的类，用于缓存控件
     */
    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView img_add,img_touxiang;
        TextView tv_name,tv_old,tv_address,tv_id;
        private MyItemClickListener mListener;

        public MyViewHolder(View view,MyItemClickListener myItemClickListener) {
            super(view);
            img_touxiang = (ImageView) view.findViewById(R.id.img_touxiang);
            img_add = (ImageView) view.findViewById(R.id.img_add);
            tv_id = (TextView) view.findViewById(R.id.tv_id);
            tv_name= (TextView) view.findViewById(R.id.tv_name);
            tv_old= (TextView) view.findViewById(R.id.tv_old);
            tv_address= (TextView) view.findViewById(R.id.tv_address);
            this.mListener = myItemClickListener;
            itemView.setOnClickListener(this);
        }



        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(v, getLayoutPosition());
            }

        }
    }


    private MyItemClickListener mItemClickListener;
    public void setOnItemClickListener(MyItemClickListener onItemClickListener) {
        mItemClickListener = onItemClickListener;
    }
    public interface MyItemClickListener {
        void onItemClick(View view, int position);
    }
}