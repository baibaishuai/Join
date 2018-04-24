package com.lhf.join.View.Stadium;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.lhf.join.Bean.Stadium;
import com.lhf.join.Bean.User;
import com.lhf.join.R;

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
        getWindow().setStatusBarColor(Color.parseColor("#FF029ACC"));

    }

    private void initdata() {
        user = (User) getIntent().getSerializableExtra("user");
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


    }


}
