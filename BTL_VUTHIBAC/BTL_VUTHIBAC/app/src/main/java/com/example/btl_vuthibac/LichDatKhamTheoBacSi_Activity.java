package com.example.btl_vuthibac;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;

public class LichDatKhamTheoBacSi_Activity extends AppCompatActivity {
    Database database;
    ListView lv;
    ArrayList<ThongTinKhachHangDatLich> mangDL;
    ThongTinKhachHangDatLichAdapter adapter;
    String mabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lich_dat_kham_theo_bac_si);
        ImageButton btncanhan = findViewById(R.id.btncanhan);
        ImageButton btndatlich = findViewById(R.id.btnlichdat);
        ImageButton btntinnhan = findViewById(R.id.btntinnhan);

        ImageButton btntrangchu = findViewById(R.id.btntrangchu);
        btntrangchu.setOnClickListener(new View.OnClickListener() {
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
                    Intent intent = new Intent(getApplicationContext(),TrangchuBacSi_Activity.class);
                    startActivity(intent);
                }
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
                    Intent intent = new Intent(getApplicationContext(),TrangCaNhan_Bacsi_Activity.class);
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
        // Ánh xạ ListView
        lv = findViewById(R.id.listtk);
        mangDL = new ArrayList<>();
        adapter = new ThongTinKhachHangDatLichAdapter(getApplicationContext(), mangDL, false);
        lv.setAdapter(adapter);

        // Khởi tạo database
        database = new Database(this, "datlichkham.db", null, 1);
        database.QueryData("CREATE TABLE IF NOT EXISTS thongtinlichdatkham(maso INTEGER PRIMARY KEY AUTOINCREMENT, hoten NVARCHAR(50), sdt VARCHAR(15), diachi TEXT, giomuonkham VARCHAR(15), ngaymuonkham VARCHAR(30), dichvukham NVARCHAR(200) NOT NULL, tongtien VARCHAR(50), tendn VARCHAR(20), mabs VARCHAR(20))");

        // Nhận mã bác sĩ từ Intent
        mabs = getIntent().getStringExtra("mabs1");

        if (mabs == null) {
            SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
            mabs = sharedPreferences.getString("mabs1", null);
        }

        if (mabs == null) {
            Toast.makeText(this, "Mã bác sĩ (mabs) không tồn tại!", Toast.LENGTH_SHORT).show();
            return; // Dừng nếu không có mabs
        }

        // Gọi hàm để tải dữ liệu lịch đặt khám theo bác sĩ
        Loaddulieudatlichtheouser();
    }

    private void Loaddulieudatlichtheouser() {
        Cursor dataCongViec;
        // Lấy dữ liệu từ database theo mã bác sĩ (mabs)
        dataCongViec = database.GetData("SELECT * FROM thongtinlichdatkham WHERE mabs = ?", new String[]{mabs});

        if (dataCongViec.getCount() == 0) {
            Toast.makeText(this, "Không có dữ liệu cho mã bác sĩ: " + mabs, Toast.LENGTH_SHORT).show();
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

            // Thông báo thông tin từng bản ghi
//            Toast.makeText(this, "Mã: " + maso + ", Họ tên: " + hoten, Toast.LENGTH_SHORT).show();

            mangDL.add(new ThongTinKhachHangDatLich(maso, hoten, sdt, diachi, giokham, ngaykham, dichvu, tongtien, tendnResult, mabs));
        }

        // Thông báo số lượng bản ghi và cập nhật adapter
//        Toast.makeText(this, "Số bản ghi: " + mangDL.size(), Toast.LENGTH_SHORT).show();
//        adapter.notifyDataSetChanged(); // Cập nhật adapter sau khi dữ liệu thay đổi
    }
}