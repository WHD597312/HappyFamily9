package com.xr.happyFamily.bao.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xr.happyFamily.R;
import com.xr.happyFamily.bean.PersonCard;

import java.util.List;

/**
 * Created by win7 on 2018/5/21.
 */
//瀑布流适配器
public class WaterFallAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<PersonCard> mData; //定义数据源

    //定义构造方法，默认传入上下文和数据源
    public WaterFallAdapter(Context context, List<PersonCard> data) {
        mContext = context;
        mData = data;
    }

    @Override  //将ItemView渲染进来，创建ViewHolder
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.recyclerview_item, null);
        return new MyViewHolder(view);
    }

    @Override  //将数据源的数据绑定到相应控件上
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyViewHolder holder2 = (MyViewHolder) holder;
        PersonCard personCard = mData.get(position);
        int uri = personCard.avatarUrl;
        holder2.userAvatar.setBackgroundResource(uri);
//        holder2.userAvatar.getLayoutParams().height = personCard.imgHeight; //从数据源中获取图片高度，动态设置到控件上
        holder2.userName.setText(personCard.name);
    }

    @Override
    public int getItemCount() {
        if (mData != null) {
            return mData.size();
        }
        return 0;
    }

    //定义自己的ViewHolder，将View的控件引用在成员变量上
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView userAvatar;
        public TextView userName;

        public MyViewHolder(View itemView) {
            super(itemView);
            userAvatar = (ImageView) itemView.findViewById(R.id.user_avatar);
            userName = (TextView) itemView.findViewById(R.id.shop_name);
        }
    }
}

