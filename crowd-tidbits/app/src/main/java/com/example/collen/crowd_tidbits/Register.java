package com.example.collen.crowd_tidbits;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.InputStream;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by COLLEN on 11/4/2015.
 */
public class Register extends Activity {
    public ConnectionHandler connectionHandler;
    public String myResponse = "";
    private Button back,register;
    private EditText username,email, password,cpassword;
    private TextView respo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        connectionHandler = new ConnectionHandler();
        initialise();
        onclicks();
    }

    public void initialise(){
        back = (Button) findViewById(R.id.rg_back);
        register= (Button) findViewById(R.id.rg_register);
        username = (EditText) findViewById(R.id.rg_username);
        password = (EditText) findViewById(R.id.rg_pass);
        cpassword = (EditText) findViewById(R.id.rg_cpass);
        email = (EditText) findViewById(R.id.rg_email);
        respo = (TextView) findViewById(R.id.resp);

    }

    public void onclicks(){
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //connectionHandler.register(username.getText().toString(),email.getText().toString(),password.getText().toString());
                    Toast.makeText(getApplication(),"Test new features",Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    String res = connectionHandler.register(username.getText().toString(),email.getText().toString(),password.getText().toString());
                    Toast.makeText(getApplication(), res ,Toast.LENGTH_LONG).show();
                    respo.setText(res);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }



}
