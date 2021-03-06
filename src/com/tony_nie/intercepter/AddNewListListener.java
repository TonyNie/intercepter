package com.tony_nie.intercepter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.support.v4.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;

public class AddNewListListener implements android.view.View.OnClickListener {

	private String mBaseName;
	private Fragment mFragment;
	private final String ADDNEW = "add new list";

	public AddNewListListener(android.support.v4.app.Fragment fragment,
			String baseName) {
		mFragment = fragment;
		mBaseName = baseName;
	}

	@SuppressLint("NewApi")
	private int addNewListFromContacts() {
		Intent pickContactIntent = new Intent(Intent.ACTION_PICK,
				ContactsContract.Contacts.CONTENT_URI);
		pickContactIntent
				.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
		mFragment.startActivityForResult(pickContactIntent,
				BLFragment.PICK_CONTACT_REQUEST);
		return 0;
	}

	private int addNewListFromCallRecords() {
		Intent intent = new Intent(mFragment.getActivity(),
				CallLogActivity.class);
		mFragment.startActivityForResult(intent,
				BLFragment.PICK_CALLLOG_REQUEST);

		return 0;
	}

	

	private int addNewListFromSMSRecords() {
		Intent intent = new Intent(mFragment.getActivity(),
				SMSLogActivity.class);
		mFragment.startActivityForResult(intent, BLFragment.PICK_SMSLOG_REQUEST);
		return 0;
	}

	private int addNewListManaul() {
		Intent intent = new Intent(mFragment.getActivity(),
				EditNumberActivity.class);
		mFragment.startActivityForResult(intent, BLFragment.PICK_MANAUL_REQUEST);
		return 0;
	}

	private int addNewList(int where) {

		switch (where) {
		case 0:
			addNewListFromContacts();
			break;
		case 1:
			addNewListFromCallRecords();
			break;
		case 2:
			addNewListFromSMSRecords();
			break;
		case 3:
			addNewListManaul();
			break;
		default:
			break;
		}

		return 0;
	}

	@SuppressLint("NewApi")
	@Override
	public void onClick(View v) {

		String[] source = mFragment.getActivity().getResources()
				.getStringArray(R.array.str_list_source);

		AlertDialog.Builder builder = new AlertDialog.Builder(
				mFragment.getActivity());
		builder.setTitle(mBaseName + " source");
		builder.setSingleChoiceItems(source, 0,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						Log.i(ADDNEW, "button down :choose " + which);
						addNewList(which);
						dialog.dismiss();

					}
				});
		builder.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						dialog.dismiss();
					}
				});
		builder.create();
		builder.show();
	}

}
