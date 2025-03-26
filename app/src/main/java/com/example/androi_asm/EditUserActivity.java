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

public class EditUserActivity extends Activity {

    private EditText edtUsername;
    private Spinner spnRole;
    private Button btnUpdate, btnCancel;
    private DatabaseManager dbManager;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        dbManager = new DatabaseManager(this);

        edtUsername = findViewById(R.id.edtUsername);
        spnRole = findViewById(R.id.spnRole);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnCancel = findViewById(R.id.btnCancel);

        userId = getIntent().getIntExtra("USER_ID", -1);
        String username = getIntent().getStringExtra("USERNAME");
        String role = getIntent().getStringExtra("ROLE");

        if (userId == -1) {
            Toast.makeText(this, "Invalid user ID", Toast.LENGTH_SHORT).show();
            finish();
        }

        edtUsername.setText(username);

        btnUpdate.setOnClickListener(v -> updateUser());
        btnCancel.setOnClickListener(v -> finish());
    }

    private void updateUser() {
        String username = edtUsername.getText().toString().trim();
        String role = spnRole.getSelectedItem().toString();

        if (username.isEmpty()) {
            Toast.makeText(this, "Please enter username", Toast.LENGTH_SHORT).show();
            return;
        }

        int result = dbManager.updateUser(userId, username, role);
        if (result > 0) {
            Toast.makeText(this, "User updated successfully", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Failed to update user", Toast.LENGTH_SHORT).show();
        }
    }
}
