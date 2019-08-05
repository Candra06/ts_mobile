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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class activity_detail_menu extends AppCompatActivity {
    TextView txt_title_menu, txt_harga, txt_komposisi, txt_contact;
    ImageView img_detail_menu, img_back_menu;
    ProgressDialog pd;
    String kd_menu;
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
                activity_detail_menu.this.finish();
            }
        });

        Bundle bundle = getIntent().getExtras();
        kd_menu = bundle.getString("kd_menu");
        test_jam();
        loadJson();
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

                    txt_contact.setText(data.getString("kontak"));

                    Picasso.get()
                            .load(ServerAPI.IPSever+data.getString("foto"))
                            .into(img_detail_menu);

//                    if (data.getString("open").toString() > ) {
//                        txt_status.setText("Perempuan");
//                    } else if (data.getString("gender").equals("2")) {
//                        txt_status.setText("Laki-laki");
//                    }



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

    public void test_jam(){
        String startTime;
        startTime = "21:06:30";
        StringTokenizer tk = new StringTokenizer(startTime);
        String time = tk.nextToken();

        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
        SimpleDateFormat sdfs = new SimpleDateFormat("hh:mm a");
        Date dt;
        try {
            dt = sdf.parse(time);
            String out = "Time Display: " + sdfs.format(dt);
            Log.e("jamnya : ", out);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
