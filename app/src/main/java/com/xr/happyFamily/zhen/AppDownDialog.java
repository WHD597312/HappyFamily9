package com.xr.happyFamily.zhen;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ProgressBar;

import com.xr.happyFamily.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 删除房间
 */
public class AppDownDialog extends Dialog {

    private String name;
    @BindView(R.id.progress)
    ProgressBar progress;
    Context context;
    public AppDownDialog(@NonNull Context context) {
        super(context, R.style.MyDialog);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popview_delete_device);
        progress= (ProgressBar) findViewById(R.id.progress);
        ButterKnife.bind(this);

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    public ProgressBar getProgress() {
        return progress;
    }

    public void setProgress(ProgressBar progress) {
        this.progress = progress;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @OnClick({R.id.tv_device_cancel, R.id.tv_device_ensure})
    public void onClick(View view){
        switch(view.getId()){
            case R.id.tv_device_cancel:
                if (onNegativeClickListener!=null){
                    onNegativeClickListener.onNegativeClick();
                }
                break;
            case R.id.tv_device_ensure:
                if (onPositiveClickListener!=null){

                    onPositiveClickListener.onPositiveClick();
                }

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
