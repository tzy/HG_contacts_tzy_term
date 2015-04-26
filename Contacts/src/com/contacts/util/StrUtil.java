package com.contacts.util;

public class StrUtil {
	public static String connectString(String str1, String str2){
		if(str1 == null && str2 != null) return str2;
		if(str1 != null && str2 == null) return str1;
		if(str1 != null && str2 != null) return str1 + " " + str2; 
		return null;
	}
	
	public static String getPinyin(String str){
		StringBuffer allPinyin = new StringBuffer();
		StringBuffer pinyin = new StringBuffer();
		String[] strings = str.split("[\u4E00-\u9FA5]+");
		for(String s : strings){
			if(s != ""){
				allPinyin.append(s);
				pinyin.append(s.charAt(0));
			}
		}
		return allPinyin.toString();
	}
	
}
