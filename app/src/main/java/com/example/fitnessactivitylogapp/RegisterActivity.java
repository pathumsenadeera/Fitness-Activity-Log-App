package com.example.fitnessactivitylogapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    // UI Component declarations
    EditText etFullName, etEmail, etPassword;
    Button btnRegister;
    TextView tvLoginRedirect;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_page);

        // Initialize DatabaseHelper
        db = new DatabaseHelper(this);

        // Link Java variables with UI elements from XML
        etFullName = findViewById(R.id.etFullName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnRegister = findViewById(R.id.btnRegister);
        tvLoginRedirect = findViewById(R.id.tvLoginRedirect);

        // Redirect user to Login screen if they already have an account
        tvLoginRedirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}