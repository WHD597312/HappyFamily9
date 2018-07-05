package com.xr.happyFamily.le.adapter;

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
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.xr.happyFamily.R;
import com.xr.happyFamily.bao.PingLunActivity;
import com.xr.happyFamily.bao.adapter.DingdanAdapter;
import com.xr.happyFamily.bean.ShopCartBean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//快递列表适配器

public class ClockQinglvAdapter extends RecyclerView.Adapter<ClockQinglvAdapter.MyViewHolder> implements View.OnClickListener {
    private Context context;
    private List<Map<String, Object>> data;
    private ButtonInterface buttonInterface;

    private int defItem = -1;
    private OnItemListener onItemListener;
    private int type = 0;

    public ClockQinglvAdapter(Context context, List<Map<String, Object>> list) {
        this.context = context;
        this.data = list;
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
                context).inflate(R.layout.item_clock_qinglv, parent,
                false));
        return holder;
    }




    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.tv_time.setText(data.get(position).get("time").toString());
        holder.tv_name.setText(data.get(position).get("name").toString());
        holder.tv_context.setText(data.get(position).get("context").toString());
        holder.tv_day.setText(data.get(position).get("day").toString());

        final int[] sign = {Integer.parseInt(data.get(position).get("sign") + "")};


            if (sign[0] == 0)
                holder.img_btn.setImageResource(R.drawable.btn_clock_false);
            else
                holder.img_btn.setImageResource(R.drawable.btn_qinglv_true);


        holder.img_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("qqqqqqqqSSS", sign[0] + "?" + position);

                if (sign[0] == 1) {
                    sign[0] = 0;
                    holder.img_btn.setImageResource(R.drawable.btn_clock_false);
                } else {
                    sign[0] = 1;
                    holder.img_btn.setImageResource(R.drawable.btn_qinglv_true);
                }

            }
        });

        holder.img_btn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showPopup();
                return false;
            }
        });

        holder.rl_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"查看详情"+position,Toast.LENGTH_SHORT).show();
            }
        });

        holder.rl_item.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showPopup();
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_zhuiping:
                context.startActivity(new Intent(context, PingLunActivity.class));
                break;

        }
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    /**
     * ViewHolder的类，用于缓存控件
     */
    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView img_btn;
        TextView tv_time, tv_context, tv_name, tv_day;
        RelativeLayout rl_item;

        public MyViewHolder(View view) {
            super(view);

            img_btn = (ImageView) view.findViewById(R.id.img_btn);
            tv_time = (TextView) view.findViewById(R.id.tv_time);
            tv_context = (TextView) view.findViewById(R.id.tv_context);
            tv_name = (TextView) view.findViewById(R.id.tv_name);
            tv_day = (TextView) view.findViewById(R.id.tv_day);
            rl_item= (RelativeLayout) view.findViewById(R.id.rl_item);

        }


    }




    private View contentViewSign;
    private PopupWindow mPopWindow;
    private TextView tv_quxiao, tv_queding, tv_context;

    private void showPopup() {
        contentViewSign = LayoutInflater.from(context).inflate(R.layout.popup_main, null);
        tv_quxiao = (TextView) contentViewSign.findViewById(R.id.tv_quxiao);
        tv_queding = (TextView) contentViewSign.findViewById(R.id.tv_queren);
        tv_context = (TextView) contentViewSign.findViewById(R.id.tv_context);
        tv_quxiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopWindow.dismiss();
            }
        });
        tv_queding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "删除闹钟成功", Toast.LENGTH_SHORT).show();
                mPopWindow.dismiss();
                notifyDataSetChanged();
                //刷新数据
            }
        });
        ((TextView) contentViewSign.findViewById(R.id.tv_title)).setText("删除闹钟");
        tv_context.setText("是否确认删除闹钟？");
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
}