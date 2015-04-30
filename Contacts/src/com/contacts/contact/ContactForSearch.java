package com.contacts.contact;

import com.contacts.index.IndexConfig;
import com.contacts.util.StrUtil;

public class ContactForSearch {
	private String contactId;
	private String name;
	private String dataType;
	private String data;
	
	public ContactForSearch(String id){
		contactId = id;
		name = null;
		dataType = null;
		data = null;
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
	
	public String getDataType(){
		return dataType;
	}
	
	public String getData(){
		return data;
	}
	
	public void setDate(String dataType, String data){
		this.dataType = dataType;
		this.data = data;
	}
	
	public void addData(String newData){
		data = StrUtil.connectString(data, newData);
	}
	
	public String getInfo(){
		if(dataType == null) return null;
		if(dataType.equals(IndexConfig.TYPE_PHONE))  return  "∫≈¬Î£∫ " + data;
		if(dataType.equals(IndexConfig.TYPE_ADRESS))  return  "µÿ÷∑£∫ " + data;
		if(dataType.equals(IndexConfig.TYPE_EMAIL))  return  "” œ‰£∫ " + data;
		if(dataType.equals(IndexConfig.TYPE_NOTE))  return  "±∏◊¢£∫ " + data;
		if(dataType.equals(IndexConfig.TYPE_TAG))  return  "±Í«©£∫ " + data;
		
		return null;
	}
}
