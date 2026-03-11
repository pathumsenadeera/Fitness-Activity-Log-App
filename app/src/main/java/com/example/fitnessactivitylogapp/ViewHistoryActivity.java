package com.example.fitnessactivitylogapp;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import androidx.appcompat.app.AppCompatActivity;

public class ViewHistoryActivity extends AppCompatActivity {
    ListView lvWorkoutHistory;
    DatabaseHelper db;
    Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_history_page);

        // UI components initialization
        lvWorkoutHistory = findViewById(R.id.lvWorkoutHistory);
        btnBack = findViewById(R.id.btnBack);
        db = new DatabaseHelper(this);

        // Close activity when Back button is clicked
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        String userEmail = sharedPreferences.getString("user_email", "");
        loadData(userEmail);


    }

    private void loadData(String email) {
        Cursor cursor = db.getUserWorkouts(email);

        // Database columns to UI views mapping
        String[] from = new String[]{DatabaseHelper.COL_TYPE, DatabaseHelper.COL_VALUE};
        int[] to = new int[]{android.R.id.text1, android.R.id.text2};

        // simple_list_item_2 provides two text lines for each list item
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_2, cursor, from, to, 0);

        lvWorkoutHistory.setAdapter(adapter);
    }
}