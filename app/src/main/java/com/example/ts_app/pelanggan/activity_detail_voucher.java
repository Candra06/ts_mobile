package com.example.ts_app.pelanggan;

import androidx.appcompat.app.AppCompatActivity;

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
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class activity_detail_voucher extends AppCompatActivity {
    String kd_detail;
    TextView txt_voucher, txt_syarat;
    ImageView img_qr_voucher, img_back;
    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_voucher);

        pd = new ProgressDialog(activity_detail_voucher.this);
        txt_syarat = (TextView) findViewById(R.id.txt_syarat_voucher);
        txt_voucher = (TextView) findViewById(R.id.txt_nama_voucher);

        img_qr_voucher = (ImageView) findViewById(R.id.img_qr_voucher);
        img_back = (ImageView) findViewById(R.id.img_back);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(activity_detail_voucher.this, activity_voucher.class);
                activity_detail_voucher.this.startActivity(in);
            }
        });

        Bundle bundle = getIntent().getExtras();
        kd_detail = bundle.getString("kd_detail");

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

                    txt_voucher.setText(data.getString("voucher"));
                    txt_syarat.setText(data.getString("deskripsi"));

                    Picasso.get()
                            .load(ServerAPI.IPSever+"foto/qrcode/"+data.getString("kd_detail")+".png")
                            .into(img_qr_voucher);


                } catch (JSONException e) {
                    Log.e("Erornya", e.getMessage());
                    Toast.makeText(activity_detail_voucher.this, "Masuk Gagal", Toast.LENGTH_LONG).show();
                    e.printStackTrace();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                Toast.makeText(activity_detail_voucher.this, error.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("Erronya ", error.getMessage(), error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("tipe", "voucher");
                params.put("kode", authdata.getInstance(activity_detail_voucher.this).getAuth());
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }
}
