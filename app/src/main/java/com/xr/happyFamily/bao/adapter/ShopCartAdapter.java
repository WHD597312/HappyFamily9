package com.xr.happyFamily.bao.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.squareup.picasso.Picasso;
import com.xr.happyFamily.R;
import com.xr.happyFamily.bao.ShopCartActivity;
import com.xr.happyFamily.bean.ShopCartBean;
import com.xr.happyFamily.together.http.HttpUtils;
import com.xr.happyFamily.together.util.Utils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//购物车列表适配器

public class ShopCartAdapter extends RecyclerView.Adapter<ShopCartAdapter.MyViewHolder> implements View.OnClickListener {

    private Context context;
    private List<ShopCartBean> data;
    private View headerView;
    private OnDeleteClickListener mOnDeleteClickListener;
    private OnEditClickListener mOnEditClickListener;
    private OnResfreshListener mOnResfreshListener;
    private boolean isEdit=false;


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



        holder.tvShopType.setText(data.get(position).getGoods().getSimpleDescribe());
        holder.tvShopName.setText(data.get(position).getGoods().getGoodsName());
        holder.tvShopPrice.setText(data.get(position).getGoodsPrice().getPrice()+"");
        holder.tvShopNum.setText(data.get(position).getQuantity() + "");

        Picasso.with(context)
                .load(data.get(position).getGoods().getImage())
                .into(holder.ivShopPic);//此种策略并不会压缩图片

        if(mOnResfreshListener != null){
            boolean isSelect = true;
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

        holder.view_jian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i=Integer.parseInt(holder.tvShopNum.getText().toString());
                if(i>1)
                    holder.tvShopNum.setText(i-1+"");
                Map<String, Object> params = new HashMap<>();
                params.put("priceId", data.get(position).getGoodsPrice().getPriceId());
                params.put("cartId", data.get(position).getCartId());
                new subShopAsync().execute(params);
                if(data.get(position).getQuantity() > 1) {
                    int count = data.get(position).getQuantity() - 1;
                    if (mOnEditClickListener != null) {
                        mOnEditClickListener.onEditClick(position, data.get(position).getId(), count);
                    }
                    data.get(position).setQuantity(count);
                    notifyDataSetChanged();
                }
            }
        });

        holder.view_jia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i=Integer.parseInt(holder.tvShopNum.getText().toString());
                if(i>1)
                    holder.tvShopNum.setText(i+1+"");
                Map<String, Object> params = new HashMap<>();
                params.put("priceId", data.get(position).getGoodsPrice().getPriceId());
                new addShopAsync().execute(params);
                int count = data.get(position).getQuantity() + 1;
                if(mOnEditClickListener != null){
                    mOnEditClickListener.onEditClick(position,data.get(position).getId(),count);
                }
                data.get(position).setQuantity(count);
                notifyDataSetChanged();
            }
        });


        holder.tvShopNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              showPopup(position);
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

        private View view_jia,view_jian;


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
            view_jia=view.findViewById(R.id.view_jia);
            view_jian=  view.findViewById(R.id.view_jian);

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



    @Override
    public void onClick(View v) {
        int i=Integer.parseInt(ed_num.getText().toString());
        switch (v.getId()) {
            case R.id.tv_quxiao:
                mPopWindow.dismiss();
                break;
            case R.id.tv_shop_reduce:
                if(i>1)
                ed_num.setText(i-1+"");
                break;
            case R.id.tv_shop_add:
                    ed_num.setText(i+1+"");
                break;
        }
    }



    public static boolean isInteger(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private View contentViewSign;
    private PopupWindow mPopWindow;
    private TextView tv_quxiao,tv_queding,tv_context,tv_add,tv_jian;
    EditText ed_num;

    private void showPopup(final int pos) {
        contentViewSign = LayoutInflater.from(context).inflate(R.layout.popup_shopcart_num, null);
        tv_quxiao = (TextView) contentViewSign.findViewById(R.id.tv_quxiao);
        tv_queding = (TextView) contentViewSign.findViewById(R.id.tv_queren);
        tv_context = (TextView) contentViewSign.findViewById(R.id.tv_context);
        tv_jian = (TextView) contentViewSign.findViewById(R.id.tv_shop_reduce);
        ed_num = (EditText) contentViewSign.findViewById(R.id.ed_shop_num);
        tv_add = (TextView) contentViewSign.findViewById(R.id.tv_shop_add);
        ed_num.setText(data.get(pos).getQuantity()+"");
        tv_quxiao.setOnClickListener(this);
        tv_queding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopWindow.dismiss();
                data.get(pos).setQuantity(Integer.parseInt(ed_num.getText().toString()));
                Map<String, Object> params = new HashMap<>();
                params.put("cartId", data.get(pos).getCartId());
                params.put("priceId", data.get(pos).getGoodsPrice().getPriceId());
                params.put("quantity", ed_num.getText().toString());
                new editAsync().execute(params);
                notifyDataSetChanged();
            }
        });
        tv_jian.setOnClickListener(this);
        tv_add.setOnClickListener(this);
        mPopWindow = new PopupWindow(contentViewSign);
        mPopWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //在PopupWindow里面就加上下面代码，让键盘弹出时，不会挡住pop窗口。
        mPopWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        mPopWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        //点击空白处时，隐藏掉pop窗口
        mPopWindow.setFocusable(true);
