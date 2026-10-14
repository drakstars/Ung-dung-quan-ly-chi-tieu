package com.example.quanlychitieu;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ExpenseDetailActivity extends AppCompatActivity {

    private ImageView btnBackDetail;
    private TextView tvDetailName;
    private TextView tvDetailAmount;
    private TextView tvDetailCategory;
    private TextView tvDetailDate;
    private TextView tvDetailNote;
    private Button btnBack;
    private Button btnDeleteExpense;

    private Expense currentExpense;
    private int expensePosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_detail);

        // Ánh xạ View
        btnBackDetail = findViewById(R.id.btnBackDetail);
        tvDetailName = findViewById(R.id.tvDetailName);
        tvDetailAmount = findViewById(R.id.tvDetailAmount);
        tvDetailCategory = findViewById(R.id.tvDetailCategory);
        tvDetailDate = findViewById(R.id.tvDetailDate);
        tvDetailNote = findViewById(R.id.tvDetailNote);
        btnBack = findViewById(R.id.btnBack);
        btnDeleteExpense = findViewById(R.id.btnDeleteExpense);

        // Nhận dữ liệu truyền từ MainActivity qua Intent
        if (getIntent() != null) {
            currentExpense = (Expense) getIntent().getSerializableExtra(MainActivity.EXTRA_EXPENSE);
            expensePosition = getIntent().getIntExtra(MainActivity.EXTRA_POSITION, -1);
        }

        // Hiển thị dữ liệu lên giao diện
        if (currentExpense != null) {
            tvDetailName.setText(currentExpense.getName());
            tvDetailAmount.setText(currentExpense.getFormattedAmount());
            tvDetailCategory.setText(currentExpense.getCategory());
            tvDetailDate.setText(currentExpense.getDate());

            if (currentExpense.getNote() != null && !currentExpense.getNote().trim().isEmpty()) {
                tvDetailNote.setText(currentExpense.getNote());
            } else {
                tvDetailNote.setText("(Không có ghi chú)");
            }
        }

        // Xử lý nút mũi tên quay lại trên Toolbar
        btnBackDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Xử lý nút Quay lại ở phía dưới
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Xử lý nút Xóa khoản chi
        btnDeleteExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDelete();
            }
        });
    }

    /**
     * Hiển thị hộp thoại xác nhận xóa để tránh người dùng bấm nhầm
     */
    private void confirmDelete() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.confirm_delete_title)
                .setMessage(R.string.confirm_delete_message)
                .setPositiveButton(R.string.btn_delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteAndReturn();
                    }
                })
                .setNegativeButton(R.string.btn_cancel, null)
                .show();
    }

    /**
     * Trả kết quả xóa về cho MainActivity và đóng Activity
     */
    private void deleteAndReturn() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(MainActivity.ACTION_TYPE, MainActivity.ACTION_DELETE);
        resultIntent.putExtra(MainActivity.EXTRA_POSITION, expensePosition);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }
}
