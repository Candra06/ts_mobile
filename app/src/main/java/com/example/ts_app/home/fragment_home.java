package com.example.ts_app.home;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.ts_app.R;
import com.example.ts_app.activity_input_password;
import com.example.ts_app.activity_login;
import com.example.ts_app.config.AppController;
import com.example.ts_app.config.ServerAPI;
import com.example.ts_app.config.authdata;
import com.example.ts_app.home.Adapter.Adapter_blog;
import com.example.ts_app.home.Adapter.Adapter_promo;
import com.example.ts_app.home.Model.mdl_blog;
import com.example.ts_app.home.Model.mdl_promo;
import com.example.ts_app.pelanggan.acitivity_data_diri;
import com.example.ts_app.pelanggan.activity_tab_dashboard;
import com.squareup.picasso.RequestHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class fragment_home extends Fragment {
    TextView txt_nama, txt_poin;
    ProgressDialog pd;
    private List<mdl_promo> listPromo;
    private List<mdl_blog> listBlog;
    private RecyclerView list_promo, list_blog;
    private Adapter_promo adapter_promo;
    private Adapter_blog adapter_blog;
    RecyclerView.LayoutManager mManager, mBlogManager;
    AlertDialog.Builder dialog;

    RecyclerView recyclerView;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private OnFragmentInteractionListener mListener;

    public fragment_home() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        recyclerView = (RecyclerView) recyclerView.findViewById(R.id.recycler_promo);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        pd = new ProgressDialog(getActivity());
        txt_nama = (TextView)v.findViewById(R.id.txt_nama);
        txt_poin= (TextView)v.findViewById(R.id.txt_poin);

        list_blog = (RecyclerView)v.findViewById(R.id.recycler_blog);
        list_promo = (RecyclerView)v.findViewById(R.id.rcy_promo);
        list_blog.setHasFixedSize(true);
        list_promo.setHasFixedSize(true);
        listPromo = new ArrayList<>();
        listBlog = new ArrayList<>();
        adapter_blog = new Adapter_blog(getActivity(),(ArrayList<mdl_blog>) listBlog);
        adapter_promo = new Adapter_promo(getActivity(),(ArrayList<mdl_promo>) listPromo);
        mManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL,false);
        mBlogManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL,false);
        list_blog.setLayoutManager(mBlogManager);
        list_promo.setLayoutManager(mManager);

        list_blog.setAdapter(adapter_blog);
        list_promo.setAdapter(adapter_promo);
        loadPromo();
        profil();
        loadBlog();
        return v;
    }

    private void DialogForm() {
        dialog = new AlertDialog.Builder(getContext());

        dialog.setCancelable(true);
        dialog.setMessage("Profil Kamu belum lengkap nih! lengkapi dulu yuukk, nanti dapat poin gratis loo!");


        dialog.setPositiveButton("Gass", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(getContext(), acitivity_data_diri.class);
                intent.putExtra("kode", authdata.getInstance(getContext()).getAuth());
                intent.putExtra("status", 2);
                fragment_home.this.startActivity(intent);
            }
        });

        dialog.setNegativeButton("Nanti aja lah", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void profil(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerAPI.GET_DATA, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    JSONArray data_profil = object.getJSONArray("data");
                    JSONObject data = data_profil.getJSONObject(0);

                    txt_nama.setText(data.getString("nama"));
                    txt_poin.setText(data.getString("poin")+" poin");

                    if (data.getString("status").equals(0)){
                        DialogForm();
                    }


                } catch (JSONException e) {
                    Log.e("Erornya", e.getMessage());
                    Toast.makeText(getActivity(), "Masuk Gagal", Toast.LENGTH_LONG).show();
                    e.printStackTrace();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("Erronya ", error.getMessage(), error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("kode", authdata.getInstance(getActivity()).getAuth());
                params.put("tipe", "profil");
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void loadPromo() {
        pd.setMessage("Menampilkan Data");
        pd.setCancelable(false);
        pd.show();
        StringRequest senddata = new StringRequest(Request.Method.POST, ServerAPI.GET_DATA, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject res = null;
                try {
                    res = new JSONObject(response);
                    JSONArray arr = res.getJSONArray("data");
                    if (arr.length() > 0) {
                        for (int i = 0; i < arr.length(); i++) {
                            try {
                                JSONObject data = arr.getJSONObject(i);
                                mdl_promo md = new mdl_promo();
                                md.setKode_promo(data.getString("kd_promo"));
                                md.setJudul_promo(data.getString("judul_promo"));
                                listPromo.add(md);
                            } catch (Exception ea) {
                                ea.printStackTrace();

                            }
                        }
                        pd.cancel();
                        adapter_promo.notifyDataSetChanged();
                    } else {
                        pd.cancel();
//                        not_found.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    pd.cancel();
                }
            }
        },new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pd.cancel();
                        Log.d("volley", "errornya : " + error.getMessage());
                    }
                }) {

            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("kode", authdata.getInstance(getActivity()).getAuth());
                params.put("tipe", "promo");
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(senddata);
    }

    public void loadBlog() {
        pd.setMessage("Menampilkan Data");
        pd.setCancelable(false);
        pd.show();
        StringRequest senddata = new StringRequest(Request.Method.POST, ServerAPI.GET_DATA, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject res = null;
                try {
                    res = new JSONObject(response);
                    JSONArray arr = res.getJSONArray("data");
                    if (arr.length() > 0) {
                        for (int i = 0; i < arr.length(); i++) {
                            try {
                                JSONObject data = arr.getJSONObject(i);
                                mdl_blog md = new mdl_blog();
                                md.setKode_blog(data.getString("kd_blog"));
                                md.setJudul(data.getString("judul"));
                                md.setGambar(data.getString("foto"));
                                listBlog.add(md);
                            } catch (Exception ea) {
                                ea.printStackTrace();

                            }
                        }
                        pd.cancel();
                        adapter_promo.notifyDataSetChanged();
                    } else {
                        pd.cancel();
//                        not_found.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    pd.cancel();
                }
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.cancel();
                Log.d("volley", "errornya : " + error.getMessage());
            }
        }) {

            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("kode", authdata.getInstance(getActivity()).getAuth());
                params.put("tipe", "blog");
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(senddata);
    }
}
