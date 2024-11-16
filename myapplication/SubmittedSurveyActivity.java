package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class SubmittedSurveyActivity extends AppCompatActivity {

    private ListView surveyListView;
    private DatabaseHelper databaseHelper;
    private ArrayList<Survey> surveyList;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submitted_survey);

        surveyListView = findViewById(R.id.surveyListView);
        databaseHelper = new DatabaseHelper(this);
        surveyList = new ArrayList<>();

        loadSurveys();

        surveyListView.setOnItemClickListener((parent, view, position, id) -> {
            Survey selectedSurvey = surveyList.get(position);
            int surveyId = selectedSurvey.getId();
            Intent intent = new Intent(SubmittedSurveyActivity.this, UpdateDeleteSurveyActivity.class);
            intent.putExtra("SURVEY_ID", surveyId);
            startActivity(intent);
        });

        // Button to view all survey data
        Button viewSurveyDataButton = findViewById(R.id.viewSurveyDataButton);
        viewSurveyDataButton.setOnClickListener(v -> showAllSurveyData());
    }

    private void loadSurveys() {
        Cursor cursor = databaseHelper.getAllSurveys();
        int count = cursor.getCount();
        Log.d("SubmittedSurveyActivity", "Number of surveys retrieved: " + count);

        if (count == 0) {
            Toast.makeText(this, "No surveys submitted", Toast.LENGTH_SHORT).show();
            return;
        }

        surveyList.clear();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0); // Assuming ID is in the first column
            String name = cursor.getString(1);
            String email = cursor.getString(2);
            surveyList.add(new Survey(id, name, email)); // Add survey objects
        }
        cursor.close();

        // Use custom adapter to populate the ListView
        ArrayAdapter<Survey> adapter = new ArrayAdapter<Survey>(this, android.R.layout.simple_list_item_2, surveyList) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_survey, parent, false);
                }

                Survey survey = getItem(position);

                TextView surveyTitle = convertView.findViewById(R.id.surveyTitle);
                TextView surveyDetails = convertView.findViewById(R.id.surveyDetails);

                surveyTitle.setText(survey.getName());
                surveyDetails.setText("Email: " + survey.getEmail());

                return convertView;
            }
        };
        surveyListView.setAdapter(adapter);
    }

    private void showAllSurveyData() {
        Cursor cursor = databaseHelper.getAllSurveys();
        StringBuilder surveyData = new StringBuilder();

        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No surveys submitted", Toast.LENGTH_SHORT).show();
            cursor.close();
            return;
        }

        while (cursor.moveToNext()) {
            surveyData.append("ID: ").append(cursor.getString(0)).append("\n")
                    .append("Name: ").append(cursor.getString(1)).append("\n")
                    .append("Email: ").append(cursor.getString(2)).append("\n")
                    .append("Gender: ").append(cursor.getString(3)).append("\n")
                    .append("Date of Birth: ").append(cursor.getString(4)).append("\n")
                    .append("Course: ").append(cursor.getString(5)).append("\n")
                    .append("Year: ").append(cursor.getString(6)).append("\n")
                    .append("College: ").append(cursor.getString(7)).append("\n")
                    .append("Review: ").append(cursor.getString(8)).append("\n\n");
        }
        cursor.close();

        new AlertDialog.Builder(this)
                .setTitle("Submitted Survey Data")
                .setMessage(surveyData.toString())
                .setPositiveButton("OK", null)
                .show();
    }

    // Survey model class
    private static class Survey {
        private final int id;
        private final String name;
        private final String email;

        public Survey(int id, String name, String email) {
            this.id = id;
            this.name = name;
            this.email = email;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getEmail() {
            return email;
        }
    }
}
