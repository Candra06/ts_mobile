package com.example.ts_app.kasir.model;

public class mdl_status_menu {

    String menu;
    String kode_menu;
    String gambar;
    int status;

    public mdl_status_menu(){
        this.menu = menu;
        this.kode_menu = kode_menu;
        this.gambar = gambar;

    }


    public void setMenu(String menu) {
        this.menu = menu;
    }
    public String get_menu(){ return menu;}

    public void setKode_menu(String kode_menu){
        this.kode_menu = kode_menu;
    }

    public String get_kdMenu(){ return kode_menu;}

    public void setStatus(int status){
        this.status = status;
    }

    public int getStatus(){ return status;}

    public void setGambar(String gambar){
        this.gambar = gambar;
    }

    public String getGambar(){ return gambar;}
}
