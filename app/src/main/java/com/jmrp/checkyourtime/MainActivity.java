package com.jmrp.checkyourtime;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.ads.MobileAds;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextInputLayout til_action;
    private TextInputLayout til_spended_times;
    private TextInputLayout til_duration;
    private EditText userBirthdate;
    private Calendar myCalendar = Calendar.getInstance();
    private Spinner spinner_units;
    private EditText edt_units;
    private Spinner spinner_time_interval;
    private Spinner spinner_ocurrences;
    private EditText edt_duration;
    private Button btn_calculate;
    private boolean enabledFirst = true;
    private boolean enabledSecond = true;
    private boolean errors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MobileAds.initialize(getApplicationContext(), "ca-app-pub-9483075303153381~1062037872");

        configTextInputLayouts();

        configSpinnerTime();

        configSpinnerTimeIntervals();

        configSpinnerTimeUnits();

        configEdtBirthdate();
    }


    private void configTextInputLayouts(){

        til_action = (TextInputLayout) findViewById(R.id.til_action);
        til_spended_times = (TextInputLayout) findViewById(R.id.til_spended_times);
        til_duration = (TextInputLayout) findViewById(R.id.til_duration);

        if(null!=til_action.getEditText()) {
            til_action.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        isValidAction(til_action.getEditText().getText().toString());
                    }
                }
            });
        }

        if(null!=til_spended_times.getEditText()) {
            til_spended_times.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        isValidSpendedTime(til_spended_times.getEditText().getText().toString());
                    }
                }
            });
        }

        if(null!=til_duration.getEditText()) {
            til_duration.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
                public void onFocusChange(View v, boolean hasFocus) {
                    if(!hasFocus) {
                        isValidDuration(til_duration.getEditText().getText().toString());
                    }
                }
            });
        }
    }


    private void configSpinnerTime(){
        edt_units = (EditText) findViewById(R.id.edt_units);

        spinner_units = (Spinner) findViewById(R.id.spinner_units);

        // Initializing an ArrayAdapter
        List<String> items = Arrays.asList(getResources().getStringArray(R.array.tiempo_intervalos));
        final ArrayAdapter<String> adapterTime = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, items){
            @Override
            public boolean isEnabled(int position){
                return position != 0;
            }

            @Override
            public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0) tv.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.secondary_text));
                return view;
            }
        };

        adapterTime.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_units.setAdapter(adapterTime);

        spinner_units.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                if(position != 0) spinner_time_interval.setEnabled(true);
                else {
                    spinner_time_interval.setSelection(0);
                    spinner_time_interval.setEnabled(false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
    }


    private boolean isValidAction(String titulo) {
        if (!titulo.equals("")){
            til_action.setError(null);
            errors = false;
            return true;
        } else {
            errors = true;
            til_action.setError(getResources().getString(R.string.error_empty_action));
            return false;
        }
    }


    private boolean isValidSpendedTime(String titulo) {
        if (!titulo.equals("")){
            til_spended_times.setError(null);
            errors = false;
            return true;
        } else {
            errors = true;
            til_spended_times.setError(getResources().getString(R.string.error_empty_time));
            return false;
        }
    }


    private boolean isValidDuration(String titulo) {
        if (!titulo.equals("")){
            til_duration.setError(null);
            errors = false;
            return true;
        } else {
            errors = true;
            til_duration.setError(getResources().getString(R.string.error_empty_duration));
            return false;
        }
    }


    private void configSpinnerTimeIntervals(){
        spinner_time_interval = (Spinner) findViewById(R.id.spinner_time_interval);

        // Initializing an ArrayAdapter
        final List<String> items = Arrays.asList(getResources().getStringArray(R.array.time_units));
        final ArrayAdapter<String> adapterTime = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, items){
            @Override
            public boolean isEnabled(int position) {
                return position!=0;
            }

            @Override
            public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0) tv.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.secondary_text));
                return view;
            }
        };

        adapterTime.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_time_interval.setAdapter(adapterTime);

        spinner_time_interval.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                if(position == 1) {
                    spinner_ocurrences.setEnabled(true);
                    enabledFirst = false;
                    enabledSecond = true;
                }
                else if(position == 2) {
                    spinner_ocurrences.setEnabled(true);
                    enabledFirst = false;
                    enabledSecond = true;
                }
                else if(position==3) {
                    spinner_ocurrences.setEnabled(true);
                    enabledFirst = false;
                    enabledSecond = false;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
    }


    private void configSpinnerTimeUnits(){

        edt_duration = (EditText) findViewById(R.id.edt_duration);

        spinner_ocurrences = (Spinner) findViewById(R.id.spinner_ocurrences);

        // Initializing an ArrayAdapter
        List<String> items = Arrays.asList(getResources().getStringArray(R.array.time_units));
        final ArrayAdapter<String> adapterTime = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, items){
            @Override
            public boolean isEnabled(int position){
                return position != 0 && !(!enabledFirst && position == 1) && !(!enabledSecond && position == 2);
            }

            @Override
            public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0) tv.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.secondary_text));
                else if(!enabledFirst && position==1) tv.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.secondary_text));
                else if(!enabledSecond && position==2) tv.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.secondary_text));
                return view;
            }
        };

        adapterTime.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_ocurrences.setAdapter(adapterTime);
    }


    private void configEdtBirthdate(){
        userBirthdate = (EditText) findViewById(R.id.edt_birthdate);

        userBirthdate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(MainActivity.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        btn_calculate = (Button) findViewById(R.id.btn_calculate);

    }


    private DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            String myFormat = "MM/dd/yyyy";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            userBirthdate.setText(sdf.format(myCalendar.getTime()));

            btn_calculate.setEnabled(true);
        }
    };


    public void calculate(View view){
        if(!errors){
            long totalLifeTime = System.currentTimeMillis() - myCalendar.getTimeInMillis();
            long totalTime = getDedicatedTime();

            float percent = round((totalTime*100)/totalLifeTime, 2);

            Log.i(getClass().getSimpleName(), "TotalTime: " + totalTime);
            Log.i(getClass().getSimpleName(), "Percent: " + percent);

            Intent intent = new Intent(MainActivity.this, ResultActivity.class);
            intent.putExtra("ACTIVITY", til_action.getEditText().getText().toString());
            intent.putExtra("TIME", totalTime);
            intent.putExtra("PERCENT", percent);
            startActivity(intent);

            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
        }
    }


    private long getDedicatedTime(){
        long dedicatedTime = 0L;

        if(spinner_units.getSelectedItemPosition()==1) dedicatedTime = (long)(Integer.parseInt(edt_units.getText().toString())*1000);
        else if(spinner_units.getSelectedItemPosition()==2) dedicatedTime = (long)(Integer.parseInt(edt_units.getText().toString())*60000);
        else if(spinner_units.getSelectedItemPosition()==3) dedicatedTime = (long)(Integer.parseInt(edt_units.getText().toString())*3600000);


        if(spinner_time_interval.getSelectedItemPosition()==1 && spinner_ocurrences.getSelectedItemPosition()==2) {
            dedicatedTime = Long.parseLong(edt_duration.getText().toString())*30*dedicatedTime;
        }
        else if(spinner_time_interval.getSelectedItemPosition()==1 && spinner_ocurrences.getSelectedItemPosition()==3) {
            dedicatedTime = Long.parseLong(edt_duration.getText().toString())*365*dedicatedTime;
        }

        if(spinner_time_interval.getSelectedItemPosition()==2 && spinner_ocurrences.getSelectedItemPosition()==3) {
            dedicatedTime = Long.parseLong(edt_duration.getText().toString())*12*dedicatedTime;
        }

        Log.i(getClass().getSimpleName(), "DedicatedTime: " + dedicatedTime);
        return dedicatedTime;
    }


    public static float round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }
}
