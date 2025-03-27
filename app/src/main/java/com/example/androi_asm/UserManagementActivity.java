package com.example.androi_asm;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androi_asm.DataBase.DatabaseManager;

import java.util.ArrayList;

public class UserManagementActivity extends AppCompatActivity {
    private ListView userListView;
    private Button btnAdd, btnEdit, btnDelete, btnBackToHome;
    private DatabaseManager dbManager;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> userList;
    private ArrayList<Integer> userIds;
    private int selectedUserId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_management);

        dbManager = new DatabaseManager(this);
        userListView = findViewById(R.id.userListView);
        btnAdd = findViewById(R.id.btnAdd);
        btnEdit = findViewById(R.id.btnEdit);
        btnDelete = findViewById(R.id.btnDelete);
        btnBackToHome = findViewById(R.id.btnBackToHome);

        loadUserList();

        userListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedUserId = userIds.get(position);
                Toast.makeText(UserManagementActivity.this, "Selected User ID: " + selectedUserId, Toast.LENGTH_SHORT).show();
            }
        });

        btnAdd.setOnClickListener(view -> {
            Intent intent = new Intent(UserManagementActivity.this, AddUserActivity.class);
            startActivity(intent);
        });

        btnEdit.setOnClickListener(view -> {
            if (selectedUserId != -1) {
                Intent intent = new Intent(UserManagementActivity.this, EditUserActivity.class);
                intent.putExtra("userId", selectedUserId);
                startActivity(intent);
            } else {
                Toast.makeText(UserManagementActivity.this, "Please select a user to edit", Toast.LENGTH_SHORT).show();
            }
        });

        btnDelete.setOnClickListener(view -> {
            if (selectedUserId != -1) {
                confirmDeleteUser(selectedUserId);
            } else {
                Toast.makeText(UserManagementActivity.this, "Please select a user to delete", Toast.LENGTH_SHORT).show();
            }
        });

        btnBackToHome.setOnClickListener(view -> finish());
    }

    private void loadUserList() {
        Cursor cursor = dbManager.getAllUsers();
        userList = new ArrayList<>();
        userIds = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex("userID"));
                String username = cursor.getString(cursor.getColumnIndex("username"));
                String role = cursor.getString(cursor.getColumnIndex("role"));
                userList.add(username + " - " + role);
                userIds.add(id);
            } while (cursor.moveToNext());
        }
        cursor.close();

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, userList);
        userListView.setAdapter(adapter);
    }

    private void confirmDeleteUser(int userId) {
        new AlertDialog.Builder(this)
                .setTitle("Delete User")
                .setMessage("Are you sure you want to delete this user?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dbManager.deleteUser(userId);
                        loadUserList();
                        Toast.makeText(UserManagementActivity.this, "User deleted successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadUserList();
    }
}
