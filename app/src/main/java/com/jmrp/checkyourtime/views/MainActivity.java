package com.jmrp.checkyourtime.views;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.jmrp.checkyourtime.R;
import com.jmrp.checkyourtime.interactors.Interactor;
import com.jmrp.checkyourtime.dagger.MainModule;
import com.jmrp.checkyourtime.models.Result;
import com.jmrp.checkyourtime.utils.GenericTextWatcher;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import dagger.ObjectGraph;

public class MainActivity extends AppCompatActivity implements Interactor.VistaMain, CommonMethods{

    @Inject
    Interactor.PresenterMain presenterMain;

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

        // Inyecta las clases con Dagger. Esto solo lo tenemos aquí por simplicidad.
        ObjectGraph objectGraph = ObjectGraph.create(new MainModule());
        objectGraph.inject(this);

        // Le dice al presenter cuál es su vista
        presenterMain.setVista(this);

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
        presenterMain.calculateResult(myCalendar);
    }


    private void clearForm(){
        edt_duration.getText().clear();
        edt_units.getText().clear();
        edt_action.getText().clear();
        userBirthdate.getText().clear();

        spinner_duration.setSelection(0);
        spinner_time_interval.setSelection(0);
        spinner_units.setSelection(0);

        mFirebaseAnalytics.logEvent("clear_form", null);
    }

    @Override
    public void showExitDialog(){
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

    @Override
    public Context getContext() {
        return getApplicationContext();
    }

    @Override
    public void getErrores(Boolean[] errores) {
        errores[0] = til_action.getError()!=null;
        errores[1] = til_spended_times.getError()!=null;
        errores[2] = til_duration.getError()!=null;
        errores[3] = til_duration.getEditText().getText().toString().isEmpty();
        errores[4] = til_spended_times.getEditText().getText().toString().isEmpty();
        errores[5] = til_action.getEditText().getText().toString().isEmpty();
        errores[6] = til_userBirthdate.getEditText().getText().toString().isEmpty();
        errores[7] = til_userBirthdate.getError()!=null;
        errores[8] = spinner_units.getSelectedItemPosition()==0;
        errores[9] = spinner_time_interval.getSelectedItemPosition()==0;
        errores[10] =  spinner_duration.getSelectedItemPosition()==0;

        errores[11] =  spinner_duration.getSelectedItemPosition()==3;
        errores[12] =  Integer.parseInt(edt_duration.getText().toString().equals("")?"0":edt_duration.getText().toString())>(Calendar.getInstance().get(Calendar.YEAR) - myCalendar.get(Calendar.YEAR));

    }

    @Override
    public void getSelectedItems(int[] selectedItems) {
        selectedItems[0] = spinner_units.getSelectedItemPosition();
        selectedItems[1] = spinner_time_interval.getSelectedItemPosition();
        selectedItems[2] = spinner_duration.getSelectedItemPosition();
    }

    @Override
    public void goResultActivity(Result result) {
        Bundle params = new Bundle();
        params.putString("activity", edt_action.getText().toString());
        params.putLong("dedicated_time", result.getTotalTime());
        mFirebaseAnalytics.logEvent("calculate", params);

        Intent intent = new Intent(MainActivity.this, ResultActivity.class);
        intent.putExtra("ACTIVITY", edt_action.getText().toString());
        intent.putExtra("TIME", result.getTotalTime());
        intent.putExtra("PERCENT", result.getPercent());
        startActivity(intent);

        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

    @Override
    public void getDuration(String[] duration) {
        duration[0] = edt_duration.getText().toString();
        duration[1] = edt_units.getText().toString();
    }

    @Override
    public void showError(String error) {
        LayoutInflater inflater = getLayoutInflater();
        View toastLayout = inflater.inflate(R.layout.custom_toast, (ViewGroup) findViewById(R.id.custom_toast_layout));
        TextView textView = (TextView) toastLayout.findViewById(R.id.custom_toast_message);
        textView.setText(error);
        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(toastLayout);
        toast.show();
    }
}
