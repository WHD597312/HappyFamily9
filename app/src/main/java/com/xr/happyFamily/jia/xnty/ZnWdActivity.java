package com.xr.happyFamily.jia.xnty;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.xr.happyFamily.R;
import com.xr.happyFamily.jia.titleview.TitleView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import pl.droidsonroids.gif.GifDrawable;

public class ZnWdActivity extends AppCompatActivity {
    Unbinder unbinder;
    GifDrawable gifDrawable1;
    GifDrawable gifDrawable2;
    GifDrawable gifDrawable3;

    TitleView titleView;

    @BindView(R.id.wd_xh)
    ImageView image;
    @BindView(R.id.wd_d)
    ImageView imageViewd;
    @BindView(R.id.iv_wd_dh)
    ImageView imageViewdh;
    @BindView(R.id.tv_wd_name)
    TextView textViewname;
    @BindView(R.id.wd_24)
    TextView textView24;
    @BindView(R.id.tv_wd_dqwd)
    TextView textViewdqwd;
    @BindView(R.id.tv_wd_wr)
    TextView textViewl;
    @BindView(R.id.tv_wd_yb)
    TextView textViewm;
    @BindView(R.id.tv_wd_fc)
    TextView textViewr;
    @BindView(R.id.tv_wd_18)
    TextView textVie18;
    @BindView(R.id.tv_wd_sz)
    TextView textViewsz;
    @BindView(R.id.bt_wd_wd)
    Button buttonwd;
    @BindView(R.id.bt_wd_sd)
    Button buttonsd;
    @BindView(R.id.bt_wd_pm25)
    Button buttonpm25;
    private ArcProgressBar mArcProgressBar;
    int i;
    protected void onCreate(Bundle savadInstanceState) {
        super.onCreate(savadInstanceState);
        setContentView(R.layout.activty_xnty_wd);
        unbinder = ButterKnife.bind(this);
        titleView = (TitleView) findViewById(R.id.title);
        titleView.setTitleText("智能终端");
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        try {//加载动画

            gifDrawable1=new GifDrawable(getResources(),R.mipmap.wd_fs);
            gifDrawable2=new GifDrawable(getResources(),R.mipmap.pm25_xh);
            gifDrawable3=new GifDrawable(getResources(),R.mipmap.wd_kt);


        }catch (Exception e){
            e.printStackTrace();
        }
        if (gifDrawable1!=null && gifDrawable2!=null&&gifDrawable3!=null){
            gifDrawable1.start();
            gifDrawable2.start();
            gifDrawable3.start();
            imageViewd.setImageDrawable(gifDrawable1);
            image.setImageDrawable(gifDrawable2);
            imageViewdh.setImageDrawable(gifDrawable3);
        }
        mArcProgressBar = (ArcProgressBar) findViewById(R.id.arcprogressBar);

        mArcProgressBar.setOnSeekBarChangeListener(new ArcProgressBar.OnSeekBarChangeListener() {
            @Override
            public void onChanged(ArcProgressBar seekbar, int curValue) {

                i =  mArcProgressBar.getSign();
                Log.i("ssss", "--->"+i);
                if (change==0){
                    textVie18.setText(""+String.valueOf((i*28/23)+16)+"℃");
                }else if (change==1){
                    textVie18.setText(""+String.valueOf((i*70/22)+20)+"%");
                }else if (change==2){
                    textVie18.setText(""+String.valueOf(i*500/22));
                }

            }
        });

    }








    int change = 0;

