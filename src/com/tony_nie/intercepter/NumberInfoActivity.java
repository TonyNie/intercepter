package com.tony_nie.intercepter;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

public class NumberInfoActivity extends Activity {

	TextView mNumberView;
	CheckBox mPhoneBox;
	CheckBox mSMSBox;
	Button mDeleteButton;
	SPConfig mSPConfig;

	public NumberInfoActivity() {
		// TODO Auto-generated constructor stub
	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_num_info);

		mSPConfig = new SPConfig(this, SPConfig.CONFIG_NAME);
		Bundle bundle = this.getIntent().getExtras();
		TextView mNumView = (TextView) findViewById(R.id.text_phone_number);
		CheckBox mPhoneBox = (CheckBox) findViewById(R.id.checkbox_phone);
		CheckBox mSMSBox = (CheckBox) findViewById(R.id.checkbox_SMS);
		Button mDeButton = (Button) findViewById(R.id.btn_delete_number);

		mNumView.setText(bundle.getString(BLFragment.NUMBER));
		mPhoneBox.setChecked(bundle.getBoolean(BLFragment.PHONE));
		mSMSBox.setChecked(bundle.getBoolean(BLFragment.SMS));
		this.setTitle(bundle.getString(BLFragment.DOMAIN));

		mDeButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				
				Bundle bundle = NumberInfoActivity.this.getIntent().getExtras();
				String number = bundle.getString(BLFragment.NUMBER);
				String domain = bundle.getString(BLFragment.DOMAIN);
				
				if (domain.equals(SPConfig.BLACKLIST))
					mSPConfig.removeNumberFromBlacklist(number);
				else if (domain.equals(SPConfig.WHITELIST))
					mSPConfig.removeNumberFromWhitelist(number);
			}
		});
	}
}
