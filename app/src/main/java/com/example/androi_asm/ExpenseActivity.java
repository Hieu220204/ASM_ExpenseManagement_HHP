package com.example.androi_asm;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import models.ExpenseItem;
import java.util.ArrayList;
import java.util.Calendar;

public class ExpenseActivity extends AppCompatActivity {
    EditText txtDateIncome, txtNoteExpense, txtAmountExpense;
    Button btnSubmitExpense, btnBack;
    ListView listViewExpenses;
    ArrayList<ExpenseItem> expenseList;
    ArrayAdapter expenseAdapter;
    int editIndex = -1;

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

        expenseList = new ArrayList<>();
        expenseAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, expenseList);
        listViewExpenses.setAdapter(expenseAdapter);

        txtDateIncome.setOnClickListener(v -> showDatePicker());

        btnSubmitExpense.setOnClickListener(v -> {
            String date = txtDateIncome.getText().toString();
            String note = txtNoteExpense.getText().toString();
            String amountStr = txtAmountExpense.getText().toString();

            if (date.isEmpty() || note.isEmpty() || amountStr.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            double amount = Double.parseDouble(amountStr);
            String category = detectCategory(note);

            if (editIndex == -1) {
                expenseList.add(new ExpenseItem(date, note, amount, category));
            } else {
                ExpenseItem item = expenseList.get(editIndex);
                item = new ExpenseItem(date, note, amount, category);
                expenseList.set(editIndex, item);
                editIndex = -1;
            }

            expenseAdapter.notifyDataSetChanged();
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
            expenseList.remove(position);
            expenseAdapter.notifyDataSetChanged();
            Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show();
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
}
