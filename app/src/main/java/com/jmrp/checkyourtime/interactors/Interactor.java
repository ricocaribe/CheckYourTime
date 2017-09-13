package com.jmrp.checkyourtime.interactors;

import android.content.Context;

import com.jmrp.checkyourtime.models.Result;

import java.util.Calendar;

public interface Interactor {

    interface VistaMain {
        Context getContext();
        void getErrores(Boolean[] operandos);
        void getSelectedItems(int[] selectedItems);
        void goResultActivity(Result result);
        void getDuration(String[] duration);
        void showError(String error);
    }

    interface PresenterMain {
        void setVista(VistaMain vista);
        void calculateResult(Calendar myCalendar);
    }

    interface VistaResult {
        void showResult(String time);
    }

    interface PresenterResult{
        void setVista(VistaResult vista);
        void getParsedTime(long time);
    }

    void showExitDialog();
}
