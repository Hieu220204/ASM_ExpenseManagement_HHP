package com.example.androi_asm;

import android.annotation.SuppressLint;
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

        // Enable edge-to-edge display
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        // Adjust padding for system bars (status bar, navigation bar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Check and request notification permission for devices running Android 13 or above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 100);
            }
        }

        // Map buttons to their respective UI components
        Button btnExpense = findViewById(R.id.btnExpense);
        Button btnBudgetSetting = findViewById(R.id.btnBudgetSetting);
        Button btnRecurringExpenses = findViewById(R.id.btnRecurringExpenses);
        Button btnReport = findViewById(R.id.btnReport);
        Button btnExpenseNotifications = findViewById(R.id.btnExpenseNotifications);
        Button btnLogout = findViewById(R.id.btnLogout);

        // Set click listeners to navigate to corresponding activities
        btnExpense.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, ExpenseActivity.class)));
        btnBudgetSetting.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, BudgetSettingActivity.class)));
        btnRecurringExpenses.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, RecurringExpensesActivity.class)));
        btnReport.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, ReportsExpenseActivity.class)));
        btnExpenseNotifications.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, ExpenseNotificationActivity.class)));

        // Handle logout by navigating back to LoginActivity and finishing the current activity
        btnLogout.setOnClickListener(v -> {
            startActivity(new Intent(HomeActivity.this, LoginActivity.class));
            finish(); // Close HomeActivity after logout
        });
    }
}
