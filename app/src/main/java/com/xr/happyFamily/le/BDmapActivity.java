package com.xr.happyFamily.le;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.baidu.mapapi.SDKInitializer;
import com.xr.database.dao.DerailBeanDao;
import com.xr.database.dao.daoimpl.DerailBeanDaoImpl;
import com.xr.database.dao.daoimpl.DerailResultDaoImpl;
import com.xr.happyFamily.R;
import com.xr.happyFamily.jia.MyApplication;
import com.xr.happyFamily.le.pojo.DerailBean;
import com.xr.happyFamily.le.pojo.MapAdress;
import com.xr.happyFamily.together.MyDialog;
import com.xr.happyFamily.together.http.HttpUtils;
import com.xr.happyFamily.together.util.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import crossoverone.statuslib.StatusUtil;

public class BDmapActivity extends AppCompatActivity {
    private LocationClient mLocationClient;
    private LocationClientOption mOption;
    TextView locationResult;
    @BindView(R.id.tv_map_name)
    TextView tv_map_name;
    String derailId;
    SharedPreferences preferences;
    String ip = "http://47.98.131.11:8084";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_le_bdmap);
        ButterKnife.bind(this);
        preferences = getSharedPreferences("my", MODE_PRIVATE);
        derailId = preferences.getString("derailId", "");
        new getSiteData().execute();
    }

    String name="他";
    class getSiteData extends AsyncTask<Void, Void, Integer> {

        @Override
        protected Integer doInBackground(Void... voids) {
            int code = 0;
            String url = ip + "/happy/derailed/getDerailModel?derailId=" + derailId+"&derailRole="+1;
            String result = HttpUtils.getOkHpptRequest(url);
            Log.e("ressssssss11", "doInBackground: -->" + result);
            try {
                if (!TextUtils.isEmpty(result)) {
                    JSONObject jsonObject = new JSONObject(result);
                    code = jsonObject.getInt("returnCode");
                    JSONObject jsonObject1 = jsonObject.getJSONObject("returnData");
                     name = jsonObject1.getString("username");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return code;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            switch (integer) {
                case 100:
                    tv_map_name.setText("你已经被“"+name+"”绑定");
                    break;
                default:

                    break;
            }
        }
    }


    @OnClick({R.id.iv_bdmap_back})
    public void onClick (View view ){
        switch (view.getId()){
            case R.id.iv_bdmap_back:
                finish();
                Log.e("dddd", "onClick: -----" );
                break;
        }
    }





    @Override
    protected void onDestroy() {
        ButterKnife.bind(this).unbind();
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }



}
