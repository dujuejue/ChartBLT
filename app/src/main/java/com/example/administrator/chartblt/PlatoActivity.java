package com.example.administrator.chartblt;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * 　　　　　　　　┏┓　　　┏┓
 * 　　　　　　　┏┛┻━━━┛┻┓
 * 　　　　　　　┃　　　　　　　┃
 * 　　　　　　　┃　　　━　　　┃
 * 　　　　　　 ████━████     ┃
 * 　　　　　　　┃　　　　　　　┃
 * 　　　　　　　┃　　　┻　　　┃
 * 　　　　　　　┃　　　　　　　┃
 * 　　　　　　　┗━┓　　　┏━┛
 * 　　　　　　　　　┃　　　┃
 * 　　　　　　　　　┃　　　┃
 * 　　　　　　　　　┃　　　┃
 * 　　　　　　　　　┃　　　┃
 * 　　　　　　　　　┃　　　┃
 * 　　　　　　　　　┃　　　┃
 * 　　　　　　　　　┃　 　 ┗━━━┓
 * 　　　　　　　　　┃ 神兽保佑　　 ┣┓
 * 　　　　　　　　　┃ 代码无BUG   ┏┛
 * 　　　　　　　　　┗┓┓┏━┳┓┏┛
 * 　　　　　　　　　　┃┫┫　┃┫┫
 * 　　　　　　　　　　┗┻┛　┗┻┛
 * Created by dutingjue on 2016/10/10.
 */
public class PlatoActivity extends Activity {
    private CombineChart combineChart;
    private List<BarChartBean> mBarChartBeanList = new ArrayList<BarChartBean>();
    private Handler mHandler,webHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plato);
        combineChart = (CombineChart) findViewById(R.id.test);
        String startTime=this.getIntent().getStringExtra("startTime");
        String endTime=this.getIntent().getStringExtra("endTime");
        final Bundle bundleWeb=new Bundle();
        bundleWeb.putString("startTime",startTime);
        bundleWeb.putString("endTime",endTime);
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case 1:
                        if (webHandler!=null){
                            Message message=webHandler.obtainMessage();
                            message.obj=bundleWeb;
                            webHandler.sendMessage(message);
                        }
                        break;
                    case 2:
                        mBarChartBeanList = (List<BarChartBean>) msg.obj;
                        combineChart.setItemData(mBarChartBeanList);
                        break;
                    case 3:
                        Toast.makeText(getApplicationContext(),"输入格式不正确",Toast.LENGTH_SHORT).show();
                        finish();
                        break;
                }
            }
        };
        new WebThread().start();
    }

    private class WebThread extends Thread {
        @Override
        public void run() {
            Looper.prepare();

            webHandler=new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    Bundle bundle= (Bundle) msg.obj;
                    String startTime=bundle.getString("startTime");
                    String endTime=bundle.getString("endTime");
                    List<String> Name = new ArrayList<String>();
                    List<String> Count = new ArrayList<String>();
                    Boolean dataGet = WebUtils.getPLATOData(Name, Count,startTime,endTime);
                    if (dataGet) {
                        List<BarChartBean> barChartBeanList = new ArrayList<BarChartBean>();
                        for (int i = 0; i < Name.size(); i++) {
                            int num = Integer.parseInt(Count.get(i));
                            barChartBeanList.add(new BarChartBean(Name.get(i), num, Color.BLUE));
                        }
                        Message message = mHandler.obtainMessage();
                        message.what=2;
                        message.obj = barChartBeanList;
                        mHandler.sendMessage(message);
                    }else {
                        Message message = mHandler.obtainMessage();
                        message.what=3;
                        mHandler.sendMessage(message);
                    }
                }
            };
            Message message=mHandler.obtainMessage();
            message.what=1;
            mHandler.sendMessage(message);
            Looper.loop();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
