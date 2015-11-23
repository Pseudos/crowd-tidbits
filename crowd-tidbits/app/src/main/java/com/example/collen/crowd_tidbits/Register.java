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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private Pattern passPattern;
    private Matcher matcher;
    private TextView respo;
    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static final String PASSWORD_PATTERN =
            "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$&%*+-]).{8,20})";


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
                    Intent logger = new Intent(Register.this, Login.class);
                    startActivity(logger);
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fieldsValidation()) {
                    try {

                        String res = connectionHandler.register(username.getText().toString(), email.getText().toString(), password.getText().toString());
                        Toast.makeText(getApplication(), res, Toast.LENGTH_LONG).show();
                        respo.setText(res);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else{

                }
            }
        });

    }

    public boolean fieldsValidation(){

        if(username.getText().toString().trim().length() == 0){
            username.requestFocus();
            username.setError("username is empty");
            return false;
        }

        if(cpassword.getText().toString().trim().length() == 0){
            cpassword.requestFocus();
            cpassword.setError("confirm password is empty");
            return false;
        }

        if(!cpassword.getText().toString().trim().equals(password.getText().toString().trim())){
            cpassword.requestFocus();
            cpassword.setError("passwords not the same");
            return false;
        }

        if(email.getText().toString().trim().length() == 0 || !validateEmail(email.getText().toString().trim()) ){
            email.requestFocus();
            email.setError("sorry, invalid email");
            return false;
        }


        if(password.getText().toString().trim().length() == 0 || !validatePassword(password.getText().toString().trim())) {
            password.requestFocus();
            password.setError("sorry, UpperCase, LowerCase, Numbers and Special Characters in a String. It must have 8 characters minimum");
            return false;
        }

        return true;
    }

    public boolean validatePassword(String password1){

        passPattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = passPattern.matcher(password1);
        return matcher.matches();

    }

    public boolean validateEmail(String str){
        passPattern = Pattern.compile(EMAIL_PATTERN);
        matcher = passPattern.matcher(str);
        return matcher.matches();
    }



}
