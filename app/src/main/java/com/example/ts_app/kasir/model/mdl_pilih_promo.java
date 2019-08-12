package com.example.ts_app.kasir.model;

public class mdl_pilih_promo {
    String promo;
    String kode_promo;
    String gambar;
    int status;

    public mdl_pilih_promo(){
        this.promo = promo;
        this.kode_promo = kode_promo;
        this.gambar = gambar;

    }


    public void setPromo(String promo) {
        this.promo = promo;
    }
    public String getPromo(){ return promo;}

    public void setKode_promo(String kode_promo){
        this.kode_promo = kode_promo;
    }

    public String getKode_promo(){ return kode_promo;}


}
