package com.xr.happyFamily.zhen.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xr.happyFamily.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class VersionAdapter extends RecyclerView.Adapter <VersionAdapter.MyViewholder> {

    Context mContext;
    List<String> mData;
    public VersionAdapter(Context context , List<String> list){
        this.mContext = context;
        this.mData = list;
    }

    @Override
    public MyViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewholder viewholder =  new MyViewholder( LayoutInflater.from(mContext).inflate(R.layout.version_item,parent,false));
        return viewholder;
    }

    @Override
    public void onBindViewHolder(MyViewholder holder, int position) {
        List<String> textv1 = new ArrayList<String>(Arrays.asList("1.家功能介绍", "2.乐的功能介绍", "3.宝的功能介绍" ));
        List<String> line1 = new ArrayList<String>(Arrays.asList("可添加新家创建房间，WiFi连接设备即可手机APP远程控制设备",
                "乐模块主要是娱乐模块,包含变态闹钟和有轨小程序", "商城主要卖食品家电、空气家电、娱乐家电、照明家电、等设备"));
        List<String> line2 = new ArrayList<String>(Arrays.asList("虚拟体验，无需购买设备即可体验控制设备功能,更能快速体验理解","变态闹钟的特点是可以替别人设置闹铃"+'\n'+"有轨特点可随意掌控别人的位置",""));
        int img[] = new int[]{R.mipmap.vers_1,R.mipmap.vers_2,R.mipmap.vers_3};
        holder.textView1.setText(textv1.get(position));
        holder.textView2.setText(line1.get(position));
        holder.textView3.setText(line2.get(position));
        holder.imageView1.setImageResource(img[position]);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class MyViewholder extends ImageAdapter.ViewHolder {
        TextView textView1;
        TextView textView2 ,textView3;
        ImageView imageView1;
        public MyViewholder(View itemView) {

            super(itemView);
            textView1=(TextView) itemView.findViewById(R.id.tv_bz2);
            textView2= (TextView)itemView.findViewById(R.id.tv_bz3);
            textView3= (TextView)itemView.findViewById(R.id.tv_bz4);
            imageView1=(ImageView) itemView.findViewById(R.id.iv_vers_pic);
        }
    }

}
