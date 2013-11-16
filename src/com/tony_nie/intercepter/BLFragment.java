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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class BLFragment extends android.support.v4.app.Fragment {
	private final String FRAGMENT = "fragment";
	private ListView listView;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		return inflater.inflate(R.layout.layout_blacklist, container, false);

	}

	private void updateListviewData() {
		String[] strings = { "number", "phone", "SMS" };
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
			map.put("number", numbers[i]);
			map.put("phone", phone);
			map.put("SMS", sms);

			list.add(map);
		}

		return list;
	}

	private int getContactFromIntent(Intent data) {
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

		updateListviewData();

		return 0;
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case AddNewListListener.PICK_CONTACT_REQUEST:
			Log.i(FRAGMENT, "received result");
			if (Activity.RESULT_OK == resultCode)
				getContactFromIntent(data);
			break;
		default:
			break;
		}
	}
}
