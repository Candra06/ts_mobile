package com.example.ts_app.outlet;

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
import com.example.ts_app.pelanggan.activity_detail_outlet;
import com.example.ts_app.config.ServerAPI;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class adapter_outlet extends RecyclerView.Adapter<adapter_outlet.ViewHolder> {

    private ArrayList<mdl_outlet> list_outlet;
    private Activity activity;
    private Context context;
    public adapter_outlet(Activity activity, ArrayList<mdl_outlet> list_outlet) {
        this.list_outlet = list_outlet;
        this.activity = activity;
    }

    @Override
    public adapter_outlet.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.template_outlet, parent, false);
        adapter_outlet.ViewHolder vh = new adapter_outlet.ViewHolder(v);
        return vh;
    }


    @Override
    public void onBindViewHolder(adapter_outlet.ViewHolder holder, int position) {
        mdl_outlet md = list_outlet.get(position);
        holder.txt_nama_outlet.setText(md.getOutlet());
        holder.txt_kd_outlet.setText(md.getKode_outlet());
        holder.distance = Double.parseDouble(new DecimalFormat("##.##").format(md.getDistance()));

        holder.txt_distance.setText(String.valueOf(holder.distance));
        Picasso.get()
                .load(ServerAPI.IPSever + md.getGambar())
                .into(holder.txt_gambar);
        holder.url = md.getGambar();
        holder.kd_outlet = md.getKode_outlet();
        Fragment fragment;

    }
    @Override
    public int getItemCount() {
        return list_outlet.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cv;
        private TextView txt_kd_outlet, txt_nama_outlet, status, txt_distance;
        private ImageView txt_gambar;
        String url, kd_outlet;
        Double distance;

        public ViewHolder(View v) {
            super(v);
            txt_kd_outlet=(TextView)v.findViewById(R.id.txt_kd_otlet);
            txt_nama_outlet=(TextView)v.findViewById(R.id.txt_nm_outlet);
            txt_gambar=(ImageView)v.findViewById(R.id.img_outlet);
            txt_distance=(TextView)v.findViewById(R.id.txt_distance);
            cv = (CardView)v.findViewById(R.id.card_outlet);

            cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), activity_detail_outlet.class);
                    intent.putExtra("kd_outlet", kd_outlet);
                    v.getContext().startActivity(intent);
                }
            });
        }
    }
}
