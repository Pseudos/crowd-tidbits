package com.example.collen.crowd_tidbits;

import android.app.Activity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by COLLEN on 11/4/2015.
 */
public class Receive extends Activity{
    public LatLng lat_logi = new LatLng(21,57);
    public LatLng lat_logi1 = new LatLng(51,37);
    public LatLng lat_logi2 = new LatLng(23,7);
    public LatLng lat_logi3 = new LatLng(31,97);
    private GoogleMap googleMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.receive);

        try{
            if(googleMap == null){
                googleMap = ( (MapFragment) getFragmentManager().findFragmentById(R.id.rec_map)).getMap();
            }
            googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            googleMap.addMarker(new MarkerOptions().position(lat_logi).title("Crowd-tidbit demo"));
            googleMap.addMarker(new MarkerOptions().position(lat_logi1).title("Crowd-tidbit demo 1"));
            googleMap.addMarker(new MarkerOptions().position(lat_logi2).title("Crowd-tidbit demo 2"));
            googleMap.addMarker(new MarkerOptions().position(lat_logi3).title("Crowd-tidbit demo 3"));


        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
}
