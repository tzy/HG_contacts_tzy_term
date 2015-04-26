package com.contacts.activity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.contacts.db.DbHelper;
import com.contacts.index.IndexConfig;
import com.contacts.index.IndexOperateHelper;
import com.contacts.util.PinyinUtil;

public class TestActivity extends Activity {
	EditText nameEdit;
	EditText phoneEdit1;
	EditText phoneEdit2;
	EditText emailEdit1;
	EditText emailEdit2;
	EditText addressEdit;
	EditText noteEdit;
	Button submitButton;
	Button searchButton;
	TextView mTextView;

	DbHelper dbHelper = null;
	SQLiteDatabase db = null;
	IndexOperateHelper ioh = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_layout);

		nameEdit = (EditText) findViewById(R.id.edt_name);
		phoneEdit1 = (EditText) findViewById(R.id.edt_phone1);
		phoneEdit2 = (EditText) findViewById(R.id.edt_phone2);
		emailEdit1 = (EditText) findViewById(R.id.edt_email1);
		emailEdit2 = (EditText) findViewById(R.id.edt_email2);
		addressEdit = (EditText) findViewById(R.id.edt_address);
		noteEdit = (EditText) findViewById(R.id.edt_note);
		mTextView = (TextView) findViewById(R.id.tv_result);
		submitButton = (Button) findViewById(R.id.bt_submit);
		searchButton = (Button) findViewById(R.id.bt_go_search);

		dbHelper = new DbHelper(TestActivity.this, "Db_Contacts");
		db = dbHelper.getWritableDatabase();

		ioh = new IndexOperateHelper(TestActivity.this.getApplicationContext()
				.getFilesDir().getAbsolutePath());

		submitButton.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				InsertTask insertTask = new InsertTask();  
		        insertTask.execute();  
			}

		});
		
		searchButton.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(TestActivity.this, TestSearchActivity.class);
				startActivity(intent);
			}
			
		});
	}

	private class InsertTask extends AsyncTask<Void, Void, Void> {

		private String name;
		private String address;
		private String note;;

		private String phone1;
		private String phone2;

		private String email1;
		private String email2;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

			name = nameEdit.getText().toString();
			address = addressEdit.getText().toString();
			note = noteEdit.getText().toString();

			phone1 = phoneEdit1.getText().toString();
			phone2 = phoneEdit2.getText().toString();

			email1 = emailEdit1.getText().toString();
			email2 = emailEdit2.getText().toString();
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			String fullPinyin = PinyinUtil.getFullPinYin(name);
			ContentValues values = new ContentValues();
			values.put("name", name);
			values.put("sort_key", fullPinyin);
			values.put("address", address);
			values.put("note", note);
			long status = db.insert("tb_contacts", null, values);

			if (status != -1) {
				String contactId = String.valueOf(status);
				String firstPinyin = PinyinUtil.getFirstPinYin(name);

				ioh.addCommonDocument(contactId, name, IndexConfig.TYPE_NAME);
				ioh.addCommonDocument(contactId, fullPinyin,
						IndexConfig.TYPE_FULL_PINYIN);
				ioh.addCommonDocument(contactId, firstPinyin,
						IndexConfig.TYPE_FIRST_PINYIN);
				ioh.addCommonDocument(contactId, address,
						IndexConfig.TYPE_ADRESS);
				ioh.addCommonDocument(contactId, note, IndexConfig.TYPE_NOTE);

				if (phone1 != null && !phone1.equals("")) {
					values.clear();
					values.put("_id", status);
					values.put("phone_num", phone1);
					db.insert("tb_phones", null, values);

					ioh.addCommonDocument(contactId, phone1,
							IndexConfig.TYPE_PHONE);
				}

				if (phone2 != null && !phone2.equals("")) {
					values.clear();
					values.put("_id", status);
					values.put("phone_num", phone2);
					db.insert("tb_phones", null, values);

					ioh.addCommonDocument(contactId, phone2,
							IndexConfig.TYPE_PHONE);
				}

				if (email1 != null && !email1.equals("")) {
					values.clear();
					values.put("_id", status);
					values.put("email", email1);
					db.insert("tb_emails", null, values);
					ioh.addCommonDocument(contactId, email1,
							IndexConfig.TYPE_EMAIL);
				}

				if (email2 != null && !email2.equals("")) {
					values.clear();
					values.put("_id", status);
					values.put("email", email2);
					db.insert("tb_emails", null, values);
					ioh.addCommonDocument(contactId, email2,
							IndexConfig.TYPE_EMAIL);
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			mTextView.setText("保存成功");
			Toast.makeText(TestActivity.this, "执行完毕", Toast.LENGTH_SHORT).show();
		}

	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(db != null) db.close();
		if(dbHelper != null) dbHelper.close();
		if(ioh != null) ioh.close();
	}

}
