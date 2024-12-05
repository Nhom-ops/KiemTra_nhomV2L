package com.example.btl_vuthibac;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class TrangCaNhan_Bacsi_Activity extends AppCompatActivity {
    Database database;
    TextView textMaBacSi; // Khai báo biến cho textMaBacSi

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trang_ca_nhan_bacsi);

        // Khai báo các thành phần giao diện
        Button dangxuat = findViewById(R.id.btndangxuat);
        TextView textTendn = findViewById(R.id.tendn);
        ImageButton btncanhan = findViewById(R.id.btncanhan);
        ImageButton btndatlich = findViewById(R.id.btnlichdat);
        ImageButton btntinnhan = findViewById(R.id.btntinnhan);
        TextView textHoTen = findViewById(R.id.ht1);
        textMaBacSi = findViewById(R.id.mbs1); // Khởi tạo textMaBacSi
        TextView textNamSinh = findViewById(R.id.ns1);
        TextView textSDT = findViewById(R.id.sdt1);
        TextView textChuyenNganh = findViewById(R.id.cn1);
        TextView textGioitinh = findViewById(R.id.gt1);
        ImageView imgBacSi = findViewById(R.id.imgbs);

        // Khởi tạo Database
        database = new Database(this, "datlichkham.db", null, 1);
        database.QueryData("CREATE TABLE IF NOT EXISTS bacsi(mabs VARCHAR(20) PRIMARY KEY, tenbs NVARCHAR(50), gioitinh NVARCHAR(20), namsinh DATE, sdt VARCHAR(15), chuyennganh NVARCHAR(200), tendn VARCHAR(20) NOT NULL, anh BLOB)");

        // Lấy tendn từ SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String tendn = sharedPreferences.getString("tendn", null);

        if (tendn != null) {
            textTendn.setText(tendn);
            loadBacSiData(tendn, textHoTen, textMaBacSi, textGioitinh, textNamSinh, textSDT, textChuyenNganh, imgBacSi);
        } else {
            redirectToLogin();
        }

        setupButtonListeners();
    }

    private void loadBacSiData(String tendn, TextView textHoTen, TextView textMaBacSi, TextView textGioitinh, TextView textNamSinh, TextView textSDT, TextView textChuyenNganh, ImageView imgBacSi) {
        Cursor cursor = database.GetData("SELECT bacsi.* FROM bacsi INNER JOIN taikhoan ON bacsi.tendn = taikhoan.tendn WHERE taikhoan.tendn = ?", new String[]{tendn});

        if (cursor != null && cursor.moveToFirst()) {
            int indexMabs = cursor.getColumnIndex("mabs");
            int indexHoTen = cursor.getColumnIndex("tenbs");
            int indexGioitinh = cursor.getColumnIndex("gioitinh");
            int indexNamSinh = cursor.getColumnIndex("namsinh");
            int indexSDT = cursor.getColumnIndex("sdt");
            int indexChuyenNganh = cursor.getColumnIndex("chuyennganh");
            int indexAnh = cursor.getColumnIndex("anh");

            if (indexMabs != -1) {
                String mabsValue = cursor.getString(indexMabs);
                textMaBacSi.setText(mabsValue);
//                Toast.makeText(this, "Mã bác sĩ: " + mabsValue, Toast.LENGTH_SHORT).show(); // Thay thế Log bằng Toast
            }

            if (indexHoTen != -1) {
                textHoTen.setText(cursor.getString(indexHoTen));
            }

            if (indexGioitinh != -1) {
                textGioitinh.setText(cursor.getString(indexGioitinh));
            }

            if (indexNamSinh != -1) {
                textNamSinh.setText(cursor.getString(indexNamSinh));
            }

            if (indexSDT != -1) {
                textSDT.setText(cursor.getString(indexSDT));
            }

            if (indexChuyenNganh != -1) {
                textChuyenNganh.setText(cursor.getString(indexChuyenNganh));
            }

            if (indexAnh != -1) {
                byte[] anhByteArray = cursor.getBlob(indexAnh);
                if (anhByteArray != null && anhByteArray.length > 0) {
                    Bitmap imganhbs = BitmapFactory.decodeByteArray(anhByteArray, 0, anhByteArray.length);
                    imgBacSi.setImageBitmap(imganhbs);
                } else {
                    imgBacSi.setImageResource(R.drawable.imgmacdinh);
                }
            } else {
                imgBacSi.setImageResource(R.drawable.imgmacdinh);
            }

            cursor.close();
        } else {
            redirectToLogin();
        }
    }

    private void redirectToLogin() {
        Intent intent = new Intent(getApplicationContext(), Login.class);
        startActivity(intent);
        finish();
    }

    private void setupButtonListeners() {
        Button dangxuat = findViewById(R.id.btndangxuat);
        ImageButton btncanhan = findViewById(R.id.btncanhan);
        ImageButton btndatlich = findViewById(R.id.btnlichdat);
        ImageButton btntinnhan = findViewById(R.id.btntinnhan);
        ImageButton btntrangchu = findViewById(R.id.btntrangchu);

        btndatlich.setOnClickListener(view -> {
            String mabs = textMaBacSi.getText().toString(); // Lấy mã bác sĩ từ TextView
//            Toast.makeText(this, "Mã bác sĩ (truyền đi): " + mabs, Toast.LENGTH_SHORT).show(); // Thay thế Log bằng Toast

            Intent intent = new Intent(getApplicationContext(), LichDatKhamTheoBacSi_Activity.class);
            intent.putExtra("mabs1", mabs); // Truyền mabs
            startActivity(intent); // Khởi động Activity mới
        });
        btntrangchu.setOnClickListener(view -> {
            SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
            boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);
            Intent intent = isLoggedIn ? new Intent(getApplicationContext(), TrangchuBacSi_Activity.class) : new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
        });

        btntinnhan.setOnClickListener(view -> {
            SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
            boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);

        });

        btncanhan.setOnClickListener(view -> {
            SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
            boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);
            Intent intent = isLoggedIn ? new Intent(getApplicationContext(), TrangCaNhan_Bacsi_Activity.class) : new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
        });

        dangxuat.setOnClickListener(v -> {
            new AlertDialog.Builder(TrangCaNhan_Bacsi_Activity.this)
                    .setTitle("Đăng Xuất")
                    .setMessage("Bạn có chắc chắn muốn đăng xuất?")
                    .setPositiveButton("Có", (dialog, which) -> {
                        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("isLoggedIn", false);
                        editor.putString("tendn", null);
                        editor.apply();

                        Intent intent = new Intent(getApplicationContext(), TrangchuBacSi_Activity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    })
                    .setNegativeButton("Không", null)
                    .show();
        });
    }
}