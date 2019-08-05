package com.example.ts_app.voucher;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ts_app.R;
import com.example.ts_app.pelanggan.activity_detail_voucher;
import com.example.ts_app.config.ServerAPI;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class adapter_myvoucher extends RecyclerView.Adapter<adapter_myvoucher.ViewHolder>{

    private ArrayList<mdl_voucher> list_voucher;
    private Activity activity;
    private Context context;
    public adapter_myvoucher(Context context, ArrayList<mdl_voucher> list_voucher) {
        this.list_voucher = list_voucher;
        this.context = context;
    }

    @Override
    public adapter_myvoucher.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.template_my_voucher, parent, false);
        adapter_myvoucher.ViewHolder vh = new adapter_myvoucher.ViewHolder(v);
        return vh;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        mdl_voucher md = list_voucher.get(position);
        final ViewHolder x=holder;
        try {
            x.txt_kd_voucher.setText(md.getKode_voucher());
            x.txt_voucher.setText(md.getVoucher());
            x.kd_detail = md.getDetail_voucher();
            Picasso.get()
                    .load(ServerAPI.IPSever + md.getGambar())
                    .into(x.txtGambar);
            x.url = md.getGambar();
        }catch (Exception ea){
            ea.printStackTrace();
        }


    }
    @Override
    public int getItemCount() {
        return list_voucher.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cv;
        private TextView txt_kd_voucher, txt_voucher, status;
        private ImageView txtGambar;
        String url, kd_detail;

        public ViewHolder(View v) {
            super(v);
            txt_kd_voucher=(TextView)v.findViewById(R.id.id_voucher);
            txt_voucher=(TextView)v.findViewById(R.id.title_voucher);
            txtGambar=(ImageView) v.findViewById(R.id.img_voucher);
            cv= (CardView) v.findViewById(R.id.template_my_voucher);

            cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), activity_detail_voucher.class);
                    intent.putExtra("kd_detail", kd_detail);

                    v.getContext().startActivity(intent);
                }
            });
        }
    }
}
