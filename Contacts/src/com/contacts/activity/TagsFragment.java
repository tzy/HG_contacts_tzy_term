package com.contacts.activity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.contacts.db.DbHelper;
import com.contacts.db.SqliteQueryLoader;

public class TagsFragment extends Fragment implements
		LoaderManager.LoaderCallbacks<Cursor> {

	View rootView = null;

	ListView TagsListView;

	SimpleCursorAdapter tagsAdapter;
	
	DbHelper dbHelper = null;
	SQLiteDatabase db = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		dbHelper = new DbHelper(getActivity(), DbHelper.DB_NAME);
		db = dbHelper.getReadableDatabase();
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (rootView == null) {
			rootView = inflater.inflate(R.layout.fragment_tags, null);
			TagsListView = (ListView) rootView.findViewById(R.id.lv_tags);
		}
		ViewGroup parent = (ViewGroup) rootView.getParent();
		if (parent != null) {
			parent.removeView(rootView);
		}
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

		tagsAdapter = new SimpleCursorAdapter(getActivity(),
				android.R.layout.simple_list_item_1, null,
				new String[] { DbHelper.TAG },
				new int[] { android.R.id.text1 }, 0);
		TagsListView.setAdapter(tagsAdapter);

		TagsListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				int id = (int)arg3;

			}
		});

		getLoaderManager().initLoader(0, null, this);
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		db.close();
		dbHelper.close();
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		// TODO Auto-generated method stub
		String[] projection = new String[] { DbHelper.TAG_ID + "  as _id",
				DbHelper.TAG };
		return new SqliteQueryLoader(getActivity(), db, DbHelper.TAG_TABLE,
				DbHelper.URI_TAG_TABLE, projection, null,
				null, null);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		// TODO Auto-generated method stub
		tagsAdapter.swapCursor(data);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		// TODO Auto-generated method stub
		tagsAdapter.swapCursor(null);
	}

}
