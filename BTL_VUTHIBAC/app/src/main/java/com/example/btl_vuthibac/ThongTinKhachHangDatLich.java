package com.example.btl_vuthibac;

public class ThongTinKhachHangDatLich {
    String maso,tendn,mabs,hoten,sdt,diachi,giokham,ngaykham,dichvu,tienthanhtoan;

    public ThongTinKhachHangDatLich(String maso,  String hoten, String sdt, String diachi, String giokham, String ngaykham, String dichvu, String tienthanhtoan,String tendn, String mabs) {
        this.maso = maso;

        this.hoten = hoten;
        this.sdt = sdt;
        this.diachi = diachi;
        this.giokham = giokham;
        this.ngaykham = ngaykham;
        this.dichvu = dichvu;
        this.tienthanhtoan = tienthanhtoan;
        this.tendn = tendn;
        this.mabs = mabs;
    }

    public String getMaso() {
        return maso;
    }

    public void setMaso(String maso) {
        this.maso = maso;
    }

    public String getTendn() {
        return tendn;
    }

    public void setTendn(String tendn) {
        this.tendn = tendn;
    }

    public String getMabs() {
        return mabs;
    }

    public void setMabs(String mabs) {
        this.mabs = mabs;
    }

    public String getHoten() {
        return hoten;
    }

    public void setHoten(String hoten) {
        this.hoten = hoten;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getDiachi() {
        return diachi;
    }

    public void setDiachi(String diachi) {
        this.diachi = diachi;
    }

    public String getGiokham() {
        return giokham;
    }

    public void setGiokham(String giokham) {
        this.giokham = giokham;
    }

    public String getNgaykham() {
        return ngaykham;
    }

    public void setNgaykham(String ngaykham) {
        this.ngaykham = ngaykham;
    }

    public String getDichvu() {
        return dichvu;
    }

    public void setDichvu(String dichvu) {
        this.dichvu = dichvu;
    }

    public String getTienthanhtoan() {
        return tienthanhtoan;
    }

    public void setTienthanhtoan(String tienthanhtoan) {
        this.tienthanhtoan = tienthanhtoan;
    }
}
