package com.example.ts_app.kasir.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ts_app.R;
import com.example.ts_app.config.ServerAPI;
import com.example.ts_app.kasir.model.mdl_status_menu;
import com.example.ts_app.pelanggan.activity_detail_menu;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class adpt_status_menu extends RecyclerView.Adapter<com.example.ts_app.kasir.adapter.adpt_status_menu.ViewHolder>{

    private ArrayList<mdl_status_menu> list_menu;
    private Activity activity;
    private Context context;
    String edit,hapus, detail;
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
                .load(ServerAPI.IPSever+md.getGambar())
                .into(holder.txt_gambar);
        holder.url = md.getGambar();
//        holder.status.setText(md.getStatus());
        holder.get_status = md.getStatus();
        if (holder.get_status == 1){
            holder.status.setText("ADA");
            holder.status.setTextColor(Color.GREEN);
        }else if (holder.get_status == 2){
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
        String url, kd_menu;
        Integer get_status;
        public ImageView txt_gambar;

        public ViewHolder(View v) {
            super(v);
            txt_kd_menu=(TextView)v.findViewById(R.id.kd_menu);
            txt_nama_menu=(TextView)v.findViewById(R.id.txt_nama_menu);
            txt_gambar=(ImageView) v.findViewById(R.id.img_menu);
            status=(TextView) v.findViewById(R.id.txt_status);
            cv = (CardView) v.findViewById(R.id.card_menu_status);

            cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent intent = new Intent(v.getContext(), activity_detail_menu.class);
//                    intent.putExtra("kd_menu", kd_menu);
//                    v.getContext().startActivity(intent);
                    update_status();
                }
            });
        }

        public void update_status(){

        }
    }
}

