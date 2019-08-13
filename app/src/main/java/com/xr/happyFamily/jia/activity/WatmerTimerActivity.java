package com.xr.happyFamily.jia.activity;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xr.happyFamily.R;
import com.xr.happyFamily.bao.base.ToastUtil;
import com.xr.happyFamily.jia.pojo.TimerTask;
import com.xr.happyFamily.jia.view_custom.ChangeDialog;
import com.xr.happyFamily.jia.view_custom.DialogLoad;
import com.xr.happyFamily.jia.view_custom.MyDecoration;
import com.xr.happyFamily.main.MainActivity;
import com.xr.happyFamily.together.http.BaseWeakAsyncTask;
import com.xr.happyFamily.together.util.Utils;
import com.xr.happyFamily.together.util.mqtt.MQService;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class WatmerTimerActivity extends AppCompatActivity {

    Unbinder unbinder;
    @BindView(R.id.rv_timer)
    RecyclerView rv_timer;
    @BindView(R.id.tv_week1)
    TextView tv_week1;
    @BindView(R.id.tv_week2)
    TextView tv_week2;
    @BindView(R.id.tv_week3)
    TextView tv_week3;
    @BindView(R.id.tv_week4)
    TextView tv_week4;
    @BindView(R.id.tv_week5)
    TextView tv_week5;
    @BindView(R.id.tv_week6)
    TextView tv_week6;
    @BindView(R.id.tv_week7)
    TextView tv_week7;
    TextView[] weekTv = new TextView[7];
    private List<TimerTask> list = new ArrayList<>();
    TimerAdapter adapter;
    String deviceMac;
    int week;
    int initLoad = 0;
    @BindView(R.id.tv_copy)
    TextView tv_copy;

    private long houseId;//家的id
    public static boolean running = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watmer_timer);
        unbinder = ButterKnife.bind(this);
        Intent intent = getIntent();
        deviceMac = intent.getStringExtra("deviceMac");
        houseId = intent.getLongExtra("houseId", 0);
        weekTimerTasks = (List<List<TimerTask>>) intent.getSerializableExtra("weekTimerTasks");
        weekTv[0] = tv_week1;
        weekTv[1] = tv_week2;
        weekTv[2] = tv_week3;
        weekTv[3] = tv_week4;
        weekTv[4] = tv_week5;
        weekTv[5] = tv_week6;
        weekTv[6] = tv_week7;

        Calendar calendar = Calendar.getInstance();
        int week2 = calendar.get(Calendar.DAY_OF_WEEK);
        week = Utils.getWeek(week2);

        switch (week) {
            case 1:
                initWeek = 1;
                break;
            case 2:
                initWeek2 = 1;
                break;
            case 3:
                initWeek3 = 1;
                break;
            case 4:
                initWeek4 = 1;
                break;
            case 5:
                initWeek5 = 1;
                break;
            case 6:
                initWeek6 = 1;
                break;
            case 7:
                initWeek7 = 1;
                break;
        }
        deviceMac = intent.getStringExtra("deviceMac");
        Intent service = new Intent(this, MQService.class);
        bind = bindService(service, connection, Context.BIND_AUTO_CREATE);
        IntentFilter filter = new IntentFilter("WatmerTimerActivity");
        registerReceiver(receiver, filter);
        MyDecoration decoration = new MyDecoration();
        decoration.setColor(Color.parseColor("#F5F5F5"))
                .setDeiverHeight(getDimen(R.dimen.dp_jy_15))
                .setMargin(0);
        rv_timer.setLayoutManager(new LinearLayoutManager(this));
        rv_timer.addItemDecoration(decoration);

        list = weekTimerTasks.get(week - 1);
        adapter = new TimerAdapter(this, list);
        rv_timer.setAdapter(adapter);
        setWeekTvBack();
    }

    @Override
    protected void onStart() {
        super.onStart();
        running = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        running = false;
    }

    private void setWeekTvBack() {
        for (int i = 0; i < 7; i++) {
            if (i == week - 1) {
                weekTv[i].setTextColor(getResources().getColor(R.color.white));
                weekTv[i].setBackground(getDrawable(R.drawable.shape_week));
                List<TimerTask> timerTasks = weekTimerTasks.get(i);
                for (int j = 0; j < 4; j++) {
                    list.set(j, timerTasks.get(j));
                }
                adapter.notifyDataSetChanged();

            } else {
                weekTv[i].setTextColor(getResources().getColor(R.color.heater_black));
                weekTv[i].setBackground(getDrawable(R.drawable.shape_week2));
            }
        }
    }

    private void setCopyTv(int i) {
        if (i == 0) {
            weekTv[copyWeek - 1].setTextColor(getResources().getColor(R.color.heater_black));
            weekTv[copyWeek - 1].setBackground(getDrawable(R.drawable.shape_week2));
        } else if (i == 1) {
            weekTv[copyWeek - 1].setTextColor(getResources().getColor(R.color.white));
            weekTv[copyWeek - 1].setBackground(getDrawable(R.drawable.shape_time_task));
        }

    }

    //当这7个变量的值为1时，表明已经请求过这周内某一天的定时数据
    int initWeek = 0;
    int initWeek2 = 0;
    int initWeek3 = 0;
    int initWeek4 = 0;
    int initWeek5 = 0;
    int initWeek6 = 0;
    int initWeek7 = 0;

    int copyOrPaster = 1;
    Map<Integer, Integer> integerMap = new TreeMap<>(new Comparator<Integer>() {
        @Override
        public int compare(Integer o1, Integer o2) {
            return o1 - o2;
        }
    });
    int copyWeek = 0;

    @OnClick({R.id.img_back, R.id.tv_copy, R.id.timer_add, R.id.tv_week1, R.id.tv_week2, R.id.tv_week3, R.id.tv_week4, R.id.tv_week5, R.id.tv_week6, R.id.tv_week7})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.tv_copy:
                if (list.isEmpty()) {
                    ToastUtil.showShortToast("这天没有定时可以复制");
                    break;
                }
                if (copyOrPaster == 1) {
                    copyOrPaster = 2;
                    tv_copy.setText("粘贴");
                    tv_copy.setTextColor(Color.parseColor("#ffffff"));
                    tv_copy.setBackground(getResources().getDrawable(R.drawable.shape_time_task2));
                } else if (copyOrPaster == 2) {
                    tv_copy.setText("复制");
                    tv_copy.setTextColor(Color.parseColor("#333333"));
                    tv_copy.setBackgroundResource(0);
                    copyOrPaster = 1;
                    copyWeek = 0;
                    new PasterTimerTaskAsyn(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }
                break;
            case R.id.timer_add:
                if (list.size() == 4) {
                    ToastUtil.showShortToast("一天最多有4段定时");
                    break;
                }
                Intent intent = new Intent(this, WarmerAddTimerActivity.class);
                intent.putExtra("deviceMac", deviceMac);
                intent.putExtra("week", week);
                intent.putExtra("index", list.size());//修改4段定时的哪一段定时
                intent.putExtra("weekTimerTask", (Serializable) weekTimerTasks.get(week - 1));
                startActivityForResult(intent, 100);
                break;
            case R.id.tv_week1:
                if (isDialogShowing())
                    break;
                if (copyOrPaster == 2) {
                    copyWeek = 1;
                }
                if (copyOrPaster == 2 && copyWeek == week) {
                    ToastUtil.showShortToast("请选择其他星期");
                    break;
                } else if (copyOrPaster == 2 && copyWeek != week) {
                    if (integerMap.containsKey(1)) {
                        integerMap.remove(1);
                        setCopyTv(0);
                    } else {
                        integerMap.put(1, 1);
                        setCopyTv(1);
                    }
                } else if (copyOrPaster == 1) {
                    week = 1;
                    setWeekTvBack();
                    if (initWeek == 0) {
                        initWeek = 1;
                        if (mqService.getTimerData(deviceMac, week) != 3)
                            countTimer.start();
                    }
                }
                break;
            case R.id.tv_week2:
                if (isDialogShowing())
                    break;
                if (copyOrPaster == 2) {
                    copyWeek = 2;
                }
                if (copyOrPaster == 2 && copyWeek == week) {
                    ToastUtil.showShortToast("请选择其他星期");
                    break;
                } else if (copyOrPaster == 2 && copyWeek != week) {
                    if (integerMap.containsKey(2)) {
                        integerMap.remove(2);
                        setCopyTv(0);
                    } else {
                        integerMap.put(2, 2);
                        setCopyTv(1);
                    }
                } else if (copyOrPaster == 1) {
                    week = 2;
                    setWeekTvBack();
                    if (initWeek2 == 0) {
                        initWeek2 = 1;
                        if (mqService.getTimerData(deviceMac, week) != 3)
                            countTimer.start();
                    }
                }
                break;
            case R.id.tv_week3:
                if (isDialogShowing())
                    break;
                if (copyOrPaster == 2) {
                    copyWeek = 3;
                }
                if (copyOrPaster == 2 && copyWeek == week) {
                    ToastUtil.showShortToast("请选择其他星期");
                    break;
                } else if (copyOrPaster == 2 && copyWeek != week) {
                    if (integerMap.containsKey(3)) {
                        integerMap.remove(3);
                        setCopyTv(0);
                    } else {
                        integerMap.put(3, 3);
                        setCopyTv(1);
                    }
                } else if (copyOrPaster == 1) {
                    week = 3;
                    setWeekTvBack();
                    if (initWeek3 == 0) {
                        initWeek3 = 1;
                        if (mqService.getTimerData(deviceMac, week) != 3)
                            countTimer.start();
                    }
                }
                break;
            case R.id.tv_week4:
                if (isDialogShowing())
                    break;
                if (copyOrPaster == 2) {
                    copyWeek = 4;
                }
                if (copyOrPaster == 2 && copyWeek == week) {
                    ToastUtil.showShortToast("请选择其他星期");
                    break;
                } else if (copyOrPaster == 2 && copyWeek != week) {
                    if (integerMap.containsKey(4)) {
                        integerMap.remove(4);
                        setCopyTv(0);
                    } else {
                        integerMap.put(4, 4);
                        setCopyTv(1);
                    }
                } else if (copyOrPaster == 1) {
                    week = 4;
                    setWeekTvBack();
                    if (initWeek4 == 0) {
                        initWeek4 = 1;
                        if (mqService.getTimerData(deviceMac, week) != 3)
                            countTimer.start();
                    }
                }
                break;
            case R.id.tv_week5:
                if (isDialogShowing())
                    break;
                if (copyOrPaster == 2) {
                    copyWeek = 5;
                }
                if (copyOrPaster == 2 && copyWeek == week) {
                    ToastUtil.showShortToast("请选择其他星期");
                    break;
                } else if (copyOrPaster == 2 && copyWeek != week) {
                    if (integerMap.containsKey(5)) {
                        integerMap.remove(5);
                        setCopyTv(0);
                    } else {
                        integerMap.put(5, 5);
                        setCopyTv(1);
                    }
                } else if (copyOrPaster == 1) {
                    week = 5;
                    setWeekTvBack();
                    if (initWeek5 == 0) {
                        initWeek5 = 1;
                        if (mqService.getTimerData(deviceMac, week) != 3)
                            countTimer.start();
                    }
                }
                break;
            case R.id.tv_week6:
                if (isDialogShowing())
                    break;
                if (copyOrPaster == 2) {
                    copyWeek = 6;
                }
                if (copyOrPaster == 2 && copyWeek == week) {
                    ToastUtil.showShortToast("请选择其他星期");
                    break;
                } else if (copyOrPaster == 2 && copyWeek != week) {
                    if (integerMap.containsKey(6)) {
                        integerMap.remove(6);
                        setCopyTv(0);
                    } else {
                        integerMap.put(6, 6);
                        setCopyTv(1);
                    }
                } else if (copyOrPaster == 1) {
                    week = 6;
                    setWeekTvBack();
                    if (initWeek6 == 0) {
                        initWeek6 = 1;
                        if (mqService.getTimerData(deviceMac, week) != 3)
                            countTimer.start();
                    }
                }
                break;
            case R.id.tv_week7:
                if (isDialogShowing())
                    break;
                if (copyOrPaster == 2) {
                    copyWeek = 7;
                }
                if (copyOrPaster == 2 && copyWeek == week) {
                    ToastUtil.showShortToast("请选择其他星期");
                    break;
                } else if (copyOrPaster == 2 && copyWeek != week) {
                    if (integerMap.containsKey(7)) {
                        integerMap.remove(7);
                        setCopyTv(0);
                    } else {
                        integerMap.put(7, 7);
                        setCopyTv(1);
                    }
                } else if (copyOrPaster == 1) {
                    week = 7;
                    setWeekTvBack();
                    if (initWeek7 == 0) {
                        initWeek7 = 1;
                        if (mqService.getTimerData(deviceMac, week) != 3)
                            countTimer.start();
                    }
                }
                break;
        }
    }

    ChangeDialog dialog;

    private void changeDialog(final int position) {
        if (dialog != null && dialog.isShowing()) {
            return;
        }
        dialog = new ChangeDialog(this);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMode(1);
        dialog.setTitle("删除定时");
        dialog.setTips("是否删除该定时?");
        backgroundAlpha(0.4f);
        dialog.setOnNegativeClickListener(new ChangeDialog.OnNegativeClickListener() {
            @Override
            public void onNegativeClick() {
                dialog.dismiss();
            }
        });
        dialog.setOnPositiveClickListener(new ChangeDialog.OnPositiveClickListener() {
            @Override
            public void onPositiveClick() {
                if (mqService != null) {
                    TimerTask timerTask = list.get(position);
                    list.set(position, timerTask);
                    boolean success = mqService.sendTimer(deviceMac, list, week, 1, position);
                    if (success) {
                        countTimer.start();
                    } else {
                        ToastUtil.showShortToast("当前网络不可用");
                    }
                }
                dialog.dismiss();
            }
        });
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                backgroundAlpha(1.0f);
            }
        });
        dialog.show();
    }

    //设置蒙版
    private void backgroundAlpha(float f) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = f;
        getWindow().setAttributes(lp);
    }

    private float getDimen(int dimenId) {
        return getResources().getDimension(dimenId);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 100) {
            List<TimerTask> timerTasks = (List<TimerTask>) data.getSerializableExtra("weekTimerTask");
            int index = data.getIntExtra("index", 0);
            mqService.sendTimer(deviceMac, timerTasks, week, 1, index);
            countTimer.start();
        }
    }

    class PasterTimerTaskAsyn extends BaseWeakAsyncTask<Void, Void, Integer, WatmerTimerActivity> {


        public PasterTimerTaskAsyn(WatmerTimerActivity watmerTimerActivity) {
            super(watmerTimerActivity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            setLoadDialog();
        }

        @Override
        protected Integer doInBackground(WatmerTimerActivity watmerTimerActivity, Void... voids) {
            int code = 0;
            try {

                Set<Integer> set = integerMap.keySet();
                Iterator<Integer> iterator = set.iterator();
                int count = 0;
                while (iterator.hasNext()) {
                    Integer key2 = iterator.next();
                    mqService.sendTimer(deviceMac, list, key2, 2, 0);
                    Log.i("PasterTimerTaskAsyn", "-->" + key2);
                    Thread.sleep(1000);
                    count++;
                }
                if (count == set.size()) {
                    code = 2000;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return code;
        }

        @Override
        protected void onPostExecute(WatmerTimerActivity watmerTimerActivity, Integer integer) {
            if (integer == 2000 && dialogLoad != null && dialogLoad.isShowing()) {
                dialogLoad.dismiss();
                for (Map.Entry<Integer, Integer> entry : integerMap.entrySet()) {
                    int key = entry.getKey();
                    weekTv[key - 1].setTextColor(getResources().getColor(R.color.heater_black));
                    weekTv[key - 1].setBackground(getResources().getDrawable(R.drawable.shape_week2));
                }
                integerMap.clear();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        if (bind)
            unbindService(connection);
        if (receiver != null)
            unregisterReceiver(receiver);
    }

    class TimerAdapter extends RecyclerView.Adapter<HolderView> {

        private Context context;
        private List<TimerTask> list;

        public TimerAdapter(Context context, List<TimerTask> list) {
            this.context = context;
            this.list = list;
        }

        @NonNull
        @Override
        public HolderView onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = View.inflate(context, R.layout.item_timer, null);
            return new HolderView(view);
        }

        @Override
        public void onBindViewHolder(@NonNull HolderView holderView, final int i) {

            final TimerTask timerTask = list.get(i);
            int hour = timerTask.getHour();
            int min = timerTask.getMin();
            int hour2 = timerTask.getHour2();
            int min2 = timerTask.getMin2();
            final int open = timerTask.getOpen();
            int temp = timerTask.getTemp();
            holderView.tv_open_timer.setText(hour + ":" + min);
            holderView.tv_close_timer.setText(hour2 + ":" + min2);
            holderView.tv_temp.setText(temp + "");
            if (open == 0) {
                holderView.img_state.setImageResource(R.mipmap.heater_timer_close);
            } else if (open == 1) {
                holderView.img_state.setImageResource(R.mipmap.heater_timer_open);
            }

            holderView.img_state.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (open == 0) {
                        timerTask.setOpen(1);
                    } else if (open == 1) {
                        timerTask.setOpen(0);
                    }
                    list.set(i, timerTask);
                    boolean success = mqService.sendTimer(deviceMac, list, week, 1, i);
                    if (success) {
                        countTimer.start();
                    } else {
                        ToastUtil.showShortToast("当前网络不可用");
                    }
                }
            });
            holderView.rl_timer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(WatmerTimerActivity.this, WarmerAddTimerActivity.class);
                    intent.putExtra("deviceMac", deviceMac);
                    intent.putExtra("week", week);
                    intent.putExtra("index", i);//修改4段定时的哪一段定时
                    intent.putExtra("weekTimerTask", (Serializable) list);
                    startActivityForResult(intent, 100);
                }
            });

        }

        @Override
        public int getItemCount() {
            return 4;
        }
    }

    class HolderView extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_open_timer)
        TextView tv_open_timer;
        @BindView(R.id.tv_close_timer)
        TextView tv_close_timer;
        @BindView(R.id.tv_temp_set)
        TextView tv_temp;
        @BindView(R.id.img_state)
        ImageView img_state;
        @BindView(R.id.rl_timer)
        RelativeLayout rl_timer;

        public HolderView(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    /**
     * 判断加载对话框是否正在显示
     *
     * @return
     */
    private boolean isDialogShowing() {
        if (dialogLoad != null && dialogLoad.isShowing()) {
            ToastUtil.showShortToast("请稍后...");
            return true;
        }
        return false;
    }

    DialogLoad dialogLoad;

    CountTimer countTimer = new CountTimer(2000, 1000);

    class CountTimer extends CountDownTimer {

        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        public CountTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            setLoadDialog();
            Log.e("CountDownTimer", "-->" + millisUntilFinished);
        }

        @Override
        public void onFinish() {
            if (dialogLoad != null && dialogLoad.isShowing()) {
                dialogLoad.dismiss();
            }
        }
    }

    /**
     * 显示加载数据对话框
     */
    private void setLoadDialog() {
        if (dialogLoad != null && dialogLoad.isShowing()) {
            return;
        }

        dialogLoad = new DialogLoad(this);
        dialogLoad.setCanceledOnTouchOutside(false);
        dialogLoad.setLoad("正在加载,请稍后");
        dialogLoad.show();
    }

    private MQService mqService;
    private List<List<TimerTask>> weekTimerTasks;//存储7天定时的集合
    private boolean bind;
    ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MQService.LocalBinder binder = (MQService.LocalBinder) service;
            mqService = binder.getService();
            initLoad = 1;
            if (mqService.getTimerData(deviceMac, week) != 3)
                countTimer.start();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                String macAddress = intent.getStringExtra("deviceMac");
                if (!TextUtils.isEmpty(macAddress) && macAddress.equals(deviceMac)) {
                    if (intent.hasExtra("reSet")) {
                        Utils.showToast(WatmerTimerActivity.this, "该设备已被重置");
                        Intent intent2 = new Intent(WatmerTimerActivity.this, MainActivity.class);
                        intent2.putExtra("houseId", houseId);
                        startActivity(intent2);
                    } else {
                        int week2 = intent.getIntExtra("week", 0);
                        List<TimerTask> timerTasks2 = (List<TimerTask>) intent.getSerializableExtra("weekTimerTask");
                        week2 += 1;
                        switch (week2) {
                            case 1:
                                initWeek = 1;
                                break;
                            case 2:
                                initWeek2 = 1;
                                break;
                            case 3:
                                initWeek3 = 1;
                                break;
                            case 4:
                                initWeek4 = 1;
                                break;
                            case 5:
                                initWeek5 = 1;
                                break;
                            case 6:
                                initWeek6 = 1;
                                break;
                            case 7:
                                initWeek7 = 1;
                                break;
                        }
                        if (week2 == week) {
                            weekTimerTasks.set(week - 1, timerTasks2);
                            List<TimerTask> timerTasks = weekTimerTasks.get(week - 1);
                            for (int i = 0; i < 4; i++) {
                                list.set(i, timerTasks.get(i));
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            weekTimerTasks.set(week2 - 1, timerTasks2);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
}
