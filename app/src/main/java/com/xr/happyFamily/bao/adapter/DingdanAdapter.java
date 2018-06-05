package com.xr.happyFamily.bao.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xr.happyFamily.R;
import com.xr.happyFamily.bao.PingLunActivity;
import com.xr.happyFamily.bao.ShopDingdanActivity;
import com.xr.happyFamily.bao.ShopDingdanXQActivity;
import com.xr.happyFamily.bao.TuiKuanSuccessActivity;
import com.xr.happyFamily.bao.TuiKuanXQActivity;
import com.xr.happyFamily.bao.WuLiuActivity;

import java.util.ArrayList;
import java.util.Map;

//订单适配器


public class DingdanAdapter extends RecyclerView.Adapter<DingdanAdapter.MyViewHolder> implements View.OnClickListener {
    private Context context;
    private ArrayList<Map<String,Object>> list;
    private ButtonInterface buttonInterface;

    private int defItem = -1;
    private OnItemListener onItemListener;
    //0:正在交易，1：交易完成，2：正在退款，3：退款完成
    private int type=0;

    public DingdanAdapter(Context context, ArrayList<Map<String,Object>> list) {
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
                context).inflate(R.layout.item_dingdan, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        Log.e("qqqqqq",list.get(position).get("name").toString());
        holder.tv_shop_name.setText(list.get(position).get("name").toString());
        type=Integer.parseInt(list.get(position).get("type").toString());
        switch (type){
            case 0:
                //正在交易
                holder.tv_state.setText("正在交易");
                holder.img1.setVisibility(View.INVISIBLE);
                holder.img2.setImageResource(R.mipmap.btn_dingdan2);
                holder.img3.setImageResource(R.mipmap.btn_dingdan1);
                break;
            case 1:
                //交易成功
                holder.tv_state.setText("交易成功");
                holder.img1.setImageResource(R.mipmap.btn_dingdan2);
                holder.img2.setImageResource(R.mipmap.btn_dingdan1);
                holder.img3.setImageResource(R.mipmap.btn_dingdan_pingjia);
                break;
            case 2:
                //申请退款
                holder.tv_state.setText("正在退款");
                holder.img3.setImageResource(R.mipmap.btn_dingdan_tuikuan);
                holder.img1.setVisibility(View.INVISIBLE);
                holder.img2.setVisibility(View.INVISIBLE);

                break;
            case 3:
                //申请退款
                holder.tv_state.setText("退款完成");
                holder.img3.setImageResource(R.mipmap.btn_dingdan_tuikuan);
                holder.img1.setVisibility(View.INVISIBLE);
                holder.img2.setVisibility(View.INVISIBLE);

                break;
        }


        holder.img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (Integer.parseInt(list.get(position).get("type").toString())){
                    case 0:
                        //正在交易  确认收货

                        break;
                    case 1:
                        //交易成功
                        Toast.makeText(context,"删除订单",Toast.LENGTH_SHORT).show();
                        showPopup();
                        break;
                    case 2:
                        //申请退款

                        break;
                }
            }
        });
        holder.img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (Integer.parseInt(list.get(position).get("type").toString())){
                    case 0:
                        //正在交易  删除订单
                        Toast.makeText(context,"删除订单",Toast.LENGTH_SHORT).show();
                        showPopup();
                        break;
                    case 1:
                        //交易成功  查看物流
                        context.startActivity(new Intent(context, WuLiuActivity.class));
                        break;
                    case 2:
                        //申请退款  无

                        break;
                }
            }
        });
        holder.img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (Integer.parseInt(list.get(position).get("type").toString())){
                    case 0:
                        //正在交易  查看物流
                        context.startActivity(new Intent(context, WuLiuActivity.class));
                        break;
                    case 1:
                        //交易成功  评价
                        context.startActivity(new Intent(context, PingLunActivity.class));
                        break;
                    case 2:
                        //申请退款  退款详情
                        context.startActivity(new Intent(context, TuiKuanXQActivity.class));
                        break;
                    case 3:
                        //退款完成  退款详情
                        context.startActivity(new Intent(context, TuiKuanSuccessActivity.class));
                        break;
                }
            }
        });
        holder.rl_dd.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.rl_dingdan:
                MyViewHolder tag = (MyViewHolder)v.getTag();
                Intent intent  = new Intent(v.getContext(),ShopDingdanXQActivity.class);
//                intent.putExtra("argCon",tag.argCon);
//                intent.putExtra("argName",tag.argName);
//                intent.putExtra("argValue",tag.argValue);
                v.getContext().startActivity(intent);
                break;

            case R.id.tv_quxiao:
                mPopWindow.dismiss();
                break;
            case R.id.tv_queren:
                mPopWindow.dismiss();
                break;
        }
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    /**
     * ViewHolder的类，用于缓存控件
     */
    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView img1,img2,img3;
        TextView tv_state,tv_shop_price,tv_shop_name,tv_shop_type,tv_shop_num;
        RelativeLayout rl_dd;

        public MyViewHolder(View view) {
            super(view);
            img1 = (ImageView) view.findViewById(R.id.img1);
            img2 = (ImageView) view.findViewById(R.id.img2);
            img3 = (ImageView) view.findViewById(R.id.img3);
            tv_state= (TextView) view.findViewById(R.id.tv_state);
            tv_shop_name= (TextView) view.findViewById(R.id.tv_shop_name);
            tv_shop_type= (TextView) view.findViewById(R.id.tv_shop_type);
            tv_shop_price= (TextView) view.findViewById(R.id.tv_shop_price);
            tv_shop_num= (TextView) view.findViewById(R.id.tv_shop_num);
            rl_dd= (RelativeLayout) view.findViewById(R.id.rl_dingdan);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemListener != null) {
                        onItemListener.onClick(v,getLayoutPosition(),list.get(getLayoutPosition()).get("name").toString());
                    }
                }
            });
        }


    }



    private View contentViewSign;
    private PopupWindow mPopWindow;
    private TextView tv_quxiao,tv_queding,tv_context;

    private void showPopup() {
        contentViewSign = LayoutInflater.from(context).inflate(R.layout.popup_shop_search, null);
        tv_quxiao = (TextView) contentViewSign.findViewById(R.id.tv_quxiao);
        tv_queding = (TextView) contentViewSign.findViewById(R.id.tv_queren);
        tv_context = (TextView) contentViewSign.findViewById(R.id.tv_context);
        tv_quxiao.setOnClickListener(this);
        tv_queding.setOnClickListener(this);
        tv_context.setText("是否确认删除订单？");
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
        mPopWindow.setOnDismissListener(new poponDismissListener());
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
}