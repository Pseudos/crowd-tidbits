package com.banchi.googlemapsv2;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.GoogleMap.OnMyLocationChangeListener;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.text.format.Time;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class locate extends Activity  implements OnMyLocationChangeListener{
	Button next;
	
	TextView value;
	GoogleMap map;
	private double longitude;
	private double latitude;
	
	private int status;
	private String time;
	private String date;
	private String myAddress;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.locate);
		
		status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());
		
		next = (Button) findViewById(R.id.nextButton);
		value = (TextView) findViewById(R.id.value);
		
		
		
		next.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				 //TODO Auto-generated method stub
				initialize();
			}
		});
		
		
	}
	
	
	public void initialize(){
		
		if( status != ConnectionResult.SUCCESS){
			int requestCode = 10;
			Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
			dialog.show();
		}else{
			
			map = ((MapFragment) getFragmentManager().findFragmentById(R.id.fragId)).getMap();
			map.setMyLocationEnabled(true);
			map.setOnMyLocationChangeListener(this);
			
		}
		
	}


	@Override
	public void onMyLocationChange(Location loc) {
		// TODO Auto-generated method stub
		longitude = loc.getLongitude();
		latitude = loc.getLatitude();
		Calendar c = Calendar.getInstance();
        System.out.println("Current time => "+c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = df.format(c.getTime());
        getAddress(longitude,latitude);
		String Text = "My current location is: " +
		        "Latitud = " + latitude +
		        "\nLongitud = " + longitude+
		        "\nTime = "+loc.getTime()+
		        "\nBearing = "+loc.getBearing()+
		        "\nAltitude = "+loc.getAltitude()+
		        "\nAccuracy = "+loc.getAccuracy()+
		        "\nDate = "+formattedDate+
		        "\nAddress = "+myAddress;
		        
				value.setText(Text);
		        Toast.makeText( getApplicationContext(), Text, Toast.LENGTH_SHORT).show();
		
		
	}
	
	public void getAddress(double longi, double lati){
		
		 Geocoder geocoder = new Geocoder(this, Locale.ENGLISH);

	       try {
	  List<Address> addresses = geocoder.getFromLocation(lati, longi, 1);
	 
	  if(addresses != null) {
	   Address returnedAddress = addresses.get(0);
	   StringBuilder strReturnedAddress = new StringBuilder("Address:\n");
	   for(int i=0; i<returnedAddress.getMaxAddressLineIndex(); i++) {
	    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
	   }
	   myAddress = strReturnedAddress.toString();
	  }
	  else{
	   myAddress = "No Address returned!";
	  }
	 } catch (IOException e) {
	  // TODO Auto-generated catch block
	  e.printStackTrace();
	  myAddress = "Canont get Address!";
	 }
	}

}
