package com.contacts.activity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.contacts.contact.Contact;
import com.contacts.contact.ContactSelectAdapter;
import com.contacts.contact.ContactSelectAdapter.ViewHolder;
import com.contacts.db.DbHelper;
import com.contacts.db.SqliteQueryLoader;
import com.contacts.index.IndexOperateHelper;

public class NewTagActivity extends FragmentActivity implements
		LoaderManager.LoaderCallbacks<Cursor> {

	EditText tagNameEdit;
	Button submitButton;

	private List<Contact> contacts;
	private ContactSelectAdapter adapter;
	ListView selectors;

	private List<Contact> selectedContacts;

	/**
	 * 数据库
	 */
	DbHelper dbHelper = null;
	SQLiteDatabase db = null;
	
	/**
	 * 索引
	 */
	IndexOperateHelper ioh = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_tag_layout);

		initUI();
		setListener();

		dbHelper = new DbHelper(this, DbHelper.DB_NAME);
		db = dbHelper.getWritableDatabase();
		
		ioh = new IndexOperateHelper(NewTagActivity.this.getApplicationContext()
				.getFilesDir().getAbsolutePath());
		
		getSupportLoaderManager().initLoader(0, null, this);
	}

	private void initUI() {
		// TODO Auto-generated method stub
		tagNameEdit = (EditText) findViewById(R.id.edt_tag_name);
		submitButton = (Button) findViewById(R.id.bt_new_tag);
		selectors = (ListView) findViewById(R.id.lv_selectors);
	}

	private void setListener() {
		// TODO Auto-generated method stub
		submitButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String tagName = tagNameEdit.getText().toString();
				selectedContacts = adapter.getSelectedContacts();
				if (tagName == null || tagName.equals("")) {
					Toast.makeText(NewTagActivity.this, "请输入标签名",
							Toast.LENGTH_SHORT).show();
				} else {
					if (selectedContacts.size() == 0) {
						Toast.makeText(NewTagActivity.this, "请选择联系人",
								Toast.LENGTH_SHORT).show();
					} else {
						NewTagTask task = new NewTagTask();
						task.execute(tagName);
					}
				}
			}
		});

		selectors.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view,
					int position, long arg3) {
				// TODO Auto-generated method stub
				ViewHolder vHollder = (ViewHolder) view.getTag();
				// 在每次获取点击的item时将对于的checkbox状态改变，同时修改map的值。
				vHollder.cBox.toggle();
				adapter.setSelected(position, vHollder.cBox.isChecked());
			}
		});
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		db.close();
		dbHelper.close();
		ioh.close();
	}

	private class NewTagTask extends AsyncTask<String, Void, Void> {

		@Override
		protected Void doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			String tagName = arg0[0];
			ContentValues values = new ContentValues();
			values.put(DbHelper.TAG, tagName);
			long status = db.insert(DbHelper.TAG_TABLE, null, values);
			if (status != -1) {
				String tagId = String.valueOf(status);
				
				Iterator<Contact> it = selectedContacts.iterator();
				while(it.hasNext()) {
					Contact contact = it.next();
					values.clear();
					values.put(DbHelper.CONTACT_ID, Integer.parseInt(contact.getId()));
					values.put(DbHelper.TAG_ID, status);
					db.insert(DbHelper.CONTACT_TAG_TABLE, null, values);
					
					ioh.addTagDocument(contact.getId(), contact.getName(), tagName, tagId);
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			Toast.makeText(NewTagActivity.this, "执行完毕", Toast.LENGTH_SHORT)
					.show();
			NewTagActivity.this.getContentResolver().notifyChange(
					DbHelper.URI_TAG_TABLE, null);
		}

	}

	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		// TODO Auto-generated method stub
		String[] projection = { DbHelper.CONTACT_ID, DbHelper.NAME };
		String sortOder = DbHelper.SORT_KEY + " COLLATE LOCALIZED asc";
		return new SqliteQueryLoader(this, db, DbHelper.CONTACT_TABLE,
				DbHelper.URI_CONTACT_TABLE, projection, null, null, sortOder);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor cursor) {
		// TODO Auto-generated method stub
		if (cursor != null && cursor.getCount() > 0) {
			contacts = new ArrayList<Contact>();
			if (cursor.moveToFirst()) {
				do {
					String contactId = cursor.getString(cursor
							.getColumnIndex(DbHelper.CONTACT_ID));
					String name = cursor.getString(cursor
							.getColumnIndex(DbHelper.NAME));
					Contact contact = new Contact(contactId);
					contact.setName(name);
					contacts.add(contact);
				} while (cursor.moveToNext());
			}
			adapter = new ContactSelectAdapter(this, contacts);
			selectors.setAdapter(adapter);
			selectors.setItemsCanFocus(false);
			selectors.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		}
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		// TODO Auto-generated method stub
		contacts = null;
	}

}
