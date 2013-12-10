package com.tony_nie.intercepter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.text.style.BulletSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class BLFragment extends android.support.v4.app.Fragment {
	static final int PICK_CONTACT_REQUEST = 0;
	static final int PICK_CALLLOG_REQUEST = 1;
	static final int PICK_SMSLOG_REQUEST = 2;
	static final int EDIT_NUMBER = 3;
	static final String NUMBER = "number";
	static final String PHONE = "phone";
	static final String SMS = "SMS";
	static final String DOMAIN = "DOMAIN";
	static final String ACTION = "ACTION";
	static final String ACTION_NONE = "do_nothing";
	static final String ACTION_DELETE = "delete";
	static final String ACTION_UPDATE_NUMBER = "update_number";
	static final String ACTION_UPDATE_STATE = "update_state";
	static final String ORIGINAL_NUMBER = "orginal_number";
	private final String FRAGMENT = "fragment";
	private ListView listView;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		return inflater.inflate(R.layout.layout_blacklist, container, false);

	}

	private void updateListviewData() {
		String[] strings = { NUMBER, PHONE, SMS };
		int[] ids = { R.id.text_phone_number, R.id.checkbox_phone,
				R.id.checkbox_SMS };
		SimpleAdapter simpleAdapter = new SimpleAdapter(getActivity(),
				getData(), R.layout.layout_entry, strings, ids);
		listView.setAdapter(simpleAdapter);
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		listView = (ListView) getActivity().findViewById(
				R.id.listview_blacklist);

		listView.setClickable(true);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				HashMap<String, Object> data = (HashMap<String, Object>) listView
						.getItemAtPosition(position);
				String number = data.get(NUMBER).toString();
				Boolean phone = (Boolean) data.get(PHONE);
				Boolean sms = (Boolean) data.get(SMS);
				Log.i(FRAGMENT, "Touce " + number + " " + phone + sms);
				Bundle bundle = new Bundle();
				bundle.putString(DOMAIN, SPConfig.BLACKLIST);
				bundle.putString(NUMBER, number);
				bundle.putBoolean(PHONE, phone);
				bundle.putBoolean(SMS, sms);
				Intent intent = new Intent(BLFragment.this.getActivity(),
						NumberInfoActivity.class);
				intent.putExtras(bundle);
				BLFragment.this.startActivityForResult(intent, EDIT_NUMBER);
			}
		});

		updateListviewData();

		android.support.v4.app.Fragment fragment = this;
		View.OnClickListener listnener = new AddNewListListener(fragment,
				"blacklist");
		Button button = (Button) getActivity().findViewById(
				R.id.btn_new_blacklist);
		button.setOnClickListener(listnener);

	}

	private List<HashMap<String, Object>> getData() {
		ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> map = null;
		SPConfig spConfig = new SPConfig(getActivity(), SPConfig.CONFIG_NAME);

		String[] numbers = spConfig.getBlacklist();

		for (int i = 0; i < numbers.length; i++) {
			map = new HashMap<String, Object>();
			long state = spConfig.getEntryState(SPConfig.BLACKLIST, numbers[i]);
			boolean phone = ((state & SPConfig.ENTRY_ENABLE_PHONE) != 0) ? true
					: false;
			boolean sms = ((state & SPConfig.ENTRY_ENABLE_SMS) != 0) ? true
					: false;
			map.put(NUMBER, numbers[i]);
			map.put(PHONE, phone);
			map.put(SMS, sms);

			Log.i(FRAGMENT, numbers[i] + " " + phone);
			list.add(map);
		}

		return list;
	}

	private int getNumberFromCallLog(Intent data) {
		Log.i(FRAGMENT, "get number form call log");
		Bundle bundle = data.getExtras();

		if (null == bundle)
			return 0;

		String number = bundle.getString(CallLogActivity.CALLLOG_NUMBER);

		long state = 0;
		state |= SPConfig.ENTRY_ENABLE_ALL;

		SPConfig config = new SPConfig(getActivity(), SPConfig.CONFIG_NAME);
		config.addNumber2Blacklist(number);
		config.setEntrySate(SPConfig.BLACKLIST, number, state);

		updateListviewData();

		return 0;
	}

	private int getNumberFromSMSLog(Intent data) {
		Log.i(FRAGMENT, "get number form call log");
		Bundle bundle = data.getExtras();

		if (null == bundle)
			return 0;

		String number = bundle.getString(SMSLogActivity.SMSLOG_NUMBER);

		long state = 0;
		state |= SPConfig.ENTRY_ENABLE_ALL;

		SPConfig config = new SPConfig(getActivity(), SPConfig.CONFIG_NAME);
		config.addNumber2Blacklist(number);
		config.setEntrySate(SPConfig.BLACKLIST, number, state);

		updateListviewData();

		return 0;
	}

	private int getNumberFromContacts(Intent data) {
		Uri contactUri = data.getData();
		String[] projection = { Phone.NUMBER };
		Cursor cursor = getActivity().getContentResolver().query(contactUri,
				projection, null, null, null);
		cursor.moveToFirst();
		int column = cursor.getColumnIndex(Phone.NUMBER);
		String number = cursor.getString(column);
		Log.i(FRAGMENT, "choose number for contacts:" + number);

		SPConfig config = new SPConfig(getActivity(), SPConfig.CONFIG_NAME);
		config.addNumber2Blacklist(number);

		cursor.close();

		updateListviewData();

		return 0;
	}

	private int updateNumber(Intent data) {
		Bundle bundle = data.getExtras();

		if (null == bundle)
			return 0;

		SPConfig config = new SPConfig(getActivity(), SPConfig.CONFIG_NAME);

		String action = bundle.getString(ACTION);
		String number = bundle.getString(NUMBER);
		boolean phone = bundle.getBoolean(PHONE);
		boolean sms = bundle.getBoolean(SMS);
		long state = 0;

		if (phone)
			state |= SPConfig.ENTRY_ENABLE_PHONE;
		if (sms)
			state |= SPConfig.ENTRY_ENABLE_SMS;

		Log.i(FRAGMENT, "" + number + " state: " + state);
		if (action.equals(ACTION_DELETE)) {
			config.removeNumberFromBlacklist(number);
		} else if (action.equals(ACTION_UPDATE_NUMBER)) {
			String orignal = bundle.getString(ORIGINAL_NUMBER);
			config.removeNumberFromBlacklist(orignal);
			config.addNumber2Blacklist(number);
			config.setEntrySate(SPConfig.BLACKLIST, number, state);
		} else if (action.equals(ACTION_UPDATE_STATE)) {
			config.setEntrySate(SPConfig.BLACKLIST, number, state);
		}

		updateListviewData();
		return 0;
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		Log.i(FRAGMENT, "requestcode " + requestCode);
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case PICK_CONTACT_REQUEST:
			Log.i(FRAGMENT, "received result");
			if (Activity.RESULT_OK == resultCode)
				getNumberFromContacts(data);
			break;
		case EDIT_NUMBER:
			if (null != data)
				updateNumber(data);
			break;
		case PICK_CALLLOG_REQUEST:
			if (null != data)
				getNumberFromCallLog(data);
			break;
		case PICK_SMSLOG_REQUEST:
			if (null != data)
				getNumberFromSMSLog(data);
			break;
		default:
			break;
		}
	}
}
