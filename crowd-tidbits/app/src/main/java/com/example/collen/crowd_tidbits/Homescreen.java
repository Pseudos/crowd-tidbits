package com.example.collen.crowd_tidbits;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by COLLEN on 11/4/2015.
 */
public class Homescreen extends Activity {
    private Button Share;
    private Button Receive;
    String username,email,password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homescreen);
        Intent rec = getIntent();

        username = rec.getStringExtra("username");
        email = rec.getStringExtra("email");
        password = rec.getStringExtra("password");
        //Toast.makeText(getApplication(), username + "---" + email +"---"+password, Toast.LENGTH_LONG).show();
        initialise();
        onclicks();
    }

    public void initialise(){
        Share = (Button) findViewById(R.id.share);
        Receive= (Button) findViewById(R.id.receive);
    }

    public void onclicks(){
        Share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent logger = new Intent(Homescreen.this, Share.class);
                logger.putExtra("username",username);
                logger.putExtra("email",email);
                logger.putExtra("password",password);
                startActivity(logger);
            }
        });

        Receive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent logger = new Intent(Homescreen.this, Receive.class);
                startActivity(logger);
            }
        });

    }
}
