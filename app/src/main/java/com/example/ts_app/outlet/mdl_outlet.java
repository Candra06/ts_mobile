package com.example.ts_app.outlet;

public class mdl_outlet {
    String outlet;
    String kode_outlet;
    String gambar;
    int status;

    public mdl_outlet(){
        this.outlet = outlet;
        this.kode_outlet = kode_outlet;
        this.gambar = gambar;

    }


    public void setOutlet(String outlet) {
        this.outlet = outlet;
    }
    public String getOutlet(){ return outlet;}

    public void setKode_outlet(String kode_outlet){
        this.kode_outlet = kode_outlet;
    }

    public String getKode_outlet(){ return kode_outlet;}

    public void setStatus(int status){
        this.status = status;
    }

    public int getStatus(){ return status;}

    public void setGambar(String gambar){
        this.gambar = gambar;
    }

    public String getGambar(){ return gambar;}
}
