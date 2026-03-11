package com.example.fitnessactivitylogapp;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
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


        lvWorkoutHistory.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // Here, 'id' is the primary key (ID) from the database because we used "AS _id" in the query
                String workoutId = String.valueOf(id);

                new AlertDialog.Builder(ViewHistoryActivity.this)
                        .setTitle("Delete Record")
                        .setMessage("Are you sure you want to delete this workout?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                boolean isDeleted = db.deleteWorkout(workoutId);
                                if(isDeleted) {
                                    Toast.makeText(ViewHistoryActivity.this, "Workout Deleted!", Toast.LENGTH_SHORT).show();
                                    // Refresh the list
                                    loadData(userEmail);
                                }
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
                return true;
            }
        });




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