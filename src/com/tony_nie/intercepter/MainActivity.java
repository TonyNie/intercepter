package com.tony_nie.intercepter;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.Window;
import android.widget.TabHost;

public class MainActivity extends FragmentActivity {
	public static final String CONFIG_PATH = "intercepter.xml";
	public static final String CONFIG_NAME = "intercepter_config";
	public static final String INTERCCPTER = "intercepter";
	private SPConfig spConfig;
	private TabHost tabHost;
	private static int currentlayout = 0;

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
		
		//requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_main);
		Log.d("Activety", "Created.");
		spConfig = new SPConfig(this, CONFIG_NAME);

		/*
		 * long begin = System.currentTimeMillis();
		 * PhoneStatReceiver.inContacts(this, "15850053917");
		 * PhoneStatReceiver.inContacts(this, "15850053721"); long end =
		 * System.currentTimeMillis(); Log.d(INTERCCPTER, "Query All cantacts: "
		 * + (end - begin));
		 */

		// testSPconfig();

		setFragment();
		changeLayout();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private void changeLayout() {

		tabHost.setCurrentTab(currentlayout);
	}

	private void setFragment() {
		tabHost = (TabHost) findViewById(R.id.tabhost);
		tabHost.setup();

		tabHost.addTab(tabHost.newTabSpec("blacklist").setIndicator("blacklist").setContent(R.id.frag_blacklist));
		tabHost.addTab(tabHost.newTabSpec("whitelist").setIndicator("whitelist").setContent(R.id.frag_whitelist));
		tabHost.addTab(tabHost.newTabSpec("setting").setIndicator("setting").setContent(R.id.frag_setting));
		

		tabHost.setCurrentTab(0);
	}

}
