package com.jmrp.checkyourtime;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        TextView txtTime = (TextView) findViewById(R.id.txt_result_time);
        long millis =  getIntent().getLongExtra("TIME", 0L);
        String hms = String.format("%02dhh:%02dmm:%02dss", TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1));
        txtTime.setText(hms);

        TextView txtPercent = (TextView) findViewById(R.id.txt_result_percent);
        txtPercent.setText(getIntent().getFloatExtra("PERCENT", 0.0f) + "% de tu vida XD");

    }

    private String secondsToString(long seconds) {
        double numyears = Math.floor(seconds / 31536000);
        double numdays = Math.floor((seconds % 31536000) / 86400);
        double numhours = Math.floor(((seconds % 31536000) % 86400) / 3600);
        double numminutes = Math.floor((((seconds % 31536000) % 86400) % 3600) / 60);
        double numseconds = (((seconds % 31536000) % 86400) % 3600) % 60;
        return numyears + " years " +  numdays + " days " + numhours + " hours " + numminutes + " minutes " + numseconds + " seconds";
    }
}
