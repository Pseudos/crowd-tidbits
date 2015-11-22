package com.example.collen.crowd_tidbits;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by COLLEN on 11/17/2015.
 */
public class Home extends Activity {
    private Button share, receive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        initialise();
        onclicks();
    }
    public void initialise(){
        share = (Button) findViewById(R.id.login);
        receive= (Button) findViewById(R.id.lg_register);


    }

    public void onclicks(){
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent logger = new Intent(Home.this, Share.class);
                startActivity(logger);
            }
        });

        receive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent logger = new Intent(Home.this, Receive.class);
                startActivity(logger);
            }
        });

    }
}
