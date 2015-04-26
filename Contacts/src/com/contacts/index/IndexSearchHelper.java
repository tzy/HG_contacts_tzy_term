package com.contacts.index;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class IndexSearchHelper {
	Analyzer analyzer;
	IndexReader reader = null;
	Query mQuery;
	IndexSearcher indexSearcher = null;

	public IndexSearchHelper(String dir) {
		analyzer = AnalyzerFactory.getAnalyzer();

		try {
			Directory director = FSDirectory.open(new File(dir
					+ IndexConfig.INDEXDIR));
			reader = IndexReader.open(director);
			indexSearcher = new IndexSearcher(reader);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public ScoreDoc[] query(String queryString){
		QueryParser queryParser = new QueryParser(IndexConfig.VERSION,
				IndexConfig.CONTENT_FILED, analyzer);
		Query query;
		ScoreDoc[] scoreDocs = null;
		try {
			query = queryParser.parse(queryString + "*");
			this.mQuery = query;
			TopDocs topdocs = indexSearcher.search(query, 100);
			scoreDocs = topdocs.scoreDocs;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return scoreDocs;
	}

	public IndexReader getReader() {
		return reader;
	}
	
	public Query getQuery(){
		return mQuery;
	}
	
	public IndexSearcher getSearcher(){
		return indexSearcher;
	}

	public void close() throws IOException {
		if(indexSearcher != null) indexSearcher.close();
	}

}
