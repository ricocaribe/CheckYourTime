package com.jmrp.checkyourtime;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private EditText userBirthdate;
    private Calendar myCalendar = Calendar.getInstance();
    private Spinner spinner_units;
    private Spinner spinner_time_interval;
    private Spinner spinner_duration;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        configSpinnerTime();

        configSpinnerTimeIntervals();

        configSpinnerTimeUnits();

        configEdtBirthdate();
    }


    private void configSpinnerTime(){
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
    }


    private void configSpinnerTimeIntervals(){
        spinner_time_interval = (Spinner) findViewById(R.id.spinner_time_interval);

        // Initializing an ArrayAdapter
        List<String> items = Arrays.asList(getResources().getStringArray(R.array.time_units));
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
        spinner_time_interval.setAdapter(adapterTime);
    }


    private void configSpinnerTimeUnits(){
        spinner_duration = (Spinner) findViewById(R.id.spinner_time_units);

        // Initializing an ArrayAdapter
        List<String> items = Arrays.asList(getResources().getStringArray(R.array.time_units));
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
        spinner_duration.setAdapter(adapterTime);
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
        }
    };


    public void calculate(View view){
        long totalLifeTime = System.currentTimeMillis() - myCalendar.getTimeInMillis();
        long totalTime = getDedicatedTime()*getTimeInterval();

        String time = String.valueOf(totalTime/totalLifeTime);
        String percent = String.valueOf((totalTime*100)/totalLifeTime);

        Intent intent = new Intent(MainActivity.this, ResultActivity.class);
        intent.putExtra("TIME", time);
        intent.putExtra("PERCENT", percent);
        startActivity(intent);

        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

    private long getDedicatedTime(){
        long dedicatedTime = 0L;

        if(spinner_units.getSelectedItemPosition()==0) dedicatedTime = Long.parseLong(spinner_units.getSelectedItem().toString())*1000;
        else if(spinner_units.getSelectedItemPosition()==1) dedicatedTime = Long.parseLong(spinner_units.getSelectedItem().toString())*60000;
        else if(spinner_units.getSelectedItemPosition()==2) dedicatedTime = Long.parseLong(spinner_units.getSelectedItem().toString())*3600000;

        return dedicatedTime;
    }

    private int getTimeInterval(){
        int interval = 0;

        if(spinner_duration.getSelectedItemPosition()==0) return 1;
        else if(spinner_duration.getSelectedItemPosition()==1) return 30;
        else if(spinner_duration.getSelectedItemPosition()==2) return 365;

        return interval;
    }

}
