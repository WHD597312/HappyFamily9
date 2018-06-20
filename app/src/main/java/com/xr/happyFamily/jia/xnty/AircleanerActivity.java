package com.xr.happyFamily.jia.xnty;

import android.os.Bundle;

import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xr.happyFamily.R;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class AircleanerActivity extends AppCompatActivity implements View.OnClickListener {
    Unbinder unbinder;
    Animation rotate;
    @BindView(R.id.kqjh_ld1)
    ImageView image1;
    @BindView(R.id.kqjh_ld2)
    ImageView image2;
    @BindView(R.id.kqjh_ld3)
    ImageView image3;
    @BindView(R.id.kqjh_ld4)
    ImageView image4;
    @BindView(R.id.kqjh_ld5)
    ImageView image5;
    @BindView(R.id.kqjh_ld6)
    ImageView image6;
    @BindView(R.id.kqjh_ld7)
    ImageView image7;
    @BindView(R.id.kqjh_ld8)
    ImageView image8;
    @BindView(R.id.kqjh_ld9)
    ImageView image9;
    @BindView(R.id.kqjh_ld10)
    ImageView image10;
    @BindView(R.id.kqjh_ld11)
    ImageView image11;
    @BindView(R.id.kqjh_ld12)
    ImageView image12;
    @BindView(R.id.kqjh_ld13)
    ImageView image13;
    @BindView(R.id.kqjh_ld14)
    ImageView image14;
    @BindView(R.id.kqjh_ld15)
    ImageView image15;
    @BindView(R.id.kqjh_ld16)
    ImageView image16;
    @BindView(R.id.kqjh_ld17)
    ImageView image17;
    @BindView(R.id.kqjh_ld18)
    ImageView image18;
    @BindView(R.id.kqjh_ld19)
    ImageView image19;
    @BindView(R.id.kqjh_ld20)
    ImageView image20;
    @BindView(R.id.kqjh_ld21)
    ImageView image21;
    @BindView(R.id.kqjh_ld22)
    ImageView image22;
    @BindView(R.id.kqjh_ld23)
    ImageView image23;
    @BindView(R.id.kqjh_ld24)
    ImageView image24;
    @BindView(R.id.kqjh_ld25)
    ImageView image25;
    @BindView(R.id.kqjh_ld26)
    ImageView image26;
    @BindView(R.id.kqjh_ld27)
    ImageView image27;
    @BindView(R.id.kqjh_ld28)
    ImageView image28;
    @BindView(R.id.kqjh_rd1)
    ImageView image1r;
    @BindView(R.id.kqjh_rd2)
    ImageView image2r;
    @BindView(R.id.kqjh_rd3)
    ImageView image3r;
    @BindView(R.id.kqjh_rd4)
    ImageView image4r;
    @BindView(R.id.kqjh_rd5)
    ImageView image5r;
    @BindView(R.id.kqjh_rd6)
    ImageView image6r;
    @BindView(R.id.kqjh_rd7)
    ImageView image7r;
    @BindView(R.id.kqjh_rd8)
    ImageView image8r;
    @BindView(R.id.kqjh_rd9)
    ImageView image9r;
    @BindView(R.id.kqjh_rd10)
    ImageView image10r;
    @BindView(R.id.kqjh_rd11)
    ImageView image11r;
    @BindView(R.id.kqjh_rd12)
    ImageView image12r;
    @BindView(R.id.kqjh_rd13)
    ImageView image13r;
    @BindView(R.id.kqjh_rd14)
    ImageView image14r;
    @BindView(R.id.kqjh_rd15)
    ImageView image15r;
    @BindView(R.id.kqjh_rd16)
    ImageView image16r;
    @BindView(R.id.kqjh_rd17)
    ImageView image17r;
    @BindView(R.id.kqjh_rd18)
    ImageView image18r;
    @BindView(R.id.kqjh_rd19)
    ImageView image19r;
    @BindView(R.id.kqjh_rd20)
    ImageView image20r;
    @BindView(R.id.kqjh_rd21)
    ImageView image21r;
    @BindView(R.id.kqjh_rd22)
    ImageView image22r;
    @BindView(R.id.kqjh_rd23)
    ImageView image23r;
    @BindView(R.id.kqjh_rd24)
    ImageView image24r;
    @BindView(R.id.kqjh_rd25)
    ImageView image25r;
    @BindView(R.id.kqjh_rd26)
    ImageView image26r;
    @BindView(R.id.kqjh_rd27)
    ImageView image27r;
    @BindView(R.id.kqjh_rd28)
    ImageView image28r;
    @BindView(R.id.iv_kqjh_rair)
    ImageView imageairr;
    @BindView(R.id.iv_kqjh_lair)
    ImageView imageairl;
    @BindView(R.id.iv_kqjh_mair)
    ImageView imageairm;
    @BindView(R.id.iv_kqjh_fs)
    ImageView imgefs;
    @BindView(R.id.iv_kqjh_kg)
    ImageView  imageViewkg;
    @BindView(R.id.tv_kqjh_kg)
    TextView textViewkg;
    @BindView(R.id.tv_kqjh_fs)
    TextView textViewfs;
    @BindView(R.id.tv_kqjh_sm)
    TextView textViewsm;
    @BindView(R.id.tv_kqjh_ds)
    TextView textViewds;
    @BindView(R.id.tm_ks1)
    Timepicker timepicker1;
    @BindView(R.id.tm_ks2)
    Timepicker timepicker2;
    @BindView(R.id.tm_js1)
    Timepicker timepicker3;
    @BindView(R.id.tm_js2)
    Timepicker timepicker4;
    int flag = 0;
    Animation anim;
    Animation anim1;
    Animation anim2;
    Animation anim3;
    Animation anim4;
    Animation animr;
    Animation animr1;
    Animation anim2r;
    Animation anim3r;
    Animation anim4r;
    Animation animairleft;
    Animation animairright;
    Animation animairmiddle;
    @BindView(R.id.iv_b_1)
    ImageView imageb1;
    @BindView(R.id.iv_b_2)
    ImageView imageb2;
    @BindView(R.id.iv_b_3)
    ImageView imageb3;
    @BindView(R.id.tv_b_1)
    TextView textb1;
    @BindView(R.id.tv_b_2)
    TextView textb2;
    @BindView(R.id.tv_b_3)
    TextView textb3;
    @BindView(R.id.iv_kqjh_sm)
    ImageView imageViewsm;
    private BottomSheetBehavior bottomSheetBehavior;
    private BottomSheetBehavior bottomSheetBehavior2;


    @Override

    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xnty_kqjhq);
        unbinder = ButterKnife.bind(this);
        imageViewsm.setTag("close");
         initTimer();//定时设置
         initViews();//上拉列表

        // 启动动画

        /*translateAnimation.setDuration(2000);      //设置位置变化动画的持续时间
        alphaAnimation.setDuration(2000);          //设置透明度渐变动画的持续时间
        AnimationSet set=new AnimationSet(true);    //创建动画集对象
        set.addAnimation(translateAnimation);       //添加位置变化动画
        set.addAnimation(alphaAnimation);           //添加透明度渐变动画
        set.setRepeatCount(-1);
        image.setAnimation(set);                    //设置动画
        set.startNow();                         //启动动画
*/
         anim = AnimationUtils.loadAnimation(this, R.anim.doubleani);

        image1.startAnimation(anim);
        image6.startAnimation(anim);
        image3.startAnimation(anim);
        image7.startAnimation(anim);
        image11.startAnimation(anim);
        image21.startAnimation(anim);
        image3.startAnimation(anim);
        image12.startAnimation(anim);


        image8.startAnimation(anim);
        image10.startAnimation(anim);
        anim1 = AnimationUtils.loadAnimation(this, R.anim.doubleani1);
        image4.startAnimation(anim1);
        image5.startAnimation(anim1);

        image2.startAnimation(anim1);
        image13.startAnimation(anim1);
        image9.startAnimation(anim1);

        anim2 = AnimationUtils.loadAnimation(this, R.anim.doubleani2);
        image23.startAnimation(anim2);

        image17.startAnimation(anim2);
        image20.startAnimation(anim2);

        image15.startAnimation(anim2);

        image19.startAnimation(anim2);
        image14.startAnimation(anim2);

        anim4 = AnimationUtils.loadAnimation(this, R.anim.doubleani4);
        image18.startAnimation(anim4);
        image22.startAnimation(anim4);
        image16.startAnimation(anim4);
        image24.startAnimation(anim4);
         anim3 = AnimationUtils.loadAnimation(this, R.anim.doubleani3);
        image25.startAnimation(anim3);
        image26.startAnimation(anim3);
        image27.startAnimation(anim3);
        image28.startAnimation(anim3);
        animr = AnimationUtils.loadAnimation(this, R.anim.doubleanir);

        image1r.startAnimation(animr);
        image6r.startAnimation(animr);
        image3r.startAnimation(animr);
        image7r.startAnimation(animr);
        image11r.startAnimation(animr);
        image21r.startAnimation(animr);
        image3r.startAnimation(animr);
        image12r.startAnimation(animr);
        image8r.startAnimation(animr);
        image10r.startAnimation(animr);
         animr1 = AnimationUtils.loadAnimation(this, R.anim.doubleanir1);
        image4r.startAnimation(animr1);
        image5r.startAnimation(animr1);

        image2r.startAnimation(animr1);
        image13r.startAnimation(animr1);
        image9r.startAnimation(animr1);

         anim2r = AnimationUtils.loadAnimation(this, R.anim.doubleanir2);
        image23r.startAnimation(anim2r);

        image17r.startAnimation(anim2r);
        image20r.startAnimation(anim2r);

        image15r.startAnimation(anim2r);

        image19r.startAnimation(anim2r);
        image14r.startAnimation(anim2r);

         anim4r = AnimationUtils.loadAnimation(this, R.anim.doubleanir4);
        image18r.startAnimation(anim4r);
        image22r.startAnimation(anim4r);
        image16r.startAnimation(anim4r);
        image24r.startAnimation(anim4r);
         anim3r = AnimationUtils.loadAnimation(this, R.anim.doubleanir3);
        image25r.startAnimation(anim3r);
        image26r.startAnimation(anim3r);
        image27r.startAnimation(anim3r);
        image28r.startAnimation(anim3r);
         animairleft = AnimationUtils.loadAnimation(this, R.anim.airleft);

         animairright = AnimationUtils.loadAnimation(this, R.anim.airright);

         animairmiddle = AnimationUtils.loadAnimation(this, R.anim.airmiddle);
        imageairr.startAnimation(animairright);
        imageairl.startAnimation(animairleft);
        imageairm.startAnimation(animairmiddle);
        rotate = AnimationUtils.loadAnimation(this, R.anim.rotate_kqjh);
        /*imagefs.setAnimation(rotate);
        imagefs.startAnimation(rotate);*/
        LinearInterpolator lin = new LinearInterpolator();//设置动画匀速运动
        rotate.setInterpolator(lin);
        imgefs.startAnimation(rotate);

        imageViewkg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag==0){
                    imageViewkg.setImageResource(R.mipmap.kqjh_kgg);
                    imgefs.setImageResource(R.mipmap.kqjh_fsg);
                    imgefs.clearAnimation();
                    imgefs.setClickable(false);
                    textViewkg.setText("关机状态");
                    image1.clearAnimation();
                    image6.clearAnimation();
                    image3.clearAnimation();
                    image7.clearAnimation();
                    image11.clearAnimation();
                    image21.clearAnimation();
                    image3.clearAnimation();
                    image12.clearAnimation();
                    imageairl.clearAnimation();
                    imageairm.clearAnimation();
                    imageairr.clearAnimation();

                    image8.clearAnimation();
                    image10.clearAnimation();

                    image4.clearAnimation();
                    image5.clearAnimation();

                    image2.clearAnimation();
                    image13.clearAnimation();
                    image9.clearAnimation();


                    image23.clearAnimation();

                    image17.clearAnimation();
                    image20.clearAnimation();

                    image15.clearAnimation();

                    image19.clearAnimation();
                    image14.clearAnimation();


                    image18.clearAnimation();
                    image22.clearAnimation();
                    image16.clearAnimation();
                    image24.clearAnimation();

                    image25.clearAnimation();
                    image26.clearAnimation();
                    image27.clearAnimation();
                    image28.clearAnimation();


                    image1r.clearAnimation();
                    image6r.clearAnimation();
                    image3r.clearAnimation();
                    image7r.clearAnimation();
                    image11r.clearAnimation();
                    image21r.clearAnimation();
                    image3r.clearAnimation();
                    image12r.clearAnimation();
                    image8r.clearAnimation();
                    image10r.clearAnimation();

                    image4r.clearAnimation();
                    image5r.clearAnimation();

                    image2r.clearAnimation();
                    image13r.clearAnimation();
                    image9r.clearAnimation();


                    image23r.clearAnimation();

                    image17r.clearAnimation();
                    image20r.clearAnimation();

                    image15r.clearAnimation();

                    image19r.clearAnimation();
                    image14r.clearAnimation();


                    image18r.clearAnimation();
                    image22r.clearAnimation();
                    image16r.clearAnimation();
                    image24r.clearAnimation();

                    image25r.clearAnimation();
                    image26r.clearAnimation();
                    image27r.clearAnimation();
                    image28r.clearAnimation();
                    imgefs.clearAnimation();

                    flag=1;
                }else {
                    imageViewkg.setImageResource(R.mipmap.kqjh_kg);
                    imgefs.setImageResource(R.mipmap.kqjh_fs);
                    imgefs.startAnimation(rotate);
                    imgefs.setClickable(true);
                    textViewkg.setText("开机状态");
                    image1.startAnimation(anim);
                    image6.startAnimation(anim);
                    image3.startAnimation(anim);
                    image7.startAnimation(anim);
                    image11.startAnimation(anim);
                    image21.startAnimation(anim);
                    image3.startAnimation(anim);
                    image12.startAnimation(anim);
                    imageairl.startAnimation(animairleft);
                    imageairm.startAnimation(animairmiddle);
                    imageairr.startAnimation(animairright);

                    image8.startAnimation(anim);
                    image10.startAnimation(anim);

                    image4.startAnimation(anim1);
                    image5.startAnimation(anim1);

                    image2.startAnimation(anim1);
                    image13.startAnimation(anim1);
                    image9.startAnimation(anim1);


                    image23.startAnimation(anim2);

                    image17.startAnimation(anim2);
                    image20.startAnimation(anim2);

                    image15.startAnimation(anim2);

                    image19.startAnimation(anim2);
                    image14.startAnimation(anim2);


                    image18.startAnimation(anim4);
                    image22.startAnimation(anim4);
                    image16.startAnimation(anim4);
                    image24.startAnimation(anim4);

                    image25.startAnimation(anim3);
                    image26.startAnimation(anim3);
                    image27.startAnimation(anim3);
                    image28.startAnimation(anim3);

                    image1r.startAnimation(animr);
                    image6r.startAnimation(animr);
                    image3r.startAnimation(animr);
                    image7r.startAnimation(animr);
                    image11r.startAnimation(animr);
                    image21r.startAnimation(animr);
                    image3r.startAnimation(animr);
                    image12r.startAnimation(animr);
                    image8r.startAnimation(animr);
                    image10r.startAnimation(animr);

                    image4r.startAnimation(animr1);
                    image5r.startAnimation(animr1);

                    image2r.startAnimation(animr1);
                    image13r.startAnimation(animr1);
                    image9r.startAnimation(animr1);


                    image23r.startAnimation(anim2r);

                    image17r.startAnimation(anim2r);
                    image20r.startAnimation(anim2r);

                    image15r.startAnimation(anim2r);

                    image19r.startAnimation(anim2r);
                    image14r.startAnimation(anim2r);


                    image18r.startAnimation(anim4r);
                    image22r.startAnimation(anim4r);
                    image16r.startAnimation(anim4r);
                    image24r.startAnimation(anim4r);

                    image25r.startAnimation(anim3r);
                    image26r.startAnimation(anim3r);
                    image27r.startAnimation(anim3r);
                    image28r.startAnimation(anim3r);
                    flag=0;
                }
            }
        });



    }
    private void initViews() {


        bottomSheetBehavior = BottomSheetBehavior.from(findViewById(R.id.bottomSheetLayout));
        bottomSheetBehavior2= BottomSheetBehavior.from(findViewById(R.id.bottomSheetLayout2));

    }
    private void initListeners() {


        imgefs.setOnClickListener(this);


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

    private void initTimer(){//设置定时时间
        timepicker1.setMaxValue(23);
        timepicker1.setMinValue(00);
        timepicker1.setValue(49);
        //timepicker1.setBackgroundColor(Color.LTGRAY);
        timepicker1.setNumberPickerDividerColor(timepicker1);
        timepicker2.setMaxValue(59);
        timepicker2.setMinValue(00);
        timepicker2.setValue(49);
        //timepicker2.setBackgroundColor(Color.LTGRAY);
        timepicker2.setNumberPickerDividerColor(timepicker2);
        timepicker3.setMaxValue(23);
        timepicker3.setMinValue(00);
        timepicker3.setValue(49);
        //timepicker3.setBackgroundColor(Color.LTGRAY);
        timepicker3.setNumberPickerDividerColor(timepicker3);
        timepicker4.setMaxValue(59);
        timepicker4.setMinValue(00);
        timepicker4.setValue(49);
        //timepicker4.setBackgroundColor(Color.LTGRAY);
        timepicker4.setNumberPickerDividerColor(timepicker4);

    }
    int hour = -1;
    int minute=-1;
    int position=-1;

    @OnClick({R.id.iv_kqjh_fh, R.id.iv_kqjh_fs,R.id.iv_kqjh_ds,R.id.iv_b_qx,R.id.iv_b2_qx,R.id.iv_b_2,R.id.iv_b_1,R.id.iv_b_3,R.id.iv_b_qd
         ,R.id.iv_b2_qd,R.id.tv_b_1,R.id.tv_b_2,R.id.tv_b_3 ,R.id.iv_kqjh_sm
    })


    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.iv_kqjh_sm://叫出风速页面
               if ("close".equals(imageViewsm.getTag())){
                   imageViewsm.setImageResource(R.mipmap.kqjh_sm);
                   anim.setDuration(2800);
                   anim1.setDuration(1900);
                   anim2.setDuration(1400);
                   anim3.setDuration(1600);
                   anim4.setDuration(1800);
                   animr.setDuration(2800);
                   animr1.setDuration(1900);
                   anim2r.setDuration(1400);
                   anim3r.setDuration(1600);
                   anim4r.setDuration(1800);
                   animairleft.setDuration(2000);
                   animairmiddle.setDuration(2000);
                   animairright.setDuration(2000);
                   rotate.setDuration(2000);//设置动画持续周期
                   rotate.setRepeatCount(-1);//设置重复次数
                   imageairr.startAnimation(animairright);
                   imageairl.startAnimation(animairleft);
                   imageairm.startAnimation(animairmiddle);
                   image1.startAnimation(anim);
                   image6.startAnimation(anim);
                   image3.startAnimation(anim);
                   image7.startAnimation(anim);
                   image11.startAnimation(anim);
                   image21.startAnimation(anim);
                   image3.startAnimation(anim);
                   image12.startAnimation(anim);


                   image8.startAnimation(anim);
                   image10.startAnimation(anim);

                   image4.startAnimation(anim1);
                   image5.startAnimation(anim1);

                   image2.startAnimation(anim1);
                   image13.startAnimation(anim1);
                   image9.startAnimation(anim1);


                   image23.startAnimation(anim2);

                   image17.startAnimation(anim2);
                   image20.startAnimation(anim2);

                   image15.startAnimation(anim2);

                   image19.startAnimation(anim2);
                   image14.startAnimation(anim2);


                   image18.startAnimation(anim4);
                   image22.startAnimation(anim4);
                   image16.startAnimation(anim4);
                   image24.startAnimation(anim4);

                   image25.startAnimation(anim3);
                   image26.startAnimation(anim3);
                   image27.startAnimation(anim3);
                   image28.startAnimation(anim3);


                   image1r.startAnimation(animr);
                   image6r.startAnimation(animr);
                   image3r.startAnimation(animr);
                   image7r.startAnimation(animr);
                   image11r.startAnimation(animr);
                   image21r.startAnimation(animr);
                   image3r.startAnimation(animr);
                   image12r.startAnimation(animr);
                   image8r.startAnimation(animr);
                   image10r.startAnimation(animr);

                   image4r.startAnimation(animr1);
                   image5r.startAnimation(animr1);

                   image2r.startAnimation(animr1);
                   image13r.startAnimation(animr1);
                   image9r.startAnimation(animr1);



                   image23r.startAnimation(anim2r);

                   image17r.startAnimation(anim2r);
                   image20r.startAnimation(anim2r);

                   image15r.startAnimation(anim2r);

                   image19r.startAnimation(anim2r);
                   image14r.startAnimation(anim2r);


                   image18r.startAnimation(anim4r);
                   image22r.startAnimation(anim4r);
                   image16r.startAnimation(anim4r);
                   image24r.startAnimation(anim4r);

                   image25r.startAnimation(anim3r);
                   image26r.startAnimation(anim3r);
                   image27r.startAnimation(anim3r);
                   image28r.startAnimation(anim3r);
                   textViewfs.setText("风速1级");
                   imageViewsm.setTag("open");
                   textViewsm.setTextColor(getResources().getColor(R.color.color_green5));
               }else if ("open".equals(imageViewsm.getTag())){
                   imageViewsm.setImageResource(R.mipmap.kqjh_smg);
                   imageViewsm.setTag("close");
                   textViewsm.setTextColor(getResources().getColor(R.color.color_gray2));
               }
                break;
            case R.id.iv_kqjh_fs://叫出风速页面
                if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                    // bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);按键可在隐藏
                } else if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_HIDDEN || bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
                break;
            case R.id.iv_kqjh_fh://返回
                finish();
                break;
            case R.id.iv_kqjh_ds://叫出风速页面
                if (bottomSheetBehavior2.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                    // bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);按键可在隐藏
                } else if (bottomSheetBehavior2.getState() == BottomSheetBehavior.STATE_HIDDEN || bottomSheetBehavior2.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                    bottomSheetBehavior2.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
                break;
            case R.id.iv_b_qx:
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                break;
            case R.id.iv_b2_qx:
                bottomSheetBehavior2.setState(BottomSheetBehavior.STATE_HIDDEN);
                break;
            case R.id.iv_b2_qd:
                int sm= timepicker2.getValue();
                int sh= timepicker1.getValue();
                int em = timepicker4.getValue();
                int eh = timepicker3.getValue();
//                if (eh>sh&&em>sm){//倒计时效果
//                     hour = eh-sh;
//                     minute = em-sm;
//                }else if (eh>sh &&em<sm)
//                    {
//                    hour = eh-sh-1;
//                    minute = 60-sm+em;
//                }else if (eh>sh &&em==sm)
//                {
//                    hour = eh-sh;
//                    minute = 0;
//                }
//                else if (eh==sh&&em>=sm){
//                    hour=0;
//                    minute=em-sm;
//                }else if (eh==sh&&em<sm){
//                    hour=23;
//                    minute=60-sm+em;
//                }else if (eh<sh&&em<sm){
//                    hour=23-sh+eh;
//                    minute=60-sm+em;
//                }else if (eh<sh&&em>sm){
//                    hour=24-sh+eh;
//                    minute=em-sm;
//                }else if (eh<sh&&em==sm){
//                    hour=24-sh+eh;
//                    minute=0;
//                }
//                textViewds.setText(hour+"小时"+minute+"分钟后开启");
                if (((eh*60+em)-(sh*60+sm))>0){
                    if (sm<10&&em<10){
                        textViewds.setText(sh+":0"+sm+"-"+eh+":0"+em);
                    }else if (sm>=10&&em<10){
                        textViewds.setText(sh+":"+sm+"-"+eh+":0"+em);
                    }else if (sm<10&&em>=10){
                        textViewds.setText(sh+":0"+sm+"-"+eh+":"+em);
                    }else if (sm>=10&&em>=10){
                        textViewds.setText(sh+":"+sm+"-"+eh+":"+em);
                    }

                }else {
                    Toast.makeText(this ,"结束时间需要大于开始时间", Toast.LENGTH_SHORT).show();
                }

                bottomSheetBehavior2.setState(BottomSheetBehavior.STATE_HIDDEN);
                break;
            case R.id.iv_b_qd:

                if (position==0){
                    anim.setDuration(2800);
                    anim1.setDuration(1900);
                    anim2.setDuration(1400);
                    anim3.setDuration(1600);
                    anim4.setDuration(1800);
                    animr.setDuration(2800);
                    animr1.setDuration(1900);
                    anim2r.setDuration(1400);
                    anim3r.setDuration(1600);
                    anim4r.setDuration(1800);
                    animairleft.setDuration(2000);
                    animairmiddle.setDuration(2000);
                    animairright.setDuration(2000);
                    rotate.setDuration(2000);//设置动画持续周期
                    rotate.setRepeatCount(-1);//设置重复次数
                    imageairr.startAnimation(animairright);
                    imageairl.startAnimation(animairleft);
                    imageairm.startAnimation(animairmiddle);
                    image1.startAnimation(anim);
                    image6.startAnimation(anim);
                    image3.startAnimation(anim);
                    image7.startAnimation(anim);
                    image11.startAnimation(anim);
                    image21.startAnimation(anim);
                    image3.startAnimation(anim);
                    image12.startAnimation(anim);


                    image8.startAnimation(anim);
                    image10.startAnimation(anim);

                    image4.startAnimation(anim1);
                    image5.startAnimation(anim1);

                    image2.startAnimation(anim1);
                    image13.startAnimation(anim1);
                    image9.startAnimation(anim1);


                    image23.startAnimation(anim2);

                    image17.startAnimation(anim2);
                    image20.startAnimation(anim2);

                    image15.startAnimation(anim2);

                    image19.startAnimation(anim2);
                    image14.startAnimation(anim2);


                    image18.startAnimation(anim4);
                    image22.startAnimation(anim4);
                    image16.startAnimation(anim4);
                    image24.startAnimation(anim4);

                    image25.startAnimation(anim3);
                    image26.startAnimation(anim3);
                    image27.startAnimation(anim3);
                    image28.startAnimation(anim3);


                    image1r.startAnimation(animr);
                    image6r.startAnimation(animr);
                    image3r.startAnimation(animr);
                    image7r.startAnimation(animr);
                    image11r.startAnimation(animr);
                    image21r.startAnimation(animr);
                    image3r.startAnimation(animr);
                    image12r.startAnimation(animr);
                    image8r.startAnimation(animr);
                    image10r.startAnimation(animr);

                    image4r.startAnimation(animr1);
                    image5r.startAnimation(animr1);

                    image2r.startAnimation(animr1);
                    image13r.startAnimation(animr1);
                    image9r.startAnimation(animr1);


                    image23r.startAnimation(anim2r);

                    image17r.startAnimation(anim2r);
                    image20r.startAnimation(anim2r);

                    image15r.startAnimation(anim2r);

                    image19r.startAnimation(anim2r);
                    image14r.startAnimation(anim2r);


                    image18r.startAnimation(anim4r);
                    image22r.startAnimation(anim4r);
                    image16r.startAnimation(anim4r);
                    image24r.startAnimation(anim4r);

                    image25r.startAnimation(anim3r);
                    image26r.startAnimation(anim3r);
                    image27r.startAnimation(anim3r);
                    image28r.startAnimation(anim3r);
                    textViewfs.setText("风速1级");
                    imageViewsm.setImageResource(R.mipmap.kqjh_smg);
                    imageViewsm.setTag("close");
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                }else if (position==1){

                    anim.setDuration(2000);
                    anim.setRepeatCount(-1);//设置重复次数
                    anim1.setDuration(1200);
                    anim1.setRepeatCount(-1);//设置重复次数
                    anim2.setDuration(1100);
                    anim2.setRepeatCount(-1);//设置重复次数
                    anim3.setDuration(1000);
                    anim3.setRepeatCount(-1);//设置重复次数
                    anim4.setDuration(1100);
                    anim4.setRepeatCount(-1);//设置重复次数
                    animr.setDuration(1800);
                    animr.setRepeatCount(-1);//设置重复次数
                    animr1.setDuration(1200);
                    animr1.setRepeatCount(-1);//设置重复次数
                    anim2r.setDuration(1100);
                    anim2r.setRepeatCount(-1);//设置重复次数
                    anim3r.setDuration(1400);
                    anim3r.setRepeatCount(-1);//设置重复次数
                    anim4r.setDuration(1800);
                    anim4r.setRepeatCount(-1);//设置重复次数
                    animairleft.setDuration(1300);
                    animairmiddle.setDuration(1300);
                    animairright.setDuration(1300);
                    imageairr.startAnimation(animairright);
                    imageairl.startAnimation(animairleft);
                    imageairm.startAnimation(animairmiddle);
                    image1.startAnimation(anim);
                    image6.startAnimation(anim);
                    image3.startAnimation(anim);
                    image7.startAnimation(anim);
                    image11.startAnimation(anim);
                    image21.startAnimation(anim);
                    image3.startAnimation(anim);
                    image12.startAnimation(anim);


                    image8.startAnimation(anim);
                    image10.startAnimation(anim);

                    image4.startAnimation(anim1);
                    image5.startAnimation(anim1);

                    image2.startAnimation(anim1);
                    image13.startAnimation(anim1);
                    image9.startAnimation(anim1);


                    image23.startAnimation(anim2);

                    image17.startAnimation(anim2);
                    image20.startAnimation(anim2);

                    image15.startAnimation(anim2);

                    image19.startAnimation(anim2);
                    image14.startAnimation(anim2);


                    image18.startAnimation(anim4);
                    image22.startAnimation(anim4);
                    image16.startAnimation(anim4);
                    image24.startAnimation(anim4);

                    image25.startAnimation(anim3);
                    image26.startAnimation(anim3);
                    image27.startAnimation(anim3);
                    image28.startAnimation(anim3);


                    image1r.startAnimation(animr);
                    image6r.startAnimation(animr);
                    image3r.startAnimation(animr);
                    image7r.startAnimation(animr);
                    image11r.startAnimation(animr);
                    image21r.startAnimation(animr);
                    image3r.startAnimation(animr);
                    image12r.startAnimation(animr);
                    image8r.startAnimation(animr);
                    image10r.startAnimation(animr);

                    image4r.startAnimation(animr1);
                    image5r.startAnimation(animr1);

                    image2r.startAnimation(animr1);
                    image13r.startAnimation(animr1);
                    image9r.startAnimation(animr1);


                    image23r.startAnimation(anim2r);

                    image17r.startAnimation(anim2r);
                    image20r.startAnimation(anim2r);

                    image15r.startAnimation(anim2r);

                    image19r.startAnimation(anim2r);
                    image14r.startAnimation(anim2r);


                    image18r.startAnimation(anim4r);
                    image22r.startAnimation(anim4r);
                    image16r.startAnimation(anim4r);
                    image24r.startAnimation(anim4r);

                    image25r.startAnimation(anim3r);
                    image26r.startAnimation(anim3r);
                    image27r.startAnimation(anim3r);
                    image28r.startAnimation(anim3r);
                    rotate.setDuration(1000);//设置动画持续周期
                    rotate.setRepeatCount(-1);//设置重复次数
                    textViewfs.setText("风速2级");
                    imageViewsm.setImageResource(R.mipmap.kqjh_smg);
                    imageViewsm.setTag("close");
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                    }else{
                    anim.setDuration(1000);
                    anim1.setDuration(800);
                    anim2.setDuration(500);
                    anim3.setDuration(900);
                    anim4.setDuration(300);
                    animr.setDuration(800);
                    animr1.setDuration(1100);
                    anim2r.setDuration(500);
                    anim3r.setDuration(400);
                    anim4r.setDuration(600);
                    animairleft.setDuration(800);
                    animairmiddle.setDuration(800);
                    animairright.setDuration(800);
                    imageairr.startAnimation(animairright);
                    imageairl.startAnimation(animairleft);
                    imageairm.startAnimation(animairmiddle);
                    image1.startAnimation(anim);
                    image6.startAnimation(anim);
                    image3.startAnimation(anim);
                    image7.startAnimation(anim);
                    image11.startAnimation(anim);
                    image21.startAnimation(anim);
                    image3.startAnimation(anim);
                    image12.startAnimation(anim);


                    image8.startAnimation(anim);
                    image10.startAnimation(anim);

                    image4.startAnimation(anim1);
                    image5.startAnimation(anim1);

                    image2.startAnimation(anim1);
                    image13.startAnimation(anim1);
                    image9.startAnimation(anim1);


                    image23.startAnimation(anim2);

                    image17.startAnimation(anim2);
                    image20.startAnimation(anim2);

                    image15.startAnimation(anim2);

                    image19.startAnimation(anim2);
                    image14.startAnimation(anim2);


                    image18.startAnimation(anim4);
                    image22.startAnimation(anim4);
                    image16.startAnimation(anim4);
                    image24.startAnimation(anim4);

                    image25.startAnimation(anim3);
                    image26.startAnimation(anim3);
                    image27.startAnimation(anim3);
                    image28.startAnimation(anim3);


                    image1r.startAnimation(animr);
                    image6r.startAnimation(animr);
                    image3r.startAnimation(animr);
                    image7r.startAnimation(animr);
                    image11r.startAnimation(animr);
                    image21r.startAnimation(animr);
                    image3r.startAnimation(animr);
                    image12r.startAnimation(animr);
                    image8r.startAnimation(animr);
                    image10r.startAnimation(animr);

                    image4r.startAnimation(animr1);
                    image5r.startAnimation(animr1);

                    image2r.startAnimation(animr1);
                    image13r.startAnimation(animr1);
                    image9r.startAnimation(animr1);


                    image23r.startAnimation(anim2r);

                    image17r.startAnimation(anim2r);
                    image20r.startAnimation(anim2r);

                    image15r.startAnimation(anim2r);

                    image19r.startAnimation(anim2r);
                    image14r.startAnimation(anim2r);


                    image18r.startAnimation(anim4r);
                    image22r.startAnimation(anim4r);
                    image16r.startAnimation(anim4r);
                    image24r.startAnimation(anim4r);

                    image25r.startAnimation(anim3r);
                    image26r.startAnimation(anim3r);
                    image27r.startAnimation(anim3r);
                    image28r.startAnimation(anim3r);
                    rotate.setDuration(500);//设置动画持续周期
                    rotate.setRepeatCount(-1);//设置重复次数
                    textViewfs.setText("风速3级");
                    imageViewsm.setImageResource(R.mipmap.kqjh_smg);
                    imageViewsm.setTag("close");
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                }
                break;
            case R.id.iv_b_2:
                position=1;
                imageb2.setImageResource(R.mipmap.csj_dj);
                imageb1.setImageResource(R.mipmap.csj_djnull);
                imageb3.setImageResource(R.mipmap.csj_djnull);

                break;
            case R.id.iv_b_1:
                position=0;
                imageb1.setImageResource(R.mipmap.csj_dj);
                imageb2.setImageResource(R.mipmap.csj_djnull);
                imageb3.setImageResource(R.mipmap.csj_djnull);

                break;
            case R.id.iv_b_3:
                position=2;
                imageb3.setImageResource(R.mipmap.csj_dj);
                imageb1.setImageResource(R.mipmap.csj_djnull);
                imageb2.setImageResource(R.mipmap.csj_djnull);

                break;
            case R.id.tv_b_2:
                position=1;
                imageb2.setImageResource(R.mipmap.csj_dj);
                imageb1.setImageResource(R.mipmap.csj_djnull);
                imageb3.setImageResource(R.mipmap.csj_djnull);

                break;
            case R.id.tv_b_1:
                position=0;
                imageb1.setImageResource(R.mipmap.csj_dj);
                imageb2.setImageResource(R.mipmap.csj_djnull);
                imageb3.setImageResource(R.mipmap.csj_djnull);

                break;
            case R.id.tv_b_3:
                position=2;
                imageb3.setImageResource(R.mipmap.csj_dj);
                imageb1.setImageResource(R.mipmap.csj_djnull);
                imageb2.setImageResource(R.mipmap.csj_djnull);

                break;
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unbinder!=null){
            unbinder.unbind();
        }
    }

}
