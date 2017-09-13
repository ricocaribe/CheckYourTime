package com.jmrp.checkyourtime.presenters;


import android.util.Log;

import com.jmrp.checkyourtime.interactors.Interactor;

import java.util.concurrent.TimeUnit;


public class ResultActivityPresenter implements Interactor.PresenterResult {

    private Interactor.VistaResult vista;

    // El presentador recibe su vista para devolverle cosas.
    @Override
    public void setVista(Interactor.VistaResult vista) {
        this.vista = vista;
    }

    @Override
    public void getParsedTime(long time) {

        vista.showResult(parseTime(time));
    }

    public String parseTime(long millis){
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
