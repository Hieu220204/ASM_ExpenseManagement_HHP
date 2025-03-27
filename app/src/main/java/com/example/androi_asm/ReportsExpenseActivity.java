package com.example.androi_asm;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports_expense);

        // Ánh xạ view
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

        // Xử lý khi nhấn Submit
        btnSubmit.setOnClickListener(v -> addExpenseReport());

        // Quay lại màn hình chính
        btnBackHome.setOnClickListener(v -> finish());
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
            loadExpenseReports(); // Load lại danh sách sau khi thêm mới
        } else {
            Toast.makeText(this, "Failed to add expense report!", Toast.LENGTH_SHORT).show();
        }
    }

    // Load danh sách báo cáo chi tiêu từ database
    private void loadExpenseReports() {
        expenseList.clear();
        Cursor cursor = databaseManager.getExpenseReports();

        if (cursor.moveToFirst()) {
            do {
                String content = cursor.getString(1);
                String date = cursor.getString(2);
                expenseList.add(content + " - " + date);
            } while (cursor.moveToNext());
        }
        cursor.close();

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, expenseList);
        listViewReports.setAdapter(adapter);
    }
}