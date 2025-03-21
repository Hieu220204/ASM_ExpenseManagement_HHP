package com.example.androi_asm;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import models.BudgetItem;
import java.util.ArrayList;
import java.util.Calendar;

public class BudgetSettingActivity extends AppCompatActivity {
    private EditText edtContent, edtAmount, edtStartDate, edtEndDate;
    private Button btnAddExpense, btnEditExpense, btnBackHome;
    private ListView listViewExpenses;
    private ArrayAdapter<BudgetItem> adapter;
    private ArrayList<BudgetItem> budgetItems = new ArrayList<>();
    private int selectedPosition = -1; // để biết item nào đang được chọn để chỉnh sửa

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget_setting);

        edtContent = findViewById(R.id.edtExpenseContent);
        edtAmount = findViewById(R.id.edtAmount);
        edtStartDate = findViewById(R.id.edtStartDate);
        edtEndDate = findViewById(R.id.edtEndDate);
        btnAddExpense = findViewById(R.id.btnAddExpense);
        btnEditExpense = findViewById(R.id.btnEditExpense);
        btnBackHome = findViewById(R.id.btnBackHome);
        listViewExpenses = findViewById(R.id.listViewExpenses);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, budgetItems);
        listViewExpenses.setAdapter(adapter);

        edtStartDate.setOnClickListener(v -> showDatePicker(edtStartDate));
        edtEndDate.setOnClickListener(v -> showDatePicker(edtEndDate));

        btnAddExpense.setOnClickListener(v -> addBudgetItem());
        btnEditExpense.setOnClickListener(v -> editBudgetItem());

        listViewExpenses.setOnItemClickListener((parent, view, position, id) -> {
            selectedPosition = position;
            BudgetItem item = budgetItems.get(position);
            edtContent.setText(item.getContent());
            edtAmount.setText(String.valueOf(item.getAmount()));
            edtStartDate.setText(item.getStartDate());
            edtEndDate.setText(item.getEndDate());
        });

        btnBackHome.setOnClickListener(v -> finish());
    }

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
        adapter.notifyDataSetChanged();
        clearFields();
    }

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
        adapter.notifyDataSetChanged();
        clearFields();
        selectedPosition = -1;
        Toast.makeText(this, "Budget amount updated", Toast.LENGTH_SHORT).show();
    }

    private void clearFields() {
        edtContent.setText("");
        edtAmount.setText("");
        edtStartDate.setText("");
        edtEndDate.setText("");
    }
}
