package com.TemanSebangkuApp.ts_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.TemanSebangkuApp.ts_app.R;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.TemanSebangkuApp.ts_app.config.AppController;
import com.TemanSebangkuApp.ts_app.config.ServerAPI;
import com.TemanSebangkuApp.ts_app.config.authdata;
import com.TemanSebangkuApp.ts_app.kasir.adapter.adpt_status_menu;
import com.TemanSebangkuApp.ts_app.kasir.model.mdl_status_menu;
import com.TemanSebangkuApp.ts_app.menu.adapter_menu;
import com.TemanSebangkuApp.ts_app.menu.mdl_menu;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class activity_set_menu extends AppCompatActivity {
    ProgressDialog pd;
    private List<mdl_status_menu> listMenu;
    private RecyclerView list_menu;
    private com.TemanSebangkuApp.ts_app.kasir.adapter.adpt_status_menu adapter_menu;
    FloatingActionButton fab_add;
    RecyclerView.LayoutManager mManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_menu);

        pd = new ProgressDialog(activity_set_menu.this);

        fab_add = (FloatingActionButton) findViewById(R.id.add);

        list_menu = (RecyclerView) findViewById(R.id.recycler_menu);
        list_menu.setHasFixedSize(true);
        listMenu = new ArrayList<>();
        adapter_menu = new adpt_status_menu(activity_set_menu.this,(ArrayList<mdl_status_menu>) listMenu);
        mManager = new GridLayoutManager(activity_set_menu.this,2);
        list_menu.setLayoutManager(mManager);

        list_menu.setAdapter(adapter_menu);

        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(activity_set_menu.this, activity_add_detail.class);
                startActivity(in);
            }
        });
        loadJson();
    }

    public void loadJson() {
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
                                mdl_status_menu md = new mdl_status_menu();
                                md.setKode_menu(data.getString("kd_menu"));
                                md.setMenu(data.getString("menu"));
                                md.setGambar(data.getString("foto"));
                                md.setStatus(data.getInt("status"));
                                md.setKd_detail(data.getString("kd_detail"));
                                Log.e("kd", data.getString("kd_menu"));
                                Log.e("nama", data.getString("menu"));
                                listMenu.add(md);
                            } catch (Exception ea) {
                                ea.printStackTrace();

                            }
                        }
                        pd.cancel();
                        adapter_menu.notifyDataSetChanged();
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
                params.put("kode", authdata.getInstance(activity_set_menu.this).getAuth());
                params.put("tipe", "status_menu");
                params.put("kd_outlet", authdata.getInstance(activity_set_menu.this).getKd_outlet());
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(senddata);
    }
}
