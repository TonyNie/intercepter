package com.tony_nie.intercepter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class SMSLogActivity extends Activity {
	static final String SMSLOG_NUMBER = "number";
	static final String SMSLOG_USERNAME = "username";
	private ListView mListView;

	static final String SMSLOGACTIVITY = "SMSLogActivity";
	static final String SMS_CONTENT = "content://sms/";
	static final String SMS_DB_ID = "_id";
	static final String SMS_DB_ADDRESS = "address";
	static final String SMS_DB_PERSON = "person";

	public SMSLogActivity() {
		// TODO Auto-generated constructor stub
	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_smslog);

		mListView = (ListView) findViewById(R.id.listview_smslog);
		mListView.setClickable(true);
		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				HashMap<String, Object> data = (HashMap<String, Object>) mListView
						.getItemAtPosition(position);
				String number = data.get(SMSLOG_NUMBER).toString();
				String user = data.get(SMSLOG_USERNAME).toString();

				Log.i(SMSLOGACTIVITY, "Touch " + user + " " + number);
				Bundle bundle = new Bundle();
				bundle.putString(SMSLOG_NUMBER, number);
				bundle.putString(SMSLOG_USERNAME, user);

				Intent intent = new Intent();
				intent.putExtras(bundle);
				SMSLogActivity.this.setResult(RESULT_OK, intent);
				SMSLogActivity.this.finish();
			}
		});

		updateListviewData();
	}

	private void updateListviewData() {
		String[] strings = { SMSLOG_USERNAME, SMSLOG_NUMBER };
		int[] ids = { R.id.text_user_number, R.id.text_phone_number };
		SimpleAdapter simpleAdapter = new SimpleAdapter(this, getData(),
				R.layout.layout_entry_smslog, strings, ids);
		mListView.setAdapter(simpleAdapter);

	}

	private String getUserName(String number) {
		String[] projection = new String[] { Phone.NUMBER, Phone.DISPLAY_NAME };
		Uri uri = Uri.withAppendedPath(
				ContactsContract.CommonDataKinds.Phone.CONTENT_FILTER_URI,
				number);
		ContentResolver cr = getContentResolver();
		Cursor phone = cr.query(uri, projection, null, null, null);
		String userName = "";

		if (phone.moveToFirst()) {
			userName = phone
					.getString(phone
							.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
			Log.d(SMSLOGACTIVITY, number + " " + userName);
		}

		phone.close();

		return userName;
	}

	private List<HashMap<String, Object>> getData() {
		ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		String[] projection = new String[] { SMS_DB_ID, SMS_DB_ADDRESS,
				SMS_DB_PERSON };
		Uri SMS_CONTENT_URI = Uri.parse(SMS_CONTENT);

		Cursor cursor = getContentResolver().query(SMS_CONTENT_URI, projection,
				null, null, null);

		if (cursor != null) {
			HashMap<String, Boolean> numberSet = new HashMap<String, Boolean>();

			while (cursor.moveToNext()) {
				String number = cursor.getString(cursor
						.getColumnIndex(SMS_DB_ADDRESS));
				if (null == number || number.equals(""))
					continue;
				if (numberSet.get(number) != null)
					continue;
				numberSet.put(number, true);

				HashMap<String, Object> map = new HashMap<String, Object>();

				String user = getUserName(number);

				if (user == null || user.equals(""))
					user = "Unkown";
				map.put(SMSLOG_USERNAME, user);
				map.put(SMSLOG_NUMBER, number);
				// Log.i(SMSLOGACTIVITY, user + " " + number);
				list.add(map);
			}

			cursor.close();
		}

		return list;
	}

}
