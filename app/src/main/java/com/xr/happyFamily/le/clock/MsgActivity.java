package com.xr.happyFamily.le.clock;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xr.happyFamily.R;
import com.xr.happyFamily.le.adapter.MsgClockAdapter;
import com.xr.happyFamily.le.adapter.MsgFriendAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MsgActivity extends AppCompatActivity {


    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.rv_msg)
    RecyclerView rvMsg;
    @BindView(R.id.rv_friend)
    RecyclerView rvFriend;

    MsgClockAdapter msgClockAdapter;
    MsgFriendAdapter msgFriendAdapter;
    @BindView(R.id.tv_msg)
    TextView tvMsg;
    @BindView(R.id.ll_msg)
    LinearLayout llMsg;
    @BindView(R.id.tv_friend)
    TextView tvFriend;
    @BindView(R.id.ll_friend)
    LinearLayout llFriend;
    Drawable draw_msg_true,draw_msg_false,draw_friend_true,draw_friend_false;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock_msg);
        ButterKnife.bind(this);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        titleText.setText("消息");
        List<String> qunzuList = new ArrayList<>();
        qunzuList.add("大宝");
        qunzuList.add("卡布奇诺");
        qunzuList.add("哈根达狮");
        qunzuList.add("冰糖雪狸");
        qunzuList.add("焦糖玛奇");
        qunzuList.add("熊猫");
        qunzuList.add("大象");
        qunzuList.add("长颈鹿");

        msgClockAdapter = new MsgClockAdapter(this, qunzuList);
        msgFriendAdapter = new MsgFriendAdapter(this, qunzuList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvFriend.setLayoutManager(linearLayoutManager);
        rvFriend.setAdapter(msgFriendAdapter);

        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(this);
        rvMsg.setLayoutManager(linearLayoutManager2);
        rvMsg.setAdapter(msgClockAdapter);

        draw_msg_true = getResources().getDrawable(R.mipmap.ic_clock_msg_clock1);
        draw_msg_true.setBounds(0, 0, draw_msg_true.getMinimumWidth(), draw_msg_true.getMinimumHeight());
        draw_msg_false = getResources().getDrawable(R.mipmap.ic_clock_msg_clock);
        draw_msg_false.setBounds(0, 0, draw_msg_false.getMinimumWidth(), draw_msg_false.getMinimumHeight());
        draw_friend_true = getResources().getDrawable(R.mipmap.ic_clock_msg_friend1);
        draw_friend_true.setBounds(0, 0, draw_friend_true.getMinimumWidth(), draw_friend_true.getMinimumHeight());
        draw_friend_false = getResources().getDrawable(R.mipmap.ic_clock_msg_friend);
        draw_friend_false.setBounds(0, 0, draw_friend_false.getMinimumWidth(), draw_friend_false.getMinimumHeight());

    }


    @OnClick({R.id.back, R.id.ll_msg, R.id.ll_friend})
    public void onClick(View view) {
        switch (view.getId()) {


            case R.id.back:
                finish();
                break;
            case R.id.ll_msg:
                if (rvFriend.getVisibility() == View.VISIBLE) {
                    rvFriend.setVisibility(View.GONE);
                    rvMsg.setVisibility(View.VISIBLE);
                    tvFriend.setCompoundDrawables(draw_friend_false,null,null,null);
                    tvMsg.setCompoundDrawables(draw_msg_true,null,null,null);
                    tvMsg.setTextColor(Color.parseColor("#3393ED"));
                    tvFriend.setTextColor(Color.parseColor("#9e9e9e"));
                }
                break;
            case R.id.ll_friend:
                if (rvMsg.getVisibility() == View.VISIBLE) {
                    rvMsg.setVisibility(View.GONE);
                    rvFriend.setVisibility(View.VISIBLE);
                    tvFriend.setCompoundDrawables(draw_friend_true,null,null,null);
                    tvFriend.setTextColor(Color.parseColor("#3393ED"));
                    tvMsg.setTextColor(Color.parseColor("#9e9e9e"));
                    tvMsg.setCompoundDrawables(draw_msg_false,null,null,null);
                }
                break;
        }
    }


}
