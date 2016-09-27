package com.example.administrator.chartblt;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private int colors[] = new int[]{Color.parseColor("#6FC5F4"),
            Color.parseColor("#78DA9F"), Color.parseColor("#FCAE84"),Color.BLUE,Color.RED};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
