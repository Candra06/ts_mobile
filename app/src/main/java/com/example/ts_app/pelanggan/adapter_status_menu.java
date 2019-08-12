package com.example.ts_app.pelanggan;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ts_app.R;

import java.util.ArrayList;

public class adapter_status_menu extends RecyclerView.Adapter<adapter_status_menu.ViewHolder> {

    private final ArrayList<model_status_menu> list_status;
    private Activity activity;
    private Context context;
    public adapter_status_menu(Activity activity, ArrayList<model_status_menu> list_status) {
        this.list_status = list_status;
        this.activity = activity;
    }
    @NonNull
    @Override
    public adapter_status_menu.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_menu_front, parent, false);
        adapter_status_menu.ViewHolder vh = new adapter_status_menu.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull adapter_status_menu.ViewHolder holder, int position) {
        model_status_menu md = list_status.get(position);
        final adapter_status_menu.ViewHolder x=holder;
        holder.txt_outlet.setText(md.getOutlet());
        holder.txt_status.setText(md.getStatus());

        holder.kd_detail = md.getKd_detail();
    }

    @Override
    public int getItemCount() { return list_status.size(); }
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txt_outlet, txt_status;
        String kd_detail;

        public ViewHolder(View v) {
            super(v);
            txt_outlet = (TextView) v.findViewById(R.id.txt_outlet_nama);
            txt_status = (TextView) v.findViewById(R.id.txt_status_menu);
        }
    }
}
