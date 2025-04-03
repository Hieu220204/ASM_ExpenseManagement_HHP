package com.example.androi_asm;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import models.ExpenseItem;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;

public class ExpenseActivity extends AppCompatActivity {
    EditText txtDateIncome, txtNoteExpense, txtAmountExpense;
    Button btnSubmitExpense, btnBack;
    ListView listViewExpenses;
    ArrayList<ExpenseItem> expenseList;
    ArrayAdapter<String> expenseAdapter;
    int editIndex = -1; // Biến lưu vị trí của mục được chọn để chỉnh sửa

    private static final String PREFS_NAME = "ExpensePrefs";
    private static final String EXPENSE_LIST_KEY = "ExpenseList";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);

        txtDateIncome = findViewById(R.id.txtDateIncome);
        txtNoteExpense = findViewById(R.id.txtNoteExpense);
        txtAmountExpense = findViewById(R.id.txtAmountExpense);
        btnSubmitExpense = findViewById(R.id.btnSubmitExpense);
        btnBack = findViewById(R.id.btnBack);
        listViewExpenses = findViewById(R.id.listViewExpenses);

        expenseList = loadExpenseList();
        updateExpenseAdapter();

        txtDateIncome.setOnClickListener(v -> showDatePicker());

        // Xử lý sự kiện khi nhấn nút Submit (hoặc Update nếu đang chỉnh sửa)
        btnSubmitExpense.setOnClickListener(v -> {
            String date = txtDateIncome.getText().toString();
            String note = txtNoteExpense.getText().toString();
            String amountStr = txtAmountExpense.getText().toString();

            if (date.isEmpty() || note.isEmpty() || amountStr.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            double amount = Double.parseDouble(amountStr);
            String category = detectCategory(note);
            ExpenseItem newItem = new ExpenseItem(date, note, amount, category);

            if (editIndex == -1) {
                // Thêm mới mục chi tiêu
                expenseList.add(newItem);
            } else {
                // Cập nhật mục chi tiêu đã chọn
                expenseList.set(editIndex, newItem);
                editIndex = -1; // Reset chỉ số chỉnh sửa sau khi cập nhật
            }

            saveExpenseList();
            updateExpenseAdapter();
            clearInputs();
            btnSubmitExpense.setText("Submit"); // Đặt lại tên nút về "Submit"
        });

        // Xử lý sự kiện khi chọn một mục trong ListView để chỉnh sửa
        listViewExpenses.setOnItemClickListener((parent, view, position, id) -> {
            ExpenseItem item = expenseList.get(position);
            txtDateIncome.setText(item.getDate());
            txtNoteExpense.setText(item.getNote());
            txtAmountExpense.setText(String.valueOf(item.getAmount()));
            editIndex = position; // Lưu vị trí của mục được chọn

            // Đổi tên nút Submit thành Update
            btnSubmitExpense.setText("Update");
        });

        // Xử lý sự kiện khi long click (nhấn lâu) vào một mục để xóa
        listViewExpenses.setOnItemLongClickListener((parent, view, position, id) -> {
            new AlertDialog.Builder(this)
                    .setTitle("Delete Expense")
                    .setMessage("Are you sure you want to delete this expense?")
                    .setPositiveButton("Delete", (dialog, which) -> {
                        expenseList.remove(position);
                        saveExpenseList();
                        updateExpenseAdapter();
                        Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                    .show();
            return true;
        });

        btnBack.setOnClickListener(v -> finish());
    }

    // Hiển thị DatePicker để chọn ngày
    private void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year1, month1, dayOfMonth) -> txtDateIncome.setText(dayOfMonth + "/" + (month1 + 1) + "/" + year1),
                year, month, day);
        datePickerDialog.show();
    }

    // Làm sạch các trường nhập liệu
    private void clearInputs() {
        txtDateIncome.setText("");
        txtNoteExpense.setText("");
        txtAmountExpense.setText("");
    }

    // Xác định danh mục của chi tiêu dựa trên ghi chú
    private String detectCategory(String note) {
        note = note.toLowerCase();
        if (note.contains("food")) return "Food";
        if (note.contains("transport")) return "Transport";
        if (note.contains("shop")) return "Shopping";
        if (note.contains("bill")) return "Bills";
        if (note.contains("entertainment")) return "Entertainment";
        if (note.contains("health")) return "Health";
        if (note.contains("education")) return "Education";
        if (note.contains("travel")) return "Travel";
        if (note.contains("gift")) return "Gift";
        return "Other";
    }

    // Lưu danh sách chi tiêu vào SharedPreferences
    private void saveExpenseList() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(expenseList);
        editor.putString(EXPENSE_LIST_KEY, json);
        editor.apply();
    }

    // Tải danh sách chi tiêu từ SharedPreferences
    private ArrayList<ExpenseItem> loadExpenseList() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(EXPENSE_LIST_KEY, null);
        Type type = new TypeToken<ArrayList<ExpenseItem>>() {}.getType();
        return json == null ? new ArrayList<>() : gson.fromJson(json, type);
    }

    // Cập nhật Adapter của ListView
    private void updateExpenseAdapter() {
        ArrayList<String> formattedExpenses = new ArrayList<>();
        for (ExpenseItem item : expenseList) {
            formattedExpenses.add(item.getDate() + " - " + item.getNote() + " - $" + item.getAmount() + " (" + item.getCategory() + ")");
        }
        expenseAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, formattedExpenses);
        listViewExpenses.setAdapter(expenseAdapter);
    }
}