    @OnClick({R.id.bt_wd_sd,R.id.bt_wd_pm25,R.id.bt_wd_wd})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_wd_sd:
                imageViewd.setImageResource(R.mipmap.sd_sd);
                textViewname.setText("智能加湿");
                imageViewdh.setImageResource(R.mipmap.sd_kqjhq);
                textView24.setText("70%");
                textViewdqwd.setText("当前湿度");
                textViewl.setText("潮湿");
                textViewm.setText("最佳");
                textViewr.setText("干燥");
                textVie18.setText("20%");
                textViewsz.setText("当前湿度");
                buttonsd.setTextColor(getResources().getColor(R.color.color_blue_pm25));
                buttonwd.setTextColor(getResources().getColor(R.color.color_gray2));
                buttonpm25.setTextColor(getResources().getColor(R.color.color_gray2));
                mArcProgressBar.restart();
                try {//加载动画

                        gifDrawable1=new GifDrawable(getResources(),R.mipmap.sd_sd);
                        gifDrawable2=new GifDrawable(getResources(),R.mipmap.pm25_xh);
                        gifDrawable3=new GifDrawable(getResources(),R.mipmap.sd_kqjhq);


                }catch (Exception e){
                    e.printStackTrace();
                }
                if (gifDrawable1!=null && gifDrawable2!=null&&gifDrawable3!=null){
                    gifDrawable1.start();
                    gifDrawable2.start();
                    gifDrawable3.setSpeed(5.0f);
                    gifDrawable3.start();
                    imageViewd.setImageDrawable(gifDrawable1);
                    image.setImageDrawable(gifDrawable2);
                    imageViewdh.setImageDrawable(gifDrawable3);
                }
                change =1;
                break;
            case R.id.bt_wd_pm25:
                imageViewd.setImageResource(R.mipmap.pm25_pm25);
                textViewname.setText("简化版净化空气");
                imageViewdh.setImageResource(R.mipmap.pm25_kqjh);
                textView24.setText("200");
                textViewdqwd.setText("当前PM值");
                textViewl.setText("污染");
                textViewm.setText("一般");
                textViewr.setText("非常好");
                textVie18.setText("0");
                textViewsz.setText("设置PM值");
                buttonsd.setTextColor(getResources().getColor(R.color.color_gray2));
                buttonwd.setTextColor(getResources().getColor(R.color.color_gray2));
                buttonpm25.setTextColor(getResources().getColor(R.color.color_blue_pm25));
                mArcProgressBar.restart();
                try {//加载动画

                        gifDrawable1=new GifDrawable(getResources(),R.mipmap.pm25_pm25);
                        gifDrawable2=new GifDrawable(getResources(),R.mipmap.pm25_xh);
                        gifDrawable3=new GifDrawable(getResources(),R.mipmap.pm25_kqjh);

                }catch (Exception e){
                    e.printStackTrace();
                }
                if (gifDrawable1!=null && gifDrawable2!=null&&gifDrawable3!=null){
                    gifDrawable1.start();
                    gifDrawable2.start();
                    gifDrawable3.setSpeed(5.0f);
                    gifDrawable3.start();
                    imageViewd.setImageDrawable(gifDrawable1);
                    image.setImageDrawable(gifDrawable2);
                    imageViewdh.setImageDrawable(gifDrawable3);
                }
                change=2;
                break;
            case R.id.bt_wd_wd:
                imageViewd.setImageResource(R.mipmap.wd_fs);
                textViewname.setText("智能制冷");
                imageViewdh.setImageResource(R.mipmap.wd_kt);
                textView24.setText("39℃");
                textViewdqwd.setText("当前温度");
                textViewl.setText("寒冷");
                textViewm.setText("舒适");
                textViewr.setText("酷热");
                textVie18.setText("16℃");
                textViewsz.setText("当前温度");
                buttonsd.setTextColor(getResources().getColor(R.color.color_gray2));
                buttonwd.setTextColor(getResources().getColor(R.color.color_blue_pm25));
                buttonpm25.setTextColor(getResources().getColor(R.color.color_gray2));
                mArcProgressBar.restart();
                try {//加载动画

                        gifDrawable1=new GifDrawable(getResources(),R.mipmap.wd_fs);
                        gifDrawable2=new GifDrawable(getResources(),R.mipmap.pm25_xh);
                        gifDrawable3=new GifDrawable(getResources(),R.mipmap.wd_kt);


                }catch (Exception e){
                    e.printStackTrace();
                }
                if (gifDrawable1!=null && gifDrawable2!=null&&gifDrawable3!=null){
                    gifDrawable1.start();
                    gifDrawable2.start();

                    gifDrawable3.start();
                    imageViewd.setImageDrawable(gifDrawable1);
                    image.setImageDrawable(gifDrawable2);
                    imageViewdh.setImageDrawable(gifDrawable3);
                }
                change=0;
                break;
        }

    }
    @Override
    protected void onStart() {
        super.onStart();


    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unbinder!=null){
            unbinder.unbind();
        }
    }
}