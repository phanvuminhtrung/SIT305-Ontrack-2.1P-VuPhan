package com.example.task21p;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Spinner spinnerConversionType, spinnerSourceUnit, spinnerDestinationUnit;
    private EditText editTextInput;
    private Button buttonConvert;
    private TextView textViewResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinnerConversionType = findViewById(R.id.spinnerConversionType);
        spinnerSourceUnit = findViewById(R.id.spinnerSourceUnit);
        spinnerDestinationUnit = findViewById(R.id.spinnerDestinationUnit);
        editTextInput = findViewById(R.id.editTextInput);
        buttonConvert = findViewById(R.id.buttonConvert);
        textViewResult = findViewById(R.id.textViewResult);

        // Populate conversion type spinner
        ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(
                this, R.array.conversion_types, android.R.layout.simple_spinner_item);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerConversionType.setAdapter(typeAdapter);

        // Load corresponding unit spinners on type selection
        spinnerConversionType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                int unitsArrayId = R.array.length_units;
                if (position == 1) unitsArrayId = R.array.weight_units;
                else if (position == 2) unitsArrayId = R.array.temperature_units;

                ArrayAdapter<CharSequence> unitsAdapter = ArrayAdapter.createFromResource(
                        MainActivity.this, unitsArrayId, android.R.layout.simple_spinner_item);
                unitsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerSourceUnit.setAdapter(unitsAdapter);
                spinnerDestinationUnit.setAdapter(unitsAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        buttonConvert.setOnClickListener(view -> {
            String inputStr = editTextInput.getText().toString();
            if (inputStr.isEmpty()) {
                Toast.makeText(this, "Please enter a value to convert.", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                double inputValue = Double.parseDouble(inputStr);
                String type = spinnerConversionType.getSelectedItem().toString();
                String fromUnit = spinnerSourceUnit.getSelectedItem().toString();
                String toUnit = spinnerDestinationUnit.getSelectedItem().toString();

                double result = convert(type, fromUnit, toUnit, inputValue);
                textViewResult.setText("Result: " + result);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid number format.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Covert method
    private double convert(String type, String from, String to, double value) {
        if (type.equals("Length")) {
            double cm = 0;
            if (from.equals("Inch")) cm = value * 2.54;
            else if (from.equals("Foot")) cm = value * 30.48;
            else if (from.equals("Yard")) cm = value * 91.44;
            else if (from.equals("Mile")) cm = value * 160934;
            else if (from.equals("Kilometer")) cm = value * 100000;
            else if (from.equals("Centimeter")) cm = value;

            if (to.equals("Inch")) return cm / 2.54;
            else if (to.equals("Foot")) return cm / 30.48;
            else if (to.equals("Yard")) return cm / 91.44;
            else if (to.equals("Mile")) return cm / 160934;
            else if (to.equals("Kilometer")) return cm / 100000;
            else return cm;
        }

        if (type.equals("Weight")) {
            double kg = 0;
            if (from.equals("Pound")) kg = value * 0.453592;
            else if (from.equals("Ounce")) kg = value * 0.0283495;
            else if (from.equals("Ton")) kg = value * 907.185;
            else if (from.equals("Gram")) kg = value / 1000;
            else if (from.equals("Kilogram")) kg = value;

            if (to.equals("Pound")) return kg / 0.453592;
            else if (to.equals("Ounce")) return kg / 0.0283495;
            else if (to.equals("Ton")) return kg / 907.185;
            else if (to.equals("Gram")) return kg * 1000;
            else return kg;
        }

        if (type.equals("Temperature")) {
            if (from.equals("Celsius")) {
                if (to.equals("Fahrenheit")) return (value * 1.8) + 32;
                else if (to.equals("Kelvin")) return value + 273.15;
                else return value;
            } else if (from.equals("Fahrenheit")) {
                double c = (value - 32) / 1.8;
                if (to.equals("Kelvin")) return c + 273.15;
                else if (to.equals("Celsius")) return c;
                else return value;
            } else if (from.equals("Kelvin")) {
                double c = value - 273.15;
                if (to.equals("Fahrenheit")) return (c * 1.8) + 32;
                else if (to.equals("Celsius")) return c;
                else return value;
            }
        }

        return 0;
    }
}