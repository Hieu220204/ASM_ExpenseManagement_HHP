package com.example.androi_asm;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import models.ExpenseItem;
import java.util.ArrayList;
import java.util.Calendar;

public class ExpenseActivity extends AppCompatActivity {
    // UI components declaration
    EditText txtDateIncome, txtNoteExpense, txtAmountExpense;
    Button btnSubmitExpense, btnBack;
    ListView listViewExpenses;
    ArrayList<ExpenseItem> expenseList; // List to store expense items
    ArrayAdapter expenseAdapter;        // Adapter to display expenses in the ListView
    int editIndex = -1;                // Index of the item being edited; -1 means adding a new item

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense); // Set the layout for the activity

        // Map UI components from XML layout
        txtDateIncome = findViewById(R.id.txtDateIncome);
        txtNoteExpense = findViewById(R.id.txtNoteExpense);
        txtAmountExpense = findViewById(R.id.txtAmountExpense);
        btnSubmitExpense = findViewById(R.id.btnSubmitExpense);
        btnBack = findViewById(R.id.btnBack);
        listViewExpenses = findViewById(R.id.listViewExpenses);

        // Initialize expense list and adapter
        expenseList = new ArrayList<>();
        expenseAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, expenseList);
        listViewExpenses.setAdapter(expenseAdapter);

        // Show date picker dialog when clicking on the date field
        txtDateIncome.setOnClickListener(v -> showDatePicker());

        // Handle the submit button click
        btnSubmitExpense.setOnClickListener(v -> {
            String date = txtDateIncome.getText().toString();
            String note = txtNoteExpense.getText().toString();
            String amountStr = txtAmountExpense.getText().toString();

            // Check for empty fields
            if (date.isEmpty() || note.isEmpty() || amountStr.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            double amount = Double.parseDouble(amountStr);
            String category = detectCategory(note); // Automatically detect category based on the note

            // Add new expense or update existing expense
            if (editIndex == -1) {
                expenseList.add(new ExpenseItem(date, note, amount, category));
            } else {
                ExpenseItem item = new ExpenseItem(date, note, amount, category);
                expenseList.set(editIndex, item);
                editIndex = -1; // Reset to add new mode
            }

            expenseAdapter.notifyDataSetChanged(); // Refresh ListView
            clearInputs(); // Clear input fields
        });

        // Handle clicking on an item to edit
        listViewExpenses.setOnItemClickListener((parent, view, position, id) -> {
            ExpenseItem item = expenseList.get(position);
            txtDateIncome.setText(item.getDate());
            txtNoteExpense.setText(item.getNote());
            txtAmountExpense.setText(String.valueOf(item.getAmount()));
            editIndex = position; // Store index of the item being edited
        });

        // Handle long-click on an item to delete
        listViewExpenses.setOnItemLongClickListener((parent, view, position, id) -> {
            expenseList.remove(position);
            expenseAdapter.notifyDataSetChanged();
            Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show();
            return true;
        });

        // Back button to return to the previous screen
        btnBack.setOnClickListener(v -> finish());
    }

    // Display a date picker dialog
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

    // Clear all input fields after submission
    private void clearInputs() {
        txtDateIncome.setText("");
        txtNoteExpense.setText("");
        txtAmountExpense.setText("");
    }

    // Automatically detect the category based on keywords in the note
    private String detectCategory(String note) {
        note = note.toLowerCase();
        if (note.contains("food")) return "Food";
        if (note.contains("transport")) return "Transport";
        if (note.contains("shop")) return "Shopping";
        if (note.contains("bill")) return "Bills";
        return "Other";
    }

}
