package com.example.ts_app;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class dialog_set_lokasi extends BottomSheetDialogFragment {


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_set_location_xml, container, false);

        assert getArguments() != null;
        TextView txt_lokasi = v.findViewById(R.id.get_location);
        TextView txt_longitude = v.findViewById(R.id.get_longitude);
        TextView txt_latitude = v.findViewById(R.id.get_latitude);

        txt_lokasi.setText(getArguments().getString("alamat"));
        txt_longitude.setText(getArguments().getString("longitude"));
        txt_latitude.setText(getArguments().getString("latitude"));
        return v;
    }
}
