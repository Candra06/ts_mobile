package com.example.ts_app;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
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
import com.example.ts_app.config.AppController;
import com.example.ts_app.config.ImageUtil;
import com.example.ts_app.config.ServerAPI;
import com.example.ts_app.config.authdata;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class activity_detail_blog extends AppCompatActivity {

    ProgressDialog pd;
    String text;

    ImageView img_detail_blog, img_back;

    TextView txt_title, txt_conten, txt_tgl, txt_author;
    String kd_blog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_blog);

        pd = new ProgressDialog(activity_detail_blog.this);

        Bundle bundle = getIntent().getExtras();
        kd_blog = bundle.getString("kd_blog");

        txt_title = (TextView) findViewById(R.id.txt_title_blog);
        txt_conten = (TextView) findViewById(R.id.txt_conten);
        txt_tgl = (TextView) findViewById(R.id.tgl_upload);
        txt_author = (TextView) findViewById(R.id.txt_author);

        img_detail_blog = (ImageView) findViewById(R.id.img_detail_blog);
        img_back = (ImageView) findViewById(R.id.img_back_dt_blog);

        loadJSON();
    }

    private void loadJSON() {
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

                    pd.cancel();


                    txt_title.setText(data.getString("judul"));
                    txt_conten.setText(data.getString("conten"));
                    txt_author.setText(data.getString("nama"));
                    txt_tgl.setText(ImageUtil.settanggal(data.getString("tgl_upload")));

                    Picasso.get()
                            .load(ServerAPI.IPSever+data.getString("foto"))
                            .into(img_detail_blog);

                    Log.e("link foto", ServerAPI.IPSever+data.getString("foto"));
                } catch (JSONException e) {
                    Log.e("Erornya", e.getMessage());
                    Toast.makeText(activity_detail_blog.this, "Masuk Gagal" + e.getMessage(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Erronya ", error.toString(), error);
                Toast.makeText(activity_detail_blog.this, error.getMessage(), Toast.LENGTH_LONG).show();


            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("kode", authdata.getInstance(activity_detail_blog.this).getAuth());
                params.put("kd_blog", kd_blog);
                params.put("tipe", "detail_blog");
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }
}
