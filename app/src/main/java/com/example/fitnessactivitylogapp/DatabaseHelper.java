package com.example.fitnessactivitylogapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "FitnessTracker.db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE users(ID INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, email TEXT UNIQUE, password TEXT)");

        db.execSQL("CREATE TABLE workouts(ID INTEGER PRIMARY KEY AUTOINCREMENT, user_email TEXT, workout_type TEXT, duration TEXT, date TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS users");
        db.execSQL("DROP TABLE IF EXISTS workouts");
        onCreate(db);
    }

    // Method to insert new user data into database
    public boolean addUser(String name, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("email", email);
        contentValues.put("password", password);

        long result = db.insert("users", null, contentValues);
        // If result is -1, data insertion failed
        return result != -1;
    }
}