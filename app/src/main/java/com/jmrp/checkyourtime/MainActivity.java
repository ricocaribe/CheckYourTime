package com.jmrp.checkyourtime;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private Spinner spinner_time;
    private Spinner spinner_time_units;
    private EditText userBirthdate;
    private Calendar myCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        configSpinnerTime();

        configSpinnerTimeUnits();

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

    private void configSpinnerTime(){
        spinner_time = (Spinner) findViewById(R.id.spinner_time);

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
        spinner_time.setAdapter(adapterTime);
    }

    private void configSpinnerTimeUnits(){
        spinner_time_units = (Spinner) findViewById(R.id.spinner_time_units);
        ArrayAdapter<CharSequence> adapterTimeUnits = ArrayAdapter.createFromResource(this,
                R.array.time_units, android.R.layout.simple_spinner_item);
        adapterTimeUnits.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_time_units.setAdapter(adapterTimeUnits);
        spinner_time_units.setSelection(0);
    }
}
