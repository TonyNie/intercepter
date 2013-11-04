package com.tony_nie.intercepter;

import java.lang.reflect.Method;

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
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.telephony.TelephonyManager;
import android.util.Log;

public class PhoneStatReceiver extends BroadcastReceiver {

	String TAG = "State";
	TelephonyManager telMgr;
	private SPConfig config;

	@Override
	public void onReceive(Context context, Intent intent) {

		try {
			config = new SPConfig(context, SPConfig.CONFIG_NAME);
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


	static public  boolean inContacts(Context context, String number) {
		String[] projection = new String[] { Phone.NUMBER };
		ContentResolver cr = context.getContentResolver();
		Cursor phone = cr.query(
				ContactsContract.CommonDataKinds.Phone.CONTENT_URI, projection,
				null, null, null);

		while (phone.moveToNext()) {
			String __number = phone
					.getString(phone
							.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
			if (__number.contains(number) || number.contains(__number)) {
				Log.d("Query Cantacts", number + " Match " + __number);
				return true;
			}
		}
		
		Log.d("Query Cantacts", number + " does not exist in Contacts ");
		phone.close();

		return false;
	}
	
	private void porcessRinging(Context context, Intent intent) {
		boolean stop = false;
		String phone = intent
				.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
		Log.v(TAG, "number:" + phone);

		long begin = System.currentTimeMillis();
		if (inContacts(context, phone)) {
			Log.i(TAG, "" + phone + " in address book");
			return;
		}
		long end = System.currentTimeMillis();
		Log.v(TAG, "query Contact: " + (end - begin));
		
		if (config.isBlacklistEnable()) {
			if (config.isInBlacklist(phone)) {
				long state = config.getEntryState(SPConfig.BLACKLIST, ""
						+ phone);
				if ((state & SPConfig.ENTRY_ENABLE_PHONE) == 1)
					stop = true;
				else
					stop = false;
			} else {
				stop = false;
			}
		}
		
		if (config.isWhitelistEnable()) {
			if (config.isInWhitelist(phone)) {
				long state = config.getEntryState(SPConfig.WHITELIST, ""
						+ phone);
				Log.v(TAG, "" + phone + " in whitelist");

				if ((state & SPConfig.ENTRY_ENABLE_PHONE) == 1) {
					stop = false;
					return;
				} else {
					stop = true;
				}
			} else {
				stop = true;
			}
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
