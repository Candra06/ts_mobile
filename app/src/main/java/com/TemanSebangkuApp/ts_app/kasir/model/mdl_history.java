package com.TemanSebangkuApp.ts_app.kasir.model;

public class mdl_history {

    String kd_history;
    String aktivitas;
    int status;
    int jumlah;

    public mdl_history(){
        this.kd_history = kd_history;
        this.aktivitas = aktivitas;
        this.status = status;
        this.jumlah = jumlah;
    }

    public void setKd_history(String kd_history) {
        this.kd_history = kd_history;
    }
    public String getKd_history(){ return kd_history;}

    public void setAktivitas(String aktivitas) {
        this.aktivitas = aktivitas;
    }

    public String getAktivitas(){ return aktivitas;}

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus() {return status; }

    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
    }

    public int getJumlah() {return jumlah; }
}
