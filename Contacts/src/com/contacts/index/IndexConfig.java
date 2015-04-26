package com.contacts.index;

import org.apache.lucene.util.Version;

public class IndexConfig {
	
	public static final String INDEXDIR = "/contactsIndex";// Ë÷ÒýÄ¿Â¼
	public static final Version VERSION = Version.LUCENE_36;// lucene°æ±¾
	
	public static final String ID_FILED = "contactId";
	public static final String CONTENT_FILED = "content";
	public static final String TYPE_FILED = "type";
	public static final String TAG_ID_FILED = "tagId";
	
	public static final String TYPE_NAME = "0";
	public static final String TYPE_FULL_PINYIN = "1";
	public static final String TYPE_FIRST_PINYIN = "2";
	public static final String TYPE_ADRESS = "3";
	public static final String TYPE_NOTE = "4";
	public static final String TYPE_PHONE = "5";
	public static final String TYPE_EMAIL = "6";
	public static final String TYPE_TAG = "7";

}
