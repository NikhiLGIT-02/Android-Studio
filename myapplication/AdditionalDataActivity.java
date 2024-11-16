package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AdditionalDataActivity extends AppCompatActivity {
    private EditText courseInput, yearInput, collegeInput;
    private CheckBox checkBoxSports, checkBoxMusic, checkBoxReading;
    private DatabaseHelper databaseHelper; // Declare DatabaseHelper

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_additional_data);

        courseInput = findViewById(R.id.courseInput);
        yearInput = findViewById(R.id.yearInput);
        collegeInput = findViewById(R.id.collegeInput);
        checkBoxSports = findViewById(R.id.checkBoxSports);
        checkBoxMusic = findViewById(R.id.checkBoxMusic);
        checkBoxReading = findViewById(R.id.checkBoxReading);

        Button submitButton = findViewById(R.id.submitButton);
        databaseHelper = new DatabaseHelper(this); // Initialize DatabaseHelper

        submitButton.setOnClickListener(v -> {
            if (validateInputs()) {
                String course = courseInput.getText().toString();
                String year = yearInput.getText().toString();
                String college = collegeInput.getText().toString();
                String interests = getInterests();

                // Handle submission logic here (e.g., save to database)
                boolean isInserted = databaseHelper.insertSurveyData(
                        "", "", "", "", interests, course, year, college, ""
                ); // Pass other necessary parameters

                Toast.makeText(AdditionalDataActivity.this,
                        isInserted ? "Survey submitted!" : "Error submitting survey.",
                        Toast.LENGTH_SHORT).show();

                // Redirect to SubmittedSurveyActivity
                Intent intent = new Intent(AdditionalDataActivity.this, SubmittedSurveyActivity.class);
                startActivity(intent);
                finish(); // Optional: finish this activity
            }
        });
    }

    private boolean validateInputs() {
        // Validate inputs here (add your own validation logic if needed)
        if (courseInput.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please enter your course", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (yearInput.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please enter your year", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (collegeInput.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please enter your college name", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private String getInterests() {
        StringBuilder interests = new StringBuilder();
        if (checkBoxSports.isChecked()) interests.append("Sports ");
        if (checkBoxMusic.isChecked()) interests.append("Music ");
        if (checkBoxReading.isChecked()) interests.append("Reading ");
        return interests.toString().trim();
    }
}
