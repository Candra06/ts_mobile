package com.TemanSebangkuApp.ts_app.pelanggan;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.TemanSebangkuApp.ts_app.R;
import com.TemanSebangkuApp.ts_app.activity_login;
import com.TemanSebangkuApp.ts_app.activity_scan_voucher;
import com.TemanSebangkuApp.ts_app.config.authdata;
import com.TemanSebangkuApp.ts_app.scan_qr;


public class fragment_akun extends Fragment {

    ProgressDialog pd;

    CardView cardProfil, cardVoucher, cardAbout, cardKontak, cardLogout;
    ImageView imgProfil, imgVoucher, imgAbout, imgKontak, imgLogout;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public fragment_akun() {
        // Required empty public constructor
    }

    public static fragment_akun newInstance(String param1, String param2) {
        fragment_akun fragment = new fragment_akun();
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
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_akun, container, false);

        pd = new ProgressDialog(getActivity());

        cardProfil = (CardView)v.findViewById(R.id.card_profil);
        cardVoucher = (CardView)v.findViewById(R.id.card_voucher);
        cardAbout = (CardView)v.findViewById(R.id.card_about);
        cardKontak = (CardView)v.findViewById(R.id.card_contact);
        cardLogout = (CardView)v.findViewById(R.id.card_log_out);

        cardProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getActivity(), activity_profil.class);
                myIntent.putExtra("kd_auth", authdata.getInstance(getActivity()).getAuth()); //Optional parameters
                getActivity().startActivity(myIntent);
            }
        });

        cardVoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getActivity(), scan_qr.activity_my_point.class);
                myIntent.putExtra("kd_auth", authdata.getInstance(getActivity()).getAuth()); //Optional parameters
                getActivity().startActivity(myIntent);
            }
        });

        cardAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), activity_scan_voucher.class);
                getActivity().startActivity(intent);
            }
        });

        cardKontak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        cardLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authdata.getInstance(getActivity()).logout();
                Intent i = new Intent(getActivity(), activity_login.class);
                getActivity().startActivity(i);
            }
        });
        return v;
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




    public void goVoucher(){

    }

    public void goLogout(){

    }
}
