package com.example.androi_asm;

import android.app.DatePickerDialog;
import android.icu.text.Edits;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import models.BudgetItem;
import java.util.ArrayList;
import java.util.Calendar;


// This activity allows users to set and manage budgets by adding, editing, and viewing budget items.
public class BudgetSettingActivity extends AppCompatActivity {

    // UI components
    private EditText edtContent, edtAmount, edtStartDate, edtEndDate;
    private Button btnAddExpense, btnEditExpense, btnBackHome;
    private ListView listViewExpenses;

    // Adapter and list to manage budget items
    private ArrayAdapter<BudgetItem> adapter;
    private ArrayList<BudgetItem> budgetItems = new ArrayList<>();
    private int selectedPosition = -1; // Keeps track of the selected item for editing

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget_setting);

        // Initialize UI elements
        edtContent = findViewById(R.id.edtExpenseContent);
        edtAmount = findViewById(R.id.edtAmount);
        edtStartDate = findViewById(R.id.edtStartDate);
        edtEndDate = findViewById(R.id.edtEndDate);
        btnAddExpense = findViewById(R.id.btnAddExpense);
        btnEditExpense = findViewById(R.id.btnEditExpense);
        btnBackHome = findViewById(R.id.btnBackHome);
        listViewExpenses = findViewById(R.id.listViewExpenses);

        // Set up adapter for ListView
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, budgetItems);
        listViewExpenses.setAdapter(adapter);

        // Set date pickers for start and end date fields
        edtStartDate.setOnClickListener(v -> showDatePicker(edtStartDate));
        edtEndDate.setOnClickListener(v -> showDatePicker(edtEndDate));

        // Handle add and edit button clicks
        btnAddExpense.setOnClickListener(v -> addBudgetItem());
        btnEditExpense.setOnClickListener(v -> editBudgetItem());

        // Handle item selection from the ListView for editing
        listViewExpenses.setOnItemClickListener((parent, view, position, id) -> {
            selectedPosition = position;
            BudgetItem item = budgetItems.get(position);
            // Pre-fill fields with selected item details
            edtContent.setText(item.getContent());
            edtAmount.setText(String.valueOf(item.getAmount()));
            edtStartDate.setText(item.getStartDate());
            edtEndDate.setText(item.getEndDate());
        });

        // Handle back button to return to the previous screen
        btnBackHome.setOnClickListener(v -> finish());
    }


    // Displays a date picker dialog and sets the selected date in the given EditText.
    private void showDatePicker(EditText editText) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePicker = new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> {
                    String date = dayOfMonth + "/" + (month + 1) + "/" + year;
                    editText.setText(date);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        datePicker.show();
    }


    // Adds a new budget item to the list based on user input.
    private void addBudgetItem() {
        String content = edtContent.getText().toString();
        String amountStr = edtAmount.getText().toString();
        String startDate = edtStartDate.getText().toString();
        String endDate = edtEndDate.getText().toString();

        // Validate that all fields are filled
        if (content.isEmpty() || amountStr.isEmpty() || startDate.isEmpty() || endDate.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double amount = Double.parseDouble(amountStr);
        BudgetItem item = new BudgetItem(content, amount, startDate, endDate);

        // Add new item to the list and update the adapter
        budgetItems.add(item);
        adapter.notifyDataSetChanged();
        clearFields();
    }


    // Edits the amount of the selected budget item.

    private void editBudgetItem() {
        if (selectedPosition == -1) {
            Toast.makeText(this, "Please select an item to edit", Toast.LENGTH_SHORT).show();
            return;
        }

        String amountStr = edtAmount.getText().toString();
        if (amountStr.isEmpty()) {
            Toast.makeText(this, "Enter new amount", Toast.LENGTH_SHORT).show();
            return;
        }

        double newAmount = Double.parseDouble(amountStr);
        // Update amount for the selected item
        budgetItems.get(selectedPosition).setAmount(newAmount);
        adapter.notifyDataSetChanged();
        clearFields();
        selectedPosition = -1;
        Toast.makeText(this, "Budget amount updated", Toast.LENGTH_SHORT).show();
    }

    /**
     * Clears all input fields.
     */
    private void clearFields() {
        edtContent.setText("");
        edtAmount.setText("");
        edtStartDate.setText("");
        edtEndDate.setText("");
    }
}