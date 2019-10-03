package com.TemanSebangkuApp.ts_app.home.Model;

public class mdl_blog {
    String judul;
    String kode_blog;
    String gambar;
    int status;

    public mdl_blog(){
        this.gambar = gambar;
        this.judul = judul;
        this.kode_blog = kode_blog;
    }


    public void setJudul(String judul) {
        this.judul = judul;
    }
    public String get_judul_blog(){ return judul;}

    public void setKode_blog(String kode_blog){
        this.kode_blog = kode_blog;
    }

    public String getKode_blog(){ return kode_blog;}

    public void setStatus(int status){
        this.status = status;
    }

    public int getStatus(){ return status;}

    public void setGambar(String gambar){
        this.gambar = gambar;
    }

    public String getGambar(){ return gambar;}





}
