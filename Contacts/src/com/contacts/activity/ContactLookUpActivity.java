package com.contacts.activity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.contacts.db.DbHelper;
import com.contacts.db.SqliteQueryLoader;
import com.contacts.index.IndexOperateHelper;

public class ContactLookUpActivity extends FragmentActivity implements
		LoaderManager.LoaderCallbacks<Cursor> {

	private static final int PHONE_LOADER_ID = 0;
	private static final int EMAIL_LOADER_ID = 1;
	private static final int ADDRESS_NOTE_LOADER_ID = 2;

	String contactId;
	String name;

	TextView nameTextView;
	TextView addressTextView;
	TextView noteTextView;

	Button createQCodeButton;
	Button deleteButton;

	ListView phones;
	ListView emails;
	private SimpleCursorAdapter phonesAdapter;
	private SimpleCursorAdapter emailsAdapter;

	DbHelper dbHelper = null;
	SQLiteDatabase db = null;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact_lookup_layout);

		nameTextView = (TextView) findViewById(R.id.tv_username);
		addressTextView = (TextView) findViewById(R.id.tv_address);
		noteTextView = (TextView) findViewById(R.id.tv_note);

		createQCodeButton = (Button) findViewById(R.id.bt_create_qcode);
		createQCodeButton.setOnClickListener(new MyButtonListener());
		deleteButton = (Button) findViewById(R.id.bt_delete_contact);
		deleteButton.setOnClickListener(new MyButtonListener());

		phones = (ListView) findViewById(R.id.lv_phonenumber);
		phonesAdapter = new SimpleCursorAdapter(this, R.layout.item_phone,
				null, new String[] { DbHelper.PHONE_NUM },
				new int[] { R.id.tv_phonenumber }, 0);
		phones.setAdapter(phonesAdapter);

		emails = (ListView) findViewById(R.id.lv_emails);
		emailsAdapter = new SimpleCursorAdapter(this, R.layout.item_email,
				null, new String[] { DbHelper.EMAIL },
				new int[] { R.id.tv_email }, 0);
		emails.setAdapter(emailsAdapter);

		ActionBar ab = getActionBar();
		ab.setDisplayHomeAsUpEnabled(true);
		ab.setHomeButtonEnabled(true);
		Drawable colorDrawable = new ColorDrawable(Color.parseColor("#FF0099CC"
				.toString()));
		ab.setBackgroundDrawable(colorDrawable);

		Bundle bundle = this.getIntent().getExtras();
		contactId = bundle.getString(DbHelper.CONTACT_ID);

		dbHelper = new DbHelper(this, DbHelper.DB_NAME);
		db = dbHelper.getWritableDatabase();

		getSupportLoaderManager().initLoader(PHONE_LOADER_ID, null, this);
		getSupportLoaderManager().initLoader(EMAIL_LOADER_ID, null, this);
		getSupportLoaderManager()
				.initLoader(ADDRESS_NOTE_LOADER_ID, null, this);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (db != null)
			db.close();
		if (dbHelper != null)
			dbHelper.close();
	}

	public void fixListViewHeight(ListView listView) {

		// 如果没有设置数据适配器，则ListView没有子项，返回。
		ListAdapter listAdapter = listView.getAdapter();
		int totalHeight = 0;
		if (listAdapter == null) {
			return;
		} else {
			for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
				View listViewItem = listAdapter.getView(i, null, listView);
				// 计算子项View 的宽高
				listViewItem.measure(0, 0);
				// 计算所有子项的高度和
				totalHeight += listViewItem.getMeasuredHeight();
			}
			ViewGroup.LayoutParams params = listView.getLayoutParams();
			// listView.getDividerHeight()获取子项间分隔符的高度
			// params.height设置ListView完全显示需要的高度
			params.height = totalHeight
					+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
			listView.setLayoutParams(params);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.contact_menu, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			overridePendingTransition(R.anim.push_right_in,
					R.anim.push_right_out);
			return true;
		case R.id.alter_icon:
			Intent intent = new Intent();
			intent.setClass(ContactLookUpActivity.this, AddContact.class);
			Bundle bundle = new Bundle();
			bundle.putString(DbHelper.CONTACT_ID, contactId);
			bundle.putString(DbHelper.NAME, name);
			bundle.putString(DbHelper.ADDRESS, addressTextView.getText()
					.toString());
			bundle.putString(DbHelper.NOTE, noteTextView.getText().toString());
			intent.putExtras(bundle);
			intent.putStringArrayListExtra(DbHelper.PHONE_NUM,
					getDataFromCursor(phonesAdapter.getCursor()));
			intent.putStringArrayListExtra(DbHelper.EMAIL,
					getDataFromCursor(emailsAdapter.getCursor()));
			startActivity(intent);
			break;
		}
		return true;
	}

	private class MyButtonListener implements View.OnClickListener {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			switch (arg0.getId()) {

			case R.id.bt_delete_contact:
				new AlertDialog.Builder(ContactLookUpActivity.this)
						.setTitle("删除联系人")
						.setMessage("是否删除联系人")
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface arg0,
											int arg1) {
										// TODO Auto-generated method stub
										deleteContact();
										finish();
									}
								})
						.setNegativeButton("取消",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {// 响应事件
										// TODO Auto-generated method stub

									}
								}).show();// 在按键响应事件中显示此对话框
				break;

			case R.id.bt_create_qcode:
				String mName = name;
				String address = addressTextView.getText().toString();
				String note = noteTextView.getText().toString();
				List<String> phones = getDataFromCursor(phonesAdapter.getCursor());
				List<String> emails = getDataFromCursor(emailsAdapter.getCursor());
				
				String contentString = mName + "|" + address + "|" + note + "|";
				Iterator<String> phoneIterator = phones.iterator();
				while(phoneIterator.hasNext()) {
					
					contentString = contentString + phoneIterator.next()+" ";
					
				}
				
				contentString = contentString+"|";
				
				Iterator<String> emailsIterator = emails.iterator();
				while(emailsIterator.hasNext()) {
					
					contentString = contentString + emailsIterator.next()+" ";
					
				}
				Bundle codeString = new Bundle();
				codeString.putString("String_for_pic", contentString);
				Intent intent = new Intent(ContactLookUpActivity.this,GenerateQRCode.class);
				intent.putExtras(codeString);
				startActivity(intent);
				break;
			}
		}
	}

	private void deleteContact() {
		db.delete(DbHelper.CONTACT_TABLE, DbHelper.CONTACT_ID + "=?",
				new String[] { contactId });
		IndexOperateHelper ioh = new IndexOperateHelper(ContactLookUpActivity.this
				.getApplicationContext().getFilesDir().getAbsolutePath());
		ioh.deleteContact(contactId);
		ioh.close();
		ContactLookUpActivity.this.getContentResolver().notifyChange(
				DbHelper.URI_CONTACT_TABLE, null);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle arg1) {
		// TODO Auto-generated method stub
		SqliteQueryLoader loader = null;
		String selection = "_id = ?";
		String[] selectionArgs = new String[] { contactId };
		switch (id) {
		case PHONE_LOADER_ID:
			String[] phoneProjection = new String[] { DbHelper.CONTACT_ID,
					DbHelper.PHONE_NUM };
			loader = new SqliteQueryLoader(this, db, DbHelper.PHONE_TABLE,
					DbHelper.URI_PHONE_TABLE, phoneProjection, selection,
					selectionArgs, null);
			break;
		case EMAIL_LOADER_ID:
			String[] emailProjection = new String[] { DbHelper.CONTACT_ID,
					DbHelper.EMAIL };
			loader = new SqliteQueryLoader(this, db, DbHelper.EMAIL_TABLE,
					DbHelper.URI_EMAIL_TABLE, emailProjection, selection,
					selectionArgs, null);
			break;
		case ADDRESS_NOTE_LOADER_ID:
			String[] otherProjection = new String[] {DbHelper.NAME, DbHelper.ADDRESS,
					DbHelper.NOTE };
			loader = new SqliteQueryLoader(this, db, DbHelper.CONTACT_TABLE,
					DbHelper.URI_CONTACT_TABLE, otherProjection, selection,
					selectionArgs, null);
			break;
		}
		return loader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		// TODO Auto-generated method stub
		int id = loader.getId();
		switch (id) {
		case PHONE_LOADER_ID:
			if (cursor.getCount() > 0) {
				phonesAdapter.swapCursor(cursor);
				fixListViewHeight(phones);
			}
			break;
		case EMAIL_LOADER_ID:
			if (cursor.getCount() > 0) {
				emailsAdapter.swapCursor(cursor);
				fixListViewHeight(emails);
			}
			break;
		case ADDRESS_NOTE_LOADER_ID:
			if (cursor.moveToFirst()) {
				name = cursor.getString(cursor.getColumnIndex(DbHelper.NAME));
				nameTextView.setText(name);
				addressTextView.setText(cursor.getString(cursor.getColumnIndex(DbHelper.ADDRESS)));
				noteTextView.setText(cursor.getString(cursor.getColumnIndex(DbHelper.NOTE)));
			}
			break;
		}

	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		// TODO Auto-generated method stub
		int id = loader.getId();
		switch (id) {
		case PHONE_LOADER_ID:
			phonesAdapter.swapCursor(null);
			break;
		case EMAIL_LOADER_ID:
			phonesAdapter.swapCursor(null);
			break;
		case ADDRESS_NOTE_LOADER_ID:
			break;
		}

	}

	private ArrayList<String> getDataFromCursor(Cursor cursor) {
		ArrayList<String> datas = new ArrayList<String>();
		if (cursor != null && cursor.getCount() > 0) {
			if (cursor.moveToFirst()) {
				do {
					String data = cursor.getString(1);
					datas.add(data);
				} while (cursor.moveToNext());
			}
		}
		return datas;
	}
}
