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
        return new MyViewHolder(view,mItemClickListener);
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
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView userAvatar;
        public TextView userName;
        private MyItemClickListener mListener;

        public MyViewHolder(View itemView, MyItemClickListener myItemClickListener) {
            super(itemView);
            userAvatar = (ImageView) itemView.findViewById(R.id.user_avatar);
            userName = (TextView) itemView.findViewById(R.id.shop_name);
            this.mListener = myItemClickListener;
            itemView.setOnClickListener(this);
        }
        /**
         * 实现OnClickListener接口重写的方法
         * @param v
         */
        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(v, getPosition());
            }

        }
    }

    private MyItemClickListener mItemClickListener;
    /**
     * 创建一个回调接口
     */
    public interface MyItemClickListener {
        void onItemClick(View view, int position);
    }

    /**
     * 在activity里面adapter就是调用的这个方法,将点击事件监听传递过来,并赋值给全局的监听
     *
     * @param myItemClickListener
     */
    public void setItemClickListener(MyItemClickListener myItemClickListener) {
        this.mItemClickListener = myItemClickListener;
    }
}

