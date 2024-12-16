package com.example.btl_vuthibac;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class ChiTietBacSi_Activity extends AppCompatActivity {
    private String mabs, tendn; // Thêm từ khóa private
    private ChiTietBacSi chiTietBacSi; // Để lưu thông tin bác sĩ

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_bac_si);

        Button datlichkhambenh = findViewById(R.id.btndatlich);
        TextView tenbs = findViewById(R.id.ht1);
       ImageView imgctbs = findViewById(R.id.imgctbs);
        TextView gt = findViewById(R.id.gt1);
        TextView chuyennganh = findViewById(R.id.cn1);
        TextView sdt = findViewById(R.id.sdt1);
        TextView namsinh = findViewById(R.id.ns1);

    // Nhận dữ liệu từ Intent
        Intent intent = getIntent();
        chiTietBacSi = intent.getParcelableExtra("chitietbacsi");

        // Kiểm tra và lấy tendn từ SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        tendn = sharedPreferences.getString("tendn", null); // Giả sử bạn đã lưu tendn ở đây
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
                    Intent intent = new Intent(getApplicationContext(),LichDatKhamTheoUser_Activity.class);
                    startActivity(intent);
                }
            }
        });

        btncanhan.setOnClickListener(new View.OnClickListener() {
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
                    Intent intent = new Intent(getApplicationContext(),TrangCaNhan_Activity.class);
                    startActivity(intent);
                }
            }
        });

        btntrangchu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //kiểm tra trạng thái đăng nhập của ng dùng

                // Đã đăng nhập, chuyển đến trang 2
                Intent intent = new Intent(getApplicationContext(), TrangchuBacSi_Activity.class);
                startActivity(intent);
            }
        });
        if (chiTietBacSi != null) {
            mabs = chiTietBacSi.getMabs(); // Lấy mabs từ đối tượng bác sĩ

            // Cập nhật UI với dữ liệu bác sĩ
            tenbs.setText(chiTietBacSi.getTenbs());
            gt.setText(chiTietBacSi.getGioitinh() != null ? chiTietBacSi.getGioitinh() : "Không có dữ liệu");
            namsinh.setText(chiTietBacSi.getNamsinh() != null ? chiTietBacSi.getNamsinh() : "Không có dữ liệu");
            sdt.setText(chiTietBacSi.getSdt() != null ? chiTietBacSi.getSdt() : "Không có dữ liệu");
            chuyennganh.setText(chiTietBacSi.getChuyennganh() != null ? chiTietBacSi.getChuyennganh() : "Không có dữ liệu");

            // Hiển thị ảnh
            byte[] anhByteArray = chiTietBacSi.getAnh();
            if (anhByteArray != null && anhByteArray.length > 0) {
                Bitmap imganhbs = BitmapFactory.decodeByteArray(anhByteArray, 0, anhByteArray.length);
                imgctbs.setImageBitmap(imganhbs);
            } else {
                imgctbs.setImageResource(R.drawable.imgmacdinh); // Ảnh mặc định
            }
        } else {
            tenbs.setText("Không có dữ liệu");
        }

        // Thiết lập sự kiện click cho nút "Đặt lịch khám bệnh"
        datlichkhambenh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Kiểm tra trạng thái đăng nhập
                boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);
                if (!isLoggedIn) {
                    // Chưa đăng nhập, chuyển đến trang đăng nhập
                    Intent loginIntent = new Intent(getApplicationContext(), Login.class);
                    startActivity(loginIntent);
                } else {
                    // Đã đăng nhập, chuyển đến ThemLichDatKham_Activity
                    Intent intentToThemLich = new Intent(ChiTietBacSi_Activity.this, ThemLichDatKham_Activity.class);
                    intentToThemLich.putExtra("mabs", mabs); // Truyền mabs
                    intentToThemLich.putExtra("tendn", tendn); // Truyền tendn
                    startActivity(intentToThemLich);
                }
            }
        });
    }
}