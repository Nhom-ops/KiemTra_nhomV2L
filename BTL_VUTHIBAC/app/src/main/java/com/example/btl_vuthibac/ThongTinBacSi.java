package com.example.btl_vuthibac;

import android.graphics.Bitmap;

import java.util.Date;

public class ThongTinBacSi {
    private String mabs;
    private String tenbs;
    private String gioitinh;
    private String namsinh;
    private String sdt;
    private String chuyennganh;
    private String tendn;

    private byte[] anh; // Thay đổi thành byte[]

    // Constructor
    public ThongTinBacSi(String mabs, String tenbs, String gioitinh, String namsinh,
                         String sdt, String chuyennganh, String tendn, byte[] anh) {
        this.mabs = mabs;
        this.tenbs = tenbs;
        this.gioitinh = gioitinh;
        this.namsinh = namsinh;
        this.sdt = sdt;
        this.chuyennganh = chuyennganh;
        this.tendn = tendn;
        this.anh = anh; // Gán byte[] vào thuộc tính
    }
    // Constructor cho 3 thuộc tính
    public ThongTinBacSi(String tenbs,  String chuyennganh, byte[] anh) {
        this.tenbs = tenbs;

        this.chuyennganh = chuyennganh;
        this.anh = anh; // Gán byte[] vào thuộc tính
    }

    //Constructor cho 6 thuộc tính
    public ThongTinBacSi( String tenbs, String gioitinh, String namsinh,
                         String sdt, String chuyennganh,  byte[] anh) {

        this.tenbs = tenbs;
        this.gioitinh = gioitinh;
        this.namsinh = namsinh;
        this.sdt = sdt;
        this.chuyennganh = chuyennganh;

        this.anh = anh; // Gán byte[] vào thuộc tính
    }
    public String getTenbs() {
        return tenbs;
    }

    public void setTenbs(String tenbs) {
        this.tenbs = tenbs;
    }

    public byte[] getAnh() {
        return anh;
    }

    public void setAnh(byte[] anh) {
        this.anh = anh;
    }

    public String getMabs() {
        return mabs;
    }

    public void setMabs(String mabs) {
        this.mabs = mabs;
    }



    public String getGioitinh() {
        return gioitinh;
    }

    public void setGioitinh(String gioitinh) {
        this.gioitinh = gioitinh;
    }

    public String getNamsinh() {
        return namsinh;
    }

    public void setNamsinh(String namsinh) {
        this.namsinh = namsinh;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getChuyennganh() {
        return chuyennganh;
    }

    public void setChuyennganh(String chuyennganh) {
        this.chuyennganh = chuyennganh;
    }

    public String getTendn() {
        return tendn;
    }

    public void setTendn(String tendn) {
        this.tendn = tendn;
    }
}
