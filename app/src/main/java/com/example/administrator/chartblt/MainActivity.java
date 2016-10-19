package com.example.administrator.chartblt;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Button button;
    //    private Button btnDate;
    private EditText start, end;
//    private TextView date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        start = (EditText) findViewById(R.id.startTime);
        end = (EditText) findViewById(R.id.endTime);
        button = (Button) findViewById(R.id.plato);
        button.setOnClickListener(new View.OnClickListener() {
            Calendar c = Calendar.getInstance();

            @Override
            public void onClick(View v) {
                String startStr = start.getText().toString();
                String endStr = end.getText().toString();
                Intent intent = new Intent(MainActivity.this, PlatoActivity.class);
                intent.putExtra("startTime", startStr);
                intent.putExtra("endTime", endStr);
                startActivity(intent);
            }
        });

//        date= (TextView) findViewById(R.id.textDate);
//        btnDate= (Button) findViewById(R.id.datePicker);
//        btnDate.setOnClickListener(new View.OnClickListener() {
//            Calendar c = Calendar.getInstance();
//            @Override
//            public void onClick(View v) {
//
//                new DoubleDatePickerDialog(MainActivity.this, 0, new DoubleDatePickerDialog.OnDateSetListener() {
//                    @Override
//                    public void onDateSet(DatePicker startDatePicker, int startYear,
//                                          int startMonthOfYear, int startDayOfMonth,
//                                          DatePicker endDatePicker, int endYear, int endMonthOfYear, int endDayOfMonth) {
//                        String textString = String.format("开始时间：%d-%d-%d\n结束时间：%d-%d-%d\n", startYear,
//                                startMonthOfYear + 1, startDayOfMonth, endYear, endMonthOfYear + 1, endDayOfMonth);
//                        date.setText(textString);
//                    }
//                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE), true).show();
//            }
//        });
    }


}
