package com.TemanSebangkuApp.ts_app.pelanggan;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.TemanSebangkuApp.ts_app.R;
import com.TemanSebangkuApp.ts_app.home.Model.mdl_promo;

import java.util.List;

public class activity_dashboard extends AppCompatActivity {

    TextView txt_nama, txt_poin;
    ProgressDialog pd;
    private List<mdl_promo> list;
    private RecyclerView list_promo;
//    RecyclerView LayoutManager mManager;
    private RecyclerView list_blog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        txt_nama = findViewById(R.id.txt_nama);
        txt_poin = findViewById(R.id.txt_poin);
    }

    public void profil(){

    }

    public void loadBlog(){

    }
}
