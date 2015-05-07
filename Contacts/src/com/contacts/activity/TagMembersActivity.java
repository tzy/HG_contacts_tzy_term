package com.contacts.activity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.widget.ListView;

import com.contacts.db.DbHelper;
import com.contacts.db.SqliteRawQueryLoader;

public class TagMembersActivity extends FragmentActivity implements
		LoaderManager.LoaderCallbacks<Cursor> {

	ListView membersListView;

	SimpleCursorAdapter membersAdapter;

	String tagId;

	DbHelper dbHelper = null;
	SQLiteDatabase db = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tag_members_layout);

		membersListView = (ListView) findViewById(R.id.lv_tag_members);
		membersAdapter = new SimpleCursorAdapter(TagMembersActivity.this,
				android.R.layout.simple_list_item_1, null,
				new String[] { DbHelper.NAME },
				new int[] { android.R.id.text1 }, 0);
		membersListView.setAdapter(membersAdapter);

		dbHelper = new DbHelper(TagMembersActivity.this, DbHelper.DB_NAME);
		db = dbHelper.getReadableDatabase();

		Bundle bundle = this.getIntent().getExtras();
		tagId = bundle.getString(DbHelper.TAG_ID);

		getSupportLoaderManager().initLoader(0, null, this);
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(db != null && db.isOpen()){
			db.close();
			dbHelper.close();
		}
	}

	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		// TODO Auto-generated method stub
		String sql = "SELECT " + DbHelper.CONTACT_TABLE + "." + DbHelper.CONTACT_ID + "," + DbHelper.NAME
				+ " FROM " + DbHelper.CONTACT_TABLE + " INNER JOIN "
				+ DbHelper.CONTACT_TAG_TABLE + " ON " 
				+ DbHelper.CONTACT_TABLE + "." + DbHelper.CONTACT_ID + "  = "
				+ DbHelper.CONTACT_TAG_TABLE + "." + DbHelper.CONTACT_ID
				+ " WHERE " + DbHelper.TAG_ID + " = ?";
		String[] sqlArgs = { tagId };
		return new SqliteRawQueryLoader(TagMembersActivity.this, db, sql, sqlArgs);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor cursor) {
		// TODO Auto-generated method stub
		membersAdapter.swapCursor(cursor);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		// TODO Auto-generated method stub
		membersAdapter.swapCursor(null);
	}
}
