package com.xr.happyFamily.bao.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.xr.happyFamily.R;
import com.xr.happyFamily.bao.PayFailActivity;
import com.xr.happyFamily.bao.PaySuccessActivity;
import com.xr.happyFamily.bao.PingLunActivity;
import com.xr.happyFamily.bao.ShopConfActivity;
import com.xr.happyFamily.bao.ShopDingdanXQActivity;
import com.xr.happyFamily.bao.TestActivity;
import com.xr.happyFamily.bao.TuiKuanActivity;
import com.xr.happyFamily.bao.TuiKuanSuccessActivity;
import com.xr.happyFamily.bao.TuiKuanXQActivity;
import com.xr.happyFamily.bao.WuLiuActivity;
import com.xr.happyFamily.bao.alipay.PayActivity;
import com.xr.happyFamily.bao.view.LinearGradientView;
import com.xr.happyFamily.bean.OrderBean;
import com.xr.happyFamily.together.http.HttpUtils;
import com.xr.happyFamily.together.util.TimeUtils;
import com.xr.happyFamily.together.util.Utils;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//订单适配器


public class DingdanAdapter extends RecyclerView.Adapter<DingdanAdapter.MyViewHolder> implements View.OnClickListener {
    private Context context;
    private List<OrderBean.OrderDetailsList> list = new ArrayList<>();
    private ButtonInterface buttonInterface;

    private int defItem = -1;
    private OnItemListener onItemListener;
    //0:正在交易，1：交易完成，2：正在退款，3：退款完成
    private int type = 0;

