package com.contacts.contact;

public class Contact {  
	/**
	 * 联系人Id
	 */
	protected String contactId;
	  
    /** 
     * 联系人姓名 
     */  
	protected String name;  
    
    /**
     * 联系人photoId
     */
	protected long photoId;
  
    /** 
     * 排序字母 
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
