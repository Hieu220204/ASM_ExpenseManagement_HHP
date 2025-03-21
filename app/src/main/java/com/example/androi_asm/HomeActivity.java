package com.example.androi_asm;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class HomeActivity extends AppCompatActivity {

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Check notification permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 100);
            }
        }

        // Ánh xạ và điều hướng các nút
        Button btnExpense = findViewById(R.id.btnExpense);
        Button btnBudgetSetting = findViewById(R.id.btnBudgetSetting);
        Button btnRecurringExpenses = findViewById(R.id.btnRecurringExpenses);
        Button btnReport = findViewById(R.id.btnReport);
        Button btnExpenseNotifications = findViewById(R.id.btnExpenseNotifications);
        Button btnLogout = findViewById(R.id.btnLogout);

        btnExpense.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, ExpenseActivity.class)));
        btnBudgetSetting.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, BudgetSettingActivity.class)));
        btnRecurringExpenses.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, RecurringExpensesActivity.class)));
        btnReport.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, ReportsExpenseActivity.class)));
        btnExpenseNotifications.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, ExpenseNotificationActivity.class)));
        btnLogout.setOnClickListener(v -> {
            startActivity(new Intent(HomeActivity.this, LoginActivity.class));
            finish();
        });
    }



}
