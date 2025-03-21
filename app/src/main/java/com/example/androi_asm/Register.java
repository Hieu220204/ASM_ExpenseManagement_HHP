package com.example.androi_asm;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class Register extends AppCompatActivity {

    private EditText edtFullName, txtEmailRegister, txtPasswordRegister;
    private Button btnRegister, btnBackToLogin;
    private TextView txtRegisterSuccess;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Ánh xạ các thành phần giao diện
        edtFullName = findViewById(R.id.edtFullName);
        txtEmailRegister = findViewById(R.id.txtEmailRegister);
        txtPasswordRegister = findViewById(R.id.txtPasswordRegister);
        btnRegister = findViewById(R.id.btnRegister);
        btnBackToLogin = findViewById(R.id.btnBackToLogin);
        txtRegisterSuccess = findViewById(R.id.txtRegisterSuccess);

        // Xử lý sự kiện khi bấm nút Register
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullName = edtFullName.getText().toString().trim();
                String email = txtEmailRegister.getText().toString().trim();
                String password = txtPasswordRegister.getText().toString().trim();

                if (fullName.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(Register.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(Register.this, "Invalid email format", Toast.LENGTH_SHORT).show();
                } else {
                    // Hiển thị thông báo thành công ở đáy trang
                    txtRegisterSuccess.setText("Registration successful!");
                    txtRegisterSuccess.setVisibility(View.VISIBLE);
                }
            }
        });

        // Quay về LoginActivity
        btnBackToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register.this, Login.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
