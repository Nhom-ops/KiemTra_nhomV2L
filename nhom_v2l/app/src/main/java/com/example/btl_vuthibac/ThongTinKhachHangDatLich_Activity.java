package com.example.btl_vuthibac;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;

public class ThongTinKhachHangDatLich_Activity extends AppCompatActivity {
    EditText ngaykham;


    ArrayList<ThongTinKhachHangDatLich> mangDL;
    ThongTinKhachHangDatLichAdapter adapter;
    EditText giokham;
    EditText dichvu;
    TextView tongtienthanhtoan;
    EditText currentEditText; // Biến để lưu ô EditText hiện tại
    // Mảng dịch vụ và mảng giá tương ứng
    String[] dichVuArray = {"Khám Tổng Quát", "Chụp X- Quang", "Xét Nghiệm Máu"};
    String[] giaArray = {"100000", "200000", "300000"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_tin_khach_hang_dat_lich);
     dichvu = findViewById(R.id.dichvu);
       tongtienthanhtoan = findViewById(R.id.tongtienthanhtoan);


        dichvu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDichVuDialog();
            }
        });
        ngaykham = findViewById(R.id.ngaykham);
        giokham = findViewById(R.id.giokham);

        ngaykham.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentEditText = ngaykham; // Lưu ô EditText hiện tại
                showDatePickerDialog();
            }
        });

        giokham.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentEditText = giokham; // Lưu ô EditText hiện tại
                showTimePickerDialog();
            }
        });
    }
    private void showDichVuDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ThongTinKhachHangDatLich_Activity.this);
        builder.setTitle("Chọn Dịch Vụ");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dichVuArray);
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Khi chọn dịch vụ
                dichvu.setText(dichVuArray[which]);

                // Chuyển đổi giá từ chuỗi sang số và hiển thị
                String gia = giaArray[which];
               tongtienthanhtoan.setText(gia + " VND"); // Thêm đơn vị tiền tệ
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

        DatePickerDialog datePickerDialog = new DatePickerDialog(ThongTinKhachHangDatLich_Activity.this,
                new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                        Calendar selectedDate = Calendar.getInstance();
                        selectedDate.set(selectedYear, selectedMonth, selectedDay);

                        Calendar currentDate = Calendar.getInstance();

                        // Kiểm tra xem ngày chọn có lớn hơn hoặc bằng ngày hiện tại không
                        if (selectedDate.compareTo(currentDate) >= 0) {
                            ngaykham.setText(selectedYear + "-" + (selectedMonth + 1) + "-" + selectedDay);
                        } else {
                            // Hiển thị thông báo nếu ngày không hợp lệ
                            Toast.makeText(ThongTinKhachHangDatLich_Activity.this, "Ngày khám phải lớn hơn hoặc bằng ngày hôm nay!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, year, month, day);
        datePickerDialog.show();
    }


    private void showTimePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(ThongTinKhachHangDatLich_Activity.this,
                new TimePickerDialog.OnTimeSetListener() {
                    public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {
                        // Kiểm tra giờ đã chọn
                        if ((selectedHour >= 7 && selectedHour < 11) || (selectedHour >= 13 && selectedHour < 17)) {
                            // Chuyển đổi giờ sang định dạng 12 giờ
                            String timeFormat = String.format("%02d:%02d %s",
                                    selectedHour % 12 == 0 ? 12 : selectedHour % 12,
                                    selectedMinute,
                                    selectedHour < 12 ? "AM" : "PM");
                            giokham.setText(timeFormat);
                        } else {
                            // Hiển thị thông báo nếu giờ không hợp lệ
                            Toast.makeText(ThongTinKhachHangDatLich_Activity.this, "Giờ khám chỉ được từ 7h đến 11h và từ 13h đến 17h!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, hour, minute, false); // false cho định dạng 12 giờ
        timePickerDialog.show();
    }

}
