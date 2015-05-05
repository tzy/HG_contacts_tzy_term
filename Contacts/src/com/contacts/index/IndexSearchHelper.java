package com.contacts.index;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import com.contacts.util.StrUtil;

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

	public ScoreDoc[] query(String queryString) {
		ScoreDoc[] scoreDocs = null;
		try {
			mQuery = this.queryGenerater(queryString);
			TopDocs topdocs = indexSearcher.search(mQuery, 100);
			scoreDocs = topdocs.scoreDocs;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return scoreDocs;
	}

	private Query queryGenerater(String queryString) {
		Query query = null;
		if (StrUtil.containChinese(queryString)) {
			try {
				QueryParser queryParser = new QueryParser(IndexConfig.VERSION,
						IndexConfig.CONTENT_FILED, analyzer);
				query = queryParser.parse(queryString);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			query = new PrefixQuery(new Term(IndexConfig.CONTENT_FILED,
					queryString.toLowerCase(Locale.ENGLISH)));
		}

		return query;
	}

	public IndexReader getReader() {
		return reader;
	}

	public Query getQuery() {
		return mQuery;
	}

	public IndexSearcher getSearcher() {
		return indexSearcher;
	}

	public void close() throws IOException {
		if (indexSearcher != null)
			indexSearcher.close();
	}

}
