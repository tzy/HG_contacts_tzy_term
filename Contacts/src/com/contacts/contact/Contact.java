package com.contacts.contact;

public class Contact {  
	/**
	 * ��ϵ��Id
	 */
	protected String contactId;
	  
    /** 
     * ��ϵ������ 
     */  
	protected String name;  
    
    /**
     * ��ϵ��photoId
     */
	protected long photoId;
  
    /** 
     * ������ĸ 
     */  
	protected String sortKey;
    
    public Contact(String id){
    	this.contactId = id;
    }
    
    public void setName(String name) {  
        this.name = name;
    }
  
    public void setPhotoId(long photoId) {  
        this.photoId = photoId;  
    } 
    
    public void setSortKey(String sortKey) {  
        this.sortKey = sortKey;  
    } 
  
    public String getSortKey() {  
        return sortKey;  
    }  
    
    public String getId() {  
        return contactId;  
    } 
    
    public long getPhotoId() {  
        return photoId;  
    }  
  
    public String getName() {  
        return name;  
    } 
  
     
  
}  
