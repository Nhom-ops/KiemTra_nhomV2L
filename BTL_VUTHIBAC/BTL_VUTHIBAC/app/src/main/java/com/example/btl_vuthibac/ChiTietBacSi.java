package com.example.btl_vuthibac;

import android.os.Parcel;
import android.os.Parcelable;

public class ChiTietBacSi implements Parcelable {
    private String mabs;
    private String tenbs;
    private String gioitinh;
    private String namsinh;
    private String sdt;
    private String chuyennganh;
    private String tendn;
    private byte[] anh;

    // Constructor
    public ChiTietBacSi(String mabs, String tenbs, String gioitinh, String namsinh,
                        String sdt, String chuyennganh, String tendn, byte[] anh) {
        this.mabs = mabs;
        this.tenbs = tenbs;
        this.gioitinh = gioitinh;
        this.namsinh = namsinh;
        this.sdt = sdt;
        this.chuyennganh = chuyennganh;
        this.tendn = tendn;
        this.anh = anh;
    }

    protected ChiTietBacSi(Parcel in) {
        mabs = in.readString();
        tenbs = in.readString();
        gioitinh = in.readString();
        namsinh = in.readString();
        sdt = in.readString();
        chuyennganh = in.readString();
        tendn = in.readString();
        anh = in.createByteArray();
    }
    //Constructor cho 6 thuộc tính
    public ChiTietBacSi( String tenbs, String gioitinh, String namsinh,
                         String sdt, String chuyennganh,  byte[] anh) {

        this.tenbs = tenbs;
        this.gioitinh = gioitinh;
        this.namsinh = namsinh;
        this.sdt = sdt;
        this.chuyennganh = chuyennganh;

        this.anh = anh; // Gán byte[] vào thuộc tính
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mabs);
        dest.writeString(tenbs);
        dest.writeString(gioitinh);
        dest.writeString(namsinh);
        dest.writeString(sdt);
        dest.writeString(chuyennganh);
        dest.writeString(tendn);
        dest.writeByteArray(anh);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ChiTietBacSi> CREATOR = new Creator<ChiTietBacSi>() {
        @Override
        public ChiTietBacSi createFromParcel(Parcel in) {
            return new ChiTietBacSi(in);
        }

        @Override
        public ChiTietBacSi[] newArray(int size) {
            return new ChiTietBacSi[size];
        }
    };

    // Getters và Setters
    public String getMabs() {
        return mabs;
    }

    public void setMabs(String mabs) {
        this.mabs = mabs;
    }

    public String getTenbs() {
        return tenbs;
    }

    public void setTenbs(String tenbs) {
        this.tenbs = tenbs;
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

    public byte[] getAnh() {
        return anh;
    }

    public void setAnh(byte[] anh) {
        this.anh = anh;
    }
}