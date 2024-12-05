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

public class DanhsachLichKhamDaDat_Activity extends AppCompatActivity {
    Database database;
    ListView lv;
    ArrayList<ThongTinKhachHangDatLich> mangDL;
    ThongTinKhachHangDatLichAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danhsach_lich_kham_da_dat);
        ImageButton btncanhan = findViewById(R.id.btncanhan);
        ImageButton btndatlich = findViewById(R.id.btnlichdat);
        ImageButton btnbacsi = findViewById(R.id.btnbacsi);

        ImageButton btntaikhoan = findViewById(R.id.btntaikhoan);
        ImageButton btntrangchu = findViewById(R.id.btntrangchu);
        btntrangchu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), TrangchuAdmin_Activity.class);
                startActivity(intent);
            }
        });
        btntaikhoan.setOnClickListener(new View.OnClickListener() {
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
                    Intent intent = new Intent(getApplicationContext(), TaiKhoan_Activity.class);
                    startActivity(intent);
                }
            }
        });

        btndatlich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Đã đăng nhập, chuyển đến trang 2
                Intent intent = new Intent(getApplicationContext(), DanhsachLichKhamDaDat_Activity.class);
                startActivity(intent);

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
                    Intent intent = new Intent(getApplicationContext(), TrangCaNhan_Admin_Activity.class);
                    startActivity(intent);
                }
            }
        });

        btnbacsi.setOnClickListener(new View.OnClickListener() {
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
                    Intent intent = new Intent(getApplicationContext(), TTBacSi_Activity.class);
                    startActivity(intent);
                }
            }
        });
        lv = findViewById(R.id.listtk); // Ánh xạ ListView
        mangDL = new ArrayList<>();
        adapter = new ThongTinKhachHangDatLichAdapter(getApplicationContext(), mangDL, true);
        lv.setAdapter(adapter); // Thiết lập adapter cho ListView

        database = new Database(this, "datlichkham.db", null, 1);
        database.QueryData("CREATE TABLE IF NOT EXISTS thongtinlichdatkham(maso INTEGER PRIMARY KEY AUTOINCREMENT, hoten NVARCHAR(50), sdt VARCHAR(15), diachi TEXT, giomuonkham VARCHAR(15), ngaymuonkham VARCHAR(30), dichvukham NVARCHAR(200) NOT NULL, tongtien VARCHAR(50), tendn VARCHAR(20), mabs VARCHAR(20))");

        // Thêm dữ liệu mẫu
//        database.QueryData("INSERT INTO thongtinlichdatkham(hoten, sdt, diachi, giomuonkham, ngaymuonkham, dichvukham, tongtien, tendn, mabs) VALUES ('Nguyen Van A', '0123456789', '123 Nguyen Trai', '08:00', '2024-10-01', 'Khám tổng quát', '100000', 'helo', 'BS001')");
//        database.QueryData("INSERT INTO thongtinlichdatkham(hoten, sdt, diachi, giomuonkham, ngaymuonkham, dichvukham, tongtien, tendn, mabs) VALUES ('Tran Thi B', '0987654321', '456 Le Lai', '09:00', '2024-10-02', 'Khám tổng quát', '100000', 'bac1', 'BS002')");
//        database.QueryData("INSERT INTO thongtinlichdatkham(hoten, sdt, diachi, giomuonkham, ngaymuonkham, dichvukham, tongtien, tendn, mabs) VALUES ('Le Van C', '0912345678', '789 Hoang Hoa Tham', '10:00', '2024-10-03', 'Khám tổng quát', '100000', 'vubac', 'BS003')");


        Loaddulieudtalich(); // Gọi hàm để tải dữ liệu
    }

    private void Loaddulieudtalich() {
        Cursor dataCongViec = database.GetData("SELECT "
                + "thongtinlichdatkham.maso, "
                + "thongtinlichdatkham.hoten, "
                + "thongtinlichdatkham.sdt, "
                + "thongtinlichdatkham.diachi, "
                + "thongtinlichdatkham.giomuonkham, "
                + "thongtinlichdatkham.ngaymuonkham, "
                + "thongtinlichdatkham.dichvukham, "
                + "thongtinlichdatkham.tongtien, "
                + "thongtinlichdatkham.tendn, "
                + "thongtinlichdatkham.mabs "
                + "FROM thongtinlichdatkham "
                + "INNER JOIN taikhoan ON thongtinlichdatkham.tendn = taikhoan.tendn "
                + "INNER JOIN bacsi ON bacsi.mabs = thongtinlichdatkham.mabs");

        mangDL.clear();


        while (dataCongViec.moveToNext()) {

            String maso = dataCongViec.getString(0);
            String hoten = dataCongViec.getString(1);
            String sdt = dataCongViec.getString(2);
            String diachi = dataCongViec.getString(3);
            String giokham = dataCongViec.getString(4);
            String ngaykham = dataCongViec.getString(5);
            String dichvu = dataCongViec.getString(6);
            String tongtien = dataCongViec.getString(7);
            String tendn = dataCongViec.getString(8);
            String mabs = dataCongViec.getString(9);

            mangDL.add(new ThongTinKhachHangDatLich(maso, hoten, sdt, diachi, giokham, ngaykham, dichvu, tongtien, tendn, mabs));
        }
        Log.d("DataCount", "Số bản ghi: " + mangDL.size());


        adapter.notifyDataSetChanged(); // Cập nhật adapter
    }

}
