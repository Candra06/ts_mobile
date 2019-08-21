package com.example.ts_app.outlet;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;

import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.Settings;
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
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.content.Context.LOCATION_SERVICE;
import static androidx.constraintlayout.motion.widget.Debug.getLocation;
import static com.example.ts_app.MapsActivity.MY_PERMISSIONS_REQUEST_LOCATION;


public class fragment_outlet extends Fragment implements OnMapReadyCallback, LocationListener {

    ProgressDialog pd;
    private List<mdl_outlet> listOutlet;
    private RecyclerView list_outlet;
    private LocationManager locationManager;
    private LocationListener listener;
    Marker mCurrLocationMarker;
    private com.example.ts_app.outlet.adapter_outlet adapter_outlet;
    RecyclerView.LayoutManager mManager;
    GoogleApiClient mGoogleApiClient;
    GoogleMap mg_map;
    MapView mapView;
    TextView txt_lokasi;
    Geocoder geocoder;
    View v;
    LocationRequest mLocationRequest;
    private Location mLocation;
    double latitude = 0, longitude = 0;

    List addresses = new ArrayList();

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

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_outlet, container, false);
        mapView = (MapView) v.findViewById(R.id.map_user);

//        getLocation();
        loadJson();
        geocoder = new Geocoder(getContext(), Locale.getDefault());
        checkLocationPermission();
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


        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (mapView != null){
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

//    @Override
//    public void onLocationChanged(Location location) {
//        try {
//            //Add initialization:
//            geocoder = new Geocoder(getContext(), Locale.getDefault());
//            //Make sure that the Location is not null:
//            if (location != null) {
//                addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 10);
//            }
//
//        } catch (IOException e) {
//            Log.e("LocateMe", "Could not get Geocoder data", e);
//
//        }
//
//        mLocation = location;
//        if (mCurrLocationMarker != null) {
//            mCurrLocationMarker.remove();
//        }
////Showing Current Location Marker on Map
//
//        if (mGoogleApiClient != null)
//            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,
//                    (com.google.android.gms.location.LocationListener) this);
//    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());

        mg_map = googleMap;
        mg_map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.addMarker(new MarkerOptions().position(new LatLng(-7.9784695,112.5617418)).title("Lokasi anda").snippet("Lokasi terkini"));

        CameraPosition Liberty = CameraPosition.builder().target(new LatLng(-7.9784695,112.5617418)).zoom(16).bearing(0).tilt(45).build();
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(Liberty));


    }

//    protected synchronized void buildGoogleApiClient() {
//        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
//                .addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this)
//                .addApi(LocationServices.API)
//                .build();
//        mGoogleApiClient.connect();
//    }

//    @SuppressLint("RestrictedApi")
//    @Override
//    public void onConnected(@Nullable Bundle bundle) {
//        mLocationRequest = new LocationRequest();
//        mLocationRequest.setInterval(1000);
//        mLocationRequest.setFastestInterval(1000);
//        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
//        if (ContextCompat.checkSelfPermission(getContext(),
//                Manifest.permission.ACCESS_FINE_LOCATION)
//                == PackageManager.PERMISSION_GRANTED) {
//            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
//                    mLocationRequest, (com.google.android.gms.location.LocationListener) getContext());
//        }
//    }

//    @Override
//    public void onConnectionSuspended(int i) {
//
//    }
//
//    @Override
//    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
//
//    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            } else {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        longitude = location.getLongitude();
        latitude = location.getLatitude();

        Log.e("Long", String.valueOf(longitude));
        Log.e("Lat", String.valueOf(latitude));
        LatLng latLng = new LatLng(latitude, longitude); //menentukan awal lokasi
        mg_map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mg_map.animateCamera(CameraUpdateFactory.zoomTo(11));
        mCurrLocationMarker = mg_map.addMarker(new MarkerOptions().position(latLng).title("Lokasi")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));
    }

    void getLocation() {
        try {
            locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
//            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5, getFragmentManager().get);
            if(!(latitude == 0) && !(longitude == 0)){
                loadJson();
                LatLng latLng = new LatLng(latitude, longitude); //menentukan awal lokasi
                mg_map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                mg_map.animateCamera(CameraUpdateFactory.zoomTo(11));
                mCurrLocationMarker = mg_map.addMarker(new MarkerOptions().position(latLng).title("Lokasi")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));
            }
        }
        catch(SecurityException e) {
            e.printStackTrace();
            Log.e("errorsatu", "errornya : " + e.getMessage());
        }
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
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
                        Log.e("Lat", String.valueOf(latitude));
                        Log.e("Lon", String.valueOf(longitude));
                        Log.e("Data tidak ditemukan", "Datanya gk keluar");
                        Toast.makeText(getContext(), "Lokasi tidak ditemukan", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    pd.cancel();
                    Log.e("Lat Lon", String.valueOf(latitude) + " " + String.valueOf(longitude));
                    Toast.makeText(getContext(), "Lokasi tidak ditemukan bray", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.cancel();
                Log.d("volley", "errornya : " + error.getMessage());
                Toast.makeText(getContext(), "Lokasi tidak ditemukan bro", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("kode", authdata.getInstance(getActivity()).getAuth());
                params.put("tipe", "distance_location");
                params.put("latitude", String.valueOf(latitude));
                params.put("longitude", String.valueOf(longitude));
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(senddata);
    }
}
