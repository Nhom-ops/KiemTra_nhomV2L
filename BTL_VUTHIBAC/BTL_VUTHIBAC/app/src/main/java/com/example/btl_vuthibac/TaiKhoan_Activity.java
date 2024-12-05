package com.example.btl_vuthibac;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class TaiKhoan_Activity extends AppCompatActivity {
    Database database;
    ListView lv;
    int vitri;
    ArrayList<TaiKhoan> mangTK;
   TaiKhoanAdapter adapter;
    FloatingActionButton dauconggocphai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tai_khoan);
       dauconggocphai = findViewById(R.id.btnthem);
        lv = findViewById(R.id.listtk);
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
        dauconggocphai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent a=new Intent(getApplicationContext(),ThemTaiKhoan_Activity.class);
                startActivity(a);
            }
        });
        mangTK = new ArrayList<>();
        adapter = new TaiKhoanAdapter(getApplicationContext(), R.layout.ds_taikhoan, mangTK);
        lv.setAdapter(adapter);
        database = new Database(this, "datlichkham.db", null, 1);
        database.QueryData("CREATE TABLE IF NOT EXISTS taikhoan(tendn VARCHAR(20) PRIMARY KEY, matkhau VARCHAR(50), quyen VARCHAR(50))");
        // Thêm 2 dòng dữ liệu
   database.QueryData("INSERT  INTO taikhoan VALUES ('admin', '1234', 'admin')");
 database.QueryData("INSERT  INTO taikhoan VALUES ('bac2', '1111', 'bacsi')");
//        database.QueryData("INSERT  INTO taikhoan VALUES ('bac3', '1111', 'bacsi')");
//        database.QueryData("INSERT  INTO taikhoan VALUES ('bac4', '1111', 'bacsi')");
        Loaddulieutaikhoan();



// sự kiên di chuyển dấu cộng cuối góc phải
//        dauconggocphai.setOnTouchListener(new View.OnTouchListener() {
//            float dX, dY;
//
//            @Override
//            public boolean onTouch(View view, MotionEvent event) {
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                        // Lưu vị trí ban đầu
//                        dX = view.getX() - event.getRawX();
//                        dY = view.getY() - event.getRawY();
//                        break;
//                    case MotionEvent.ACTION_MOVE:
//                        // Di chuyển FAB
//                        view.animate()
//                                .x(event.getRawX() + dX)
//                                .y(event.getRawY() + dY)
//                                .setDuration(0)
//                                .start();
//                        break;
//                    case MotionEvent.ACTION_UP:
//                        // Nếu di chuyển không đủ lớn, coi như là một click
//                        if (event.getRawX() == view.getX() && event.getRawY() == view.getY()) {
//                            view.performClick();
//                        }
//                        break;
//                    default:
//                        return false;
//                }
//                return true; // Xử lý thành công
//            }
//        });
    }
    private void Loaddulieutaikhoan() {
        Cursor dataCongViec = database.GetData("SELECT * FROM taikhoan");
        mangTK.clear();
        while (dataCongViec.moveToNext()) {
            String tdn = dataCongViec.getString(0);
            String mk= dataCongViec.getString(1);
            String q = dataCongViec.getString(2);
            mangTK.add(new TaiKhoan(tdn, mk, q));
        }
        adapter.notifyDataSetChanged();
    }
}