package com.TemanSebangkuApp.ts_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.TemanSebangkuApp.ts_app.R;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.TemanSebangkuApp.ts_app.config.AppController;
import com.TemanSebangkuApp.ts_app.config.ServerAPI;
import com.TemanSebangkuApp.ts_app.config.authdata;
import com.TemanSebangkuApp.ts_app.kasir.adapter.adapter_history;
import com.TemanSebangkuApp.ts_app.kasir.model.mdl_history;
import com.TemanSebangkuApp.ts_app.pelanggan.activity_tab_dashboard;
import com.google.zxing.Result;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.dm7.barcodescanner.zxing.ZXingScannerView;


public class scan_qr extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    ProgressDialog pd;
    ZXingScannerView Scannerview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Scannerview = new ZXingScannerView(this);
        setContentView(Scannerview);
        pd = new ProgressDialog(scan_qr.this);

//        Log.d("berhasil dapat", "kebukak gan");
    }

    @Override
    public void handleResult(Result result) {
//        activity_scan_voucher.txt_get_code.setText(result.getText());
//        get_code.equals(activity_scan_voucher.txt_get_code.toString());

        onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Scannerview.stopCamera();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Scannerview.setResultHandler(this);
        Scannerview.startCamera();
    }


    public static class activity_my_point extends AppCompatActivity {

        ProgressDialog pd;
        private List<mdl_history> listHistory;
        private RecyclerView list_history;
        private com.TemanSebangkuApp.ts_app.kasir.adapter.adapter_history adapter_history;
        RecyclerView.LayoutManager mManager;
        TextView txt_poin;
        ImageView img_back;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_scan_qr);

            pd = new ProgressDialog(activity_my_point.this);
            txt_poin = (TextView) findViewById(R.id.txt_poin) ;
            img_back = (ImageView) findViewById(R.id.img_back);

            list_history = (RecyclerView) findViewById(R.id.ryc_list_history);
            list_history.setHasFixedSize(true);
            listHistory = new ArrayList<>();
            adapter_history = new adapter_history(activity_my_point.this, (ArrayList<mdl_history>) listHistory);
            mManager = new LinearLayoutManager(activity_my_point.this, LinearLayoutManager.VERTICAL, false);
            list_history.setLayoutManager(mManager);
            list_history.setAdapter(adapter_history);

            img_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity_my_point.this, activity_tab_dashboard.class);
                    activity_my_point.this.finish();
                }
            });

            get_profil();
            loadData();
        }

        public void loadData(){
            pd.setMessage("Menampilkan Data");
            pd.setCancelable(false);
            pd.show();
            StringRequest senddata = new StringRequest(Request.Method.POST, ServerAPI.GET_DATA, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    JSONObject res = null;
                    try {
                        res = new JSONObject(response);
                        JSONArray arr = res.getJSONArray("data");
                        if (arr.length() > 0) {
                            for (int i = 0; i < arr.length(); i++) {
                                try {
                                    JSONObject data = arr.getJSONObject(i);
                                    mdl_history md = new mdl_history();
                                    md.setKd_history(data.getString("kd_history"));
                                    md.setAktivitas(data.getString("aktivitas"));
                                    md.setStatus(data.getInt("status"));
                                    md.setJumlah(data.getInt("banyak_poin"));
                                    listHistory.add(md);
                                } catch (Exception ea) {
                                    ea.printStackTrace();

                                }
                            }
                            pd.cancel();
                            adapter_history.notifyDataSetChanged();
                        } else {
                            Log.e("Data tidak ditemukan", "Datanya gk keluar");
                            Toast.makeText(activity_my_point.this, "Datanya tidak ditemukan", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        e.printStackTrace();
                        pd.cancel();
                        Log.e("Data tidak ditemukan", "not available " + e.getMessage());
                        Toast.makeText(activity_my_point.this, "Data tidak ditemukan ", Toast.LENGTH_SHORT).show();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
    //                pd.cancel();
                    Log.d("volley", "errornya : " + error.getMessage());
                    Toast.makeText(activity_my_point.this, "Data tidak ditemukan bro", Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                public Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("kode", authdata.getInstance(activity_my_point.this).getAuth());
                    params.put("tipe", "history_poin");
                    params.put("kd_user", authdata.getInstance(activity_my_point.this).getKd_user());
                    Log.e("kode",  authdata.getInstance(activity_my_point.this).getAuth());
                    Log.e("tipe",  "history_point");
                    Log.e("kode_user",  authdata.getInstance(activity_my_point.this).getKd_user());
                    return params;
                }
            };
            AppController.getInstance().addToRequestQueue(senddata);
        }

        public void get_profil() {
            pd.setMessage("Tunggu....");
            pd.show();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerAPI.GET_DATA, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    pd.dismiss();
                    try {
                        JSONObject object = new JSONObject(response);
                        JSONArray data_profil = object.getJSONArray("data");
                        JSONObject data = data_profil.getJSONObject(0);

                            txt_poin.setText(data.getString("poin"));



                    } catch (JSONException e) {
                        Log.e("Erornya", e.getMessage());
                        Toast.makeText(activity_my_point.this, "Masuk Gagal", Toast.LENGTH_LONG).show();
                        e.printStackTrace();

                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    pd.dismiss();
                    Toast.makeText(activity_my_point.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    Log.d("Erronya ", error.getMessage(), error);
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("tipe", "profil");
                    params.put("kode", authdata.getInstance(activity_my_point.this).getAuth());
                    return params;
                }
            };

            AppController.getInstance().addToRequestQueue(stringRequest);
        }


    }
}
