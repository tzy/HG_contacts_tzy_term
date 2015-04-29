package com.contacts.activity;

import android.app.ActionBar;
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
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.contacts.db.DbHelper;
import com.contacts.db.SqliteQueryLoader;

public class ContactLookUpActivity extends FragmentActivity implements
		LoaderManager.LoaderCallbacks<Cursor> {

	private static final int PHONE_LOADER_ID = 0;
	private static final int EMAIL_LOADER_ID = 1;
	private static final int ADDRESS_NOTE_LOADER_ID = 2;

	String contactId;
	String name;
	long photoId;

	TextView nameTextView;
	TextView addressTextView;
	TextView noteTextView;

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

		phones = (ListView) findViewById(R.id.lv_phonenumber);
		phonesAdapter = new SimpleCursorAdapter(this, R.layout.item_phone,
				null,
				new String[] { DbHelper.PHONE_NUM },
				new int[] { R.id.tv_phonenumber }, 0);
		phones.setAdapter(phonesAdapter);

		emails = (ListView) findViewById(R.id.lv_emails);
		emailsAdapter = new SimpleCursorAdapter(this, R.layout.item_email,
				null,
				new String[] { DbHelper.EMAIL },
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
		name = bundle.getString(DbHelper.NAME);

		dbHelper = new DbHelper(this, DbHelper.DB_NAME);
		db = dbHelper.getReadableDatabase();
		
		getSupportLoaderManager().initLoader(PHONE_LOADER_ID, null, this);
		getSupportLoaderManager().initLoader(EMAIL_LOADER_ID, null, this);
		getSupportLoaderManager().initLoader(ADDRESS_NOTE_LOADER_ID, null, this);

		nameTextView.setText(name);
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
		}
		return true;
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle arg1) {
		// TODO Auto-generated method stub
		SqliteQueryLoader loader = null;
		String selection = "_id = ?";
		String[] selectionArgs = new String[]{contactId};
		switch (id) {
		case PHONE_LOADER_ID:
			String[] phoneProjection = new String[] {DbHelper.CONTACT_ID, DbHelper.PHONE_NUM};
			loader = new SqliteQueryLoader(this, db, DbHelper.PHONE_TABLE, phoneProjection, selection, selectionArgs, null);
			break;
		case EMAIL_LOADER_ID:
			String[] emailProjection = new String[] {DbHelper.CONTACT_ID, DbHelper.EMAIL};
			loader = new SqliteQueryLoader(this, db, DbHelper.EMAIL_TABLE, emailProjection, selection, selectionArgs, null);
			break;
		case ADDRESS_NOTE_LOADER_ID:
			String[] otherProjection = new String[] {DbHelper.ADDRESS, DbHelper.NOTE};
			loader = new SqliteQueryLoader(this, db, DbHelper.CONTACT_TABLE, otherProjection, selection, selectionArgs, null);
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
				addressTextView.setText(cursor.getString(0));
				noteTextView.setText(cursor.getString(1));
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
}
