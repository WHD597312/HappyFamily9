package com.xr.happyFamily.jia;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.xr.database.dao.daoimpl.HourseDaoImpl;
import com.xr.happyFamily.R;
import com.xr.happyFamily.jia.adapter.HouseAdapter;
import com.xr.happyFamily.jia.pojo.Hourse;
import com.xr.happyFamily.jia.titleview.TitleView;
import com.xr.happyFamily.together.http.HttpUtils;
import com.xr.happyFamily.together.util.Utils;
import org.json.JSONObject;
import java.util.List;
import java.util.Map;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class HourseActivity extends AppCompatActivity {
    Unbinder unbinder;
    TitleView titleView;
    String ip = "http://47.98.131.11:8084;";
    SharedPreferences preferences;
    String houseName;
    String houseAddress;
    private HourseDaoImpl hourseDao;
   @BindView(R.id.lv_hourse)
   RecyclerView recyclerView;
    List<Hourse> hourses;
    HouseAdapter ListAdapter;


    protected void onCreate(Bundle savadInstanceState) {
        super.onCreate(savadInstanceState);

        setContentView(R.layout.activity_home_hourse);
        unbinder = ButterKnife.bind(this);
        titleView = (TitleView) findViewById(R.id.title_addroom);
        titleView.setTitleText("家庭管理");

//        preferences = getSharedPreferences("my", MODE_PRIVATE);
//        houseName=preferences.getString("phone", "");
//        houseAddress=preferences.getString("password", "");

        hourseDao=new HourseDaoImpl(getApplicationContext());
        hourses = hourseDao.findAllHouse();


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        ListAdapter = new HouseAdapter(this, hourses);

        recyclerView.setAdapter(ListAdapter);
//        ListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    class HourseAsyncTask extends AsyncTask<Map<String, Object>, Void, Integer> {

        @Override
        protected Integer doInBackground(Map<String, Object>... maps) {
            int code = 0;

            String url =ip+ "/family/room/getExistRooms?houseId=" ;
            String result = HttpUtils.getOkHpptRequest(url);

            try {
                if (!Utils.isEmpty(result)) {
                    JSONObject jsonObject = new JSONObject(result);
                    code = jsonObject.getInt("returnCode");

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return code;
        }


        protected void onPostExecute(Integer code) {
            super.onPostExecute(code);
            switch (code) {

                case 100:

                    break;
            }
        }
    }

    @OnClick({R.id.tv_hourse_xjjt})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_hourse_xjjt:
                startActivity(new Intent(this, AddhourseActivity.class));
                break;


        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }


}
