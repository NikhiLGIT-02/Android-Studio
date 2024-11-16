package com.example.myapplication;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class SurveyDataActivity extends AppCompatActivity {
    private DatabaseHelper databaseHelper;
    private TextView surveyDataTextView;
    private int surveyId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_data);

        surveyDataTextView = findViewById(R.id.surveyDataTextView);
        databaseHelper = new DatabaseHelper(this);
        surveyId = getIntent().getIntExtra("SURVEY_ID", -1);

        displaySurveyData();
    }

    private void displaySurveyData() {
        Cursor cursor = databaseHelper.getSurveyById(String.valueOf(surveyId));
        if (cursor.moveToFirst()) {
            @SuppressLint("Range") String data = "Name: " + cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME)) + "\n" +
                    "Email: " + cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_EMAIL)) + "\n" +
                    "Gender: " + cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_GENDER)) + "\n" +
                    "Date of Birth: " + cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_DOB)) + "\n" +
                    "Course: " + cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_COURSE)) + "\n" +
                    "Year: " + cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_YEAR)) + "\n" +
                    "College: " + cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_COLLEGE)) + "\n" +
                    "Review: " + cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_REVIEW));
            surveyDataTextView.setText(data);
        } else {
            surveyDataTextView.setText("No data found.");
        }
        cursor.close();
    }
}
