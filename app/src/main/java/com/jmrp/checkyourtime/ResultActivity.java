package com.jmrp.checkyourtime;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.NativeExpressAdView;

import java.util.concurrent.TimeUnit;

public class ResultActivity extends AppCompatActivity {

    private NativeExpressAdView mNativeExpressAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        mNativeExpressAdView = (NativeExpressAdView) findViewById(R.id.adViewNative);
        AdRequest request = new AdRequest.Builder().build();//.addTestDevice("32A296D5FA19CA4B7400E595FEE254E4")
        mNativeExpressAdView.loadAd(request);

        mNativeExpressAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                Log.i("Ads", "onAdLoaded");
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
                Log.i("Ads", "onAdFailedToLoad");
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
                Log.i("Ads", "onAdOpened");
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
                Log.i("Ads", "onAdLeftApplication");
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when when the user is about to return
                // to the app after tapping on an ad.
                Log.i("Ads", "onAdClosed");
            }
        });

        TextView txt_result_actividad = (TextView) findViewById(R.id.txt_result_actividad);

        txt_result_actividad.setText(getIntent().getStringExtra("ACTIVITY"));

        TextView txtTime = (TextView) findViewById(R.id.txt_result_time);
        String hms = parseTime(getIntent().getLongExtra("TIME", 0L));
        txtTime.setText(hms);

        TextView txtPercent = (TextView) findViewById(R.id.txt_result_percent);
        txtPercent.setText(String.format("%s%% \n %s", String.format("%.2f", getIntent().getDoubleExtra("PERCENT", 0.0d)), getResources().getString(R.string.live_percent)));

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_result_act, menu);
        MenuItem newFormItem = menu.findItem(R.id.item_new_form);
        //Click del item del menu para abrir la barra de búsquedas o para contraerla
        newFormItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                newActivity();
                return false;
            }
        });

        return true;
    }


    @Override
    public void onResume() {
        super.onResume();
        // Resume the NativeExpressAdView.
        mNativeExpressAdView.resume();
    }

    @Override
    public void onPause() {
        // Pause the NativeExpressAdView.
        mNativeExpressAdView.pause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        // Destroy the NativeExpressAdView.
        mNativeExpressAdView.destroy();
        super.onDestroy();
    }


    @Override
    public void onBackPressed() {
        showExitDialog();
    }

    private void newActivity(){
        startActivity(new Intent(ResultActivity.this, MainActivity.class));
    }

    private void showExitDialog(){
        new android.app.AlertDialog.Builder(ResultActivity.this)
                .setTitle(R.string.app_name)
                .setCancelable(false)
                .setMessage(getResources().getString(R.string.exit_message))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private String parseTime(long millis){
        String time = "";

        long days = TimeUnit.MILLISECONDS.toDays(millis);
        long hours = TimeUnit.MILLISECONDS.toHours(millis) % TimeUnit.DAYS.toHours(1);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1);

        Log.i(getClass().getSimpleName(), "Time: Days:" + days + ", Hours: " + hours + ", Minutes: " + minutes + ", Seconds: " + seconds);

        if(days>0) time = String.valueOf(days) + "d ";
        if(hours>0) time = time.concat(String.format("%sh ", String.valueOf(hours)));
        if(minutes>0) time = time.concat(String.format("%s' ", String.valueOf(minutes)));
        if(seconds>0) time = time.concat(String.format("%s''", String.valueOf(seconds)));

        return time;
    }
}
