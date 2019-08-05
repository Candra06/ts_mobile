package com.example.ts_app.config;

import android.graphics.Bitmap;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

public class ImageUtil {

    public static String settanggal(String tanggal){

        String waktunya;
        String[] splittanggal = tanggal.split("-");
        String[] bulan = {"bln","Januari","Februari","Maret","April","Mei","Juni","Juli","Agustus","September","Oktober","November","Desember"};
        waktunya = splittanggal[2] +" "+bulan[Integer.parseInt(splittanggal[1])]+" "+splittanggal[0];
        return waktunya;
    }

    public static String setbulan(String tanggal){

        String waktunya;
        String[] splittanggal = tanggal.split("-");
        String[] bulan = {"bln","Januari","Februari","Maret","April","Mei","Juni","Juli","Agustus","September","Oktober","November","Desember"};
        waktunya = bulan[Integer.parseInt(splittanggal[1])];
        return waktunya;
    }

    public static String setJam(String jam){

        String waktunya;
        String[] splittanggal = jam.split(":");
        String[] bulan = {"bln","Januari","Februari","Maret","April","Mei","Juni","Juli","Agustus","September","Oktober","November","Desember"};
        waktunya = bulan[Integer.parseInt(splittanggal[1])];
        return waktunya;
    }

    public static String convert(Bitmap bitmap)
    {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);

        return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);
    }

    public static String setDay(String tanggal){

        String waktunya;
        String[] splittanggal = tanggal.split("-");
        String[] bulan = {"bln","Januari","Februari","Maret","April","Mei","Juni","Juli","Agustus","September","Oktober","November","Desember"};
        waktunya = bulan[Integer.parseInt(splittanggal[1])];
        return waktunya;
    }

    public static String settanggal_kegiatan(String tanggal){

        String waktunya;
        String[] splittanggal = tanggal.split("-");
        String[] bulan = {"bln","Januari","Februari","Maret","April","Mei","Juni","Juli","Agustus","September","Oktober","November","Desember"};
        waktunya = splittanggal[0] +" "+bulan[Integer.parseInt(splittanggal[1])]+" "+splittanggal[2];
        return waktunya;
    }
}
