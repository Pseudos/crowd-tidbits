package com.banchi.googlemapsv2;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;

import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private GoogleMap googleMap;
	private double longitude;
	private double latitude;
	private float time;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		try{
			
			initilizeMap();
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	private void initilizeMap() {
	     if (googleMap == null) {
	         googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
	         googleMap.setMyLocationEnabled(true);
	         
	         latitude = googleMap.getMyLocation().getLatitude();
	         longitude = googleMap.getMyLocation().getLongitude();
	         time = googleMap.getMyLocation().getTime();
	         
	         Toast.makeText(getApplicationContext(), "LOngitude : "+longitude+" Latitude : "+latitude+" Time : "+time, Toast.LENGTH_SHORT).show();
	          // check if map is created successfully or not
            if (googleMap == null) {
            	Toast.makeText(getApplicationContext(), "Sorry! unable to create maps", Toast.LENGTH_SHORT).show();
	            }
	        }
	   
	}


	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		initilizeMap();
	}
	
	
	

	
}
