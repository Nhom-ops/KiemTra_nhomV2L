package com.example.btl_vuthibac;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.app.AlertDialog;

public class TrangCaNhan_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trang_ca_nhan);

       Button dangxuat = findViewById(R.id.btndangxuat);
        TextView textTendn = findViewById(R.id.tendn); // TextView hiển thị tên đăng nhập

        // Lấy tendn từ SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String tendn = sharedPreferences.getString("tendn", null);

        if (tendn != null) {
            textTendn .setText(tendn);

            Intent c = new Intent(getApplicationContext(), ThemLichDatKham_Activity.class);
            c.putExtra("tendn", tendn); // Truyền mabs


        } else {
            Intent intent = new Intent(TrangCaNhan_Activity.this, Login.class);

            startActivity(intent);
            finish(); // Kết thúc TrangCaNhan_Activity; // Thông báo nếu chưa đăng nhập
        }
        ImageButton btncanhan = findViewById(R.id.btncanhan);
        ImageButton btndatlich = findViewById(R.id.btnlichdat);
        ImageButton btntinnhan = findViewById(R.id.btntinnhan);

        ImageButton btntrangchu = findViewById(R.id.btntrangchu);
        btntrangchu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), TrangchuNgdung_Activity.class);
                startActivity(intent);
            }
        });
        btntinnhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btndatlich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //kiểm tra trạng thái đăng nhập của ng dùng
                SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);

                if (!isLoggedIn) {
                    // Chưa đăng nhập, chuyển đến trang login
                    Intent intent = new Intent(getApplicationContext(), Login.class);
                    startActivity(intent);
                } else {
                    // Đã đăng nhập, chuyển đến trang 2
                    Intent intent = new Intent(getApplicationContext(), LichDatKhamTheoUser_Activity.class);
                    startActivity(intent);
                }

            }
        });

        btncanhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });

        btntrangchu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //kiểm tra trạng thái đăng nhập của ng dùng

                // Đã đăng nhập, chuyển đến trang 2
                Intent intent = new Intent(getApplicationContext(),TrangchuNgdung_Activity.class);
                startActivity(intent);
            }
        });
        dangxuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(TrangCaNhan_Activity.this)
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
                            Intent intent = new Intent(getApplicationContext(), TrangchuNgdung_Activity.class);
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