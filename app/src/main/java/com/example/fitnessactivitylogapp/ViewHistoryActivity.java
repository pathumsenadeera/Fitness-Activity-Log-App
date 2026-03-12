package com.example.fitnessactivitylogapp;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
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
        int[] to = {R.id.tvRowType, R.id.tvRowValue};

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
                R.layout.list_row_dark, cursor, from, to, 0);

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
        // 1. Create a Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // 2. Inflate our custom neon-themed layout
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_update_workout, null);
        builder.setView(dialogView);

        // Link the EditTexts from the custom layout
        final EditText etUpdateType = dialogView.findViewById(R.id.etUpdateType);
        final EditText etUpdateValue = dialogView.findViewById(R.id.etUpdateValue);

        // 3. Setup the Update Button
        builder.setPositiveButton("Update Now", (dialog, which) -> {
            String type = etUpdateType.getText().toString().trim();
            String value = etUpdateValue.getText().toString().trim();

            if (!type.isEmpty() && !value.isEmpty()) {
                // Update the database using the ID passed to this method
                boolean isUpdated = db.updateWorkout(id, type, value);

                if (isUpdated) {
                    Toast.makeText(this, "Workout Updated! 💪", Toast.LENGTH_SHORT).show();
                    loadUserHistory(); // Refresh the list to show new data
                } else {
                    Toast.makeText(this, "Update Failed!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Fields cannot be empty!", Toast.LENGTH_SHORT).show();
            }
        });

        // 4. Setup the Cancel Button
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        // 5. Create and Show the Dialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    // Logic to delete a workout record
    private void confirmDelete(String id) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Record")
                .setMessage("Are you sure you want to delete this log permanently?")
                .setPositiveButton("Yes, Delete", (dialog, which) -> {
                    if (db.deleteWorkout(id)) {
                        Toast.makeText(this, "Record Deleted", Toast.LENGTH_SHORT).show();
                        loadUserHistory();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
}