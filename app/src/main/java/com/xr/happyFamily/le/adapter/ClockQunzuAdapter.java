package com.xr.happyFamily.le.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Message;
import android.os.Parcelable;
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

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.xr.database.dao.daoimpl.ClockDaoImpl;
import com.xr.database.dao.daoimpl.UserInfosDaoImpl;
import com.xr.happyFamily.R;
import com.xr.happyFamily.bao.PingLunActivity;
import com.xr.happyFamily.bao.base.ToastUtil;
import com.xr.happyFamily.le.ClockActivity;
import com.xr.happyFamily.le.bean.ClickFriendBean;
import com.xr.happyFamily.le.clock.QunzuAddActivity;
import com.xr.happyFamily.le.clock.QunzuEditActivity;
import com.xr.happyFamily.le.fragment.QunZuFragment;
import com.xr.happyFamily.le.pojo.ClockBean;
import com.xr.happyFamily.le.pojo.UserInfo;
import com.xr.happyFamily.login.login.LoginActivity;
import com.xr.happyFamily.together.MyDialog;
import com.xr.happyFamily.together.RoundTransform;
import com.xr.happyFamily.together.http.HttpUtils;
import com.xr.happyFamily.together.util.GlideCircleTransform;
import com.xr.happyFamily.together.util.TimeUtils;
import com.xr.happyFamily.together.util.Utils;
import com.xr.happyFamily.together.util.mqtt.ClockService;
import com.xr.happyFamily.together.util.mqtt.MQService;

import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

//快递列表适配器

public class ClockQunzuAdapter extends RecyclerView.Adapter<ClockQunzuAdapter.MyViewHolder> implements View.OnClickListener {
    private ClockActivity context;
    private ButtonInterface buttonInterface;
    List<ClockBean> clockBeanList;
    List<UserInfo> userInfoList;
    private int defItem = -1;

    private int type = 0;
    private ClockDaoImpl clockBeanDao;
    private UserInfosDaoImpl userInfosDao;
    private String userId;

    MQService mqService;
    ClockService clockService;

