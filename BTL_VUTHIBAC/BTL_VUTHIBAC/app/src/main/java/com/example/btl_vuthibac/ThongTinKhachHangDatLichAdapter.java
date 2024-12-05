package com.example.btl_vuthibac;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ThongTinKhachHangDatLichAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<ThongTinKhachHangDatLich> datlichkhamList;
    private boolean showFullDetails;
    private Database database; // Add Database reference

    public ThongTinKhachHangDatLichAdapter(Context context, ArrayList<ThongTinKhachHangDatLich> bacsiList, boolean showFullDetails) {
        this.context = context;
        this.datlichkhamList = bacsiList;
        this.showFullDetails = showFullDetails;
        this.database = new Database(context, "datlichkham.db", null, 1);
    }

    @Override
    public int getCount() {
        return datlichkhamList.size();
    }

    @Override
    public Object getItem(int position) {
        return datlichkhamList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (showFullDetails) {
            return getViewWithTenProperties(position, convertView, parent);
        } else {
            return getViewWithTen1Properties(position, convertView, parent);
        }
    }

    public View getViewWithTenProperties(int i, View view, ViewGroup viewGroup) {
        View viewtemp;
        if (view == null) {
            viewtemp = View.inflate(viewGroup.getContext(), R.layout.ds_thongtinkhachhangdatlich, null);
        } else {
            viewtemp = view;
        }

        ThongTinKhachHangDatLich tt = datlichkhamList.get(i);

        // Ánh xạ TextView
        TextView mabs = viewtemp.findViewById(R.id.mabs);
        TextView hotenbenhnhan = viewtemp.findViewById(R.id.ht1);
        TextView diachi = viewtemp.findViewById(R.id.dc);
        TextView sdt = viewtemp.findViewById(R.id.sdt);
        TextView dichvukham = viewtemp.findViewById(R.id.dvk);
        TextView giomuonkham = viewtemp.findViewById(R.id.gmk);
        TextView tendn = viewtemp.findViewById(R.id.tendn);
        TextView ngaymuonkham = viewtemp.findViewById(R.id.nk);
        TextView madatlich = viewtemp.findViewById(R.id.mdl);
        TextView tongtienthanhtoan = viewtemp.findViewById(R.id.ttt);

        // Cập nhật dữ liệu với kiểm tra null
        mabs.setText(tt.getMabs() != null ? tt.getMabs() : "");
        hotenbenhnhan.setText(tt.getHoten() != null ? tt.getHoten() : "");
        sdt.setText(tt.getSdt() != null ? tt.getSdt() : "");
        diachi.setText(tt.getDiachi() != null ? tt.getDiachi() : "");
        giomuonkham.setText(tt.getGiokham() != null ? tt.getGiokham() : "");
        ngaymuonkham.setText(tt.getNgaykham() != null ? tt.getNgaykham() : "");
        dichvukham.setText(tt.getDichvu() != null ? tt.getDichvu() : "");
        madatlich.setText(tt.getMaso() != null ? tt.getMaso() : "");
        tongtienthanhtoan.setText(tt.getTienthanhtoan() != null ? tt.getTienthanhtoan() : "");
        tendn.setText(tt.getTendn() != null ? tt.getTendn() : "");

        return viewtemp;
    }

    public View getViewWithTen1Properties(int i, View view, ViewGroup viewGroup) {
        View viewtemp;
        if (view == null) {
            viewtemp = View.inflate(viewGroup.getContext(), R.layout.ds_thongtndatlichtheo_user, null);
        } else {
            viewtemp = view;
        }

        ThongTinKhachHangDatLich tt = datlichkhamList.get(i);

        // Ánh xạ TextView
        TextView mabs = viewtemp.findViewById(R.id.mabs);
        TextView hotenbenhnhan = viewtemp.findViewById(R.id.ht1);
        TextView diachi = viewtemp.findViewById(R.id.dc);
        TextView sdt = viewtemp.findViewById(R.id.sdt);
        TextView dichvukham = viewtemp.findViewById(R.id.dvk);
        TextView giomuonkham = viewtemp.findViewById(R.id.gmk);
        TextView tendn = viewtemp.findViewById(R.id.tendn);
        TextView ngaymuonkham = viewtemp.findViewById(R.id.nk);
        TextView madatlich = viewtemp.findViewById(R.id.mdl);
        TextView tongtienthanhtoan = viewtemp.findViewById(R.id.ttt);
        Button huylich = viewtemp.findViewById(R.id.btnHuy);

        // Cập nhật dữ liệu với kiểm tra null
        mabs.setText(tt.getMabs() != null ? tt.getMabs() : "");
        hotenbenhnhan.setText(tt.getHoten() != null ? tt.getHoten() : "");
        sdt.setText(tt.getSdt() != null ? tt.getSdt() : "");
        diachi.setText(tt.getDiachi() != null ? tt.getDiachi() : "");
        giomuonkham.setText(tt.getGiokham() != null ? tt.getGiokham() : "");
        ngaymuonkham.setText(tt.getNgaykham() != null ? tt.getNgaykham() : "");
        dichvukham.setText(tt.getDichvu() != null ? tt.getDichvu() : "");
        madatlich.setText(tt.getMaso() != null ? tt.getMaso() : "");
        tongtienthanhtoan.setText(tt.getTienthanhtoan() != null ? tt.getTienthanhtoan() : "");
        tendn.setText(tt.getTendn() != null ? tt.getTendn() : "");

        // Set click listener for the button
        huylich.setOnClickListener(v -> {
            String maDatLich = tt.getMaso(); // Get the unique identifier
            if (maDatLich != null) {
                database.deleteData(maDatLich); // Call the delete method
                Toast.makeText(viewGroup.getContext(), "Đã hủy lịch", Toast.LENGTH_SHORT).show();

                // Remove from the list and notify the adapter
                datlichkhamList.remove(i);
                notifyDataSetChanged(); // Ensure your adapter has this method
            }
        });

        return viewtemp;
    }

}