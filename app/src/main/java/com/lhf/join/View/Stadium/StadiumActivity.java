package com.lhf.join.View.Stadium;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.lhf.join.Adapter.StadiumAdapter;
import com.lhf.join.Bean.Stadium;
import com.lhf.join.Bean.User;
import com.lhf.join.R;
import com.lhf.join.View.Find.InsertNeedActivity;
import com.sackcentury.shinebuttonlib.ShineButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.lhf.join.Constant.Constant.URL_DELETECOLLECTION;
import static com.lhf.join.Constant.Constant.URL_INSERTCOLLECTION;
import static com.lhf.join.Constant.Constant.URL_ISCOLLECTED;
import static com.lhf.join.Constant.Constant.URL_PICTURE;
import static com.lhf.join.Constant.Constant.URL_SEARCHSTADIUM;

public class StadiumActivity extends AppCompatActivity {
    private TextView tv;
    private TextView tv_stadiumname;
    private TextView tv_area;
    private TextView tv_type;
    private TextView tv_num;
    private TextView tv_indoor;
    private TextView tv_aircondition;
    private TextView tv_adress;
    private Stadium stadium;
    private ImageView icon_back;
    private ImageView icon_stadium;
    private RatingBar ratingBar;
    private Button btn_order;
    private User user;
    private int collect = 0;
    private ToggleButton shineButton;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stadium);
        stadium = (Stadium) getIntent().getSerializableExtra("stadium");
        initview();
        initdata();


    }

    private void initview() {
        tv = findViewById(R.id.tv_stadiumname);
        icon_back = findViewById(R.id.icon_back);
        icon_stadium = findViewById(R.id.icon_stadium);
        tv_stadiumname = findViewById(R.id.tv_stadiumname1);
        tv_type = findViewById(R.id.tv_changguan_type);
        tv_area = findViewById(R.id.tv_area);
        tv_num = findViewById(R.id.tv_num);
        tv_indoor = findViewById(R.id.tv_indoor);
        tv_aircondition = findViewById(R.id.tv_aircondition);
        tv_adress = findViewById(R.id.tv_adress);
        ratingBar = findViewById(R.id.ratbar);
        btn_order = findViewById(R.id.btn_order);
        shineButton = findViewById(R.id.po_image3);

        getWindow().setStatusBarColor(Color.parseColor("#FF029ACC"));

    }

    private void initdata() {
        shineButton.setChecked(true);
        user = (User) getIntent().getSerializableExtra("user");
        isCollectd(stadium.getStadiumId(), user.getUserId());
        System.out.println("userId:" + user.getUserId());
        icon_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv.setText(stadium.getStadiumname());
        tv_stadiumname.setText(stadium.getStadiumname());
        tv_type.setText(stadium.getStadiumtype());
        tv_area.setText("面积:" + stadium.getArea() + "平方米");
        tv_num.setText("可容纳:" + stadium.getNum() + "人");
        if (stadium.getIndoor() == 1) {
            tv_indoor.setText("是否室内：是");
        } else {
            tv_indoor.setText("是否室内：否");
        }
        if (stadium.getAircondition() == 1) {
            tv_aircondition.setText("是否有空调：是");
        } else {
            tv_aircondition.setText("是否有空调：否");
        }
        tv_adress.setText("地址:" + stadium.getCity() + stadium.getAdress());
        Glide.with(this)
                .load(stadium.getMainpicture())
                .placeholder(R.drawable.loading)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .error(R.drawable.error)
                .into(icon_stadium);
        btn_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StadiumActivity.this, OrderStadiumActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putSerializable("user", user);
                mBundle.putSerializable("stadium", stadium);
                intent.putExtras(mBundle);
                startActivity(intent);
            }
        });
        shineButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isChecked()) {
                    if (collect == 1) {
                        collect(stadium.getStadiumId(), user.getUserId(), true);
                    } else {

                    }
                } else {
                    if (collect == 1) {
                        collect(stadium.getStadiumId(), user.getUserId(), false);
                    } else {

                    }
                }
            }
        });


    }

    private void collect(int stadiunmId, int userId, boolean flag) {
        String SearchUrl = null;
        if (flag) {
            SearchUrl = URL_INSERTCOLLECTION;
        } else {
            SearchUrl = URL_DELETECOLLECTION;
        }
        new insertCollectionAsyncTask().execute(SearchUrl, String.valueOf(stadiunmId), String.valueOf(userId));
    }

    private class insertCollectionAsyncTask extends AsyncTask<String, Integer, String> {
        public insertCollectionAsyncTask() {
        }

        @Override
        protected String doInBackground(String... params) {
            Response response = null;
            String results = null;
            JSONObject json = new JSONObject();
            try {
                json.put("stadiumId", params[1]);
                json.put("userId", params[2]);
                OkHttpClient okHttpClient = new OkHttpClient();
                RequestBody requestBody = RequestBody.create(JSON, String.valueOf(json));
                Request request = new Request.Builder()
                        .url(params[0])
                        .post(requestBody)
                        .build();
                response = okHttpClient.newCall(request).execute();
                results = response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return results;
        }

        @Override
        protected void onPostExecute(String s) {
            System.out.println("返回的数据：" + s);
            if (!"".equals(s)) {
                try {
                    JSONObject results = new JSONObject(s);
                    String loginresult = results.getString("result");
                    if (loginresult.equals("1")) {
                        Toast.makeText(StadiumActivity.this, "收藏成功", Toast.LENGTH_SHORT).show();
                    } else if (loginresult.equals("2")) {
                        Toast.makeText(StadiumActivity.this, "取消收藏", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(StadiumActivity.this, "系统异常", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("结果为空");
            }
        }
    }

    private void isCollectd(int stadiunmId, int userId) {
        collect = collect + 1;
        String SearchUrl = URL_ISCOLLECTED;
        new isCollectionAsyncTask().execute(SearchUrl, String.valueOf(stadiunmId), String.valueOf(userId));
    }

    private class isCollectionAsyncTask extends AsyncTask<String, Integer, String> {
        public isCollectionAsyncTask() {
        }

        @Override
        protected String doInBackground(String... params) {
            Response response = null;
            String results = null;
            JSONObject json = new JSONObject();
            try {
                json.put("stadiumId", params[1]);
                json.put("userId", params[2]);
                OkHttpClient okHttpClient = new OkHttpClient();
                RequestBody requestBody = RequestBody.create(JSON, String.valueOf(json));
                Request request = new Request.Builder()
                        .url(params[0])
                        .post(requestBody)
                        .build();
                response = okHttpClient.newCall(request).execute();
                results = response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return results;
        }

        @Override
        protected void onPostExecute(String s) {
            System.out.println("返回的数据：" + s);
            if (!"".equals(s)) {
                try {
                    JSONObject results = new JSONObject(s);
                    String loginresult = results.getString("result");
                    if (loginresult.equals("1")) {
                        shineButton.setChecked(false);
                    } else {
                        shineButton.setChecked(true);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("结果为空");
            }
        }
    }
}
