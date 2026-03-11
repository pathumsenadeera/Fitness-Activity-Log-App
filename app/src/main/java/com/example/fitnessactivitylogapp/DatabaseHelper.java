package com.example.fitnessactivitylogapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "FitnessTracker.db";

    public static final String TABLE_WORKOUTS = "workouts";
    public static final String COL_W_ID = "ID";
    public static final String COL_USER_EMAIL = "user_email";
    public static final String COL_TYPE = "workout_type";
    public static final String COL_VALUE = "duration";
    public static final String COL_DATE = "date";

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

    // Method to check if email and password match for login
    public boolean checkUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        // Query the users table for a matching email and password
        android.database.Cursor cursor = db.rawQuery("SELECT * FROM users WHERE email=? AND password=?", new String[]{email, password});

        boolean exists = cursor.getCount() > 0;
        cursor.close(); // Always close the cursor to avoid memory leaks
        return exists;
    }
    // Workout insert method
    public boolean addWorkout(String userEmail, String type, String value) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COL_USER_EMAIL, userEmail);
        contentValues.put(COL_TYPE, type);
        contentValues.put(COL_VALUE, value);

        // Auto generate current date
        String currentDate = new java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault()).format(new java.util.Date());
        contentValues.put(COL_DATE, currentDate);

        long result = db.insert(TABLE_WORKOUTS, null, contentValues);
        return result != -1;
    }


    // 2. Get User Workouts
    public android.database.Cursor getUserWorkouts(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        // SELECT ID AS _id, type, value, date FROM workouts WHERE user_email = ?
        return db.rawQuery("SELECT " + COL_W_ID + " AS _id, " + COL_TYPE + ", " + COL_VALUE + ", " + COL_DATE +
                " FROM " + TABLE_WORKOUTS + " WHERE " + COL_USER_EMAIL + " = ?", new String[]{email});
    }

    // 3. Delete Workout
    public boolean deleteWorkout(String id) {
        SQLiteDatabase db = this.getWritableDatabase();

        int result = db.delete(TABLE_WORKOUTS, COL_W_ID + " = ?", new String[]{id});
        return result > 0;
    }

    //update and delete workout

    public boolean updateWorkout(String id, String type, String value) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COL_TYPE, type);
        contentValues.put(COL_VALUE, value);

        // ID එකට අදාළ පේළිය පමණක් update කිරීම
        int result = db.update(TABLE_WORKOUTS, contentValues, COL_W_ID + " = ?", new String[]{id});

        return result > 0; // update එක සාර්ථක නම් true ලැබෙයි
    }



}

