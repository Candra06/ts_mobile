package com.example.ts_app.kasir.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ts_app.R;
import com.example.ts_app.kasir.model.mdl_history;

import java.util.ArrayList;

public class adapter_history extends RecyclerView.Adapter<adapter_history.ViewHolder> {

    private final ArrayList<mdl_history> list_history;
    private Activity activity;
    private Context context;

    public adapter_history(Activity activity, ArrayList<mdl_history> list_history){
        this.list_history = list_history;
        this.activity = activity;
    }
    @NonNull
    @Override
    public adapter_history.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.template_histori_point, parent, false);
        adapter_history.ViewHolder vh = new adapter_history.ViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull adapter_history.ViewHolder holder, int position) {
        mdl_history md = list_history.get(position);
        final adapter_history.ViewHolder x =holder;
        x.txt_aktivitas.setText(md.getAktivitas());
        x.status = md.getStatus();
        if (x.status == 1){
            x.txt_status.setText("+");
        }else if (x.status == 2){
            x.txt_status.setText("-");
        }
        x.txt_jumlah.setText(String.valueOf(md.getJumlah()));
        x.kd_history = md.getKd_history();
    }

    @Override
    public int getItemCount() {
        return list_history.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txt_aktivitas, txt_status, txt_jumlah;
        String kd_history;
        int status;
        CardView cv;
        public ViewHolder(View v) {
            super(v);
            txt_aktivitas = (TextView) v.findViewById(R.id.txt_aktivitas);
            txt_status = (TextView) v.findViewById(R.id.txt_status_poin);
            txt_jumlah = (TextView) v.findViewById(R.id.txt_jumlah_poin);
        }
    }
}
