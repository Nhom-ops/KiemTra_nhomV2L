package com.example.btl_vuthibac;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import java.util.ArrayList;

public class LichDatKhamTheoUser_Activity extends AppCompatActivity {
    Database database;
    ListView lv;
    ArrayList<ThongTinKhachHangDatLich> mangDL;
    ThongTinKhachHangDatLichAdapter adapter;
    String tendn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lich_dat_kham_theo_user);
        ImageButton btncanhan = findViewById(R.id.btncanhan);
        ImageButton btndatlich = findViewById(R.id.btnlichdat);
        ImageButton btntinnhan = findViewById(R.id.btntinnhan);

        ImageButton btntrangchu = findViewById(R.id.btntrangchu);
        btntrangchu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), TrangchuAdmin_Activity.class);
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
                Intent intent = new Intent(getApplicationContext(), TrangchuNgdung_Activity.class);
                startActivity(intent);
            }
        });
        // Ánh xạ ListView
        lv = findViewById(R.id.listtk);
        mangDL = new ArrayList<>();
        adapter = new ThongTinKhachHangDatLichAdapter(getApplicationContext(), mangDL, false);
        lv.setAdapter(adapter);

        // Khởi tạo database
        database = new Database(this, "datlichkham.db", null, 1);
        database.QueryData("CREATE TABLE IF NOT EXISTS thongtinlichdatkham(maso INTEGER PRIMARY KEY AUTOINCREMENT, hoten NVARCHAR(50), sdt VARCHAR(15), diachi TEXT, giomuonkham VARCHAR(15), ngaymuonkham VARCHAR(30), dichvukham NVARCHAR(200) NOT NULL, tongtien VARCHAR(50), tendn VARCHAR(20), mabs VARCHAR(20))");

        // Nhận tên đăng nhập từ Intent
        tendn = getIntent().getStringExtra("tendn");
        if (tendn == null) {
            SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
            tendn = sharedPreferences.getString("tendn", null);
        }

        if (tendn == null) {
            Log.e("LichDatKhamTheoUser", "Tên đăng nhập (tendn) không tồn tại!");
            return; // Dừng nếu không có tendn
        }

        // Gọi hàm để tải dữ liệu lịch đặt khám theo user
        Loaddulieudatlichtheouser();
    }

    private void Loaddulieudatlichtheouser() {
        Cursor dataCongViec;
        // Lấy dữ liệu từ database theo tên đăng nhập (tendn)
        dataCongViec = database.GetData("SELECT * FROM thongtinlichdatkham WHERE tendn = ?", new String[]{tendn});

        if (dataCongViec.getCount() == 0) {
            Log.e("LichDatKhamTheoUser", "Không có dữ liệu cho tên đăng nhập: " + tendn);
            return;
        }

        // Xóa dữ liệu cũ trong danh sách
        mangDL.clear();

        // Duyệt qua dữ liệu và thêm vào mảng
        while (dataCongViec.moveToNext()) {
            String maso = dataCongViec.getString(0);
            String hoten = dataCongViec.getString(1);
            String sdt = dataCongViec.getString(2);
            String diachi = dataCongViec.getString(3);
            String giokham = dataCongViec.getString(4);
            String ngaykham = dataCongViec.getString(5);
            String dichvu = dataCongViec.getString(6);
            String tongtien = dataCongViec.getString(7);
            String tendnResult = dataCongViec.getString(8);
            String mabs = dataCongViec.getString(9);

            mangDL.add(new ThongTinKhachHangDatLich(maso, hoten, sdt, diachi, giokham, ngaykham, dichvu, tongtien, tendnResult, mabs));
        }

        // Log số lượng bản ghi và cập nhật adapter
        Log.d("DataCount", "Số bản ghi: " + mangDL.size());
        adapter.notifyDataSetChanged(); // Cập nhật adapter sau khi dữ liệu thay đổi
    }
}