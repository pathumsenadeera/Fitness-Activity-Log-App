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
    // Inside onCreate, after Part 1 code
        btnSave.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String type = etType.getText().toString().trim();
            String value = etValue.getText().toString().trim();

            // Get current logged-in user's email from SharedPreferences
            SharedPreferences sp = getSharedPreferences("UserSession", MODE_PRIVATE);
            String userEmail = sp.getString("user_email", "");

            // Basic validation: Check if fields are empty
            if (type.isEmpty() || value.isEmpty()) {
                Toast.makeText(AddWorkoutActivity.this, "Please enter all details", Toast.LENGTH_SHORT).show();
            } else {
                // Call the addWorkout method from DatabaseHelper
                boolean isInserted = db.addWorkout(userEmail, type, value);

                if (isInserted) {
                    Toast.makeText(AddWorkoutActivity.this, "Workout Saved Successfully!", Toast.LENGTH_SHORT).show();
                    finish(); // Close activity and go back to Dashboard
                } else {
                    Toast.makeText(AddWorkoutActivity.this, "Failed to save workout. Try again.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    });
}