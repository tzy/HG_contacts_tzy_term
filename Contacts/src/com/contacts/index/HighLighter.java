package com.contacts.index;

import java.io.IOException;
import java.util.Locale;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.highlight.Formatter;
import org.apache.lucene.search.highlight.Fragmenter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.Scorer;
import org.apache.lucene.search.highlight.SimpleFragmenter;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.search.highlight.TokenSources;

import com.contacts.util.PinyinUtil;

public class HighLighter {

	private static Formatter formatter = null;
	private static final String STARTTAG = "<font color='red'>";
	private static final String ENDTAG = "</font>";
	private static final int SUBLENGTH = 25;

	String queryString;

	Query mQuery;

	public HighLighter(String queryString, Query query) {
		this.queryString = queryString.toLowerCase(Locale.ENGLISH);
		mQuery = query;
	}

	/*public String highlightByFullPinyin(String name, String fullPinyin) {
		String highlightName = null;
		if (queryString.contains(fullPinyin.toLowerCase(Locale.ENGLISH))) {
			highlightName = highlightString(name, name.length());
		} else {
			StringBuffer sb = new StringBuffer(name);
			int length = 0;
			for (int i = 0; i < name.length(); i++) {
				String subFullPinyin = PinyinUtil.getFullPinYin(sb.substring(i,
						i + 1));
				length += subFullPinyin.length();
				if (queryString.length() <= length) {
					highlightName = highlightString(name, i + 1);
					break;
				}
			}
		}
		return highlightName;
	}*/
	
	public String highlightByPinyin(String name, String Pinyin) {
		String highlightName = null;
		if (queryString.contains(Pinyin.toLowerCase(Locale.ENGLISH))) {
			highlightName = highlightString(name, name.length());
		} else {
			if(name.length() >= Pinyin.length()){
				highlightName = highlightString(name, queryString.length());
			}else{
				StringBuffer sb = new StringBuffer(name);
				int length = 0;
				for (int i = 0; i < name.length(); i++) {
					String subFullPinyin = PinyinUtil.getFullPinYin(sb.substring(i,
							i + 1));
					length += subFullPinyin.length();
					if (queryString.length() <= length) {
						highlightName = highlightString(name, i + 1);
						break;
					}
				}
			}
		}
		return highlightName;
	}
	
	public String highlightPrefix(String content){
		String highlightInfo = null;
		if (queryString.contains(content.toLowerCase(Locale.ENGLISH))) {
			highlightInfo = highlightString(content, content.length());
		} else {
			highlightInfo = highlightString(content, queryString.length());
		}
		return highlightInfo;
	}

	private String highlightString(String name, int length) {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer(name);
		sb.insert(length, ENDTAG);
		sb.insert(0, STARTTAG);
		return sb.toString();
	}

	public String defaultHighlight(IndexReader reader, int docnum, String content) {

		formatter = new SimpleHTMLFormatter(STARTTAG, ENDTAG);
		Scorer score = new QueryScorer(mQuery);
		Fragmenter fragmenter = new SimpleFragmenter(SUBLENGTH);
		Highlighter highlighter = new Highlighter(formatter, score);
		highlighter.setTextFragmenter(fragmenter);

		String result = null;
		try {
			TokenStream tokenStream = TokenSources.getAnyTokenStream(reader,
					docnum, IndexConfig.CONTENT_FILED, null);
			result = highlighter.getBestFragment(tokenStream, content);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidTokenOffsetsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

}
