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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class activity_detail_outlet extends AppCompatActivity {
    TextView txt_title_outlet, txt_jam_buka, txt_jam_tutup, txt_alamat, txt_contact, txt_status;
    ImageView img_detail_outlet, img_back_outlet;
    ProgressDialog pd;
    String kd_outlet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_outlet);

        pd = new ProgressDialog(activity_detail_outlet.this);
        txt_title_outlet = (TextView) findViewById(R.id.txt_title_outlet);
        txt_jam_buka = (TextView) findViewById(R.id.txt_jam_buka);
        txt_jam_tutup = (TextView) findViewById(R.id.txt_jam_tutup);
        txt_alamat = (TextView) findViewById(R.id.txt_alamat);
        txt_contact = (TextView) findViewById(R.id.txt_contact);
        txt_status = (TextView) findViewById(R.id.status_buka);

        img_detail_outlet = (ImageView) findViewById(R.id.img_detail_outlet);
        img_back_outlet = (ImageView) findViewById(R.id.img_back_outlet);

        img_back_outlet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity_detail_outlet.this, activity_tab_dashboard.class);
//                intent.putExtra("kd_auth", authdata.getInstance(activity_profil.this).getAuth()); //Optional parameters

                activity_detail_outlet.this.finish();
            }
        });

        Bundle bundle = getIntent().getExtras();
        kd_outlet = bundle.getString("kd_outlet");

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

                    txt_title_outlet.setText(data.getString("nama_outlet"));
                    txt_jam_buka.setText(data.getString("open"));
                    txt_jam_tutup.setText(data.getString("closed"));
                    txt_alamat.setText(data.getString("alamat"));

                    txt_contact.setText(data.getString("kontak"));

                    Picasso.get()
                            .load(ServerAPI.IPSever+data.getString("foto"))
                            .into(img_detail_outlet);

                    String open = data.getString("open");
                    String closed = data.getString("closed");

                    SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm:ss");
                    SimpleDateFormat dateFormat2 = new SimpleDateFormat("HH:mm");

                    try {
                        Date date = dateFormat.parse(open);
                        Date date2 = dateFormat.parse(closed);

                        String out = dateFormat2.format(date);
                        String out2 = dateFormat2.format(date2);
                        txt_jam_buka.setText(out);
                        txt_jam_tutup.setText(out2);
                    } catch (ParseException e) {
                    }

                    Calendar calendar1 = Calendar.getInstance();
                    String banding = dateFormat.format(calendar1.getTime());
                    String banding2 = dateFormat.format(calendar1.getTime());

                    final String timeString = open;
                    final String timeString2 = closed;
                    String datadb =timeString;
                    String datadb2 =timeString2;

                    if(banding.compareTo(datadb)>=0 && banding2.compareTo(datadb2)<=0) {
                        txt_status.setText("Now Open");
                    }else {
                        txt_status.setText("Now Closed");
                    }

                } catch (JSONException e) {
                    Log.e("Erornya", e.getMessage());
                    Toast.makeText(activity_detail_outlet.this, "Masuk Gagal", Toast.LENGTH_LONG).show();
                    e.printStackTrace();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                Toast.makeText(activity_detail_outlet.this, error.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("Erronya ", error.getMessage(), error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("tipe", "detail_outlet");
                params.put("kd_outlet", kd_outlet);
                params.put("kode", authdata.getInstance(activity_detail_outlet.this).getAuth());
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
    }
}
