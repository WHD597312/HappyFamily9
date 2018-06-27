package com.xr.happyFamily.jia.xnty;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.xr.happyFamily.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class Smartsj extends AppCompatActivity {
Unbinder unbinder;
    @BindView(R.id.iv_zncz_sjf)
    ImageView imageViewsjf;
    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_zncz_sj);
        unbinder = ButterKnife.bind(this);
    }

    @OnClick({R.id.iv_zncz_sjf})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_zncz_sjf:
                finish();
                break;
        }
    }
}
