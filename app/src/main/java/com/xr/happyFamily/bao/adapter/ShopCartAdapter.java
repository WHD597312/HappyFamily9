package com.xr.happyFamily.bao.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.xr.happyFamily.R;
import com.xr.happyFamily.bao.ShopCartActivity;
import com.xr.happyFamily.bean.ShopCartBean;

import java.util.List;

 //购物车列表适配器

public class ShopCartAdapter extends RecyclerView.Adapter<ShopCartAdapter.MyViewHolder> {

    private Context context;
    private List<ShopCartBean> data;
    private View headerView;
    private OnDeleteClickListener mOnDeleteClickListener;
    private OnEditClickListener mOnEditClickListener;
    private OnResfreshListener mOnResfreshListener;


    public ShopCartAdapter(Context context, List<ShopCartBean> data){
        this.context = context;
        this.data = data;
    }

    @Override
    public ShopCartAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.item_shopcart, parent, false);
        return new ShopCartAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ShopCartAdapter.MyViewHolder holder, final int position) {




        holder.tvShopType.setText(data.get(position).getDetails());
        holder.tvShopName.setText(data.get(position).getProductName());

        holder.tvShopPrice.setText(data.get(position).getPrice());
        holder.tvShopNum.setText(data.get(position).getCount() + "");


        if(mOnResfreshListener != null){
            boolean isSelect = false;
            for(int i = 0;i < data.size(); i++){
                if(!data.get(i).getIsSelect()){
                    isSelect = false;
                    break;
                }else{
                    isSelect = true;
                }
            }
            mOnResfreshListener.onResfresh(isSelect);
        }

        holder.llShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnItemOnClickListener.onItemOnClick(holder.itemView,position);
            }
        });
//
//        holder.llShop.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                mOnItemOnClickListener.onItemLongOnClick(holder.itemView,position);
//                return true;
//            }
//        });

        holder.tvShopReduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("qqqqqqq222",position+"?????");
                if(data.get(position).getCount() > 1) {
                    int count = data.get(position).getCount() - 1;
                    if (mOnEditClickListener != null) {
                        mOnEditClickListener.onEditClick(position, data.get(position).getId(), count);
                    }
                    data.get(position).setCount(count);
                    notifyDataSetChanged();
                }
            }
        });

        holder.tvShopAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("qqqqqqq222",position+"?????");
                int count = data.get(position).getCount() + 1;
                if(mOnEditClickListener != null){
                    mOnEditClickListener.onEditClick(position,data.get(position).getId(),count);
                }
                data.get(position).setCount(count);
                notifyDataSetChanged();
            }
        });

        if(data.get(position).getIsSelect()){
            holder.ivShopSel.setImageDrawable(ContextCompat.getDrawable(context,R.mipmap.xuanzhong_shop3x));
        }else {
            holder.ivShopSel.setImageDrawable(ContextCompat.getDrawable(context,R.mipmap.weixuanzhong3x));
        }





        holder.ivShopSel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.get(position).setSelect(!data.get(position).getIsSelect());
                //通过循环找出不同商铺的第一个商品的位置
                for(int i = 0;i < data.size(); i++){
                    if(data.get(i).getIsFirst() == 1) {
                        //遍历去找出同一家商铺的所有商品的勾选情况
                        for(int j = 0;j < data.size();j++){
                            //如果是同一家商铺的商品，并且其中一个商品是未选中，那么商铺的全选勾选取消
                            if(data.get(j).getShopId() == data.get(i).getShopId() && !data.get(j).getIsSelect()){
                                data.get(i).setShopSelect(false);
                                break;
                            }else{
                                //如果是同一家商铺的商品，并且所有商品是选中，那么商铺的选中全选勾选
                                data.get(i).setShopSelect(true);
                            }
                        }
                    }
                }
                notifyDataSetChanged();
            }
        });


    }

    public void showDialog(final View view, final int position){
            //调用删除某个规格商品的接口
            if(mOnDeleteClickListener != null){
                mOnDeleteClickListener.onDeleteClick(view,position,data.get(position).getId());
            }
            data.remove(position);
            //重新排序，标记所有商品不同商铺第一个的商品位置
            ShopCartActivity.isSelectFirst(data);
            notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        int count = (data == null ? 0 : data.size());
        if(headerView != null){
            count++;
        }
        return count;
    }




    class MyViewHolder extends RecyclerView.ViewHolder
    {



        private TextView tvShopName;
        private TextView tvShopPrice;
        private TextView tvShopNum;
        private TextView tvShopType;
        private TextView tvShopReduce;
        private TextView tvShopAdd;
        private ImageView ivShopSel;
        private ImageView ivShopPic;

        private RelativeLayout llShop;


        public MyViewHolder(View view)
        {
            super(view);

            tvShopName = (TextView) view.findViewById(R.id.tv_shop_name);
            tvShopPrice = (TextView) view.findViewById(R.id.tv_shop_price);
            tvShopNum = (TextView) view.findViewById(R.id.tv_shop_num);
            tvShopType = (TextView) view.findViewById(R.id.tv_shop_type);
            ivShopSel = (ImageView) view.findViewById(R.id.radio_shop);
            tvShopReduce = (TextView) view.findViewById(R.id.tv_shop_reduce);
            tvShopAdd = (TextView) view.findViewById(R.id.tv_shop_add);
            ivShopPic = (ImageView) view.findViewById(R.id.img_shop_pic);
            llShop= (RelativeLayout) view.findViewById(R.id.ll_shop);

        }
    }


    public View getHeaderView(){
        return headerView;
    }

    private ShopCartAdapter.OnItemClickListener mOnItemClickListener;
    public interface OnItemClickListener{
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(ShopCartAdapter.OnItemClickListener mOnItemClickListener){
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public interface OnDeleteClickListener{
        void onDeleteClick(View view, int position, int cartid);
    }

    public void setOnDeleteClickListener(OnDeleteClickListener mOnDeleteClickListener){
        this.mOnDeleteClickListener = mOnDeleteClickListener;
    }

    public interface OnEditClickListener{
        void onEditClick(int position, int cartid, int count);
    }

    public void setOnEditClickListener(OnEditClickListener mOnEditClickListener){
        this.mOnEditClickListener = mOnEditClickListener;
    }

    public interface OnResfreshListener{
        void onResfresh(boolean isSelect);
    }

    public void setResfreshListener(OnResfreshListener mOnResfreshListener){
        this.mOnResfreshListener = mOnResfreshListener;
    }


    public interface OnItemOnClickListener{
        void onItemOnClick(View view, int pos);
        void onItemLongOnClick(View view, int pos);
    }



    private OnItemOnClickListener mOnItemOnClickListener;
    public boolean setOnItemClickListener(OnItemOnClickListener listener){
        this.mOnItemOnClickListener = listener;
        return false;
    }





}
