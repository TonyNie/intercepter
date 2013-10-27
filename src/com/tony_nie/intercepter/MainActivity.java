package com.tony_nie.intercepter;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;

public class MainActivity extends Activity {
	public static final String CONFIG_PATH = "intercepter.xml";
	public static final String CONFIG_NAME = "intercepter_config";
	public static final String INTERCCPTER = "intercepter";
	private SPConfig spConfig;
	
    private void testSPconfig() {
    	long phone = 1580053721;
    	
    	spConfig.addNumber2Blacklist("" + phone++);
    	spConfig.addNumber2Blacklist("" + phone++);
    	spConfig.addNumber2Blacklist("" + phone++);
    	spConfig.addNumber2Blacklist("" + phone++);
    	
    	spConfig.addNumber2WhiteList("" + phone++);
    	spConfig.addNumber2WhiteList("" + phone++);
    	spConfig.addNumber2WhiteList("" + phone++);
    	spConfig.addNumber2WhiteList("" + phone++);
    	spConfig.addNumber2WhiteList("" + phone++);
    	
    	String list[] = spConfig.getBlacklist();
    	for(String str: list) {
    		Log.d(INTERCCPTER, "Blacklist:" + str);
    	}
    }
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
		Log.d("Activety", "Created.");
		spConfig = new SPConfig(this, CONFIG_NAME);
		
		testSPconfig();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
