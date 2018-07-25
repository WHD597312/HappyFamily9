package com.xr.happyFamily.bao.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.xr.happyFamily.R;
import com.xr.happyFamily.bao.PingLunActivity;
import com.xr.happyFamily.le.bean.HappyBannerBean;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//快递列表适配器

public class ShopXqAdapter extends RecyclerView.Adapter<ShopXqAdapter.MyViewHolder> implements View.OnClickListener {
    private Context context;
    private List<String> data;
    private ButtonInterface buttonInterface;

    private int defItem = -1;
    private OnItemListener onItemListener;
    private int type=0;

    public ShopXqAdapter(Context context, List<String> list) {
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
                context).inflate(R.layout.item_shop_xq,  parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        Log.e("qqqqqqqqIIII",data.get(position));
        String str=data.get(position);
        String str1 = null;
        if (str != null) {
            Pattern pattern = Pattern.compile(" |\t|\r|\n|\\s*");
            Matcher matcher = pattern.matcher(str);
            str1 = matcher.replaceAll("");
        }
        Glide.with(context).load(str1)
                .placeholder(R.mipmap.bg_loading)
               .into(holder.img_footBanner);
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

        Log.e("qqqqqqqqIII",data.size()+"???");
        return data.size();
    }

    /**
     * ViewHolder的类，用于缓存控件
     */
    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView img_footBanner;

        public MyViewHolder(View view) {
            super(view);
            img_footBanner= (ImageView) view.findViewById(R.id.img_xiangqing);
        }


    }
}