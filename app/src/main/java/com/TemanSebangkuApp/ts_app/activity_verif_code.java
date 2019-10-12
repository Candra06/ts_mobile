package com.TemanSebangkuApp.ts_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class activity_verif_code extends AppCompatActivity {

    EditText txtCodeVerifikasi;
    Button btnVerif;

    FirebaseAuth myAuth;
    String VerivicationId, no_hp, title, sub_title, type, code, nomor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verif_code);

        myAuth = FirebaseAuth.getInstance();
        txtCodeVerifikasi = (EditText) findViewById(R.id.txt_verifikasi);
        Bundle bundle = getIntent().getExtras();
        no_hp = bundle.getString("no_hp");
        title = bundle.getString("title");
        sub_title = bundle.getString("sub_title");
        type = bundle.getString("type");
        Log.e("Nomor hp", no_hp);
        nomor = "+62"+no_hp;
        sendVerivicationCode(nomor);
        code = txtCodeVerifikasi.getText().toString();
        btnVerif = (Button) findViewById(R.id.btn_verif);

        btnVerif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e("Kode", txtCodeVerifikasi.getText().toString());
                if (txtCodeVerifikasi.equals("")){
                    txtCodeVerifikasi.setError("Masukkan kode verifikasi");
                    txtCodeVerifikasi.requestFocus();
                    return;
                }else {
                    verifikasi(txtCodeVerifikasi.getText().toString());
                }

            }
        });
    }

    private void verifikasi(String code){
        try {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(VerivicationId, code);
            signInWithCredential(credential);
        }catch (Exception e){
            Toast.makeText(activity_verif_code.this, "Kode verifikasi tidak valid", Toast.LENGTH_LONG).show();
        }
    }

    private void signInWithCredential(PhoneAuthCredential credential){
        myAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Intent intent = new Intent(activity_verif_code.this, activity_input_password.class);
                            intent.putExtra("title", title);
                            intent.putExtra("no_hp", no_hp);
                            intent.putExtra("sub_title", sub_title);
                            intent.putExtra("type", type);
                            Toast.makeText(activity_verif_code.this, "Kode verifikasi Berhasil", Toast.LENGTH_LONG).show();
                            startActivity(intent);
                        }else {
                            Toast.makeText(activity_verif_code.this, "Kode verifikasi tidak valid", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void sendVerivicationCode(String number){
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallBack
        );
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken){
            super.onCodeSent(s, forceResendingToken);
            VerivicationId = s;
        }
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null){

            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(activity_verif_code.this,  e.getMessage(),Toast.LENGTH_LONG).show();
        }
    };

}
