package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class UpdateDeleteSurveyActivity extends AppCompatActivity {
    private DatabaseHelper databaseHelper;
    private EditText nameInput, emailInput, dobInput, courseInput, yearInput, collegeInput, reviewInput;
    private RadioGroup genderGroup;
    private int surveyId;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_delete_survey);

        // Initialize views
        nameInput = findViewById(R.id.nameInput);
        emailInput = findViewById(R.id.emailInput);
        dobInput = findViewById(R.id.dobInput);
        courseInput = findViewById(R.id.courseInput);
        yearInput = findViewById(R.id.yearInput);
        collegeInput = findViewById(R.id.collegeInput);
        reviewInput = findViewById(R.id.reviewInput);
        genderGroup = findViewById(R.id.genderGroup);

        Button updateButton = findViewById(R.id.updateButton);
        Button deleteButton = findViewById(R.id.deleteButton);
        // Removed the view data button since you are navigating from the list directly

        databaseHelper = new DatabaseHelper(this);

        // Get surveyId from Intent
        surveyId = getIntent().getIntExtra("SURVEY_ID", -1);

        // Load survey data
        loadSurveyData();

        updateButton.setOnClickListener(v -> updateSurvey());

        deleteButton.setOnClickListener(v -> confirmDelete());
    }

    @SuppressLint("Range")
    private void loadSurveyData() {
        Cursor cursor = databaseHelper.getSurveyById(String.valueOf(surveyId));
        if (cursor.moveToFirst()) {
            nameInput.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME)));
            emailInput.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_EMAIL)));

            String gender = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_GENDER));
            if ("Male".equals(gender)) {
                genderGroup.check(R.id.radioMale);
            } else if ("Female".equals(gender)) {
                genderGroup.check(R.id.radioFemale);
            } else {
                genderGroup.check(R.id.radioOther);
            }

            // Set course, year, and college fields
            courseInput.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_COURSE)));
            yearInput.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_YEAR)));
            collegeInput.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_COLLEGE)));
        }
        cursor.close();
    }

    private void updateSurvey() {
        String name = nameInput.getText().toString();
        String email = emailInput.getText().toString();
        String gender = getSelectedGender();
        String dob = dobInput.getText().toString(); // Ensure DOB is included
        String course = courseInput.getText().toString();
        String year = yearInput.getText().toString();
        String college = collegeInput.getText().toString();
        String review = reviewInput.getText().toString();

        boolean updated = databaseHelper.updateSurveyData(String.valueOf(surveyId), name, email, gender, dob, course, year, college, "", review);
        if (updated) {
            showConfirmationDialog(name, email, gender, dob, course, year, college, review);
        } else {
            Toast.makeText(this, "Failed to update survey.", Toast.LENGTH_SHORT).show();
        }
    }

    private void showConfirmationDialog(String name, String email, String gender, String dob, String course, String year, String college, String review) {
        new AlertDialog.Builder(this)
                .setTitle("Survey Updated")
                .setMessage("Survey data updated successfully!\n\n" +
                        "Name: " + name + "\n" +
                        "Email: " + email + "\n" +
                        "Gender: " + gender + "\n" +
                        "Date of Birth: " + dob + "\n" +
                        "Course: " + course + "\n" +
                        "Year: " + year + "\n" +
                        "College: " + college + "\n" +
                        "Review: " + review)
                .setPositiveButton("OK", null)
                .show();
    }

    private void confirmDelete() {
        new AlertDialog.Builder(this)
                .setTitle("Delete Survey")
                .setMessage("Are you sure you want to delete this survey?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int deletedRows = databaseHelper.deleteSurveyData(String.valueOf(surveyId));
                        if (deletedRows > 0) {
                            Toast.makeText(UpdateDeleteSurveyActivity.this, "Survey deleted successfully!", Toast.LENGTH_SHORT).show();
                            // Redirect or finish the activity
                            finish();
                        } else {
                            Toast.makeText(UpdateDeleteSurveyActivity.this, "Failed to delete survey.", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    private String getSelectedGender() {
        int selectedId = genderGroup.getCheckedRadioButtonId();
        if (selectedId == R.id.radioMale) {
            return "Male";
        } else if (selectedId == R.id.radioFemale) {
            return "Female";
        } else if (selectedId == R.id.radioOther) {
            return "Other";
        }
        return ""; // Default case
    }
}
