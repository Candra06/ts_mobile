package com.example.ts_app;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class dialog_set_lokasi extends BottomSheetDialogFragment {

    ProgressDialog progressDialog;
    TextView txt_lokasi, txt_longitude, txt_latitude;
    Button btn_konfirmasi;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_set_location_xml, container, false);

        assert getArguments() != null;
        progressDialog = new ProgressDialog(getContext());
        txt_lokasi = v.findViewById(R.id.get_location);
        txt_longitude = v.findViewById(R.id.get_longitude);
        txt_latitude = v.findViewById(R.id.get_latitude);
        btn_konfirmasi = v.findViewById(R.id.btn_fix_location);

        txt_lokasi.setText(getArguments().getString("alamat"));
        txt_longitude.setText(getArguments().getString("longitude"));
        txt_latitude.setText(getArguments().getString("latitude"));
        btn_konfirmasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                set_location();
            }
        });
        return v;
    }

    public void set_location() {
        progressDialog.setMessage("Tunggu....");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerAPI.SAVE_DATA, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject object = new JSONObject(response);
                    JSONObject data = object.getJSONObject("respon");

                    if (data.getBoolean("bool")) {
                        Toast.makeText(getContext(), data.getString("pesan"), Toast.LENGTH_LONG).show();
                        Intent myIntent = new Intent(getContext(), MapsActivity.class);
                        getContext().startActivity(myIntent);
//                        Toast.makeText(getContext(), "Input Berhasil " + data.getString("pesan"), Toast.LENGTH_LONG).show();

                    } else {
                        Toast.makeText(getContext(), "Input Gagal " + data.getString("pesan"), Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    Log.e("Erornya", e.getMessage());
                    Toast.makeText(getContext(), "Gagal memuat data", Toast.LENGTH_LONG).show();
                    e.printStackTrace();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("Erronya ", error.getMessage(), error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("longitude", txt_longitude.getText().toString());
                params.put("latitude", txt_latitude.getText().toString());
                params.put("alamat", txt_lokasi.getText().toString());
                params.put("tipe", "set_location");
                params.put("kode", SharedPrefManager.getInstance(getContext()).getAuthKey());
                params.put("kd_outlet", authdata.getInstance(getContext()).getKd_outlet());
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }
}
