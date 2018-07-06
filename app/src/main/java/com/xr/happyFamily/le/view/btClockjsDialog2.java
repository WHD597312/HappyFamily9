package com.xr.happyFamily.le.view;

import android.app.Dialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xr.happyFamily.R;
import com.xr.happyFamily.together.util.Utils;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 制懒闹钟脑筋急转弯关闭
 */
public class btClockjsDialog2 extends Dialog {


    @BindView(R.id.tv_zl_time)
    TextView tv_zl_time;
    @BindView(R.id.tv_zl_njwt)
    TextView tv_zl_njwt;
    @BindView(R.id.tv_zl_xx1)
    TextView tv_zl_xx1;
    @BindView(R.id.tv_zl_xx2)
    TextView tv_zl_x2;
    @BindView(R.id.tv_zl_xx3)
    TextView tv_zl_xx3;
    @BindView(R.id.iv_zl_njxx1)
    ImageView iv_zl_njxx1;
    @BindView(R.id.iv_zl_njxx2)
    ImageView iv_zl_njxx2;
    @BindView(R.id.iv_zl_njxx3)
    ImageView iv_zl_njxx3;
    @BindView(R.id.bt_zl_njqd)
    Button bt_zl_njqd;

    private String name;

    String text;
    Context mcontext;
    private MediaPlayer mediaPlayer;
//    public static final int FLAG_HOMEKEY_DISPATCHED = 0x80000000;//定义屏蔽参数
    public btClockjsDialog2(@NonNull Context context) {
        super(context, R.style.MyDialog);
        mcontext=context;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_le_zldialog3);
        ButterKnife.bind(this);
//        Calendar calendar = Calendar.getInstance();
//        int hour = calendar.get(Calendar.HOUR_OF_DAY);
//        int minute=calendar.get(Calendar.MINUTE);
//        tv_zl_time.setText(hour+":"+minute);
        mediaPlayer = MediaPlayer.create(mcontext, R.raw.music1);
        mediaPlayer.start();//一进来就播放
        mediaPlayer.setLooping(true);
        iv_zl_njxx1.setTag("open");
        iv_zl_njxx2.setTag("close");
        iv_zl_njxx3.setTag("close");
    }




    @Override
    protected void onStart() {
        super.onStart();

    }


   public String getText(){
        return text;
   }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @OnClick({R.id.iv_zl_njxx1,R.id.iv_zl_njxx2,R.id.iv_zl_njxx3,R.id.bt_zl_njqd})
    public void onClick(View view){
        switch(view.getId()){
            case R.id.ib_zl_sc:
                if (onNegativeClickListener!=null){
                    onNegativeClickListener.onNegativeClick();
                }

                break;
            case R.id.ib_zl_qd:
                if (onPositiveClickListener!=null){

                    onPositiveClickListener.onPositiveClick();
                }


            {
                   dismiss();
                   mediaPlayer.stop();
                }
                    Toast.makeText(mcontext,"输入错误请从新输入",Toast.LENGTH_SHORT).show();


                break;

            case R.id.iv_zl_njxx1:
                if ("close".equals(iv_zl_njxx1.getTag())){
                    iv_zl_njxx1.setImageResource(R.mipmap.bt_zlzd);
                    iv_zl_njxx1.setTag("open");
                    iv_zl_njxx2.setTag("close");
                    iv_zl_njxx3.setTag("close");
                }
                break;

            case R.id.iv_zl_njxx2:
                if ("close".equals(iv_zl_njxx2.getTag())){
                    iv_zl_njxx2.setImageResource(R.mipmap.bt_zlzd);
                    iv_zl_njxx2.setTag("open");
                    iv_zl_njxx1.setTag("close");
                    iv_zl_njxx3.setTag("close");
                }
                break;

            case R.id.iv_zl_njxx3:
                if ("close".equals(iv_zl_njxx3.getTag())){
                    iv_zl_njxx3.setImageResource(R.mipmap.bt_zlzd);
                    iv_zl_njxx3.setTag("open");
                    iv_zl_njxx1.setTag("close");
                    iv_zl_njxx2.setTag("close");
                }
                break;

            case R.id.bt_zl_njqd:

                break;


        }
    }
    private   OnKeyListener onKeyListener;
    private OnPositiveClickListener onPositiveClickListener;

    public void setOnKeyListener(OnKeyListener onKeyListener){
        this.onKeyListener=onKeyListener;
    }
    public void setOnPositiveClickListener(OnPositiveClickListener onPositiveClickListener) {


        this.onPositiveClickListener = onPositiveClickListener;
    }

    private OnNegativeClickListener onNegativeClickListener;

    public void setOnNegativeClickListener(OnNegativeClickListener onNegativeClickListener) {

        this.onNegativeClickListener = onNegativeClickListener;
    }

    public interface OnPositiveClickListener {
        void onPositiveClick();
    }

    public interface OnNegativeClickListener {
        void onNegativeClick();
    }
    public interface OnKeyListener{
        void  OnKeyListener();
    }
}
