package com.xr.happyFamily.le.BtClock;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import com.xr.database.dao.daoimpl.TimeDaoImpl;
import com.xr.happyFamily.R;
import com.xr.happyFamily.le.adapter.ChooseRingAdapter;
import com.xr.happyFamily.le.adapter.ChooseTimeAdapter;
import com.xr.happyFamily.le.pojo.Time;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import android.preference.PreferenceActivity;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class clockRingActivity extends Activity {

    @BindView(R.id.colockring_recyc)
    RecyclerView recyclerView;
    private TimeDaoImpl timeDao;
    String[] str = {"阿里郎", "浪人琵琶", "学猫叫", "芙蓉雨", "七月上", "佛系少女", "离人愁", "不仅仅是喜欢", "纸短情长", "远走高飞"};
    private List<String> mData = new ArrayList<String>(Arrays.asList(str));
    ChooseRingAdapter adapter;
    Time time;
    int hour, minutes;
    String userId;
    public static final String ALARM_RINGTONE    = "pref_alarm_ringtone";

    private Preference mAlarmSoundsPref;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_le_clockring);
        ButterKnife.bind(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(clockRingActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new ChooseRingAdapter(clockRingActivity.this,mData);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK){
            int pos = adapter.getLastPos();
            Log.e("pos", "onClick: "+pos );
            if (pos==-1){
                Intent intent = new Intent();
                intent.putExtra("pos",0);
                setResult(111,intent);
            }else {
                Intent intent = new Intent();
                intent.putExtra("pos",pos);
                setResult(111,intent);
            }

            finish();
        }
        return true;
    }

    @OnClick({R.id.iv_ring_fh, R.id.rl_clock_xt})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.iv_ring_fh:
                int pos = adapter.getLastPos();
                Log.e("pos", "onClick: "+pos );
                if (pos==-1){
                    Intent intent = new Intent();
                    intent.putExtra("pos",0);
                    setResult(111,intent);
                }else {
                    Intent intent = new Intent();
                    intent.putExtra("pos",pos);
                    setResult(111,intent);
                }

                finish();
                break;
            case R.id.rl_clock_xt:
                doPickAlarmRingtone();
                break;

        }
    }
    private String alarmStr;
    private static final int ALARM_RINGTONE_PICKED = 3;
    private void doPickAlarmRingtone(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        alarmStr = sharedPreferences.getString(ALARM_RINGTONE, null);

        Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
        // Allow user to pick 'Default'
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true);
        // Show only ringtones
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_ALARM);
        //set the default Notification value
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_DEFAULT_URI, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM));
        // Don't show 'Silent'
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, true);

        Uri alarmUri;
        if (alarmStr != null) {
            alarmUri = Uri.parse(alarmStr);
            // Put checkmark next to the current ringtone for this contact
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, alarmUri);
        } else {
            // Otherwise pick default ringtone Uri so that something is selected.
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
            // Put checkmark next to the current ringtone for this contact
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, alarmUri);
        }

        startActivityForResult(intent, ALARM_RINGTONE_PICKED);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (resultCode != RESULT_OK) {
            return;
        }

        switch (requestCode) {

            case ALARM_RINGTONE_PICKED:{
                Uri pickedUri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
                if(null == pickedUri){

                    editor.putString(ALARM_RINGTONE, null);
                    editor.commit();
                }else{
                    Ringtone ringtone =  RingtoneManager.getRingtone(clockRingActivity.this, pickedUri);
                    String strRingtone = ringtone.getTitle(clockRingActivity.this);
                    editor.putString(ALARM_RINGTONE, pickedUri.toString());
                    editor.commit();

                }
                break;
            }


            default:break;
        }
    }

