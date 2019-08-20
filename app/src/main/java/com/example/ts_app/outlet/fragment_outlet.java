package com.example.ts_app.outlet;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import com.google.android.gms.location.LocationListener;

import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
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
import com.example.ts_app.activity_get_location;
import com.example.ts_app.config.AppController;
import com.example.ts_app.config.ServerAPI;
import com.example.ts_app.config.authdata;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;


public class fragment_outlet extends Fragment implements OnMapReadyCallback, LocationListener {

    ProgressDialog pd;
    private List<mdl_outlet> listOutlet;
    private RecyclerView list_outlet;
    private com.example.ts_app.outlet.adapter_outlet adapter_outlet;
    RecyclerView.LayoutManager mManager;
    GoogleMap mg_map;
    MapView mapView;
    TextView txt_lokasi;
    Geocoder geocoder;

    private FusedLocationProviderClient cli;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public fragment_outlet() {
        // Required empty public constructor
    }

    public static fragment_outlet newInstance(String param1, String param2) {
        fragment_outlet fragment = new fragment_outlet();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_outlet, container, false);

        mapView = (MapView) v.findViewById(R.id.map_user);
        geocoder = new Geocoder(getContext(), Locale.getDefault());

        txt_lokasi = (TextView) v.findViewById(R.id.lokasi);

        pd = new ProgressDialog(getActivity());
        cli = LocationServices.getFusedLocationProviderClient(getContext());

        list_outlet = (RecyclerView) v.findViewById(R.id.recycler_outlet);
        list_outlet.setHasFixedSize(true);
        listOutlet = new ArrayList<>();
        adapter_outlet = new adapter_outlet(getActivity(), (ArrayList<mdl_outlet>) listOutlet);
        mManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        list_outlet.setLayoutManager(mManager);

        list_outlet.setAdapter(adapter_outlet);

        if (mapView != null) {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }
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
                                mdl_outlet md = new mdl_outlet();
                                md.setKode_outlet(data.getString("kd_outlet"));
                                md.setOutlet(data.getString("nama_outlet"));
                                md.setGambar(data.getString("foto"));
                                listOutlet.add(md);
                            } catch (Exception ea) {
                                ea.printStackTrace();

                            }
                        }
                        pd.cancel();
                        adapter_outlet.notifyDataSetChanged();
                    } else {
                        pd.cancel();
//                        not_found.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    pd.cancel();
                }
            }
        }, new Response.ErrorListener() {
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
                params.put("tipe", "outlet");
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

    @Override
    public void onMapReady(GoogleMap googleMap) {


        LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();

        if (ActivityCompat.checkSelfPermission(getContext(), ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        cli.getLastLocation().addOnSuccessListener((Activity) getContext(), new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {

                if (location != null) {
                    txt_lokasi.setText(location.toString());
                }
            }
        });
        Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
        if (location!=null){
            Toast.makeText(getContext(), "Lokasi ditemukan", Toast.LENGTH_SHORT).show();
            mg_map = googleMap;
            LatLng myLocation = new LatLng(location.getLatitude(), location.getLongitude());
            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            googleMap.addMarker(new MarkerOptions().position(myLocation).title("Lokasi ku").snippet("Jawa"));
        }else{
            Toast.makeText(getContext(), "Lokasi tidak ditemukan", Toast.LENGTH_SHORT).show();

        }

    }



    @Override
    public void onLocationChanged(Location location) {

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
