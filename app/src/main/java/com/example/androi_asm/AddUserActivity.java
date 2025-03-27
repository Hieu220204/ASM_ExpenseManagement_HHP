package com.example.androi_asm;

import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import com.example.androi_asm.DataBase.DatabaseManager;

public class AddUserActivity extends Activity {

    private EditText edtUsername, edtPassword, edtEmail;
    private Spinner spnRole;
    private Button btnSave, btnCancel;
    private DatabaseManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        dbManager = new DatabaseManager(this);

        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        edtEmail = findViewById(R.id.edtEmail);
        spnRole = findViewById(R.id.spnRole);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);

        btnSave.setOnClickListener(v -> addUser());
        btnCancel.setOnClickListener(v -> finish());
    }

    private void addUser() {
        String username = edtUsername.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String role = spnRole.getSelectedItem().toString();

        if (username.isEmpty() || password.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        long result = dbManager.addUser(password, email, role);
        if (result > 0) {
            Toast.makeText(this, "User added successfully", Toast.LENGTH_SHORT).show();
            finish();
        } else if (result == -1) {
            Toast.makeText(this, "Email already exists", Toast.LENGTH_SHORT).show();
        } else if (result == -2) {
            Toast.makeText(this, "Username already exists", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to add user", Toast.LENGTH_SHORT).show();
        }
    }
}