package com.contacts.db;

import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import com.contacts.contact.Contact;
import com.contacts.db.DbHelper;
import com.contacts.index.IndexConfig;
import com.contacts.index.IndexOperateHelper;
import com.contacts.util.PinyinUtil;

public class InsertContactTask extends AsyncTask<Void, Void, Void> {
	private Context mContext;

	private Contact newContact;

	private DbHelper dbHelper;

	private SQLiteDatabase db;

	private IndexOperateHelper ioh;

	public InsertContactTask(Context context, Contact contact) {
		mContext = context;
		newContact = contact;
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
	}

	@Override
	protected Void doInBackground(Void... arg0) {
		// TODO Auto-generated method stub
		dbHelper = new DbHelper(mContext, DbHelper.DB_NAME);
		db = dbHelper.getWritableDatabase();

		ioh = new IndexOperateHelper(mContext.getApplicationContext()
				.getFilesDir().getAbsolutePath());
		try{
			ContentValues values = new ContentValues();
			String name = newContact.getName();
			if (name != null && !"".equals(name)) {
				values.clear();
				
				values.put(DbHelper.NAME, name);
				
				String fullPinyin = PinyinUtil.getFullPinYin(newContact.getName());
				values.put(DbHelper.SORT_KEY, fullPinyin);
				
				String address = newContact.getAddress();
				if (address != null && !"".equals(address)) {
					values.put(DbHelper.ADDRESS, address);
				}
				
				String note = newContact.getNote();
				if (note != null && !"".equals(note)) {
					values.put(DbHelper.NOTE, note);
				}
				
				long status = db.insert(DbHelper.CONTACT_TABLE, null, values);
				if (status != -1) {
					
					String contactId = String.valueOf(status);
					
					String firstPinyin = PinyinUtil.getFirstPinYin(name);
					ioh.addCommonDocument(contactId, name, name,
							IndexConfig.TYPE_NAME);
					ioh.addCommonDocument(contactId, name, fullPinyin,
							IndexConfig.TYPE_FULL_PINYIN);
					ioh.addCommonDocument(contactId, name, firstPinyin,
							IndexConfig.TYPE_FIRST_PINYIN);
					if (address != null && !"".equals(address)) {
						ioh.addCommonDocument(contactId, name, address,
								IndexConfig.TYPE_ADRESS);
					}
					if (note != null && !"".equals(note)) {
						ioh.addCommonDocument(contactId, name, note,
								IndexConfig.TYPE_NOTE);
					}
					
					
					List<String> phones = newContact.getPhones();
					if (phones != null) {
						Iterator<String> it = phones.iterator();
						while(it.hasNext()){
							String phoneNum = it.next();
							if (!"".equals(phoneNum)) {
								values.clear();
								values.put(DbHelper.CONTACT_ID, status);
								values.put(DbHelper.PHONE_NUM, phoneNum);
								db.insert(DbHelper.PHONE_TABLE, null, values);
								
								ioh.addCommonDocument(contactId, name, phoneNum,
										IndexConfig.TYPE_PHONE);
							}
						}
					}
					
					List<String> emails = newContact.getEmails();
					if (emails != null) {
						Iterator<String> it = emails.iterator();
						while(it.hasNext()){
							String email = it.next();
							if (!"".equals(email)) {
								values.clear();
								values.put(DbHelper.CONTACT_ID, status);
								values.put(DbHelper.EMAIL, email);
								db.insert(DbHelper.EMAIL_TABLE, null, values);
								
								ioh.addCommonDocument(contactId, name, email,
										IndexConfig.TYPE_EMAIL);
							}
						}
					}
				}
			}
		}finally{
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
		mContext.getContentResolver().notifyChange(DbHelper.URI_CONTACT_TABLE,
				null);
		((Activity) mContext).finish();
	}
	
	@Override
	protected void onCancelled() {
		// TODO Auto-generated method stub
		super.onCancelled();
		if (db != null && db.isOpen()){
			db.close();
			dbHelper.close();
		}
		if (ioh != null)
			ioh.close();
	}
}
