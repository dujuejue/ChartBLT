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
    private static final int webTrue = 2;
    private static final int dataFalse = 3;
    private static final int dataNull = 4;
    private static final int netFalse = 5;
    private static final int PlatoData = 0;
    private static final int BarData = 1;
    private Button button, buttonInOut;
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
                                Log.i("btn", "1");
                                String startStr = start.getText().toString();
                                String endStr = end.getText().toString();
                                Message messageWeb = webHandler.obtainMessage();
                                Bundle bundleWeb = new Bundle();
                                bundleWeb.putString("startTime", startStr);
                                bundleWeb.putString("endTime", endStr);
                                messageWeb.obj = bundleWeb;
                                messageWeb.what = PlatoData;
                                webHandler.sendMessage(messageWeb);
                            }
                        });
                        buttonInOut.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String startStr = start.getText().toString();
                                String endStr = end.getText().toString();
                                Message messageWeb = webHandler.obtainMessage();
                                Bundle bundleWeb = new Bundle();
                                bundleWeb.putString("startTime", startStr);
                                bundleWeb.putString("endTime", endStr);
                                messageWeb.obj = bundleWeb;
                                messageWeb.what = BarData;
                                webHandler.sendMessage(messageWeb);
                            }
                        });
                    }
                    break;
                case webTrue:
                    Intent intent= (Intent) msg.obj;
                    startActivity(intent);
                    break;
                case dataFalse:
                    start.setText("");
                    end.setText("");
                    start.requestFocus();
                    Toast.makeText(getApplicationContext(), "输入格式错误", Toast.LENGTH_LONG).show();
                    break;
                case dataNull:
                    start.setText("");
                    end.setText("");
                    start.requestFocus();
                    Toast.makeText(getApplicationContext(), "该时间段内无数据", Toast.LENGTH_LONG).show();
                    break;
                case netFalse:
                    Toast.makeText(getApplicationContext(), "网络出错", Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        start = (EditText) findViewById(R.id.startTime);
        end = (EditText) findViewById(R.id.endTime);
        button = (Button) findViewById(R.id.plato);
        buttonInOut = (Button) findViewById(R.id.inoutput);
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
                    switch (msg.what) {
                        case PlatoData:
                            Bundle bundle = (Bundle) msg.obj;
                            getPalto(bundle);
                            break;
                        case BarData:
                            Bundle bundleBar = (Bundle) msg.obj;
                            getBar(bundleBar);
                            break;
                    }


                }
            };
            Message message = mHandler.obtainMessage();
            message.what = 1;
            mHandler.sendMessage(message);
            Looper.loop();
        }
    }

    private void getPalto(Bundle bundle) {
        String startTime = bundle.getString("startTime");
        String endTime = bundle.getString("endTime");
        List<String> Name = new ArrayList<String>();
        List<String> Count = new ArrayList<String>();
        Message message = mHandler.obtainMessage();
        int dataGet = WebUtils.getPLATOData(Name, Count, startTime, endTime);
        if (dataGet == WebUtils.dataTrue) {
            List<BarChartBean> barChartBeanList = new ArrayList<BarChartBean>();
            for (int i = 0; i < Name.size(); i++) {
                int num = Integer.parseInt(Count.get(i));
                barChartBeanList.add(new BarChartBean(Name.get(i), num, Color.BLUE));
            }
            Intent intent=new Intent(MainActivity.this,PlatoActivity.class);
            intent.putExtra("list", (Serializable) barChartBeanList);
            message.obj = intent;
            message.what = webTrue;
        } else if (dataGet == WebUtils.dataFalse) {
            message.what = dataFalse;
        } else if (dataGet == WebUtils.dataNull) {
            message.what = dataNull;
        } else if (dataGet == WebUtils.netFalse) {
            message.what = netFalse;
        }
        mHandler.sendMessage(message);
    }

    private void getBar(Bundle bundle) {
        String startTime = bundle.getString("startTime");
        String endTime = bundle.getString("endTime");
        List<Integer> input = new ArrayList<Integer>();
        List<Integer> output = new ArrayList<Integer>();
        Message message = mHandler.obtainMessage();
        int dataGet=WebUtils.getInOutData(input,output,startTime,endTime);
        if (dataGet == WebUtils.dataTrue) {
            List<Inputoutput>inputoutputs=new ArrayList<Inputoutput>();
            for (int i=0;i<input.size();i++){
                inputoutputs.add(new Inputoutput(input.get(i),output.get(i)));
            }
            Intent intent=new Intent(MainActivity.this,InoutActivity.class);
            intent.putExtra("list", (Serializable) inputoutputs);
            message.obj=intent;
            message.what=webTrue;
        } else if (dataGet == WebUtils.dataFalse) {
            message.what = dataFalse;
        } else if (dataGet == WebUtils.dataNull) {
            message.what = dataNull;
        } else if (dataGet == WebUtils.netFalse) {
            message.what = netFalse;
        }
        mHandler.sendMessage(message);
    }
}
