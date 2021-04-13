/*
 * Copyright 2017 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.walkmyandroid;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

public class MainActivity extends AppCompatActivity implements FetchAddresstask.OnTaskCompleted {

    // Varible UI
    private Button mLocationButton;
    private TextView mLocationText;

    // Variable Permission Location
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    // Untuk Menggunakan geolocation
    private FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLocationButton=findViewById(R.id.button_location);
        mLocationText=findViewById(R.id.textview_location);


        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this);

        //Ketika tombol Location di klik akan mengambil data location
        mLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLocation();
            }
        });
    }

    private void getLocation(){
        //check permission
        //Bila tidak disetujui
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){

            // Meminta permision dengan menampilkan toast
            ActivityCompat.requestPermissions(this, new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION
            }, REQUEST_LOCATION_PERMISSION);

        }else{

            // Untuk mengambil data locasi  harus ngekilik (Last Location)
          /* fusedLocationProviderClient.getLastLocation().addOnSuccessListener(
                   new OnSuccessListener<Location>() {
                       @Override
                       public void onSuccess(Location location) {
                           if(location != null){
                               new FetchAddresstask(MainActivity.this, MainActivity.this)
                                       .execute(location);
                           }
                       }
                   }); */

            // Akan mengupdate setiap frekuensi tertentu (update location)
            fusedLocationProviderClient.requestLocationUpdates(getLocationRequest(),
                    new LocationCallback(){
                        @Override
                        public void onLocationResult(LocationResult locationResult) {

                            Location location = locationResult.getLastLocation();

                            // bila locasi masih kosong akan menjalankan class fetchAddressstask
                            if(location!=null){
                                new FetchAddresstask(MainActivity.this,MainActivity.this)
                                        .execute(location);
                            }
                        }
                    }, null);
        }
    }

    //Update location
    private LocationRequest getLocationRequest(){
        LocationRequest locationRequest = new LocationRequest();
        // Setiap 5 detik akan di update
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return locationRequest;
    }

    //check permission results
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_LOCATION_PERMISSION:
                //Bila disetujui
                if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    getLocation();
                }
                //Bila tidak disetujui
                else{
                    Toast.makeText(this, "Permission Denied!",
                            Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    // Proses data
    @Override
    public void onTaskCompeleted(String result) {
        mLocationText.setText(result);
    }
}