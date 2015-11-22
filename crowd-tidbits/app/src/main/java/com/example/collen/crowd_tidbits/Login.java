package com.example.collen.crowd_tidbits;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by COLLEN on 11/4/2015.
 */
public class Login extends Activity{
    private Button login;
    private Button register;
    private EditText username;
    private EditText password;
    public TextView resp;
    public ConnectionHandler connectionHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        connectionHandler = new ConnectionHandler();

        initialise();
        onclicks();
    }

    public void initialise(){
        login = (Button) findViewById(R.id.login);
        register= (Button) findViewById(R.id.lg_register);
        username = (EditText) findViewById(R.id.lg_username);
        password = (EditText) findViewById(R.id.lg_pass);
        resp = (TextView) findViewById(R.id.resp2);

    }

    public void onclicks(){
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String ss = connectionHandler.authenticate(username.getText().toString(),password.getText().toString());
                    String s[] = ss.split("-");
                    String str = s[1];
                    if(s[0].equals("Authenticated")){
                        Intent logger = new Intent(Login.this,Homescreen.class);
                        logger.putExtra("username",username.getText().toString());
                        logger.putExtra("password", password.getText().toString());
                        logger.putExtra("email",str);
                        startActivity(logger);
                        finish();

                    }else{
                        resp.setText(ss);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent logger = new Intent(Login.this,Register.class);
                startActivity(logger);
            }
        });

    }
}
