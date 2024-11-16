package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "SurveyDB.db";
    private static final int DATABASE_VERSION = 2; // Increment this version when schema changes
    private static final String TABLE_NAME = "SURVEY_TABLE";
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_NAME = "NAME";
    public static final String COLUMN_EMAIL = "EMAIL";
    public static final String COLUMN_GENDER = "GENDER";
    public static final String COLUMN_DOB = "DOB";
    public static final String COLUMN_INTERESTS = "INTERESTS";
    public static final String COLUMN_REVIEW = "REVIEW";
    public static final String COLUMN_COURSE = "COURSE";
    public static final String COLUMN_YEAR = "YEAR";
    public static final String COLUMN_COLLEGE = "COLLEGE";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create the table with all columns
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_EMAIL + " TEXT, " +
                COLUMN_GENDER + " TEXT, " +
                COLUMN_DOB + " TEXT, " +
                COLUMN_INTERESTS + " TEXT, " +
                COLUMN_COURSE + " TEXT, " +
                COLUMN_YEAR + " TEXT, " + // Ensure YEAR column is included
                COLUMN_COLLEGE + " TEXT, " +
                COLUMN_REVIEW + " TEXT);";
        db.execSQL(createTable);
        Log.d("DatabaseHelper", "Table created: " + TABLE_NAME);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop the existing table and recreate it to ensure the new schema
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db); // Recreate the table
    }

    public Cursor getAllSurveys() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        Log.d("DatabaseHelper", "getAllSurveys - retrieved rows: " + cursor.getCount());
        return cursor;
    }

    public boolean insertSurveyData(String name, String email, String gender, String dob, String interests, String course, String year, String college, String review) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME, name);
        contentValues.put(COLUMN_EMAIL, email);
        contentValues.put(COLUMN_GENDER, gender);
        contentValues.put(COLUMN_DOB, dob);
        contentValues.put(COLUMN_INTERESTS, interests);
        contentValues.put(COLUMN_COURSE, course);
        contentValues.put(COLUMN_YEAR, year);
        contentValues.put(COLUMN_COLLEGE, college);
        contentValues.put(COLUMN_REVIEW, review);

        long result = db.insert(TABLE_NAME, null, contentValues);
        Log.d("DatabaseHelper", "Insert result: " + result);
        return result != -1;
    }

    public Cursor getSurveyById(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " = ?", new String[]{id});
    }

    public boolean updateSurveyData(String id, String name, String email, String gender, String dob, String course, String year, String college, String interests, String review) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME, name);
        contentValues.put(COLUMN_EMAIL, email);
        contentValues.put(COLUMN_GENDER, gender);
        contentValues.put(COLUMN_DOB, dob);
        contentValues.put(COLUMN_COURSE, course);
        contentValues.put(COLUMN_YEAR, year); // Ensure YEAR is updated
        contentValues.put(COLUMN_COLLEGE, college); // Ensure COLLEGE is updated
        contentValues.put(COLUMN_INTERESTS, interests);
        contentValues.put(COLUMN_REVIEW, review);

        int rowsAffected = db.update(TABLE_NAME, contentValues, COLUMN_ID + " = ?", new String[]{id});
        return rowsAffected > 0;
    }

    public int deleteSurveyData(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[]{id});
    }

    public void closeCursor(Cursor cursor) {
        if (cursor != null) {
            cursor.close();
        }
    }
}
