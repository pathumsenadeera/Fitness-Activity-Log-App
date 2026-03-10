package com.example.fitnessactivitylogapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AddWorkoutActivity extends AppCompatActivity {

    // UI Component declarations
    EditText etType, etValue;
    Button btnSave;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_workout_page);

        // Initialize DatabaseHelper
        db = new DatabaseHelper(this);

        // Link Java variables with XML IDs
        etType = findViewById(R.id.etWorkoutName);
        etValue = findViewById(R.id.etWorkoutValue);
        btnSave = findViewById(R.id.btnSaveWorkout);
    }
}