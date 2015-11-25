package com.example.collen.crowd_tidbits;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by COLLEN on 11/4/2015.
 */
public class Receive extends Activity implements GoogleMap.OnMyLocationChangeListener{

    private GoogleMap googleMap;
    private double latitude,longitude;
    int priority = 0;
    RadioGroup priorityRG;
    public GoogleMap map;
    Button receive;
    int status;


    ConnectionHandler connectionhandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.receive);

        try{
            LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
            boolean enabled = service.isProviderEnabled(LocationManager.GPS_PROVIDER);
            if (!enabled) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                Toast.makeText(this, "Enabled :" + enabled, Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());
        connectionhandler = new ConnectionHandler();
        receive = (Button) findViewById(R.id.rec_view);
        priorityRG = (RadioGroup) findViewById(R.id.rec_priority);

        try{
            intialiseLocation();

            priorityRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    View radioButton = group.findViewById(checkedId);
                    priority = group.indexOfChild(radioButton) + 1;
                }
            });


            receive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                       // intialiseLocation();
                        Toast.makeText(getApplicationContext(),"Spots of priority "+priority+" are selected",Toast.LENGTH_LONG).show();
                        getPosts(priority);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });


        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    public void getPosts(int p) throws Exception {

        List<ConnectionHandler.Post> listOfPosts = new ArrayList<ConnectionHandler.Post>();
        listOfPosts = connectionhandler.radius(latitude+"",longitude+"",p+"");


        if(listOfPosts != null) {
            if (map == null) {
                map = ((MapFragment) getFragmentManager().findFragmentById(R.id.rec_map)).getMap();
            }
            map.setMapType(GoogleMap.MAP_TYPE_HYBRID);

            for (ConnectionHandler.Post post : listOfPosts) {
              //  if(post.getPriority() ==  p) {
                    LatLng lat_logi = new LatLng(post.getLatitude(), post.getLongitude());
                    map.addMarker(new MarkerOptions().position(lat_logi).title(post.description  + "\n Priority: " + post.getPriority()));
                    map.addMarker(new MarkerOptions().position(lat_logi).snippet("\n Date: " + post.getPostTime()));
                    CameraPosition cameraPosition = new CameraPosition.Builder().target(lat_logi).zoom(14.0f).build();
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
                    map.moveCamera(cameraUpdate);
                //  }
            }


        }
        else
        {
            Toast.makeText(getApplicationContext(), "Sorry there are no hot spots around this location", Toast.LENGTH_LONG).show();;
        }
    }


    public void intialiseLocation(){
        if(status != ConnectionResult.SUCCESS){
            int resquestCode = 10;
            Dialog dialog  = GooglePlayServicesUtil.getErrorDialog(status, this, resquestCode);
            dialog.show();

        }else{
            map = ((MapFragment) getFragmentManager().findFragmentById(R.id.rec_map)).getMap();
            map.setMyLocationEnabled(true);
            map.setOnMyLocationChangeListener(this);
        }
    }

    @Override
    public void onMyLocationChange(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
    }
}
