package com.tony_nie.intercepter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SimpleAdapter;
import android.widget.TextView;

/* deprecated */
public class ListAdapter extends SimpleAdapter {

	private LayoutInflater mInflater;

	public ListAdapter(Context context, List<? extends Map<String, ?>> data,
			int resource, String[] from, int[] to) {
		super(context, data, resource, from, to);
		this.mInflater = LayoutInflater.from(context);

	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (null == convertView) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.layout_entry, null);
			holder.phone = (TextView) convertView
					.findViewById(R.id.text_phone_number);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.deleteBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				SPConfig config = new SPConfig(mInflater.getContext(),
						SPConfig.CONFIG_NAME);
				ViewHolder holder = (ViewHolder) v.getTag();
				String number = holder.phone.getText().toString();
				config.removeNumberFromBlacklist(number);
			}
		});

		return convertView;
	}

	public final class ViewHolder {
		public TextView phone;
		public Button deleteBtn;
	};

}