    public ClockQunzuAdapter(ClockActivity context, List<ClockBean> clockBeanList, String userId, MQService mqService,   ClockService clockService) {
        this.context = context;
        this.clockBeanList = clockBeanList;
        this.userId = userId;
        this.mqService = mqService;
        this.clockService=clockService;
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
                context).inflate(R.layout.item_clock_qunzu, parent,
                false));
        userInfosDao = new UserInfosDaoImpl(context.getApplicationContext());
        clockBeanDao=new ClockDaoImpl(context.getApplicationContext());
        return holder;
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {


        userInfoList = userInfosDao.findUserInfoByClockId(clockBeanList.get(position).getClockId());
        String hourStr = "";
        String mumber = "";
        int hour = clockBeanList.get(position).getClockHour();
        if (hour < 10) {
            hourStr = "0" + hour;
        } else hourStr = hour + "";
        String minStr = "";
        int min = clockBeanList.get(position).getClockMinute();
        if (min < 10) {
            minStr = "0" + min;
        } else minStr = min + "";
        holder.tv_time.setText(hourStr + ":" + minStr);
        holder.tv_context.setText(clockBeanList.get(position).getFlag());


//            for(int j=0;j<userInfoList.size();j++){
//                if (userInfoList.get(j).getClockId()==clockBeanList.get(position).getClockId())
//                    myUserInfoList.add(userInfoList.get(j));
//            }

        int size = 0;
        if (userInfoList.size() < 5) {
            size = userInfoList.size();
        } else
            size = 5;
        for (int a = 0; a < 5; a++)
            holder.imgs[a].setVisibility(View.GONE);
        for (int i = 0; i < size; i++) {
            mumber = mumber + userInfoList.get(i).getUserId() + ",";
            holder.imgs[i].setVisibility(View.VISIBLE);
            if (Utils.isEmpty(userInfoList.get(i).getHeadImgUrl())) {
                Log.e("qqqqqqqqTTTT","1111,"+userInfoList.get(i).getUsername());
                holder.imgs[i].setImageResource(R.mipmap.ic_touxiang_moren);
            }
            else {
                Log.e("qqqqqqqqTTTT",userInfoList.get(i).getHeadImgUrl()+","+userInfoList.get(i).getUsername());
                Glide.with(context).load(userInfoList.get(i).getHeadImgUrl()).transform(new GlideCircleTransform(context.getApplicationContext())).error(R.mipmap.ic_touxiang_moren).into(holder.imgs[i]);
            }
        }
        if (!(clockBeanList.get(position).getClockCreater() + "").equals(userId)) {
            holder.img_btn.setVisibility(View.GONE);
            if (clockBeanList.get(position).getSwitchs() == 0) {
                holder.tv_context.setTextColor(Color.parseColor("#CECECE"));
                holder.tv_time.setTextColor(Color.parseColor("#828282"));
            }
        } else {
            Log.e("qqqqqKKKKK",clockBeanList.get(position).getSwitchs()+"");
            final int[] sign = {clockBeanList.get(position).getSwitchs()};
            if (clockBeanList.get(position).getSwitchs() == 0)
                holder.img_btn.setImageResource(R.drawable.btn_clock_false);
            else
                holder.img_btn.setImageResource(R.mipmap.btn_clock_qunzu);
            final String finalMumber = mumber.substring(0, mumber.length() - 1);
            holder.img_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clockId = clockBeanList.get(position).getClockId();
                    upPos = position;
                    if (clockBeanList.get(position).getSwitchs() == 1) {
                        upState = 0;
                        upSwitch(finalMumber, position, 0, 2);
                    } else {
                        upState = 1;
                        upSwitch(finalMumber, position, 1, 2);
                    }
                }
            });
            holder.rl_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, QunzuEditActivity.class);
                    intent.putExtra("clock", clockBeanList.get(position));
                    intent.putExtra("uesr", finalMumber);
                    context.startActivity(intent);
                }
            });
            holder.rl_item.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    showPopup(finalMumber, position);
                    return false;
                }
            });
        }
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
        return clockBeanList.size();
    }

    /**
     * ViewHolder的类，用于缓存控件
     */
    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView img_btn, img1, img2, img3, img4, img5;
        ImageView[] imgs;
        TextView tv_time, tv_context;
        RelativeLayout rl_item;

        public MyViewHolder(View view) {
            super(view);

            img_btn = (ImageView) view.findViewById(R.id.img_btn);
            img1 = (ImageView) view.findViewById(R.id.img1);
            img2 = (ImageView) view.findViewById(R.id.img2);
            img3 = (ImageView) view.findViewById(R.id.img3);
            img4 = (ImageView) view.findViewById(R.id.img4);
            img5 = (ImageView) view.findViewById(R.id.img5);
            tv_time = (TextView) view.findViewById(R.id.tv_time);
            imgs = new ImageView[]{img1, img2, img3, img4, img5};
            tv_context = (TextView) view.findViewById(R.id.tv_context);

            rl_item = (RelativeLayout) view.findViewById(R.id.rl_item);

        }


    }


    private View contentViewSign;
    private PopupWindow mPopWindow;
    private TextView tv_quxiao, tv_queding, tv_context;

    private void showPopup(final String mum, final int pos) {
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
                delPos = pos;
                clockCreater = clockBeanList.get(pos).getClockCreater();
                clockId = clockBeanList.get(pos).getClockId();
                upSwitch(mum, pos, 0, 3);
                new deleteClock().execute();

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


    private int clockCreater, clockId, delPos;

    class deleteClock extends AsyncTask<Map<String, Object>, Void, String> {
        @Override
        protected String doInBackground(Map<String, Object>... maps) {
            String url = "/happy/clock/deleteClock";
            url = url + "?clockCreater=" + clockCreater + "&clockId=" + clockId;
            String result = HttpUtils.doGet(context, url);
            Log.e("qqqqqqqRRR", result);
            String code = "";
            try {
                if (!Utils.isEmpty(result)) {
                    if (result.length() < 6) {
                        code = result;
                    }
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
                Toast.makeText(context, "删除闹钟成功", Toast.LENGTH_SHORT).show();
                mPopWindow.dismiss();
            } else if (!Utils.isEmpty(s) && "401".equals(s)) {
                Toast.makeText(context.getApplicationContext(), "用户信息超时请重新登陆", Toast.LENGTH_SHORT).show();
                SharedPreferences preferences;
                preferences = context.getSharedPreferences("my", MODE_PRIVATE);
                MyDialog.setStart(false);
                if (preferences.contains("password")) {
                    preferences.edit().remove("password").commit();
                }
                context.startActivity(new Intent(context.getApplicationContext(), LoginActivity.class));
            }
        }
    }

    public void setMqService(MQService mqService) {
        this.mqService = mqService;
        notifyDataSetChanged();
    }


    ClockBean clockBean;
    public void upSwitch(String mum, int position, int state, int del) {
        SharedPreferences preferences;
        preferences = context.getSharedPreferences("my", MODE_PRIVATE);
//                    holder.img_btn.setImageResource(R.drawable.btn_clock_false);
        String image = preferences.getString("headImgUrl", "");
        String username = preferences.getString("username", "");
        String phone = preferences.getString("phone", "");
        boolean sex = preferences.getBoolean("sex", false);
        String birthday = preferences.getString("birthday", "");

        String date = TimeUtils.getTime(birthday);
        String str = date.substring(0, 4);
        Calendar c = Calendar.getInstance();//
        int age = c.get(Calendar.YEAR) - Integer.parseInt(str) + 1;
        List<ClickFriendBean> userInfos = new ArrayList<>();
        ClickFriendBean myInfo = new ClickFriendBean();

        clockBean = clockBeanList.get(position);
        Map map = new HashMap();
        map.put("state", del);
        map.put("clockId", clockBean.getClockId());
        map.put("clockHour", clockBean.getClockHour());
        map.put("clockMinute", clockBean.getClockMinute());
        map.put("clockDay", "0");
        map.put("flag", clockBean.getFlag());
        map.put("music", clockBean.getMusic());
        map.put("switchs", state);
        clockBean.setSwitchs(state);
        map.put("clockCreater", userId);
        map.put("clockType", 2);
        long timeStampSec = System.currentTimeMillis() / 1000;
        String timestamp = String.format("%010d", timeStampSec);
        map.put("createrName", username);
        long createTime = Long.parseLong(timestamp);
        map.put("createTime", createTime);
        if (Utils.isEmpty(image)) {
            myInfo.setHeadImgUrl("null");
        } else {
            myInfo.setHeadImgUrl(image);
        }
        myInfo.setMemSign(0);
        myInfo.setAge(age);
        myInfo.setPhone(phone);
        myInfo.setSex(sex);
        myInfo.setUserId(Integer.parseInt(userId));
        myInfo.setUsername(username);
        userInfos.add(myInfo);
        String[] mums = mum.split(",");
        UserInfosDaoImpl userInfosDao = new UserInfosDaoImpl(context.getApplicationContext());
        UserInfo userInfo1;
        for (int i = 1; i < mums.length; i++) {
            List<UserInfo> upUserInfoList = userInfosDao.findUserInfoByUserId(mums[i]);
            userInfo1 = upUserInfoList.get(0);
            ClickFriendBean userInfo = new ClickFriendBean();
            if (Utils.isEmpty(userInfo1.getHeadImgUrl())) {
                userInfo.setHeadImgUrl("null");
            }else
                userInfo.setHeadImgUrl(userInfo1.getHeadImgUrl());
            userInfo.setMemSign(0);
            userInfo.setAge(userInfo1.getAge());
            userInfo.setPhone(userInfo1.getPhone());
            userInfo.setSex(userInfo1.getSex());
            userInfo.setUserId(userInfo1.getUserId());
            userInfo.setUsername(userInfo1.getUsername());
            userInfos.add(userInfo);
            map.put("userInfos", userInfos);
        }
        String macAddress = JSON.toJSONString(map, true);
        new addMqttAsync().execute(macAddress);
    }

    private int upPos, upState;

    private class addMqttAsync extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... macs) {
            String macAddress = macs[0];
            boolean step2 = false;
            if (mqService != null) {
                String topicName = "p99/2_" + clockId + "/clockuniversal";
                step2 = mqService.publish(topicName, 1, macAddress);
                Log.e("qqqqqqSSSS1111", step2 + "?" + topicName + "," + macAddress);
            }
            return step2;
        }

        @Override
        protected void onPostExecute(Boolean s) {
            super.onPostExecute(s);
            if (s) {
                Log.e("qqqqccccc",clockBean.getSwitchs()+"?");
                clockBeanDao.update(clockBean);
                if(clockService!=null)
                    clockService.startClock();
                else {
                    Log.e("qqqqTAG222",s+"1111");
                }
                if (upState == 0) {
                    clockBeanList.get(upPos).setSwitchs(0);
                } else
                    clockBeanList.get(upPos).setSwitchs(1);

            } else
                ToastUtil.showShortToast("请检查网络");
        }
    }


    public void setClockService(ClockService clockService){
        this.clockService=clockService;
        notifyDataSetChanged();
    }
}