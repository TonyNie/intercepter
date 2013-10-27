package com.tony_nie.intercepter;

import java.lang.reflect.Method;
import java.util.ArrayList;

import com.android.internal.telephony.ITelephony;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.media.AudioManager;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;
import android.util.Log;

public class PhoneStatReceiver extends BroadcastReceiver {

	String TAG = "State";
	TelephonyManager telMgr;
	private IntercepterConfig config;

	@Override
	public void onReceive(Context context, Intent intent) {

		try {
			config = new IntercepterConfig(MainActivity.CONFIG_PATH);
		} catch (Exception e) {

		}
		// TODO Auto-generated method stub
		telMgr = (TelephonyManager) context
				.getSystemService(Service.TELEPHONY_SERVICE);
		switch (telMgr.getCallState()) {
		case TelephonyManager.CALL_STATE_RINGING:
			porcessRinging(context, intent);
			break;
		case TelephonyManager.CALL_STATE_OFFHOOK:
			break;
		case TelephonyManager.CALL_STATE_IDLE:
			break;
		}
	}

	private void endCall() {
		Class<TelephonyManager> c = TelephonyManager.class;
		try {
			Method getITelephonyMethod = c.getDeclaredMethod("getITelephony",
					(Class[]) null);
			getITelephonyMethod.setAccessible(true);
			ITelephony iTelephony = null;
			Log.e(TAG, "End call.");
			iTelephony = (ITelephony) getITelephonyMethod.invoke(telMgr,
					(Object[]) null);
			iTelephony.endCall();
		} catch (Exception e) {
			Log.e(TAG, "Fail to answer ring call.", e);
		}
	}

	private ArrayList<String> getCantacts(Context context) {
		ArrayList<String> numList = new ArrayList<String>();
		// 得到ContentResolver对象
		ContentResolver cr = context.getContentResolver();
		// 取得电话本中开始一项的光标
		Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null,
				null, null, null);
		while (cursor.moveToNext()) {
			// 取得联系人ID
			String contactId = cursor.getString(cursor
					.getColumnIndex(ContactsContract.Contacts._ID));
			Cursor phone = cr.query(
					ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
					ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = "
							+ contactId, null, null);
			// 取得电话号码(可能存在多个号码)
			while (phone.moveToNext()) {
				String strPhoneNumber = phone
						.getString(phone
								.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
				numList.add(strPhoneNumber);
				Log.v("tag", "strPhoneNumber:" + strPhoneNumber);
			}

			phone.close();
		}
		cursor.close();
		return numList;
	}

	private boolean isInContacts(Context context, String phone) {
		return getCantacts(context).contains(phone);
	}

	private void porcessRinging(Context context, Intent intent) {
		boolean stop = false;
		String phone = intent
				.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
		Log.v(TAG, "number:" + phone);

		if (config.isWhitelistEnable()
				&& (config.isInWhitelist(phone) || isInContacts(context,
						phone))) {
			Log.v(TAG, "" + phone + " in whitelist or address book");
			stop = false;
			return;

		} else {
			stop = true;
		}
		
		if (config.isBlacklistEnable() && config.isInBlacklist(phone)) {
			stop = true;
		} else {
			stop = false;
		}
		
		if (stop) {
			SharedPreferences phonenumSP = context.getSharedPreferences(
					"in_phone_num", Context.MODE_PRIVATE);
			Editor editor = phonenumSP.edit();
			editor.putString(phone, phone);
			editor.commit();

			AudioManager am = (AudioManager) context
					.getSystemService(Context.AUDIO_SERVICE);
			int mode = am.getRingerMode();
			am.setRingerMode(AudioManager.RINGER_MODE_SILENT);

			try {
				Thread.sleep(200);
			} catch (Exception ex) {

			}

			endCall();

			am.setRingerMode(mode);
		}

	}
}
