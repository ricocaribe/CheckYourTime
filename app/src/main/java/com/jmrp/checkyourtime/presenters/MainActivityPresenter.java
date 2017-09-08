package com.jmrp.checkyourtime.presenters;

import android.util.Log;

import com.jmrp.checkyourtime.R;
import com.jmrp.checkyourtime.interactors.Interactor;
import com.jmrp.checkyourtime.models.Result;

import java.util.Calendar;


public class MainActivityPresenter implements Interactor.PresenterMain {

    private Interactor.VistaMain vista;

    // El presentador recibe su vista para devolverle cosas.
    @Override
    public void setVista(Interactor.VistaMain vista) {
        this.vista = vista;
    }


    // Cuando la vista pide al presentador el resultado de la operación, el presentador ejecutará
    // el caso de uso pertinente.
    @Override
    public void calculateResult(Calendar myCalendar){

        long totalLifeTime = System.currentTimeMillis() - myCalendar.getTimeInMillis();

        if(!errors(totalLifeTime)){
            // Ejecuta el caso de uso
            long totalTime = getDedicatedTime();

            Log.i(getClass().getSimpleName(), "LifeTime: " + totalLifeTime);
            Log.i(getClass().getSimpleName(), "Time: " + totalTime);

            double percent = (double) (totalTime*100)/totalLifeTime;

            Log.i(getClass().getSimpleName(), "Percent: " + percent);

            Result resultado = new Result(totalTime, percent);
            // Se llama a un método de la vista para devolverle el resultado.
            vista.goResultActivity(resultado);
        }
    }

    private boolean errors(long lifetime) {

        Boolean[] errores = new Boolean[13];

        vista.getErrores(errores);

        boolean errors = false;

        if((errores[0] || errores[1] || errores[2] || errores[3] || errores[4] || errores[5] || errores[6] || errores[7] || errores[8] || errores[9] || errores[10])){

            vista.showError(vista.getContext().getResources().getString(R.string.error_complete));
            errors = true;
        }
        else if(getDedicatedTime()>=lifetime || (errores[11] && errores[12])){

            vista.showError(vista.getContext().getResources().getString(R.string.error_duration));
            errors = true;
        }

        return errors;
    }

    private long getDedicatedTime() {
        long dedicatedTime = 0L;

        int[] selectedItems = new int[3];
        vista.getSelectedItems(selectedItems);
        String[] edtFields = new String[2];
        vista.getDuration(edtFields);

        if(selectedItems[0]==1) dedicatedTime = (long)(Integer.parseInt(edtFields[1])*1000);
        else if(selectedItems[0]==2) dedicatedTime = (long)(Integer.parseInt(edtFields[1])*60000);
        else if(selectedItems[0]==3) dedicatedTime = (long)(Integer.parseInt(edtFields[1])*3600000);

        Log.i(getClass().getSimpleName(), "InitialDedicatedTime: " + dedicatedTime);

        if(selectedItems[1]==1 && selectedItems[2]==1) {
            dedicatedTime = Long.parseLong(edtFields[0])*dedicatedTime;
        }
        else if(selectedItems[1]==1 && selectedItems[2]==2) {
            dedicatedTime = Long.parseLong(edtFields[0])*30*dedicatedTime;
        }
        else if(selectedItems[1]==1 && selectedItems[2]==3) {
            dedicatedTime = Long.parseLong(edtFields[0])*365*dedicatedTime;
        }

        if(selectedItems[1]==2 && selectedItems[2]==2) {
            dedicatedTime = Long.parseLong(edtFields[0])*dedicatedTime;
        }
        else if(selectedItems[1]==2 && selectedItems[2]==3) {
            dedicatedTime = Long.parseLong(edtFields[0])*12*dedicatedTime;
        }

        if(selectedItems[1]==3 && selectedItems[2]==3) {
            dedicatedTime = Long.parseLong(edtFields[0])*dedicatedTime;
        }

        Log.i(getClass().getSimpleName(), "FinalDedicatedTime: " + dedicatedTime);

        return dedicatedTime;
    }
}
