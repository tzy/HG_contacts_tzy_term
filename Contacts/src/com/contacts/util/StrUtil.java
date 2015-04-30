package com.contacts.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StrUtil {
	public static String connectString(String str1, String str2){
		if(str1 == null && str2 != null) return str2;
		if(str1 != null && str2 == null) return str1;
		if(str1 != null && str2 != null) return str1 + "  " + str2; 
		return null;
	}
	
	public static boolean containChinese(String str) {
		// TODO Auto-generated method stub
		Pattern p = Pattern.compile("[\u4e00-\u9fa5]+");
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
	}
	
}
