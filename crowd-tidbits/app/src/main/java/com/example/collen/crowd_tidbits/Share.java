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
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;

/**
 * Created by COLLEN on 11/4/2015.
 */
public class Share extends Activity implements GoogleMap.OnMyLocationChangeListener {
    public EditText message;
    RadioGroup priority;
    Button share;
    public int status;
    public GoogleMap map;
    public  double longitude, latitude;
    public int priority_index = 0;
    public ConnectionHandler connectionHandler;
    public String username,email,password;
    TextView resp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.share);

        connectionHandler = new ConnectionHandler();

        Intent rec = getIntent();

        username = rec.getStringExtra("username");
        email = rec.getStringExtra("email");
        password = rec.getStringExtra("password");

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

        try{
            intialiseLocation();
            initialise();
            onClick();

        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    public void initialise(){
        message = (EditText)findViewById(R.id.tidbit_msg);
        priority = (RadioGroup) findViewById(R.id.share_priority);
        share = (Button) findViewById(R.id.share_tidbits);
        resp = (TextView) findViewById(R.id.resp1);
        priority.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                View radioButton = group.findViewById(checkedId);
                priority_index = group.indexOfChild(radioButton)+1;
            }
        });
    }

    public void onClick(){
       share.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if(validate()) {
                   Toast.makeText(getApplicationContext(), longitude + " ** " + latitude + " message: " + message.getText().toString() + "  priority: " + priority_index, Toast.LENGTH_LONG).show();
                   try {
                       String r = connectionHandler.submit(message.getText().toString(),latitude+"",longitude+"",username,email,password,priority_index+"");
                       resp.setText(r);
                   } catch (Exception e) {
                       e.printStackTrace();
                   }

               }else{

                   Toast.makeText(getApplicationContext(), "Please fill all fields", Toast.LENGTH_LONG).show();

               }
           }
       });

    }
     public void intialiseLocation(){
         if(status != ConnectionResult.SUCCESS){
             int resquestCode = 10;
             Dialog dialog  = GooglePlayServicesUtil.getErrorDialog(status, this, resquestCode);
             dialog.show();

         }else{
             map = ((MapFragment) getFragmentManager().findFragmentById(R.id.fragId)).getMap();
             map.setMyLocationEnabled(true);
             map.setOnMyLocationChangeListener(this);
         }
     }

    @Override
    public void onMyLocationChange(Location location) {

         try{
             longitude = location.getLongitude();
             latitude = location.getLatitude();

         }catch(Exception ex){
             ex.printStackTrace();
         }

    }

    public boolean validate(){

        if(message.getText().toString().equals(null)|| message.getText().toString().isEmpty()){
            return false;
        }else if(priority_index < 1){
            return false;
        }
        else
            return true;
    }
}
