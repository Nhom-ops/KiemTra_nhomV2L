package com.example.btl_vuthibac;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Calendar;

public class ThemLichDatKham_Activity extends AppCompatActivity {
    EditText ngaykham, giokham, dichvu, hoten, sdt1, diachi;
    TextView tongtienthanhtoan;
    Button themdatlich;
    Database database;
    String[] dichVuArray = {"Khám Tổng Quát", "Chụp X- Quang", "Xét Nghiệm Máu"};
    String[] giaArray = {"100000", "200000", "300000"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_lich_dat_kham);

        dichvu = findViewById(R.id.dichvu);
        tongtienthanhtoan = findViewById(R.id.tongtienthanhtoan);
        themdatlich = findViewById(R.id.btnDatngay);
        hoten = findViewById(R.id.hoten);
        sdt1 = findViewById(R.id.sdt);
        diachi = findViewById(R.id.diachi);
        ngaykham = findViewById(R.id.ngaykham);
        giokham = findViewById(R.id.giokham);

        database = new Database(this, "datlichkham.db", null, 1);
        database.QueryData("CREATE TABLE IF NOT EXISTS thongtinlichdatkham(maso INTEGER PRIMARY KEY AUTOINCREMENT, hoten NVARCHAR(50), sdt VARCHAR(15), diachi TEXT, giomuonkham VARCHAR(15), ngaymuonkham VARCHAR(30), dichvukham NVARCHAR(200) NOT NULL, tongtien VARCHAR(50), tendn VARCHAR(20), mabs VARCHAR(20))");

        themdatlich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                themdatlich();
            }
        });

        dichvu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDichVuDialog();
            }
        });

        ngaykham.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });

        giokham.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePickerDialog();
            }
        });

        // Nhận dữ liệu từ Intent
        Intent intent = getIntent();
        String tendn = intent.getStringExtra("tendn");
        String mabs = intent.getStringExtra("mabs");

        // Kiểm tra giá trị của tendn và mabs
        Log.d("ThemLichDatKham", "Tên đăng nhập: " + tendn);
        Log.d("ThemLichDatKham", "Mã bác sĩ: " + mabs);

        if (tendn == null) {
            Toast.makeText(this, "Không nhận được tên đăng nhập!", Toast.LENGTH_SHORT).show();
        }
        if (mabs == null) {
            Toast.makeText(this, "Không nhận được mã bác sĩ!", Toast.LENGTH_SHORT).show();
        }
    }

    private void showDichVuDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ThemLichDatKham_Activity.this);
        builder.setTitle("Chọn Dịch Vụ");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dichVuArray);
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dichvu.setText(dichVuArray[which]);
                String gia = giaArray[which];
                tongtienthanhtoan.setText(gia + " VND");
            }
        });

        builder.setNegativeButton("Hủy", null);
        builder.show();
    }

    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(ThemLichDatKham_Activity.this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    Calendar selectedDate = Calendar.getInstance();
                    selectedDate.set(selectedYear, selectedMonth, selectedDay);
                    Calendar currentDate = Calendar.getInstance();

                    if (selectedDate.compareTo(currentDate) >= 0) {
                        ngaykham.setText(selectedYear + "-" + (selectedMonth + 1) + "-" + selectedDay);
                    } else {
                        Toast.makeText(ThemLichDatKham_Activity.this, "Ngày khám phải lớn hơn hoặc bằng ngày hôm nay!", Toast.LENGTH_SHORT).show();
                    }
                }, year, month, day);
        datePickerDialog.show();
    }

    private void showTimePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(ThemLichDatKham_Activity.this,
                (view, selectedHour, selectedMinute) -> {
                    if ((selectedHour >= 7 && selectedHour < 11) || (selectedHour >= 13 && selectedHour < 17)) {
                        String timeFormat = String.format("%02d:%02d %s",
                                selectedHour % 12 == 0 ? 12 : selectedHour % 12,
                                selectedMinute,
                                selectedHour < 12 ? "AM" : "PM");
                        giokham.setText(timeFormat);
                    } else {
                        Toast.makeText(ThemLichDatKham_Activity.this, "Giờ khám chỉ được từ 7h đến 11h và từ 13h đến 17h!", Toast.LENGTH_SHORT).show();
                    }
                }, hour, minute, false);
        timePickerDialog.show();
    }

    private void themdatlich() {
        String tenDichVu = dichvu.getText().toString();
        String gioKham = giokham.getText().toString();
        String ngayKham = ngaykham.getText().toString();
        String tongTien = tongtienthanhtoan.getText().toString().replace(" VND", "");
        String hoTen = hoten.getText().toString();
        String sdt = sdt1.getText().toString();
        String diaChi = diachi.getText().toString();

        Intent intent = getIntent();
        String tendn = intent.getStringExtra("tendn");
        String mabs = intent.getStringExtra("mabs");

        if (hoTen.trim().isEmpty() || sdt.trim().isEmpty() || diaChi.trim().isEmpty() || tenDichVu.trim().isEmpty() || gioKham.trim().isEmpty() || ngayKham.trim().isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kiểm tra và log lại tendn và mabs trước khi lưu
        Log.d("ThemLichDatKham", "Đang lưu dữ liệu với Tên đăng nhập: " + tendn + ", Mã bác sĩ: " + mabs);

        database.QueryData("INSERT INTO thongtinlichdatkham(hoten, sdt, diachi, giomuonkham, ngaymuonkham, dichvukham, tongtien, tendn, mabs) VALUES ('"
                + hoTen + "', '" + sdt + "', '" + diaChi + "', '" + gioKham + "', '" + ngayKham + "', '" + tenDichVu + "', '" + tongTien + "', '"
                + tendn + "', '" + mabs + "')");

        Toast.makeText(this, "Đặt lịch thành công!", Toast.LENGTH_SHORT).show();
        Log.d("DataCount", "Số bản ghi: " + tongTien);
        Intent a = new Intent(getApplicationContext(), DanhsachLichKhamDaDat_Activity.class);
        startActivity(a);

        // Truyền dữ liệu
        Intent b = new Intent(getApplicationContext(), LichDatKhamTheoUser_Activity.class);
        b.putExtra("tendn", tendn); // Gửi tendn tới activity mới
        startActivity(b);
    }
}