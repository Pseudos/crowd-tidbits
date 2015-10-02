package com.banchi.googlemapsv2;

import com.google.android.gms.maps.GoogleMap.OnMyLocationChangeListener;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class locat extends Activity{
	
	private double longitude;
	private double latitude;
	Button next;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.locate);
		next = (Button)findViewById(R.id.nextButton);
		next.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				LocationManager locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
				
				LocationListener locationListener = new MyLocation();
				locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
				
				String Text = "My current location is: " +
				        "Latitud = " + latitude +
				        "Longitud = " + longitude;

				        Toast.makeText( getApplicationContext(), Text, Toast.LENGTH_SHORT).show();
			}
		});
		
	}
	
	public class MyLocation implements LocationListener{

		@Override
		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub
			longitude = location.getLongitude();
			latitude = location.getLatitude();
			
			      
			
		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			  Toast.makeText( getApplicationContext(), "Gps Disabled", Toast.LENGTH_SHORT ).show();
		      
		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			  Toast.makeText( getApplicationContext(), "Gps Enabled", Toast.LENGTH_SHORT).show();
		      
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
			
		}
		
	}

}
