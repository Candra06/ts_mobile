package com.example.ts_app.menu;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.ts_app.R;
import com.example.ts_app.config.AppController;
import com.example.ts_app.config.ServerAPI;
import com.example.ts_app.config.authdata;
import com.example.ts_app.menu.adapter_menu;
import com.example.ts_app.menu.mdl_menu;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class fragment_menu extends Fragment {

    ProgressDialog pd;
    private List<mdl_menu> listMenu;
    private RecyclerView list_menu;
    private adapter_menu adapter_menu;
    RecyclerView.LayoutManager mManager;

    private OnFragmentInteractionListener mListener;

    public fragment_menu() {
        // Required empty public constructor
    }

    public static fragment_menu newInstance(String param1, String param2) {
        fragment_menu fragment = new fragment_menu();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @SuppressLint("WrongConstant")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_menu, container, false);

        pd = new ProgressDialog(getActivity());

        list_menu = (RecyclerView)v.findViewById(R.id.recycler_menu);
        list_menu.setHasFixedSize(true);
        listMenu = new ArrayList<>();
        adapter_menu = new adapter_menu(getActivity(),(ArrayList<mdl_menu>) listMenu);
        mManager = new GridLayoutManager(getActivity(),2);
        list_menu.setLayoutManager(mManager);

        list_menu.setAdapter(adapter_menu);
        loadJson();
        return v;
    }

    public void loadJson() {
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
                                mdl_menu md = new mdl_menu();
                                md.setKode_menu(data.getString("kd_menu"));
                                md.setMenu(data.getString("menu"));
                                md.setGambar(data.getString("foto"));
                                Log.e("kd", data.getString("kd_menu"));
                                Log.e("nama", data.getString("menu"));
                                listMenu.add(md);
                            } catch (Exception ea) {
                                ea.printStackTrace();

                            }
                        }
                        pd.cancel();
                        adapter_menu.notifyDataSetChanged();
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
                params.put("tipe", "menu");
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(senddata);
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
}
