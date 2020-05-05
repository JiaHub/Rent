package com.zhh.rent.activity;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhh.rent.R;
import com.zhh.rent.sqlite.Rent;
import com.zhh.rent.sqlite.RentDao;
import com.zhh.rent.wheel.adapter.NumericWheelAdapter;
import com.zhh.rent.wheel.widget.WheelView;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 用于计算房租
 * 按钮 开始时间 截止时间 开始计算
 * 输入框 电费 水费
 * 文本框 计算天数 计算结果
 */
public class RentActivity extends AppCompatActivity {

    Button   mBtnStartTime,mBtnEndTime,mBtnSubmit;
    EditText mEtElectric,mEtWater,mEtMonthRent;
    TextView mTvDays,mTvResult;

    Integer rentMouth,rentWater,rentElectric;

    String strStartTime="2020-1-1",strEndTime ="2020-1-2";

    private WheelView year;
    private WheelView month;
    private WheelView day;

    boolean isStartTime = true;

    RentDao rentDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rent_activity);
        initView();
        setClickListener();
        rentDao = new RentDao(this);
    }

    private void initView(){
        mBtnStartTime = findViewById(R.id.rent_start_time);
        mBtnEndTime = findViewById(R.id.rent_end_time);
        mBtnSubmit = findViewById(R.id.rent_submit);
        mEtElectric = findViewById(R.id.rent_electric);
        mEtWater = findViewById(R.id.rent_water);
        mEtMonthRent = findViewById(R.id.rent_month);
        mTvDays = findViewById(R.id.rent_days);
        mTvResult = findViewById(R.id.rent_result);
    }

    private void setClickListener(){
        mBtnStartTime.setOnClickListener(new buttonListener());
        mBtnEndTime.setOnClickListener(new buttonListener());
        mBtnSubmit.setOnClickListener(new buttonListener());
    }

    private void getEditContent(){
        String water = mEtWater.getText().toString();
        String month = mEtMonthRent.getText().toString();
        String electric = mEtElectric.getText().toString();
        rentWater = water.isEmpty()?0:Integer.parseInt(water);
        rentMouth = month.isEmpty()?0:Integer.parseInt(month);
        rentElectric = electric.isEmpty()?0:Integer.parseInt(electric);
    }

    private class buttonListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.rent_start_time:
                    showDateDialog(isStartTime);
                    break;
                case R.id.rent_end_time:
                    showDateDialog(!isStartTime);
                    break;
                case R.id.rent_submit:
                    String zero = "0.00";
                    Float result = calculationRent();
                    if(result==0){
                        mTvResult.setText(zero);
                    }else {
                        DecimalFormat df = new DecimalFormat(zero);//设置保留位数
                        mTvResult.setText(df.format(result));
                    }
                    //添加记录 到数据库
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String nowTime = sdf.format(new Date());
                    String rentDays = mTvDays.getText().toString();
                    Rent rent = new Rent();
                    rent.setStartTime(strStartTime);
                    rent.setEndTime(strEndTime);
                    rent.setMonthRent(String.valueOf(rentMouth));
                    rent.setRentWater(String.valueOf(rentWater));
                    rent.setRentElectric(String.valueOf(rentElectric));
                    rent.setNowTime(nowTime);
                    rent.setRentDays(rentDays);
                    rent.setRentResult(result.toString());
                    if(!rentDays.equals("时间选择不正确！"))rentDao.insert(rent);
                    break;
                default:
                    break;
            }
        }
    }

    private float calculationRent(){
        getEditContent();
        if(mBtnStartTime.getText().toString().equals("开始时间")||mBtnEndTime.getText().toString().equals("截至时间")){
            Toast.makeText(RentActivity.this,"请选择时间！！！",Toast.LENGTH_LONG).show();
            return 0;
        }else if(rentMouth==0){
            Toast.makeText(RentActivity.this,"请输入月租！！！",Toast.LENGTH_LONG).show();
        } else{
            String verticalResult = calculateDateDifferent();
            if(verticalResult.isEmpty()) return 0;
            int indexOfMonth = verticalResult.lastIndexOf("月");
            int indexOfDay = verticalResult.lastIndexOf("天");
            int month = Integer.parseInt(verticalResult.substring(0,indexOfMonth));
            int day = Integer.parseInt(verticalResult.substring(indexOfMonth+1,indexOfDay));

            DecimalFormat df=new DecimalFormat("0.00");//设置保留位数
            float rentDay = Float.parseFloat(df.format((float)rentMouth/30));
            return month*rentMouth+day*rentDay+rentWater+rentElectric;
        }
        return 0;
    }

    /**
     * 显示日期
     */
    private void showDateDialog(final boolean isStartTime) {
        final AlertDialog dialog = new AlertDialog.Builder(RentActivity.this)
                .create();
        dialog.show();
        Window window = dialog.getWindow();
        // 设置布局
        window.setContentView(R.layout.date_picker_layout);
        // 设置位置
        window.setGravity(Gravity.CENTER);
        // 设置宽高
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        // 设置弹出的动画效果
        window.setWindowAnimations(R.style.AnimBottom);


        Calendar c = Calendar.getInstance();
        int curYear = c.get(Calendar.YEAR);
        int curMonth = c.get(Calendar.MONTH) + 1;//通过Calendar算出的月数要+1
        int curDate = c.get(Calendar.DATE);
        year = (WheelView) window.findViewById(R.id.year);
        initYear();
        month = (WheelView) window.findViewById(R.id.month);
        initMonth();
        day = (WheelView) window.findViewById(R.id.day);
        initDay(curYear,curMonth);


        year.setCurrentItem(curYear - 1950);
        month.setCurrentItem(curMonth - 1);
        day.setCurrentItem(curDate - 1);
        year.setVisibleItems(7);
        month.setVisibleItems(7);
        day.setVisibleItems(7);

        // 设置监听
        Button ok = (Button) window.findViewById(R.id.set);
        Button cancel = (Button) window.findViewById(R.id.cancel);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = String.format(Locale.CHINA,"%4d年%2d月%2d日",year.getCurrentItem()+1950,month.getCurrentItem()+1,day.getCurrentItem()+1);
                String str1 = String.format(Locale.CHINA,"%4d-%2d-%2d",year.getCurrentItem()+1950,month.getCurrentItem()+1,day.getCurrentItem()+1);
                Log.d("RentTime",str1);
                if(isStartTime){
                    mBtnStartTime.setText(str);
                    strStartTime = str1;
                }else{
                    mBtnEndTime.setText(str);
                    strEndTime = str1;
                }
                dialog.cancel();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        LinearLayout cancelLayout = (LinearLayout) window.findViewById(R.id.view_none);
        cancelLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                dialog.cancel();
                return false;
            }
        });

    }

    /**
     * 初始化年
     */
    private void initYear() {
        NumericWheelAdapter numericWheelAdapter = new NumericWheelAdapter(this,1950, 2050);
        numericWheelAdapter.setLabel(" 年");
        //		numericWheelAdapter.setTextSize(15);  设置字体大小
        year.setViewAdapter(numericWheelAdapter);
        year.setCyclic(true);
    }

    /**
     * 初始化月
     */
    private void initMonth() {
        NumericWheelAdapter numericWheelAdapter = new NumericWheelAdapter(this,1, 12, "%02d");
        numericWheelAdapter.setLabel(" 月");
        //		numericWheelAdapter.setTextSize(15);  设置字体大小
        month.setViewAdapter(numericWheelAdapter);
        month.setCyclic(true);
    }

    /**
     * 初始化天
     */
    private void initDay(int arg1, int arg2) {
        NumericWheelAdapter numericWheelAdapter=new NumericWheelAdapter(this,1, getDay(arg1, arg2), "%02d");
        numericWheelAdapter.setLabel(" 日");
        //		numericWheelAdapter.setTextSize(15);  设置字体大小
        day.setViewAdapter(numericWheelAdapter);
        day.setCyclic(true);
    }

    private int getDay(int year, int month) {
        boolean isLeap  = year%4 == 0 && year%100!=0 || year % 400 == 0;    //普通闰年||世纪闰年
        int monthOfDays[] = {31,28,31,30,31,30,31,31,30,31,30,31};
        return isLeap&&month==2?29:monthOfDays[month-1];
    }

    /**
     * 计算入住天数，按照对应月份计算，非每月30天
     * @return ""代表异常 "5月10天"
     */
    private String calculateDateDifferent() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd");
        try {
            //Date 的getDay方法返回的是一周中的第几天，getYear方法返回的是从1900年到现在过去几年了
            Date startDate = sdf.parse(strStartTime);
            Date endDate = sdf.parse(strEndTime);
            if(startDate.getTime()>endDate.getTime()){
                mTvDays.setText("时间选择不正确！");
                return "";
            }
            String resultInterval = calculateDifferentByCalender(startDate,endDate);
            mTvDays.setText(resultInterval);
            return resultInterval;
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e("RentActivity",e.getMessage());
            mTvDays.setText(e.getMessage());
            return "";
        }
    }

    private String calculateDifferentByCalender(Date startTime,Date endTime){
        //使用Calendar来计算时间间隔,通过get方法获取月份时要加一
        Calendar startCalender = Calendar.getInstance();
        startCalender.setTime(startTime);
        Calendar endCalender = Calendar.getInstance();
        endCalender.setTime(endTime);

        //如果开始时间大于截止时间，退出返回空值
        if(startTime.getTime()>endTime.getTime()) return "";
        if(startTime.getTime()==endTime.getTime()) return "0月0天";

        int intervalYear=0,intervalMonth=0,intervalDay=0;
        int startYear,startMonth,startDay;
        int endYear,endMonth,endDay;

        startYear = startCalender.get(Calendar.YEAR);
        startMonth = startCalender.get(Calendar.MONTH)+1;  //获取月份不正常，要加1，因为Calender月份初始值为0
        startDay = startCalender.get(Calendar.DAY_OF_MONTH);

        endYear = endCalender.get(Calendar.YEAR);
        endMonth = endCalender.get(Calendar.MONTH)+1;  //获取月份不正常，要加1，因为Calender月份初始值为0
        endDay = endCalender.get(Calendar.DAY_OF_MONTH);

        intervalYear = endYear - startYear;       //endYear>=startYear 恒成立

        if(startMonth<=endMonth && startDay<=endDay){
            intervalMonth = endMonth - startMonth;
            intervalDay = endDay - startDay;
        } else if(startMonth<=endMonth && startDay>endDay){
            intervalMonth = endMonth-startMonth-1;             //开始日期大于截止日期，向前借一位
            int lastMonthDays = endMonth==1?31:getDay(endYear,endMonth-1);     //如果截止月份为一月，则前一个月就是31天，否则正常计算
            intervalDay = lastMonthDays - startDay + endDay;   //5月10 到 7月2号  6月有30天 间隔天=30-10+2
        } else if(startMonth>endMonth && startDay<=endDay){
            intervalYear -= 1;                                 //开始月份大于截止月份，代表前一年入住，年份减一，月份加12
            intervalMonth = endMonth + 12 - startMonth;
            intervalDay = endDay - startDay;
        } else if(startMonth>endMonth && startDay>endDay){     //最后一种情况，为方便计算，将条件写出
            intervalYear -= 1;                                  //开始月份大于截止月份，向前借一位
            intervalMonth = endMonth + 12 - startMonth - 1;             //开始日期大于截止日期，向前借一位
            int lastMonthDays = endMonth==1?31:getDay(endYear,endMonth-1);     //如果截止月份为一月，则前一个月就是31天，否则正常计算
            intervalDay = lastMonthDays - startDay + endDay;   //5月10 到 7月2号  6月有30天 间隔天=30-10+2
        }

        int sumMonth = intervalYear*12 + intervalMonth;

        return ""+sumMonth+"月"+intervalDay+"天";
    }

}
