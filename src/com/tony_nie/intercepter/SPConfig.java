package com.tony_nie.intercepter;

import java.util.Map;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class SPConfig {
	static final String BLACKLIST = "blacklist";
	static final String WHITELIST = "whitelist";
	static final String PHONENUMBER = "phoneNumber";
	static final String INTERCEPTER = "intercepter";
	static final String INDICATOR = "_";
	static final String ENTRY_ENABLE = "enable";
	static final String ENTRY_DISABLE = "disable";
	static final long ENTRY_ENABLE_PHONE = 1 << 0;
	static final long ENTRY_ENABLE_SMS = 1 << 1;
	static final long ENTRY_ENABLE_ALL = (ENTRY_ENABLE_PHONE | ENTRY_ENABLE_SMS);
	static final long ENTRY_DISABLE_ALL = 0;
	public static final String CONFIG_NAME = "intercepter_config";
	
	private SharedPreferences spPreferences;

	public SPConfig(Context context, String config) {
		spPreferences = context.getSharedPreferences(config,
				Context.MODE_PRIVATE);
		
		if (!spPreferences.contains(BLACKLIST))
			setBlacklist(false);
		
		if (!spPreferences.contains(WHITELIST))
			setWhitelist(false);
	}

	private int removePhoneNumber(String type, String phone) {
		Editor editor = spPreferences.edit();
		editor.remove(type + INDICATOR + phone);
		editor.commit();

		return 0;
	}

	public void removeNumberFromBlacklist(String phone) {
		removePhoneNumber(BLACKLIST, phone);
	}

	public void removeNumberFromWhitelist(String phone) {
		removePhoneNumber(WHITELIST, phone);
	}

	private int addPhoneNumber(String type, String phone) {
		Editor editor = spPreferences.edit();
		editor.putLong(type + INDICATOR + phone, ENTRY_ENABLE_ALL);
		editor.commit();

		return 0;
	}

	public int addNumber2Blacklist(String phone) {
		return addPhoneNumber(BLACKLIST, phone);
	}

	public int addNumber2WhiteList(String phone) {
		return addPhoneNumber(WHITELIST, phone);
	}

	private void clearList(String type) {
		Editor editor = spPreferences.edit();

		Map<String, ?> keys = spPreferences.getAll();
		for (Map.Entry<String, ?> entry : keys.entrySet()) {
			String key = entry.getKey();
			if (key.indexOf(type + INDICATOR) >= 0)
				editor.remove(key);
		}

		editor.commit();
	}

	public void clearBlacklist() {
		clearList(BLACKLIST);
	}

	public String[] getList(String type) {
		int length = 0;
		Map<String, ?> keys = spPreferences.getAll();
		String prefix = type + INDICATOR;
		
		for (Map.Entry<String, ?> entry : keys.entrySet()) {
			String key = entry.getKey();
			if (key.indexOf(prefix) >= 0)
				length++;
		}

		if (length <= 0)
			return new String[0];

		String list[] = new String[length];
		int i = 0;
		for (Map.Entry<String, ?> entry : keys.entrySet()) {
			String key = entry.getKey();
			if (key.indexOf(prefix) >= 0) {
				int index = key.indexOf(INDICATOR.charAt(0));
				String number = key.substring(index + 1);
				list[i++] = number;
			}
		}

		return list;
	}

	public String[] getBlacklist() {
		return getList(BLACKLIST);
	}

	public String[] getWhitelist() {
		return getList(WHITELIST);
	}

	public void clearWhitelist() {
		clearList(WHITELIST);
	}

	private boolean isListEnable(String type) {

		String value = spPreferences.getString(type, ENTRY_DISABLE);

		if (value.equals(ENTRY_ENABLE))
			return true;
		else
			return false;
	}

	public boolean isBlacklistEnable() {
		return isListEnable(BLACKLIST);
	}

	public int setBlacklist(boolean enable) {
		Editor editor = spPreferences.edit();
		if (enable)
			editor.putString(BLACKLIST, ENTRY_ENABLE);
		else
			editor.putString(BLACKLIST, ENTRY_DISABLE);
		editor.commit();

		return 0;
	}

	public boolean isWhitelistEnable() {
		return isListEnable(WHITELIST);
	}

	public int setWhitelist(boolean enable) {
		Editor editor = spPreferences.edit();
		if (enable)
			editor.putString(WHITELIST, ENTRY_ENABLE);
		else
			editor.putString(WHITELIST, ENTRY_DISABLE);
		editor.commit();

		return 0;
	}

	private boolean isInList(String type, String phone) {
		String key = type + INDICATOR + phone;

		return spPreferences.contains(key);
	}

	public boolean isInBlacklist(String phone) {
		return isInList(BLACKLIST, phone);
	}

	public boolean isInWhitelist(String phone) {
		return isInList(WHITELIST, phone);
	}

	public int setEntrySate(String type, String entry, String stat) {
		String key = type + INDICATOR + entry;
		Editor editor = spPreferences.edit();

		editor.putString(key, stat);
		editor.commit();

		return 0;
	}

	public int setEntrySate(String type, String entry, long stat) {
		String key = type + INDICATOR + entry;
		Editor editor = spPreferences.edit();

		editor.putLong(key, stat);
		editor.commit();

		return 0;
	}

	public long getEntryState(String type, String entry) {
		return spPreferences.getLong(type + INDICATOR + entry, 0);
	}
}