//        mPopWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopWindow.setOutsideTouchable(true);
        backgroundAlpha(0.5f);
        //添加pop窗口关闭事件
        mPopWindow.setOnDismissListener(new ShopCartAdapter.poponDismissListener());
        mPopWindow.showAtLocation(((Activity)context).getWindow().getDecorView(), Gravity.CENTER, 0, 0);
    }


    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = ((Activity)context).getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        ((Activity)context).getWindow().setAttributes(lp); //添加pop窗口关闭事件
    }



    class poponDismissListener implements PopupWindow.OnDismissListener {

        @Override
        public void onDismiss() {
            // TODO Auto-generated method stub
            backgroundAlpha(1f);
        }
    }


    class editAsync extends AsyncTask<Map<String, Object>, Void, String> {
        @Override
        protected String doInBackground(Map<String, Object>... maps) {
            Map<String, Object> params = maps[0];
            String url = "shoppingcart/editNumeberToShoppingCart";
            String result = HttpUtils.headerPostOkHpptRequest(context, url, params);
            String code = "";
            try {
                if (!Utils.isEmpty(result)) {
                    JSONObject jsonObject = new JSONObject(result);
                    code = jsonObject.getString("returnCode");
                    JSONObject content = jsonObject.getJSONObject("returnData");

                    if ("100".equals(code)) {

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return code;
        }

    }
    class addShopAsync extends AsyncTask<Map<String, Object>, Void, String> {
        @Override
        protected String doInBackground(Map<String, Object>... maps) {
            Map<String, Object> params = maps[0];
            String url = "shoppingcart/addOneToShoppingCart";
            String result = HttpUtils.headerPostOkHpptRequest(context, url, params);
            String code = "";
            try {
                if (!Utils.isEmpty(result)) {
                    JSONObject jsonObject = new JSONObject(result);
                    code = jsonObject.getString("returnCode");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return code;
        }

    }

    class subShopAsync extends AsyncTask<Map<String, Object>, Void, String> {
        @Override
        protected String doInBackground(Map<String, Object>... maps) {
            Map<String, Object> params = maps[0];
            String url = "shoppingcart/subOneToShoppingCart";
            String result = HttpUtils.headerPostOkHpptRequest(context, url, params);
            String code = "";
            try {
                if (!Utils.isEmpty(result)) {
                    JSONObject jsonObject = new JSONObject(result);
                    code = jsonObject.getString("returnCode");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return code;
        }

    }



}
