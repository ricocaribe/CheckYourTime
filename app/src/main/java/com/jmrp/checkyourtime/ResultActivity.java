package com.jmrp.checkyourtime;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.NativeExpressAdView;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.concurrent.TimeUnit;

public class ResultActivity extends AppCompatActivity {

    private NativeExpressAdView mNativeExpressAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        mNativeExpressAdView = (NativeExpressAdView) findViewById(R.id.adViewNative);
//        AdRequest request = new AdRequest.Builder().addTestDevice("32A296D5FA19CA4B7400E595FEE254E4").build();
        AdRequest request = new AdRequest.Builder().addTestDevice("32A296D5FA19CA4B7400E595FEE254E4").build();
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
        long millis =  getIntent().getLongExtra("TIME", 0L);
        String hms = String.format("%02dhh:%02dmm:%02dss", TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1));
        txtTime.setText(hms);

        TextView txtPercent = (TextView) findViewById(R.id.txt_result_percent);
        txtPercent.setText(getIntent().getFloatExtra("PERCENT", 0.0f) + "% de tu vida");

    }

    private String secondsToString(long seconds) {
        double numyears = Math.floor(seconds / 31536000);
        double numdays = Math.floor((seconds % 31536000) / 86400);
        double numhours = Math.floor(((seconds % 31536000) % 86400) / 3600);
        double numminutes = Math.floor((((seconds % 31536000) % 86400) % 3600) / 60);
        double numseconds = (((seconds % 31536000) % 86400) % 3600) % 60;
        return numyears + " years " +  numdays + " days " + numhours + " hours " + numminutes + " minutes " + numseconds + " seconds";
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
}
