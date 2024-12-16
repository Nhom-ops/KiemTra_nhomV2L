package com.example.btl_vuthibac;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class TTBacSi_Activity extends AppCompatActivity {
    Database database;
    ListView lv;
    int vitri;
    FloatingActionButton dauconggocphai;
    ArrayList<ThongTinBacSi> mangBS;
    ThongTinBacSiAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ttbac_si);
        lv = findViewById(R.id.listtk);
        dauconggocphai = findViewById(R.id.btnthem);
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
        mangBS = new ArrayList<>();

        adapter = new ThongTinBacSiAdapter(this, mangBS, true);
        lv.setAdapter(adapter);
        database = new Database(this, "datlichkham.db", null, 1);
        database.QueryData("CREATE TABLE IF NOT EXISTS bacsi(mabs VARCHAR(20) PRIMARY KEY, tenbs NVARCHAR(50), gioitinh NVARCHAR(20), namsinh Date, sdt varchar(15), chuyennganh NVARCHAR(200), tendn VARCHAR(20) NOT NULL, anh BLOB)");

        Loaddulieubacsi();


        dauconggocphai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent a = new Intent(getApplicationContext(), ThemThongTinBacSi_Activity.class);
                startActivity(a);
            }
        });
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Xác Nhận Xóa");
        builder.setMessage("Bạn có chắc chắn muốn xóa bác sĩ này?");
        builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Xóa dữ liệu từ cơ sở dữ liệu
                ThongTinBacSi selectedBS = mangBS.get(vitri);
                database.QueryData("DELETE FROM bacsi WHERE mabs = ?", selectedBS.getMabs());
                // Cập nhật lại danh sách
                Loaddulieubacsi();
            }
        });
        builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss(); // Đóng dialog nếu người dùng chọn "Không"
            }
        });
        builder.show();
    }

    private void Loaddulieubacsi() {
        Cursor dataCongViec = database.GetData("SELECT bacsi.* FROM bacsi INNER JOIN taikhoan ON bacsi.tendn = taikhoan.tendn");
        mangBS.clear();

        while (dataCongViec.moveToNext()) {
            String mabs = dataCongViec.getString(0);
            String tenbs = dataCongViec.getString(1);
            String gioitinh = dataCongViec.getString(2);
            String namsinh = dataCongViec.getString(3);
            String sdt = dataCongViec.getString(4);
            String chuyennganh = dataCongViec.getString(5);
            String tdn = dataCongViec.getString(6);
            byte[] blob = dataCongViec.getBlob(7); // Lấy mảng byte từ cột chứa ảnh
            mangBS.add(new ThongTinBacSi(mabs, tenbs, gioitinh, namsinh, sdt, chuyennganh, tdn, blob));
        }

        adapter.notifyDataSetChanged();
    }

    private byte[] convertBitmapToByteArray(int resourceId) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resourceId);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }
}