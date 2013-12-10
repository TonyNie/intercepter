package com.tony_nie.intercepter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class CallLogActivity extends Activity {
	static final String CALLLOG_NUMBER = "number";
	static final String CALLLOG_USERNAME = "username";
	private ListView mListView;

	static final String CALLLOGACTIVITY = "CallLogActivity";
	public CallLogActivity() {
		// TODO Auto-generated constructor stub
	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_calllog);

		mListView = (ListView) findViewById(R.id.listview_calllog);
		mListView.setClickable(true);
		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				HashMap<String, Object> data = (HashMap<String, Object>) mListView
						.getItemAtPosition(position);
				String number = data.get(CALLLOG_NUMBER).toString();
				String user = data.get(CALLLOG_USERNAME).toString();
				
				Log.i(CALLLOGACTIVITY, "Touch " + user + " " + number);
				Bundle bundle = new Bundle();
				bundle.putString(CALLLOG_NUMBER, number);
				bundle.putString(CALLLOG_USERNAME, user);
				
				Intent intent = new Intent();
				intent.putExtras(bundle);
				CallLogActivity.this.setResult(RESULT_OK, intent);
				CallLogActivity.this.finish();
			}
		});
		
		updateListviewData();
	}

	private void updateListviewData() {
		String[] strings = { CALLLOG_USERNAME, CALLLOG_NUMBER };
		int[] ids = { R.id.text_user_number, R.id.text_phone_number };
		SimpleAdapter simpleAdapter = new SimpleAdapter(this, getData(),
				R.layout.layout_entry_calllog, strings, ids);
		mListView.setAdapter(simpleAdapter);

	}

	private List<HashMap<String, Object>> getData() {
		ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();

		String[] callLogFields = { android.provider.CallLog.Calls._ID,
				android.provider.CallLog.Calls.CACHED_NAME,
				android.provider.CallLog.Calls.NUMBER };
		Cursor cursor = getContentResolver().query(
				android.provider.CallLog.Calls.CONTENT_URI, callLogFields,
				null, null, null);

		if (cursor != null) {
			HashMap<String, Boolean> numberSet = new HashMap<String, Boolean>();

			while (cursor.moveToNext()) {
				String number = cursor.getString(cursor
						.getColumnIndex(android.provider.CallLog.Calls.NUMBER));
				if (null == number || number.equals(""))
					continue;
				if (numberSet.get(number) != null)
					continue;
				numberSet.put(number, true);

				HashMap<String, Object> map = new HashMap<String, Object>();

				String user = cursor
						.getString(cursor
								.getColumnIndex(android.provider.CallLog.Calls.CACHED_NAME));

				if (user == null || user.equals(""))
					user = "Unkown";
				map.put(CALLLOG_USERNAME, user);
				map.put(CALLLOG_NUMBER, number);

				list.add(map);
			}

			cursor.close();
		}

		return list;
	}

}
