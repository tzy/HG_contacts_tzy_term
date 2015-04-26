package com.contacts.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper{
	/**
	 * 数据库名
	 */
	public static final String  DB_NAME = "Db_Contacts";
	
	/**
	 * 表名
	 */
	public static final String CONTACT_TABLE = "tb_contacts";
	public static final String PHONE_TABLE = "tb_phones";
	public static final String EMAIL_TABLE = "tb_emails";
	public static final String TAG_TABLE = "tb_tags";
	public static final String CONTACT_TAG_TABLE ="tb_contacts_tags";
	
	/**
	 * 列名
	 */
	public static final String CONTACT_ID = "_id";
	public static final String NAME = "name";
	public static final String SORT_KEY = "sort_key";
	public static final String ADDRESS = "address";
	public static final String NOTE = "note";
	public static final String PHONE_NUM = "phone_num";
	public static final String EMAIL = "email";
	public static final String TAG_ID = "tag_id";
	public static final String TAG = "tag";

	public DbHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}
	
	public DbHelper(Context context, String name, int version){
		this(context, name, null, version);
	}
	
	public DbHelper(Context context, String name){
		this(context, name, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		//打开外键约束
		db.execSQL("PRAGMA foreign_keys = ON;");
		
		//建立表格
		db.execSQL("create table if not exists " + CONTACT_TABLE + "(" + 
					CONTACT_ID + " integer primary key autoincrement,"+
				    NAME+ " varchar(20),"+
		            SORT_KEY + " varchar(30),"+
				    ADDRESS + " text,"+
		            NOTE+ " text)");
		db.execSQL("create table if not exists " + PHONE_TABLE + "(" + 
		            CONTACT_ID +  " integer references " + CONTACT_TABLE+  "(" + CONTACT_ID + ") not null,"+
				    PHONE_NUM + " varchar(20) not null,"+
		            "primary key(" + CONTACT_ID + "," + PHONE_NUM + "))");
		db.execSQL("create table if not exists " + EMAIL_TABLE + "(" + 
	            	CONTACT_ID +  " integer references " + CONTACT_TABLE+  "(" + CONTACT_ID + ") not null,"+
	            	EMAIL + " varchar(20) not null,"+
	            	"primary key(" + CONTACT_ID + "," + EMAIL + "))");
		db.execSQL("create table if not exists " + TAG_TABLE + "(" +
		            TAG_ID+ " integer primary key autoincrement,"+
				    TAG + " varchar(15) not null)");
		db.execSQL("create table if not exists " + CONTACT_TAG_TABLE + "(" +
					CONTACT_ID +  " integer references " + CONTACT_TABLE+  "(" + CONTACT_ID + ") not null,"+
				    TAG_ID + " integer references " + TAG_TABLE + "(" + TAG_ID + ") not null,"+
				   "primary key(" + CONTACT_ID + "," + TAG_ID + "))");
		
		//建立索引
		db.execSQL("CREATE INDEX index_contact_id ON " + CONTACT_TABLE + "(" + CONTACT_ID + ");");
		db.execSQL("CREATE INDEX index_phone_id ON " + PHONE_TABLE + "(" + CONTACT_ID + ");");
		db.execSQL("CREATE INDEX index_email_id ON " + EMAIL_TABLE + "(" + CONTACT_ID + ");");
		db.execSQL("CREATE INDEX index_tag_id ON " + TAG_TABLE + "(" + TAG_ID + ");");
		db.execSQL("CREATE INDEX index_tags_contact_id ON " + CONTACT_TAG_TABLE + "(" + CONTACT_ID + ");");
		db.execSQL("CREATE INDEX index_tags_flag_id ON " + CONTACT_TAG_TABLE + "(" + TAG_ID+ ");");
		
		//建立触发器
		db.execSQL("CREATE  TRIGGER tri_delete_contact before DELETE ON " + CONTACT_TABLE +
					" for each row "+
					"begin "+
						 "delete from " + PHONE_TABLE + " where " +  CONTACT_ID + "=OLD._id;" +
						 "delete from " + EMAIL_TABLE + " where " +  CONTACT_ID + "=OLD._id;" +
						 "delete from " + CONTACT_TAG_TABLE + " where " +  CONTACT_ID + "=OLD._id;" +
					"end ;");
		db.execSQL("CREATE  TRIGGER tri_delete_flag before DELETE ON " + TAG_TABLE +
					" for each row "+
					"begin "+
					     "delete from " + CONTACT_TAG_TABLE + " where " + TAG_ID + "=OLD._id;"+
					"end ;");
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}
	
}
