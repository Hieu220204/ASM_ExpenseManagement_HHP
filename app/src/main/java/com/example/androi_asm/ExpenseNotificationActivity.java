package com.example.androi_asm;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androi_asm.DataBase.DatabaseManager;

import java.util.ArrayList;

public class ExpenseNotificationActivity extends AppCompatActivity {
    ListView lvNotifications;
    DatabaseManager databaseManager;
    Button btnBackToHome;
    ArrayList<String> notificationList;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_notifications);

        lvNotifications = findViewById(R.id.lvNotifications);
        btnBackToHome = findViewById(R.id.btnBackToHome);
        databaseManager = new DatabaseManager(this);

        notificationList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, notificationList);
        lvNotifications.setAdapter(adapter);

        loadNotifications(); // Load dữ liệu từ database

        btnBackToHome.setOnClickListener(view -> {
            Intent intent = new Intent(ExpenseNotificationActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        });

    }

    @SuppressLint("Range")
    private void loadNotifications() {
        Cursor cursor = databaseManager.getAllNotifications();
        if (cursor != null) {
            notificationList.clear();
            while (cursor.moveToNext()) {
                String message = cursor.getString(cursor.getColumnIndex("message"));
                notificationList.add(message);
            }
            adapter.notifyDataSetChanged();
            cursor.close();
        }
    }

}
