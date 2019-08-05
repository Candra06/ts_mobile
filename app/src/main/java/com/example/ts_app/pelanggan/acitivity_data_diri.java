package com.example.ts_app.pelanggan;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.ts_app.R;
import com.example.ts_app.SharedPrefManager;
import com.example.ts_app.config.AppController;
import com.example.ts_app.config.ImageUtil;
import com.example.ts_app.config.ServerAPI;
import com.example.ts_app.config.authdata;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class acitivity_data_diri extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    ProgressDialog pd;
    TextView txt_status;
    EditText txt_nama, txt_email, txt_domisili, txt_alamat, txt_tgl_lahir;
    String val_tgl_lahir,tgl_lahir, val_gender, status;
    Spinner spin;
    String[] gender= {"Pilih gender", "Perempuan", "Laki-laki"};
    Button btn_simpan;
    ImageView imgback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_data_diri);

        pd = new ProgressDialog(acitivity_data_diri.this);
        imgback = (ImageView) findViewById(R.id.img_back);

        txt_nama = (EditText) findViewById(R.id.txt_nama);
        txt_email = (EditText) findViewById(R.id.txt_email);
        txt_alamat = (EditText) findViewById(R.id.txt_alamat);
        txt_tgl_lahir = (EditText) findViewById(R.id.txt_tgl_lahir);
        txt_domisili = (EditText) findViewById(R.id.txt_domisili);
        btn_simpan = (Button) findViewById(R.id.btn_simpan);
        txt_status = (TextView) findViewById(R.id.textView1);

        Bundle bundle = getIntent().getExtras();
        status = bundle.getString("status");

        spin = (Spinner) findViewById(R.id.gender);
        spin.setOnItemSelectedListener(this);
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,gender);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(aa);

        get_profil();

        if (status.equals("1")){
            txt_status.setText("Rubah data berikut jika ada kesalahan!");
        }else {
            txt_status.setText("Ayoo lengkapi dan dapatkan hadiah dari kami!");
        }

        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(acitivity_data_diri.this, activity_tab_dashboard.class);
                acitivity_data_diri.this.startActivity(intent);
            }
        });

        txt_tgl_lahir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                get_tanggal();
            }
        });

        btn_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (status.equals("1")){
                    edit_profil();
                    txt_status.setText("Rubah data berikut jika ada kesalahan!");
                }else {
                    comlete();
                    txt_status.setText("Ayoo lengkapi dan dapatkan hadiah dari kami!");
                }
            }
        });

        txt_domisili.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("tanggalnya:", txt_tgl_lahir.getText().toString());
            }
        });


    }

    public void get_tanggal(){
        int mYear, mMonth, mDay;
        Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(acitivity_data_diri.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String getbulan, gettanggal;
                if((monthOfYear + 1) >= 10){
                    getbulan = String.valueOf((monthOfYear + 1));
                }else{
                    getbulan = String.valueOf("0"+(monthOfYear + 1));
                }

                if(dayOfMonth >= 10){
                    gettanggal = String.valueOf(dayOfMonth);
                }else{
                    gettanggal = String.valueOf("0"+dayOfMonth);
                }
                val_tgl_lahir = year + "-" + getbulan + "-" + gettanggal;
                txt_tgl_lahir.setText(ImageUtil.settanggal(val_tgl_lahir));

            }
        }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }


    public void get_profil() {
        pd.setMessage("Tunggu....");
        pd.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerAPI.GET_DATA, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.dismiss();
                try {
                    JSONObject object = new JSONObject(response);
                    JSONArray data_profil = object.getJSONArray("data");
                    JSONObject data = data_profil.getJSONObject(0);
                    if (data.getString("status").equals("1")) {
                        txt_nama.setText(data.getString("nama"));
                        txt_email.setText(data.getString("email"));
                        txt_domisili.setText("");
                        txt_tgl_lahir.setText("");
                        txt_alamat.setText("");
                        spin.setPrompt("Pilih Gender");

                    } else {
                        txt_nama.setText(data.getString("nama"));
                        txt_email.setText(data.getString("email"));
                        txt_domisili.setText(data.getString("domisili"));
                        txt_tgl_lahir.setText(ImageUtil.settanggal(data.getString("tgl_lahir")));
                        txt_alamat.setText(data.getString("alamat"));
                        if (data.getString("gender").equals("1")) {
                            spin.setPrompt("Perempuan");
                        } else if (data.getString("gender").equals("2")){
                            spin.setPrompt("Laki-laki");
                        }

                    }


                } catch (JSONException e) {
                    Log.e("Erornya", e.getMessage());
                    Toast.makeText(acitivity_data_diri.this, "Masuk Gagal", Toast.LENGTH_LONG).show();
                    e.printStackTrace();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                Toast.makeText(acitivity_data_diri.this, error.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("Erronya ", error.getMessage(), error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("tipe", "profil");
                params.put("kode", authdata.getInstance(acitivity_data_diri.this).getAuth());
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        spin.setPrompt(gender[position]);
        val_gender = String.valueOf(position);
//        Toast.makeText(getApplicationContext(), gender[position], Toast.LENGTH_LONG).show();
        Log.e("val", String.valueOf(position));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        spin.setPrompt("Pilih Gender");
    }

    public void edit_profil(){
        final String nama = txt_nama.getText().toString().trim();
        final String email = txt_email.getText().toString().trim();
        final String gender = spin.getPrompt().toString().trim();
        final String tgl_lahir = txt_tgl_lahir.getText().toString().trim();
        final String domisili = txt_domisili.getText().toString().trim();
        final String alamat = txt_alamat.getText().toString().trim();

        if (tgl_lahir.equals("")|val_tgl_lahir.equals("")) {
            Toast.makeText(this, "Tanggal lahir Tidak Boleh Kosong", Toast.LENGTH_SHORT).show();
            txt_tgl_lahir.setFocusable(true);
        } else if (gender.equals("")|val_gender.equals("")) {
            Toast.makeText(this, "Kendala Masih Kosong", Toast.LENGTH_SHORT).show();
            spin.setFocusable(true);
        } else if (txt_domisili.equals("")) {
            Toast.makeText(this, "Domisili harus diisi", Toast.LENGTH_SHORT).show();
            txt_domisili.setFocusable(true);
        } else {
            pd.show();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerAPI.SAVE_DATA, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    pd.dismiss();
                    try {
                        JSONObject obj = new JSONObject(response);
                        JSONObject data = obj.getJSONObject("respon");
                        if (data.getBoolean("savedata")) {
                            Intent i = new Intent(acitivity_data_diri.this, activity_profil.class);
                            Toast.makeText(acitivity_data_diri.this, data.getString("pesan"), Toast.LENGTH_LONG).show();
                            startActivity(i);
                        } else {
                            Toast.makeText(acitivity_data_diri.this, data.getString("pesan"), Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        Toast.makeText(acitivity_data_diri.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            pd.dismiss();
                            Toast.makeText(acitivity_data_diri.this, "Error", Toast.LENGTH_LONG).show();
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("tipe", "edit");
                    params.put("gender", val_gender);
                    params.put("tgl_lahir", val_tgl_lahir);
                    params.put("domisili", domisili);
                    params.put("alamat", alamat);
                    params.put("kode_user", SharedPrefManager.getInstance(acitivity_data_diri.this).getKeyUserId());
                    params.put("kode", SharedPrefManager.getInstance(acitivity_data_diri.this).getAuthKey());
                    return params;
                }
            };
            AppController.getInstance().addToRequestQueue(stringRequest);
        }


    }

    public void comlete() {
        final String nama = txt_nama.getText().toString().trim();
        final String email = txt_email.getText().toString().trim();
        final String gender = spin.getPrompt().toString().trim();
        final String tgl_lahir = txt_tgl_lahir.getText().toString().trim();
        final String domisili = txt_domisili.getText().toString().trim();

        if (tgl_lahir.equals("")) {
            Toast.makeText(this, "Tanggal lahir Tidak Boleh Kosong", Toast.LENGTH_SHORT).show();
            txt_tgl_lahir.setFocusable(true);
//        } else if (kendala.equals("")) {
//            Toast.makeText(this, "Kendala Masih Kosong", Toast.LENGTH_SHORT).show();
//            txtKendala.setFocusable(true);
        } else if (txt_domisili.equals("")) {
            Toast.makeText(this, "Domisili harus diisi", Toast.LENGTH_SHORT).show();
            txt_domisili.setFocusable(true);
        } else {
            pd.show();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerAPI.SAVE_DATA, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    pd.dismiss();
                    try {
                        JSONObject obj = new JSONObject(response);
                        JSONObject data = obj.getJSONObject("respon");
                        if (data.getBoolean("savedata")) {
                            Intent i = new Intent(acitivity_data_diri.this, activity_profil.class);
                            Toast.makeText(acitivity_data_diri.this, data.getString("pesan"), Toast.LENGTH_LONG).show();
                            startActivity(i);
                        } else {
                            Toast.makeText(acitivity_data_diri.this, data.getString("pesan"), Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        Toast.makeText(acitivity_data_diri.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            pd.dismiss();
                            Toast.makeText(acitivity_data_diri.this, "Error", Toast.LENGTH_LONG).show();
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("tipe", "complete");
                    params.put("gender", val_gender);
                    params.put("tgl_lahir", val_tgl_lahir);
                    params.put("domisili", domisili);
                    params.put("kode_user", SharedPrefManager.getInstance(acitivity_data_diri.this).getKeyUserId());
                    params.put("kode", SharedPrefManager.getInstance(acitivity_data_diri.this).getAuthKey());
                    return params;
                }
            };
            AppController.getInstance().addToRequestQueue(stringRequest);
        }
    }
}
