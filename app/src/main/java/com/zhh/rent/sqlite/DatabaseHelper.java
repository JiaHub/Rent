package com.zhh.rent.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by G40-70 on 2018/12/17.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String PLAY_DB_MANE = "MyRent" ;  //数据库名
    private static final int PLAY_VERSION= 1 ;          //版本号

    public DatabaseHelper (Context context){

        //第三个参数CursorFactory指定在执行查询时获得一个游标实例的工厂类,设置为null,代表使用系统默认的工厂类
        super(context,PLAY_DB_MANE,null,PLAY_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE rent (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                   " startTime varchar(20), endTime varchar(20), monthRent varchar(20), rentWater varchar(20)," +
                   " rentElectric varchar(20), rentDays varchar(20), rentResult varchar(20),nowTime varchar(20));");

        Log.i("RentDB","这里创建数据库");

    }

    //数据库更新升级
    // 通过版本号Version 删除以前的表  然后重新调用OnCreate
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if (oldVersion==1 && newVersion==2) {//升级判断,如果再升级就要再加两个判断,从1到3,从2到3
            db.execSQL(" drop table if exists rent");
        }

        Log.i("RentDB","这里更新数据库版本");

    }
}
