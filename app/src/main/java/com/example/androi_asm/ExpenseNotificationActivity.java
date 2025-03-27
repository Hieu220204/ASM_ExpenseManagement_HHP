package com.example.androi_asm;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androi_asm.DataBase.DatabaseManager;

import java.util.ArrayList;

public class ExpenseNotificationActivity extends AppCompatActivity {
    ListView lvNotifications;
    DatabaseManager databaseManager;
    Button btnBackToHome; // Thêm biến cho nút Back to Home

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_notifications);

        lvNotifications = findViewById(R.id.lvNotifications);
        btnBackToHome = findViewById(R.id.btnBackToHome); // Ánh xạ nút Back to Home
        databaseManager = new DatabaseManager(this);

        loadNotifications();

        // Xử lý sự kiện khi nhấn nút Back to Home
        btnBackToHome.setOnClickListener(view -> {
            Intent intent = new Intent(ExpenseNotificationActivity.this, HomeActivity.class);
            startActivity(intent);
            finish(); // Đóng Activity hiện tại
        });
    }

    private void loadNotifications() {
        Cursor cursor = databaseManager.getExpenseReports();
        ArrayList<String> notifications = new ArrayList<>();

        while (cursor.moveToNext()) {
            String message = cursor.getString(cursor.getColumnIndexOrThrow("message"));
            String date = cursor.getString(cursor.getColumnIndexOrThrow("dateSent"));
            notifications.add(date + ": " + message);
        }
        cursor.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, notifications);
        lvNotifications.setAdapter(adapter);
    }
}