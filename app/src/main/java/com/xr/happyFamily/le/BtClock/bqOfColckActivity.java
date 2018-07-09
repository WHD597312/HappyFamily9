package com.xr.happyFamily.le.BtClock;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.xr.happyFamily.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/***
 * 标签页面
 * **/

public class bqOfColckActivity extends AppCompatActivity {
    @BindView(R.id.et_le_bq)
    EditText et_le_bq;
    String text;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_le_bq);
        ButterKnife.bind(this);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

    }
    @OnClick({R.id.tv_le_bqqx,R.id.tv_le_bqqd})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.tv_le_bqqx:
                finish();
                break;
            case R.id.tv_le_bqqd:
                text=et_le_bq.getText().toString();
                Log.i("text", "onCreate: -->"+text);
                Intent intent = new Intent();
                intent.putExtra("text",text);
                setResult(666,intent);
                finish();
                break;

        }
    }
}
