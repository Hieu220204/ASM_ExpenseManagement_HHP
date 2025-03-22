package com.example.androi_asm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androi_asm.Login;
import com.example.androi_asm.R;

<<<<<<< HEAD:app/src/main/java/com/example/androi_asm/Home.java
public class Home extends AppCompatActivity {
    private Button btnLogout;
=======
public class UserManagementActivity extends AppCompatActivity {
>>>>>>> restore-lost-code:app/src/main/java/com/example/androi_asm/UserManagementActivity.java

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
<<<<<<< HEAD:app/src/main/java/com/example/androi_asm/Home.java
        setContentView(R.layout.activity_home);

        btnLogout = findViewById(R.id.btnLogout);

        btnLogout.setOnClickListener(view -> {
            startActivity(new Intent(Home.this, Login.class));
            finish();
=======
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_management);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
>>>>>>> restore-lost-code:app/src/main/java/com/example/androi_asm/UserManagementActivity.java
        });
    }
}
