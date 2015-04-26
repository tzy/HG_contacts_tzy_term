package com.contacts.index;

import java.io.IOException;

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

public class HighLighter {

	private static Formatter formatter = null;
	private static final String STARTTAG = "<font color='red'>";
	private static final String ENDTAG = "</font>";
	private static final int SUBLENGTH = 25;

	Query mQuery;

	public HighLighter(Query query) {
		mQuery = query;
	}

	public String highlighterText(IndexReader reader, int docnum, String content) {

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

	public static String highlightString(String s) {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer(s);
		sb.insert(s.length(), ENDTAG);
		sb.insert(0, STARTTAG);
		return sb.toString();
	}
}
