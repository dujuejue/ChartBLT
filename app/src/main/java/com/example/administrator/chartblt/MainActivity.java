package com.example.administrator.chartblt;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int webTrue=2;
    private static final int webFalse=3;
    private Button button;
    private EditText start, end;
    private List<BarChartBean> mBarChartBeanList = new ArrayList<BarChartBean>();
    private Handler webHandler;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    if (webHandler != null) {
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Log.i("btn","1");
                                String startStr = start.getText().toString();
                                String endStr = end.getText().toString();
                                Message messageWeb = webHandler.obtainMessage();
                                Bundle bundleWeb = new Bundle();
                                bundleWeb.putString("startTime", startStr);
                                bundleWeb.putString("endTime", endStr);
                                messageWeb.obj = bundleWeb;
                                webHandler.sendMessage(messageWeb);
                            }
                        });
                    }
                    break;
                case webTrue:
                    mBarChartBeanList= (List<BarChartBean>) msg.obj;
                    if (mBarChartBeanList.size()==0){
                        Toast.makeText(getApplicationContext(),"此段时间内无数据",Toast.LENGTH_LONG).show();
                    }else {
                        Intent intent = new Intent(MainActivity.this, PlatoActivity.class);
                        intent.putExtra("list", (Serializable) mBarChartBeanList);
                        startActivity(intent);
                    }
                    break;
                case webFalse:
                    start.setText("");
                    end.setText("");
                    start.requestFocus();
                    Toast.makeText(getApplicationContext(),"输入格式错误",Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };
    ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        start = (EditText) findViewById(R.id.startTime);
        end = (EditText) findViewById(R.id.endTime);
        button = (Button) findViewById(R.id.plato);
        WebThread webThread = new WebThread();
        webThread.start();

    }

    private class WebThread extends Thread {
        @Override
        public void run() {
            Looper.prepare();

            webHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    Bundle bundle = (Bundle) msg.obj;
                    String startTime = bundle.getString("startTime");
                    String endTime = bundle.getString("endTime");
                    List<String> Name = new ArrayList<String>();
                    List<String> Count = new ArrayList<String>();
                    Boolean dataGet = WebUtils.getPLATOData(Name, Count, startTime, endTime);
                    if (dataGet) {
                        List<BarChartBean> barChartBeanList = new ArrayList<BarChartBean>();
                        for (int i = 0; i < Name.size(); i++) {
                            int num = Integer.parseInt(Count.get(i));
                            barChartBeanList.add(new BarChartBean(Name.get(i), num, Color.BLUE));
                        }
                        Message message = mHandler.obtainMessage();
                        message.what = webTrue;
                        message.obj = barChartBeanList;
                        mHandler.sendMessage(message);
                    } else {
                        Message message = mHandler.obtainMessage();
                        message.what = webFalse;
                        mHandler.sendMessage(message);
                    }
                }
            };
            Message message = mHandler.obtainMessage();
            message.what = 1;
            mHandler.sendMessage(message);
            Looper.loop();
        }
    }
}