//
//    private boolean mIsShowing = false;
//    private PopupWindow popupWindow;
//    ImageView image1, image2, image3, image4, image5, image6, image7, image8;
//    RelativeLayout relativeLayout1, relativeLayout2, relativeLayout3, relativeLayout4, relativeLayout5,
//            relativeLayout6, relativeLayout7, relativeLayout8;
//    Button buttonqx, buttonqd;
//    List<String> weeks;
//    String week=" ";
//
//    private void initPopup() {
//        if (popupWindow != null && popupWindow.isShowing()) {
//            return;
//        }
//        weeks=new ArrayList<>();
//        View parent = ((ViewGroup) this.findViewById(android.R.id.content)).getChildAt(0);
//        View pop = View.inflate(this, R.layout.fragment_addday_poup, null);
//        image1 = (ImageView) pop.findViewById(R.id.iv_add_day1);
//        image2 = (ImageView) pop.findViewById(R.id.iv_add_day2);
//        image3 = (ImageView) pop.findViewById(R.id.iv_add_day3);
//        image4 = (ImageView) pop.findViewById(R.id.iv_add_day4);
//        image5 = (ImageView) pop.findViewById(R.id.iv_add_day5);
//        image6 = (ImageView) pop.findViewById(R.id.iv_add_day6);
//        image7 = (ImageView) pop.findViewById(R.id.iv_add_day7);
//        image8 = (ImageView) pop.findViewById(R.id.iv_add_no);
//        relativeLayout1 = (RelativeLayout) pop.findViewById(R.id.rl_addday_r1);
//        relativeLayout2 = (RelativeLayout) pop.findViewById(R.id.rl_addday_r2);
//        relativeLayout3 = (RelativeLayout) pop.findViewById(R.id.rl_addday_r3);
//        relativeLayout4 = (RelativeLayout) pop.findViewById(R.id.rl_addday_r4);
//        relativeLayout5 = (RelativeLayout) pop.findViewById(R.id.rl_addday_r5);
//        relativeLayout6 = (RelativeLayout) pop.findViewById(R.id.rl_addday_r6);
//        relativeLayout7 = (RelativeLayout) pop.findViewById(R.id.rl_addday_r7);
//        relativeLayout8 = (RelativeLayout) pop.findViewById(R.id.rl_addday_r8);
//        buttonqd = (Button) pop.findViewById(R.id.bt_addday_qd);
//        buttonqx = (Button) pop.findViewById(R.id.bt_addday_qx);
//        image1.setTag("close");
//        image2.setTag("close");
//        image3.setTag("close");
//        image4.setTag("close");
//        image5.setTag("close");
//        image6.setTag("close");
//        image7.setTag("close");
//        image8.setTag("open");
//        image8.setImageResource(R.mipmap.lrclock_dh);
////        popupWindow = new PopupWindow(pop, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        popupWindow = new PopupWindow(pop, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
//        //点击空白处时，隐藏掉pop窗口
//        popupWindow.setFocusable(true);
//        popupWindow.setTouchable(true);
//        popupWindow.setOutsideTouchable(true);
////        popupWindow.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
//        ColorDrawable dw = new ColorDrawable(0x30000000);
//        popupWindow.setBackgroundDrawable(dw);
//        popupWindow.setAnimationStyle(R.style.Popupwindow);
//        popupWindow.showAtLocation(parent, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
////        mIsShowing = false;
//        View.OnClickListener listener = new View.OnClickListener() {
//            public void onClick(View v) {
//                switch (v.getId()) {
//                    case R.id.bt_addday_qx:
//                        popupWindow.dismiss();
//                        weeks.clear();
//                        break;
//                    case R.id.bt_addday_qd:
//                        weeks.clear();
//                        if ("open".equals(image1.getTag())) {
//                            weeks.add(" 周一 ");
//                        }
//                        if ("open".equals(image2.getTag())) {
//                            weeks.add(" 周二 ");
//                        }
//                        if ("open".equals(image3.getTag())) {
//                            weeks.add(" 周三 ");
//                        }
//                        if ("open".equals(image4.getTag())) {
//                            weeks.add(" 周四 ");
//                        }
//                        if ("open".equals(image5.getTag())) {
//                            weeks.add(" 周五 ");
//                        }
//                        if ("open".equals(image6.getTag())) {
//                            weeks.add(" 周六 ");
//                        }
//                        if ("open".equals(image7.getTag())) {
//                            weeks.add(" 周日 ");
//                        }
//                        if ("open".equals(image8.getTag())) {
//                            weeks.add(" 不重复 ");
//                        }
//                        for (int i=0;i<weeks.size();i++){
//                            week += weeks.get(i);
//                        }
//                        tv_lesd_week.setText(week);
//                        popupWindow.dismiss();
//                        break;
//                    case R.id.rl_addday_r8:
//                        if ("close".equals(image8.getTag())) {
//                            image8.setImageResource(R.mipmap.lrclock_dh);
//                            image1.setImageResource(0);
//                            image2.setImageResource(0);
//                            image3.setImageResource(0);
//                            image4.setImageResource(0);
//                            image5.setImageResource(0);
//                            image6.setImageResource(0);
//                            image7.setImageResource(0);
//                            image8.setTag("open");
//                            image7.setTag("close");
//                            image6.setTag("close");
//                            image5.setTag("close");
//                            image4.setTag("close");
//                            image3.setTag("close");
//                            image2.setTag("close");
//                            image1.setTag("close");
//
//                        } else if ("open".equals(image8.getTag())) {
//                            image8.setImageResource(0);
//                            image8.setTag("close");
//                        }
//
//                        break;
//                    case R.id.rl_addday_r7:
//                        if ("close".equals(image7.getTag())) {
//                            image8.setImageResource(0);
//                            image7.setImageResource(R.mipmap.lrclock_dh);
//                            image7.setTag("open");
//                        } else if ("open".equals(image7.getTag())) {
//                            image7.setImageResource(0);
//                            image7.setTag("close");
//                        }
//                        break;
//                    case R.id.rl_addday_r1:
//                        if ("close".equals(image1.getTag())) {
//                            image8.setImageResource(0);
//                            image1.setImageResource(R.mipmap.lrclock_dh);
//                            image1.setTag("open");
//                        } else if ("open".equals(image1.getTag())) {
//                            image1.setImageResource(0);
//                            image1.setTag("close");
//                        }
//                        break;
//                    case R.id.rl_addday_r2:
//                        if ("close".equals(image2.getTag())) {
//                            image8.setImageResource(0);
//                            image2.setImageResource(R.mipmap.lrclock_dh);
//                            image2.setTag("open");
//                        } else if ("open".equals(image2.getTag())) {
//                            image2.setImageResource(0);
//                            image2.setTag("close");
//                        }
//                        break;
//                    case R.id.rl_addday_r3:
//                        if ("close".equals(image3.getTag())) {
//                            image8.setImageResource(0);
//                            image3.setImageResource(R.mipmap.lrclock_dh);
//                            image3.setTag("open");
//                        } else if ("open".equals(image3.getTag())) {
//                            image3.setImageResource(0);
//                            image3.setTag("close");
//                        }
//                        break;
//                    case R.id.rl_addday_r4:
//                        if ("close".equals(image4.getTag())) {
//                            image8.setImageResource(0);
//                            image4.setImageResource(R.mipmap.lrclock_dh);
//                            image4.setTag("open");
//                        } else if ("open".equals(image4.getTag())) {
//                            image4.setImageResource(0);
//                            image4.setTag("close");
//                        }
//                        break;
//                    case R.id.rl_addday_r5:
//                        if ("close".equals(image5.getTag())) {
//                            image8.setImageResource(0);
//                            image5.setImageResource(R.mipmap.lrclock_dh);
//                            image5.setTag("open");
//                        } else if ("open".equals(image5.getTag())) {
//                            image5.setImageResource(0);
//                            image5.setTag("close");
//                        }
//                        break;
//                    case R.id.rl_addday_r6:
//                        if ("close".equals(image6.getTag())) {
//                            image8.setImageResource(0);
//                            image6.setImageResource(R.mipmap.lrclock_dh);
//                            image6.setTag("open");
//                        } else if ("open".equals(image6.getTag())) {
//                            image6.setImageResource(0);
//                            image6.setTag("close");
//                        }
//                        break;
//                }
//            }
//        };
//        relativeLayout1.setOnClickListener(listener);
//        relativeLayout2.setOnClickListener(listener);
//        relativeLayout3.setOnClickListener(listener);
//        relativeLayout4.setOnClickListener(listener);
//        relativeLayout5.setOnClickListener(listener);
//        relativeLayout6.setOnClickListener(listener);
//        relativeLayout7.setOnClickListener(listener);
//        relativeLayout8.setOnClickListener(listener);
//        buttonqd.setOnClickListener(listener);
//        buttonqx.setOnClickListener(listener);
//    }


}
