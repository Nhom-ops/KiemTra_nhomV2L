package com.example.btl_vuthibac;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
import java.util.ArrayList;
import java.util.Calendar;

public class ThongTinBacSiAdapter extends BaseAdapter {
    private Context context;
    private Uri selectedImageUri; // Biến lưu trữ URI đã chọn
    private static final int REQUEST_CODE_PICK_IMAGE = 1; // Định nghĩa mã yêu cầu
    private ArrayList<ThongTinBacSi> bacsiList;
    private boolean showFullDetails; // Biến để xác định xem có hiển thị 7 thuộc tính hay không
    private Database database;

    public ThongTinBacSiAdapter(Context context, ArrayList<ThongTinBacSi> bacsiList, boolean showFullDetails) {
        this.context = context;
        this.bacsiList = bacsiList;
        this.showFullDetails = showFullDetails; // Khởi tạo biến
        this.database = new Database(context, "datlichkham.db", null, 1);
    }

    @Override
    public int getCount() {
        return bacsiList.size();
    }

    @Override
    public Object getItem(int position) {
        return bacsiList.get(position);
    }

    public void setSelectedImageUri(Uri uri) {
        this.selectedImageUri = uri; // Setter để cập nhật URI
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (showFullDetails) {
            return getViewWithSevenProperties(position, convertView, parent);
        } else {
            return getViewWithFourProperties(position, convertView, parent);
        }
    }

    // Hiển thị danh sách chuyên ngành
    private void showSpecializationDialog(TextView chuyennganh) {
        String[] specializations = {"Ngoại Khoa", "Nội Khoa", "Chuyên Khoa 1", "Chuyên Khoa 2"};

        AlertDialog.Builder builder = new AlertDialog.Builder(chuyennganh.getContext());
        builder.setTitle("Chọn Chuyên Ngành")
                .setItems(specializations, (dialog, which) -> {
                    chuyennganh.setText(specializations[which]); // Cập nhật TextView
                });
        builder.show();
    }

