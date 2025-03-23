package com.example.androi_asm;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import models.ExpenseItem;
import java.util.ArrayList;
import java.util.Calendar;

public class ExpenseActivity extends AppCompatActivity {
    // Khai báo các thành phần giao diện
    EditText txtDateIncome, txtNoteExpense, txtAmountExpense;
    Button btnSubmitExpense, btnBack;
    ListView listViewExpenses;
    ArrayList<ExpenseItem> expenseList; // Danh sách các khoản chi tiêu
    ArrayAdapter expenseAdapter;        // Adapter để hiển thị danh sách
    int editIndex = -1;                // Vị trí đang chỉnh sửa (nếu có), mặc định là -1 nghĩa là thêm mới

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense); // Gán layout cho Activity

        // Ánh xạ các thành phần từ XML
        txtDateIncome = findViewById(R.id.txtDateIncome);
        txtNoteExpense = findViewById(R.id.txtNoteExpense);
        txtAmountExpense = findViewById(R.id.txtAmountExpense);
        btnSubmitExpense = findViewById(R.id.btnSubmitExpense);
        btnBack = findViewById(R.id.btnBack);
        listViewExpenses = findViewById(R.id.listViewExpenses);

        // Khởi tạo danh sách và adapter
        expenseList = new ArrayList<>();
        expenseAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, expenseList);
        listViewExpenses.setAdapter(expenseAdapter);

        // Mở hộp thoại chọn ngày khi click vào ô ngày
        txtDateIncome.setOnClickListener(v -> showDatePicker());

        // Xử lý sự kiện khi bấm nút Submit
        btnSubmitExpense.setOnClickListener(v -> {
            String date = txtDateIncome.getText().toString();
            String note = txtNoteExpense.getText().toString();
            String amountStr = txtAmountExpense.getText().toString();

            // Kiểm tra nếu còn ô chưa nhập
            if (date.isEmpty() || note.isEmpty() || amountStr.isEmpty()) {
                Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            double amount = Double.parseDouble(amountStr);
            String category = detectCategory(note); // Xác định danh mục chi tiêu dựa vào ghi chú

            // Nếu đang thêm mới
            if (editIndex == -1) {
                expenseList.add(new ExpenseItem(date, note, amount, category));
            } else { // Nếu đang chỉnh sửa
                ExpenseItem item = expenseList.get(editIndex);
                item = new ExpenseItem(date, note, amount, category);
                expenseList.set(editIndex, item);
                editIndex = -1; // Reset trạng thái về thêm mới
            }

            expenseAdapter.notifyDataSetChanged(); // Cập nhật giao diện
            clearInputs(); // Xóa dữ liệu trong ô nhập
        });

        // Khi click vào một item trong ListView để chỉnh sửa
        listViewExpenses.setOnItemClickListener((parent, view, position, id) -> {
            ExpenseItem item = expenseList.get(position);
            txtDateIncome.setText(item.getDate());
            txtNoteExpense.setText(item.getNote());
            txtAmountExpense.setText(String.valueOf(item.getAmount()));
            editIndex = position; // Ghi nhớ vị trí item đang chỉnh sửa
        });

        // Khi nhấn giữ vào item để xóa
        listViewExpenses.setOnItemLongClickListener((parent, view, position, id) -> {
            expenseList.remove(position);
            expenseAdapter.notifyDataSetChanged();
            Toast.makeText(this, "Đã xóa", Toast.LENGTH_SHORT).show();
            return true;
        });

        // Nút quay về màn hình chính
        btnBack.setOnClickListener(v -> finish());
    }

    // Hàm hiển thị DatePicker để chọn ngày
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

    // Hàm xóa dữ liệu trong các ô nhập sau khi submit
    private void clearInputs() {
        txtDateIncome.setText("");
        txtNoteExpense.setText("");
        txtAmountExpense.setText("");
    }

    // Hàm tự động phát hiện danh mục dựa trên từ khóa trong ghi chú
    private String detectCategory(String note) {
        note = note.toLowerCase();
        if (note.contains("food")) return "Food";
        if (note.contains("transport")) return "Transport";
        if (note.contains("shop")) return "Shopping";
        if (note.contains("bill")) return "Bills";
        return "Other";
    }
}
