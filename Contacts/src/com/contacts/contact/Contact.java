package com.contacts.contact;

import java.util.List;

public class Contact {  
	/**
	 * ��ϵ��Id
	 */
	private String contactId;
	  
    /** 
     * ��ϵ������ 
     */  
	private String name;  
    
    /** 
     * ������ĸ 
     */  
	private String sortKey;
	
	/**
	 * ��ϵ�˺���
	 */
	private List<String> phones;
	
	/**
	 * ��ϵ������
	 */
	private List<String> emails;
	
	/**
	 * ��ϵ�˵�ַ
	 */
	private String address;
	
	/**
	 * ��ϵ�˱�ע��Ϣ
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
