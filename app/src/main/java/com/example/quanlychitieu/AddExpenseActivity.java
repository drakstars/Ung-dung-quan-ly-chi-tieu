package com.example.quanlychitieu;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Locale;
import java.util.UUID;

public class AddExpenseActivity extends AppCompatActivity {

    private ImageView btnBackAdd;
    private EditText edtExpenseName;
    private EditText edtExpenseAmount;
    private Spinner spnCategory;
    private DatePicker datePicker;
    private EditText edtExpenseNote;
    private Button btnSaveExpense;

    private final String[] categories = {"Ăn uống", "Đi lại", "Học tập", "Giải trí", "Khác"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        // Ánh xạ View
        btnBackAdd = findViewById(R.id.btnBackAdd);
        edtExpenseName = findViewById(R.id.edtExpenseName);
        edtExpenseAmount = findViewById(R.id.edtExpenseAmount);
        spnCategory = findViewById(R.id.spnCategory);
        datePicker = findViewById(R.id.datePicker);
        edtExpenseNote = findViewById(R.id.edtExpenseNote);
        btnSaveExpense = findViewById(R.id.btnSaveExpense);

        // Nút back trên toolbar
        btnBackAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Thiết lập Adapter cho Spinner danh mục
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                categories
        );
        spnCategory.setAdapter(spinnerAdapter);

        // Khởi tạo ngày hiện tại cho DatePicker
        Calendar calendar = Calendar.getInstance();
        datePicker.init(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH),
                null
        );

        // Xử lý nút Lưu khoản chi
        btnSaveExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveExpense();
            }
        });
    }

    private void saveExpense() {
        String name = edtExpenseName.getText().toString().trim();
        String amountStr = edtExpenseAmount.getText().toString().trim();
        String note = edtExpenseNote.getText().toString().trim();

        // 1. Kiểm tra tên khoản chi
        if (name.isEmpty()) {
            edtExpenseName.setError("Vui lòng nhập tên khoản chi!");
            edtExpenseName.requestFocus();
            Toast.makeText(this, "Tên khoản chi không được để trống!", Toast.LENGTH_SHORT).show();
            return;
        }

        // 2. Kiểm tra số tiền
        if (amountStr.isEmpty()) {
            edtExpenseAmount.setError("Vui lòng nhập số tiền!");
            edtExpenseAmount.requestFocus();
            Toast.makeText(this, "Số tiền không được để trống!", Toast.LENGTH_SHORT).show();
            return;
        }

        long amount;
        try {
            amount = Long.parseLong(amountStr);
            if (amount <= 0) {
                edtExpenseAmount.setError("Số tiền phải lớn hơn 0!");
                edtExpenseAmount.requestFocus();
                Toast.makeText(this, "Số tiền phải lớn hơn 0!", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (NumberFormatException e) {
            edtExpenseAmount.setError("Số tiền không hợp lệ!");
            edtExpenseAmount.requestFocus();
            Toast.makeText(this, "Số tiền không hợp lệ!", Toast.LENGTH_SHORT).show();
            return;
        }

        // 3. Lấy danh mục được chọn
        String category = spnCategory.getSelectedItem().toString();

        // 4. Lấy ngày chi tiêu từ DatePicker (lưu ý: getMonth() trả về từ 0..11 nên cần +1)
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth() + 1;
        int year = datePicker.getYear();
        String date = String.format(Locale.getDefault(), "%02d/%02d/%04d", day, month, year);

        // 5. Tạo đối tượng Expense mới
        String id = UUID.randomUUID().toString();
        Expense newExpense = new Expense(id, name, amount, category, date, note);

        // 6. Đóng gói kết quả và trả về cho MainActivity
        Intent resultIntent = new Intent();
        resultIntent.putExtra(MainActivity.EXTRA_EXPENSE, newExpense);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }
}
