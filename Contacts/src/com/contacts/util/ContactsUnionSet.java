package com.contacts.util;

import java.util.Collection;
import java.util.LinkedHashMap;

import com.contacts.contact.ContactForSearch;

public class ContactsUnionSet {
	private LinkedHashMap<Integer, ContactForSearch> contactUnioset;
	
	public ContactsUnionSet(){
		contactUnioset = new LinkedHashMap<Integer, ContactForSearch>();
	}
	
	public void addContact(ContactForSearch newContact){
		Integer id = Integer.parseInt(newContact.getId()); 
		if(!contactUnioset.containsKey(id)){
			contactUnioset.put(id, newContact);
		}else{
			ContactForSearch contact = contactUnioset.get(id);
			if(contact.getDataType() == null){
				contact.setDate(newContact.getDataType(), newContact.getData());
			}else{
				String newDataType = newContact.getDataType();
				if(newDataType != null && newDataType.equals(contact.getDataType())){
					contact.addData(newContact.getData());
				}
			}
		}
	}
	
	public Collection<ContactForSearch> getSet(){
		return contactUnioset.values();
	}

}
