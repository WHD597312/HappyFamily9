package com.xr.happyFamily.jia.xnty;


import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.xr.happyFamily.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class CsjActivity extends AppCompatActivity implements View.OnClickListener {
    private BottomSheetBehavior bottomSheetBehavior;
    private BottomSheetBehavior bottomSheetBehavior2;
    private BottomSheetBehavior bottomSheetBehavior3;
    private ImageView show;
    Unbinder unbinder;
    @BindView(R.id.iv_csj_bj)
    ImageView image;
    @BindView(R.id.iv_b_1)
    ImageView image1;
    @BindView(R.id.iv_b_2)
    ImageView image2;
    @BindView(R.id.iv_b_3)
    ImageView image3;
    @BindView(R.id.tm_ks1)
    Timepicker timepicker1;
    @BindView(R.id.tm_ks2)
    Timepicker timepicker2;
    @BindView(R.id.tm_js1)
    Timepicker timepicker3;
    @BindView(R.id.tm_js2)
    Timepicker timepicker4;
    @BindView(R.id.tv_csj_36)
    RunningNumView runningNumView;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_csj);
     unbinder = ButterKnife.bind(this);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        initViews();
        initListeners();
        initTimer();

        String score = "36";
        runningNumView.setDuration(2000).setTextAndDigit(score, 3);
        runningNumView.startAnimation();

    }
    private void initTimer(){//设置定时时间
        timepicker1.setMaxValue(23);
       timepicker1.setMinValue(00);
        timepicker1.setValue(49);
        timepicker1.setBackgroundColor(Color.LTGRAY);
       timepicker1.setNumberPickerDividerColor(timepicker1);
        timepicker2.setMaxValue(59);
        timepicker2.setMinValue(00);
        timepicker2.setValue(49);
        timepicker2.setBackgroundColor(Color.LTGRAY);
        timepicker2.setNumberPickerDividerColor(timepicker2);
        timepicker3.setMaxValue(23);
        timepicker3.setMinValue(00);
        timepicker3.setValue(49);
        timepicker3.setBackgroundColor(Color.LTGRAY);
       timepicker3.setNumberPickerDividerColor(timepicker3);
        timepicker4.setMaxValue(59);
        timepicker4.setMinValue(00);
        timepicker4.setValue(49);
        timepicker4.setBackgroundColor(Color.LTGRAY);
        timepicker4.setNumberPickerDividerColor(timepicker4);

    }

    private void initViews() {


        bottomSheetBehavior = BottomSheetBehavior.from(findViewById(R.id.bottomSheetLayout));
        bottomSheetBehavior2= BottomSheetBehavior.from(findViewById(R.id.bottomSheetLayout2));
        bottomSheetBehavior3 = BottomSheetBehavior.from(findViewById(R.id.bottomSheetLayout3));
        show = (ImageView) findViewById(R.id.iv_csj_fs);


    }
    private void initListeners() {


        show.setOnClickListener(this);


        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(View bottomSheet, int newState) {



                // Check Logs to see how bottom sheets behaves
                switch (newState) {
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        Log.e("Bottom Sheet Behaviour", "STATE_COLLAPSED");
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        Log.e("Bottom Sheet Behaviour", "STATE_DRAGGING");
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        Log.e("Bottom Sheet Behaviour", "STATE_EXPANDED");
                        break;
                    case BottomSheetBehavior.STATE_HIDDEN:
                        Log.e("Bottom Sheet Behaviour", "STATE_HIDDEN");
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        Log.e("Bottom Sheet Behaviour", "STATE_SETTLING");
                        break;
                }
            }


            @Override
            public void onSlide(View bottomSheet, float slideOffset) {

            }
        });


    }
    private int position=-1;
    @OnClick({R.id.ib_csj_fh, R.id.iv_b_qx,R.id.iv_b_qd,R.id.iv_b_2,R.id.iv_b_3,R.id.iv_b_1,R.id.iv_csj_ds
            , R.id.iv_b2_qx,R.id.tv_csj_29, R.id.iv_b3_qx
    })
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.iv_csj_fs://叫出风速页面
                if(bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED ){
                   // bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);按键可在隐藏
                } else if(bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_HIDDEN || bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED){
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
                break;
            case R.id.tv_csj_29://叫出设置湿度界面
                if(bottomSheetBehavior3.getState() == BottomSheetBehavior.STATE_EXPANDED ){
                    // bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);按键可在隐藏
                } else if(bottomSheetBehavior3.getState() == BottomSheetBehavior.STATE_HIDDEN || bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED){
                    bottomSheetBehavior3.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
                break;
            case R.id.ib_csj_fh:
              finish();
                break;

            case R.id.iv_b_qx:
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                break;
            case R.id.iv_b2_qx:
                bottomSheetBehavior2.setState(BottomSheetBehavior.STATE_HIDDEN);
                break;
            case R.id.iv_b3_qx:
                bottomSheetBehavior3.setState(BottomSheetBehavior.STATE_HIDDEN);
                break;
            case R.id.iv_b_qd:

                if (position==0){
                    Glide.with(image.getContext())
                            .load(R.mipmap.man2x)
                            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                            .centerCrop()
                            .into(new GlideDrawableImageViewTarget(image, 1));
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                }else if (position==1){
                    Glide.with(image.getContext())
                            .load(R.mipmap.zhong2x)
                            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                            .centerCrop()
                            .into(new GlideDrawableImageViewTarget(image, 1));
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                }else if (position==2){
                    Glide.with(CsjActivity.this)
                            .load(R.mipmap.kuai3x)
                            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                            .centerCrop()
                            .into(new GlideDrawableImageViewTarget(image, 1));
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                }
                break;
            case R.id.iv_b_2:
                position=1;
                image2.setImageResource(R.mipmap.csj_dj3x);
                image1.setImageResource(R.mipmap.csj_djnull3x);
                image3.setImageResource(R.mipmap.csj_djnull3x);

                break;
            case R.id.iv_b_1:
                position=0;
                image1.setImageResource(R.mipmap.csj_dj3x);
                image2.setImageResource(R.mipmap.csj_djnull3x);
                image3.setImageResource(R.mipmap.csj_djnull3x);

                break;
            case R.id.iv_b_3:
                position=2;
                image3.setImageResource(R.mipmap.csj_dj3x);
                image1.setImageResource(R.mipmap.csj_djnull3x);
                image2.setImageResource(R.mipmap.csj_djnull3x);

                break;
            case R.id.iv_csj_ds://定时显示隐藏
                if(bottomSheetBehavior2.getState() == BottomSheetBehavior.STATE_EXPANDED ){
                    // bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);按键可在隐藏
                } else if(bottomSheetBehavior2.getState() == BottomSheetBehavior.STATE_HIDDEN || bottomSheetBehavior2.getState() == BottomSheetBehavior.STATE_COLLAPSED){
                    bottomSheetBehavior2.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
                break;
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        image.setImageResource(R.mipmap.kuai3x);

    }

}
