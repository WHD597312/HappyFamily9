package com.xr.happyFamily.jia.xnty;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;


import com.xr.happyFamily.R;
import com.xr.happyFamily.jia.MainActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class SmartSocket extends AppCompatActivity {
    Unbinder unbinder;
    Dialog dia ;

    @BindView(R.id.zncz_adde)
    ImageView imageView1;


    private Context context;
    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xnty_zncz);
        unbinder = ButterKnife.bind(this);
        Context context = SmartSocket.this;
        dia = new Dialog(context, R.style.edit_AlertDialog_style);
        dia.setContentView(R.layout.activity_zncz_dialog);
        ImageView imageView = (ImageView) dia.findViewById(R.id.iv_dialog1);
        imageView.setBackgroundResource(R.mipmap.zncz_dialog);
        dia.show();

        dia.setCanceledOnTouchOutside(true); // Sets whether this dialog is
        Window w = dia.getWindow();
        WindowManager.LayoutParams lp = w.getAttributes();
        lp.x = 0;
        lp.y = 40;
        dia.onWindowAttributesChanged(lp);
    }
    @OnClick({R.id.zncz_adde, R.id.iv_zncz_fanh})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.zncz_adde:
                startActivity(new Intent(this, ZnczListActivity.class));
                break;
            case R.id.iv_zncz_fanh:
                finish();
                break;

        }
    }
}
