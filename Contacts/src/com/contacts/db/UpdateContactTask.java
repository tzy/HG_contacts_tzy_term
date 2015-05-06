package com.contacts.db;

import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import com.contacts.contact.Contact;
import com.contacts.index.IndexOperateHelper;
import com.contacts.util.PinyinUtil;

public class UpdateContactTask extends AsyncTask<Void, Void, Void> {

	private Context mContext;

	private Contact updateContact;

	private DbHelper dbHelper;

	private SQLiteDatabase db;

	private IndexOperateHelper ioh;

	public UpdateContactTask(Context context, Contact contact) {
		mContext = context;
		updateContact = contact;
	}

	@Override
	protected Void doInBackground(Void... arg0) {
		// TODO Auto-generated method stub
		dbHelper = new DbHelper(mContext, DbHelper.DB_NAME);
		db = dbHelper.getWritableDatabase();

		ioh = new IndexOperateHelper(mContext.getApplicationContext()
				.getFilesDir().getAbsolutePath());
		try {
			ContentValues values = new ContentValues();
			String contactId = updateContact.getId();
			String where = "_id = ?";
			String[] whereParm = { contactId };
			if (contactId != null && contactId != "-1") {
				//更新contacts表
				values.clear();
				String name = updateContact.getName();
				if (name != null) {
					values.put(DbHelper.NAME, name);
					String fullPinyin = PinyinUtil.getFullPinYin(updateContact
							.getName());
					values.put(DbHelper.SORT_KEY, fullPinyin);
				}

				String address = updateContact.getAddress();
				if (address != null) {
					values.put(DbHelper.ADDRESS, address);
				}

				String note = updateContact.getNote();
				if (note != null) {
					values.put(DbHelper.NOTE, note);
				}

				long status = db.update(DbHelper.CONTACT_TABLE, values, where,
						whereParm);
				if (status != -1) {
					//更新phones表
					List<String> phones = updateContact.getPhones();
					if (phones != null) {
						db.delete(DbHelper.PHONE_TABLE, where, whereParm);
						
						Iterator<String> it = phones.iterator();
						while(it.hasNext()){
							String phoneNum = it.next();
							if (!"".equals(phoneNum)) {
								values.clear();
								values.put(DbHelper.CONTACT_ID, Integer.parseInt(contactId));
								values.put(DbHelper.PHONE_NUM, phoneNum);
								db.insert(DbHelper.PHONE_TABLE, null, values);
							}
						}
					}

					//更新emails表
					List<String> emails = updateContact.getEmails();
					if (emails != null) {
						db.delete(DbHelper.EMAIL_TABLE, where, whereParm);

						Iterator<String> it = emails.iterator();
						while(it.hasNext()){
							String email = it.next();
							if (!"".equals(email)) {
								values.clear();
								values.put(DbHelper.CONTACT_ID, Integer.parseInt(contactId));
								values.put(DbHelper.EMAIL, email);
								db.insert(DbHelper.EMAIL_TABLE, null, values);
							}
						}
					}
				}
				ioh.updateIndex(updateContact);
			}
		} finally {
			if (db != null && db.isOpen())
				db.close();
			if (dbHelper != null)
				dbHelper.close();
			if (ioh != null)
				ioh.close();
		}
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		ContentResolver cr = mContext.getContentResolver();
		cr.notifyChange(DbHelper.URI_CONTACT_TABLE, null);
		cr.notifyChange(DbHelper.URI_PHONE_TABLE, null);
		cr.notifyChange(DbHelper.URI_EMAIL_TABLE, null);
		((Activity) mContext).finish();
	}

	@Override
	protected void onCancelled() {
		// TODO Auto-generated method stub
		super.onCancelled();
		if (db != null && db.isOpen()) {
			db.close();
			dbHelper.close();
		}
		if (ioh != null)
			ioh.close();
	}

}
