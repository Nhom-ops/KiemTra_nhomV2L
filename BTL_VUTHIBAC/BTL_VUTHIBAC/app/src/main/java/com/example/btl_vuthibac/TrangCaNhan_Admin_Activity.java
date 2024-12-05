package com.example.btl_vuthibac;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class TrangCaNhan_Admin_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trang_ca_nhan_admin);

        Button dangxuat = findViewById(R.id.btndangxuat);
        TextView textTendn = findViewById(R.id.tendn); // TextView hiển thị tên đăng nhập
        ImageButton btncanhan = findViewById(R.id.btncanhan);
        ImageButton btndatlich = findViewById(R.id.btnlichdat);
        ImageButton btnbacsi = findViewById(R.id.btnbacsi);
        ImageButton btntaikhoan = findViewById(R.id.btntaikhoan);
        ImageButton btntrangchu = findViewById(R.id.btntrangchu);
        btntrangchu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);

                Intent intent;
                if (!isLoggedIn) {
                    // Chưa đăng nhập, chuyển đến trang login
                    intent = new Intent(getApplicationContext(), Login.class);
                } else {
                    // Đã đăng nhập, chuyển đến trang tài khoản
                    intent = new Intent(getApplicationContext(), TrangchuAdmin_Activity.class);
                }
                startActivity(intent);

            }
        });
        btntaikhoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);

                Intent intent;
                if (!isLoggedIn) {
                    // Chưa đăng nhập, chuyển đến trang login
                    intent = new Intent(getApplicationContext(), Login.class);
                } else {
                    // Đã đăng nhập, chuyển đến trang tài khoản
                    intent = new Intent(getApplicationContext(), TaiKhoan_Activity.class);
                }
                startActivity(intent);
            }
        });

        btndatlich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), DanhsachLichKhamDaDat_Activity.class);
                startActivity(intent);
            }
        });

        btncanhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);

                Intent intent;
                if (!isLoggedIn) {
                    // Chưa đăng nhập, chuyển đến trang login
                    intent = new Intent(getApplicationContext(), Login.class);
                } else {
                    // Đã đăng nhập, chuyển đến trang cá nhân
                    intent = new Intent(getApplicationContext(), TrangCaNhan_Activity.class);
                }
                startActivity(intent);
            }
        });

        btnbacsi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);

                Intent intent;
                if (!isLoggedIn) {
                    // Chưa đăng nhập, chuyển đến trang login
                    intent = new Intent(getApplicationContext(), Login.class);
                } else {
                    // Đã đăng nhập, chuyển đến trang bác sĩ
                    intent = new Intent(getApplicationContext(), TTBacSi_Activity.class);
                }
                startActivity(intent);
            }
        });

        // Lấy tendn từ SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String tendn = sharedPreferences.getString("tendn", null);

        if (tendn != null) {
            textTendn.setText(tendn);
        } else {
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish(); // Kết thúc TrangCaNhan_Activity; // Thông báo nếu chưa đăng nhập
        }

        dangxuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(TrangCaNhan_Admin_Activity.this) // Sửa tại đây
                        .setTitle("Đăng Xuất")
                        .setMessage("Bạn có chắc chắn muốn đăng xuất?")
                        .setPositiveButton("Có", (dialog, which) -> {
                            // Xóa trạng thái đăng nhập
                            SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean("isLoggedIn", false); // Đánh dấu là chưa đăng nhập
                            editor.putString("tendn", null); // Xóa tên đăng nhập
                            editor.apply();

                            // Quay lại Activity chính
                            Intent intent = new Intent(getApplicationContext(), TrangchuAdmin_Activity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK); // Xóa các Activity cũ
                            startActivity(intent);
                            finish(); // Kết thúc TrangCaNhan_Activity
                        })
                        .setNegativeButton("Không", null)
                        .show();
            }
        });
    }
}