package com.xr.happyFamily.le;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.xr.happyFamily.R;
import com.xr.happyFamily.le.view.MyHorizontalScrollView;
import com.xr.happyFamily.le.view.MyHorizontalScrollViewAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by win7 on 2018/5/22.
 */

public class XuYuanActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.myHs1)
    MyHorizontalScrollView myHs1;
    @BindView(R.id.myHs2)
    MyHorizontalScrollView myHs2;
    @BindView(R.id.myHs3)
    MyHorizontalScrollView myHs3;
    @BindView(R.id.myHs4)
    MyHorizontalScrollView myHs4;
    @BindView(R.id.myHs5)
    MyHorizontalScrollView myHs5;
    private TimerTask scrollerSchedule;
    private Timer scrollTimer = null;
    private int[] scrollPos = {0,0,0,0,0};
    /* gallery视图* */
    MyHorizontalScrollViewAdapter adapter1;
    MyHorizontalScrollViewAdapter adapter2;
    MyHorizontalScrollViewAdapter adapter3;
    MyHorizontalScrollViewAdapter adapter4;
    MyHorizontalScrollViewAdapter adapter5;
    MyHorizontalScrollViewAdapter[] adapter;
    ArrayList<Map<String, Object>> list1;
    ArrayList<Map<String, Object>> list2;
    ArrayList<Map<String, Object>> list3;
    ArrayList<Map<String, Object>> list4;
    ArrayList<Map<String, Object>> list5;
    ArrayList<Map<String, Object>>[] list;
    MyHorizontalScrollView[] myHs;

    int[] array = new int[]{0,1,2,3,4};

    String[] str=new String[]{"我想去旅游","我想啪啪啪啪啪啪啪啪啪啪","想放假","的任务而额外热碗热碗热我认为热污染温热","放电饭锅德斯特过热退热贴"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_happy_xuyuan);
        ButterKnife.bind(this);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        mContext=XuYuanActivity.this;
        myHs= new MyHorizontalScrollView[]{myHs1, myHs2, myHs3, myHs4, myHs5};
        adapter= new MyHorizontalScrollViewAdapter[]{adapter1, adapter2, adapter3, adapter4, adapter5};
        list1 = new ArrayList<>();
        list2 = new ArrayList<>();
        list3 = new ArrayList<>();
        list4 = new ArrayList<>();
        list5 = new ArrayList<>();

        list= new ArrayList[]{list1, list2, list3, list4, list5};
        for(int i=0;i<5;i++){
            for (int j = 0; j < 10; j++) {
                Map<String, Object> maps = new HashMap<>();
                maps.put("name",str[new Random().nextInt(5)]);
                maps.put("touxiang", R.mipmap.ic_shop_pinglun_touxiang);
                list[i].add(maps);
            }
            adapter[i] = new MyHorizontalScrollViewAdapter(this, list[i]);
            myHs[i].setAdapter(this, adapter[i]);
            adapter[i].notifyDataSetChanged();
        }

        startAutoScrolling();
    }


    @OnClick({R.id.img_back,R.id.img_add})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.img_add:
                showPopup();
                break;

        }
    }

    private void startAutoScrolling() {
        for (int i = 0; i < array.length; i++) {
            int s = (int)(Math.random()*array.length);
            int temp = array[i];
            array[i] = array[s];
            array[s] = temp;
        }
        if (scrollTimer == null) {
            scrollTimer = new Timer();
            if (scrollerSchedule != null) {
                scrollerSchedule.cancel();
                scrollerSchedule = null;
            }

            for(int i=0;i<array.length;i++) {
                final int sign=i;
                scrollerSchedule = new TimerTask() {
                    @Override
                    public void run() {
                        moveScrollView(array[sign]);
                    }
                };
                scrollTimer.schedule(scrollerSchedule, 500, 5);
            }

        }
    }

    private void moveScrollView(int i) {
        if (scrollPos[i] > myHs[i].getScrollX() && (scrollPos[i] != 1)) {
            myHs[i].smoothScrollTo(0, 0);
            scrollPos[i] = 0;
        } else {
            scrollPos[i] = (int) (myHs[i].getScrollX() + 1.0);
            myHs[i].smoothScrollTo(scrollPos[i], 0);
        }
    }

    /**
     * 取消滚动
     */
    private void cancelAutoScroll() {
        if (null != scrollTimer) {
            scrollTimer.cancel();
            scrollTimer = null;
        }
        if (null != scrollerSchedule) {
            scrollerSchedule.cancel();
            scrollerSchedule = null;
        }
    }

    private View contentViewSign;
    private PopupWindow mPopWindow;
    private Context mContext;
    private TextView tv_msg,tv_fabu,tv_pipei;

    private void showPopup() {

        contentViewSign = LayoutInflater.from(mContext).inflate(R.layout.popup_xuyuan, null);
        tv_msg = (TextView) contentViewSign.findViewById(R.id.tv_msg);
        tv_fabu = (TextView) contentViewSign.findViewById(R.id.tv_fabu);
        tv_pipei = (TextView) contentViewSign.findViewById(R.id.tv_pipei);
        tv_msg.setOnClickListener(this);
        tv_fabu.setOnClickListener(this);
        tv_pipei.setOnClickListener(this);
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
        mPopWindow.showAsDropDown(findViewById(R.id.img_add));
    }


    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp); //添加pop窗口关闭事件
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_msg:
                startActivity(new Intent(this, MsgActivity.class));
                mPopWindow.dismiss();
                break;
            case R.id.tv_fabu:
                startActivity(new Intent(this, FaBuActivity.class));
                mPopWindow.dismiss();
                break;
            case R.id.tv_pipei:
                startActivity(new Intent(this, PiPeiActivity.class));
                mPopWindow.dismiss();
                break;
        }
    }


    class poponDismissListener implements PopupWindow.OnDismissListener {

        @Override
        public void onDismiss() {
            // TODO Auto-generated method stub
            backgroundAlpha(1f);
        }

    }
}