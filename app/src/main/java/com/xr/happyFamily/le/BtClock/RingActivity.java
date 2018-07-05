package com.xr.happyFamily.le.BtClock;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.xr.happyFamily.R;

public class RingActivity extends AppCompatActivity{
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riing);

        mediaPlayer = MediaPlayer.create(this, R.raw.music1);
        mediaPlayer.start();//一进来就播放
        mediaPlayer.setLooping(true);
    }

    public void stop(View view){//停止的方法
        mediaPlayer.stop();
        finish();//把自己的界面干掉
    }

}
