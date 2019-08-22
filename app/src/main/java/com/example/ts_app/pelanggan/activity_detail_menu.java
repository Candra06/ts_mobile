package com.example.ts_app.pelanggan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.ts_app.R;
import com.example.ts_app.config.AppController;
import com.example.ts_app.config.ServerAPI;
import com.example.ts_app.config.authdata;
import com.example.ts_app.menu.adapter_menu;
import com.example.ts_app.menu.mdl_menu;
import com.example.ts_app.voucher.activity_my_voucher;
import com.example.ts_app.voucher.adapter_myvoucher;
import com.example.ts_app.voucher.mdl_voucher;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class activity_detail_menu extends AppCompatActivity {
    TextView txt_title_menu, txt_harga, txt_komposisi, txt_contact;
    ImageView img_detail_menu, img_back_menu;
    ProgressDialog pd;
    String kd_menu;

    private List<model_status_menu> listDetail;
    private RecyclerView list_detail;
    private com.example.ts_app.pelanggan.adapter_status_menu adapter_detail;
    com.example.ts_app.pelanggan.adapter_status_menu mAdapter;
    RecyclerView.LayoutManager mManager;


    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_menu);

        pd = new ProgressDialog(activity_detail_menu.this);
        txt_title_menu = (TextView) findViewById(R.id.txt_title_menu);
        txt_harga = (TextView) findViewById(R.id.txt_harga);
        txt_komposisi = (TextView) findViewById(R.id.txt_komposisi);
        txt_contact = (TextView) findViewById(R.id.txt_contact);

        img_detail_menu = (ImageView) findViewById(R.id.imgbanner);
        img_back_menu = (ImageView) findViewById(R.id.img_back_menu);

        img_back_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity_detail_menu.this, activity_tab_dashboard.class);
                activity_detail_menu.this.startActivity(intent);
            }
        });

        list_detail = (RecyclerView) findViewById(R.id.status_menu);
        listDetail = new ArrayList<>();
        adapter_detail = new adapter_status_menu(activity_detail_menu.this,(ArrayList<model_status_menu>) listDetail);
        mManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        list_detail.setLayoutManager(mManager);

        list_detail.setAdapter(adapter_detail);

        Bundle bundle = getIntent().getExtras();
        kd_menu = bundle.getString("kd_menu");
        loadJson();
        show_status();
    }

    public void loadJson() {
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

                    txt_title_menu.setText(data.getString("menu"));
                    txt_harga.setText(data.getString("harga"));
                    txt_komposisi.setText(data.getString("deskripsi"));


                    Picasso.get()
                            .load(ServerAPI.IPSever+data.getString("foto"))
                            .into(img_detail_menu);


                } catch (JSONException e) {
                    Log.e("Erornya", e.getMessage());
                    Toast.makeText(activity_detail_menu.this, "Masuk Gagal", Toast.LENGTH_LONG).show();
                    e.printStackTrace();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                Toast.makeText(activity_detail_menu.this, error.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("Erronya ", error.getMessage(), error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("tipe", "detail_menu");
                params.put("kd_menu", kd_menu);
                params.put("kode", authdata.getInstance(activity_detail_menu.this).getAuth());
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    public void show_status() {
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
                                model_status_menu md = new model_status_menu();
                                md.setKd_detail(data.getString("kd_detail"));
                                md.setOutlet(data.getString("nama_outlet"));
                                md.setStatus(data.getString("status"));
                                Log.e("outlet", data.getString("nama_outlet"));
                                Log.e("status", data.getString("status"));
                                listDetail.add(md);
                            } catch (Exception ea) {
                                ea.printStackTrace();

                            }
                        }
                        pd.cancel();
                        adapter_detail.notifyDataSetChanged();
                    } else {
                        pd.cancel();
//                        not_found.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    pd.cancel();
                }
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.cancel();
                Log.d("volley", "errornya : " + error.getMessage());
            }
        }) {

            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("kode", authdata.getInstance(activity_detail_menu.this).getAuth());
                params.put("tipe", "detail_status");
                params.put("kd_menu", kd_menu);
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(senddata);
    }


}
