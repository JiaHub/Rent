package com.zhh.rent.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by G40-70 on 2018/12/17.
 *
 */

public class RentDao {

    private DatabaseHelper databaseHelper;

    public RentDao(Context context) {
        databaseHelper = new DatabaseHelper(context);       //参数是上下文
    }

    //插入数据
    public void insert(Rent rent) {

        //得到一个可写的数据库
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        //ContentValues 键值的用法   key要跟列名一致
        ContentValues cv = new ContentValues();
        cv.put("nowTime", rent.getNowTime());
        cv.put("startTime",rent.getStartTime());
        cv.put("endTime", rent.getEndTime());
        cv.put("monthRent",rent.getMonthRent());
        cv.put("rentWater", rent.getRentWater());
        cv.put("rentElectric",rent.getRentElectric());
        cv.put("rentDays", rent.getRentDays());
        cv.put("rentResult",rent.getRentResult());
        String nowTime = rent.getNowTime();
        Log.i("RentDao","这里调用了插入方法"+cv.toString());
        db.insert("rent", nowTime,cv);
        db.close();

    }

    //删除一条数据
    public void deleteRent(Integer id) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        db.delete("rent","_id=?",new String[]{String.valueOf(id)});//注意这里的id要使用建表时的
//        String sql = "delete from rent where id="+id;
//        db.execSQL(sql);
        db.close();

        Log.i("RentDao","这里调用了删除方法"+id);

    }

    //删除所有数据
    public void delete() {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        db.delete("rent", null, null);
        db.close();

        Log.i("RentDao","这里调用了删除所有记录方法");

    }

    //修改数据
    public void update(Rent rent) {

        //得到一个可写的数据库
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        //ContentValues 键值的用法   key要跟列名一致
        ContentValues cv = new ContentValues();
        cv.put("nowTime", rent.getNowTime());
        cv.put("startTime",rent.getStartTime());
        cv.put("endTime", rent.getEndTime());
        cv.put("monthRent",rent.getMonthRent());
        cv.put("rentWater", rent.getRentWater());
        cv.put("rentElectric",rent.getRentElectric());
        cv.put("rentDays", rent.getRentDays());
        cv.put("rentResult",rent.getRentResult());
        String id = String.valueOf(rent.getId());
        db.update("rent", cv, "id= ? ", new String[]{id});
        db.close();

        Log.i("RentDao","这里调用了修改方法"+id);

    }

    //查询记录
    public Rent queryRent(Integer id){

        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String newId = String.valueOf(id);;
        Cursor cs = db.query("rent", null, "id= ? ", new String[]{newId}, null, null, null, null);
        Rent rent = null;

        if (cs != null && cs.getCount() > 0) {

            while (cs.moveToNext()) {
                rent = new Rent();
                // cs.getColumnIndex("_id")   id 这一列结果中的下标
                rent.setId(cs.getInt(cs.getColumnIndex("_id")));
                rent.setStartTime(cs.getString(cs.getColumnIndex("startTime")));
                rent.setEndTime(cs.getString(cs.getColumnIndex("endTime")));
                rent.setMonthRent(cs.getString(cs.getColumnIndex("monthRent")));
                rent.setRentWater(cs.getString(cs.getColumnIndex("rentWater")));
                rent.setRentElectric(cs.getString(cs.getColumnIndex("rentElectric")));
                rent.setRentDays(cs.getString(cs.getColumnIndex("rentDays")));
                rent.setRentResult(cs.getString(cs.getColumnIndex("rentResult")));
                rent.setNowTime(cs.getString(cs.getColumnIndex("nowTime")));
            }
            cs.close();
            Log.i("RentDao", "这里调用了查询方法" + newId);
        }
        db.close();
        Log.i("RentDao", "关闭数据库！");

        return rent;

    }

    //查询所有数据
    public List<Rent> query() {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cs = db.query("rent", null, null, null, null, null, null);
        Rent rent = null;
        List<Rent> list = new ArrayList<>();
        while (cs.moveToNext()) {
            rent = new Rent();
            // cs.getColumnIndex("_id")   id 这一列结果中的下标
            rent.setId(cs.getInt(cs.getColumnIndex("_id")));
            rent.setStartTime(cs.getString(cs.getColumnIndex("startTime")));
            rent.setEndTime(cs.getString(cs.getColumnIndex("endTime")));
            rent.setMonthRent(cs.getString(cs.getColumnIndex("monthRent")));
            rent.setRentWater(cs.getString(cs.getColumnIndex("rentWater")));
            rent.setRentElectric(cs.getString(cs.getColumnIndex("rentElectric")));
            rent.setRentDays(cs.getString(cs.getColumnIndex("rentDays")));
            rent.setRentResult(cs.getString(cs.getColumnIndex("rentResult")));
            rent.setNowTime(cs.getString(cs.getColumnIndex("nowTime")));
            list.add(rent);
        }
        //关闭结果集
        cs.close();
        //关闭数据库
        db.close();

        Log.i("RentDao","这里调用了查询所有数据方法");

        return list;
    }


}
