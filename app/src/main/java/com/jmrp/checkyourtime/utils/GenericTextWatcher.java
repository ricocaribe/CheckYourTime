package com.jmrp.checkyourtime.utils;


import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;

import com.jmrp.checkyourtime.R;

public class GenericTextWatcher implements TextWatcher {

    private TextInputLayout txtInput;
    private Context context;
    public GenericTextWatcher(TextInputLayout txtInput, Context context) {
        this.txtInput = txtInput;
        this.context = context;
    }

    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

    public void afterTextChanged(Editable editable) {
        switch(txtInput.getId()){
            case R.id.til_action:
                if(editable.length()!=0) txtInput.setError(null);
                else txtInput.setError(context.getResources().getString(R.string.error_empty_action));
                break;
            case R.id.til_spended_times:
                if(editable.length()!=0) txtInput.setError(null);
                else txtInput.setError(context.getResources().getString(R.string.error_empty_action));
                break;
            case R.id.til_duration:
                if(editable.length()!=0) txtInput.setError(null);
                else txtInput.setError(context.getResources().getString(R.string.error_empty_action));
                break;
            case R.id.til_birthdate:
                if(editable.length()!=0) txtInput.setError(null);
                else txtInput.setError(context.getResources().getString(R.string.error_empty_birthdate));
                break;
        }
    }
}
