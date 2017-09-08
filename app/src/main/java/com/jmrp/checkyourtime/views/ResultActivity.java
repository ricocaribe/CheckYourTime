package com.jmrp.checkyourtime.views;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.NativeExpressAdView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.jmrp.checkyourtime.R;
import com.jmrp.checkyourtime.dagger.ResultModule;
import com.jmrp.checkyourtime.interactors.Interactor;

import javax.inject.Inject;

import dagger.ObjectGraph;

public class ResultActivity extends AppCompatActivity implements Interactor.VistaResult, CommonMethods{

    @Inject
    Interactor.PresenterResult presenterResult;

    private NativeExpressAdView mNativeExpressAdView;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        // Inyecta las clases con Dagger. Esto solo lo tenemos aquí por simplicidad.
        ObjectGraph objectGraph = ObjectGraph.create(new ResultModule());
        objectGraph.inject(this);

        // Le dice al presenter cuál es su vista
        presenterResult.setVista(this);

        configView();

        presenterResult.getParsedTime(getIntent().getLongExtra("TIME", 0L));
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

    public void configView(){
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        mNativeExpressAdView = (NativeExpressAdView) findViewById(R.id.adViewNative);
        AdRequest request = new AdRequest.Builder().build();//.addTestDevice("32A296D5FA19CA4B7400E595FEE254E4")
        mNativeExpressAdView.loadAd(request);
    }


    private void newActivity(){
        mFirebaseAnalytics.logEvent("new_form", null);

        startActivity(new Intent(ResultActivity.this, MainActivity.class));
    }

    @Override
    public void showExitDialog(){
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


    @Override
    public void showResult(String time) {
        TextView txt_result_actividad = (TextView) findViewById(R.id.txt_result_actividad);
        txt_result_actividad.setText(getIntent().getStringExtra("ACTIVITY"));

        TextView txtTime = (TextView) findViewById(R.id.txt_result_time);
        txtTime.setText(time);

        TextView txtPercent = (TextView) findViewById(R.id.txt_result_percent);
        String prcnt = String.format("%s%% \n %s", String.format("%.2f", getIntent().getDoubleExtra("PERCENT", 0.0d)), getResources().getString(R.string.live_percent));
        txtPercent.setText(prcnt);
    }
}
