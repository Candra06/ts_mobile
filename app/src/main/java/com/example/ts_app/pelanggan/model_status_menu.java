package com.example.ts_app.pelanggan;

public class model_status_menu {

    String outlet;
    String kd_detail;
    String status;

    public model_status_menu(){
        this.outlet = outlet;
        this.kd_detail = kd_detail;
        this.status = status;

    }


    public void setOutlet(String outlet) {
        this.outlet = outlet;
    }
    public String getOutlet(){ return outlet;}

    public void setKd_detail(String kd_detail){
        this.kd_detail = kd_detail;
    }

    public String getKd_detail(){ return kd_detail;}

    public void setStatus(String status){
        this.status = status;
    }

    public String getStatus(){ return status;}

}
