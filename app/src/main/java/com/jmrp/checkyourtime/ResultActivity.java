package com.jmrp.checkyourtime;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        TextView txtTime = (TextView) findViewById(R.id.txt_result_time);
        txtTime.setText(getIntent().getStringExtra("TIME") + "miliseconds");
        TextView txtPercent = (TextView) findViewById(R.id.txt_result_percent);
        txtPercent.setText(getIntent().getStringExtra("PERCENT") + "%");

    }
}
