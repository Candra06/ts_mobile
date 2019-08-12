package com.example.ts_app.home.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ts_app.R;
import com.example.ts_app.activity_detail_promo;
import com.example.ts_app.config.ServerAPI;
import com.example.ts_app.home.Model.mdl_promo;
import com.example.ts_app.home.fragment_home;
import com.example.ts_app.pelanggan.activity_detail_outlet;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Adapter_promo extends RecyclerView.Adapter<Adapter_promo.ViewHolder> {

    private ArrayList<mdl_promo> listdata;
    private Activity activity;
    private Context context;
    public Adapter_promo(Activity activity, ArrayList<mdl_promo> listdata) {
        this.listdata = listdata;
        this.activity = activity;
    }

    @Override
    public Adapter_promo.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.template_promo, parent, false);
        Adapter_promo.ViewHolder vh = new Adapter_promo.ViewHolder(v);
        return vh;
    }
    @Override
    public void onBindViewHolder(Adapter_promo.ViewHolder holder, int position) {
        mdl_promo md = listdata.get(position);
        final Adapter_promo.ViewHolder x=holder;
        holder.txt_kd_promo.setText(listdata.get(position).getKode_promo());
        holder.txt_judul_promo.setText(listdata.get(position).get_judul_promo());
        Picasso.get()
                .load(ServerAPI.IPSever+md.getGambar())
                .into(holder.txt_gambar);
        holder.kd_promo = md.getKode_promo();
        Fragment fragment;

    }
    @Override
    public int getItemCount() {
        return listdata.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cv;
        private TextView txt_judul_promo, txt_kd_promo;
        private ImageView  txt_gambar;
        String detailStatus, kd_promo;
        ProgressBar progress;
        String jumlah;
        public ViewHolder(View v) {
            super(v);
            txt_kd_promo=(TextView)v.findViewById(R.id.txt_kd_promo);
            txt_judul_promo=(TextView)v.findViewById(R.id.txt_judul_promo);
            txt_gambar=(ImageView)v.findViewById(R.id.img_promo);
            cv = (CardView)v.findViewById(R.id.card_promo);

            cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), activity_detail_promo.class);
                    intent.putExtra("kd_promo", kd_promo);
                    v.getContext().startActivity(intent);
                }
            });

    }
}
}
