package com.contacts.util;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

import com.contacts.util.HanziToPinyin.Token;

public class PinyinUtil {

	 public static String getFullPinYin(String source){ 
		  
	      if (!Arrays.asList(Collator.getAvailableLocales()).contains(Locale.CHINA)) { 
	  
	          return source; 
	  
	      }  
	  
	      ArrayList<Token> tokens = HanziToPinyin.getInstance().get(source); 
	  
	      	if (tokens == null || tokens.size() == 0) { 
	  
	      		return source; 
	  
	      } 
	  
	      StringBuffer result = new StringBuffer(); 
	  
	      for (Token token : tokens) { 
	  
	    	  if (token.type == Token.PINYIN) { 
	  
	    		  result.append(token.target); 
	  
	    	  } else { 
	  
	    		  result.append(token.source); 
	  
	    	  } 
	  
	  
	      }
	      return result.toString();
	  
	  } 
	 
	  public static String getFirstPinYin(String source){ 
	  
	      if (!Arrays.asList(Collator.getAvailableLocales()).contains(Locale.CHINA)) { 
	  
	          return source; 
	  
	      }  
	  
	      ArrayList<Token> tokens = HanziToPinyin.getInstance().get(source); 
	  
	      if (tokens == null || tokens.size() == 0) { 
	  
	         return source; 
	  
	      } 
	  
	      StringBuffer result = new StringBuffer(); 
	  
	      for (Token token : tokens) { 
	  
	    	  if (token.type == Token.PINYIN) { 
	  
	    		  result.append(token.target.charAt(0)); 
	  
	    	  } else { 
	  
	    		  result.append("#"); 
	  
	    	  } 
	  
	      }
	      return result.toString(); 
	  
	  } 

	
}
