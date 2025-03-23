package com.example.androi_asm;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.*;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import java.text.SimpleDateFormat;
import java.util.*;

public class RecurringExpensesActivity extends AppCompatActivity {

    // UI elements declaration
    private EditText edtExpenseContent, edtAmount, edtStartDate, edtEndDate;
    private Button btnAddExpense, btnEditExpense, btnBackHome;
    private ListView listViewExpenses;

    // List to store expense entries
    private ArrayList<String> expenseList = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private int selectedPosition = -1; // variable to store the position of the selected item for editing

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Enable edge-to-edge display and set the layout
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_recurring_expenses);

        // Handle padding for system bars (status bar, navigation bar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Map UI components to variables
        edtExpenseContent = findViewById(R.id.edtExpenseContent);
        edtAmount = findViewById(R.id.edtAmount);
        edtStartDate = findViewById(R.id.edtStartDate);
        edtEndDate = findViewById(R.id.edtEndDate);
        btnAddExpense = findViewById(R.id.btnAddExpense);
        btnEditExpense = findViewById(R.id.btnEditExpense);
        btnBackHome = findViewById(R.id.btnBackHome);
        listViewExpenses = findViewById(R.id.listViewExpenses);

        // Initialize list adapter for displaying expenses
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, expenseList);
        listViewExpenses.setAdapter(adapter);

        // Set click listeners to show date picker dialog when clicking start or end date fields
        edtStartDate.setOnClickListener(v -> showDatePickerDialog(edtStartDate));
        edtEndDate.setOnClickListener(v -> showDatePickerDialog(edtEndDate));

        // Add or update expense entry
        btnAddExpense.setOnClickListener(v -> {
            String content = edtExpenseContent.getText().toString().trim();
            String amount = edtAmount.getText().toString().trim();
            String startDate = edtStartDate.getText().toString().trim();
            String endDate = edtEndDate.getText().toString().trim();

            // Validate input fields
            if (content.isEmpty() || amount.isEmpty() || startDate.isEmpty() || endDate.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Format expense string
            String expense = content + " - " + amount + " VND (From " + startDate + " to " + endDate + ")";
            if (selectedPosition == -1) {
                // Add new expense if not editing
                expenseList.add(expense);
            } else {
                // Update existing expense if an item was selected
                expenseList.set(selectedPosition, expense);
                selectedPosition = -1;
                btnAddExpense.setText("Submit"); // Reset button text back to submit
            }

            adapter.notifyDataSetChanged(); // Refresh list
            clearFields(); // Clear input fields
        });

        // Handle selecting an item from the list for editing
        listViewExpenses.setOnItemClickListener((parent, view, position, id) -> {
            String item = expenseList.get(position);
            // Example: "Electricity - 300000 VND (From 01/04/2024 to 30/04/2024)"
            String[] parts = item.split(" - | VND \\(From | to |\\)");
            if (parts.length >= 4) {
                edtExpenseContent.setText(parts[0]);
                edtAmount.setText(parts[1]);
                edtStartDate.setText(parts[2]);
                edtEndDate.setText(parts[3]);
                selectedPosition = position;
                btnAddExpense.setText("Update"); // Change button text to indicate update mode
            }
        });

        // Edit button reminder to select an item from the list first
        btnEditExpense.setOnClickListener(v -> {
            if (selectedPosition == -1) {
                Toast.makeText(this, "Tap an item in the list first to edit", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Now you can edit fields and click Update", Toast.LENGTH_SHORT).show();
            }
        });

        // Back to home screen
        btnBackHome.setOnClickListener(v -> finish());
    }

    // Method to show a date picker and set selected date to the EditText field
    private void showDatePickerDialog(EditText editText) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> {
                    calendar.set(year, month, dayOfMonth);
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                    editText.setText(sdf.format(calendar.getTime()));
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    // Method to clear input fields after adding or updating an expense
    private void clearFields() {
        edtExpenseContent.setText("");
        edtAmount.setText("");
        edtStartDate.setText("");
        edtEndDate.setText("");
    }
}