    // Hiển thị DatePicker
    private void showDatePickerDialog(EditText editText) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                editText.getContext(),
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String date = selectedYear + "-" + (selectedMonth + 1) + "-" + selectedDay; // Định dạng ngày
                    editText.setText(date); // Cập nhật EditText
                },
                year, month, day
        );
        datePickerDialog.show();
    }

    public View getViewWithSevenProperties(int i, View view, ViewGroup parent) {
        View viewtemp;
        if (view == null) {
            viewtemp = LayoutInflater.from(parent.getContext()).inflate(R.layout.ds_bacsi, parent, false);
        } else {
            viewtemp = view;
        }

        ThongTinBacSi tt = bacsiList.get(i);
        TextView mabs = viewtemp.findViewById(R.id.mbs1);
        TextView hoten = viewtemp.findViewById(R.id.ht1);
        TextView gioitinh = viewtemp.findViewById(R.id.gt1);
        TextView nams = viewtemp.findViewById(R.id.ns1);
        TextView sdt = viewtemp.findViewById(R.id.sdt1);
        TextView chuyennganh = viewtemp.findViewById(R.id.cn1);
        TextView tendn = viewtemp.findViewById(R.id.tdn1);
        ImageView anh = viewtemp.findViewById(R.id.imgbs);
        ImageButton sua = viewtemp.findViewById(R.id.imgsua);
        ImageButton xoa = viewtemp.findViewById(R.id.imgxoa);

        // Hiển thị thông tin bác sĩ
        mabs.setText(tt.getMabs());
        hoten.setText(tt.getTenbs());
        gioitinh.setText(tt.getGioitinh());
        nams.setText(tt.getNamsinh());
        sdt.setText(tt.getSdt());
        chuyennganh.setText(tt.getChuyennganh());
        tendn.setText(tt.getTendn());

        // Hiển thị ảnh bác sĩ
        byte[] anhByteArray = tt.getAnh();
        if (anhByteArray != null && anhByteArray.length > 0) {
            Bitmap imganhbs = BitmapFactory.decodeByteArray(anhByteArray, 0, anhByteArray.length);
            anh.setImageBitmap(imganhbs);
        } else {
            anh.setImageResource(R.drawable.imgmacdinh);
        }

        // Sự kiện cho nút "Sửa"
        sua.setOnClickListener(view1 -> showEditDialog(tt));

        // Sự kiện cho nút "Xóa"
        xoa.setOnClickListener(v -> {
            new AlertDialog.Builder(parent.getContext())
                    .setTitle("Xác nhận")
                    .setMessage("Bạn có chắc chắn muốn xóa bác sĩ này?")
                    .setPositiveButton("Có", (dialog, which) -> {
                        SQLiteDatabase db = database.getWritableDatabase();
                        int rowsAffected = db.delete("bacsi", "mabs = ?", new String[]{tt.getMabs()});
                        if (rowsAffected > 0) {
                            bacsiList.remove(i);
                            notifyDataSetChanged(); // Cập nhật giao diện
                            Toast.makeText(parent.getContext(), "Xóa bác sĩ thành công", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(parent.getContext(), "Không tìm thấy bác sĩ để xóa", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Không", (dialog, which) -> dialog.dismiss())
                    .show();
        });

        return viewtemp;
    }

    // Hàm hiển thị dialog sửa thông tin bác sĩ
    private void showEditDialog(ThongTinBacSi tt) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Chỉnh sửa thông tin bác sĩ");

        View dialogView = LayoutInflater.from(context).inflate(R.layout.activity_sua_thong_tin_bac_si, null);
        builder.setView(dialogView);

        // Các trường EditText và RadioButton
        EditText editMabs = dialogView.findViewById(R.id.mabs);
        EditText editHoten = dialogView.findViewById(R.id.tenbs);
        EditText editSdt = dialogView.findViewById(R.id.sdt1);
        RadioButton radioNam = dialogView.findViewById(R.id.nam);
        RadioButton radioNu = dialogView.findViewById(R.id.nu);
        EditText editNamsinh = dialogView.findViewById(R.id.ns1);
        EditText editChuyennganh = dialogView.findViewById(R.id.chuyennganh);
        EditText editTendn = dialogView.findViewById(R.id.tdn1);
        ImageView imgBacSi = dialogView.findViewById(R.id.imgbs);

        // Sự kiện click cho năm sinh
        editNamsinh.setOnClickListener(view -> showDatePickerDialog(editNamsinh));

        // Sự kiện click cho chuyên ngành
        editChuyennganh.setOnClickListener(view -> showSpecializationDialog(editChuyennganh));

        // Điền dữ liệu hiện tại vào các trường
        editMabs.setText(tt.getMabs());
        editHoten.setText(tt.getTenbs());
        editNamsinh.setText(tt.getNamsinh());
        editChuyennganh.setText(tt.getChuyennganh());
        editTendn.setText(tt.getTendn());
        editSdt.setText(tt.getSdt());

        // Cài đặt giới tính
        if (tt.getGioitinh().equalsIgnoreCase("nam")) {
            radioNam.setChecked(true);
        } else {
            radioNu.setChecked(true);
        }

        // Hiển thị ảnh bác sĩ
        byte[] anhByteArray = tt.getAnh();
        if (anhByteArray != null && anhByteArray.length > 0) {
            Bitmap imganhbs = BitmapFactory.decodeByteArray(anhByteArray, 0, anhByteArray.length);
            imgBacSi.setImageBitmap(imganhbs);
        } else {
            imgBacSi.setImageResource(R.drawable.imgmacdinh);
        }

        // Sự kiện chọn ảnh từ bộ nhớ
        imgBacSi.setOnClickListener(imgView -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            ((Activity) context).startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
        });

        // Sự kiện chọn ảnh từ drawable
        Button imgAddanh = dialogView.findViewById(R.id.btnAddImg);
        imgAddanh.setOnClickListener(v1 -> openDrawableImagePicker(imgBacSi));

        builder.setPositiveButton("Lưu", (dialog, which) -> {
            // Cập nhật thông tin bác sĩ
            updateBacSi(tt, editMabs, editHoten, radioNam, editSdt, editNamsinh, editChuyennganh, editTendn);
        });

        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    // Phương thức cập nhật thông tin bác sĩ
    private void updateBacSi(ThongTinBacSi tt, EditText editMabs, EditText editHoten, RadioButton radioNam,
                             EditText editSdt, EditText editNamsinh, EditText editChuyennganh, EditText editTendn) {
        String newMabs = editMabs.getText().toString().trim();
        String newHoten = editHoten.getText().toString().trim();
        String newGioitinh = radioNam.isChecked() ? "Nam" : "Nữ";
        String newNamsinh = editNamsinh.getText().toString().trim();
        String newSdt = editSdt.getText().toString().trim();
        String newChuyennganh = editChuyennganh.getText().toString().trim();
        String newTendn = editTendn.getText().toString().trim();

        // Cập nhật ảnh nếu có
        byte[] newAnh = selectedImageUri != null ? getBytesFromUri(selectedImageUri) : null;

        // Cập nhật vào cơ sở dữ liệu
        SQLiteDatabase db = database.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("mabs", newMabs);
        values.put("tenbs", newHoten);
        values.put("gioitinh", newGioitinh);
        values.put("sdt", newSdt);
        values.put("namsinh", newNamsinh);
        values.put("chuyennganh", newChuyennganh);
        values.put("tendn", newTendn);
        if (newAnh != null) {
            values.put("anh", newAnh); // Cập nhật ảnh nếu có
        }

        // Cập nhật dữ liệu
        db.update("bacsi", values, "mabs = ?", new String[]{tt.getMabs()});

        // Cập nhật đối tượng ThongTinBacSi
        tt.setMabs(newMabs);
        tt.setTenbs(newHoten);
        tt.setGioitinh(newGioitinh);
        tt.setSdt(newSdt);
        tt.setNamsinh(newNamsinh);
        tt.setChuyennganh(newChuyennganh);
        tt.setTendn(newTendn);
        if (newAnh != null) {
            tt.setAnh(newAnh); // Cập nhật ảnh nếu có
        }

        notifyDataSetChanged(); // Cập nhật giao diện
    }

    // Phương thức để mở hộp thoại chọn ảnh từ drawable
    private void openDrawableImagePicker(ImageView imgBacSi) {
        final String[] imageNames = {"bs1", "bs2", "bs3", "bs4", "bs5", "bs6", "bs7", "bs8", "bs9"};

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Chọn ảnh từ drawable");
        builder.setItems(imageNames, (dialog, which) -> {
            // Lấy tên hình ảnh đã chọn
            String selectedImageName = imageNames[which];

            // Lấy ID tài nguyên drawable
            int resourceId = context.getResources().getIdentifier(selectedImageName, "drawable", context.getPackageName());

            // Cập nhật ImageView
            imgBacSi.setImageResource(resourceId);

            // Cập nhật URI
            selectedImageUri = Uri.parse("android.resource://" + context.getPackageName() + "/" + resourceId);
        });
        builder.show();
    }

    public View getViewWithFourProperties(int i, View view, ViewGroup parent) {
        View viewtemp;
        if (view == null) {
            viewtemp = LayoutInflater.from(parent.getContext()).inflate(R.layout.ds_hienthibacsi_view_nguoidung, parent, false);
        } else {
            viewtemp = view;
        }

        ThongTinBacSi tt = bacsiList.get(i);
        TextView mabs = viewtemp.findViewById(R.id.mbs1);
        TextView hoten = viewtemp.findViewById(R.id.ht1);
        TextView gioitinh = viewtemp.findViewById(R.id.gt1);
        TextView nams = viewtemp.findViewById(R.id.ns1);
        TextView sdt = viewtemp.findViewById(R.id.sdt1);
        TextView chuyennganh = viewtemp.findViewById(R.id.cn1);
        TextView tendn = viewtemp.findViewById(R.id.tdn1);
        ImageView anh = viewtemp.findViewById(R.id.imgbs);

        mabs.setText(tt.getMabs());
        hoten.setText(tt.getTenbs());
        gioitinh.setText(tt.getGioitinh());
        nams.setText(tt.getNamsinh());
        sdt.setText(tt.getSdt());
        chuyennganh.setText(tt.getChuyennganh());
        tendn.setText(tt.getTendn());

        // Hiển thị ảnh bác sĩ
        byte[] anhByteArray = tt.getAnh();
        if (anhByteArray != null && anhByteArray.length > 0) {
            Bitmap imganhbs = BitmapFactory.decodeByteArray(anhByteArray, 0, anhByteArray.length);
            anh.setImageBitmap(imganhbs);
        } else {
            anh.setImageResource(R.drawable.imgmacdinh);
        }

        // Thêm sự kiện click để chuyển đến trang chi tiết
        viewtemp.setOnClickListener(v -> {
            Intent intent = new Intent(parent.getContext(), ChiTietBacSi_Activity.class);
            ChiTietBacSi chiTietBacSi = new ChiTietBacSi(
                    tt.getMabs(),
                    tt.getTenbs(),
                    tt.getGioitinh(),
                    tt.getNamsinh(),
                    tt.getSdt(),
                    tt.getChuyennganh(),
                    tt.getTendn(),
                    tt.getAnh()
            );
            intent.putExtra("chitietbacsi", chiTietBacSi); // Truyền đối tượng ChiTietBacSi
            parent.getContext().startActivity(intent);
        });

        return viewtemp;
    }

    // Chuyển đổi URI thành mảng byte
    private byte[] getBytesFromUri(Uri uri) {
        if (uri == null) {
            return null; // Trả về null nếu URI không hợp lệ
        }
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
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
}