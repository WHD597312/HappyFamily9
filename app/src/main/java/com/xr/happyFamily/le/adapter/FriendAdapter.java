package com.xr.happyFamily.le.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
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

import com.bumptech.glide.Glide;
import com.xr.happyFamily.R;
import com.xr.happyFamily.bao.PingLunActivity;
import com.xr.happyFamily.le.FriendActivity;
import com.xr.happyFamily.le.bean.ClickFriendBean;
import com.xr.happyFamily.le.pojo.UserInfo;
import com.xr.happyFamily.together.http.HttpUtils;
import com.xr.happyFamily.together.util.GlideCircleTransform;
import com.xr.happyFamily.together.util.Utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//快递列表适配器

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.MyViewHolder> implements View.OnClickListener {
    private FriendActivity context;
    private List<ClickFriendBean> data;
    private ButtonInterface buttonInterface;
    private int selPosition = -1;
    private int defItem = -1;
    private OnItemListener onItemListener;
    private int type = 0;
    boolean isFirst = true;
    private String userId;

    public FriendAdapter(FriendActivity context, List<ClickFriendBean> list,int type,String userId) {
        this.context = context;
        this.data = list;
        this.type=type;
        this.userId=userId;
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
                context).inflate(R.layout.item_friend, parent,
                false));
        return holder;
    }


    public void setSelection(int position) {
        this.selPosition = position;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        if (Utils.isEmpty(data.get(position).getHeadImgUrl())) {

            holder.img_touxiang.setImageResource(R.mipmap.ic_touxiang_moren);
        } else
            Glide.with(context).load(data.get(position).getHeadImgUrl()).transform(new GlideCircleTransform(context.getApplicationContext())).error(R.mipmap.ic_touxiang_moren).into(holder.img_touxiang);

        holder.tv_name.setText(data.get(position).getUsername().toString());
        holder.tv_tel.setText(data.get(position).getPhone().toString());


//        holder.tv_context.setText(data.get(position).get("context").toString());
        final int[] sign = {0};


        if(type==1000)
            holder.img_choose.setVisibility(View.GONE);
        else
            holder.img_choose.setVisibility(View.VISIBLE);

        if (selPosition == position) {
            holder.img_choose.setImageResource(R.mipmap.ic_friend_true);
            data.get(position).setMemSign(1);
        } else {
            holder.img_choose.setImageResource(R.mipmap.ic_friend_false);
            data.get(position).setMemSign(0);
        }

        if (isFirst) {
            Log.e("qqqqqqqqqIIIII222", loveId + "????");
            if (data.get(position).getUserId() == loveId) {
                isFirst = false;
                sign[0] = 1;
                this.selPosition = position;
                holder.img_choose.setImageResource(R.mipmap.ic_friend_true);
                data.get(position).setMemSign(1);
            }
        }


        holder.rl_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(type==1001) {
                    setSelection(position);
                    if (sign[0] == 1) {
                        sign[0] = 0;
                        holder.img_choose.setImageResource(R.mipmap.ic_friend_false);
                    } else {
                        sign[0] = 1;
                        holder.img_choose.setImageResource(R.mipmap.ic_friend_true);
                    }
                }

            }
        });

        holder.rl_item.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showPopup(data, position);
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

        ImageView img_touxiang;
        TextView tv_name,tv_tel;
        ImageView img_choose;
        RelativeLayout rl_item;


        public MyViewHolder(View view) {
            super(view);

            img_touxiang = (ImageView) view.findViewById(R.id.img_touxiang);
            tv_name = (TextView) view.findViewById(R.id.tv_name);
            tv_tel = (TextView) view.findViewById(R.id.tv_tel);
            img_choose = (ImageView) view.findViewById(R.id.img_choose);
            rl_item= (RelativeLayout) view.findViewById(R.id.rl_item);


        }


    }


    public String getMember() {
        String member = "0";
        if (selPosition != -1) {
            member = data.get(selPosition).getUserId() + "";
        }
        return member;
    }


    public int getItem() {
        return selPosition;
    }

    private int loveId = -1;

    public void setUserId(int loveId) {
        this.loveId = loveId;
        notifyDataSetChanged();
    }

    private View contentViewSign;
    private PopupWindow mPopWindow;
    private TextView tv_quxiao, tv_queding, tv_context;

    private void showPopup(final List<ClickFriendBean> myUserInfoList, final int pos) {
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
//                delPos = pos;
//                clockCreater = clockBeanList.get(pos).getClockCreater();
//                clockId = clockBeanList.get(pos).getClockId();
//                upSwitch(myUserInfoList, pos, 0, 3);
//                new ClockQinglvAdapter.deleteClock().execute();
//                //刷新数据
            }
        });
        ((TextView) contentViewSign.findViewById(R.id.tv_title)).setText("删除好友");
        tv_context.setText("是否删除好友吗？");
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