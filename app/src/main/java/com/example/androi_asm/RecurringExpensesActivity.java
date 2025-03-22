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
    private EditText edtExpenseContent, edtAmount, edtStartDate, edtEndDate;
    private Button btnAddExpense, btnEditExpense, btnBackHome;
    private ListView listViewExpenses;

    private ArrayList<String> expenseList = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private int selectedPosition = -1; // để lưu vị trí item khi chọn sửa

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_recurring_expenses);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Ánh xạ view
        edtExpenseContent = findViewById(R.id.edtExpenseContent);
        edtAmount = findViewById(R.id.edtAmount);
        edtStartDate = findViewById(R.id.edtStartDate);
        edtEndDate = findViewById(R.id.edtEndDate);
        btnAddExpense = findViewById(R.id.btnAddExpense);
        btnEditExpense = findViewById(R.id.btnEditExpense);
        btnBackHome = findViewById(R.id.btnBackHome);
        listViewExpenses = findViewById(R.id.listViewExpenses);

        // Khởi tạo adapter danh sách
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, expenseList);
        listViewExpenses.setAdapter(adapter);

        // Pick Date cho edtStartDate & edtEndDate
        edtStartDate.setOnClickListener(v -> showDatePickerDialog(edtStartDate));
        edtEndDate.setOnClickListener(v -> showDatePickerDialog(edtEndDate));

        // Thêm chi phí mới
        btnAddExpense.setOnClickListener(v -> {
            String content = edtExpenseContent.getText().toString().trim();
            String amount = edtAmount.getText().toString().trim();
            String startDate = edtStartDate.getText().toString().trim();
            String endDate = edtEndDate.getText().toString().trim();

            if (content.isEmpty() || amount.isEmpty() || startDate.isEmpty() || endDate.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            String expense = content + " - " + amount + " VND (From " + startDate + " to " + endDate + ")";
            if (selectedPosition == -1) {
                // Thêm mới
                expenseList.add(expense);
            } else {
                // Cập nhật
                expenseList.set(selectedPosition, expense);
                selectedPosition = -1;
                btnAddExpense.setText("Submit"); // reset nút về trạng thái thêm mới
            }

            adapter.notifyDataSetChanged();
            clearFields();
        });

        // Khi chọn item trong list để sửa
        listViewExpenses.setOnItemClickListener((parent, view, position, id) -> {
            String item = expenseList.get(position);
            // Tách dữ liệu từ chuỗi
            // Ví dụ chuỗi: "Electricity - 300000 VND (From 01/04/2024 to 30/04/2024)"
            String[] parts = item.split(" - | VND \\(From | to |\\)");
            if (parts.length >= 4) {
                edtExpenseContent.setText(parts[0]);
                edtAmount.setText(parts[1]);
                edtStartDate.setText(parts[2]);
                edtEndDate.setText(parts[3]);
                selectedPosition = position;
                btnAddExpense.setText("Update");
            }
        });

        // Nút Edit chuyển sang chế độ chỉnh sửa nhanh nếu có item được chọn
        btnEditExpense.setOnClickListener(v -> {
            if (selectedPosition == -1) {
                Toast.makeText(this, "Tap an item in the list first to edit", Toast.LENGTH_SHORT).show();
            } else {
                // Đã có thao tác chọn rồi nên không cần làm thêm gì
                Toast.makeText(this, "Now you can edit fields and click Update", Toast.LENGTH_SHORT).show();
            }
        });

        // Nút quay về màn hình chính
        btnBackHome.setOnClickListener(v -> finish());
    }

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

    private void clearFields() {
        edtExpenseContent.setText("");
        edtAmount.setText("");
        edtStartDate.setText("");
        edtEndDate.setText("");
    }
}
