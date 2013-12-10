package com.tony_nie.intercepter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

public class NumberInfoActivity extends Activity {

	TextView mNumberView;
	CheckBox mPhoneBox;
	CheckBox mSMSBox;
	Button mDeleteButton;
	Button mOkButton;
	SPConfig mSPConfig;
	Bundle mResultBundle;

	public NumberInfoActivity() {
		// TODO Auto-generated constructor stub
	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_num_info);

		mSPConfig = new SPConfig(this, SPConfig.CONFIG_NAME);
		Bundle bundle = this.getIntent().getExtras();
		mResultBundle = new Bundle(bundle);
		mResultBundle.putString(BLFragment.ACTION, BLFragment.ACTION_NONE);

		TextView mNumView = (TextView) findViewById(R.id.text_phone_number);
		CheckBox mPhoneBox = (CheckBox) findViewById(R.id.checkbox_phone);
		CheckBox mSMSBox = (CheckBox) findViewById(R.id.checkbox_SMS);
		mDeleteButton = (Button) findViewById(R.id.btn_delete_number);
		mOkButton = (Button) findViewById(R.id.btn_ok);

		mNumView.setText(bundle.getString(BLFragment.NUMBER));
		mPhoneBox.setChecked(bundle.getBoolean(BLFragment.PHONE));
		mSMSBox.setChecked(bundle.getBoolean(BLFragment.SMS));
		this.setTitle(bundle.getString(BLFragment.DOMAIN));

		mDeleteButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				Intent intent = new Intent();

				mResultBundle.putString(BLFragment.ACTION,
						BLFragment.ACTION_DELETE);

				intent.putExtras(mResultBundle);
				NumberInfoActivity.this.setResult(RESULT_OK, intent);
				NumberInfoActivity.this.finish();
				/*
				 * String number = bundle.getString(BLFragment.NUMBER); String
				 * domain = bundle.getString(BLFragment.DOMAIN);
				 * 
				 * if (domain.equals(SPConfig.BLACKLIST))
				 * mSPConfig.removeNumberFromBlacklist(number); else if
				 * (domain.equals(SPConfig.WHITELIST))
				 * mSPConfig.removeNumberFromWhitelist(number);
				 */
			}
		});

		mOkButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.putExtras(mResultBundle);
				NumberInfoActivity.this.setResult(RESULT_OK, intent);
				NumberInfoActivity.this.finish();
			}
		});

		CompoundButton.OnCheckedChangeListener listener = new CompoundButton.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {

				mResultBundle.putString(BLFragment.ACTION,
						BLFragment.ACTION_UPDATE_STATE);

				switch (buttonView.getId()) {
				case R.id.checkbox_phone:
					mResultBundle.putBoolean(BLFragment.PHONE, isChecked);
					break;
				case R.id.checkbox_SMS:
					mResultBundle.putBoolean(BLFragment.SMS, isChecked);
					break;
				default:
					return;
				}
			}

		};

		mPhoneBox.setOnCheckedChangeListener(listener);
		mSMSBox.setOnCheckedChangeListener(listener);
	}
}
