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
    // UI components
    EditText txtDateIncome, txtNoteExpense, txtAmountExpense;
    Button btnSubmitExpense, btnBack;
    ListView listViewExpenses;
    ArrayList<ExpenseItem> expenseList;
    ArrayAdapter<String> expenseAdapter;
    int editIndex = -1;

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

        // Load expenses from SharedPreferences
        expenseList = loadExpenseList();
        updateExpenseAdapter();

        txtDateIncome.setOnClickListener(v -> showDatePicker());

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
                expenseList.add(newItem);
            } else {
                expenseList.set(editIndex, newItem);
                editIndex = -1;
            }

            saveExpenseList();
            updateExpenseAdapter();
            clearInputs();
        });

        listViewExpenses.setOnItemClickListener((parent, view, position, id) -> {
            ExpenseItem item = expenseList.get(position);
            txtDateIncome.setText(item.getDate());
            txtNoteExpense.setText(item.getNote());
            txtAmountExpense.setText(String.valueOf(item.getAmount()));
            editIndex = position;
        });

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

    private void clearInputs() {
        txtDateIncome.setText("");
        txtNoteExpense.setText("");
        txtAmountExpense.setText("");
    }

    private String detectCategory(String note) {
        note = note.toLowerCase();
        if (note.contains("food")) return "Food";
        if (note.contains("transport")) return "Transport";
        if (note.contains("shop")) return "Shopping";
        if (note.contains("bill")) return "Bills";
        return "Other";
    }

    private void saveExpenseList() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(expenseList);
        editor.putString(EXPENSE_LIST_KEY, json);
        editor.apply();
    }

    private ArrayList<ExpenseItem> loadExpenseList() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(EXPENSE_LIST_KEY, null);
        Type type = new TypeToken<ArrayList<ExpenseItem>>() {}.getType();
        return json == null ? new ArrayList<>() : gson.fromJson(json, type);
    }

    private void updateExpenseAdapter() {
        ArrayList<String> formattedExpenses = new ArrayList<>();
        for (ExpenseItem item : expenseList) {
            formattedExpenses.add(item.getDate() + " - " + item.getNote() + " - $" + item.getAmount());
        }
        expenseAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, formattedExpenses);
        listViewExpenses.setAdapter(expenseAdapter);
    }
}
