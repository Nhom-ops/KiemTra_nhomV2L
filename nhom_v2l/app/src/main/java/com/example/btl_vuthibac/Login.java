package com.example.btl_vuthibac;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends AppCompatActivity {

    Database database;
    String tendn;
    private Handler handler = new Handler();
    private Runnable timeoutRunnable;
    private static final long TIMEOUT_DURATION = 300000; // 30 giây

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button btnLogin = findViewById(R.id.btnLogin);
        EditText tdn = findViewById(R.id.tdn);
        EditText mk = findViewById(R.id.mk);
        TextView dangki = findViewById(R.id.dangki);

        database = new Database(this, "datlichkham.db", null, 1);

        dangki.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent a = new Intent(getApplicationContext(), DangKiTaiKhoan_Activity.class);
                startActivity(a);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = tdn.getText().toString();
                String password = mk.getText().toString();

                if (validateLogin(username, password)) {
                    // Gán tên đăng nhập cho biến tendn
                    tendn = username;

                    // Lưu thông tin vào SharedPreferences
                    SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("tendn", tendn); // Lưu tên đăng nhập
                    editor.putBoolean("isLoggedIn", true); // Đánh dấu là đã đăng nhập
                    editor.apply();

                    // Khởi động Timer
                    startAutoLogoutTimer();

                    // Chuyển đến activity phù hợp theo quyền
                    String quyen = getUserQuyen(username);
                    Intent intent;
                    if (quyen.equals("admin")) {
                        intent = new Intent(Login.this, TrangchuAdmin_Activity.class);
                    } else if (quyen.equals("bacsi")) {
                        intent = new Intent(Login.this, TrangCaNhan_Bacsi_Activity.class);
                    } else {
                        intent = new Intent(Login.this, TrangchuNgdung_Activity.class);
                    }
                    startActivity(intent);
                    finish(); // Kết thúc Login Activity
                } else {
                    Toast.makeText(Login.this, "Tên đăng nhập hoặc mật khẩu không đúng", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean validateLogin(String username, String password) {
        Cursor cursor = database.getReadableDatabase().rawQuery(
                "SELECT * FROM taikhoan WHERE tendn = ? AND matkhau = ?",
                new String[]{username, password});
        return cursor.getCount() > 0; // Kiểm tra xem có hàng nào không
    }

    private String getUserQuyen(String username) {
        Cursor cursor = database.getReadableDatabase().rawQuery(
                "SELECT quyen FROM taikhoan WHERE tendn = ?",
                new String[]{username});

        String quyen = "";
        if (cursor.moveToFirst()) {
            int quyenColumnIndex = cursor.getColumnIndex("quyen");
            if (quyenColumnIndex != -1) {
                quyen = cursor.getString(quyenColumnIndex);
            } else {
                Log.e("Error", "Column 'quyen' not found in result set");
            }
        }
        return quyen;
    }

    private void startAutoLogoutTimer() {
        // Hủy bỏ bất kỳ Runnable nào trước đó
        handler.removeCallbacks(timeoutRunnable);

        // Tạo Runnable cho việc đăng xuất tự động
        timeoutRunnable = new Runnable() {
            @Override
            public void run() {
                // Đăng xuất người dùng
                SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("isLoggedIn", false); // Đánh dấu là chưa đăng nhập
                editor.putString("tendn", null); // Xóa tên đăng nhập
                editor.apply();

                // Quay lại activity chính
                Intent intent = new Intent(Login.this, TrangchuNgdung_Activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        };

        // Đặt thời gian để đăng xuất sau 30 giây
        handler.postDelayed(timeoutRunnable, TIMEOUT_DURATION);
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        // Nếu có tương tác của người dùng, reset lại timer
        startAutoLogoutTimer();
    }



}