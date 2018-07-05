package com.xr.happyFamily.le.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.xr.happyFamily.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 删除房间
 */
public class btClockjsDialog extends Dialog {


    @BindView(R.id.tv_zl_js)
    TextView tv_zl_js;
    @BindView(R.id.tv_zl_jg)
    TextView tv_zl_jg;
    private String name;
    int x;
    int y;
    String text;
    Context context;
    public btClockjsDialog(@NonNull Context context) {
        super(context, R.style.MyDialog);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_le_zldialog2);
        ButterKnife.bind(this);
        x= (int)(Math.random()*1000);
        y= (int)(Math.random()*1000);
        tv_zl_js.setText(x+"×"+y+"=");

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    public int getX(){
        return  x;
    }
    public int getY(){
        return y;
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

    @OnClick({R.id.ib_zl_sc, R.id.ib_zl_qd,R.id.bt_zl_sz1,R.id.bt_zl_sz2,R.id.bt_zl_sz3,R.id.bt_zl_sz4,
            R.id.bt_zl_sz5,R.id.bt_zl_sz6,R.id.bt_zl_sz7,R.id.bt_zl_sz8,R.id.bt_zl_sz9,R.id.bt_zl_sz0})
    public void onClick(View view){
        switch(view.getId()){
            case R.id.ib_zl_sc:
                if (onNegativeClickListener!=null){
                    onNegativeClickListener.onNegativeClick();
                }
                tv_zl_jg.setText(" ");
                break;
            case R.id.ib_zl_qd:
                if (onPositiveClickListener!=null){

                    onPositiveClickListener.onPositiveClick();
                }
                text=String.valueOf(tv_zl_js.getText());
                break;

            case R.id.bt_zl_sz1:
                tv_zl_jg.append("1");
                break;

            case R.id.bt_zl_sz2:
                tv_zl_jg.append("2");
                break;

            case R.id.bt_zl_sz3:
                tv_zl_jg.append("3");
                break;

            case R.id.bt_zl_sz4:
                tv_zl_jg.append("4");
                break;

            case R.id.bt_zl_sz5:
                tv_zl_jg.append("5");
                break;

            case R.id.bt_zl_sz6:
                tv_zl_jg.append("6");
                break;

            case R.id.bt_zl_sz7:
                tv_zl_jg.append("7");
                break;

            case R.id.bt_zl_sz8:
                tv_zl_jg.append("8");
                break;

            case R.id.bt_zl_sz9:
                tv_zl_jg.append("9");
                break;

            case R.id.bt_zl_sz0:
                tv_zl_jg.append("0");
                break;
        }
    }
    private OnPositiveClickListener onPositiveClickListener;

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
}
