package com.example.ts_app.voucher;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.net.Uri;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class activity_my_voucher extends AppCompatActivity {
    ProgressDialog pd;

    private List<mdl_voucher> listVoucher;
    private RecyclerView list_voucher;
    private com.example.ts_app.voucher.adapter_myvoucher adapter_myvoucher;
    adapter_myvoucher mAdapter;
    RecyclerView.LayoutManager mManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_voucher);
        pd = new ProgressDialog(activity_my_voucher.this);


        list_voucher = (RecyclerView) findViewById(R.id.recycler_myVOucher);
        listVoucher = new ArrayList<>();
        adapter_myvoucher = new adapter_myvoucher(activity_my_voucher.this,(ArrayList<mdl_voucher>) listVoucher);
        mManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        list_voucher.setLayoutManager(mManager);

        list_voucher.setAdapter(adapter_myvoucher);
        loadJson();
    }

    public void loadJson() {
        pd.setMessage("Menampilkan Data");
        pd.setCancelable(false);
        pd.show();
        StringRequest senddata = new StringRequest(Request.Method.POST, ServerAPI.GET_DATA, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject res = new JSONObject(response);
                    JSONArray arr = res.getJSONArray("data");
                    if (arr.length() > 0) {
                        for (int i = 0; i < arr.length(); i++) {
                            try {
                                JSONObject data = arr.getJSONObject(i);
                                mdl_voucher md = new mdl_voucher();
                                md.setKode_voucher(data.getString("kd_voucher"));
                                md.setVoucher(data.getString("voucher"));
                                md.setDetail_voucher(data.getString("kd_detail"));
                                Log.e("kd", data.getString("kd_voucher"));
                                Log.e("nama", data.getString("voucher"));
                                listVoucher.add(md);
                            } catch (Exception ea) {
                                ea.printStackTrace();

                            }
                        }
                        pd.cancel();
                        adapter_myvoucher.notifyDataSetChanged();
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
                params.put("kode", authdata.getInstance(activity_my_voucher.this).getAuth());
                params.put("tipe", "voucher");
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(senddata);
    }


}
