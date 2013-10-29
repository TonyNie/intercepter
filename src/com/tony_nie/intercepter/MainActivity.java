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

	private void printSPconfig() {
		String list[] = spConfig.getBlacklist();
		if (list != null) {
			for (String str : list) {
				Log.i(INTERCCPTER, "Blacklist:" + str);
			}
		}

		list = spConfig.getWhitelist();
		if (list != null) {
			for (String str : list) {
				Log.i(INTERCCPTER, "Whitelist:" + str);
			}
		}

		Log.i(INTERCCPTER, "Whitelist: " + spConfig.isWhitelistEnable());
		Log.i(INTERCCPTER, "Blacklist: " + spConfig.isBlacklistEnable());
	}

	private void testSPconfig() {
		long phone = 15850053721L;

		spConfig.addNumber2Blacklist("" + phone++);
		spConfig.addNumber2Blacklist("" + phone++);
		

		spConfig.addNumber2WhiteList("" + phone++);
		spConfig.addNumber2WhiteList("" + phone++);

		spConfig.setBlacklist(true);
		spConfig.setWhitelist(true);

		printSPconfig();
	}



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);
		Log.d("Activety", "Created.");
		spConfig = new SPConfig(this, CONFIG_NAME);

		/* long begin = System.currentTimeMillis();
		PhoneStatReceiver.inContacts(this, "15850053917");
		PhoneStatReceiver.inContacts(this, "15850053721");
		long end = System.currentTimeMillis();
		Log.d(INTERCCPTER, "Query All cantacts: " + (end - begin)); */
		
		//testSPconfig();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
