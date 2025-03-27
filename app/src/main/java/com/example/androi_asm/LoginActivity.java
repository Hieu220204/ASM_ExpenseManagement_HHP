package com.example.androi_asm;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.androi_asm.DataBase.DatabaseManager;

public class LoginActivity extends AppCompatActivity {
    private EditText txtEmail, txtPassword;
    private Button btnLogin, btnRegister;
    private DatabaseManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dbManager = new DatabaseManager(this);
        txtEmail = findViewById(R.id.txtEmail);
        txtPassword = findViewById(R.id.txtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);

        btnLogin.setOnClickListener(view -> {
            String email = txtEmail.getText().toString().trim();
            String password = txtPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (dbManager.checkUser(email, password)) {
                Toast.makeText(this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                finish();
            } else {
                Toast.makeText(this, "Email hoặc mật khẩu sai!", Toast.LENGTH_SHORT).show();
            }
        });

        btnRegister.setOnClickListener(view -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });

    }
}