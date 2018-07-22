package com.xr.happyFamily.le.adapter;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.IBinder;
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

import com.alibaba.fastjson.JSON;
import com.squareup.picasso.Picasso;
import com.xr.database.dao.daoimpl.ClockDaoImpl;
import com.xr.database.dao.daoimpl.UserInfosDaoImpl;
import com.xr.happyFamily.R;
import com.xr.happyFamily.bao.PingLunActivity;
import com.xr.happyFamily.bao.adapter.DingdanAdapter;
import com.xr.happyFamily.bean.ShopCartBean;
import com.xr.happyFamily.le.ClockActivity;
import com.xr.happyFamily.le.bean.ClickFriendBean;
import com.xr.happyFamily.le.clock.QinglvAddActivity;
import com.xr.happyFamily.le.clock.QinglvEditActivity;
import com.xr.happyFamily.le.fragment.QingLvFragment;
import com.xr.happyFamily.le.pojo.ClockBean;
import com.xr.happyFamily.le.pojo.UserInfo;
import com.xr.happyFamily.login.login.LoginActivity;
import com.xr.happyFamily.together.MyDialog;
import com.xr.happyFamily.together.http.HttpUtils;
import com.xr.happyFamily.together.util.Utils;
import com.xr.happyFamily.together.util.mqtt.MQService;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

//快递列表适配器

public class ClockQinglvAdapter extends RecyclerView.Adapter<ClockQinglvAdapter.MyViewHolder> implements View.OnClickListener {
    private ClockActivity context;
    private ButtonInterface buttonInterface;
    List<ClockBean> clockBeanList;
    List<UserInfo> userInfoList;
    List<UserInfo> allInfoList;
    private int defItem = -1;

    private int type = 0;
    private ClockDaoImpl clockBeanDao;
    private UserInfosDaoImpl userInfosDao;
    private String userId;

    MQService mqService;

    public ClockQinglvAdapter(ClockActivity context, List<ClockBean> clockBeanList,  String userId, MQService mqService) {
        this.context = context;
        this.clockBeanList = clockBeanList;

        this.userId = userId;
        this.mqService = mqService;

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

        userInfosDao = new UserInfosDaoImpl(context.getApplicationContext());
        userInfoList=userInfosDao.findUserInfoByClockId(clockBeanList.get(position).getClockId());

        Log.e("qqqqqqqqWWWW",userInfosDao.findAll().size()+"ppppppppp");

        String hourStr = "";
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





        final int[] sign = {Integer.parseInt(clockBeanList.get(position).getSwitchs() + "")};


        Log.e("qqqqqqqqWWWW", clockBeanList.get(position).getClockId()+"???"+userInfoList.size());

        if (!(clockBeanList.get(position).getClockCreater() + "").equals(userId)) {
            holder.tv_name.setText(userInfoList.get(0).getUsername());
            holder.img_btn.setVisibility(View.GONE);
            if (clockBeanList.get(position).getSwitchs() == 0) {
                holder.tv_context.setTextColor(Color.parseColor("#CECECE"));
                holder.tv_name.setTextColor(Color.parseColor("#828282"));
                holder.tv_time.setTextColor(Color.parseColor("#828282"));
            }
        } else {
            holder.tv_name.setText(userInfoList.get(1).getUsername());
            Log.e("qqqqqqqqqqWWWWW",clockBeanList.get(position).getSwitchs()+"??"+clockBeanList.get(position).getClockMinute());
            if (clockBeanList.get(position).getSwitchs() == 0) {
                holder.img_btn.setImageResource(R.drawable.btn_clock_false);
            } else {
                holder.img_btn.setImageResource(R.drawable.btn_qinglv_true);

            }
            holder.rl_item.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    showPopup(userInfoList,position);
                    return false;
                }
            });

            holder.rl_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, QinglvEditActivity.class);
                    intent.putExtra("clock", clockBeanList.get(position));
                    Log.e("qqqqqqqIIII11111",userInfoList.get(1).getUserId()+"???");
                    intent.putExtra("loveId", userInfoList.get(1).getUserId());
                    context.startActivity(intent);
                }
            });
        }


        holder.img_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("qqqqqqqqSSS", sign[0] + "?" + position);
                clockId = clockBeanList.get(position).getClockId();
                upPos = position;
                if (clockBeanList.get(position).getSwitchs() == 1) {
                    upState = 0;
                    upSwitch(userInfoList, position, 0,2);

                } else {
                    upState = 1;
                    upSwitch(userInfoList, position, 1,2);
                }

            }
        });



    }

    public void upSwitch(List<UserInfo> myUserInfoList, int position, int state,int del) {
        SharedPreferences preferences;
        preferences = context.getSharedPreferences("my", MODE_PRIVATE);
//                    holder.img_btn.setImageResource(R.drawable.btn_clock_false);
        String image = preferences.getString("image", "");
        String username = preferences.getString("username", "");
        String phone = preferences.getString("phone", "");
        boolean sex = preferences.getBoolean("sex", false);
        String birthday = preferences.getString("birthday", "");
        String str = birthday.substring(0, 4);
        Calendar c = Calendar.getInstance();//
        int age = c.get(Calendar.YEAR) - Integer.parseInt(str) + 1;

        List<ClickFriendBean> userInfos = new ArrayList<>();
        ClickFriendBean userInfo = new ClickFriendBean();
        ClickFriendBean myInfo = new ClickFriendBean();
        if (Utils.isEmpty(userInfo.getHeadImgUrl())) {
            userInfo.setHeadImgUrl("null");
        }
        ClockBean clockBean = clockBeanList.get(position);
        UserInfo userInfo1 = myUserInfoList.get(1);
        Map map = new HashMap();
        map.put("state", del);
        map.put("clockId", clockBean.getClockId());
        map.put("clockHour", clockBean.getClockHour());
        map.put("clockMinute", clockBean.getClockMinute());
        map.put("clockDay", "0");
        map.put("flag", clockBean.getFlag());
        map.put("music", clockBean.getMusic());
        map.put("switchs", state);
        if (Utils.isEmpty(image)) {
            myInfo.setHeadImgUrl("null");
        }
        myInfo.setMemSign(0);
        myInfo.setAge(age);
        myInfo.setPhone(phone);
        myInfo.setSex(sex);
        myInfo.setUserId(Integer.parseInt(userId));
        myInfo.setUsername(username);

        userInfo.setMemSign(0);
        userInfo.setAge(userInfo1.getAge());
        userInfo.setPhone(userInfo1.getPhone());
        userInfo.setSex(userInfo1.getSex());
        userInfo.setUserId(userInfo1.getUserId());
        userInfo.setUsername(userInfo1.getUsername());

        userInfos.add(myInfo);
        userInfos.add(userInfo);

        map.put("userInfos", userInfos);
        map.put("clockCreater", userId);
        map.put("clockType", 3);

        long timeStampSec = System.currentTimeMillis()/1000;
        String timestamp = String.format("%010d", timeStampSec);
        map.put("createrName", username);
        long createTime=Long.parseLong(timestamp);
        map.put("createTime", createTime);
        String macAddress = JSON.toJSONString(map, true);
        new addMqttAsync().execute(macAddress);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_zhuiping:
                context.startActivity(new Intent(context, PingLunActivity.class));
                break;

        }
    }


