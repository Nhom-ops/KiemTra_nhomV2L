package com.example.btl_vuthibac;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ChiTietBacSiAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<ThongTinBacSi> bacsiList;
    private boolean showFullDetails; // Biến để xác định xem có hiển thị 7 thuộc tính hay không

    public ChiTietBacSiAdapter(Context context, ArrayList<ThongTinBacSi> bacsiList, boolean showFullDetails) {
        this.context = context;
        this.bacsiList = bacsiList;
        this.showFullDetails = showFullDetails; // Khởi tạo biến
    }

    @Override
    public int getCount() {
        return bacsiList.size();
    }

    @Override
    public Object getItem(int position) {
        return bacsiList.get(position);
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

    public View getViewWithSevenProperties(int i, View view, ViewGroup viewGroup) {
        // Thực hiện việc hiển thị 7 thuộc tính
        View viewtemp;
        if (view == null) {
            viewtemp = View.inflate(viewGroup.getContext(), R.layout.ds_bacsi, null);
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

        // Kiểm tra namsinh có khác null không

        nams.setText(tt.getNamsinh()); // Hiển thị ngày sinh

        sdt.setText(tt.getSdt());
        chuyennganh.setText(tt.getChuyennganh());
        tendn.setText(tt.getTendn());

        // Giả sử anh là byte[] trong ThongTinBacSi
        byte[] anhByteArray = tt.getAnh();
        if (anhByteArray != null && anhByteArray.length > 0) {
            Bitmap imganhbs = BitmapFactory.decodeByteArray(anhByteArray, 0, anhByteArray.length);
            anh.setImageBitmap(imganhbs);
        } else {
            anh.setImageResource(R.drawable.imgmacdinh);
        }

        return viewtemp;
    }


    public View getViewWithFourProperties(int i, View view, ViewGroup viewGroup) {
       //6 thuộc tính

        View viewtemp;
        if (view == null) {
            viewtemp = View.inflate(viewGroup.getContext(), R.layout.activity_chi_tiet_bac_si, null);
        } else {
            viewtemp = view;
        }

        ThongTinBacSi tt = bacsiList.get(i);

        TextView hoten = viewtemp.findViewById(R.id.ht1);
        TextView gioitinh = viewtemp.findViewById(R.id.gt1);
        TextView nams = viewtemp.findViewById(R.id.ns1);
        TextView sdt = viewtemp.findViewById(R.id.sdt1);
        TextView chuyennganh = viewtemp.findViewById(R.id.cn1);
        ImageView anh = viewtemp.findViewById(R.id.imgbs);

        hoten.setText(tt.getTenbs());
        gioitinh.setText(tt.getGioitinh());
        nams.setText(tt.getNamsinh());
        sdt.setText(tt.getSdt());
        chuyennganh.setText(tt.getChuyennganh());

        // Hiển thị ảnh
        byte[] anhByteArray = tt.getAnh();
        if (anhByteArray != null && anhByteArray.length > 0) {
            Bitmap imganhbs = BitmapFactory.decodeByteArray(anhByteArray, 0, anhByteArray.length);
            anh.setImageBitmap(imganhbs);
        } else {
            anh.setImageResource(R.drawable.imgmacdinh);
        }

        // Thêm sự kiện click để chuyển đến trang chi tiết
        viewtemp.setOnClickListener(v -> {
            Intent intent = new Intent(viewGroup.getContext(), ChiTietBacSi_Activity.class);
            // Truyền toàn bộ đối tượng ChiTietBacSi thay vì chỉ truyền mabs
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
            viewGroup.getContext().startActivity(intent);
        });

        return viewtemp;
    }
}

