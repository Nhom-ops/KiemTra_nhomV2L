package com.example.btl_vuthibac;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ThemThongTinBacSi_Activity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1; // Mã yêu cầu cho việc chọn ảnh
    EditText ns, sdt, chuyennganh;
    Database database;
    ImageView imgbs;
    ArrayList<ThongTinBacSi> mangBS;
    ThongTinBacSiAdapter adapter;
    ImageButton back;
    private Uri imageUri; // Biến để lưu trữ URI của ảnh

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_thong_tin_bac_si);

        back = findViewById(R.id.imgback);
        ns = findViewById(R.id.ns1);
        EditText mabacsi = findViewById(R.id.mabs);
        EditText hoten = findViewById(R.id.tenbs);
        RadioButton nam = findViewById(R.id.nam);
        RadioButton nu = findViewById(R.id.nu);
        EditText tdn = findViewById(R.id.tdn1);
        sdt = findViewById(R.id.sdt1); // EditText cho số điện thoại
        chuyennganh = findViewById(R.id.chuyennganh); // EditText cho chuyên ngành
        imgbs = findViewById(R.id.imgbs);
        Button chonimgbs = findViewById(R.id.btnAddImg);
        Button btnthem = findViewById(R.id.btnadd);

        // Thiết lập OnClickListener cho EditText năm sinh
        ns.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });
chuyennganh.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        showSpecializationDialog(chuyennganh);
    }
});
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), TaiKhoan_Activity.class);
                startActivity(intent);
            }
        });

        mangBS = new ArrayList<>();
        adapter = new ThongTinBacSiAdapter(getApplicationContext(), mangBS, true);
        database = new Database(this, "datlichkham.db", null, 1);
        database.QueryData("CREATE TABLE IF NOT EXISTS bacsi(mabs VARCHAR(20) PRIMARY KEY, tenbs NVARCHAR(50), gioitinh NVARCHAR(20), namsinh TEXT, sdt VARCHAR(15), chuyennganh NVARCHAR(200), tendn VARCHAR(20) NOT NULL, anh BLOB)");

        // Thiết lập OnClickListener cho nút chọn ảnh
        chonimgbs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDrawableImagePicker(); // Gọi hàm mở hình ảnh từ drawable
            }
        });

        btnthem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Lấy dữ liệu từ các trường
                String maBS = mabacsi.getText().toString().trim();
                String tenBS = hoten.getText().toString().trim();
                String gioiTinh = nam.isChecked() ? "Nam" : "Nữ";
                String soDT = sdt.getText().toString().trim();
                String chuyenNganh = chuyennganh.getText().toString().trim();
                String tenDN = tdn.getText().toString().trim();

                // Lấy giá trị từ EditText ns
                String namsinh = ns.getText().toString().trim();

                // Kiểm tra dữ liệu không rỗng
                if (maBS.isEmpty() || tenBS.isEmpty() || namsinh.isEmpty() || soDT.isEmpty() || chuyenNganh.isEmpty() || tenDN.isEmpty()) {
                    Toast.makeText(ThemThongTinBacSi_Activity.this, "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Khởi tạo biến imageBytes
                byte[] imageBytes = null;

                // Kiểm tra imageUri có khác null không
                if (imageUri != null) {
                    imageBytes = getBytesFromUri(imageUri);
                    if (imageBytes == null) {
                        Toast.makeText(ThemThongTinBacSi_Activity.this, "Lỗi khi lấy ảnh!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                // Gọi phương thức QueryData
                database.QueryData("INSERT INTO bacsi VALUES(?, ?, ?, ?, ?, ?, ?, ?)",
                        maBS, tenBS, gioiTinh, namsinh, soDT, chuyenNganh, tenDN, imageBytes);
                Toast.makeText(ThemThongTinBacSi_Activity.this, "Thêm bác sĩ thành công!", Toast.LENGTH_LONG).show();

                // Chuyển đến Activity thứ hai
                Intent intent = new Intent(getApplicationContext(), TTBacSi_Activity.class);
                startActivity(intent);
                finish(); // Đóng Activity hiện tại
            }
        });
    }

    // Mở dialog chọn hình ảnh từ drawable
    private void openDrawableImagePicker() {
        final String[] imageNames = {"bs1", "bs2", "bs3", "bs4", "bs5", "bs6", "bs7", "bs8", "bs9"}; // Thay đổi tên hình ảnh theo tài nguyên của bạn

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Chọn ảnh từ drawable");
        builder.setItems(imageNames, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Lấy tên hình ảnh đã chọn
                String selectedImageName = imageNames[which];

                // Lấy ID tài nguyên drawable
                int resourceId = getResources().getIdentifier(selectedImageName, "drawable", getPackageName());

                // Cập nhật ImageView
                imgbs.setImageResource(resourceId);
                imageUri = Uri.parse("android.resource://" + getPackageName() + "/" + resourceId); // Cập nhật URI
            }
        });
        builder.show();
    }

    // Chọn ngày sinh
    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(ThemThongTinBacSi_Activity.this,
                new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                        ns.setText(selectedYear + "-" + (selectedMonth + 1) + "-" + selectedDay);
                    }
                }, year, month, day);
        datePickerDialog.show();
    }

    // Chuyển đổi URI thành mảng byte
    private byte[] getBytesFromUri(Uri uri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];
            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                byteBuffer.write(buffer, 0, len);
            }
            return byteBuffer.toByteArray(); // Trả về mảng byte của ảnh
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    private void showSpecializationDialog(TextView chuyennganh) {
        String[] specializations = {"Ngoại Khoa", "Nội Khoa", "Chuyên Khoa 1", "Chuyên Khoa 2"};

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(chuyennganh.getContext());
        builder.setTitle("Chọn Chuyên Ngành")
                .setItems(specializations, (dialog, which) -> {
                    chuyennganh.setText(specializations[which]); // Cập nhật TextView
                });
        builder.show();
    }
}