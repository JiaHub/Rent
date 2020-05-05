package com.zhh.rent.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhh.rent.R;
import com.zhh.rent.record.ItemBean;
import com.zhh.rent.record.MyAdapter;
import com.zhh.rent.record.RecordContentActivity;
import com.zhh.rent.sqlite.Rent;
import com.zhh.rent.sqlite.RentDao;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

public class RecordActivity extends AppCompatActivity {

    private List<ItemBean> list;
    private MyAdapter myAdapter;
    private RecyclerView recyclerView;
    RefreshLayout refreshLayout;
    private RentDao rentDao;
    private List<ItemBean> itemList = new ArrayList<>();    //存对象避免刷新后队列中没有数据
    private Queue<ItemBean> itemQueue = new ArrayDeque<>(); //取对象
    private List<Rent> rentList = new ArrayList<>();
    private final int FIRST_LOAD_COUNT = 10;   //首次加载数目
    private final int TIMES_LOAD_COUNT = 5;   //每次加载条数

    private boolean isLongClick = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record_activity);
        rentDao = new RentDao(this);
        refreshLayout = (RefreshLayout) findViewById(R.id.refreshLayout);
        initDate();
        setPullRefresher();
        Button deleteAll = findViewById(R.id.delete_all);
        deleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new AlertDialog.Builder(RecordActivity.this)      //实例化对象
                        .setTitle("删除记录")               //显示标题
                        .setMessage("点击确定将删除全部记录！！！")     //显示内容
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                rentDao.delete();
                            }
                        })
                        .create();                          //创建对话框
                dialog.show();                      //显示对话框
            }
        });
    }


    private void initDate() {
        list = new ArrayList<ItemBean>();
        rentList = rentDao.query();
        Stack<Rent> rentStack = new Stack<>();
        rentStack.addAll(rentList);
        int index = 0;
        while (!rentStack.empty()){
            Rent rent = rentStack.pop();
            ItemBean itemBean = new ItemBean(R.drawable.box,"计算时间："+rent.getNowTime(),"计算结果："+rent.getRentResult(),rent);
            if(index<=FIRST_LOAD_COUNT){
                list.add(itemBean);
            }else {
                itemList.add(itemBean);
                itemQueue.add(itemBean);
            }
            index+=1;
        }
//        for (int i = 0; i < 35; i++) {
//            ItemBean itemBean = new ItemBean(R.mipmap.ic_launcher,
//                    "initTitle" + i,
//                    System.currentTimeMillis() + "");
//            if(i<=FIRST_LOAD_COUNT){
//                list.add(itemBean);
//            }else {
//                itemList.add(itemBean);
//                itemQueue.add(itemBean);
//            }
//        }

        myAdapter = new MyAdapter(list, new MyAdapter.OnItemClickListener() {
            @Override
            public void OnClick(int position, View v) {
                if(isLongClick){
                    isLongClick = false;
                    return;   //如果是长按就不跳转
                }
                ItemBean item  = list.get(position);
                Rent rent = item.getRent();
                Gson gson = new Gson();
                String rentJson = gson.toJson(rent);
                Intent intent = new Intent(RecordActivity.this, RecordContentActivity.class);
                intent.putExtra("rent",rentJson);
                startActivity(intent);
            }
        }, new MyAdapter.OnItemLongClickListener() {
            @Override
            public boolean onLongClick(final int position, View v) {
//                Toast.makeText(RecordActivity.this, list.get(position).toString(), Toast.LENGTH_SHORT).show();
                isLongClick = true;
                final Dialog dialog = new AlertDialog.Builder(RecordActivity.this)      //实例化对象
                        .setTitle("删除记录")               //显示标题
                        .setMessage("点击确定将删除记录！！！")     //显示内容
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            rentDao.deleteRent(list.get(position).getRent().getId());
                        }
                        })
                        .create();                          //创建对话框
                dialog.show();                      //显示对话框

                return false;
            }
        });
        recyclerView = findViewById(R.id.recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(RecordActivity.this);//纵向线性布局

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(myAdapter);
    }

    private void getNewDate(){
        list.clear();
        rentList.clear();
        itemQueue.clear();
        rentList = rentDao.query();
        Stack<Rent> rentStack = new Stack<>();
        rentStack.addAll(rentList);
        int index = 0;
        while (!rentStack.empty()){
            Rent rent = rentStack.pop();
            ItemBean itemBean = new ItemBean(R.drawable.box,"计算时间："+rent.getNowTime(),"计算结果："+rent.getRentResult(),rent);
            if(index<=FIRST_LOAD_COUNT){
                list.add(itemBean);
            }else {
                itemList.add(itemBean);
                itemQueue.add(itemBean);
            }
            index+=1;
        }
    }

    private void setPullRefresher() {
        //设置 Header 为 MaterialHeader
        refreshLayout.setRefreshHeader(new ClassicsHeader(this));
        //设置 Footer 为 经典样式
        refreshLayout.setRefreshFooter(new ClassicsFooter(this));

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                //在这里执行上拉刷新时的具体操作(网络请求、更新UI等)
                getNewDate();
                //模拟网络请求到的数据
                ArrayList<ItemBean> newList = new ArrayList<ItemBean>();
                newList.addAll(list);
                myAdapter.refresh(newList);
                itemQueue.clear();   //重置
                itemQueue.addAll(itemList);  //重置，本地数据加载方式，如果联网加载就不需要这样，可以选择执行initDate
                refreshlayout.finishRefresh(2000/*,false*/);
                //不传时间则立即停止刷新    传入false表示刷新失败
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {

                //模拟网络请求到的数据
                ArrayList<ItemBean> newList = new ArrayList<ItemBean>();
                while(!itemQueue.isEmpty()&&newList.size()<TIMES_LOAD_COUNT){
                    ItemBean itemBean = itemQueue.poll();
                    if(itemBean!=null){
                        newList.add(itemBean);
                    }
                }
                myAdapter.add(newList);
                //在这里执行下拉加载时的具体操作(网络请求、更新UI等)
                refreshlayout.finishLoadmore(2000/*,false*/);//不传时间则立即停止刷新    传入false表示加载失败
            }
        });
    }
    
}
