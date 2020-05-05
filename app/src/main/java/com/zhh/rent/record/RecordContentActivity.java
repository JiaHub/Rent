package com.zhh.rent.record;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.gson.Gson;
import com.zhh.rent.R;
import com.zhh.rent.sqlite.Rent;
import com.zhh.rent.sqlite.RentDao;

public class RecordContentActivity extends AppCompatActivity {

    Button mBtnStartTime,mBtnEndTime;
    TextView mTvMonth,mTvWater,mTvElectric,mTvDays,mTvResult,mTvCalculateTime;
    ImageButton mIBtnBack,mIBtnDelete;

    Rent rent;
    RentDao rentDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record_content_activity);
        rentDao = new RentDao(this);
        initView();
        initDate();
        setBtnClickListener();
    }

    private void initView(){
        mBtnEndTime = findViewById(R.id.record_end_time);
        mBtnStartTime = findViewById(R.id.record_start_time);

        mTvDays = findViewById(R.id.record_days);
        mTvElectric = findViewById(R.id.record_electric);
        mTvMonth = findViewById(R.id.record_month_rent);
        mTvResult = findViewById(R.id.record_result);
        mTvWater = findViewById(R.id.record_water);
        mTvCalculateTime = findViewById(R.id.record_time);

        mIBtnBack = findViewById(R.id.back);
        mIBtnDelete = findViewById(R.id.delete);
    }

    private void initDate(){
        Intent intent = getIntent();
        Gson gson = new Gson();
        String rentJson = intent.getStringExtra("rent");
        if(rentJson==null||rentJson.isEmpty())return;
        rent = gson.fromJson(rentJson,Rent.class);

        mBtnEndTime.setText(rent.getEndTime());
        mBtnStartTime.setText(rent.getStartTime());

        mTvWater.setText(rent.getRentWater());
        mTvResult.setText(rent.getRentResult());
        mTvMonth.setText(rent.getMonthRent());
        mTvDays.setText(rent.getRentDays());
        mTvElectric.setText(rent.getRentElectric());
        mTvCalculateTime.setText(rent.getNowTime());
    }

    private void setBtnClickListener(){
        mIBtnDelete.setOnClickListener(new btnClickListener());
        mIBtnBack.setOnClickListener(new btnClickListener());
    }

    private class btnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.back:
                    finish();
                    break;
                case R.id.delete:
                    rentDao.deleteRent(rent.getId());
                    Log.i("RentDao","删除了id为"+rent.getId());
                    finish();
                    break;
                default:
                    break;
            }
        }
    }
}
