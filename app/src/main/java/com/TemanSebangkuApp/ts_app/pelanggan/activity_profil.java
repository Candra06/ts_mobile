package com.TemanSebangkuApp.ts_app.pelanggan;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.TemanSebangkuApp.ts_app.R;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.TemanSebangkuApp.ts_app.activity_qr_profile;
import com.TemanSebangkuApp.ts_app.config.AppController;
import com.TemanSebangkuApp.ts_app.config.ImageUtil;
import com.TemanSebangkuApp.ts_app.config.ServerAPI;
import com.TemanSebangkuApp.ts_app.config.authdata;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class activity_profil extends AppCompatActivity {

    TextView txt_alamat, txt_nama, txt_email, txt_no_hp, txt_tglLahir, txt_domisili, txt_gender;
    ProgressDialog pd;
    ImageView img_back, img_qr;
    String status;

    Button btn_profil;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);


        pd =  new ProgressDialog(activity_profil.this);
        btn_profil = (Button) findViewById(R.id.btn_edit_profil);

        txt_nama = (TextView) findViewById(R.id.txt_nama);
        txt_alamat = (TextView) findViewById(R.id.txt_alamat);
        txt_email = (TextView) findViewById(R.id.txt_email);
        txt_no_hp = (TextView) findViewById(R.id.txt_no_hp);
        txt_domisili = (TextView) findViewById(R.id.txt_domisili);
        txt_tglLahir = (TextView) findViewById(R.id.txt_tgl_lahir);
        txt_gender = (TextView) findViewById(R.id.txt_gender);
        img_back = (ImageView) findViewById(R.id.img_back);
        img_qr = (ImageView) findViewById(R.id.img_qr);

        goProfil();

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity_profil.this, activity_tab_dashboard.class);
//                intent.putExtra("kd_auth", authdata.getInstance(activity_profil.this).getAuth()); //Optional parameters

                activity_profil.this.finish();
            }
        });

        btn_profil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity_profil.this, acitivity_data_diri.class);
//                intent.putExtra("kd_auth", authdata.getInstance(activity_profil.this).getAuth()); //Optional parameters
                intent.putExtra("status", status);
                activity_profil.this.startActivity(intent);
            }
        });

        img_qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity_profil.this, activity_qr_profile.class);
//                intent.putExtra("kd_auth", authdata.getInstance(activity_profil.this).getAuth()); //Optional parameters
                intent.putExtra("kode_user", authdata.getInstance(activity_profil.this).getKd_user());
                activity_profil.this.startActivity(intent);
            }
        });

    }

    public void goProfil(){
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
                    txt_no_hp.setText(data.getString("no_hp"));
                    txt_email.setText(data.getString("email"));

                    status = data.getString("status");

                    Log.e("Status",status);

                    if (data.getString("domisili").equals("")){
                        txt_domisili.setText("-");
                    }else{
                        txt_domisili.setText(data.getString("domisili"));
                    }

                    if (data.getString("alamat").equals("")){
                        txt_alamat.setText("-");
                    }else{
                        txt_alamat.setText(data.getString("alamat"));
                    }

                    if (data.getString("tgl_lahir").equals("0000-00-00")){
                        txt_tglLahir.setText("-");
                    }else{
                        txt_tglLahir.setText(data.getString("tgl_lahir"));
                        txt_tglLahir.setText(ImageUtil.settanggal(data.getString("tgl_lahir")));
                    }

                    if (data.getString("gender").equals("1")){
                        txt_gender.setText("Perempuan");
                    }else if (data.getString("gender").equals("2")){
                        txt_gender.setText("Laki-laki");
                    }else {
                        txt_gender.setText("-");
                    }


//                    txt_poin.setText(data.getString("poin"));

                } catch (JSONException e) {
                    Log.e("Erornya", e.getMessage());
                    Toast.makeText(activity_profil.this, "Masuk Gagal", Toast.LENGTH_LONG).show();
                    e.printStackTrace();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                Toast.makeText(activity_profil.this, error.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("Erronya ", error.getMessage(), error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("tipe", "profil");
                params.put("kode", authdata.getInstance(activity_profil.this).getAuth());
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }


}
