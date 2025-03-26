package com.example.androi_asm;

import android.content.Intent;
import android.os.Bundle;
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

        // Khởi tạo DatabaseManager
        dbManager = new DatabaseManager(this);

        // Ánh xạ view
        txtEmail = findViewById(R.id.txtEmail);
        txtPassword = findViewById(R.id.txtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);

        // Xử lý sự kiện nút đăng nhập
        btnLogin.setOnClickListener(view -> {
            String email = txtEmail.getText().toString().trim();
            String password = txtPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (dbManager.checkUser(email, password)) {
                Toast.makeText(LoginActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                finish();
            } else {
                Toast.makeText(LoginActivity.this, "Tên đăng nhập hoặc mật khẩu sai!", Toast.LENGTH_SHORT).show();
            }
        });

        // Xử lý sự kiện nút đăng ký
        btnRegister.setOnClickListener(view -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });
    }
}
