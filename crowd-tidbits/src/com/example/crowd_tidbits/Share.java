package com.example.crowd_tidbits;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

public class Share extends Activity {
	public EditText tidbit_msg;
	public RadioButton low, medium, high, critical;
	public Button share_tidbit;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.share);
	}
	

}
