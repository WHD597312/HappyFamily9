package com.xr.happyFamily.le.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import com.xr.happyFamily.R;
import java.util.Calendar;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 制懒闹钟选择关闭方式
 * 算一算
 * 脑际急转弯
 * 听歌识曲
 */
public class btClockjsDialog3 extends Dialog {



    @BindView(R.id.iv_zl_xz1)
    ImageView iv_zl_xz1;
    @BindView(R.id.iv_zl_xz2)
    ImageView iv_zl_xz2;
    @BindView(R.id.iv_zl_xz3)
    ImageView iv_zl_xz3;


    private String name;

    String text;
    Context mcontext;

    public btClockjsDialog3(@NonNull Context context) {
        super(context, R.style.MyDialog);
        mcontext=context;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_le_zldialog1);
        ButterKnife.bind(this);
        iv_zl_xz1.setTag("open");
        iv_zl_xz2.setTag("close");
        iv_zl_xz3.setTag("close");
        text="听歌识曲";
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

    @OnClick({R.id.rl_zl_tgsq,R.id.rl_zl_njjzw,R.id.rl_zl_sys,R.id.bt_zl_qd,R.id.bt_zl_qx})
    public void onClick(View view){
        switch(view.getId()){
            case R.id.bt_zl_qx:
                if (onNegativeClickListener!=null){
                    onNegativeClickListener.onNegativeClick();
                }

                break;
            case R.id.bt_zl_qd:
                if (onPositiveClickListener!=null){

                    onPositiveClickListener.onPositiveClick();
                }


                break;

            case R.id.rl_zl_tgsq:
                if ("close".equals(iv_zl_xz1.getTag())){
                    iv_zl_xz1.setImageResource(R.mipmap.lrclock_dh);
                    iv_zl_xz2.setImageResource(0);
                    iv_zl_xz3.setImageResource(0);
                    iv_zl_xz1.setTag("open");
                    iv_zl_xz2.setTag("close");
                    iv_zl_xz3.setTag("close");
                    text="听歌识曲";
                }
                break;

            case R.id.rl_zl_njjzw:
                if ("close".equals(iv_zl_xz2.getTag())){
                    iv_zl_xz2.setImageResource(R.mipmap.lrclock_dh);
                    iv_zl_xz1.setImageResource(0);
                    iv_zl_xz3.setImageResource(0);
                    iv_zl_xz2.setTag("open");
                    iv_zl_xz1.setTag("close");
                    iv_zl_xz3.setTag("close");
                    text="脑筋急转弯";
                }
                break;

            case R.id.rl_zl_sys:
                if ("close".equals(iv_zl_xz3.getTag())){
                    iv_zl_xz3.setImageResource(R.mipmap.lrclock_dh);
                    iv_zl_xz1.setImageResource(0);
                    iv_zl_xz2.setImageResource(0);
                    iv_zl_xz3.setTag("open");
                    iv_zl_xz1.setTag("close");
                    iv_zl_xz2.setTag("close");
                    text="算一算";
                }
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
