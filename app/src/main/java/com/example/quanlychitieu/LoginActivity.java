package com.example.quanlychitieu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    // Tài khoản cố định trong code theo yêu cầu đề bài
    private static final String VALID_USERNAME = "admin";
    private static final String VALID_PASSWORD = "123456";

    private EditText edtUsername;
    private EditText edtPassword;
    private TextView tvErrorMessage;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Ánh xạ các thành phần giao diện
        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        tvErrorMessage = findViewById(R.id.tvErrorMessage);
        btnLogin = findViewById(R.id.btnLogin);

        // Tự động ẩn thông báo lỗi khi người dùng gõ phím lại
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int count, int after) {
                if (tvErrorMessage.getVisibility() == View.VISIBLE) {
                    tvErrorMessage.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };
        edtUsername.addTextChangedListener(textWatcher);
        edtPassword.addTextChangedListener(textWatcher);

        TextView tvSampleAccount = findViewById(R.id.tvSampleAccount);
        if (tvSampleAccount != null) {
            tvSampleAccount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    edtUsername.setText(VALID_USERNAME);
                    edtPassword.setText(VALID_PASSWORD);
                    tvErrorMessage.setVisibility(View.GONE);
                }
            });
        }

        // Xử lý sự kiện nhấn nút Đăng nhập
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleLogin();
            }
        });
    }

    private void handleLogin() {
        String username = edtUsername.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        // Kiểm tra không để trống
        if (username.isEmpty() || password.isEmpty()) {
            tvErrorMessage.setText("Vui lòng nhập đầy đủ tên đăng nhập và mật khẩu!");
            tvErrorMessage.setVisibility(View.VISIBLE);
            Toast.makeText(LoginActivity.this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kiểm tra thông tin đăng nhập với tài khoản cố định
        if (VALID_USERNAME.equals(username) && VALID_PASSWORD.equals(password)) {
            tvErrorMessage.setVisibility(View.GONE);
            Toast.makeText(LoginActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();

            // Chuyển sang MainActivity
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);

            // Gọi finish() để người dùng không quay lại màn hình đăng nhập bằng nút Back
            finish();
        } else {
            // Sai tên đăng nhập hoặc mật khẩu
            tvErrorMessage.setText(R.string.error_wrong_credentials);
            tvErrorMessage.setVisibility(View.VISIBLE);
            Toast.makeText(LoginActivity.this, R.string.error_wrong_credentials, Toast.LENGTH_SHORT).show();
        }
    }
}
