package com.example.fitnessactivitylogapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    // UI Component declarations
    Button btnAddWorkout, btnViewHistory, btnLogout;
    TextView tvWelcomeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_main);

        // Initialize Buttons
        btnAddWorkout = findViewById(R.id.btnAddWorkout);
        btnViewHistory = findViewById(R.id.btnViewHistory);
        btnLogout = findViewById(R.id.btnLogout);
        tvWelcomeName = findViewById(R.id.tvWelcomeName);

        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        String userEmail = sharedPreferences.getString("user_email", "");


        if (userEmail.contains("@")) {
            String name = userEmail.split("@")[0];
            name = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();

            // කහ ඉර අයින් වෙන්න String.format පාවිච්චි කළා
            String welcomeMessage = String.format("Hello %s \uD83D\uDCAA", name);
            tvWelcomeName.setText(welcomeMessage);
        }

        // Navigate to Add Workout Screen
        btnAddWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddWorkoutActivity.class);
                startActivity(intent);
            }
        });

        // Navigate to View History Screen
        btnViewHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ViewHistoryActivity.class);
                startActivity(intent);
            }
        });
        // Logout Button Logic
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Access SharedPreferences and clear user session
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();

                Toast.makeText(MainActivity.this, "Successfully Logged Out!", Toast.LENGTH_SHORT).show();

                // Redirect user back to Login screen
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish(); // Close MainActivity
            }
        });

    }
}