package com.example.quanlychitieu;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ExpenseAdapter.OnItemClickListener {

    public static final String EXTRA_EXPENSE = "EXTRA_EXPENSE";
    public static final String EXTRA_POSITION = "EXTRA_POSITION";
    public static final String ACTION_TYPE = "ACTION_TYPE";
    public static final String ACTION_DELETE = "ACTION_DELETE";

    private TextView tvTotalAmount;
    private RecyclerView rvExpenses;
    private TextView tvEmptyList;
    private TextView tvHintDetail;
    private Button btnAddExpense;

    private ArrayList<Expense> expenseList;
    private ExpenseAdapter adapter;

    // Launcher mở màn hình Thêm khoản chi và nhận kết quả trả về
    private final ActivityResultLauncher<Intent> addExpenseLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Expense newExpense = (Expense) result.getData().getSerializableExtra(EXTRA_EXPENSE);
                        if (newExpense != null) {
                            expenseList.add(newExpense);
                            adapter.notifyItemInserted(expenseList.size() - 1);
                            updateTotalAmount();
                            Toast.makeText(MainActivity.this, "Đã thêm khoản chi mới!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
    );

    // Launcher mở màn hình Chi tiết khoản chi và nhận kết quả xóa nếu có
    private final ActivityResultLauncher<Intent> detailExpenseLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        String action = result.getData().getStringExtra(ACTION_TYPE);
                        if (ACTION_DELETE.equals(action)) {
                            int position = result.getData().getIntExtra(EXTRA_POSITION, -1);
                            if (position >= 0 && position < expenseList.size()) {
                                expenseList.remove(position);
                                adapter.notifyItemRemoved(position);
                                adapter.notifyItemRangeChanged(position, expenseList.size() - position);
                                updateTotalAmount();
                                Toast.makeText(MainActivity.this, "Đã xóa khoản chi thành công!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Ánh xạ View
        tvTotalAmount = findViewById(R.id.tvTotalAmount);
        rvExpenses = findViewById(R.id.rvExpenses);
        tvEmptyList = findViewById(R.id.tvEmptyList);
        tvHintDetail = findViewById(R.id.tvHintDetail);
        btnAddExpense = findViewById(R.id.btnAddExpense);

        // Khởi tạo dữ liệu mẫu ban đầu theo đề bài
        initSampleData();

        // Thiết lập RecyclerView và Adapter
        rvExpenses.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ExpenseAdapter(this, expenseList, this);
        rvExpenses.setAdapter(adapter);

        // Cập nhật tổng số tiền
        updateTotalAmount();

        // Xử lý nút Thêm khoản chi
        btnAddExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddExpenseActivity.class);
                addExpenseLauncher.launch(intent);
            }
        });
    }

    /**
     * Khởi tạo 3 khoản chi mẫu theo đúng yêu cầu đề bài:
     * 1. Ăn sáng - 30.000 đ - Ăn uống
     * 2. Đổ xăng - 50.000 đ - Đi lại
     * 3. Mua sách - 120.000 đ - Học tập
     */
    private void initSampleData() {
        expenseList = new ArrayList<>();
        expenseList.add(new Expense("1", "Ăn sáng", 30000, "Ăn uống", "12/10/2026", "Ăn phở bò"));
        expenseList.add(new Expense("2", "Đổ xăng", 50000, "Đi lại", "12/10/2026", "Đổ xăng xe máy"));
        expenseList.add(new Expense("3", "Mua sách", 120000, "Học tập", "13/10/2026", "Sách Lập trình Android"));
    }

    /**
     * Tính tổng số tiền và cập nhật lên TextView tổng tiền
     */
    private void updateTotalAmount() {
        long total = 0;
        for (Expense expense : expenseList) {
            total += expense.getAmount();
        }
        tvTotalAmount.setText(Expense.formatMoney(total));

        // Ẩn/hiển thị thông báo khi danh sách trống
        if (expenseList.isEmpty()) {
            tvEmptyList.setVisibility(View.VISIBLE);
            tvHintDetail.setVisibility(View.GONE);
        } else {
            tvEmptyList.setVisibility(View.GONE);
            tvHintDetail.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Xử lý khi nhấn vào 1 khoản chi: mở màn hình chi tiết
     */
    @Override
    public void onItemClick(Expense expense, int position) {
        Intent intent = new Intent(MainActivity.this, ExpenseDetailActivity.class);
        intent.putExtra(EXTRA_EXPENSE, expense);
        intent.putExtra(EXTRA_POSITION, position);
        detailExpenseLauncher.launch(intent);
    }
}
