package com.example.ts_app.kasir.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.ts_app.R;
import com.example.ts_app.SharedPrefManager;
import com.example.ts_app.activity_set_menu;
import com.example.ts_app.config.AppController;
import com.example.ts_app.config.ServerAPI;
import com.example.ts_app.config.authdata;
import com.example.ts_app.kasir.model.mdl_status_menu;
import com.example.ts_app.pelanggan.activity_detail_menu;
import com.example.ts_app.pelanggan.activity_register;
import com.example.ts_app.pelanggan.activity_tab_dashboard;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class adpt_status_menu extends RecyclerView.Adapter<com.example.ts_app.kasir.adapter.adpt_status_menu.ViewHolder> {

    private ArrayList<mdl_status_menu> list_menu;
    private Activity activity;
    private Context context;
    String edit, hapus, detail;

    public adpt_status_menu(Activity activity, ArrayList<mdl_status_menu> list_menu) {
        this.list_menu = list_menu;
        this.activity = activity;
    }

    @Override
    public com.example.ts_app.kasir.adapter.adpt_status_menu.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.template_status_menu, parent, false);
        com.example.ts_app.kasir.adapter.adpt_status_menu.ViewHolder vh = new com.example.ts_app.kasir.adapter.adpt_status_menu.ViewHolder(v);
        return vh;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(com.example.ts_app.kasir.adapter.adpt_status_menu.ViewHolder holder, int position) {
        mdl_status_menu md = list_menu.get(position);
        holder.txt_nama_menu.setText(md.get_menu());
        holder.txt_kd_menu.setText(md.get_kdMenu());
        Picasso.get()
                .load(ServerAPI.IPSever + md.getGambar())
                .into(holder.txt_gambar);
        holder.url = md.getGambar();
        holder.kd_detail = md.getKd_detail();
//        holder.status.setText(md.getStatus());
        holder.get_status = md.getStatus();
        if (holder.get_status == 1) {
            holder.status.setText("Ready");
            holder.status.setTextColor(Color.GREEN);

        } else if (holder.get_status == 2) {
            holder.status.setText("Habis");
            holder.status.setTextColor(Color.RED);
        }
        holder.kd_menu = md.get_kdMenu();
    }

    @Override
    public int getItemCount() {
        return list_menu.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cv;
        private TextView txt_kd_menu, txt_nama_menu, status;
        String url, kd_menu, kd_detail;
        Integer get_status;
        public ImageView txt_gambar;

        public ViewHolder(View v) {
            super(v);
            txt_kd_menu = (TextView) v.findViewById(R.id.kd_menu);
            txt_nama_menu = (TextView) v.findViewById(R.id.txt_nama_menu);
            txt_gambar = (ImageView) v.findViewById(R.id.img_menu);
            status = (TextView) v.findViewById(R.id.txt_status);
            cv = (CardView) v.findViewById(R.id.card_menu_status);

            cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent intent = new Intent(v.getContext(), activity_detail_menu.class);
//                    intent.putExtra("kd_menu", kd_menu);
//                    v.getContext().startActivity(intent);
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(v.getContext());
                    builder1.setMessage("Apakah anda yakin ingin merubah status menu?.");
                    builder1.setCancelable(true);

                    builder1.setNegativeButton(
                            "Tidak",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    builder1.setPositiveButton(
                            "Ya",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    if (get_status == 1) {
                                        update_kosong();
                                    } else if (get_status == 2) {
                                        update_ready();
                                    }
//                                    update_status();
                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();
//                    update_status();
                }
            });
        }

        public void update_kosong() {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerAPI.SAVE_DATA, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject object = new JSONObject(response);
                        JSONObject data = object.getJSONObject("respon");

                        if (data.getBoolean("bool")) {
//                            Toast.makeText(activity_register.this, data.getString("pesan"), Toast.LENGTH_LONG).show();
                            Intent myIntent = new Intent(cv.getContext(), activity_set_menu.class);
                            itemView.getContext().startActivity(myIntent);
                            Toast.makeText(itemView.getContext(), " " + data.getString("pesan"), Toast.LENGTH_LONG).show();

                        } else {

//                            Toast.makeText(activity_register.this, "Selamat Datang "+data.getString("pesan"), Toast.LENGTH_LONG).show();
//                            Intent myIntent = new Intent(activity_register.this, activity_register.class);
//                            activity_register.this.startActivity(myIntent);
                            Toast.makeText(itemView.getContext(), "Input Gagal " + data.getString("pesan"), Toast.LENGTH_LONG).show();
                        }

                    } catch (JSONException e) {
                        Log.e("Erornya", e.getMessage());
                        Toast.makeText(itemView.getContext(), "Masuk Gagal", Toast.LENGTH_LONG).show();
                        e.printStackTrace();

                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(itemView.getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    Log.d("Erronya ", error.getMessage(), error);
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("status", String.valueOf(2));
                    params.put("tipe", "update_status_menu");
                    params.put("kd_detail", kd_detail);
                    params.put("kode", authdata.getInstance(itemView.getContext()).getAuth());
                    return params;
                }
            };

            AppController.getInstance().addToRequestQueue(stringRequest);
        }

        public void update_ready() {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerAPI.SAVE_DATA, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject object = new JSONObject(response);
                        JSONObject data = object.getJSONObject("respon");

                        if (data.getBoolean("bool")) {
                            Intent myIntent = new Intent(cv.getContext(), activity_set_menu.class);
                            itemView.getContext().startActivity(myIntent);
                            Toast.makeText(itemView.getContext(), " " + data.getString("pesan"), Toast.LENGTH_LONG).show();
                        } else {

//                            Toast.makeText(activity_register.this, "Selamat Datang "+data.getString("pesan"), Toast.LENGTH_LONG).show();
//                            Intent myIntent = new Intent(activity_register.this, activity_register.class);
//                            activity_register.this.startActivity(myIntent);
                            Toast.makeText(itemView.getContext(), "Input Gagal " + data.getString("pesan"), Toast.LENGTH_LONG).show();
                        }

                    } catch (JSONException e) {
                        Log.e("Erornya", e.getMessage());
                        Toast.makeText(itemView.getContext(), "Masuk Gagal", Toast.LENGTH_LONG).show();
                        e.printStackTrace();

                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(itemView.getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    Log.d("Erronya ", error.getMessage(), error);
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("status", String.valueOf(1));
                    params.put("tipe", "update_status_menu");
                    params.put("kd_detail", kd_detail);
                    params.put("kode", authdata.getInstance(itemView.getContext()).getAuth());
                    return params;
                }
            };

            AppController.getInstance().addToRequestQueue(stringRequest);
        }
    }
}

