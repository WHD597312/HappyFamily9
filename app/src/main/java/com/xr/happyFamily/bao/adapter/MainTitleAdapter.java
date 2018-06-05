package com.xr.happyFamily.bao.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xr.happyFamily.R;

import java.util.ArrayList;
import java.util.List;

//商城主页标题列表适配器

public class MainTitleAdapter extends RecyclerView.Adapter<MainTitleAdapter.ViewHolder>  {

    private Context context;
    private List<String> datas;

    private int mPosition = 0;
    public void setPosition(int position) {
        mPosition = position;
    }

    public MainTitleAdapter(Context context, List<String> datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_shop_title, parent, false);
        ViewHolder vh=new ViewHolder(view,mItemClickListener);
        return vh;
    }




    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (mPosition == position){
            holder.img.setVisibility(View.VISIBLE);
        } else {
            holder.img.setVisibility(View.INVISIBLE);
        }
        holder.tv_name.setText(datas.get(position));


    }

    @Override
    public int getItemCount() {
        return datas.size();
    }//Integer.MAX_VALUE



    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView img;
        TextView tv_name;
        private MyItemClickListener mListener;

        public ViewHolder(View itemView,MyItemClickListener myItemClickListener) {
            super(itemView);
            img= (ImageView) itemView.findViewById(R.id.img_title);
            tv_name= (TextView) itemView.findViewById(R.id.tv_name);
            this.mListener = myItemClickListener;
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(v, getPosition());
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