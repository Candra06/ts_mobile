package com.TemanSebangkuApp.ts_app;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.TemanSebangkuApp.ts_app.R;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.TemanSebangkuApp.ts_app.config.AppController;
import com.TemanSebangkuApp.ts_app.config.ServerAPI;
import com.TemanSebangkuApp.ts_app.config.authdata;
import com.TemanSebangkuApp.ts_app.kasir.activity_dashboard_kasir;
import com.TemanSebangkuApp.ts_app.pelanggan.activity_tab_dashboard;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class activity_login extends AppCompatActivity {

    EditText txtNoHp;
    Spinner spinnegara;
    Button btn_login;
    ProgressDialog pd;

    FirebaseAuth auth;

    public String xdataauth;
    String codeSent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        pd = new ProgressDialog(activity_login.this);
        btn_login = findViewById(R.id.btn_join);

        spinnegara = findViewById(R.id.spinnegara);
        auth = FirebaseAuth.getInstance();

        txtNoHp = findViewById(R.id.txt_no_hp);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String no_hp = txtNoHp.getText().toString();

                if (no_hp.equals("")) {
                    Toast.makeText(activity_login.this, "Harap Masukkan Nomor HP anda", Toast.LENGTH_LONG).show();
                } else if (no_hp.length() < 11){
                    Toast.makeText(activity_login.this, "Nomor HP tidak valid", Toast.LENGTH_LONG).show();
                    txtNoHp.setError("Nomor HP tidak valid");
                    txtNoHp.requestFocus();
                    return;
                }else {
                    Cek_akun();
                }
            }
        });
    }

    private void Cek_akun() {
        pd.setMessage("Tunggu....");
        pd.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerAPI.LOGIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.dismiss();
                try {
                    JSONObject object = new JSONObject(response);
                    JSONObject data = object.getJSONObject("respon");
                    pd.cancel();

                    if (data.getBoolean("boollogin")) {

                        if (data.getInt("status") == 2) {
                            String type = "2";
                            String judul = "Masukkan password";
                            String sub_title = "Silahkan Login";
                            String no_hp = txtNoHp.getText().toString();
//                            Toast.makeText(activity_login.this, data.getString("pesan"), Toast.LENGTH_LONG).show();
                            Intent myIntent = new Intent(activity_login.this, activity_input_password.class);
                            myIntent.putExtra("no_hp", no_hp); //Optional parameters
                            myIntent.putExtra("judul", judul); //Optional parameters
                            myIntent.putExtra("sub_title", sub_title); //Optional parameters
                            myIntent.putExtra("type", type); //Optional parameters
                            activity_login.this.startActivity(myIntent);
                            Log.e("toastnya : ", "selamat datang karyawan");
                        } else if (data.getInt("status") == 3) {
                            String type = "3";
                            String judul = "Masukkan password";
                            String sub_title = "Silahkan masukkan password anda";
                            String no_hp = txtNoHp.getText().toString();
//                            Toast.makeText(activity_login.this, data.getString("pesan"), Toast.LENGTH_LONG).show();
                            Intent myIntent = new Intent(activity_login.this, activity_input_password.class);
                            myIntent.putExtra("no_hp", no_hp); //Optional parameters
                            myIntent.putExtra("judul", judul); //Optional parameters
                            myIntent.putExtra("sub_title", sub_title); //Optional parameters
                            myIntent.putExtra("type", type); //Optional parameters
                            activity_login.this.startActivity(myIntent);
                        } else if (data.getInt("status") == 0) {
                            String type = "0";
                            String judul = "Buat password anda";
                            String sub_title = "Amankan akun anda";
                            String no_hp = txtNoHp.getText().toString();
//                            Toast.makeText(activity_login.this, data.getString("pesan"), Toast.LENGTH_LONG).show();
                            Intent myIntent = new Intent(activity_login.this, activity_input_password.class);
                            myIntent.putExtra("no_hp", no_hp); //Optional parameters
                            myIntent.putExtra("judul", judul); //Optional parameters
                            myIntent.putExtra("sub_title", sub_title); //Optional parameters
                            myIntent.putExtra("type", type); //Optional parameters
                            activity_login.this.startActivity(myIntent);
                            Verifikasi();
                            Log.e("toastnya : ", "selamat datangpelanggan");
                        } else {
                            Log.e("toastnya : ", "");
                        }

                    } else {
//                        String judul = "Amankan akun anda";
//                        String sub_title = "Masukkan password anda";
//                        String no_hp = txtNoHp.getText().toString();
                        Toast.makeText(activity_login.this, "Nomor hp tidak valid " + data.getString("pesan"), Toast.LENGTH_LONG).show();
//                        Intent myIntent = new Intent(activity_login.this, activity_input_password.class);
//                        myIntent.putExtra("no_hp", no_hp); //Optional parameters
//                        myIntent.putExtra("judul", judul); //Optional parameters
//                        myIntent.putExtra("sub_title", sub_title); //Optional parameters
//                        activity_login.this.startActivity(myIntent);
//                        Toast.makeText(activity_login.this, "Akun baru "+data.getString("pesan"), Toast.LENGTH_LONG).show();
                        Log.e("toastnya : ", "selamat gagal");
                    }

                } catch (JSONException e) {
                    Log.e("Erornya", e.getMessage());
                    Toast.makeText(activity_login.this, "Masuk Gagal" + e.getMessage(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Erronya ", error.toString(), error);
                Toast.makeText(activity_login.this, error.getMessage(), Toast.LENGTH_LONG).show();


            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("no_hp", txtNoHp.getText().toString());
                params.put("tipe", "cek_nohp");
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    public void Verifikasi(){
        String phone = txtNoHp.getText().toString();

        if(phone.isEmpty()){
            txtNoHp.setError("Nomor HP harus diisi");
            txtNoHp.requestFocus();
            return;
        }

        if(phone.length() < 11 ){
            txtNoHp.setError("Nomor HP tidak valid");
            txtNoHp.requestFocus();
            return;
        }


        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phone,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
    }

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

        }

        @Override
        public void onVerificationFailed(FirebaseException e) {

        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            codeSent = s;
        }
    };



}
