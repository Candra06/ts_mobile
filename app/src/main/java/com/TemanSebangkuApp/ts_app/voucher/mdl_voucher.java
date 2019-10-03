package com.TemanSebangkuApp.ts_app.voucher;

public class mdl_voucher {
    String voucher;
    String kode_voucher;
    String detail_voucher;
    int status;
    String gambar;

    public mdl_voucher(){
        this.voucher = voucher;
        this.kode_voucher = kode_voucher;
        this.detail_voucher = detail_voucher;
//        this.status = voucher;
        this.gambar = gambar;
    }


    public void setVoucher(String voucher) {
        this.voucher = voucher;
    }
    public String getVoucher(){ return voucher;}

    public void setKode_voucher(String kode_voucher){
        this.kode_voucher = kode_voucher;
    }

    public String getKode_voucher(){ return kode_voucher;}

    public void setGambar(String gambar){
        this.gambar = gambar;
    }

    public String getGambar(){ return gambar;}

    public void setDetail_voucher(String detail_voucher){
        this.detail_voucher = detail_voucher;
    }

    public String getDetail_voucher(){ return detail_voucher;}

    public void setStatusVoucher(int status){
        this.status = status;
    }

    public int getStatusVoucher(){ return status;}
}
