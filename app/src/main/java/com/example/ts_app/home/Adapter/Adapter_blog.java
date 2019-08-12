package com.example.ts_app.home.Adapter;

import android.app.Activity;
import android.content.Context;
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
import com.example.ts_app.activity_detail_blog;
import com.example.ts_app.activity_detail_promo;
import com.example.ts_app.config.ServerAPI;
import com.example.ts_app.home.Model.mdl_blog;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Adapter_blog extends RecyclerView.Adapter<Adapter_blog.ViewHolder> {

    private ArrayList<mdl_blog> list_blog;
    private Activity activity;
    private Context context;
    String edit,hapus, detail;
    public Adapter_blog(Activity activity, ArrayList<mdl_blog> list_blog) {
        this.list_blog = list_blog;
        this.activity = activity;
    }

    @Override
    public Adapter_blog.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.template_blog, parent, false);
        Adapter_blog.ViewHolder vh = new Adapter_blog.ViewHolder(v);
        return vh;
    }
    @Override
    public void onBindViewHolder(Adapter_blog.ViewHolder holder, int position) {
        mdl_blog md = list_blog.get(position);
        final Adapter_blog.ViewHolder x=holder;
        holder.txt_kd_blog.setText(md.getKode_blog());
        holder.txt_judul_blog.setText(md.get_judul_blog());
        Picasso.get()
                .load(ServerAPI.IPSever + md.getGambar())
                .into(holder.txt_gambar);
        holder.url = md.getGambar();
        holder.kd_blog = md.getKode_blog();
        Fragment fragment;

    }
    @Override
    public int getItemCount() {
        return list_blog.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cv;
        private TextView txt_judul_blog, txt_kd_blog;
        ImageView txt_gambar;
        String url, kd_blog;

        public ViewHolder(View v) {
            super(v);
            txt_kd_blog=(TextView)v.findViewById(R.id.txt_kd_blog);
            txt_judul_blog=(TextView)v.findViewById(R.id.txt_judul_blog);
            txt_gambar=(ImageView)v.findViewById(R.id.img_blog_template);
            cv=(CardView) v.findViewById(R.id.card_blog);

            cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), activity_detail_blog.class);
                    intent.putExtra("kd_blog", kd_blog);
                    v.getContext().startActivity(intent);
                }
            });
    }
}
}
