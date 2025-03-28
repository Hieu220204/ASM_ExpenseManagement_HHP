package com.example.androi_asm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androi_asm.DataBase.DatabaseManager;

public class RegisterActivity extends AppCompatActivity {
    private EditText edtFullName, txtEmailRegister, txtPasswordRegister;
    private Button btnRegister, btnBackToLogin;
    private DatabaseManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        dbManager = new DatabaseManager(this);
        edtFullName = findViewById(R.id.edtFullName);
        txtEmailRegister = findViewById(R.id.txtEmailRegister);
        txtPasswordRegister = findViewById(R.id.txtPasswordRegister);
        btnRegister = findViewById(R.id.btnRegister);
        btnBackToLogin = findViewById(R.id.btnBackToLogin);

        btnRegister.setOnClickListener(view -> {
            String fullName = edtFullName.getText().toString().trim();
            String email = txtEmailRegister.getText().toString().trim();
            String password = txtPasswordRegister.getText().toString().trim();

            if (fullName.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields!", Toast.LENGTH_SHORT).show();
                return;
            }

            long result = dbManager.addUser(email, password, "user");
            if (result != -1) {
                Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            } else {
                Toast.makeText(this, "Registration failed! Email might already exist.", Toast.LENGTH_SHORT).show();
            }
        });

        btnBackToLogin.setOnClickListener(view -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            finish();
        });
    }
}
