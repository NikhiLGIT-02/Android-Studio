package com.example.myapplication;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;

public class SurveyActivity extends AppCompatActivity {

    private EditText nameInput, emailInput, courseInput, yearInput, collegeInput;
    private RadioGroup genderGroup;
    private DatePicker datePicker;
    private CheckBox checkBoxSports, checkBoxMusic, checkBoxReading;
    private Button submitButton;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);

        initializeUIComponents();
        configureDatePicker();
        databaseHelper = new DatabaseHelper(this);

        submitButton.setOnClickListener(view -> {
            boolean inserted = saveSurveyData();
            if (inserted) {
                Toast.makeText(SurveyActivity.this, "Survey submitted successfully!", Toast.LENGTH_SHORT).show();

                // Clear focus from input fields to avoid any lingering keyboard animations
                clearInputFocus();

                // Small delay before transition to SubmittedSurveyActivity
                new Handler().postDelayed(() -> {
                    Intent intent = new Intent(SurveyActivity.this, SubmittedSurveyActivity.class);
                    startActivity(intent);
                    overridePendingTransition(0, 0); // Disable transition animations
                }, 200); // 200 ms delay
            } else {
                Toast.makeText(SurveyActivity.this, "Failed to submit survey.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initializeUIComponents() {
        nameInput = findViewById(R.id.nameInput);
        emailInput = findViewById(R.id.emailInput);
        genderGroup = findViewById(R.id.genderGroup);
        datePicker = findViewById(R.id.datePicker);
        checkBoxSports = findViewById(R.id.checkBoxSports);
        checkBoxMusic = findViewById(R.id.checkBoxMusic);
        checkBoxReading = findViewById(R.id.checkBoxReading);
        courseInput = findViewById(R.id.courseInput);
        yearInput = findViewById(R.id.yearInput);
        collegeInput = findViewById(R.id.collegeInput);
        submitButton = findViewById(R.id.submitButton);
    }

    private void configureDatePicker() {
        datePicker.setSpinnersShown(true);
        datePicker.setCalendarViewShown(false);
    }

    private void clearInputFocus() {
        nameInput.clearFocus();
        emailInput.clearFocus();
        courseInput.clearFocus();
        yearInput.clearFocus();
        collegeInput.clearFocus();
    }

    private boolean saveSurveyData() {
        if (!isInputValid()) return false;

        String name = nameInput.getText().toString();
        String email = emailInput.getText().toString();
        String gender = getSelectedGender();
        String dob = getDateOfBirth();
        String interests = getInterests();
        String course = courseInput.getText().toString();
        String year = yearInput.getText().toString();
        String college = collegeInput.getText().toString();
        String review = "";  // Placeholder for review if not provided

        boolean inserted = databaseHelper.insertSurveyData(name, email, gender, dob, interests, course, year, college, review);
        Log.d("SurveyActivity", "Survey inserted: " + inserted); // Log success or failure
        return inserted;
    }

    private boolean isInputValid() {
        if (nameInput.getText().toString().isEmpty() ||
                emailInput.getText().toString().isEmpty() ||
                genderGroup.getCheckedRadioButtonId() == -1 ||
                courseInput.getText().toString().isEmpty() ||
                yearInput.getText().toString().isEmpty() ||
                collegeInput.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Check for valid email format
        String email = emailInput.getText().toString();
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }


    private String getSelectedGender() {
        int selectedGenderId = genderGroup.getCheckedRadioButtonId();
        RadioButton selectedGender = findViewById(selectedGenderId);
        return selectedGender != null ? selectedGender.getText().toString() : "Not specified";
    }

    private String getDateOfBirth() {
        return datePicker.getDayOfMonth() + "/" + (datePicker.getMonth() + 1) + "/" + datePicker.getYear();
    }

    private String getInterests() {
        StringBuilder interests = new StringBuilder();
        if (checkBoxSports.isChecked()) interests.append("Sports ");
        if (checkBoxMusic.isChecked()) interests.append("Music ");
        if (checkBoxReading.isChecked()) interests.append("Reading ");
        return interests.toString().trim();
    }
}
