package com.example.androi_asm;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

public class RecurringExpensesActivity extends AppCompatActivity {
    private EditText edtExpenseContent, edtAmount, edtStartDate, edtEndDate, spinnerFrequency;
    private Button btnAddExpense, btnEditExpense, btnBackHome;
    private ListView listViewExpenses;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> expenseList;
    private int selectedExpenseIndex = -1;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recurring_expenses);

        sharedPreferences = getSharedPreferences("ExpensesPrefs", MODE_PRIVATE);
        edtExpenseContent = findViewById(R.id.edtExpenseContent);
        spinnerFrequency = findViewById(R.id.spinnerFrequency);
        edtAmount = findViewById(R.id.edtAmount);
        edtStartDate = findViewById(R.id.edtStartDate);
        edtEndDate = findViewById(R.id.edtEndDate);
        btnAddExpense = findViewById(R.id.btnAddExpense);
        btnEditExpense = findViewById(R.id.btnEditExpense);
        btnBackHome = findViewById(R.id.btnBackHome);
        listViewExpenses = findViewById(R.id.listViewExpenses);

        expenseList = new ArrayList<>(loadExpenses());
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, expenseList);
        listViewExpenses.setAdapter(adapter);

        edtStartDate.setOnClickListener(v -> showDatePickerDialog(edtStartDate));
        edtEndDate.setOnClickListener(v -> showDatePickerDialog(edtEndDate));

        btnAddExpense.setOnClickListener(v -> addExpense());
        btnEditExpense.setOnClickListener(v -> editExpense());
        btnBackHome.setOnClickListener(v -> {
            saveExpenses();
            finish();
        });

        listViewExpenses.setOnItemClickListener((parent, view, position, id) -> {
            selectedExpenseIndex = position;
            String[] data = expenseList.get(position).split(" - ");
            edtExpenseContent.setText(data[0]);
            spinnerFrequency.setText(data[1]);
            edtAmount.setText(data[2].replace("$", ""));
            edtStartDate.setText(data[3]);
            edtEndDate.setText(data[4]);
        });

        listViewExpenses.setOnItemLongClickListener((parent, view, position, id) -> {
            new AlertDialog.Builder(this)
                    .setTitle("Delete Expense")
                    .setMessage("Are you sure you want to delete this expense?")
                    .setPositiveButton("Delete", (dialog, which) -> {
                        expenseList.remove(position);
                        adapter.notifyDataSetChanged();
                        saveExpenses();
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
            return true;
        });
    }

    private void showDatePickerDialog(EditText editText) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, selectedYear, selectedMonth, selectedDay) -> {
            String date = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
            editText.setText(date);
        }, year, month, day);
        datePickerDialog.show();
    }

    private void addExpense() {
        String content = edtExpenseContent.getText().toString();
        String frequency = spinnerFrequency.getText().toString();
        String amount = edtAmount.getText().toString();
        String startDate = edtStartDate.getText().toString();
        String endDate = edtEndDate.getText().toString();

        if (content.isEmpty() || frequency.isEmpty() || amount.isEmpty() || startDate.isEmpty() || endDate.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        String expense = content + " - " + frequency + " - " + amount + "$ - " + startDate + " - " + endDate;
        expenseList.add(expense);
        adapter.notifyDataSetChanged();
        saveExpenses();
        clearFields();
    }

    private void editExpense() {
        if (selectedExpenseIndex == -1) {
            Toast.makeText(this, "Select an expense to edit", Toast.LENGTH_SHORT).show();
            return;
        }

        String content = edtExpenseContent.getText().toString();
        String frequency = spinnerFrequency.getText().toString();
        String amount = edtAmount.getText().toString();
        String startDate = edtStartDate.getText().toString();
        String endDate = edtEndDate.getText().toString();

        String updatedExpense = content + " - " + frequency + " - " + amount + "$ - " + startDate + " - " + endDate;
        expenseList.set(selectedExpenseIndex, updatedExpense);
        adapter.notifyDataSetChanged();
        saveExpenses();
        clearFields();
        selectedExpenseIndex = -1;
    }

    private void clearFields() {
        edtExpenseContent.setText("");
        spinnerFrequency.setText("");
        edtAmount.setText("");
        edtStartDate.setText("");
        edtEndDate.setText("");
    }

    private void saveExpenses() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Set<String> set = new HashSet<>(expenseList);
        editor.putStringSet("expenses", set);
        editor.apply();
    }

    private Set<String> loadExpenses() {
        return sharedPreferences.getStringSet("expenses", new HashSet<>());
    }
}
