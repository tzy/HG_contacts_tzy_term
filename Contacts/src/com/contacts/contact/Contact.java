package com.contacts.contact;

import java.util.List;

public class Contact {  
	/**
	 * 联系人Id
	 */
	private String contactId;
	  
    /** 
     * 联系人姓名 
     */  
	private String name;  
    
    /** 
     * 排序字母 
     */  
	private String sortKey;
	
	/**
	 * 联系人号码
	 */
	private List<String> phones;
	
	/**
	 * 联系人邮箱
	 */
	private List<String> emails;
	
	/**
	 * 联系人地址
	 */
	private String address;
	
	/**
	 * 联系人备注信息
	 */
	private String note;
	
	public Contact(){
	}
	
    public Contact(String id){
    	this.contactId = id;
    }
    
    public Contact setName(String name) {  
        this.name = name;
        return this;
    }
  
    public Contact setSortKey(String sortKey) {  
        this.sortKey = sortKey;
        return this;
    } 
  
    public String getSortKey() {  
        return sortKey;  
    }  
    
    public String getId() {  
        return contactId;  
    } 
    
    public String getName() {  
        return name;  
    } 
  
    public Contact setAddress(String address){
    	this.address = address;
    	return this;
    }
    
    public Contact setNote(String note){
    	this.note = note;
    	return this;
    }
    
    public Contact setPhones(List<String> phones){
    	this.phones = phones;
    	return this;
    }
    
    public Contact setEmails(List<String> emails){
    	this.emails = emails;
    	return this;
    }
    
    public String getAddress(){
    	return address;
    }
    
    public String getNote(){
    	return note;
    }
    
    public List<String> getPhones(){
    	return phones;
    }
    
    public List<String> getEmails(){
    	return emails;
    }
    
}  
