package com.xr.happyFamily.le.BtClock;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.xr.happyFamily.R;
import com.xr.happyFamily.together.http.HttpUtils;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class TimeRemFragment extends Fragment {
    View view;
    Unbinder unbinder;
    String ip = "http://47.98.131.11:8084";
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_le_sgjs1, container, false);
        unbinder = ButterKnife.bind(this, view);

        return view;
    }


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


                    break;
                default:

                    break;
            }
        }
    }
}
