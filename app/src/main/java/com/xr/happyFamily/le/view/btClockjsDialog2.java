package com.xr.happyFamily.le.view;

import android.app.Dialog;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xr.happyFamily.R;
import com.xr.happyFamily.together.http.HttpUtils;
import com.xr.happyFamily.together.util.Utils;

import org.json.JSONObject;

import java.io.IOException;
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
    TextView tv_zl_xx2;
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
    String ip = "http://47.98.131.11:8084";
    private String name;

    String text;
    Context mcontext;
    String choose;
    private MediaPlayer mediaPlayer;
    private AudioManager audioMa;
    public btClockjsDialog2(@NonNull Context context) {
        super(context, R.style.MyDialog);
        mcontext=context;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_le_zldialog3);
        ButterKnife.bind(this);
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute=calendar.get(Calendar.MINUTE);
        tv_zl_time.setText(hour+":"+minute);
        mediaPlayer = MediaPlayer.create(mcontext, R.raw.music2);
        mediaPlayer.start();//一进来就播放
        mediaPlayer.setLooping(true);
        iv_zl_njxx1.setTag("open");
        iv_zl_njxx2.setTag("close");
        iv_zl_njxx3.setTag("close");
        choose="a";
        new getQuestionAsync().execute();
        audioMa = (AudioManager)mcontext.getSystemService(Context.AUDIO_SERVICE);
        audioMa.setStreamVolume(AudioManager.STREAM_MUSIC,audioMa.getStreamMaxVolume
                (AudioManager.STREAM_MUSIC),AudioManager.FLAG_SHOW_UI);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
// TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            Log.i("11111", "======KEYCODE_VOLUME_DOWN======>>>");
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            Log.i("11111", "======KEYCODE_VOLUME_UP======>>>");
            return true;
        }
        return false;
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
                    iv_zl_njxx2.setImageResource(R.mipmap.bt_zlhd);
                    iv_zl_njxx3.setImageResource(R.mipmap.bt_zlhd);
                    iv_zl_njxx1.setTag("open");
                    iv_zl_njxx2.setTag("close");
                    iv_zl_njxx3.setTag("close");
                    choose="a";
                }
                break;

            case R.id.iv_zl_njxx2:
                if ("close".equals(iv_zl_njxx2.getTag())){
                    iv_zl_njxx2.setImageResource(R.mipmap.bt_zlzd);
                    iv_zl_njxx1.setImageResource(R.mipmap.bt_zlhd);
                    iv_zl_njxx3.setImageResource(R.mipmap.bt_zlhd);
                    iv_zl_njxx2.setTag("open");
                    iv_zl_njxx1.setTag("close");
                    iv_zl_njxx3.setTag("close");
                    choose="b";
                }
                break;

            case R.id.iv_zl_njxx3:
                if ("close".equals(iv_zl_njxx3.getTag())){
                    iv_zl_njxx3.setImageResource(R.mipmap.bt_zlzd);
                    iv_zl_njxx2.setImageResource(R.mipmap.bt_zlhd);
                    iv_zl_njxx1.setImageResource(R.mipmap.bt_zlhd);
                    iv_zl_njxx3.setTag("open");
                    iv_zl_njxx1.setTag("close");
                    iv_zl_njxx2.setTag("close");
                    choose="c";
                }
                break;

            case R.id.bt_zl_njqd:
                if (choose.equals(answer)){
                    mediaPlayer.stop();
                    dismiss();
                }else {
                    Toast.makeText(mcontext, "输入错误请从新输入", Toast.LENGTH_SHORT).show();
                    new getQuestionAsync().execute();
                }
                break;


        }
    }
    String question ;
    String  answer;
    String A;
    String B;
    String C;
    class getQuestionAsync extends AsyncTask<Void,Void,Integer> {

        @Override
        protected Integer doInBackground(Void... voids) {
            int code=0;
            String url=ip+"/happy/clock/getClockLazyRiddle";
            String result= HttpUtils.getOkHpptRequest(url);
            Log.i("result","-->"+result);
            try {
                if (!TextUtils.isEmpty(result)){
                    JSONObject jsonObject=new JSONObject(result);
                    String returnCode=jsonObject.getString("returnCode");
                    JSONObject returnData = jsonObject.getJSONObject("returnData");

                    if ("100".equals(returnCode)) {
                        code = 100;
                        question=returnData.getString("question");
                        answer = returnData.getString("answer");
                        A=returnData.getString("a");
                        B=returnData.getString("b");
                        C=returnData.getString("c");
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return code;
        }
        @Override
        protected void onPostExecute(Integer code) {
            super.onPostExecute(code);
            switch (code){
                case 100:
                    tv_zl_njwt.setText(question+"?");
                    Log.e(question, "onPostExecute: "+question );
                    tv_zl_xx1.setText(A);
                    tv_zl_xx2.setText(B);
                    tv_zl_xx3.setText(C);

                    break;
                default:

                    break;
            }
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
