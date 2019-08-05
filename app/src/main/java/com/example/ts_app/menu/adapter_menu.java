package com.example.ts_app.menu;

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
import com.example.ts_app.pelanggan.activity_detail_menu;
import com.example.ts_app.config.ServerAPI;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class adapter_menu extends RecyclerView.Adapter<adapter_menu.ViewHolder>{

    private ArrayList<mdl_menu> list_menu;
    private Activity activity;
    private Context context;
    String edit,hapus, detail;
    public adapter_menu(Activity activity, ArrayList<mdl_menu> list_menu) {
        this.list_menu = list_menu;
        this.activity = activity;
    }

    @Override
    public adapter_menu.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.template_menu, parent, false);
        adapter_menu.ViewHolder vh = new adapter_menu.ViewHolder(v);
        return vh;
    }
    @Override
    public void onBindViewHolder(adapter_menu.ViewHolder holder, int position) {
        mdl_menu md = list_menu.get(position);
        holder.txt_nama_menu.setText(md.get_menu());
        holder.txt_kd_menu.setText(md.get_kdMenu());
        Picasso.get()
                .load(ServerAPI.IPSever+md.getGambar())
                .into(holder.txt_gambar);
        holder.url = md.getGambar();
        holder.kd_menu = md.get_kdMenu();
        Fragment fragment;

    }
    @Override
    public int getItemCount() {
        return list_menu.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cv;
        private TextView txt_kd_menu, txt_nama_menu, status;
        String url, kd_menu;
        public ImageView txt_gambar;

        public ViewHolder(View v) {
            super(v);
            txt_kd_menu=(TextView)v.findViewById(R.id.txt_kd_menu);
            txt_nama_menu=(TextView)v.findViewById(R.id.txt_nama_menu);
            txt_gambar=(ImageView) v.findViewById(R.id.img_menu);
            cv = (CardView) v.findViewById(R.id.card_menu);

            cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), activity_detail_menu.class);
                    intent.putExtra("kd_menu", kd_menu);
                    v.getContext().startActivity(intent);
                }
            });
        }
    }
}
