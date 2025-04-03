package com.example.androi_asm;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.androi_asm.DataBase.DatabaseManager;
import java.util.ArrayList;
import java.util.Calendar;

public class ReportsExpenseActivity extends AppCompatActivity {

    private EditText edtExpenseContent, edtDate;
    private Button btnSubmit, btnBackHome;
    private ListView listViewReports;
    private DatabaseManager databaseManager;
    private ArrayList<String> expenseList;
    private ArrayAdapter<String> adapter;
    private String selectedDate = "";
    private int selectedReportID = -1; // Lưu ID của mục đang chỉnh sửa

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports_expense);

        // Ánh xạ view từ XML
        edtExpenseContent = findViewById(R.id.edtExpenseContent);
        edtDate = findViewById(R.id.edtDate);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnBackHome = findViewById(R.id.btnBackHome);
        listViewReports = findViewById(R.id.listViewReports);

        databaseManager = new DatabaseManager(this);
        expenseList = new ArrayList<>();

        // Chọn ngày bằng DatePickerDialog
        edtDate.setOnClickListener(v -> showDatePicker());

        // Load danh sách báo cáo chi tiêu từ database
        loadExpenseReports();

        // Xử lý khi nhấn Submit (thêm mới hoặc cập nhật)
        btnSubmit.setOnClickListener(v -> {
            if (selectedReportID == -1) {
                addExpenseReport();
            } else {
                updateExpenseReport();
            }
        });

        // Quay lại màn hình chính
        btnBackHome.setOnClickListener(v -> finish());

        // Xử lý nhấn vào item trong ListView để chỉnh sửa
        listViewReports.setOnItemClickListener((parent, view, position, id) -> editExpenseReport(position));

        // Xử lý nhấn giữ để xóa
        listViewReports.setOnItemLongClickListener((parent, view, position, id) -> {
            showDeleteDialog(position);
            return true;
        });
    }

    // Hiển thị DatePickerDialog để chọn ngày
    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year1, month1, dayOfMonth) -> {
                    selectedDate = year1 + "-" + (month1 + 1) + "-" + dayOfMonth;
                    edtDate.setText(selectedDate);
                }, year, month, day);
        datePickerDialog.show();
    }

    // Thêm báo cáo chi tiêu vào database
    private void addExpenseReport() {
        String content = edtExpenseContent.getText().toString().trim();
        if (content.isEmpty() || selectedDate.isEmpty()) {
            Toast.makeText(this, "Please enter content and select a date!", Toast.LENGTH_SHORT).show();
            return;
        }

        long result = databaseManager.addExpenseReport(content, selectedDate);
        if (result != -1) {
            Toast.makeText(this, "Expense report added!", Toast.LENGTH_SHORT).show();
            resetFields();
            loadExpenseReports(); // Load lại danh sách sau khi thêm mới
        } else {
            Toast.makeText(this, "Failed to add expense report!", Toast.LENGTH_SHORT).show();
        }
    }

    // Chỉnh sửa báo cáo chi tiêu khi nhấn vào ListView
    private void editExpenseReport(int position) {
        Cursor cursor = databaseManager.getExpenseReports();
        if (cursor.moveToPosition(position)) {
            selectedReportID = cursor.getInt(0);
            String content = cursor.getString(1);
            String date = cursor.getString(2);

            // Hiển thị dữ liệu lên EditText
            edtExpenseContent.setText(content);
            edtDate.setText(date);
            selectedDate = date;

            // Đổi nút Submit thành Update
            btnSubmit.setText("Update");
        }
        cursor.close();
    }

    // Cập nhật báo cáo chi tiêu
    private void updateExpenseReport() {
        String content = edtExpenseContent.getText().toString().trim();
        if (content.isEmpty() || selectedDate.isEmpty()) {
            Toast.makeText(this, "Please enter content and select a date!", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean isUpdated = databaseManager.updateExpenseReport(selectedReportID, content, selectedDate);
        if (isUpdated) {
            Toast.makeText(this, "Expense report updated!", Toast.LENGTH_SHORT).show();
            resetFields();
            loadExpenseReports(); // Load lại danh sách
        } else {
            Toast.makeText(this, "Failed to update expense report!", Toast.LENGTH_SHORT).show();
        }
    }

    // Hiển thị Dialog để xóa báo cáo chi tiêu
    private void showDeleteDialog(int position) {
        Cursor cursor = databaseManager.getExpenseReports();
        if (cursor.moveToPosition(position)) {
            int reportID = cursor.getInt(0);

            new AlertDialog.Builder(this)
                    .setTitle("Delete Expense Report")
                    .setMessage("Are you sure you want to delete this report?")
                    .setPositiveButton("Delete", (dialog, which) -> {
                        deleteExpenseReport(reportID);
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        }
        cursor.close();
    }

    // Xóa báo cáo chi tiêu
    private void deleteExpenseReport(int reportID) {
        boolean isDeleted = databaseManager.deleteExpenseReport(reportID);
        if (isDeleted) {
            Toast.makeText(this, "Expense report deleted!", Toast.LENGTH_SHORT).show();
            loadExpenseReports(); // Load lại danh sách sau khi xóa
        } else {
            Toast.makeText(this, "Failed to delete expense report!", Toast.LENGTH_SHORT).show();
        }
    }

    // Load danh sách báo cáo chi tiêu từ database
    private void loadExpenseReports() {
        expenseList.clear();
        Cursor cursor = databaseManager.getExpenseReports();
        int index = 1;

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String content = cursor.getString(1);
                String date = cursor.getString(2);

                // Debugging logs
                Log.d("ExpenseReport", "ID: " + id + ", Content: " + content + ", Date: " + date);

                if (content == null) content = "No Description";

                String displayText = index + ". " + content + " - " + date;
                expenseList.add(displayText);
                index++;
            } while (cursor.moveToNext());
        }
        cursor.close();

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, expenseList);
        listViewReports.setAdapter(adapter);
    }

    // Reset các trường nhập liệu
    private void resetFields() {
        edtExpenseContent.setText("");
        edtDate.setText("");
        selectedDate = "";
        selectedReportID = -1;
        btnSubmit.setText("Submit");
    }
}
