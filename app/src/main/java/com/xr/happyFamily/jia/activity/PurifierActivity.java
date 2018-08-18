package com.xr.happyFamily.jia.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.xr.happyFamily.R;
import com.xr.happyFamily.bao.view.DoubleWaveView;

public class PurifierActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purifier);

        DoubleWaveView doubleWaveView= (DoubleWaveView) findViewById(R.id.doubleW);
        ImageView img= (ImageView) findViewById(R.id.img);
        doubleWaveView.setProHeight(30);
    }
}