//    public void getData() {
//        clockBeanList = new ArrayList<>();
//        clockBeanDao = new ClockDaoImpl(context.getApplicationContext());
//        userInfosDao = new UserInfosDaoImpl(context.getApplicationContext());
//        List<ClockBean> allClockList = clockBeanDao.findAll();
//        for (int i = 0; i < allClockList.size(); i++) {
//            if (allClockList.get(i).getClockType() == 3)
//                clockBeanList.add(allClockList.get(i));
//        }
//
//        userInfoList = userInfosDao.findAll();
//
////        List<ClockBean> test=clockBeanDao.findClockByClockId(clockBeanList.get(upPos).getClockId());
////        Log.e("qqqqqqqqqqWWWWW??11111",test.get(0).getSwitchs()+"????");
//    }


    @Override
    public int getItemCount() {
        return clockBeanList.size();
    }

    /**
     * ViewHolder的类，用于缓存控件
     */
    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView img_btn;
        TextView tv_time, tv_context, tv_name;
        RelativeLayout rl_item;

        public MyViewHolder(View view) {
            super(view);

            img_btn = (ImageView) view.findViewById(R.id.img_btn);
            tv_time = (TextView) view.findViewById(R.id.tv_time);
            tv_context = (TextView) view.findViewById(R.id.tv_context);
            tv_name = (TextView) view.findViewById(R.id.tv_name);
            rl_item = (RelativeLayout) view.findViewById(R.id.rl_item);

        }


    }


    private View contentViewSign;
    private PopupWindow mPopWindow;
    private TextView tv_quxiao, tv_queding, tv_context;

    private void showPopup(final List<UserInfo> myUserInfoList, final int pos) {
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
                upSwitch(myUserInfoList, pos, 0,3);
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
//                clockBeanDao.delete(clockBeanList.get(delPos));
//                clockBeanList.remove(clockBeanList.get(delPos));
//                context.upQinglvData();
                mPopWindow.dismiss();
//                notifyDataSetChanged();
            }else if (!Utils.isEmpty(s) && "401".equals(s)) {
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


//    MQService mqService;
//    private boolean bound = false;
//    private String deviceName;
//    public ServiceConnection connection = new ServiceConnection() {
//        @Override
//        public void onServiceConnected(ComponentName name, IBinder service) {
//            MQService.LocalBinder binder = (MQService.LocalBinder) service;
//            mqService = binder.getService();
//            bound = true;
//
//
//        }
//
//        @Override
//        public void onServiceDisconnected(ComponentName name) {
//            bound = false;
//        }
//    };

    private int upPos, upState;

    private class addMqttAsync extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... macs) {
            String macAddress = macs[0];
            boolean step2 = false;
            if (mqService != null) {
                String topicName = "p99/3_" + clockId + "/clockuniversal";
                step2 = mqService.publish(topicName, 1, macAddress);

                Log.e("qqqqqqSSSS1111", step2 + "?"+topicName+","+macAddress);
            }
            return step2;
        }

        @Override
        protected void onPostExecute(Boolean s) {
            super.onPostExecute(s);

            Log.e("qqqqqqSSSS", s + "?");
            if (s) {
                if (upState == 0) {
                    clockBeanList.get(upPos).setSwitchs(0);
                } else
                    clockBeanList.get(upPos).setSwitchs(1);

//                clockBeanDao.update(clockBeanList.get(upPos));

//                List<ClockBean> test=clockBeanDao.findClockByClockId(clockBeanList.get(upPos).getClockId());
//                Log.e("qqqqqqqqqqWWWWW??",test.get(0).getSwitchs()+"????");
//                setDao();
            } else
                Toast.makeText(context, "请检查网络", Toast.LENGTH_SHORT).show();
        }
    }


    public void setMqService(MQService mqService) {
        this.mqService=mqService;
        notifyDataSetChanged();

    }

//    public void setDao() {
//       getData();
//        notifyDataSetChanged();
//
//    }
}