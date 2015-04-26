package com.contacts.contact;

import com.contacts.index.IndexConfig;

public class ContactForSearch {
	private String contactId;
	private String name;
	private String dataType;
	private String data;
	private boolean highlightName;
	
	public ContactForSearch(String id){
		contactId = id;
		name = null;
		dataType = null;
		data = null;
		highlightName = false;
	}
	
	public String getId(){
		return contactId;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public String getName(){
		return name;
	}
	
	public void setDate(String dataType, String data){
		this.dataType = dataType;
		this.data = data;
	}
	
	public void setHighlightName(Boolean highlight){
		highlightName = highlight;
	}
	
	public boolean isHighlightName(){
		return highlightName;
	}
	
	public String getInfo(){
		if(dataType == null) return null;
		if(dataType.equals(IndexConfig.TYPE_PHONE))  return  "���룺 " + data;
		if(dataType.equals(IndexConfig.TYPE_ADRESS))  return  "��ַ�� " + data;
		if(dataType.equals(IndexConfig.TYPE_EMAIL))  return  "���䣺 " + data;
		if(dataType.equals(IndexConfig.TYPE_NOTE))  return  "��ע�� " + data;
		if(dataType.equals(IndexConfig.TYPE_TAG))  return  "��ǩ�� " + data;
		
		return null;
	}
}
