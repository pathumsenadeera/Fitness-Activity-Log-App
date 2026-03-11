package com.example.fitnessactivitylogapp;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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

        lvWorkoutHistory = findViewById(R.id.lvWorkoutHistory);
        btnBack = findViewById(R.id.btnBack);
        db = new DatabaseHelper(this);

        btnBack.setOnClickListener(v -> finish());

        // Handle Edit/Delete on long item press
        lvWorkoutHistory.setOnItemLongClickListener((parent, view, position, id) -> {
            showActionDialog(String.valueOf(id));
            return true;
        });

        loadUserHistory();
    }

    // Load data into ListView using session email
    private void loadUserHistory() {
        SharedPreferences sp = getSharedPreferences("UserSession", MODE_PRIVATE);
        String email = sp.getString("user_email", "");
        Cursor cursor = db.getUserWorkouts(email);

        String[] from = {DatabaseHelper.COL_TYPE, DatabaseHelper.COL_VALUE};
        int[] to = {android.R.id.text1, android.R.id.text2};

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_2, cursor, from, to, 0);
        lvWorkoutHistory.setAdapter(adapter);
    }

    // Display dialog to choose between Edit and Delete
    private void showActionDialog(String workoutId) {
        String[] options = {"Edit Record", "Delete Record"};
        new AlertDialog.Builder(this)
                .setTitle("Manage Log")
                .setItems(options, (dialog, which) -> {
                    if (which == 0) showUpdateDialog(workoutId);
                    else confirmDelete(workoutId);
                }).show();
    }

    // Logic to update existing workout record
    private void showUpdateDialog(String id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Update Entry");

        final EditText inputType = new EditText(this);
        inputType.setHint("Workout Type");
        final EditText inputValue = new EditText(this);
        inputValue.setHint("Value");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 20, 50, 20);
        layout.addView(inputType);
        layout.addView(inputValue);
        builder.setView(layout);

        builder.setPositiveButton("Update", (dialog, which) -> {
            if (db.updateWorkout(id, inputType.getText().toString(), inputValue.getText().toString())) {
                Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show();
                loadUserHistory();
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    // Logic to delete a workout record
    private void confirmDelete(String id) {
        new AlertDialog.Builder(this)
                .setMessage("Delete this log permanently?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    if (db.deleteWorkout(id)) {
                        Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show();
                        loadUserHistory();
                    }
                })
                .setNegativeButton("No", null).show();
    }
}