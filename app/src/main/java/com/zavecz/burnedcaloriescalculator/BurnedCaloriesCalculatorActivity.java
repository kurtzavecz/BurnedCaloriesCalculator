package com.zavecz.burnedcaloriescalculator;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.NumberFormat;

public class BurnedCaloriesCalculatorActivity extends AppCompatActivity
        implements TextView.OnEditorActionListener, AdapterView.OnItemSelectedListener {

    //define the weidgets
    private EditText weightET;
    private TextView milesRanTV;
    private TextView calsBurnedTV;
    private TextView bmiTV;
    private EditText nameET;
    private SeekBar milesSB;
    private Spinner ftSpinner;
    private Spinner inchSpinner;
    private float mileRan = 1;

    private String weightString = "";
    private String feetString = "";
    private String inchString = "";

    //define SharedPrefferences object
    private SharedPreferences mSavedValues;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_burned_calories_calculator);

        //get references
        weightET = (EditText) findViewById(R.id.mWeightEditText);
        milesRanTV = (TextView) findViewById(R.id.mMilesLabel);
        calsBurnedTV = (TextView) findViewById(R.id.mCaloriesLabel);
        bmiTV = (TextView) findViewById(R.id.mBMI);
        nameET = (EditText) findViewById(R.id.mNameEditText);
        milesSB = (SeekBar) findViewById(R.id.mMileSeekBar);
        ftSpinner = (Spinner) findViewById(R.id.mFootSpinner);
        inchSpinner = (Spinner) findViewById(R.id.mInchesSpinner);


        //set listeners
        weightET.setOnEditorActionListener(this);
        milesSB.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                calcAndDisplay();
            }
        });
        nameET.setOnEditorActionListener(this);

        //set array adapter
        ArrayAdapter<CharSequence> adapterFT = ArrayAdapter.createFromResource(this, R.array.ft_split_array, android.R.layout.simple_spinner_item);
        adapterFT.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ftSpinner.setAdapter(adapterFT);

        ArrayAdapter<CharSequence> adapterINCH = ArrayAdapter.createFromResource(this, R.array.inch_split_array, android.R.layout.simple_spinner_item);
        adapterINCH.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        inchSpinner.setAdapter(adapterINCH);



        //get SharedPreferences object
        mSavedValues = getSharedPreferences("savedValues", MODE_PRIVATE);

    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if(actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_UNSPECIFIED){
            calcAndDisplay();
        }
        return false;
    }

    private void calcAndDisplay(){

        weightString = weightET.getText().toString();
        float weight;
        if(weightString.equals("")){
            weight = 0;
        }else{
            weight = Float.parseFloat(weightString);
        }

        int milesRan = milesSB.getProgress();
        mileRan = (float) milesRan;


        feetString = ftSpinner.getSelectedItem().toString();
        int feet = Integer.parseInt(feetString);


        inchString = inchSpinner.getSelectedItem().toString();
        int inches = Integer.parseInt(inchString);

        float caloriesBurned = (float) (0.75 * weight * mileRan);
        float bmi = (weight * 703) / ((12 * feet + inches) * (12 * feet + inches));

        NumberFormat numbers = NumberFormat.getInstance();
        milesRanTV.setText(numbers.format(mileRan));
        calsBurnedTV.setText(numbers.format(caloriesBurned));
        bmiTV.setText(numbers.format(bmi));

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        parent.getItemAtPosition(position);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    protected void onPause() {

        //save the instance variables
        SharedPreferences.Editor editor = mSavedValues.edit();
        editor.putString("weightETString", weightString);
        editor.putFloat("milesRanFloat", mileRan);
        editor.apply();

        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();

        //get instance variables
        weightString = mSavedValues.getString("weightETString", "");
        mileRan = mSavedValues.getFloat("milesRanFloat", 1);

        //set the bill amount on its widget
        weightET.setText(weightString);

        //call the calculate and display method
        calcAndDisplay();
    }
}
