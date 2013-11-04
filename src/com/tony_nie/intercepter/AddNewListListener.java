package com.tony_nie.intercepter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;

public class AddNewListListener implements android.view.View.OnClickListener {

	private String mBaseName;
	private Context mContext;
	private final String ADDNEW = "add new list";

	public AddNewListListener(Context context, String baseName) {

		mBaseName = baseName;
		mContext = context;
	}

	private int addNewListFromContacts() {
		return 0;
	}

	private int addNewListFromCallRecords() {
		return 0;
	}

	private int addNewListFromSMSRecords() {
		return 0;
	}

	private int addNewListManaul() {
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

	@Override
	public void onClick(View v) {

		String[] source = mContext.getResources().getStringArray(
				R.array.str_list_source);

		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
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
