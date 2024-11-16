package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class PersonalDataActivity extends AppCompatActivity {
    private EditText nameInput, emailInput;
    private RadioGroup genderGroup;
    private DatePicker dobPicker;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_data);

        nameInput = findViewById(R.id.nameInput);
        emailInput = findViewById(R.id.emailInput);
        dobPicker = findViewById(R.id.dobPicker);
        genderGroup = findViewById(R.id.genderGroup);
        Button nextButton = findViewById(R.id.nextButton);

        nextButton.setOnClickListener(v -> {
            if (validateInputs()) {
                Intent intent = new Intent(PersonalDataActivity.this, AdditionalDataActivity.class);
                intent.putExtra("NAME", nameInput.getText().toString());
                intent.putExtra("EMAIL", emailInput.getText().toString());
                intent.putExtra("GENDER", getSelectedGender());
                intent.putExtra("DOB", getSelectedDOB());
                startActivity(intent);
            }
        });
    }

    private boolean validateInputs() {
        String name = nameInput.getText().toString().trim();
        String email = emailInput.getText().toString().trim();

        if (name.isEmpty()) {
            Toast.makeText(this, "Name is required", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (email.isEmpty()) {
            Toast.makeText(this, "Email is required", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private String getSelectedGender() {
        int selectedId = genderGroup.getCheckedRadioButtonId();
        if (selectedId == -1) {
            return "Not specified"; // Default value if no selection is made
        }
        return ((RadioButton) findViewById(selectedId)).getText().toString();
    }

    private String getSelectedDOB() {
        int day = dobPicker.getDayOfMonth();
        int month = dobPicker.getMonth() + 1; // Month is 0-indexed
        int year = dobPicker.getYear();
        return day + "/" + month + "/" + year; // Format your date as needed
    }
}
