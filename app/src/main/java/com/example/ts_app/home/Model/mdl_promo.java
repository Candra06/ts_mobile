package com.example.ts_app.home.Model;

public class mdl_promo {
    String judul;
    String kode_promo;
    String gambar;
    int status;

    public mdl_promo(){
        this.judul = judul;
        this.kode_promo = kode_promo;
        this.gambar = gambar;

    }


    public void setJudul_promo(String judul) {
        this.judul = judul;
    }
    public String get_judul_promo(){ return judul;}

    public void setKode_promo(String kode_promo){
        this.kode_promo = kode_promo;
    }

    public String getKode_promo(){ return kode_promo;}

    public void setStatus(int status){
        this.status = status;
    }

    public int getStatus(){ return status;}

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }
    public String getGambar(){ return gambar;}





}
