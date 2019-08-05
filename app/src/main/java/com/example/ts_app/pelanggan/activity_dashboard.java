package com.example.ts_app.pelanggan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.ts_app.R;
import com.example.ts_app.config.AppController;
import com.example.ts_app.config.ServerAPI;
import com.example.ts_app.config.authdata;
import com.example.ts_app.home.Model.mdl_promo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class activity_dashboard extends AppCompatActivity {

    TextView txt_nama, txt_poin;
    ProgressDialog pd;
    private List<mdl_promo> list;
    private RecyclerView list_promo;
//    RecyclerView LayoutManager mManager;
    private RecyclerView list_blog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        txt_nama = findViewById(R.id.txt_nama);
        txt_poin = findViewById(R.id.txt_poin);
    }

    public void profil(){

    }

    public void loadBlog(){

    }
}
