package com.example.ts_app.kasir.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ts_app.R;
import com.example.ts_app.kasir.model.mdl_pilih_promo;
import com.example.ts_app.outlet.adapter_outlet;
import com.example.ts_app.outlet.mdl_outlet;
import com.example.ts_app.pelanggan.activity_detail_outlet;

import java.util.ArrayList;

public class adapter_pilih_promo extends RecyclerView.Adapter<adapter_pilih_promo.ViewHolder> {

    private ArrayList<mdl_pilih_promo> list_promo;
    private Activity activity;
    private Context context;
    public adapter_pilih_promo(Activity activity, ArrayList<mdl_pilih_promo> list_outlet) {
        this.list_promo = list_promo;
        this.activity = activity;
    }

    @Override
    public adapter_pilih_promo.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.template_list_promo, parent, false);
        adapter_pilih_promo.ViewHolder vh = new adapter_pilih_promo.ViewHolder(v);
        return vh;
    }


    @Override
    public void onBindViewHolder(adapter_pilih_promo.ViewHolder holder, int position) {
        mdl_pilih_promo md = list_promo.get(position);
        holder.txt_nama_promo.setText(md.getPromo());
        holder.kd_promo = md.getKode_promo();

    }
    @Override
    public int getItemCount() {
        return list_promo.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cv;
        private TextView txt_nama_promo;
        String  kd_promo;

        public ViewHolder(View v) {
            super(v);
            txt_nama_promo=(TextView)v.findViewById(R.id.txt_nama_promo);

        }
    }
}
