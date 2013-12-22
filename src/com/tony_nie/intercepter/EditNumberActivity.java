package com.tony_nie.intercepter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditNumberActivity extends Activity {
	final static String MANAUL_NUMBER = "number";
	final static String MANAUL_COMMENT = "comment";
	private Button mOkButton;
	private EditText mNumberEditText;
	private EditText mCommentEditText;
	
	public EditNumberActivity() {
		// TODO Auto-generated constructor stub
	}
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_edit_number);
		mOkButton = (Button) findViewById(R.id.btn_ok);
		mNumberEditText = (EditText) findViewById(R.id.edittext_number);
		mCommentEditText = (EditText) findViewById(R.id.edittext_comment);
		
		mOkButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				String number = mNumberEditText.getText().toString();
				String comment = mCommentEditText.getText().toString();
				
				bundle.putString(MANAUL_NUMBER, number);
				bundle.putString(MANAUL_COMMENT, comment);		
				intent.putExtras(bundle);
				EditNumberActivity.this.setResult(RESULT_OK, intent);
				EditNumberActivity.this.finish();
			}
		});
	}

}