    public DingdanAdapter(Context context, List<OrderBean.OrderDetailsList> list) {
        this.context = context;
        this.list = list;
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
     * 按钮点击事件需要的方法
     */
    public void buttonSetOnclick(ButtonInterface buttonInterface) {
        this.buttonInterface = buttonInterface;
    }

    /**
     * 按钮点击事件对应的接口
     */
    public interface ButtonInterface {
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
        final int refundState = list.get(position).getRefundState();

        Log.e("qqqqqqqT222",position+","+list.get(position).getTime()+","+list.get(position).getCreateTime());
        String sd=TimeUtils.getTime(list.get(position).getTime());

        holder.tv_time.setText(sd);
        holder.tv_shop_name.setText(list.get(position).getGoodsName());
        //内容
        holder.tv_shop_type.setText(list.get(position).getSimpleDescribe());
        holder.tv_shop_price.setText(list.get(position).getPrice() + "");
        holder.tv_shop_num.setText("X" + list.get(position).getNum());
        Picasso.with(context)
                .load(list.get(position).getImage())
                .into(holder.img_shop_pic);//此种策略并不会压缩图片
        type = Integer.parseInt(list.get(position).getState() + "");

        switch (type) {
            case 1:
                //待付款
                holder.tv_state.setText("等待付款");
                holder.img2.setVisibility(View.VISIBLE);
                holder.img3.setVisibility(View.VISIBLE);
                holder.img2.setImageResource(R.mipmap.btn_dd_quxiao);
                holder.img3.setImageResource(R.mipmap.btn_dd_pay);

//                holder.img3.setImageResource(R.mipmap.btn_dingdan3);
                break;
            case 2:
            case 3:
                //待收货
                holder.tv_state.setText("正在交易");
                holder.img2.setVisibility(View.INVISIBLE);
                holder.img3.setVisibility(View.VISIBLE);
                holder.img3.setImageResource(R.mipmap.btn_dd_wuliu);
                break;
            case 4:
                //已收货
                holder.tv_state.setText("已收货");
                holder.img3.setVisibility(View.VISIBLE);
                holder.img3.setImageResource(R.mipmap.btn_dd_pingjia);
                holder.img2.setVisibility(View.INVISIBLE);



                break;
            case 5:
                //申请退款

                if (refundState == 0)
                    holder.tv_state.setText("申请退款");
                else if (refundState == 1)
                    holder.tv_state.setText("退款成功");
                else if (refundState == 2)
                    holder.tv_state.setText("退款失败");
                holder.img3.setVisibility(View.VISIBLE);
                holder.img3.setImageResource(R.mipmap.btn_dd_tuikuan);
                holder.img2.setVisibility(View.INVISIBLE);
                break;
            case 6:
                //交易关闭
                holder.tv_state.setText("交易关闭");
                holder.img3.setVisibility(View.GONE);
                holder.img2.setVisibility(View.GONE);
                holder.img1.setVisibility(View.GONE);
                break;

        }


        holder.img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (Integer.parseInt(list.get(position).getState() + "")) {
                    case 1:
                        //待支付 取消支付
                        showPopup(position);
                        break;

                }
            }
        });
        holder.img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (Integer.parseInt(list.get(position).getState() + "")) {
                    case 1:
                        Intent intent=new Intent(context, PayFailActivity.class);
                        intent.putExtra("type", "DingDan");
                        intent.putExtra("orderNumber", list.get(position).getOrderNumber());
//                        intent.putExtra("orderDetailsLists", (Serializable) orderDetailsLists);
                        intent.putExtra("money", list.get(position).getDetailsAmount());
                        context.startActivity(intent);
                        break;
                    case 2:
                        Toast.makeText(context, "该商品暂未发货", Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        //待收货  查看物流
                        Intent intent3 = new Intent(context, WuLiuActivity.class);
                        intent3.putExtra("logisticCode", list.get(position).getLogisticCode());
                        intent3.putExtra("shipperCode", list.get(position).getShipperCode());
                        intent3.putExtra("orderNumber",list.get(position).getOrderNumber());
                        context.startActivity(intent3);
                        break;
                    case 4:
                        //已收货  评论
                        ArrayList<String> img = new ArrayList<>();
                        ArrayList<Integer> orderId = new ArrayList<>();
                        ArrayList<Integer> priceId = new ArrayList<>();
                        ArrayList<Integer> goodsId = new ArrayList<>();
                        Map<String, String> map = new HashMap<>();


                        for (int i = 0; i < position + 1; i++) {
                            if (list.get(i).getOrderId() == list.get(position).getOrderId()) {
                                img.add(list.get(i).getImage());
                                orderId.add(list.get(i).getOrderId());
                                priceId.add(list.get(i).getPriceId());
                                goodsId.add(Integer.parseInt(list.get(i).getGoodsId()));

                            }
                        }
//                        for(int i=0;i<list.size();i++) {
//                            img.add(list.get(position).getImage());
//                        }
                        Intent intent1 = new Intent(context, PingLunActivity.class);
                        intent1.putStringArrayListExtra("img", img);
                        intent1.putIntegerArrayListExtra("orderId", orderId);
                        intent1.putIntegerArrayListExtra("priceId", priceId);
                        intent1.putIntegerArrayListExtra("goodsId", goodsId);
                        context.startActivity(intent1);
                        break;
                    case 5:
                        //退款  退款详情
                        if (refundState == 0)
                        {
                            Intent intent2 = new Intent(v.getContext(), TuiKuanXQActivity.class);
                            intent2.putExtra("orderId", list.get(position).getOrderNumber() + "");
                            v.getContext().startActivity(intent2);
                        }
                        else if (refundState == 1) {
                            Intent intent5=new Intent(context, TuiKuanSuccessActivity.class);
                            intent5.putExtra("sign",refundState);
                            intent5.putExtra("money",list.get(position).getDetailsAmount());
                            intent5.putExtra("time",TimeUtils.getTime(list.get(position).getRefundTime()));
                            context.startActivity(intent5);
                        }
                        else if (refundState == 2)
                        {
                            Intent intent4=new Intent(context, TuiKuanSuccessActivity.class);
                            intent4.putExtra("sign",refundState);
                            intent4.putExtra("money",list.get(position).getDetailsAmount());
                            intent4.putExtra("time",TimeUtils.getTime(list.get(position).getRefundTime()));
//                            intent4.putExtra("time",list.get(position).)
                            context.startActivity(intent4);
                        }
                        break;
                }
            }
        });
        holder.rl_dd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String state = list.get(position).getState();
                switch (state) {
                    case "1":
                    case "2":
                    case "3":
                    case "4":
                        Intent intent = new Intent(v.getContext(), ShopDingdanXQActivity.class);
                        intent.putExtra("orderId", list.get(position).getOrderNumber() + "");
                        v.getContext().startActivity(intent);
                        break;
                    case "5":
                        if (refundState == 0)
                        {
                            Intent intent2 = new Intent(v.getContext(), TuiKuanXQActivity.class);
                            intent2.putExtra("orderId", list.get(position).getOrderNumber() + "");
                            v.getContext().startActivity(intent2);
                        }
                        else if (refundState == 1) {
                            Intent intent3=new Intent(context, TuiKuanSuccessActivity.class);
                            intent3.putExtra("sign",refundState);
                            intent3.putExtra("money",list.get(position).getDetailsAmount());
                            intent3.putExtra("time",TimeUtils.getTime(list.get(position).getRefundTime()));
                            context.startActivity(intent3);
                        }
                        else if (refundState == 2)
                        {
                            Intent intent4=new Intent(context, TuiKuanSuccessActivity.class);
                            intent4.putExtra("sign",refundState);
                            intent4.putExtra("money",list.get(position).getDetailsAmount());
                            intent4.putExtra("time",TimeUtils.getTime(list.get(position).getRefundTime()));
//                            intent4.putExtra("time",list.get(position).)
                            context.startActivity(intent4);
                        }
                        break;
                }

            }
        });
        if (!list.get(position).getFirst()) {
            holder.view_sign.setVisibility(View.GONE);
            holder.tv_state.setVisibility(View.GONE);
            holder.tv_time.setVisibility(View.GONE);
        } else {
            holder.view_sign.setVisibility(View.VISIBLE);
            holder.tv_state.setVisibility(View.VISIBLE);
            holder.tv_time.setVisibility(View.VISIBLE);
        }

        if (!list.get(position).getFinish()) {
            holder.rl_sign.setVisibility(View.GONE);
            holder.ll2.setVisibility(View.GONE);
            holder.view2.setVisibility(View.GONE);
            holder.ll3.setVisibility(View.GONE);
        } else {
            holder.rl_sign.setVisibility(View.VISIBLE);
            holder.ll2.setVisibility(View.VISIBLE);
            holder.view2.setVisibility(View.VISIBLE);
            holder.ll3.setVisibility(View.VISIBLE);
        }

        if (list.get(position).getIsRate() == 1) {
            holder.rl_sign.setVisibility(View.GONE);
        } else {
            holder.rl_sign.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {


            case R.id.tv_quxiao:
                mPopWindow.dismiss();
                break;
            case R.id.tv_queren:

                break;
        }
    }


    @Override
    public int getItemCount() {
//        return 0;
        return list.size();
    }

    /**
     * ViewHolder的类，用于缓存控件
     */
    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView img1, img2, img3, img_shop_pic;
        TextView tv_state, tv_shop_price, tv_shop_name, tv_shop_type, tv_shop_num, tv_time;
        RelativeLayout rl_dd, rl_sign;
        LinearLayout ll2;
        LinearGradientView ll3;
        View view_sign, view2;

        public MyViewHolder(View view) {
            super(view);
            img1 = (ImageView) view.findViewById(R.id.img1);
            img2 = (ImageView) view.findViewById(R.id.img2);
            img3 = (ImageView) view.findViewById(R.id.img3);
            img_shop_pic = (ImageView) view.findViewById(R.id.img_shop_pic);
            tv_time = (TextView) view.findViewById(R.id.tv_time);
            tv_state = (TextView) view.findViewById(R.id.tv_state);
            tv_shop_name = (TextView) view.findViewById(R.id.tv_shop_name);
            tv_shop_type = (TextView) view.findViewById(R.id.tv_shop_type);
            tv_shop_price = (TextView) view.findViewById(R.id.tv_shop_price);
            tv_shop_num = (TextView) view.findViewById(R.id.tv_shop_num);
            rl_dd = (RelativeLayout) view.findViewById(R.id.rl_dingdan);
            rl_sign = (RelativeLayout) view.findViewById(R.id.rl_sign);
            view_sign = view.findViewById(R.id.view_sign);
            view2 = view.findViewById(R.id.view_2);
            ll2 = (LinearLayout) view.findViewById(R.id.ll_2);
            ll3 = (LinearGradientView) view.findViewById(R.id.ll3);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemListener != null) {
                        onItemListener.onClick(v, getLayoutPosition(), list.get(getLayoutPosition()).getGoodsName().toString());
                    }
                }
            });
        }


    }


    private View contentViewSign;
    private PopupWindow mPopWindow;
    private TextView tv_quxiao, tv_queding, tv_context;

    private void showPopup(final int pos) {
        contentViewSign = LayoutInflater.from(context).inflate(R.layout.popup_main, null);
        tv_quxiao = (TextView) contentViewSign.findViewById(R.id.tv_quxiao);
        tv_queding = (TextView) contentViewSign.findViewById(R.id.tv_queren);
        tv_context = (TextView) contentViewSign.findViewById(R.id.tv_context);
        tv_quxiao.setOnClickListener(this);
        tv_queding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopWindow.dismiss();
                Map<String, Object> params = new HashMap<>();
                params.put("orderId", list.get(pos).getOrderId());
                params.put("pos", pos);
                new cancelOrderAsync().execute(params);
            }
        });
        tv_context.setText("是否确认取消订单？");
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
        mPopWindow.showAtLocation(((Activity) context).getWindow().getDecorView(), Gravity.CENTER, 0, 0);
    }


    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = ((Activity) context).getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        ((Activity) context).getWindow().setAttributes(lp); //添加pop窗口关闭事件
    }


    class poponDismissListener implements PopupWindow.OnDismissListener {

        @Override
        public void onDismiss() {
            // TODO Auto-generated method stub
            backgroundAlpha(1f);
        }

    }


    class cancelOrderAsync extends AsyncTask<Map<String, Object>, Void, String> {
        private int pos;

        @Override
        protected String doInBackground(Map<String, Object>... maps) {

            Map<String, Object> params = maps[0];
            pos = Integer.parseInt(params.get("pos").toString());
            String url = "/order/cancelOrder";
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

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (!Utils.isEmpty(s) && "100".equals(s)) {
                Toast.makeText(context, "取消订单成功", Toast.LENGTH_SHORT).show();
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getOrderId() == list.get(pos).getOrderId())
                    list.get(i).setState("6");
                }
                notifyDataSetChanged();
            }
        }
    }
}