package com.example.android.walkmyandroid;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.text.TextUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FetchAddresstask extends AsyncTask<Location, Void, String> {
    // Sama seperti fragment
    // menyimpan isi
    private Context mcontext;
    private OnTaskCompleted mListener;

    // Konstruktor
    FetchAddresstask(Context context, OnTaskCompleted listener){
        mcontext = context;
        mListener = listener;
    }

    // Proses dibelakang layar
    @Override
    protected String doInBackground(Location... locations) {
        // Mengambil data kode dari geo
        Geocoder geocoder = new Geocoder(mcontext, Locale.getDefault());
        // Lokasi
        Location location = locations[0];
        // Alamat (dapat berupa kota, provinsi, negara, jalan, dll)
        List<Address> addresses = null;
        // Hasil
        String resultMsg="";

        try {
            // mengambil data latitude dan longtitude untuk diubah menjadi alamat fisik
            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(),
                    1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Bila alamat fisik tidak ada (latitude dan longtitude tidak ada yang mencerminkan alamat fisik)
        if(addresses==null||addresses.size()==0){

            resultMsg="No Address Found !";
        }
        // Bila alamat fisik ada
        else {
            // mengambil data dari index pertama (0)
            Address address = addresses.get(0);
            // variable (array) untuk menyimpan alamat (dari jalan, kec, kot, prov, country)
            ArrayList<String> addressParts = new ArrayList<>();
            // measukan data kedalam array
            for(int i = 0; i <= address.getMaxAddressLineIndex(); i++){
                addressParts.add(address.getAddressLine(i)
                );
            }
            // merangkum dari array menjadi string utuh
            resultMsg= TextUtils.join("\n", addressParts);

        }
        return resultMsg;
    }

    @Override
    protected void onPostExecute(String s) {
        mListener.onTaskCompeleted(s);
        super.onPostExecute(s);
    }

    interface  OnTaskCompleted{
        void onTaskCompeleted(String result);
    }
}