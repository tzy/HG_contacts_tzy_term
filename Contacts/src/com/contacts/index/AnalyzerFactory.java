package com.contacts.index;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;

public class AnalyzerFactory {
	
	public static Analyzer getAnalyzer(){
		return new StandardAnalyzer(IndexConfig.VERSION);
		
	}

}
