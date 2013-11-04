package com.tony_nie.intercepter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class WLFragment extends Fragment {
	private ListView listView;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_whitelist, container, false);
    }

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		listView = (ListView) getActivity().findViewById(
				R.id.listview_whitelist);
		String[] strings = { "number", "phone", "SMS" };
		int[] ids = { R.id.text_phone_number, R.id.checkbox_phone,
				R.id.checkbox_SMS };

		SimpleAdapter simpleAdapter = new SimpleAdapter(getActivity(),
				getData(), R.layout.layout_entry, strings, ids);
		listView.setAdapter(simpleAdapter);
		
	}

	private List<HashMap<String, Object>> getData() {
		ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> map = null;
		SPConfig spConfig = new SPConfig(getActivity(), SPConfig.CONFIG_NAME);
		
		String [] numbers = spConfig.getWhitelist();
		
		for (int i = 0; i < numbers.length; i++) {
			map = new HashMap<String, Object>();
			long state = spConfig.getEntryState(SPConfig.WHITELIST, numbers[i]);
			boolean phone = ((state & SPConfig.ENTRY_ENABLE_PHONE) != 0) ? true : false;
			boolean sms = ((state & SPConfig.ENTRY_ENABLE_SMS) != 0) ? true : false;
			map.put("number", numbers[i]);
			map.put("phone", phone);
			map.put("SMS", sms);

			list.add(map);
		}

		return list;
	}
}
