package com.TemanSebangkuApp.ts_app;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.TemanSebangkuApp.ts_app.R;
import com.TemanSebangkuApp.ts_app.pelanggan.activity_profil;
import com.TemanSebangkuApp.ts_app.pelanggan.activity_tab_dashboard;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.TemanSebangkuApp.ts_app.config.AppController;
import com.TemanSebangkuApp.ts_app.config.ServerAPI;
import com.TemanSebangkuApp.ts_app.config.authdata;
import com.TemanSebangkuApp.ts_app.pelanggan.activity_detail_voucher;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class activity_qr_profile extends AppCompatActivity {
    TextView txt_nama;
    ImageView img_my_qr, img_kembali;
    ProgressDialog pd;
    String my_kode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_profile);

        Bundle bundle = getIntent().getExtras();
        my_kode = bundle.getString("kode_user");
        Log.e("kodenya", my_kode);
        pd = new ProgressDialog(activity_qr_profile.this);
        img_my_qr = (ImageView) findViewById(R.id.my_qr);
        img_kembali = (ImageView) findViewById(R.id.img_back);
        txt_nama = (TextView) findViewById(R.id.my_profile);

        img_kembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity_qr_profile.this, activity_profil.class);
//                intent.putExtra("kd_auth", authdata.getInstance(activity_profil.this).getAuth()); //Optional parameters

                activity_qr_profile.this.finish();
            }
        });
        loadJson();
    }

    public void loadJson(){
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

                    txt_nama.setText(data.getString("nama"));
                    Picasso.get()
                            .load(ServerAPI.IPSever+"foto/qrcode/"+data.getString("kd_pelanggan")+".png")
                            .into(img_my_qr);

                } catch (JSONException e) {
                    Log.e("Erornya", e.getMessage());
                    Toast.makeText(activity_qr_profile.this, "Masuk Gagal", Toast.LENGTH_LONG).show();
                    e.printStackTrace();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                Toast.makeText(activity_qr_profile.this, error.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("Erronya ", error.getMessage(), error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("tipe", "profil");
                params.put("kode", authdata.getInstance(activity_qr_profile.this).getAuth());
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }
}
