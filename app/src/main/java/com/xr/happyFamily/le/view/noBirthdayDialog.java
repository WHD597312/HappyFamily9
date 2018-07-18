package com.xr.happyFamily.le.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;

import com.xr.happyFamily.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 没有生日跳转到朕的生日修改界面
 */
public class noBirthdayDialog extends Dialog {



    private String name;


    Context mcontext;

    public noBirthdayDialog(@NonNull Context context) {
        super(context, R.style.MyDialog);
        mcontext=context;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popview_nobirthday);
        ButterKnife.bind(this);

    }




    @Override
    protected void onStart() {
        super.onStart();

    }



    @OnClick({R.id.bt_birthday_cancel,R.id.bt_birthday_ensure})
    public void onClick(View view){
        switch(view.getId()){
            case R.id.bt_birthday_cancel:
                if (onNegativeClickListener!=null){
                    onNegativeClickListener.onNegativeClick();
                }

                break;
            case R.id.bt_birthday_ensure:
                if (onPositiveClickListener!=null){

                    onPositiveClickListener.onPositiveClick();
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
