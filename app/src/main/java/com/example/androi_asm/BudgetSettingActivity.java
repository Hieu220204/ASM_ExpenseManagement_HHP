package com.example.androi_asm;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import models.BudgetItem;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;

public class BudgetSettingActivity extends AppCompatActivity {

    // UI components
    private EditText edtContent, edtAmount, edtStartDate, edtEndDate;
    private Button btnAddExpense, btnEditExpense, btnBackHome;
    private ListView listViewExpenses;

    // Adapter and list to manage budget items
    private ArrayAdapter<String> adapter;
    private ArrayList<BudgetItem> budgetItems = new ArrayList<>();
    private int selectedPosition = -1; // Keeps track of the selected item for editing

    private SharedPreferences sharedPreferences;
    private Gson gson = new Gson();

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

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("BudgetData", Context.MODE_PRIVATE);
        loadBudgetData();

        // Set up adapter for ListView
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, formatBudgetItems());
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
            edtContent.setText(item.getContent());
            edtAmount.setText(String.valueOf(item.getAmount()));
            edtStartDate.setText(item.getStartDate());
            edtEndDate.setText(item.getEndDate());
        });

        // Handle item long click for deletion
        listViewExpenses.setOnItemLongClickListener((parent, view, position, id) -> {
            showDeleteConfirmationDialog(position);
            return true;
        });

        // Handle back button to return to the previous screen
        btnBackHome.setOnClickListener(v -> {
            saveBudgetData(); // Save data before exiting
            finish();
        });
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

        if (content.isEmpty() || amountStr.isEmpty() || startDate.isEmpty() || endDate.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double amount = Double.parseDouble(amountStr);
        BudgetItem item = new BudgetItem(content, amount, startDate, endDate);

        budgetItems.add(item);
        adapter.clear();
        adapter.addAll(formatBudgetItems());
        adapter.notifyDataSetChanged();
        saveBudgetData();
        clearFields();
    }

    // Edits the selected budget item.
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
        budgetItems.get(selectedPosition).setAmount(newAmount);

        adapter.clear();
        adapter.addAll(formatBudgetItems());
        adapter.notifyDataSetChanged();
        saveBudgetData();
        clearFields();
        selectedPosition = -1;
        Toast.makeText(this, "Budget amount updated", Toast.LENGTH_SHORT).show();
    }

    // Show confirmation dialog before deleting an item
    private void showDeleteConfirmationDialog(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Expense")
                .setMessage("Are you sure you want to delete this expense?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    budgetItems.remove(position);
                    adapter.clear();
                    adapter.addAll(formatBudgetItems());
                    adapter.notifyDataSetChanged();
                    saveBudgetData();
                    Toast.makeText(this, "Expense deleted", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .show();
    }

    // Saves budget data to SharedPreferences
    private void saveBudgetData() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String json = gson.toJson(budgetItems);
        editor.putString("budget_list", json);
        editor.apply();
    }

    // Loads budget data from SharedPreferences
    private void loadBudgetData() {
        String json = sharedPreferences.getString("budget_list", null);
        if (json != null) {
            Type type = new TypeToken<ArrayList<BudgetItem>>() {}.getType();
            budgetItems = gson.fromJson(json, type);
        }
    }

    // Clears all input fields
    private void clearFields() {
        edtContent.setText("");
        edtAmount.setText("");
        edtStartDate.setText("");
        edtEndDate.setText("");
    }

    // Format budget items with a dollar sign
    private ArrayList<String> formatBudgetItems() {
        ArrayList<String> formattedList = new ArrayList<>();
        for (BudgetItem item : budgetItems) {
            formattedList.add(item.getContent() + " - $" + item.getAmount() + " - " +  item.getStartDate() + " - " + item.getEndDate());
        }
        return formattedList;
    }
}
