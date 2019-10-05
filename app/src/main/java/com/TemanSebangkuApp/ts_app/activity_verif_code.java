package com.TemanSebangkuApp.ts_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;

public class activity_verif_code extends AppCompatActivity {

    EditText txtCodeVerifikasi;
    Button btnVerif;

    FirebaseAuth myAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verif_code);
    }


}
