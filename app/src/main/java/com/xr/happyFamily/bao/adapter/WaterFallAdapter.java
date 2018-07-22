package com.xr.happyFamily.bao.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.xr.happyFamily.R;
import com.xr.happyFamily.bean.PersonCard;
import com.xr.happyFamily.bean.ShopBean;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by win7 on 2018/5/21.
 */
//瀑布流适配器
public class WaterFallAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<ShopBean.ReturnData.MyList> mData; //定义数据源

    //定义构造方法，默认传入上下文和数据源
    public WaterFallAdapter(Context context, List<ShopBean.ReturnData.MyList> data) {
        this.mContext = context;
        this.mData = data;
    }

    @Override  //将ItemView渲染进来，创建ViewHolder
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.recyclerview_item, null);
        return new MyViewHolder(view,mItemClickListener);
    }

    @Override  //将数据源的数据绑定到相应控件上
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        MyViewHolder holder2 = (MyViewHolder) holder;


        ShopBean.ReturnData.MyList myList = mData.get(position);
        Log.e("qqqqqqqqqqq333",myList.getImage());
        String url = myList.getImage();
        //得到可用的图片
        Picasso.with(mContext)
                .load(url)
                .into(holder2.userAvatar);//此种策略并不会压缩图片


//        Picasso.with(mContext).load(myList.getImage()).into(holder2.userAvatar);
//        holder2.userAvatar.getLayoutParams().height = personCard.imgHeight; //从数据源中获取图片高度，动态设置到控件上
        holder2.tv_name.setText(myList.getGoodsName());
        holder2.tv_context.setText(myList.getSimpleDescribe());
        holder2.tv_price.setText("¥"+myList.getGoodsPrice().get(0).getPrice());
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
        public TextView tv_name,tv_context,tv_price;
        private MyItemClickListener mListener;

        public MyViewHolder(View itemView, MyItemClickListener myItemClickListener) {
            super(itemView);
            userAvatar = (ImageView) itemView.findViewById(R.id.user_avatar);
            tv_name = (TextView) itemView.findViewById(R.id.shop_name);
            tv_context = (TextView) itemView.findViewById(R.id.shop_context);
            tv_price = (TextView) itemView.findViewById(R.id.shop_price);
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

