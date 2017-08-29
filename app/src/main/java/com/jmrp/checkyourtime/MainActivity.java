package com.jmrp.checkyourtime;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.MobileAds;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.jmrp.checkyourtime.utils.GenericTextWatcher;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextInputLayout til_action;
    private EditText edt_action;
    private TextInputLayout til_spended_times;
    private TextInputLayout til_duration;
    private TextInputLayout til_userBirthdate;
    private EditText userBirthdate;
    private Calendar myCalendar = Calendar.getInstance();
    private Spinner spinner_units;
    private EditText edt_units;
    private Spinner spinner_time_interval;
    private Spinner spinner_duration;
    private EditText edt_duration;
    private boolean enabledFirst = true;
    private boolean enabledSecond = true;
    private DatePickerDialog datePickerDialog;
    private FirebaseAnalytics mFirebaseAnalytics;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        MobileAds.initialize(getApplicationContext(), "ca-app-pub-9483075303153381~1062037872");

        configTextInputLayouts();

        configSpinnerTime();

        configSpinnerTimeIntervals();

        configSpinnerTimeUnits();

        configEdtBirthdate();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_act, menu);
        MenuItem clearFormItem = menu.findItem(R.id.item_clear_form);
        //Click del item del menu para abrir la barra de búsquedas o para contraerla
        clearFormItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                clearForm();
                return false;
            }
        });

        return true;
    }


    @Override
    public void onBackPressed() {
        showExitDialog();
    }



    private void configTextInputLayouts(){
        til_action = (TextInputLayout) findViewById(R.id.til_action);
        edt_action = (EditText) findViewById(R.id.edt_action);
        til_spended_times = (TextInputLayout) findViewById(R.id.til_spended_times);
        til_duration = (TextInputLayout) findViewById(R.id.til_duration);

        if(null!=til_action.getEditText()) til_action.getEditText().addTextChangedListener(new GenericTextWatcher(til_action, getApplicationContext()));
        if(null!=til_spended_times.getEditText()) til_spended_times.getEditText().addTextChangedListener(new GenericTextWatcher(til_spended_times, getApplicationContext()));
        if(null!=til_duration.getEditText()) til_duration.getEditText().addTextChangedListener(new GenericTextWatcher(til_duration, getApplicationContext()));
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

                spinner_duration.setSelection(0);
                edt_duration.getText().clear();

                if(position == 1) {
                    enabledFirst = true;
                    enabledSecond = true;
                    spinner_duration.setEnabled(true);
                }
                else if(position == 2) {
                    enabledFirst = false;
                    enabledSecond = true;
                    spinner_duration.setEnabled(true);
                }
                else if(position==3) {
                    enabledFirst = false;
                    enabledSecond = false;
                    spinner_duration.setEnabled(true);
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

        spinner_duration = (Spinner) findViewById(R.id.spinner_duration);
        spinner_duration.setEnabled(false);

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
        spinner_duration.setAdapter(adapterTime);
    }


    private void configEdtBirthdate(){
        til_userBirthdate = (TextInputLayout) findViewById(R.id.til_birthdate);

        userBirthdate = (EditText) findViewById(R.id.edt_birthdate);

        if(null!=til_userBirthdate) til_userBirthdate.getEditText().addTextChangedListener(new GenericTextWatcher(til_userBirthdate, getApplicationContext()));

        userBirthdate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                datePickerDialog = new DatePickerDialog(MainActivity.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
//                datePickerDialog.getDatePicker().setMaxDate(getDedicatedTime());
                datePickerDialog.show();
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

        if(!errors(totalLifeTime)){

            long totalTime = getDedicatedTime();

            Bundle params = new Bundle();
            params.putString("activity", edt_action.getText().toString());
            params.putLong("dedicated_time", totalTime);
            mFirebaseAnalytics.logEvent("calculate", params);

            Log.i(getClass().getSimpleName(), "LifeTime: " + totalLifeTime);
            Log.i(getClass().getSimpleName(), "Time: " + totalTime);

            double percent = (double) (totalTime*100)/totalLifeTime;

            Log.i(getClass().getSimpleName(), "Percent: " + percent);

            Intent intent = new Intent(MainActivity.this, ResultActivity.class);
            intent.putExtra("ACTIVITY", edt_action.getText().toString());
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

        Log.i(getClass().getSimpleName(), "InitialDedicatedTime: " + dedicatedTime);

        if(spinner_time_interval.getSelectedItemPosition()==1 && spinner_duration.getSelectedItemPosition()==1) {
            dedicatedTime = Long.parseLong(edt_duration.getText().toString())*dedicatedTime;
        }
        else if(spinner_time_interval.getSelectedItemPosition()==1 && spinner_duration.getSelectedItemPosition()==2) {
            dedicatedTime = Long.parseLong(edt_duration.getText().toString())*30*dedicatedTime;
        }
        else if(spinner_time_interval.getSelectedItemPosition()==1 && spinner_duration.getSelectedItemPosition()==3) {
            dedicatedTime = Long.parseLong(edt_duration.getText().toString())*365*dedicatedTime;
        }

        if(spinner_time_interval.getSelectedItemPosition()==2 && spinner_duration.getSelectedItemPosition()==2) {
            dedicatedTime = Long.parseLong(edt_duration.getText().toString())*dedicatedTime;
        }
        else if(spinner_time_interval.getSelectedItemPosition()==2 && spinner_duration.getSelectedItemPosition()==3) {
            dedicatedTime = Long.parseLong(edt_duration.getText().toString())*12*dedicatedTime;
        }

        if(spinner_time_interval.getSelectedItemPosition()==3 && spinner_duration.getSelectedItemPosition()==3) {
            dedicatedTime = Long.parseLong(edt_duration.getText().toString())*dedicatedTime;
        }

        Log.i(getClass().getSimpleName(), "FinalDedicatedTime: " + dedicatedTime);

        return dedicatedTime;
    }


    private boolean errors(long lifeTime){
        boolean errors = false;

        if(til_action.getError()!=null ||
                til_spended_times.getError()!=null ||
                til_duration.getError()!=null ||
                til_duration.getEditText().getText().toString().isEmpty() ||
                til_spended_times.getEditText().getText().toString().isEmpty() ||
                til_action.getEditText().getText().toString().isEmpty() ||
                til_userBirthdate.getEditText().getText().toString().isEmpty()  ||
                til_userBirthdate.getError()!=null ||
                spinner_units.getSelectedItemPosition()==0 ||
                spinner_time_interval.getSelectedItemPosition()==0 ||
                spinner_duration.getSelectedItemPosition()==0){

            showError(getResources().getString(R.string.error_complete));
            errors = true;
        }
        else if(getDedicatedTime()>=lifeTime || (spinner_duration.getSelectedItemPosition()==3 &&
                Integer.parseInt(edt_duration.getText().toString())>(Calendar.getInstance().get(Calendar.YEAR) - myCalendar.get(Calendar.YEAR)))){

            showError(getResources().getString(R.string.error_duration));

            errors = true;
        }

        return errors;
    }


    private void showError(String message){
        LayoutInflater inflater = getLayoutInflater();
        View toastLayout = inflater.inflate(R.layout.custom_toast, (ViewGroup) findViewById(R.id.custom_toast_layout));
        TextView textView = (TextView) toastLayout.findViewById(R.id.custom_toast_message);
        textView.setText(message);
        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(toastLayout);
        toast.show();
    }


    private void clearForm(){
        edt_duration.getText().clear();
        edt_units.getText().clear();
        edt_action.getText().clear();
        userBirthdate.getText().clear();

        spinner_duration.setSelection(0);
        spinner_time_interval.setSelection(0);
        spinner_units.setSelection(0);
    }


    private void showExitDialog(){
        new android.app.AlertDialog.Builder(MainActivity.this)
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
}
