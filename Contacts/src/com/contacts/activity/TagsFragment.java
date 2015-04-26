package com.contacts.activity;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.Contacts;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class TagsFragment extends Fragment implements  
LoaderManager.LoaderCallbacks<Cursor> {
	static final String[] CONTACTS_SUMMARY_PROJECTION = new String[] {  
        Contacts._ID, Contacts.DISPLAY_NAME, Contacts.CONTACT_STATUS,  
        Contacts.LOOKUP_KEY, };  

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View messageLayout = inflater.inflate(R.layout.fragment_tags,
				container, false);
		return messageLayout;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		getLoaderManager().initLoader(0, null, this);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		// TODO Auto-generated method stub
		Uri baseUri;  
        baseUri = Contacts.CONTENT_URI;  
  
        // Now create and return a CursorLoader that will take care of  
        // creating a Cursor for the data being displayed.  
        String select = "((" + Contacts.DISPLAY_NAME + " NOTNULL) AND ("  
                + Contacts.HAS_PHONE_NUMBER + "=1) AND ("  
                + Contacts.DISPLAY_NAME + " != '' ))";  
        String order = Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC";  
  
        return new CursorLoader(getActivity(), baseUri,  
                CONTACTS_SUMMARY_PROJECTION, select, null, order);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		// TODO Auto-generated method stub
		TextView tv = (TextView)getView().findViewById(R.id.message);
		tv.setText("Œﬁ±Í«©");
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		// TODO Auto-generated method stub
		
	}

}
