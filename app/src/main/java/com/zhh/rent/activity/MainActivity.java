package com.zhh.rent.activity;

import android.Manifest;
import android.app.LocalActivityManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.zhh.rent.R;

/**
 * 现有需求
 *         1、选择时间，计算时间间隔，以 *月*天 记
 *         2、提供收费记录，写入数据库
 *         3、输入月租，选择时间，计算金额，以30天一月记
 *         4、提供简单计算器
 *         5、提供输入框，内容为租金、水费、电费，计算总金额
 * 需要权限
 *         由于要写入数据库，需要读写权限
 */
public class MainActivity extends AppCompatActivity {

    final int REQUEST_WRITE_EXTERNAL_STORAGE = 0x1;

    private TabHost myTabHost;
    //用于添加每一个选项卡的id
    private String[] tags = {"1","2","3"};
    //所添加选项卡的文本信息
    private String[] titles = {"房租", "计算器", "记录"};
    //所添加选项卡的图片信息
    private int[] images = {R.drawable.rent_128, R.drawable.calculate_128,R.drawable.record_128};

    //用于跳转至不同的Activity
    private Intent[] intents = new Intent[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermission();
        myTabHost = (TabHost) findViewById(R.id.th_tabHost);
        //初始化activity管理者
        LocalActivityManager manager = new LocalActivityManager(this, false);
        //通过管理者保存当前页面状态
        manager.dispatchCreate(savedInstanceState);
        //将管理者类对象添加至TabHost
        myTabHost.setup(manager);                                      //初始化
        init_intent();
        changeTabColor();
    }

    private void checkPermission() {
        //检查权限（NEED_PERMISSION）是否被授权 PackageManager.PERMISSION_GRANTED表示同意授权
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //用户已经拒绝过一次，再次弹出权限申请对话框需要给用户一个解释
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission
                    .WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(this, "请开通相关权限，否则无法正常使用本应用！", Toast.LENGTH_SHORT).show();
            }
            //申请权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_EXTERNAL_STORAGE);
        }
    }

    //每个页面放置的Activity
    public void init_intent() {

        intents[0] = new Intent(this, RentActivity.class);
        intents[1] = new Intent(this, CalculateActivity.class);
        intents[2] = new Intent(this, RecordActivity.class);

        for (int i = 0; i < tags.length; i++) {
            //加载底部导航栏布局
            LayoutInflater inflater = this.getLayoutInflater();
            View view = inflater.inflate(R.layout.tabhost_item, null);
            TextView textView = (TextView) view.findViewById(R.id.title);
            ImageView imageView = (ImageView) view.findViewById(R.id.icon);
            textView.setText(titles[i]);
            imageView.setImageResource(images[i]);
            //创建选项卡
            TabHost.TabSpec spec = myTabHost.newTabSpec(tags[i]);
            spec.setIndicator(view);
            //设置每个页面的内容
            spec.setContent(intents[i]);
            //将创建的选项卡添加至tabHost上
            myTabHost.addTab(spec);
        }

    }

    public void changeTabColor(){
        /*设置标签切换监听器*/
        myTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                for (int i = 0; i < tags.length; i++) {//颜色全部重置
                    ((TextView) myTabHost.getTabWidget().getChildTabViewAt(i).findViewById(R.id.title))
                            .setTextColor(getResources().getColor(R.color.black));
                }
                if (myTabHost.getCurrentTabTag() == tabId) {
                    ((TextView) myTabHost.getCurrentTabView().findViewById(R.id.title))
                            .setTextColor(getResources().getColor(R.color.ORANGERED));
                }//选中的那个Tab文字颜色修改
            }
        });
    }

}
