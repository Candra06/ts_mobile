package com.example.ts_app;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.ts_app.config.AppController;
import com.example.ts_app.config.ServerAPI;
import com.example.ts_app.config.authdata;
import com.example.ts_app.pelanggan.activity_register;
import com.example.ts_app.pelanggan.activity_tab_dashboard;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class activity_add_detail extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    TextView txt_menu, txt_status;
    ProgressDialog pd;
    Button btn_save;
    String kd_menu, status;
    Spinner status_menu;
    ImageView img_back;

    String[] stm= {"Pilih status", "Ready", "Habis"};

    LinearLayout ln_menu, ln_status;

    ArrayList<String> data_menu = new ArrayList<String>();
    ArrayList<String> index_menu = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_detail);

        pd = new ProgressDialog(activity_add_detail.this);

        txt_menu = (TextView) findViewById(R.id.txt_pilih_menu);
//        txt_status = (TextView) findViewById(R.id.txt_status_menu);

        ln_menu = (LinearLayout) findViewById(R.id.linear_menu);
        ln_status = (LinearLayout) findViewById(R.id.ln_status);

        btn_save = (Button) findViewById(R.id.btn_save);

        img_back = (ImageView) findViewById(R.id.img_back);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity_add_detail.this, activity_set_menu.class);
                activity_add_detail.this.startActivity(intent);
            }
        });

        status_menu = (Spinner) findViewById(R.id.status_menu);
        status_menu.setOnItemSelectedListener(this);
        ArrayAdapter sm = new ArrayAdapter(this,android.R.layout.simple_spinner_item,stm);
        status_menu.setAdapter(sm);

        get_promo();

        txt_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(activity_add_detail.this);
                builder.setCancelable(false);
                builder.setTitle("Pilih menu");
                builder.setItems(data_menu.toArray(new String[0]), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        txt_menu.setText(data_menu.get(which));
                        kd_menu = index_menu.get(which);
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //the user clicked on Cancel
                    }
                });
                builder.show();
            }
        });


        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txt_menu.equals("")){
                    Toast.makeText(activity_add_detail.this, "Harap memilih menu", Toast.LENGTH_LONG).show();
                    txt_menu.setFocusable(true);
                }else if (status == "Pilih status"){
                    Toast.makeText(activity_add_detail.this, "Status tidak boleh kosong", Toast.LENGTH_LONG).show();
                    status_menu.setFocusable(true);
                }else {
                    register();
                }
            }
        });
    }

    public void get_promo() {
        StringRequest senddata = new StringRequest(Request.Method.POST, ServerAPI.GET_DATA, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject res = null;
                try {
                    res = new JSONObject(response);

                    JSONArray arr = res.getJSONArray("data");
                    for (int i = 0; i < arr.length(); i++) {
                        try {
                            JSONObject datakom = arr.getJSONObject(i);
                            String ku = datakom.getString("menu");
                            String koded = datakom.getString("kd_menu");
                            Log.e("kodenya", datakom.getString("kd_menu"));
                            data_menu.add(ku);
                            index_menu.add(koded);
                        } catch (Exception ea) {
                            ea.printStackTrace();
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("daptkan eror", e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("volley", "errornya : " + error.getMessage());
            }
        }) {

            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("kode", authdata.getInstance(getApplicationContext()).getAuth());
                params.put("tipe", "menu");
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(senddata);
    }

    public void register(){
        pd.setMessage("Tunggu....");
        pd.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerAPI.SAVE_DATA, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.dismiss();
                try {
                    JSONObject object = new JSONObject(response);
                    JSONObject data = object.getJSONObject("respon");

                    if (data.getBoolean("bool")) {
                        Intent myIntent = new Intent(activity_add_detail.this, activity_set_menu.class);
                        activity_add_detail.this.startActivity(myIntent);
                        Toast.makeText(activity_add_detail.this, "Input Berhasil "+data.getString("pesan"), Toast.LENGTH_LONG).show();

                    } else {
                        Intent myIntent = new Intent(activity_add_detail.this, activity_add_detail.class);
                        activity_add_detail.this.startActivity(myIntent);
                        Toast.makeText(activity_add_detail.this, "Input Gagal "+data.getString("pesan"), Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    Log.e("Erornya", e.getMessage());
                    Toast.makeText(activity_add_detail.this, "Gagal", Toast.LENGTH_LONG).show();
                    e.printStackTrace();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                Toast.makeText(activity_add_detail.this, error.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("Erronya ", error.getMessage(), error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("kd_menu", kd_menu);
                params.put("status", status);
                params.put("tipe", "add_detail_menu");
                params.put("kd_outlet", authdata.getInstance(activity_add_detail.this).getKd_outlet());
                params.put("kode", authdata.getInstance(activity_add_detail.this).getAuth());
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        status_menu.setPrompt(stm[position]);
        status = String.valueOf(position);
//        Toast.makeText(getApplicationContext(), gender[position], Toast.LENGTH_LONG).show();
        Log.e("val", String.valueOf(position));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        status_menu.setPrompt("Pilih Gender");
    }

}
